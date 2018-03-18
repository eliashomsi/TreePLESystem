package ca.mcgill.ecse321.treeple.service;

import java.security.SecureRandom;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.treeple.model.EnvironmentalScientist;
import ca.mcgill.ecse321.treeple.model.Location;
import ca.mcgill.ecse321.treeple.model.MunicipalArborist;
import ca.mcgill.ecse321.treeple.model.Municipality;
import ca.mcgill.ecse321.treeple.model.Resident;
import ca.mcgill.ecse321.treeple.model.Transaction;
import ca.mcgill.ecse321.treeple.model.Tree;
import ca.mcgill.ecse321.treeple.model.Tree.TreeSpecies;
import ca.mcgill.ecse321.treeple.model.Tree.TreeStatus;
import ca.mcgill.ecse321.treeple.model.TreePLESystem;
import ca.mcgill.ecse321.treeple.persistence.PersistenceXStream;

@Service
public class TreePLEService {
	private TreePLESystem rm;
	private ArrayList<Token> tokens;

	/** Constructor **/
	public TreePLEService(TreePLESystem rm) {
		this.rm = rm;
		tokens = new ArrayList<Token>();
	}

	/** Helper Methods **/
	private Token genToken(String email) {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[20];
		random.nextBytes(bytes);
		String token = bytes.toString();
		Token t = new Token(token, email, getCurrentUnixTime());
		return t;
	}

	private long getCurrentUnixTime() {
		long unixTime = System.currentTimeMillis() / 1000L;
		return unixTime;
	}

	private boolean checkIfNullOrEmptyString(String s) {
		return s == null || s.trim().contentEquals("");
	}
	
	private boolean checkIfLocationValid(double lon, double lat) {
		boolean isValid = true;
		if(lon < -180.000000 || lon > 180.000000 ) {
			isValid = false;
		}
		if(lat < -90.000000 || lat > 90.000000) {
			isValid = false;
		}
		return isValid;
	}

	private String hashPassword(String password_plaintext, String salt) {
		String hashed_password = BCrypt.hashpw(password_plaintext, salt);
		return (hashed_password);
	}

	private boolean checkPassword(String password_plaintext, String salt, String hashedPassword) {
		String hash = hashPassword(password_plaintext, salt);
		return hashedPassword.contentEquals(hash);
	}
	
	private boolean isValidEmailAddress(String email) {
		if (!email.contains("@")) {
			return false;
		}
		String[] emailParts = email.split("@");
		
		if (emailParts.length != 2 || checkIfNullOrEmptyString(emailParts[0]) || checkIfNullOrEmptyString(emailParts[1])) {
			return false;
		}
		else return true;
	}

	/** Business Logic **/
	public Tree CreateTree(TreeSpecies species, TreeStatus status, int diameter, double lon, double lat, Municipality m)
			throws InvalidInputException {
		if (species == null || status == null || diameter == 0 || lon == 0.0d || lat == 0.0d || m == null) {
			throw new InvalidInputException("You did not provide necessary information for the tree");
		}
			
		if(diameter < 5) {
			throw new InvalidInputException("Trees' diameter should be above 5cm.");
		}
		
		for (int i = 0; i < rm.getLocations().size(); i++) {
			double lonTemp = rm.getLocation(i).getLongitude();
			double latTemp = rm.getLocation(i).getLatitude();
			if(lonTemp == lon && latTemp == lat) {
				throw new InvalidInputException("This location is currently occupied");
			}
		}

		Location l = new Location(lon, lat);
		Tree t = new Tree(diameter, l, m);
		t.setSpecies(species);
		t.setStatus(status);
		rm.addTree(t);
		rm.addLocation(l);
		PersistenceXStream.saveToXMLwithXStream(rm);
		return t;
	}

	public Resident findResidentByName(String name) throws InvalidInputException {
		for (Resident r : rm.getResidents())
			if (r.getName().contentEquals(name))
				return r;

		throw new InvalidInputException("Resident was not found");
	}

	public Tree findTreeById(int id) throws InvalidInputException {
		for (Tree t : rm.getTrees())
			if (t.getId() == id)
				return t;

		throw new InvalidInputException("Tree was not found");
	}

	public Resident findResidentByEmail(String email) throws InvalidInputException {
		if(checkIfNullOrEmptyString(email)) {
			throw new InvalidInputException("Email cannot be null or empty.");
		}
		
		else if (!isValidEmailAddress(email)) {
			throw new InvalidInputException("Email is invalid");
		}
		
		for (Resident r : findAllResidents()) {
			if (r.getEmail().contentEquals(email))
				return r;
		}
		throw new InvalidInputException("Resident was not found");
	}

	public Token checkLogin(String residentEmail, String password_plaintext) throws InvalidInputException {
		Resident r = findResidentByEmail(residentEmail);
		if (r == null)
			throw new InvalidInputException("Resident was not found to login");

		if (checkPassword(password_plaintext, r.getSalt(), r.getPasswordSalted())) {
			Token t = genToken(residentEmail);
			tokens.add(t);
			return t;
		}
		throw new InvalidInputException("Password is false");
	}

	public boolean checkTokenValidity(String tokenString) {
		for (Token t : tokens) {
			if (t.getTokenValue().contentEquals(tokenString)
					&& getCurrentUnixTime() - t.getUnixTime() < Token.getThreshold()) {
				t.setUnixTime(getCurrentUnixTime());
				return true;
			}
		}
		return false;
	}

	public Municipality createMunicipality(String name) throws InvalidInputException {
		if (checkIfNullOrEmptyString(name))
			throw new InvalidInputException("Municipality Name cannot be empty or null");

		Municipality m = new Municipality(name);
		rm.addMunicipality(m);
		PersistenceXStream.saveToXMLwithXStream(rm);
		return m;
	}

	public Resident CreateResident(String aName, String aEmail, String aPassword, double lon, double lat, String type)
			throws InvalidInputException {
		if (checkIfNullOrEmptyString(aName) || checkIfNullOrEmptyString(aEmail) || checkIfNullOrEmptyString(aPassword))
			throw new InvalidInputException("Resident did not provide necessary information");
		if (!(checkIfLocationValid(lon, lat))) {
			throw new InvalidInputException("Resident did not provide a valid location");
		}
		String aSalt = BCrypt.gensalt();
		String aPasswordSalted = hashPassword(aPassword, aSalt);

		Location aPropertyLocation = new Location(lon, lat);

		// creating a resident
		Resident r = null;
		type = type.toLowerCase();

		if (type.contentEquals("resident"))
			r = new Resident(aName, aEmail, aSalt, aPasswordSalted, aPropertyLocation);
		else if (type.contentEquals("environmentalscientist"))
			r = new EnvironmentalScientist(aName, aEmail, aSalt, aPasswordSalted, aPropertyLocation);
		else if (type.contentEquals("municipalarborist"))
			r = new MunicipalArborist(aName, aEmail, aSalt, aPasswordSalted, aPropertyLocation);
		else
			throw new InvalidInputException("Type is invalid");

		rm.addResident(r);
		rm.addLocation(aPropertyLocation);
		PersistenceXStream.saveToXMLwithXStream(rm);
		return r;
	}

	public Transaction createTransaction(Time aTime, Date aDate, Resident r, Tree t,
			Transaction.TreeStatus aChangedStatusTo) throws InvalidInputException {
		if (r == null || t == null || aTime == null || aDate == null || aChangedStatusTo == null)
			throw new InvalidInputException("Resident is null or Tree is null");
		else if(!rm.getResidents().contains(r) || !rm.getTrees().contains(t)) {
			throw new InvalidInputException("Resident or Tree does not exist!");
		}
		else {
			Transaction temp = new Transaction(aTime, aDate, r, t);
			temp.setChangedStatusTo(aChangedStatusTo);

			// change the tree status
			markTree(t, Tree.TreeStatus.values()[aChangedStatusTo.ordinal()]);

			// add transaction
			rm.addTransaction(temp);
			PersistenceXStream.saveToXMLwithXStream(rm);
			return temp;
		}
	}
	
	public Tree markTree(Tree t, Tree.TreeStatus newStatus) throws InvalidInputException{
		if(t == null || newStatus == null) {
			throw new InvalidInputException("Tree or Tree status cannot be null!");
		}
		else if(!rm.getTrees().contains(t)) {
			throw new InvalidInputException("Tree does not exist!");
		}
		else {
			t.setStatus(newStatus);
			return t;
		}
	}

	public List<Tree> findAllTrees() {
		return rm.getTrees();
	}

	public List<Municipality> findAllMunicipalities() {
		return rm.getMunicipalities();
	}

	public List<Resident> findAllResidents() {
		return rm.getResidents();
	}

	public List<Transaction> findAllTransactions() {
		return rm.getTransactions();
	}

	// public Participant createParticipant(String name) throws
	// InvalidInputException {
	// if (checkIfEmptyOrNull(name))
	// throw new InvalidInputException("Participant name cannot be empty!");
	// //check if participant name already exists
	// for(Participant tmp: rm.getParticipants()) {
	// if(tmp.getName().equals(name))
	// throw new InvalidInputException("Participant name already exists");
	// }
	//
	// Participant p = new Participant(name);
	//
	// rm.addParticipant(p);
	// PersistenceXStream.saveToXMLwithXStream(rm);
	// return p;
	// }
	//
	// public Event createEvent(String name, Date date, Time startTime, Time
	// endTime) throws InvalidInputException {
	// if(name == null || date == null || startTime == null || endTime == null)
	// throw new InvalidInputException("Event name cannot be empty! Event date
	// cannot be empty! Event start time cannot be empty! Event end time cannot be
	// empty!");
	// else if (name.trim().contentEquals(""))
	// throw new InvalidInputException("Event name cannot be empty!");
	// else if (startTime.compareTo(endTime) > 0)
	// throw new InvalidInputException("Event end time cannot be before event start
	// time!");
	//
	// //check if event already exists
	// for(Event tmp: rm.getEvents()) {
	// if(tmp.getName().contentEquals(name))
	// throw new InvalidInputException("Event name already exists");
	// }
	//
	// Event e = new Event(name, date, startTime, endTime);
	// rm.addEvent(e);
	// PersistenceXStream.saveToXMLwithXStream(rm);
	//
	// return e;
	// }
	//
	// public Registration register(Participant p, Event e) throws
	// InvalidInputException {
	// if (p == null || e == null)
	// throw new InvalidInputException("Participant needs to be selected for
	// registration! Event needs to be selected for registration!");
	// else if(!checkIfParticipantExists(p.getName()) ||
	// !checkIfEventExists(e.getName())) {
	// throw new InvalidInputException("Participant does not exist! Event does not
	// exist!");
	// }
	//
	// //check if p has already registered for e
	// for(Event tmp: getEventsForParticipant(p)) {
	// if(tmp.getName().contentEquals(e.getName()))
	// throw new InvalidInputException("Participant " + p.getName() + " has already
	// registered for " + e.getName());
	// }
	//
	// Registration r = new Registration(p, e);
	// rm.addRegistration(r);
	// PersistenceXStream.saveToXMLwithXStream(rm);
	//
	// return r;
	// }
	//
	// private boolean checkIfParticipantExists(String name) {
	// for(Participant p: rm.getParticipants())
	// if(p.getName().contentEquals(name))
	// return true;
	// return false;
	// }
	//
	// private boolean checkIfEventExists(String name) {
	// for(Event e: rm.getEvents())
	// if(e.getName().contentEquals(name))
	// return true;
	// return false;
	// }
	//
	// public List<Event> findAllEvents() {
	// return rm.getEvents();
	// }
	//
	// public List<Participant> findAllParticipants() {
	// return rm.getParticipants();
	// }
	//
	// public List<Event> getEventsForParticipant(Participant p) {
	// List<Event> events = new ArrayList<>();
	// for (Registration r : rm.getRegistrations()) {
	// if (r.getParticipant().getName().contentEquals(p.getName()))
	// events.add(r.getEvent());
	// }
	//
	// return events;
	// }
	//
	// public Participant findParticipant(String name) throws InvalidInputException
	// {
	// Participant p = null;
	// for(Participant tmp: rm.getParticipants()) {
	// if(tmp.getName().contentEquals(name)) {
	// p = tmp;
	// break;
	// }
	// }
	//
	// if (p == null)
	// throw new InvalidInputException("Participant was not found");
	// return p;
	// }
	//
	// public Event findEvent(String name) throws InvalidInputException {
	// Event e = null;
	// for(Event tmp: rm.getEvents()) {
	// if(tmp.getName().contentEquals(name)) {
	// e = tmp;
	// break;
	// }
	// }
	//
	// if (e == null)
	// throw new InvalidInputException("Event was not found");
	// return e;
	// }

}
