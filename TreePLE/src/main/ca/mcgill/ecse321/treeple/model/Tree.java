/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse321.treeple.model;
import java.util.*;
import java.sql.Time;
import java.sql.Date;

// line 9 "../../../../../model.ump"
public class Tree
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum TreeSpecies { Oak, Maple }
  public enum TreeStatus { Healthy, CutDown, Planted, Diseased }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Tree Attributes
  private TreeSpecies species;
  private TreeStatus status;
  private int diameter;

  //Tree Associations
  private Location treeLocation;
  private Municipality municipality;
  private TreePLESystem treePLESystem;
  private List<Transaction> transactions;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Tree(int aDiameter, Location aTreeLocation, Municipality aMunicipality, TreePLESystem aTreePLESystem)
  {
    diameter = aDiameter;
    if (aTreeLocation == null || aTreeLocation.getTree() != null)
    {
      throw new RuntimeException("Unable to create Tree due to aTreeLocation");
    }
    treeLocation = aTreeLocation;
    boolean didAddMunicipality = setMunicipality(aMunicipality);
    if (!didAddMunicipality)
    {
      throw new RuntimeException("Unable to create tree due to municipality");
    }
    boolean didAddTreePLESystem = setTreePLESystem(aTreePLESystem);
    if (!didAddTreePLESystem)
    {
      throw new RuntimeException("Unable to create tree due to treePLESystem");
    }
    transactions = new ArrayList<Transaction>();
  }

  public Tree(int aDiameter, double aLongitudeForTreeLocation, double aLatitudeForTreeLocation, Resident aResidentForTreeLocation, Municipality aMunicipality, TreePLESystem aTreePLESystem)
  {
    diameter = aDiameter;
    treeLocation = new Location(aLongitudeForTreeLocation, aLatitudeForTreeLocation, this, aResidentForTreeLocation);
    boolean didAddMunicipality = setMunicipality(aMunicipality);
    if (!didAddMunicipality)
    {
      throw new RuntimeException("Unable to create tree due to municipality");
    }
    boolean didAddTreePLESystem = setTreePLESystem(aTreePLESystem);
    if (!didAddTreePLESystem)
    {
      throw new RuntimeException("Unable to create tree due to treePLESystem");
    }
    transactions = new ArrayList<Transaction>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setSpecies(TreeSpecies aSpecies)
  {
    boolean wasSet = false;
    species = aSpecies;
    wasSet = true;
    return wasSet;
  }

  public boolean setStatus(TreeStatus aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean setDiameter(int aDiameter)
  {
    boolean wasSet = false;
    diameter = aDiameter;
    wasSet = true;
    return wasSet;
  }

  public TreeSpecies getSpecies()
  {
    return species;
  }

  public TreeStatus getStatus()
  {
    return status;
  }

  public int getDiameter()
  {
    return diameter;
  }

  public Location getTreeLocation()
  {
    return treeLocation;
  }

  public Municipality getMunicipality()
  {
    return municipality;
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

  public boolean setMunicipality(Municipality aMunicipality)
  {
    boolean wasSet = false;
    if (aMunicipality == null)
    {
      return wasSet;
    }

    Municipality existingMunicipality = municipality;
    municipality = aMunicipality;
    if (existingMunicipality != null && !existingMunicipality.equals(aMunicipality))
    {
      existingMunicipality.removeTree(this);
    }
    municipality.addTree(this);
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
      existingTreePLESystem.removeTree(this);
    }
    treePLESystem.addTree(this);
    wasSet = true;
    return wasSet;
  }

  public static int minimumNumberOfTransactions()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Transaction addTransaction(Time aTime, Date aDate, Resident aResident, TreePLESystem aTreePLESystem)
  {
    return new Transaction(aTime, aDate, aResident, this, aTreePLESystem);
  }

  public boolean addTransaction(Transaction aTransaction)
  {
    boolean wasAdded = false;
    if (transactions.contains(aTransaction)) { return false; }
    Tree existingTree = aTransaction.getTree();
    boolean isNewTree = existingTree != null && !this.equals(existingTree);
    if (isNewTree)
    {
      aTransaction.setTree(this);
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
    //Unable to remove aTransaction, as it must always have a tree
    if (!this.equals(aTransaction.getTree()))
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
    Location existingTreeLocation = treeLocation;
    treeLocation = null;
    if (existingTreeLocation != null)
    {
      existingTreeLocation.delete();
    }
    Municipality placeholderMunicipality = municipality;
    this.municipality = null;
    if(placeholderMunicipality != null)
    {
      placeholderMunicipality.removeTree(this);
    }
    TreePLESystem placeholderTreePLESystem = treePLESystem;
    this.treePLESystem = null;
    if(placeholderTreePLESystem != null)
    {
      placeholderTreePLESystem.removeTree(this);
    }
    for(int i=transactions.size(); i > 0; i--)
    {
      Transaction aTransaction = transactions.get(i - 1);
      aTransaction.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "diameter" + ":" + getDiameter()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "species" + "=" + (getSpecies() != null ? !getSpecies().equals(this)  ? getSpecies().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "treeLocation = "+(getTreeLocation()!=null?Integer.toHexString(System.identityHashCode(getTreeLocation())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "municipality = "+(getMunicipality()!=null?Integer.toHexString(System.identityHashCode(getMunicipality())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "treePLESystem = "+(getTreePLESystem()!=null?Integer.toHexString(System.identityHashCode(getTreePLESystem())):"null");
  }
}