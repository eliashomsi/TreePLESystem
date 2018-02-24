package ca.mcgill.ecse321.treeple.dto;

public class LocationDto {
	private double lon;
	private double lat;

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public LocationDto(double lon, double lat) {
		super();
		this.lon = lon;
		this.lat = lat;
	}

	public LocationDto() {
		super();
	}
}
