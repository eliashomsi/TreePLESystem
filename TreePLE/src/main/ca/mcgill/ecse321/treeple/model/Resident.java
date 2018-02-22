/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.26.1-f40f105-3613 modeling language!*/

package ca.mcgill.ecse321.treeple.model;
import java.util.*;
import java.sql.Time;
import java.sql.Date;

// line 32 "../../../../../model.ump"
public class Resident
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextId = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Resident Attributes
  private String name;
  private String email;
  private String passwordSalted;

  //Autounique Attributes
  private int id;

  //Resident Associations
  private Location propertyLocation;
  private TreePLESystem treePLESystem;
  private List<Transaction> transactions;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Resident(String aName, String aEmail, String aPasswordSalted, Location aPropertyLocation, TreePLESystem aTreePLESystem)
  {
    name = aName;
    email = aEmail;
    passwordSalted = aPasswordSalted;
    id = nextId++;
    if (aPropertyLocation == null || aPropertyLocation.getResident() != null)
    {
      throw new RuntimeException("Unable to create Resident due to aPropertyLocation");
    }
    propertyLocation = aPropertyLocation;
    boolean didAddTreePLESystem = setTreePLESystem(aTreePLESystem);
    if (!didAddTreePLESystem)
    {
      throw new RuntimeException("Unable to create resident due to treePLESystem");
    }
    transactions = new ArrayList<Transaction>();
  }

  public Resident(String aName, String aEmail, String aPasswordSalted, double aLongitudeForPropertyLocation, double aLatitudeForPropertyLocation, Tree aTreeForPropertyLocation, TreePLESystem aTreePLESystem)
  {
    name = aName;
    email = aEmail;
    passwordSalted = aPasswordSalted;
    id = nextId++;
    propertyLocation = new Location(aLongitudeForPropertyLocation, aLatitudeForPropertyLocation, aTreeForPropertyLocation, this);
    boolean didAddTreePLESystem = setTreePLESystem(aTreePLESystem);
    if (!didAddTreePLESystem)
    {
      throw new RuntimeException("Unable to create resident due to treePLESystem");
    }
    transactions = new ArrayList<Transaction>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setPasswordSalted(String aPasswordSalted)
  {
    boolean wasSet = false;
    passwordSalted = aPasswordSalted;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public String getEmail()
  {
    return email;
  }

  public String getPasswordSalted()
  {
    return passwordSalted;
  }

  public int getId()
  {
    return id;
  }

  public Location getPropertyLocation()
  {
    return propertyLocation;
  }

  public TreePLESystem getTreePLESystem()
  {
    return treePLESystem;
  }

  public Transaction getTransaction(int index)
  {
    Transaction aTransaction = transactions.get(index);
    return aTransaction;
  }

  public List<Transaction> getTransactions()
  {
    List<Transaction> newTransactions = Collections.unmodifiableList(transactions);
    return newTransactions;
  }

  public int numberOfTransactions()
  {
    int number = transactions.size();
    return number;
  }

  public boolean hasTransactions()
  {
    boolean has = transactions.size() > 0;
    return has;
  }

  public int indexOfTransaction(Transaction aTransaction)
  {
    int index = transactions.indexOf(aTransaction);
    return index;
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
      existingTreePLESystem.removeResident(this);
    }
    treePLESystem.addResident(this);
    wasSet = true;
    return wasSet;
  }

  public static int minimumNumberOfTransactions()
  {
    return 0;
  }

  public Transaction addTransaction(Time aTime, Date aDate, Tree aTree, TreePLESystem aTreePLESystem)
  {
    return new Transaction(aTime, aDate, this, aTree, aTreePLESystem);
  }

  public boolean addTransaction(Transaction aTransaction)
  {
    boolean wasAdded = false;
    if (transactions.contains(aTransaction)) { return false; }
    Resident existingResident = aTransaction.getResident();
    boolean isNewResident = existingResident != null && !this.equals(existingResident);
    if (isNewResident)
    {
      aTransaction.setResident(this);
    }
    else
    {
      transactions.add(aTransaction);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTransaction(Transaction aTransaction)
  {
    boolean wasRemoved = false;
    //Unable to remove aTransaction, as it must always have a resident
    if (!this.equals(aTransaction.getResident()))
    {
      transactions.remove(aTransaction);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addTransactionAt(Transaction aTransaction, int index)
  {  
    boolean wasAdded = false;
    if(addTransaction(aTransaction))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTransactions()) { index = numberOfTransactions() - 1; }
      transactions.remove(aTransaction);
      transactions.add(index, aTransaction);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTransactionAt(Transaction aTransaction, int index)
  {
    boolean wasAdded = false;
    if(transactions.contains(aTransaction))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTransactions()) { index = numberOfTransactions() - 1; }
      transactions.remove(aTransaction);
      transactions.add(index, aTransaction);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTransactionAt(aTransaction, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    Location existingPropertyLocation = propertyLocation;
    propertyLocation = null;
    if (existingPropertyLocation != null)
    {
      existingPropertyLocation.delete();
    }
    TreePLESystem placeholderTreePLESystem = treePLESystem;
    this.treePLESystem = null;
    placeholderTreePLESystem.removeResident(this);
    for(int i=transactions.size(); i > 0; i--)
    {
      Transaction aTransaction = transactions.get(i - 1);
      aTransaction.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "name" + ":" + getName()+ "," +
            "email" + ":" + getEmail()+ "," +
            "passwordSalted" + ":" + getPasswordSalted()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "propertyLocation = "+(getPropertyLocation()!=null?Integer.toHexString(System.identityHashCode(getPropertyLocation())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "treePLESystem = "+(getTreePLESystem()!=null?Integer.toHexString(System.identityHashCode(getTreePLESystem())):"null");
  }
}