package ca.mcgill.ecse321.treeple.controller;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import ca.mcgill.ecse321.treeple.model.Location;
import ca.mcgill.ecse321.treeple.model.Municipality;
import ca.mcgill.ecse321.treeple.model.Resident;
import ca.mcgill.ecse321.treeple.model.Transaction;
import ca.mcgill.ecse321.treeple.model.Tree;
import ca.mcgill.ecse321.treeple.model.Tree.TreeSpecies;
import ca.mcgill.ecse321.treeple.model.Tree.TreeStatus;
import ca.mcgill.ecse321.treeple.model.TreePLESystem;
import ca.mcgill.ecse321.treeple.service.InvalidInputException;
import ca.mcgill.ecse321.treeple.service.Token;
import ca.mcgill.ecse321.treeple.service.TreePLEService;

public class ServiceStub extends TreePLEService {

	public ServiceStub() {
		super(new TreePLESystem());
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		super.delete();
	}

	@Override
	public Tree CreateTree(TreeSpecies species, TreeStatus status, int diameter, double lon, double lat, Municipality m)
			throws InvalidInputException {
		// TODO Auto-generated method stub
		Tree t =new Tree(diameter, new Location(lon, lat), m);
		t.setSpecies(species);
		t.setStatus(status);
		return t;
	}

	@Override
	public Resident findResidentByName(String name) throws InvalidInputException {
		return new Resident(name, "stub", "stub", "stub", null);
	}

	@Override
	public Tree findTreeById(int id) throws InvalidInputException {
		return new Tree(id, null, null);
	}

	@Override
	public Resident findResidentByEmail(String email) throws InvalidInputException {
		return new Resident("stub", email, "stub", "stub", null);
	}

	@Override
	public Token checkLogin(String residentEmail, String password_plaintext) throws InvalidInputException {
		return new Token("stub", "stub", 0);
	}

	@Override
	public boolean checkTokenValidity(String tokenString) {
		// TODO Auto-generated method stub
		return super.checkTokenValidity(tokenString);
	}

	@Override
	public Municipality createMunicipality(String name) throws InvalidInputException {
		return new Municipality(name);
	}

	@Override
	public Resident CreateResident(String aName, String aEmail, String aPassword, double lon, double lat, String type)
			throws InvalidInputException {
		return new Resident(aName, aEmail, "", "", new Location(lon, lat));
	}

	@Override
	public Transaction createTransaction(Time aTime, Date aDate, Resident r, Tree t,
			ca.mcgill.ecse321.treeple.model.Transaction.TreeStatus aChangedStatusTo) throws InvalidInputException {
		// TODO Auto-generated method stub
		return super.createTransaction(aTime, aDate, r, t, aChangedStatusTo);
	}

	@Override
	public Tree markTree(Tree t, TreeStatus newStatus) throws InvalidInputException {
		// TODO Auto-generated method stub
		return super.markTree(t, newStatus);
	}

	@Override
	public List<Tree> findAllTrees() {
		// TODO Auto-generated method stub
		return super.findAllTrees();
	}

	@Override
	public List<Municipality> findAllMunicipalities() {
		// TODO Auto-generated method stub
		return super.findAllMunicipalities();
	}

	@Override
	public List<Resident> findAllResidents() {
		// TODO Auto-generated method stub
		return super.findAllResidents();
	}

	@Override
	public List<Transaction> findAllTransactions() {
		// TODO Auto-generated method stub
		return super.findAllTransactions();
	}
	
	

}
