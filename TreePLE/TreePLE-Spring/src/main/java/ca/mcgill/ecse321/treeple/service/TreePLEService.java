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

	private String hashPassword(String password_plaintext, String salt) {
		String hashed_password = BCrypt.hashpw(password_plaintext, salt);
		return (hashed_password);
	}

	private boolean checkPassword(String password_plaintext, String salt, String hashedPassword) {
		String hash = hashPassword(password_plaintext, salt);
		return hashedPassword.contentEquals(hash);
	}

	/** Business Logic **/
	public Tree CreateTree(TreeSpecies species, TreeStatus status, int diameter, double lon, double lat, Municipality m)
			throws InvalidInputException {
		if (m == null)
			throw new InvalidInputException("Municipality cannot be null");

		Location l = new Location(lon, lat);
		Tree t = new Tree(diameter, l, m);
		t.setSpecies(species);
		t.setStatus(status);
		rm.addTree(t);
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

	public Resident findResidentByEmail(String email) {
		for (Resident r : findAllResidents()) {
			if (r.getEmail().contentEquals(email))
				return r;
		}
		return null;
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
		PersistenceXStream.saveToXMLwithXStream(rm);
		return r;
	}

	public Transaction CreateTransaction(Time aTime, Date aDate, Resident r, Tree t,
			Transaction.TreeStatus aChangedStatusTo) throws InvalidInputException {
		if (r == null || t == null)
			throw new InvalidInputException("Resident is null or Tree is null");

		Transaction temp = new Transaction(aTime, aDate, r, t);
		temp.setChangedStatusTo(aChangedStatusTo);

		// change the tree status
		Tree.TreeStatus newStatus = Tree.TreeStatus.values()[aChangedStatusTo.ordinal()];
		t.setStatus(newStatus);

		// add transaction
		rm.addTransaction(temp);
		PersistenceXStream.saveToXMLwithXStream(rm);
		return temp;
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

}
