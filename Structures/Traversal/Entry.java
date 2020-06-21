package Structures.Traversal;

import Structures.AttackTree.AttackTree;
import Structures.AttackTree.Gate;
import Structures.AttackTree.Leaf;
import Structures.AttackTree.Type;
import Structures.BDD.ITE;
import AttackTree.Ordering;

import java.util.ArrayList;

/**
 * Entry is used in the traversal of the BDD. It contains the location in the BDD, time & cost. When it reaches a 1-node
 * we it contains a valid CS for the AT.
 *
 * Notes:
 * - If not sorted on time, it doesn't calculate time during traversal. This is because it is n^2 while cost is n. Thus,
 * it is more efficient to calculate time once in n after traversal using getTime().
 */
public class Entry implements Comparable<Entry> {
    public EntryOrdering eo;
    public int cost;
    public int time;
    public ITE gate;
    public static final int UNCOMPLETED = -1;
    public int[] X;

    /**
     * Creates an entry in the traversal of a BDD
     * @param cost, current cost of the BASs that were taken
     * @param time, time it took to execute the BASs (calculate afterwards with getTime() if not using a time ordering)
     * @param gate, current gate location in the BDD
     * @param X, X[ID] = time spent on the node. -1 if not executed.
     */
    public Entry(EntryOrdering eo, int cost, int time, ITE gate, int[] X) {
        this.eo = eo;
        this.cost = cost;
        this.time = time;
        this.gate = gate;
        this.X = X;
    }

    /**
     * Complete a BAS
     * @param ID, ID of the BAS
     * @param mapping, mapping of ID -> AttackTree
     */
    public void completeBAS(int ID, AttackTree[] mapping) {
        Leaf leaf = (Leaf) mapping[ID];

        // Update time / cost of leaf
        this.cost += leaf.cost;
        X[ID] = leaf.time;

        if (eo == EntryOrdering.time) {
            this.time = Math.max(time, X[ID]);
            updateTime(leaf);
        }
    }

    private void updateTime(AttackTree node) {
        for (Gate parent: node.getParents()) {
            int ID = parent.getID();
            int leftID = parent.left.getID() ;
            int rightID = parent.right.getID();
            int time = -1;

            // Skip if parent not completed yet
            if (parent.type == Type.Sand || parent.type == Type.And) {
                if (X[leftID] == UNCOMPLETED || X[rightID] == UNCOMPLETED) continue;
            } else {
                if (X[leftID] == UNCOMPLETED && X[rightID] == UNCOMPLETED) continue;
            }

            if (parent.type == Type.Sand) time = X[leftID] + X[rightID];
            if (parent.type == Type.And) time = Math.max(X[leftID], X[rightID]);
            if (parent.type == Type.Or) {
                if (X[leftID] != UNCOMPLETED && X[rightID] != UNCOMPLETED) time = Math.min(X[leftID], X[rightID]);
                else time = Math.max(X[leftID], X[rightID]);
            }


            if (time != X[ID]) {
                X[ID] = time;
                this.time = Math.max(this.time, X[ID]);
                updateTime(parent);
            }

        }
    }

    /**
     * Get time of an Attack Tree. Accounts for DAGs and SAND. Uses the X array to determine whether a BAS was used.
     * @param at, Attack Tree to evaluate
     * @return time it takes for the Attack Tree to evaluate.
     */
    public int getTime(AttackTree at) {
        if (at instanceof Leaf) {
            Leaf leaf = (Leaf) at;
            return X[leaf.ID];
        }
        Gate gate = (Gate) at;
        if (gate.type == Type.Sand) {
            int timeLeft = getTime(gate.left);
            int timeRight = getTime(gate.right);
            if (timeLeft == UNCOMPLETED || timeRight == UNCOMPLETED) return UNCOMPLETED;
            return timeLeft + timeRight;
        } else if (gate.type == Type.And) {
            int timeLeft = getTime(gate.left);
            int timeRight = getTime(gate.right);
            if (timeLeft == UNCOMPLETED || timeRight == UNCOMPLETED) return UNCOMPLETED;
            return Math.max(timeLeft, timeRight);
        } else {
            int timeLeft = getTime(gate.left);
            int timeRight = getTime(gate.right);
            if (timeLeft == UNCOMPLETED && timeRight == UNCOMPLETED) return UNCOMPLETED;
            if (timeLeft != UNCOMPLETED && timeRight != UNCOMPLETED) return Math.min(timeLeft, timeRight);
            return Math.max(timeLeft, timeRight);
        }
    }

    /**
     * Get the completed BASs for the Entry. Checks each of the Leaf nodes to see whether time was spent on them.
     * Note: These are not necessarily in the correct order. For correct order, use the other method and provide
     * the ordering class used for the construction of the BDD.
     *
     * @param mapping, mapping of ID -> Attack Tree
     * @return array of BAS IDs that were completed
     */
    public ArrayList<Integer> getCompleted(AttackTree[] mapping) {
        ArrayList<Integer> result = new ArrayList<>();
        for (AttackTree at: mapping) {
            if (at instanceof Leaf) {
                Leaf leaf = (Leaf) at;
                if (X[leaf.ID] != UNCOMPLETED) result.add(leaf.ID);
            }
        }
        return result;
    }

    /**
     * Get the completed BASs for the Entry. Traverses through the BASs according to the Ordering to check which
     * BASs were involved in the attack to construct the Attack Path.
     *
     * @param mapping, mapping of ID -> Attack Tree
     * @param ordering, ordering of the BASs used in the construction of the BDD
     * @return array of BAS IDs consistent with the Attack Path of the Entry
     */
    public ArrayList<Integer> getCompleted(AttackTree[] mapping, Ordering ordering) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i: ordering.getOrdering()) {
            Leaf leaf = (Leaf) mapping[i];
            if (X[leaf.ID] != UNCOMPLETED) result.add(leaf.ID);
        }
        return result;
    }

    /**
     * Compares an entry to another entry to determine order for the algorithm. Currently sorted on cost.
     * @param e, other entry
     * @return compare result similar to Integer.compare (-1, 0, 1)
     */
    public int compareTo(Entry e) {
        if (eo == EntryOrdering.cost) {
            return Integer.compare(this.cost, e.cost);
        } else if (eo == EntryOrdering.time) {
            return Integer.compare(this.time, e.time);
        } else {
            throw new RuntimeException("Unknown ordering");
        }
    }

    /**
     * String version of the Entry
     * @return string version of the Entry
     */
    public String toString() {
        return "Gate: " + gate.getName() + "\nCost: " + cost + "\nTime: " + time + "\n------";
    }

    /**
     * Creates a copy of the Entry
     * @return copy of the entry
     */
    public Entry copy() {
        return new Entry(eo, cost, time, gate, X.clone());
    }
}
