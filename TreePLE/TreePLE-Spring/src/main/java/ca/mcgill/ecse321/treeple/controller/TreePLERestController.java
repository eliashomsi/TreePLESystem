package ca.mcgill.ecse321.treeple.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.treeple.dto.LocationDto;
import ca.mcgill.ecse321.treeple.dto.MunicipalityDto;
import ca.mcgill.ecse321.treeple.dto.ResidentDto;
import ca.mcgill.ecse321.treeple.dto.TransactionDto;
import ca.mcgill.ecse321.treeple.dto.TreeDto;
import ca.mcgill.ecse321.treeple.model.Location;
import ca.mcgill.ecse321.treeple.model.Municipality;
import ca.mcgill.ecse321.treeple.model.Resident;
import ca.mcgill.ecse321.treeple.model.Transaction;
import ca.mcgill.ecse321.treeple.model.Tree;
import ca.mcgill.ecse321.treeple.service.InvalidInputException;
import ca.mcgill.ecse321.treeple.service.Token;
import ca.mcgill.ecse321.treeple.service.TreePLEService;

@RestController
public class TreePLERestController {
	@Autowired
	private TreePLEService service;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * SHOW INFORMATION
	 */
	@RequestMapping("/")
	public String index() {
		return "application root please use the front end to contact the controller.\n";
	}

	@GetMapping(value = { "/trees", "/trees/" })
	public List<TreeDto> findAllTrees() {
		List<TreeDto> trees = new ArrayList<>();
		for (Tree t : service.findAllTrees()) {
			trees.add(convertToDto(t));
		}
		return trees;
	}

	@GetMapping(value = { "/municipalities", "/municipalities/" })
	public List<MunicipalityDto> findAllMunicipalities() {
		List<MunicipalityDto> municipalities = new ArrayList<>();
		for (Municipality m : service.findAllMunicipalities()) {
			municipalities.add(convertToDto(m));
		}
		return municipalities;
	}

	@GetMapping(value = { "/residents", "/residents/" })
	public List<ResidentDto> findAllResidents() {
		List<ResidentDto> residents = new ArrayList<>();
		for (Resident r : service.findAllResidents()) {
			residents.add(convertToDto(r));
		}
		return residents;
	}

	@GetMapping(value = { "/transactions", "/transactions/" })
	public List<TransactionDto> findAllTransactions() {
		List<TransactionDto> transactions = new ArrayList<>();
		for (Transaction t : service.findAllTransactions()) {
			transactions.add(convertToDto(t));
		}
		return transactions;
	}

	@GetMapping(value = { "/treestatuslist", "/treestatuslist/" })
	public List<String> treestatuslist() {
		List<String> res = new ArrayList<String>();
		Object[] possibleValues = Tree.TreeStatus.values();
		for (Object option : possibleValues) {
			res.add(option.toString());
		}
		return res;
	}

	@GetMapping(value = { "/treespecieslist", "/treespecieslist/" })
	public List<String> treeSpeciesList() {
		List<String> res = new ArrayList<String>();
		Object[] possibleValues = Tree.TreeSpecies.values();
		for (Object option : possibleValues) {
			res.add(option.toString());
		}
		return res;
	}

	/**
	 * CREATE INFROMATION
	 */

	@PostMapping(value = { "/trees/", "/trees" })
	public TreeDto createTree(@RequestParam(name = "treespecies") Tree.TreeSpecies treespecies,
			@RequestParam(name = "treestatus") Tree.TreeStatus treestatus,
			@RequestParam(name = "diameter") int diameter, @RequestParam(name = "longitude") double longitude,
			@RequestParam(name = "latitude") double latitude, @RequestParam(name = "municipality") MunicipalityDto mDto)
			throws InvalidInputException {
		Municipality m = convertToDomainObject(mDto);
		if (m == null)
			throw new InvalidInputException("Municipality was not found");

		Tree t = service.CreateTree(treespecies, treestatus, diameter, longitude, latitude, m);

		return convertToDto(t);
	}

	@PostMapping(value = { "/login/", "/login" })
	public Token login(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password)
			throws InvalidInputException {
		return service.checkLogin(email, password);
	}

	@PostMapping(value = { "/checkToken/", "/checkToken" })
	public boolean checkToken(@RequestParam(name = "token") String token) throws InvalidInputException {
		return service.checkTokenValidity(token);
	}

	@PostMapping(value = { "/municipalities/{name}", "/municipalities/{name}/" })
	public MunicipalityDto createMunicipality(@PathVariable("name") String name) throws InvalidInputException {
		Municipality m = service.createMunicipality(name);
		return convertToDto(m);
	}

	@PostMapping(value = { "/residents/", "/residents" })
	public ResidentDto createResident(@RequestParam(name = "name") String aName,
			@RequestParam(name = "email") String aEmail, @RequestParam(name = "password") String aPassword,
			@RequestParam(name = "longitude") double lon, @RequestParam(name = "latitude") double lat,
			@RequestParam(name = "type") String type) throws InvalidInputException {

		Resident r = service.CreateResident(aName, aEmail, aPassword, lon, lat, type);
		return convertToDto(r);
	}

	@PostMapping(value = { "/transactions/", "/transactions" })
	public TransactionDto CreateTransaction(
			@RequestParam(name = "time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "HH:mm") LocalTime aTime,
			@RequestParam(name = "date") Date aDate, @RequestParam(name = "resident") String residentEmail,
			@RequestParam(name = "tree") int treeid,
			@RequestParam(name = "status") Transaction.TreeStatus aChangedStatusTo) throws InvalidInputException {

		Resident resident = service.findResidentByEmail(residentEmail);

		Tree tree = service.findTreeById(treeid);

		@SuppressWarnings("deprecation")
		Time timesql = new Time(aTime.getHour(), aTime.getMinute(), 0);

		if (resident == null || tree == null)
			throw new InvalidInputException("Tree or Resident were not found");
		Transaction t = service.createTransaction(timesql, aDate, resident, tree, aChangedStatusTo);
		return convertToDto(t);
	}

	/** Conversion methods (not part of the API) **/
	private Municipality convertToDomainObject(MunicipalityDto mDto) {
		for (Municipality m : service.findAllMunicipalities()) {
			if (m.getName().contentEquals(mDto.getName()))
				return m;
		}

		return null;
	}

	private Resident convertToDomainObject(ResidentDto rDto) {
		for (Resident r : service.findAllResidents()) {
			if (r.getName().contentEquals(rDto.getName()))
				return r;
		}
		return null;
	}

	private Tree convertToDomainObject(TreeDto tDto) {
		for (Tree t : service.findAllTrees()) {
			if (t.getId() == tDto.getId())
				return t;
		}

		return null;
	}

	private LocationDto convertToDto(Location e) {
		// In simple cases, the mapper service is convenient
		LocationDto t = modelMapper.map(e, LocationDto.class);
		t.setLng(e.getLongitude());
		t.setLat(e.getLatitude());
		return t;
	}

	private MunicipalityDto convertToDto(Municipality m) {
		return modelMapper.map(m, MunicipalityDto.class);
	}

	private ResidentDto convertToDto(Resident r) {
		ResidentDto rd = modelMapper.map(r, ResidentDto.class);
		rd.setPropertyLocation(convertToDto(r.getPropertyLocation()));
		rd.setType(r.getClass().getName());
		return rd;
	}

	private TreeDto convertToDto(Tree t) {
		TreeDto td = modelMapper.map(t, TreeDto.class);
		td.setMunicipality(convertToDto(t.getMunicipality()));
		td.setSpecies(t.getSpecies().toString());
		td.setStatus(t.getStatus().toString());
		td.setTreeLocation(convertToDto(t.getTreeLocation()));
		td.setId(t.getId());
		return td;
	}

	private TransactionDto convertToDto(Transaction tt) {
		TransactionDto td = modelMapper.map(tt, TransactionDto.class);
		td.setResident(convertToDto(tt.getResident()));
		td.setTree(convertToDto(tt.getTree()));
		td.setStatus(tt.getChangedStatusTo());
		return td;
	}

	/** security related **/

}
