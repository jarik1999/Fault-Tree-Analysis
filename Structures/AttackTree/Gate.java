package Structures.AttackTree;

import java.util.ArrayList;

public class Gate implements AttackTree {
    public int ID;
    public Type type;
    public String name;
    public AttackTree left;
    public AttackTree right;

    private ArrayList<Gate> parents;

    public Gate(int ID, String name, Type type, AttackTree left, AttackTree right) {
        this.ID = ID;
        this.name = name;
        this.type = type;

        this.left = left;
        this.left.addParent(this);

        this.right = right;
        this.right.addParent(this);

        this.parents = new ArrayList<>();
    }

    /**
     * Get a name of the Gate
     * @return name of the Gate
     */
    public String getName() {
        return "\"" + name + "-" + type + "\"";
    }

    /**
     * Get a unique name for the Gate including ID
     * @return unique name of the Gate
     */
    public String getUniqueName() {
        return "\"" + name + "-" + type + "-" + ID + "\"";
    }

    /**
     * Get the ID of the Gate
     * @return ID of the Gate
     */
    public int getID() {
        return ID;
    }

    /**
     * Get parents of the Gate
     * @return list of parents of the Gate
     */
    public ArrayList<Gate> getParents() {
        return parents;
    }

    /**
     * Add a parent to the Gate. Used in construction.
     * @param parent, parent to add
     */
    public void addParent(Gate parent) {
        this.parents.add(parent);
    }

    /**
     * Creates a copy of the Gate
     * @return copy of the Gate
     */
    public AttackTree copy() {
        return new Gate(ID, name, type, left.copy(), right.copy());
    }
}
