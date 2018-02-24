package ca.mcgill.ecse321.treeple.dto;

public class ResidentDto {
	private String name;
	private String email;
	private LocationDto propertyLocation;
	private String type = "Resident";

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocationDto getPropertyLocation() {
		return propertyLocation;
	}

	public void setPropertyLocation(LocationDto propertyLocation) {
		this.propertyLocation = propertyLocation;
	}

	public ResidentDto(String name, String email, LocationDto propertyLocation) {
		super();
		this.name = name;
		this.email = email;
		this.propertyLocation = propertyLocation;
	}

	public ResidentDto() {
		super();
	}

}
