package ca.mcgill.ecse321.treeple.service;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.mcgill.ecse321.treeple.model.TreePLESystem;
import ca.mcgill.ecse321.treeple.persistence.PersistenceXStream;

public class TestTreePLEService {
	
	private TreePLESystem treeSystem;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PersistenceXStream.initializeModelManager("output"+File.separator+"data.xml");
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
	
	

}
