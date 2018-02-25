/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.26.1-f40f105-3613 modeling language!*/

package ca.mcgill.ecse321.treeple.model;
import java.util.*;
import java.sql.Time;
import java.sql.Date;

// line 10 "../../../../../TreePLE.ump"
public class Tree
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum TreeSpecies { ALDER, APPLE, ASH, ASPEN, BASSWOOD, BIRCH, BUCKEYE, BUCKTHORN, CALIFORNIALAUREL, CATALPA, CEDAR, CHERRY, CHESTNUT, CHINKAPIN, COTTONWOOD, CYPRESS, DOGWOOD, DOUGLASFIR, ELM, FIR, FILBERT, GIANTSEQUOIA, HAWTHORN, HAZEL, HEMLOCK, HONEYLOCUST, HOLLY, HORSECHESTNUT, INCENSECEDAR, JUNIPER, LARCH, LOCUST, MADRONE, MAPLE, MOUNTAINASH, MOUNTAINMAHOGANY, OAK, OREGONMYRTLE, PEAR, PINE, PLUM, POPLAR, REDCEDARARBORVITAE, REDWOOD, RUSSIANOLIVE, SPRUCE, SWEETGUM, SYCAMORE, TANOAK, TRUECEDAR, TRUEFIR, WALNUT, WHITECEDAR, WILLOW, YELLOWPOPLAR, YEW }
  public enum TreeStatus { HEALTHY, CUTDOWN, PLANTED, DISEASED, TOBECUTDOWN }

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextId = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Tree Attributes
  private TreeSpecies species;
  private TreeStatus status;
  private int diameter;

  //Autounique Attributes
  private int id;

  //Tree Associations
  private Location treeLocation;
  private Municipality municipality;
  private List<Transaction> transactions;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Tree(int aDiameter, Location aTreeLocation, Municipality aMunicipality)
  {
    diameter = aDiameter;
    id = nextId++;
    boolean didAddTreeLocation = setTreeLocation(aTreeLocation);
    if (!didAddTreeLocation)
    {
      throw new RuntimeException("Unable to create tree due to treeLocation");
    }
    boolean didAddMunicipality = setMunicipality(aMunicipality);
    if (!didAddMunicipality)
    {
      throw new RuntimeException("Unable to create tree due to municipality");
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

  public int getId()
  {
    return id;
  }

  public Location getTreeLocation()
  {
    return treeLocation;
  }

  public Municipality getMunicipality()
  {
    return municipality;
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

  public boolean setTreeLocation(Location aNewTreeLocation)
  {
    boolean wasSet = false;
    if (aNewTreeLocation == null)
    {
      //Unable to setTreeLocation to null, as tree must always be associated to a treeLocation
      return wasSet;
    }
    
    Tree existingTree = aNewTreeLocation.getTree();
    if (existingTree != null && !equals(existingTree))
    {
      //Unable to setTreeLocation, the current treeLocation already has a tree, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Location anOldTreeLocation = treeLocation;
    treeLocation = aNewTreeLocation;
    treeLocation.setTree(this);

    if (anOldTreeLocation != null)
    {
      anOldTreeLocation.setTree(null);
    }
    wasSet = true;
    return wasSet;
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

  public static int minimumNumberOfTransactions()
  {
    return 0;
  }

  public Transaction addTransaction(Time aTime, Date aDate, Resident aResident)
  {
    return new Transaction(aTime, aDate, aResident, this);
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
      existingTreeLocation.setTree(null);
    }
    Municipality placeholderMunicipality = municipality;
    this.municipality = null;
    placeholderMunicipality.removeTree(this);
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
            "diameter" + ":" + getDiameter()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "species" + "=" + (getSpecies() != null ? !getSpecies().equals(this)  ? getSpecies().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "treeLocation = "+(getTreeLocation()!=null?Integer.toHexString(System.identityHashCode(getTreeLocation())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "municipality = "+(getMunicipality()!=null?Integer.toHexString(System.identityHashCode(getMunicipality())):"null");
  }
}