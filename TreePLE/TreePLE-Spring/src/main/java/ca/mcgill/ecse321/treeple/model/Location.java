/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.26.1-f40f105-3613 modeling language!*/

package ca.mcgill.ecse321.treeple.model;

// line 88 "../../../../../TreePLE.ump"
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

  public Location(double aLongitude, double aLatitude)
  {
    longitude = aLongitude;
    latitude = aLatitude;
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

  public boolean hasTree()
  {
    boolean has = tree != null;
    return has;
  }

  public Resident getResident()
  {
    return resident;
  }

  public boolean hasResident()
  {
    boolean has = resident != null;
    return has;
  }

  public boolean setTree(Tree aNewTree)
  {
    boolean wasSet = false;
    if (tree != null && !tree.equals(aNewTree) && equals(tree.getTreeLocation()))
    {
      //Unable to setTree, as existing tree would become an orphan
      return wasSet;
    }

    tree = aNewTree;
    Location anOldTreeLocation = aNewTree != null ? aNewTree.getTreeLocation() : null;

    if (!this.equals(anOldTreeLocation))
    {
      if (anOldTreeLocation != null)
      {
        anOldTreeLocation.tree = null;
      }
      if (tree != null)
      {
        tree.setTreeLocation(this);
      }
    }
    wasSet = true;
    return wasSet;
  }

  public boolean setResident(Resident aNewResident)
  {
    boolean wasSet = false;
    if (resident != null && !resident.equals(aNewResident) && equals(resident.getPropertyLocation()))
    {
      //Unable to setResident, as existing resident would become an orphan
      return wasSet;
    }

    resident = aNewResident;
    Location anOldPropertyLocation = aNewResident != null ? aNewResident.getPropertyLocation() : null;

    if (!this.equals(anOldPropertyLocation))
    {
      if (anOldPropertyLocation != null)
      {
        anOldPropertyLocation.resident = null;
      }
      if (resident != null)
      {
        resident.setPropertyLocation(this);
      }
    }
    wasSet = true;
    return wasSet;
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