/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.26.1-f40f105-3613 modeling language!*/

package ca.mcgill.ecse321.treeple.model;
import java.util.*;

// line 2 "../../../../../TreePLE.ump"
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

  public boolean addTransaction(Transaction aTransaction)
  {
    boolean wasAdded = false;
    if (transactions.contains(aTransaction)) { return false; }
    transactions.add(aTransaction);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTransaction(Transaction aTransaction)
  {
    boolean wasRemoved = false;
    if (transactions.contains(aTransaction))
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

  public boolean addResident(Resident aResident)
  {
    boolean wasAdded = false;
    if (residents.contains(aResident)) { return false; }
    residents.add(aResident);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeResident(Resident aResident)
  {
    boolean wasRemoved = false;
    if (residents.contains(aResident))
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

  public boolean addMunicipality(Municipality aMunicipality)
  {
    boolean wasAdded = false;
    if (municipalities.contains(aMunicipality)) { return false; }
    municipalities.add(aMunicipality);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeMunicipality(Municipality aMunicipality)
  {
    boolean wasRemoved = false;
    if (municipalities.contains(aMunicipality))
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

  public boolean addTree(Tree aTree)
  {
    boolean wasAdded = false;
    if (trees.contains(aTree)) { return false; }
    trees.add(aTree);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTree(Tree aTree)
  {
    boolean wasRemoved = false;
    if (trees.contains(aTree))
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
    transactions.clear();
    residents.clear();
    municipalities.clear();
    trees.clear();
  }

}