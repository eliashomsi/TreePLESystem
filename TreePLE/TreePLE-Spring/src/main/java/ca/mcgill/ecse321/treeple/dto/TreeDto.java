package ca.mcgill.ecse321.treeple.dto;

public class TreeDto {
	private int id;
	private String species;
	private String status;
	private int diameter;
	private MunicipalityDto municipality;
	private LocationDto treeLocation;

	public TreeDto(int id, String species, String status, int diameter, MunicipalityDto municipality,
			LocationDto treeLocation) {
		super();
		this.id = id;
		this.species = species;
		this.status = status;
		this.diameter = diameter;
		this.municipality = municipality;
		this.treeLocation = treeLocation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getDiameter() {
		return diameter;
	}

	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}

	public MunicipalityDto getMunicipality() {
		return municipality;
	}

	public void setMunicipality(MunicipalityDto municipality) {
		this.municipality = municipality;
	}

	public LocationDto getTreeLocation() {
		return treeLocation;
	}

	public void setTreeLocation(LocationDto treeLocation) {
		this.treeLocation = treeLocation;
	}

	public TreeDto(String species, String status, int diameter, MunicipalityDto municipality,
			LocationDto treeLocation) {
		super();
		this.species = species;
		this.status = status;
		this.diameter = diameter;
		this.municipality = municipality;
		this.treeLocation = treeLocation;
	}

	public TreeDto() {
		// TODO Auto-generated constructor stub
	}
}
