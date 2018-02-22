/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.26.1-f40f105-3613 modeling language!*/

package ca.mcgill.ecse321.treeple.model;
import java.util.*;

// line 19 "../../../../../model.ump"
public class Municipality
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Municipality Attributes
  private String name;

  //Municipality Associations
  private TreePLESystem treePLESystem;
  private List<Tree> trees;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Municipality(String aName, TreePLESystem aTreePLESystem)
  {
    name = aName;
    boolean didAddTreePLESystem = setTreePLESystem(aTreePLESystem);
    if (!didAddTreePLESystem)
    {
      throw new RuntimeException("Unable to create municipality due to treePLESystem");
    }
    trees = new ArrayList<Tree>();
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

  public String getName()
  {
    return name;
  }

  public TreePLESystem getTreePLESystem()
  {
    return treePLESystem;
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
      existingTreePLESystem.removeMunicipality(this);
    }
    treePLESystem.addMunicipality(this);
    wasSet = true;
    return wasSet;
  }

  public static int minimumNumberOfTrees()
  {
    return 0;
  }

  public Tree addTree(int aDiameter, Location aTreeLocation, TreePLESystem aTreePLESystem)
  {
    return new Tree(aDiameter, aTreeLocation, this, aTreePLESystem);
  }

  public boolean addTree(Tree aTree)
  {
    boolean wasAdded = false;
    if (trees.contains(aTree)) { return false; }
    Municipality existingMunicipality = aTree.getMunicipality();
    boolean isNewMunicipality = existingMunicipality != null && !this.equals(existingMunicipality);
    if (isNewMunicipality)
    {
      aTree.setMunicipality(this);
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
    //Unable to remove aTree, as it must always have a municipality
    if (!this.equals(aTree.getMunicipality()))
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
    TreePLESystem placeholderTreePLESystem = treePLESystem;
    this.treePLESystem = null;
    placeholderTreePLESystem.removeMunicipality(this);
    for(int i=trees.size(); i > 0; i--)
    {
      Tree aTree = trees.get(i - 1);
      aTree.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "treePLESystem = "+(getTreePLESystem()!=null?Integer.toHexString(System.identityHashCode(getTreePLESystem())):"null");
  }
}