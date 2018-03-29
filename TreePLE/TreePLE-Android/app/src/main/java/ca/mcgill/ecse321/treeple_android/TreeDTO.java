package ca.mcgill.ecse321.treeple_android;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by student on 28/03/18.
 */

public class TreeDTO {
    private int id;
    private String species;
    private String status;
    private int diameter;
    private String municipality;
    private LatLng treeLocation;


    public TreeDTO(int id, String species, String status, int diameter, String municipality, LatLng treeLocation) {
        this.id = id;
        this.species = species;
        this.status = status;
        this.diameter = diameter;
        this.municipality = municipality;
        this.treeLocation = treeLocation;
    }

    /**
     * create a dto form json object
     *
     * @param o
     * @throws JSONException
     */
    public TreeDTO(JSONObject o) throws JSONException {
        this.id = Integer.parseInt(o.getString("id"));
        this.diameter = Integer.parseInt(o.getString("diameter"));
        this.municipality = o.getJSONObject("municipality").getString("name");
        this.species = o.getString("species");
        this.status = o.getString("status");
        this.treeLocation = new LatLng(o.getJSONObject("treeLocation").getDouble("lat"), o.getJSONObject("treeLocation").getDouble("lng"));
    }

    public TreeDTO() {
        // TODO Auto-generated constructor stub
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

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public LatLng getTreeLocation() {
        return treeLocation;
    }

    public void setTreeLocation(LatLng treeLocation) {
        this.treeLocation = treeLocation;
    }

    @Override
    public String toString() {
        return "id: " + id + ", municipality:" + municipality;
    }

    public String getLower() {
        return " species:" + species + ", status:" + status + ", diameter:" + diameter;

    }
}
