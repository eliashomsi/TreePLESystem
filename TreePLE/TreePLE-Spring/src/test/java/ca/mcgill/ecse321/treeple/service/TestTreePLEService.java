package ca.mcgill.ecse321.treeple.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

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
import ca.mcgill.ecse321.treeple.model.Tree.TreeSpecies;
import ca.mcgill.ecse321.treeple.model.Tree.TreeStatus;
import ca.mcgill.ecse321.treeple.model.TreePLESystem;
import ca.mcgill.ecse321.treeple.persistence.PersistenceXStream;

public class TestTreePLEService {
	
	private TreePLESystem treeSystem;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PersistenceXStream.initializeModelManager("testdata.xml");
	}
	
	@BeforeClass
	public static void setUpAfterClass() throws Exception {
		PersistenceXStream.initializeModelManager("testdata.xml");
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
	
	@Test
	public void testCreateMunicipalityWithJavaScriptCode() {
		assertEquals(0, treeSystem.getMunicipalities().size());
		
		String name = "<script> mun1 </script>";
		
		String error = null;
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		try {
			treeService.createMunicipality(name);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		//Check error
		assertEquals(error, "Municipality Name cannot be javascript code");
		
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
	
	/*
	 * Tests for CreateTree()
	 */
	
	@Test
	public void testCreateTree() {
		assertEquals(0, treeSystem.getTrees().size());
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		TreeSpecies species = Tree.TreeSpecies.ALDER;
		TreeStatus status = Tree.TreeStatus.HEALTHY;
		int diameter = 5;
		double lon = -69.739844;
		double lat = 44.724340;
		Municipality m = new Municipality("St-Michel");
		treeSystem.addMunicipality(m);
		
		try {
			treeService.CreateTree(species, status, diameter, lon, lat, m);
		} catch (InvalidInputException e) {
			fail();
		}
		
		//Check model in memory
		TreePLESystem treeSystemFromMemory = treeSystem;
		checkResultTree(species, status, diameter, lon, lat, m, treeSystemFromMemory);		

		//Check file contents
		TreePLESystem treeSystemFromFile = (TreePLESystem) PersistenceXStream.loadFromXMLwithXStream();
		checkResultTree(species, status, diameter, lon, lat, m, treeSystemFromFile);
		
	}
	
	@Test
	public void testCreateTreeNullOrDefault() {
		assertEquals(0, treeSystem.getTrees().size());
		
		TreeSpecies speciesNull = null;
		TreeStatus statusNull = null;
		int diameterDefault = 0;
		double lonDefault = 0.0d;
		double latDefault = 0.0d;
		Municipality mNull = null;
		
		TreeSpecies species = Tree.TreeSpecies.ALDER;
		TreeStatus status = Tree.TreeStatus.HEALTHY;
		int diameter = 10;
		double lon = -91.536719;
		double lat = 35.040167;
		Municipality m = new Municipality("Kirkland");
		treeSystem.addMunicipality(m);
		
		String error = null;
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		try {
			treeService.CreateTree(speciesNull, status, diameter, lon, lat, m);
			treeService.CreateTree(species, statusNull, diameter, lon, lat, m);
			treeService.CreateTree(species, status, diameterDefault, lon, lat, m);
			treeService.CreateTree(species, status, diameter, lonDefault, lat, m);
			treeService.CreateTree(species, status, diameter, lon, latDefault, m);
			treeService.CreateTree(species, status, diameter, lon, lat, mNull);
		} catch (InvalidInputException e) {
			
			error = e.getMessage();
		}
		
		//Check error
		assertEquals(error, "You did not provide necessary information for the tree");
		
		//Check no change in memory
		assertEquals(0, treeSystem.getTrees().size());
	}
	
	@Test
	public void testCreateTreeInvalidLocation() {
		assertEquals(0, treeSystem.getTrees().size());
		
		TreeSpecies species = Tree.TreeSpecies.ALDER;
		TreeStatus status = Tree.TreeStatus.HEALTHY;
		int diameter = 10;
		double lon = 4325.536719;
		double lat = 8672.040167;
		Municipality m = new Municipality("St-Leonard");
		treeSystem.addMunicipality(m);
		
		String error = null;
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		try {
			treeService.CreateTree(species, status, diameter, lon, lat, m);
		} catch (InvalidInputException e) {
			
			error = e.getMessage();
		}
		
		//Check error
		assertEquals(error, "Resident did not provide a valid location");
		
		//Check no change in memory
		assertEquals(0, treeSystem.getTrees().size());
	}
	
	@Test
	public void testCreateTreeSmallDiameter() {
		assertEquals(0, treeSystem.getTrees().size());
		
		TreeSpecies species = Tree.TreeSpecies.ALDER;
		TreeStatus status = Tree.TreeStatus.HEALTHY;
		int diameter = 4;
		double lon = -77.122656;
		double lat = 39.784315;
		Municipality m = new Municipality("St-Laurent");
		treeSystem.addMunicipality(m);
		
		String error = null;
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		try {
			treeService.CreateTree(species, status, diameter, lon, lat, m);
		} catch (InvalidInputException e) {
			
			error = e.getMessage();
		}
		
		//Check error
		assertEquals(error, "Trees' diameter should be above 5cm.");
		
		//Check no change in memory
		assertEquals(0, treeSystem.getTrees().size());
	}
	
	@Test
	public void testCreateTreeExistingLocation() {
		assertEquals(0, treeSystem.getTrees().size());
		
		TreeSpecies species = Tree.TreeSpecies.ALDER;
		TreeStatus status = Tree.TreeStatus.HEALTHY;
		int diameter = 10;
		double lon = -92.942969;
		double lat = 33.295003;
		Location l = new Location(lon, lat);
		treeSystem.addLocation(l);
		Municipality m = new Municipality("Montreal-North");
		treeSystem.addMunicipality(m);
		
		String error = null;
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		try {
			treeService.CreateTree(species, status, diameter, lon, lat, m);
		} catch (InvalidInputException e) {
			
			error = e.getMessage();
		}
		
		//Check error
		assertEquals(error, "This location is currently occupied");
		
		//Check no change in memory
		assertEquals(0, treeSystem.getTrees().size());
	}
	
	private void checkResultTree(TreeSpecies species, TreeStatus status, int diameter, double lon, double lat,
			Municipality m, TreePLESystem treeSystem) {
		assertEquals(1, treeSystem.getTrees().size());
		assertEquals(species, treeSystem.getTree(0).getSpecies());
		assertEquals(status, treeSystem.getTree(0).getStatus());
		assertEquals(diameter, treeSystem.getTree(0).getDiameter());
		assertEquals(1, treeSystem.getLocations().size());
		assertEquals(lon, treeSystem.getTree(0).getTreeLocation().getLongitude(), 0.000001);
		assertEquals(lat, treeSystem.getTree(0).getTreeLocation().getLatitude(), 0.000001);
		assertEquals(1, treeSystem.getMunicipalities().size());
		assertEquals("St-Michel", treeSystem.getTree(0).getMunicipality().getName());
	}
	
	/*
	 * Tests for FindResidentByEmail()
	 */
	
	@Test
	public void testFindResidentByEmail() {
		assertEquals(0, treeSystem.getLocations().size());
		assertEquals(0, treeSystem.getResidents().size());
		
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		String name = "Johnny";
		String email = "johnny.orange@mail.com";
		String password = "1234";
		double lon = -124.935156;
		double lat = 51.076821;
		String salt = BCrypt.gensalt();
		String aPasswordSalted = BCrypt.hashpw(password, salt);
		Location l = new Location(lon, lat);
		treeSystem.addLocation(l);
		assertEquals(1, treeSystem.getLocations().size());
		
		Resident r = new Resident(name, email, salt, aPasswordSalted, l);
		treeSystem.addResident(r);
		assertEquals(1, treeSystem.getResidents().size());
		
		Resident r2 = null;
		
		try {
			r2 = treeService.findResidentByEmail(email);
		} catch (InvalidInputException e) {
			fail();
		}
		
		assertEquals(r2, r);
		
	}
	
	@Test
	public void testFindResidentByEmailEmpty() {
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		Resident r = null;
		String email = "";
		String error = null;
		
		try {
			r = treeService.findResidentByEmail(email);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals(error, "Email cannot be null or empty.");
		assertEquals(r, null);
		
	}
	
	@Test
	public void testFindResidentByEmailNull() {
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		Resident r = null;
		String email = null;
		String error = null;
		
		try {
			r = treeService.findResidentByEmail(email);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals(error, "Email cannot be null or empty.");
		assertEquals(r, null);
		
	}
	
	@Test
	public void testFindResidentByEmailInvalid() {
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		
		Resident r = null;
		String email = "Bob";
		String error = null;
		
		try {
			r = treeService.findResidentByEmail(email);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		assertEquals(error, "Email is invalid");
		assertEquals(r, null);
		
	}
	
	/*
	 * Tests for findTreeById()
	 */

	@Test 
	public void testFindTreeById() {
		assertEquals(0, treeSystem.getTrees().size());
		
		
		double longitudeT = 4321.4321;
		double latitudeT = 1234.1234;
		
		Location lt = new Location(longitudeT, latitudeT);
		treeSystem.addLocation(lt);
		assertEquals(1, treeSystem.getLocations().size());
		
		String muni = "Saint-Michel";
		Municipality m = new Municipality(muni);
		treeSystem.addMunicipality(m);
		assertEquals(1, treeSystem.getMunicipalities().size());
		
		int diam = 5;
		Tree t1 = new Tree(diam, lt, m);
		int myid = t1.getId();
		treeSystem.addTree(t1);
		assertEquals(1, treeSystem.getTrees().size());
		assertEquals(myid, treeSystem.getTree(0).getId());
		
		diam = 6;
		lt = new Location(latitudeT, longitudeT);
		Tree t2 = new Tree(diam, lt, m);
		int myid2 = t2.getId();
		treeSystem.addTree(t2);
		assertEquals(2, treeSystem.getTrees().size());
		assertEquals(myid2, treeSystem.getTree(1).getId());
		
		TreePLEService treeService = new TreePLEService(treeSystem);
		try {
			assertEquals(t1, treeService.findTreeById(myid));
			assertEquals(t2, treeService.findTreeById(myid2));
		}
		catch(InvalidInputException e) {
			fail();
		}
		
	}
	
	@Test 
	public void testFindTreeByIdNotFound() {
		assertEquals(0, treeSystem.getTrees().size());
		
		
		double longitudeT = 4321.4321;
		double latitudeT = 1234.1234;
		
		Location lt = new Location(longitudeT, latitudeT);
		treeSystem.addLocation(lt);
		assertEquals(1, treeSystem.getLocations().size());
		
		String muni = "Parc-Extension";
		Municipality m = new Municipality(muni);
		treeSystem.addMunicipality(m);
		assertEquals(1, treeSystem.getMunicipalities().size());
		
		int diam = 5;
		Tree t1 = new Tree(diam, lt, m);
		assertEquals(0, treeSystem.getTrees().size());

		
		TreePLEService treeService = new TreePLEService(treeSystem);
		String error = null;
		try {
			assertEquals(t1, treeService.findTreeById(1));
		}
		catch(InvalidInputException e) {
			error = e.getMessage();
		}
	
		assertEquals(error, "Tree was not found");
	}

}
