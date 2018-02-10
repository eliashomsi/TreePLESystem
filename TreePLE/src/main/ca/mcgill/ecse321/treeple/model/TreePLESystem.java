/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.27.0.3728.d139ed893 modeling language!*/

package ca.mcgill.ecse321.treeple.model;
import java.util.*;
import java.sql.Time;
import java.sql.Date;

// line 2 "../../../../../model.ump"
public class TreePLESystem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TreePLESystem Associations
  private List<Transaction> transactions;
  private List<Resident> residents;
  private List<Municipality> municipalities;
  private List<Tree> trees;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TreePLESystem()
  {
    transactions = new ArrayList<Transaction>();
    residents = new ArrayList<Resident>();
    municipalities = new ArrayList<Municipality>();
    trees = new ArrayList<Tree>();
  }

  //------------------------
  // INTERFACE
  //------------------------

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

  public Resident getResident(int index)
  {
    Resident aResident = residents.get(index);
    return aResident;
  }

  public List<Resident> getResidents()
  {
    List<Resident> newResidents = Collections.unmodifiableList(residents);
    return newResidents;
  }

  public int numberOfResidents()
  {
    int number = residents.size();
    return number;
  }

  public boolean hasResidents()
  {
    boolean has = residents.size() > 0;
    return has;
  }

  public int indexOfResident(Resident aResident)
  {
    int index = residents.indexOf(aResident);
    return index;
  }

  public Municipality getMunicipality(int index)
  {
    Municipality aMunicipality = municipalities.get(index);
    return aMunicipality;
  }

  public List<Municipality> getMunicipalities()
  {
    List<Municipality> newMunicipalities = Collections.unmodifiableList(municipalities);
    return newMunicipalities;
  }

  public int numberOfMunicipalities()
  {
    int number = municipalities.size();
    return number;
  }

  public boolean hasMunicipalities()
  {
    boolean has = municipalities.size() > 0;
    return has;
  }

  public int indexOfMunicipality(Municipality aMunicipality)
  {
    int index = municipalities.indexOf(aMunicipality);
    return index;
  }

  public Tree getTree(int index)
  {
    Tree aTree = trees.get(index);
    return aTree;
  }

  public List<Tree> getTrees()
  {
    List<Tree> newTrees = Collections.unmodifiableList(trees);
    return newTrees;
  }

  public int numberOfTrees()
  {
    int number = trees.size();
    return number;
  }

  public boolean hasTrees()
  {
    boolean has = trees.size() > 0;
    return has;
  }

  public int indexOfTree(Tree aTree)
  {
    int index = trees.indexOf(aTree);
    return index;
  }

  public static int minimumNumberOfTransactions()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Transaction addTransaction(Time aTime, Date aDate, Resident aResident, Tree aTree)
  {
    return new Transaction(aTime, aDate, aResident, aTree, this);
  }

  public boolean addTransaction(Transaction aTransaction)
  {
    boolean wasAdded = false;
    if (transactions.contains(aTransaction)) { return false; }
    TreePLESystem existingTreePLESystem = aTransaction.getTreePLESystem();
    boolean isNewTreePLESystem = existingTreePLESystem != null && !this.equals(existingTreePLESystem);
    if (isNewTreePLESystem)
    {
      aTransaction.setTreePLESystem(this);
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
    //Unable to remove aTransaction, as it must always have a treePLESystem
    if (!this.equals(aTransaction.getTreePLESystem()))
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

  public static int minimumNumberOfResidents()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Resident addResident(String aName, String aEmail, String aPasswordSalted, Location aPropertyLocation)
  {
    return new Resident(aName, aEmail, aPasswordSalted, aPropertyLocation, this);
  }

  public boolean addResident(Resident aResident)
  {
    boolean wasAdded = false;
    if (residents.contains(aResident)) { return false; }
    TreePLESystem existingTreePLESystem = aResident.getTreePLESystem();
    boolean isNewTreePLESystem = existingTreePLESystem != null && !this.equals(existingTreePLESystem);
    if (isNewTreePLESystem)
    {
      aResident.setTreePLESystem(this);
    }
    else
    {
      residents.add(aResident);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeResident(Resident aResident)
  {
    boolean wasRemoved = false;
    //Unable to remove aResident, as it must always have a treePLESystem
    if (!this.equals(aResident.getTreePLESystem()))
    {
      residents.remove(aResident);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addResidentAt(Resident aResident, int index)
  {  
    boolean wasAdded = false;
    if(addResident(aResident))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfResidents()) { index = numberOfResidents() - 1; }
      residents.remove(aResident);
      residents.add(index, aResident);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveResidentAt(Resident aResident, int index)
  {
    boolean wasAdded = false;
    if(residents.contains(aResident))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfResidents()) { index = numberOfResidents() - 1; }
      residents.remove(aResident);
      residents.add(index, aResident);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addResidentAt(aResident, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfMunicipalities()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Municipality addMunicipality(String aName)
  {
    return new Municipality(aName, this);
  }

  public boolean addMunicipality(Municipality aMunicipality)
  {
    boolean wasAdded = false;
    if (municipalities.contains(aMunicipality)) { return false; }
    TreePLESystem existingTreePLESystem = aMunicipality.getTreePLESystem();
    boolean isNewTreePLESystem = existingTreePLESystem != null && !this.equals(existingTreePLESystem);
    if (isNewTreePLESystem)
    {
      aMunicipality.setTreePLESystem(this);
    }
    else
    {
      municipalities.add(aMunicipality);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeMunicipality(Municipality aMunicipality)
  {
    boolean wasRemoved = false;
    //Unable to remove aMunicipality, as it must always have a treePLESystem
    if (!this.equals(aMunicipality.getTreePLESystem()))
    {
      municipalities.remove(aMunicipality);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addMunicipalityAt(Municipality aMunicipality, int index)
  {  
    boolean wasAdded = false;
    if(addMunicipality(aMunicipality))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfMunicipalities()) { index = numberOfMunicipalities() - 1; }
      municipalities.remove(aMunicipality);
      municipalities.add(index, aMunicipality);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveMunicipalityAt(Municipality aMunicipality, int index)
  {
    boolean wasAdded = false;
    if(municipalities.contains(aMunicipality))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfMunicipalities()) { index = numberOfMunicipalities() - 1; }
      municipalities.remove(aMunicipality);
      municipalities.add(index, aMunicipality);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addMunicipalityAt(aMunicipality, index);
    }
    return wasAdded;
  }

  public static int minimumNumberOfTrees()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Tree addTree(int aDiameter, Location aTreeLocation, Municipality aMunicipality)
  {
    return new Tree(aDiameter, aTreeLocation, aMunicipality, this);
  }

  public boolean addTree(Tree aTree)
  {
    boolean wasAdded = false;
    if (trees.contains(aTree)) { return false; }
    TreePLESystem existingTreePLESystem = aTree.getTreePLESystem();
    boolean isNewTreePLESystem = existingTreePLESystem != null && !this.equals(existingTreePLESystem);
    if (isNewTreePLESystem)
    {
      aTree.setTreePLESystem(this);
    }
    else
    {
      trees.add(aTree);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTree(Tree aTree)
  {
    boolean wasRemoved = false;
    //Unable to remove aTree, as it must always have a treePLESystem
    if (!this.equals(aTree.getTreePLESystem()))
    {
      trees.remove(aTree);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  public boolean addTreeAt(Tree aTree, int index)
  {  
    boolean wasAdded = false;
    if(addTree(aTree))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTrees()) { index = numberOfTrees() - 1; }
      trees.remove(aTree);
      trees.add(index, aTree);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTreeAt(Tree aTree, int index)
  {
    boolean wasAdded = false;
    if(trees.contains(aTree))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTrees()) { index = numberOfTrees() - 1; }
      trees.remove(aTree);
      trees.add(index, aTree);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTreeAt(aTree, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (transactions.size() > 0)
    {
      Transaction aTransaction = transactions.get(transactions.size() - 1);
      aTransaction.delete();
      transactions.remove(aTransaction);
    }
    
    while (residents.size() > 0)
    {
      Resident aResident = residents.get(residents.size() - 1);
      aResident.delete();
      residents.remove(aResident);
    }
    
    while (municipalities.size() > 0)
    {
      Municipality aMunicipality = municipalities.get(municipalities.size() - 1);
      aMunicipality.delete();
      municipalities.remove(aMunicipality);
    }
    
    while (trees.size() > 0)
    {
      Tree aTree = trees.get(trees.size() - 1);
      aTree.delete();
      trees.remove(aTree);
    }
    
  }

}