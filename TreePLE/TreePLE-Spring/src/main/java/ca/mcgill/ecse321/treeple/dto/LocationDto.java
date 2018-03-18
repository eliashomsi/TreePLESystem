package ca.mcgill.ecse321.treeple.dto;

public class LocationDto {
	private double lng;
	private double lat;

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public LocationDto(double lng, double lat) {
		super();
		this.lng = lng;
		this.lat = lat;
	}

	public LocationDto() {
		super();
	}
}
