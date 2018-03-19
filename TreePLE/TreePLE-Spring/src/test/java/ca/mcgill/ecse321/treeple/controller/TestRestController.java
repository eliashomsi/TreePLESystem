package ca.mcgill.ecse321.treeple.controller;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import ca.mcgill.ecse321.treeple.dto.MunicipalityDto;
import ca.mcgill.ecse321.treeple.dto.ResidentDto;
import ca.mcgill.ecse321.treeple.service.InvalidInputException;

public class TestRestController {
	private TreePLERestController controller;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		controller = new TreePLERestController(new ServiceStub(), new ModelMapper());
	}

	@After
	public void tearDown() throws Exception {
		controller.delete();
	}

	@Test
	public void testCreateMunicipality() {
		MunicipalityDto m = null;
		try {
			m = controller.createMunicipality("stub");
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!m.getName().contentEquals("stub")) {
			fail("municipality are not equal");
		}

	}

	@Test
	public void testCreateResident() {
		ResidentDto m = null;
		try {
			m = controller.createResident("stub", "stub", "sffffffftub", 0, 0, "stub");
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!m.getName().contentEquals("stub")) {
			fail("residents are not equal");
		}

	}

	@Test
	public void testCreateResidentWithEmptyName() {
		ResidentDto m = null;
		try {
			m = controller.createResident("", "stub", "sfffffftub", 0, 0, "stub");
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			if (!e.getMessage().contentEquals("name/email/password should not be empty")) {
				fail();
			}
		}

	}
	
	@Test
	public void testCreateResidentWithEmptyEmail() {
		ResidentDto m = null;
		try {
			m = controller.createResident("st", "", "stffffffub", 0, 0, "stub");
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			if (!e.getMessage().contentEquals("name/email/password should not be empty")) {
				fail();
			}
		}

	}
	
	@Test
	public void testCreateResidentWithEmptyPassword() {
		ResidentDto m = null;
		try {
			m = controller.createResident("ss", "stub", "", 0, 0, "stub");
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			if (!e.getMessage().contentEquals("name/email/password should not be empty")) {
				fail();
			}
		}
	}
	
	@Test
	public void testCreateResidentWithNullName() {
		ResidentDto m = null;
		try {
			m = controller.createResident(null, "stub", "stfffffffub", 0, 0, "stub");
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			if (!e.getMessage().contentEquals("name/email/password should not be Null")) {
				fail();
			}
		}

	}
	
	@Test
	public void testCreateResidentWithNullEmail() {
		ResidentDto m = null;
		try {
			m = controller.createResident("st", null, "stffffffffub", 0, 0, "stub");
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			if (!e.getMessage().contentEquals("name/email/password should not be Null")) {
				fail();
			}
		}

	}
	
	@Test
	public void testCreateResidentWithNullPassword() {
		ResidentDto m = null;
		try {
			m = controller.createResident("ss", "stub", null, 0, 0, "stub");
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			if (!e.getMessage().contentEquals("name/email/password should not be Null")) {
				fail();
			}
		}
	}
	
	@Test
	public void testCreateResidentWithShortPassword() {
		ResidentDto m = null;
		try {
			m = controller.createResident("ss", "stub", "dsf", 0, 0, "stub");
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			if (!e.getMessage().contentEquals("password must be at least 6 chars")) {
				fail();
			}
		}
	}

}
