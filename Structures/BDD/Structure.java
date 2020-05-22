package Structures.BDD;

import java.util.HashMap;

public class Structure implements ITE {
    public Variable x;
    // 1-Path
    public ITE left;
    // 0-Path
    public ITE right;

    public Structure(Variable x, ITE left, ITE right) {
        this.x = x;
        this.left = left;
        this.right = right;
    }

    /**
     * Get the name of the ITE structure
     * @return name of the ITE structure
     */
    public String getName() {
        return x.toString();
    }

    /**
     * Get a string version of the ITE structure as seen in the paper
     * @return string version of the ITE structure
     */
    public String toString() {
        return "ite(" + ((char) (x.x + 'a'))  + "," + left.toString() + "," + right.toString() + ")" ;
    }

    /**
     * Creates a DOT presentation of the ITE structure
     * @param counts, current counts of the nodes to create a tree structure instead of a DAG
     * @return DOT representation of the ITE structure
     */
    public String toDOT(HashMap<String, Integer> counts) {
        int xCount = counts.getOrDefault(x.toString(), 0);
        counts.put(x.toString(), xCount + 1);
        int lCount = counts.getOrDefault(left.getName(), 0);
        counts.put(left.getName(), lCount + 1);
        int rCount = counts.getOrDefault(right.getName(), 0);
        counts.put(right.getName(), rCount + 1);

        String xID = x.toString() + xCount;
        String lID = left.getName() + lCount;
        String rID = right.getName() + rCount;

        String oneTransition = xID + " -> " + lID + "[ label = 1 ];\n";
        String zeroTransition = xID + " -> " + rID + "[ label = 0];\n";

        return oneTransition + zeroTransition + left.toDOT(counts) + right.toDOT(counts);
    }
}
