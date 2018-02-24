/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.26.1-f40f105-3613 modeling language!*/

package ca.mcgill.ecse321.treeple.model;
import java.sql.Time;
import java.sql.Date;

// line 98 "../../../../../TreePLE.ump"
public class Transaction
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum TreeStatus { HEALTHY, CUTDOWN, PLANTED, DISEASED, TOBECUTDOWN }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Transaction Attributes
  private Time time;
  private Date date;
  private TreeStatus changedStatusTo;

  //Transaction Associations
  private Resident resident;
  private Tree tree;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Transaction(Time aTime, Date aDate, Resident aResident, Tree aTree)
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

  public void delete()
  {
    Resident placeholderResident = resident;
    this.resident = null;
    placeholderResident.removeTransaction(this);
    Tree placeholderTree = tree;
    this.tree = null;
    placeholderTree.removeTransaction(this);
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "time" + "=" + (getTime() != null ? !getTime().equals(this)  ? getTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "changedStatusTo" + "=" + (getChangedStatusTo() != null ? !getChangedStatusTo().equals(this)  ? getChangedStatusTo().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "resident = "+(getResident()!=null?Integer.toHexString(System.identityHashCode(getResident())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "tree = "+(getTree()!=null?Integer.toHexString(System.identityHashCode(getTree())):"null");
  }
}