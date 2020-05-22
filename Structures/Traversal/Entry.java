package Structures.Traversal;

import Structures.AttackTree.AttackTree;
import Structures.AttackTree.Gate;
import Structures.AttackTree.Leaf;
import Structures.AttackTree.Type;
import Structures.BDD.ITE;

import java.util.ArrayList;

/**
 * Entry is used in the traversal of the BDD. It contains the location in the BDD, time & cost. When it reaches a 1-node
 * we it contains a valid CS for the AT.
 */
public class Entry implements Comparable<Entry> {
    public int cost;
    public int time;
    public ITE gate;
    public static final int UNCOMPLETED = -1;
    public int[] X;
    //private ArrayList<Integer> completed;

    /**
     * Creates an entry in the traversal of a BDD
     * @param cost, current cost of the BASs that were taken
     * @param time, time it took to execute the BASs(TODO)
     * @param gate, current gate location in the BDD
     * @param X, X[ID] = time spent on the node. -1 if not executed.
     */
    public Entry(int cost, int time, ITE gate, int[] X) {//, ArrayList<Integer> completed) {
        this.cost = cost;
        this.time = time;
        this.gate = gate;
        this.X = X;
        //this.completed = completed;
    }

    /**
     * Complete a BAS
     * @param ID, ID of the BAS
     * @param mapping, mapping of ID -> AttackTree
     */
    public void completeBAS(int ID, AttackTree[] mapping) {
        //completed.add(ID);

        Leaf leaf = (Leaf) mapping[ID];

        // Update time / cost of leaf
        this.cost += leaf.cost;
        X[ID] = leaf.time;
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
     * Compares an entry to another entry to determine order for the algorithm. Currently sorted on cost.
     * @param e, other entry
     * @return compare result similar to Integer.compare (-1, 0, 1)
     */
    public int compareTo(Entry e) {
        return Integer.compare(this.cost, e.cost);
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
     * @return
     */
    public Entry copy() {
        return new Entry(cost, time, gate, X.clone());//, new ArrayList<>(completed));
    }
}
