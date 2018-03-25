package ca.mcgill.ecse321.treeple.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ca.mcgill.ecse321.treeple.model.Location;
import ca.mcgill.ecse321.treeple.model.Municipality;
import ca.mcgill.ecse321.treeple.model.Resident;
import ca.mcgill.ecse321.treeple.model.Tree;
import ca.mcgill.ecse321.treeple.model.TreePLESystem;

public class TestPersistence {

	private TreePLESystem treeSystem;
	
	
	@Ignore
	@Before
	public void setUp() throws Exception {
		
		treeSystem = new TreePLESystem();
		
		//Create Locations
		Location tree1Location = new Location(13234321,33321234);
		Location tree2Location = new Location(34314322,33439994);
		Location resident1Location = new Location(13254322,33821204);
		Location resident2Location = new Location(13254311,33821214);
		
		//Create Municipality
		Municipality pierrefonds = new Municipality("Pierrefonds");
		
		//Create Treeslsl
		Tree tree1 = new Tree(10, tree1Location, pierrefonds);
		Tree tree2 = new Tree(15, tree2Location, pierrefonds); 
		
		//Create Residents
		Resident resident1 = new Resident("John", "john.apple@mail.com", "APASSWORDSALT1", "SALTEDPASSWORD1", resident1Location);
		Resident resident2 = new Resident("Alice", "alice.apple@mail.com", "APASSWORDSALT2", "SALTEDPASSWORD2", resident2Location);
		
		//Register trees to municipality
		pierrefonds.addTree(tree1);
		pierrefonds.addTree(tree2);
		
		//Add objects to treeSystem
		treeSystem.addMunicipality(pierrefonds);
		treeSystem.addTree(tree1);
		treeSystem.addTree(tree2);
		treeSystem.addLocation(tree1Location);
		treeSystem.addLocation(tree2Location);
		treeSystem.addLocation(resident1Location);
		treeSystem.addLocation(resident2Location);
		treeSystem.addResident(resident1);
		treeSystem.addResident(resident2);
		
		
	}

	@After
	public void tearDown() throws Exception {
		treeSystem.delete();
	}

	@Test
	public void test() {
		// initialize model file
	    PersistenceXStream.initializeModelManager("output"+File.separator+"data.xml");
	    // save model that is loaded during test setup
	    if (!PersistenceXStream.saveToXMLwithXStream(treeSystem))
	        fail("Could not save file.");

	    // clear the model in memory
	    treeSystem.delete();
	    assertEquals(0, treeSystem.getLocations().size());
	    assertEquals(0, treeSystem.getMunicipalities().size());
	    assertEquals(0, treeSystem.getTrees().size());
	    
	    // load model
	    treeSystem = (TreePLESystem) PersistenceXStream.loadFromXMLwithXStream();
	    if (treeSystem == null)
	        fail("Could not load file.");
	    
	    // check residents
	    assertEquals(2, treeSystem.getResidents().size());
	    assertEquals("John", treeSystem.getResident(0).getName());
	    assertEquals("Alice", treeSystem.getResident(1).getName());
	    assertEquals("john.apple@mail.com", treeSystem.getResident(0).getEmail());
	    assertEquals("alice.apple@mail.com", treeSystem.getResident(1).getEmail());
	    assertEquals("APASSWORDSALT1", treeSystem.getResident(0).getSalt());
	    assertEquals("APASSWORDSALT2", treeSystem.getResident(1).getSalt());
	    assertEquals("SALTEDPASSWORD1", treeSystem.getResident(0).getPasswordSalted());
	    assertEquals("SALTEDPASSWORD2", treeSystem.getResident(1).getPasswordSalted());
	    assertEquals(treeSystem.getLocation(2), treeSystem.getResident(0).getPropertyLocation());
	    assertEquals(treeSystem.getLocation(3), treeSystem.getResident(1).getPropertyLocation());
	    
	    // check trees
	    assertEquals(2, treeSystem.getTrees().size());
	    assertEquals(10, treeSystem.getTree(0).getDiameter());
	    assertEquals(15, treeSystem.getTree(1).getDiameter());
	    assertEquals(treeSystem.getLocation(0), treeSystem.getTree(0).getTreeLocation());
	    assertEquals(treeSystem.getLocation(1), treeSystem.getTree(1).getTreeLocation());
	    assertEquals("Pierrefonds", treeSystem.getTree(0).getMunicipality().getName());
	    assertEquals("Pierrefonds", treeSystem.getTree(1).getMunicipality().getName());
	    
	    //	check municipality
	    assertEquals(1, treeSystem.getMunicipalities().size());
	    assertEquals("Pierrefonds", treeSystem.getMunicipality(0).getName());
	    
	    //	check locations
	    assertEquals(4, treeSystem.getLocations().size());
	    assertEquals(13234321, treeSystem.getLocation(0).getLongitude(), 1);
	    assertEquals(34314322, treeSystem.getLocation(1).getLongitude(), 1);
	    assertEquals(13254322, treeSystem.getLocation(2).getLongitude(), 1);
	    assertEquals(13254311, treeSystem.getLocation(3).getLongitude(), 1);
	    assertEquals(33321234, treeSystem.getLocation(0).getLatitude(), 1);
	    assertEquals(33439994, treeSystem.getLocation(1).getLatitude(), 1);
	    assertEquals(33821204, treeSystem.getLocation(2).getLatitude(), 1);
	    assertEquals(33821214, treeSystem.getLocation(3).getLatitude(), 1);
	}

}
