package ca.mcgill.ecse321.treeple.service;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Time;
import java.util.Calendar;
import java.sql.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import ca.mcgill.ecse321.treeple.model.Location;
import ca.mcgill.ecse321.treeple.model.Municipality;
import ca.mcgill.ecse321.treeple.model.Resident;
import ca.mcgill.ecse321.treeple.model.Transaction;
import ca.mcgill.ecse321.treeple.model.Tree;
import ca.mcgill.ecse321.treeple.model.TreePLESystem;
import ca.mcgill.ecse321.treeple.persistence.PersistenceXStream;

public class TestTreePLEService {
	
	private TreePLESystem treeSystem;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PersistenceXStream.initializeModelManager("data.xml");
	}

	@Before
	public void setUp() throws Exception {
		
		treeSystem = new TreePLESystem();
		
	}

	@After
	public void tearDown() throws Exception {
		treeSystem.delete();
	}

	/////////////////////////////////////////////////////////////////
	//Testing Create Municipality
	/////////////////////////////////////////////////////////////////
	
	@Test
	public void testCreateMunicipality() {
		assertEquals(0, treeSystem.getMunicipalities().size());
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		String name = "Pierrefonds";
		
		try {
			treeService.createMunicipality(name);
		} catch (InvalidInputException e) {
			
			fail();
		}
		
		//Check model in memory
		TreePLESystem treeSystemFromMemory = treeSystem;
		checkResultMunicipality(name, treeSystemFromMemory);		

		//Check file contents
		TreePLESystem treeSystemFromFile = (TreePLESystem) PersistenceXStream.loadFromXMLwithXStream();
		checkResultMunicipality(name, treeSystemFromFile);
		
	}
	
	@Test
	public void testCreateMunicipalityNull() {
		assertEquals(0, treeSystem.getMunicipalities().size());
		
		String name = null;
		String error = null;
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		try {
			treeService.createMunicipality(name);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		//Check error
		assertEquals(error, "Municipality Name cannot be empty or null");
		
		//Check no change in memory
		assertEquals(0, treeSystem.getMunicipalities().size());
	}
	
	@Test
	public void testCreateMunicipalityEmpty() {
		assertEquals(0, treeSystem.getMunicipalities().size());
		
		String name = "";
		String error = null;
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		try {
			treeService.createMunicipality(name);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		//Check error
		assertEquals(error, "Municipality Name cannot be empty or null");
				
		//Check no change in memory
		assertEquals(0, treeSystem.getMunicipalities().size());
	}
	
	@Test
	public void testCreateMunicipalitySpaces() {
		assertEquals(0, treeSystem.getMunicipalities().size());
		
		String name = " ";
		
		String error = null;
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		try {
			treeService.createMunicipality(name);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		//Check error
		assertEquals(error, "Municipality Name cannot be empty or null");
		
		//Check no change in memory
		assertEquals(0, treeSystem.getMunicipalities().size());
	}
	
	private void checkResultMunicipality(String name, TreePLESystem treeSystem) {
		assertEquals(1, treeSystem.getMunicipalities().size());
		assertEquals(name, treeSystem.getMunicipality(0).getName());
		assertEquals(0, treeSystem.getLocations().size());
		assertEquals(0, treeSystem.getResidents().size());
		assertEquals(0, treeSystem.getTrees().size());
		assertEquals(0, treeSystem.getTransactions().size());
	}
	
	
	/////////////////////////////////////////////////////////////////
	//Testing Create Resident
	/////////////////////////////////////////////////////////////////
	
	@Test
	public void testCreateResident() {
		assertEquals(0, treeSystem.getResidents().size());
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		String name = "John";
		String email = "john.apple@mail.com";
		String password = "1234";
		double longitude = 12.123433;
		double latitude = 43.432112;
		String type = "resident";
		
		try {
			treeService.CreateResident(name, email, password, longitude, latitude, type);
		} catch (InvalidInputException e) {
			
			fail();
		}
		
		//Check model in memory
		TreePLESystem treeSystemFromMemory = treeSystem;
		checkResultResident(treeSystemFromMemory, name, email, password, longitude, latitude, type); 	

		//Check file contents
		TreePLESystem treeSystemFromFile = (TreePLESystem) PersistenceXStream.loadFromXMLwithXStream();
		checkResultResident(treeSystemFromFile, name, email, password, longitude, latitude, type); 	
		
	}
	
	@Test
	public void testCreateResidentNull() {
		assertEquals(0, treeSystem.getResidents().size());
		
		String nameNull = null;
		String emailNull = null;
		String passwordNull = null;
		Double longitudeNull = null;
		Double latitudeNull = null;
		String typeNull = null;
		
		String name = "John";
		String email = "john.apple@mail.com";
		String password = "1234";
		double longitude = 1234.1234;
		double latitude = 4321.4321;
		String type = "resident";
		
		String error = null;
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		try {
			treeService.CreateResident(nameNull, email, password, longitude, latitude, type);
			treeService.CreateResident(name, emailNull, password, longitude, latitude, type);
			treeService.CreateResident(name, email, passwordNull, longitude, latitude, type);
			treeService.CreateResident(name, email, password, longitudeNull, latitude, type);
			treeService.CreateResident(name, email, password, longitude, latitudeNull, type);
			treeService.CreateResident(name, email, password, longitude, latitude, typeNull);
		} catch (InvalidInputException e) {
			
			error = e.getMessage();
		}
		
		//Check error
		assertEquals(error, "Resident did not provide necessary information");
		
		//Check no change in memory
		assertEquals(0, treeSystem.getResidents().size());
	}
	
	@Test
	public void testCreateResidentEmpty() {
		assertEquals(0, treeSystem.getResidents().size());
		
		String nameEmpty = "";
		String emailEmpty = "";
		String passwordEmpty = "";
		String typeEmpty = "";
		
		String name = "John";
		String email = "john.apple@mail.com";
		String password = "1234";
		double longitude = 1234.1234;
		double latitude = 4321.4321;
		String type = "resident";
		
		String error = null;
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		try {
			treeService.CreateResident(nameEmpty, email, password, longitude, latitude, type);
			treeService.CreateResident(name, emailEmpty, password, longitude, latitude, type);
			treeService.CreateResident(name, email, passwordEmpty, longitude, latitude, type);
			treeService.CreateResident(name, email, password, longitude, latitude, typeEmpty);
		} catch (InvalidInputException e) {
			
			error = e.getMessage();
		}
		
		//Check error
		assertEquals(error, "Resident did not provide necessary information");
		
		//Check no change in memory
		assertEquals(0, treeSystem.getResidents().size());
	}
	
	@Test
	public void testCreateResidentSpaces() {
		assertEquals(0, treeSystem.getResidents().size());
		
		String nameSpaces = " ";
		String emailSpaces = " ";
		String passwordSpaces = " ";
		String typeSpaces = " ";
		
		String name = "John";
		String email = "john.apple@mail.com";
		String password = "1234";
		double longitude = 12.123456;
		double latitude = 43.432144;
		String type = "resident";
		
		String error = null;
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		try {
			treeService.CreateResident(nameSpaces, email, password, longitude, latitude, type);
			treeService.CreateResident(name, emailSpaces, password, longitude, latitude, type);
			treeService.CreateResident(name, email, passwordSpaces, longitude, latitude, type);
			treeService.CreateResident(name, email, password, longitude, latitude, typeSpaces);
		} catch (InvalidInputException e) {
			
			error = e.getMessage();
		}
		
		//Check error
		assertEquals(error, "Resident did not provide necessary information");
		
		//Check no change in memory
		assertEquals(0, treeSystem.getResidents().size());
	}
	
	@Test
	public void testCreateResidentInvalidLocation() {
		assertEquals(0, treeSystem.getResidents().size());
		
		String name = "John";
		String email = "john.apple@mail.com";
		String password = "1234";
		double longitude = 1234.1234;
		double latitude = 4321.4321;
		String type = "resident";
		
		String error = null;
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		try {
			treeService.CreateResident(name, email, password, longitude, latitude, type);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		//Check error
		assertEquals(error, "Resident did not provide a valid location");
		
		//Check no change in memory
		assertEquals(0, treeSystem.getResidents().size());
	}
	
	private void checkResultResident(TreePLESystem treeSystem, String aName, String aEmail, String aPassword, double lon, double lat, String type) {
		assertEquals(1, treeSystem.getResidents().size());
		assertEquals(aName, treeSystem.getResident(0).getName());
		assertEquals(aEmail, treeSystem.getResident(0).getEmail());
		//assertEquals(aPassword, treeSystem.getResident(0).getPasswordSalted());
		assertEquals(lon, treeSystem.getResident(0).getPropertyLocation().getLongitude(), 0.000001);
		assertEquals(lat, treeSystem.getResident(0).getPropertyLocation().getLatitude(), 0.000001);
		assertEquals(1, treeSystem.getLocations().size());
		assertEquals(0, treeSystem.getMunicipalities().size());
		assertEquals(0, treeSystem.getTrees().size());
		assertEquals(0, treeSystem.getTransactions().size());
	}
	
	/*
	 * Tests for createTransaction()
	 */
	
	@Test
	public void testCreateTransaction() {
		assertEquals(0, treeSystem.getTransactions().size());
		Transaction.TreeStatus treeStatus  = Transaction.TreeStatus.PLANTED;
		
		
		
		double longitudeR = 1234.1234;
		double latitudeR = 4321.4321;
		
		Location lr = new Location(longitudeR, latitudeR);
		treeSystem.addLocation(lr);
		assertEquals(1, treeSystem.getLocations().size());
		
		String name = "John";
		String email = "john.apple@mail.com";
		String password = "1234";
		String type = "resident";
		String salt = BCrypt.gensalt();
		String passwordSalted = BCrypt.hashpw(password, salt);
		
		Resident r = new Resident(name, email,salt, passwordSalted, lr);
		treeSystem.addResident(r);
		assertEquals(1, treeSystem.getResidents().size());
		
		double longitudeT = 4321.4321;
		double latitudeT = 1234.1234;
		
		Location lt = new Location(longitudeT, latitudeT);
		treeSystem.addLocation(lt);
		assertEquals(2, treeSystem.getLocations().size());
		
		String muni = "Rosemont";
		Municipality m = new Municipality(muni);
		treeSystem.addMunicipality(m);
		assertEquals(1, treeSystem.getMunicipalities().size());
		
		int diam = 5;
		Tree t = new Tree(diam, lt, m);
		treeSystem.addTree(t);
		assertEquals(1, treeSystem.getTrees().size());
		
		Calendar c = Calendar.getInstance();
		c.set(2018, Calendar.MARCH, 16, 10, 30, 0);
	    Date trDate = new Date(c.getTimeInMillis());
		Time trTime = new Time(c.getTimeInMillis());
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		try {
			treeService.createTransaction(trTime, trDate, r, t, treeStatus);
		}
		catch (InvalidInputException e) {
			fail();
		}
		
		checkResultTransaction(lr, r, lt, m, t,trDate,trTime,treeStatus, treeSystem);
		// Check file for persistence
		TreePLESystem treeS2 = (TreePLESystem) PersistenceXStream.loadFromXMLwithXStream();
		checkResultTransaction(lr, r, lt, m, t,trDate,trTime,treeStatus, treeSystem);
		treeS2.delete();
		
	}
	
	@Test
	public void testCreateTransactionNull() {
		assertEquals(0, treeSystem.getTransactions().size());
		Transaction.TreeStatus treeStatus  = null;
		
		
		
	
		Resident r = null;
		assertEquals(0, treeSystem.getResidents().size());
		
		
		Tree t = null;
		assertEquals(0, treeSystem.getTrees().size());
		
		
	    Date trDate = null;
		Time trTime = null;
		
		String error = null;
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		try {
			treeService.createTransaction(trTime, trDate, r, t, treeStatus);
		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals(error, "Resident is null or Tree is null");
		
		//Check model 
		assertEquals(0, treeSystem.getResidents().size());
		assertEquals(0, treeSystem.getTrees().size());
		assertEquals(0, treeSystem.getTransactions().size());


	}
	
	@Test
	public void testCreateTransactionResidentAndTreeDoNoExist() {
		assertEquals(0, treeSystem.getTransactions().size());
		Transaction.TreeStatus treeStatus  = Transaction.TreeStatus.PLANTED;
		
		
		
		double longitudeR = 1234.1234;
		double latitudeR = 4321.4321;
		
		Location lr = new Location(longitudeR, latitudeR);
		treeSystem.addLocation(lr);
		assertEquals(1, treeSystem.getLocations().size());
		
		String name = "John";
		String email = "john.apple@mail.com";
		String password = "1234";
		String type = "resident";
		String salt = BCrypt.gensalt();
		String passwordSalted = BCrypt.hashpw(password, salt);
		
		Resident r = new Resident(name, email,salt, passwordSalted, lr);
		assertEquals(0, treeSystem.getResidents().size());
		
		double longitudeT = 4321.4321;
		double latitudeT = 1234.1234;
		
		Location lt = new Location(longitudeT, latitudeT);
		treeSystem.addLocation(lt);
		assertEquals(2, treeSystem.getLocations().size());
		
		String muni = "Saint-Laurent";
		Municipality m = new Municipality(muni);
		treeSystem.addMunicipality(m);
		assertEquals(1, treeSystem.getMunicipalities().size());
		
		int diam = 5;
		Tree t = new Tree(diam, lt, m);
		assertEquals(0, treeSystem.getTrees().size());
		
		Calendar c = Calendar.getInstance();
		c.set(2018, Calendar.MARCH, 16, 10, 30, 0);
	    Date trDate = new Date(c.getTimeInMillis());
		Time trTime = new Time(c.getTimeInMillis());
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		String error = null;
		try {
			treeService.createTransaction(trTime, trDate, r, t, treeStatus);
		}
		catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals(error, "Resident or Tree does not exist!");
		
		//Check model
		assertEquals(0, treeSystem.getResidents().size());
		assertEquals(0, treeSystem.getTrees().size());
		assertEquals(0, treeSystem.getTransactions().size());
		
	}
	
	private void checkResultTransaction(Location lr, Resident r, Location lt, Municipality m, Tree t,Date d, Time time, 
			Transaction.TreeStatus status, TreePLESystem treeS2) {
		assertEquals(2, treeS2.getLocations().size());
		assertEquals(lr, treeS2.getLocation(0));
		assertEquals(lt, treeS2.getLocation(1));
		assertEquals(1, treeS2.getResidents().size());
		assertEquals(r, treeS2.getResident(0));
		assertEquals(lr, treeS2.getResident(0).getPropertyLocation());
		assertEquals(1, treeS2.getMunicipalities().size());
		assertEquals(m, treeS2.getMunicipality(0));
		assertEquals(1, treeS2.getTrees().size());
		assertEquals(t, treeS2.getTree(0));
		assertEquals(lt, treeS2.getTree(0).getTreeLocation());
		assertEquals(1, treeS2.getTransactions().size());
		assertEquals(treeS2.getResident(0), treeS2.getTransaction(0).getResident());
		assertEquals(treeS2.getTree(0), treeS2.getTransaction(0).getTree());
		assertEquals(d, treeS2.getTransaction(0).getDate());
		assertEquals(time, treeS2.getTransaction(0).getTime());
		assertEquals(status, treeS2.getTransaction(0).getChangedStatusTo());
	}
	
	/*
	 * Tests for markTree()
	 */
	
	@Test
	public void testMarkTree() {
		assertEquals(0, treeSystem.getTrees().size());
		
		double longitudeT = 4321.4321;
		double latitudeT = 1234.1234;
		
		Location lt = new Location(longitudeT, latitudeT);
		treeSystem.addLocation(lt);
		assertEquals(1, treeSystem.getLocations().size());
		
		String muni = "Villeray";
		Municipality m = new Municipality(muni);
		treeSystem.addMunicipality(m);
		assertEquals(1, treeSystem.getMunicipalities().size());
		
		int diam = 5;
		Tree t = new Tree(diam, lt, m);
		t.setStatus(Tree.TreeStatus.PLANTED);
		treeSystem.addTree(t);
		assertEquals(1, treeSystem.getTrees().size());
		assertEquals(Tree.TreeStatus.PLANTED, treeSystem.getTree(0).getStatus());
		
		Tree.TreeStatus treeStatus = Tree.TreeStatus.CUTDOWN;
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		try {
			treeService.markTree(t, treeStatus);
		}
		catch(InvalidInputException e) {
			fail();
		}
		
		assertEquals(treeStatus, treeSystem.getTree(0).getStatus());
		
		TreePLESystem treeS2 = (TreePLESystem) PersistenceXStream.loadFromXMLwithXStream();
		
		assertEquals(treeStatus, treeSystem.getTree(0).getStatus());

		treeS2.delete();
	}
	
	@Test
	public void testMarkTreeNull() {
		assertEquals(0, treeSystem.getTrees().size());
		
		
		Tree t = null;
		assertEquals(0, treeSystem.getTrees().size());

		
		Tree.TreeStatus treeStatus = null;
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		String error = null;
		try {
			treeService.markTree(t, treeStatus);
		}
		catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Tree or Tree status cannot be null!", error);
		
	}
	
	@Test
	public void testMarkTreeNotFound() {
		assertEquals(0, treeSystem.getTrees().size());
		
		
		double longitudeT = 4321.4321;
		double latitudeT = 1234.1234;
		
		Location lt = new Location(longitudeT, latitudeT);
		treeSystem.addLocation(lt);
		assertEquals(1, treeSystem.getLocations().size());
		
		String muni = "Ahuntsic";
		Municipality m = new Municipality(muni);
		treeSystem.addMunicipality(m);
		assertEquals(1, treeSystem.getMunicipalities().size());
		
		int diam = 5;
		Tree t = new Tree(diam, lt, m);
		t.setStatus(Tree.TreeStatus.PLANTED);
		assertEquals(0, treeSystem.getTrees().size());
		
		Tree.TreeStatus treeStatus = Tree.TreeStatus.CUTDOWN;
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		String error = null;
		try {
			treeService.markTree(t, treeStatus);
		}
		catch(InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals("Tree does not exist!", error);
		
	}
	
	
	
	
	

}
