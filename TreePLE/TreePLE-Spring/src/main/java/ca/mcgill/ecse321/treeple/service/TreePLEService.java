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

	/** detete **/
	public void delete() {
		this.rm.delete();
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

	private Tree checkValidTreeid(int id) {
		for (Tree t : this.rm.getTrees()) {
			if (t.getId() == id)
				return t;
		}
		return null;
	}

	private boolean checkIfNullOrEmptyString(String s) {
		return s == null || s.trim().contentEquals("");
	}

	private boolean checkIfLocationValid(double lon, double lat) {
		boolean isValid = true;
		if (lon < -180.000000 || lon > 180.000000) {
			isValid = false;
		}
		if (lat < -90.000000 || lat > 90.000000) {
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

		if (emailParts.length != 2 || checkIfNullOrEmptyString(emailParts[0])
				|| checkIfNullOrEmptyString(emailParts[1])) {
			return false;
		} else
			return true;
	}

	/**
	 * Business Logic
	 * 
	 * @throws InvalidInputException
	 **/

	public void deleteTree(int id) throws InvalidInputException {
		Tree t = checkValidTreeid(id);
		if (t == null) {
			throw new InvalidInputException("The id was not found for tree");
		}

		this.rm.removeTree(t);
		// save
		PersistenceXStream.saveToXMLwithXStream(rm);
	}

	/**
	 * this method edits tree information
	 * 
	 * @param id
	 *            the id to find the tree with
	 * @param species
	 *            new field
	 * @param status
	 *            new field
	 * @param diameter
	 *            new field
	 * @param lon
	 *            new field
	 * @param lat
	 *            new field
	 * @param m
	 *            new field
	 * @throws InvalidInputException
	 */
	public void editTree(int id, TreeSpecies species, int diameter, Municipality m)
			throws InvalidInputException {
		Tree t = checkValidTreeid(id);
		if (t == null) {
			throw new InvalidInputException("The id was not found for tree");
		}

		this.editTreeDiameter(t, diameter);
		this.editTreeMunicipality(t, m);
		this.editTreeSpecies(t, species);
		// save
		PersistenceXStream.saveToXMLwithXStream(rm);
	}

	/**
	 * creates a new tree
	 * 
	 * @param species
	 * @param status
	 * @param diameter
	 * @param lon
	 * @param lat
	 * @param m
	 * @return
	 * @throws InvalidInputException
	 */
	public Tree CreateTree(TreeSpecies species, TreeStatus status, int diameter, double lon, double lat, Municipality m)
			throws InvalidInputException {
		if (species == null || status == null || diameter == 0 || lon == 0.0d || lat == 0.0d || m == null) {
			throw new InvalidInputException("You did not provide necessary information for the tree");
		}
		if (!(checkIfLocationValid(lon, lat))) {
			throw new InvalidInputException("Resident did not provide a valid location");
		}

		if (diameter < 5) {
			throw new InvalidInputException("Trees' diameter should be above 5cm.");
		}

		for (int i = 0; i < rm.getLocations().size(); i++) {
			double lonTemp = rm.getLocation(i).getLongitude();
			double latTemp = rm.getLocation(i).getLatitude();
			if (lonTemp == lon && latTemp == lat) {
				throw new InvalidInputException("This location is currently occupied");
			}
		}

		Location l = new Location(lon, lat);
		Tree t = new Tree(diameter, l, m);
		t.setSpecies(species);
		t.setStatus(status);
		t.setTreeLocation(l);
		rm.addTree(t);
		rm.addLocation(l);
		PersistenceXStream.saveToXMLwithXStream(rm);
		return t;
	}

	/**
	 * finds a resident by name
	 * 
	 * @param name
	 * @return
	 * @throws InvalidInputException
	 */
	public Resident findResidentByName(String name) throws InvalidInputException {
		for (Resident r : rm.getResidents())
			if (r.getName().contentEquals(name))
				return r;

		throw new InvalidInputException("Resident was not found");
	}

	/**
	 * findTreeById
	 * 
	 * @param id
	 * @return
	 * @throws InvalidInputException
	 */
	public Tree findTreeById(int id) throws InvalidInputException {
		for (Tree t : rm.getTrees())
			if (t.getId() == id)
				return t;

		throw new InvalidInputException("Tree was not found");
	}

	/**
	 * findResidentByEmail
	 * 
	 * @param email
	 * @return
	 * @throws InvalidInputException
	 */
	public Resident findResidentByEmail(String email) throws InvalidInputException {
		if (checkIfNullOrEmptyString(email)) {
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

	/**
	 * generates a new token
	 * 
	 * @param residentEmail
	 * @param password_plaintext
	 * @return
	 * @throws InvalidInputException
	 */
	public Token checkLogin(String residentEmail, String password_plaintext) throws InvalidInputException {
		Resident r = findResidentByEmail(residentEmail);

		if (checkPassword(password_plaintext, r.getSalt(), r.getPasswordSalted())) {
			Token t = genToken(residentEmail);
			tokens.add(t);
			return t;
		}
		throw new InvalidInputException("Password is false");
	}

	/**
	 * check if token is valid
	 * 
	 * @param tokenString
	 * @return
	 */
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

	/**
	 * createMunicipality
	 * 
	 * @param name
	 * @return
	 * @throws InvalidInputException
	 */
	public Municipality createMunicipality(String name) throws InvalidInputException {
		if (checkIfNullOrEmptyString(name))
			throw new InvalidInputException("Municipality Name cannot be empty or null");

		if (!validVariableName(name))
			throw new InvalidInputException("Municipality with invalid name");
		if (name.contains("<script>"))
			throw new InvalidInputException("Municipality Name cannot be javascript code");
		Municipality m = new Municipality(name);
		rm.addMunicipality(m);
		PersistenceXStream.saveToXMLwithXStream(rm);
		return m;
	}

	/**
	 * valid naming convention
	 * 
	 * @param m
	 * @return true/false
	 */
	private boolean validVariableName(String m) {
		if (m.length() == 1)
			return false;

		char f = m.charAt(0);

		if (!Character.isAlphabetic(f))
			return false;

		for (int i = 1; i < m.length(); i++) {
			if (!(Character.isAlphabetic(m.charAt(i)) || Character.isDigit(m.charAt(i))
					|| Character.isSpace(m.charAt(i))))
				return false;
		}
		return true;

	}

	/**
	 * CreateResident
	 * 
	 * @param aName
	 * @param aEmail
	 * @param aPassword
	 * @param lon
	 * @param lat
	 * @param type
	 * @return
	 * @throws InvalidInputException
	 */
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

		if (!isValidEmailAddress(aEmail))
			throw new InvalidInputException("email format is not right");

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

	/**
	 * createTransaction
	 * 
	 * @param aTime
	 * @param aDate
	 * @param r
	 * @param t
	 * @param aChangedStatusTo
	 * @return
	 * @throws InvalidInputException
	 */
	public Transaction createTransaction(Time aTime, Date aDate, Resident r, Tree t,
			Transaction.TreeStatus aChangedStatusTo) throws InvalidInputException {
		if (r == null || t == null || aTime == null || aDate == null || aChangedStatusTo == null)
			throw new InvalidInputException("Resident is null or Tree is null");
		else if (!rm.getResidents().contains(r) || !rm.getTrees().contains(t)) {
			throw new InvalidInputException("Resident or Tree does not exist!");
		} else if (!checkForValidOperationAuthorization(r, aChangedStatusTo)) {
			throw new InvalidInputException("Operation is unauthorized! requires different type of user");
		} else if (!checkForValidOperationTiming(t, aChangedStatusTo)) {
			throw new InvalidInputException("Cant change the status to the same type");
		} else if (!isOwner(r, t)) {
			throw new InvalidInputException("Tree is owned by another resident");
		} else {
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

	/**
	 * checkForValidOperationTiming (it should make sense for example a cutdown
	 * can't be brought back to live).
	 * 
	 * @param t
	 * @param to
	 * @return
	 * @throws InvalidInputException
	 */
	private boolean checkForValidOperationTiming(Tree t, Transaction.TreeStatus to) throws InvalidInputException {

		Transaction.TreeStatus from = Transaction.TreeStatus.values()[t.getStatus().ordinal()];
		if (from == to)
			throw new InvalidInputException("Cant change the status to the same type");

		switch (from) {
		case DISEASED:
			if (to == Transaction.TreeStatus.PLANTED)
				throw new InvalidInputException("Cant change the status from DISEASED TO PLANTED");
			break;
		case CUTDOWN:
			throw new InvalidInputException("After cutdown can't change status");
		case HEALTHY:
			break;
		case PLANTED:
			break;
		case TOBECUTDOWN:
			if (to != Transaction.TreeStatus.CUTDOWN)
				throw new InvalidInputException("When A tree is marked as to be cutdown the only status is cutdown");
			break;
		default:
			throw new InvalidInputException("Unknown status");
		}
		return true;
	}

	/**
	 * authorization level
	 * 
	 * @param r
	 * @param aChangedStatusTo
	 * @return
	 */
	private boolean checkForValidOperationAuthorization(Resident r, Transaction.TreeStatus aChangedStatusTo) {
		if (r instanceof MunicipalArborist) {
			return true;
		} else if (r instanceof EnvironmentalScientist) {
			return true;
		} else {
			if (aChangedStatusTo == Transaction.TreeStatus.DISEASED
					|| aChangedStatusTo == Transaction.TreeStatus.CUTDOWN
					|| aChangedStatusTo == Transaction.TreeStatus.PLANTED)
				return true;
		}
		return false;
	}

	/**
	 * check if resident is owner
	 * 
	 * @param r
	 * @param t
	 * @return
	 */
	private boolean isOwner(Resident r, Tree t) {
		// remove restriction from escalated users
		if (r instanceof MunicipalArborist || r instanceof EnvironmentalScientist)
			return true;

		if (t.getTransactions() == null || t.numberOfTransactions() == 0)
			return true;
		if (t.getTransactions().get(t.numberOfTransactions() - 1).getResident().getEmail().equals(r.getEmail()))
			return true;

		return false;
	}

	/**
	 * marknig a tree with a new status
	 * 
	 * @param t
	 * @param newStatus
	 * @return
	 * @throws InvalidInputException
	 */
	public Tree markTree(Tree t, Tree.TreeStatus newStatus) throws InvalidInputException {

		if (t == null || newStatus == null) {

			throw new InvalidInputException("Tree or Tree status cannot be null!");
		} else if (!rm.getTrees().contains(t)) {
			throw new InvalidInputException("Tree does not exist!");
		} else {
			t.setStatus(newStatus);
			return t;
		}
	}

	/**
	 * editTreeSpecies
	 * 
	 * @param t
	 * @param newSpecies
	 * @return
	 * @throws InvalidInputException
	 */
	public Tree editTreeSpecies(Tree t, TreeSpecies newSpecies) throws InvalidInputException {

		if (t == null) {

			throw new InvalidInputException("Tree cannot be null!");
		} else if (newSpecies == null) {

			throw new InvalidInputException("Tree Species cannot be null!");
		} else if (!rm.getTrees().contains(t)) {

			throw new InvalidInputException("Tree does not exist!");
		} else {
			t.setSpecies(newSpecies);
			return t;
		}
	}

	/**
	 * editTreeDiameter
	 * 
	 * @param t
	 * @param newDiameter
	 * @return
	 * @throws InvalidInputException
	 */
	public Tree editTreeDiameter(Tree t, int newDiameter) throws InvalidInputException {

		if (t == null) {

			throw new InvalidInputException("Tree cannot be null!");
		} else if (newDiameter < 5) {

			throw new InvalidInputException("Trees' diameter should be above 5cm.");
		}

		else if (!rm.getTrees().contains(t)) {

			throw new InvalidInputException("Tree does not exist!");
		}

		else {
			t.setDiameter(newDiameter);
			return t;
		}
	}

	/**
	 * editTreeLocation
	 * 
	 * @param t
	 * @param newLon
	 * @param newLat
	 * @return
	 * @throws InvalidInputException
	 */
	public Tree editTreeLocation(Tree t, double newLon, double newLat) throws InvalidInputException {

		if (t == null) {

			throw new InvalidInputException("Tree cannot be null!");
		}

		else if (!rm.getTrees().contains(t)) {

			throw new InvalidInputException("Tree does not exist!");

		}

		else if (!(checkIfLocationValid(newLon, newLat))) {

			throw new InvalidInputException("Not a valid location!");

		} else {

			for (int i = 0; i < rm.getLocations().size(); i++) {
				double lonTemp = rm.getLocation(i).getLongitude();
				double latTemp = rm.getLocation(i).getLatitude();
				if (lonTemp == newLon && latTemp == newLat) {
					throw new InvalidInputException("This location is currently occupied");
				}
			}
		}

		t.getTreeLocation().setLatitude(newLat);
		t.getTreeLocation().setLongitude(newLon);

		return t;
	}

	/**
	 * editTreeMunicipality
	 * 
	 * @param t
	 * @param newMunicipality
	 * @return
	 * @throws InvalidInputException
	 */
	public Tree editTreeMunicipality(Tree t, Municipality newMunicipality) throws InvalidInputException {

		if (t == null) {

			throw new InvalidInputException("Tree cannot be null!");
		}

		else if (!rm.getTrees().contains(t)) {

			throw new InvalidInputException("Tree does not exist!");

		}

		else if (newMunicipality == null) {

			throw new InvalidInputException("Municipality cannot be null");

		}

		else if (!rm.getMunicipalities().contains(newMunicipality)) {

			throw new InvalidInputException("Municipality does not exist!");

		} else {

			t.setMunicipality(newMunicipality);

		}

		return t;
	}

	/**
	 * findAllTrees
	 * 
	 * @return
	 */
	public List<Tree> findAllTrees() {
		return rm.getTrees();
	}

	/**
	 * findAllMunicipalities
	 * 
	 * @return
	 */
	public List<Municipality> findAllMunicipalities() {
		return rm.getMunicipalities();
	}

	/**
	 * findAllResidents
	 * 
	 * @return
	 */
	public List<Resident> findAllResidents() {
		return rm.getResidents();
	}

	/**
	 * findAllTransactions
	 * 
	 * @return
	 */
	public List<Transaction> findAllTransactions() {
		return rm.getTransactions();
	}

}
