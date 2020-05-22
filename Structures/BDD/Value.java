package Structures.BDD;

import java.util.HashMap;

public class Value implements ITE {
    public boolean x;
    public Value(boolean x) {
        this.x = x;
    }

    /**
     * String notation in the ITE structure
     * @return string notation of the end node.
     */
    public String toString() {
        return x ? "1" : "0";
    }

    /**
     * Get the name of the end node
     * @return name of the end node
     */
    public String getName() {
        return toString();
    }

    /**
     * To DOT of the ITE interface. End nodes however do not create any paths.
     * @param counts, counts of each of the nodes
     * @return DOT edges for the leaf
     */
    public String toDOT(HashMap<String, Integer> counts) {
        return "";
    }
}
