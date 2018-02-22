/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.26.1-f40f105-3613 modeling language!*/

package ca.mcgill.ecse321.treeple.model;

// line 27 "../../../../../model.ump"
public class Location
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Location Attributes
  private double longitude;
  private double latitude;

  //Location Associations
  private Tree tree;
  private Resident resident;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Location(double aLongitude, double aLatitude, Tree aTree, Resident aResident)
  {
    longitude = aLongitude;
    latitude = aLatitude;
    if (aTree == null || aTree.getTreeLocation() != null)
    {
      throw new RuntimeException("Unable to create Location due to aTree");
    }
    tree = aTree;
    if (aResident == null || aResident.getPropertyLocation() != null)
    {
      throw new RuntimeException("Unable to create Location due to aResident");
    }
    resident = aResident;
  }

  public Location(double aLongitude, double aLatitude, int aDiameterForTree, Municipality aMunicipalityForTree, TreePLESystem aTreePLESystemForTree, String aNameForResident, String aEmailForResident, String aPasswordSaltedForResident, TreePLESystem aTreePLESystemForResident)
  {
    longitude = aLongitude;
    latitude = aLatitude;
    tree = new Tree(aDiameterForTree, this, aMunicipalityForTree, aTreePLESystemForTree);
    resident = new Resident(aNameForResident, aEmailForResident, aPasswordSaltedForResident, this, aTreePLESystemForResident);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setLongitude(double aLongitude)
  {
    boolean wasSet = false;
    longitude = aLongitude;
    wasSet = true;
    return wasSet;
  }

  public boolean setLatitude(double aLatitude)
  {
    boolean wasSet = false;
    latitude = aLatitude;
    wasSet = true;
    return wasSet;
  }

  public double getLongitude()
  {
    return longitude;
  }

  public double getLatitude()
  {
    return latitude;
  }

  public Tree getTree()
  {
    return tree;
  }

  public Resident getResident()
  {
    return resident;
  }

  public void delete()
  {
    Tree existingTree = tree;
    tree = null;
    if (existingTree != null)
    {
      existingTree.delete();
    }
    Resident existingResident = resident;
    resident = null;
    if (existingResident != null)
    {
      existingResident.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "longitude" + ":" + getLongitude()+ "," +
            "latitude" + ":" + getLatitude()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "tree = "+(getTree()!=null?Integer.toHexString(System.identityHashCode(getTree())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "resident = "+(getResident()!=null?Integer.toHexString(System.identityHashCode(getResident())):"null");
  }
}