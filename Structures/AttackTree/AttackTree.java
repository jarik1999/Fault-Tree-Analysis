package Structures.AttackTree;

import java.util.ArrayList;

public interface AttackTree {
    int getID();
    String getName();
    String getUniqueName();
    void addParent(Gate parent);
    ArrayList<Gate> getParents();
    AttackTree copy();
}
