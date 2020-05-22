package Structures.AttackTree;

import java.util.ArrayList;

public class Leaf implements AttackTree {
    public int ID;
    public String name;
    public int cost;
    public int time;

    private ArrayList<Gate> parents;

    public Leaf(int ID, String name, int cost, int time) {
        this.ID = ID;
        this.name = name;
        this.cost = cost;
        this.time = time;
        this.parents = new ArrayList<>();
    }

    /**
     * Get name of the Leaf
     * @return name of the leaf
     */
    public String getName() {
        return "\"" + name + "-" + ID + "\"";
    }

    /**
     * Get ID of the Leaf
     * @return ID of the Leaf
     */
    public int getID() {
        return ID;
    }

    /**
     * Get unique name for the Leaf
     * @return unique name for the Leaf
     */
    public String getUniqueName() {
        return "\"" + name + "-" + ID + "\"";
    }

    /**
     * Add parent to the Leaf.
     * @param parent parent of the leaf
     */
    public void addParent(Gate parent) {
        this.parents.add(parent);
    }

    /**
     * Get parents of the Leaf
     * @return parents of th eLeaf
     */
    public ArrayList<Gate> getParents() {
        return parents;
    }

    /**
     * Create a copy of the Leaf
     * @return copy of the Leaf
     */
    public AttackTree copy() {
        return new Leaf(ID, name, cost, time);
    }
}
