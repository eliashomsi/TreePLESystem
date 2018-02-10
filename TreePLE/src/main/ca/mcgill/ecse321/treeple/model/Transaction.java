/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse321.treeple.model;
import java.sql.Time;
import java.sql.Date;

// line 39 "../../../../../model.ump"
public class Transaction
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum TreeStatus { Healthy, CutDown, Planted, Diseased }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Transaction Attributes
  private Time time;
  private Date date;
  private TreeStatus changedStatusFrom;
  private TreeStatus changedStatusTo;

  //Transaction Associations
  private Resident resident;
  private Tree tree;
  private TreePLESystem treePLESystem;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Transaction(Time aTime, Date aDate, Resident aResident, Tree aTree, TreePLESystem aTreePLESystem)
  {
    time = aTime;
    date = aDate;
    boolean didAddResident = setResident(aResident);
    if (!didAddResident)
    {
      throw new RuntimeException("Unable to create transaction due to resident");
    }
    boolean didAddTree = setTree(aTree);
    if (!didAddTree)
    {
      throw new RuntimeException("Unable to create transaction due to tree");
    }
    boolean didAddTreePLESystem = setTreePLESystem(aTreePLESystem);
    if (!didAddTreePLESystem)
    {
      throw new RuntimeException("Unable to create transaction due to treePLESystem");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTime(Time aTime)
  {
    boolean wasSet = false;
    time = aTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setChangedStatusFrom(TreeStatus aChangedStatusFrom)
  {
    boolean wasSet = false;
    changedStatusFrom = aChangedStatusFrom;
    wasSet = true;
    return wasSet;
  }

  public boolean setChangedStatusTo(TreeStatus aChangedStatusTo)
  {
    boolean wasSet = false;
    changedStatusTo = aChangedStatusTo;
    wasSet = true;
    return wasSet;
  }

  public Time getTime()
  {
    return time;
  }

  public Date getDate()
  {
    return date;
  }

  public TreeStatus getChangedStatusFrom()
  {
    return changedStatusFrom;
  }

  public TreeStatus getChangedStatusTo()
  {
    return changedStatusTo;
  }

  public Resident getResident()
  {
    return resident;
  }

  public Tree getTree()
  {
    return tree;
  }

  public TreePLESystem getTreePLESystem()
  {
    return treePLESystem;
  }

  public boolean setResident(Resident aResident)
  {
    boolean wasSet = false;
    if (aResident == null)
    {
      return wasSet;
    }

    Resident existingResident = resident;
    resident = aResident;
    if (existingResident != null && !existingResident.equals(aResident))
    {
      existingResident.removeTransaction(this);
    }
    resident.addTransaction(this);
    wasSet = true;
    return wasSet;
  }

  public boolean setTree(Tree aTree)
  {
    boolean wasSet = false;
    if (aTree == null)
    {
      return wasSet;
    }

    Tree existingTree = tree;
    tree = aTree;
    if (existingTree != null && !existingTree.equals(aTree))
    {
      existingTree.removeTransaction(this);
    }
    tree.addTransaction(this);
    wasSet = true;
    return wasSet;
  }

  public boolean setTreePLESystem(TreePLESystem aTreePLESystem)
  {
    boolean wasSet = false;
    if (aTreePLESystem == null)
    {
      return wasSet;
    }

    TreePLESystem existingTreePLESystem = treePLESystem;
    treePLESystem = aTreePLESystem;
    if (existingTreePLESystem != null && !existingTreePLESystem.equals(aTreePLESystem))
    {
      existingTreePLESystem.removeTransaction(this);
    }
    treePLESystem.addTransaction(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Resident placeholderResident = resident;
    this.resident = null;
    if(placeholderResident != null)
    {
      placeholderResident.removeTransaction(this);
    }
    Tree placeholderTree = tree;
    this.tree = null;
    if(placeholderTree != null)
    {
      placeholderTree.removeTransaction(this);
    }
    TreePLESystem placeholderTreePLESystem = treePLESystem;
    this.treePLESystem = null;
    if(placeholderTreePLESystem != null)
    {
      placeholderTreePLESystem.removeTransaction(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "time" + "=" + (getTime() != null ? !getTime().equals(this)  ? getTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "changedStatusFrom" + "=" + (getChangedStatusFrom() != null ? !getChangedStatusFrom().equals(this)  ? getChangedStatusFrom().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "changedStatusTo" + "=" + (getChangedStatusTo() != null ? !getChangedStatusTo().equals(this)  ? getChangedStatusTo().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "resident = "+(getResident()!=null?Integer.toHexString(System.identityHashCode(getResident())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "tree = "+(getTree()!=null?Integer.toHexString(System.identityHashCode(getTree())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "treePLESystem = "+(getTreePLESystem()!=null?Integer.toHexString(System.identityHashCode(getTreePLESystem())):"null");
  }
}