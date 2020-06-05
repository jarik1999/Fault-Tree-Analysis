package BDD;

import AttackTree.Ordering;
import Structures.AttackTree.AttackTree;
import Structures.AttackTree.Gate;
import Structures.AttackTree.Leaf;
import Structures.AttackTree.Type;
import AttackTree.AT;
import Structures.BDD.ITE;
import Structures.BDD.Structure;
import Structures.BDD.Value;
import Structures.BDD.Variable;

import java.util.HashMap;

public class BDD {
    private Ordering ordering;
    private ITE bdd;
    private Subsuming subsuming;
    private boolean subsume;

    public BDD(AT at, Ordering ordering, boolean subsume) {
        this.ordering = ordering;
        this.subsuming = new Subsuming(ordering);
        this.subsume = subsume;
        this.bdd = createBDD(at.getAttackTree());
    }

    /**
     * Get ITE structure
     * @return ITE structure
     */
    public ITE getITE() {
        return bdd;
    }

    /**
     * Creates a BDD from a Attack Tree using formulae specified in 'A Fast BDD...' paper
     * @param at, the Attack Tree
     * @return BDD using ITE structure
     */
    private ITE createBDD(AttackTree at) {
        if (at instanceof Leaf) {
            Leaf leaf = (Leaf) at;
            return new Structure(
                new Variable(leaf.ID),
                new Value(true),
                new Value(false)
            );
        }
        Gate gate = (Gate) at;
        ITE left = createBDD(gate.left);
        ITE right = createBDD(gate.right);
        if (subsume) {
            left = subsuming.without(left, right);
        }
        if (gate.type == Type.Sand || gate.type == Type.And) {
            return AND(left, right);
        } else {
            return OR(left, right);
        }


//        Gate gate = (Gate) at;
//        ITE result;
//        if (gate.type == Type.Sand || gate.type == Type.And) {
//            result = AND(createBDD(gate.left), createBDD(gate.right));
//        } else {
//            result = OR(createBDD(gate.left), createBDD(gate.right));
//        }
//
//        // Subsuming
//        if (subsume && (result instanceof Structure)) {
//            Structure temp = (Structure) result;
//            temp.left = subsuming.without(temp.left, temp.right);
//        }
//        return result;
    }

    /**
     * Create a DOT from the BDD
     * @return
     */
    public String toDOT() {

        // Contains amount of each node type
        HashMap<String, Integer> counts = new HashMap<>();
        // Contain the list of attributes
        StringBuilder definitions = new StringBuilder();
        // Contain transitions
        StringBuilder edges = new StringBuilder();

        // Add top level node
        counts.put(bdd.getName(), 1);
        definitions.append(bdd.getName()).append(0).append("[label=\"").append(bdd.getName()).append("\"];\n");
        traverseITE(bdd, counts, definitions, edges);

        return "digraph {\n" + definitions + edges + "}";
    }

    /**
     * Traverse the BDD for the DOT
     * @param ite, current ITE structure in traversal
     * @param counts, how often certain nodes have been reached (used to create different A,B etc. in the BDD to get a tree)
     * @param definitions, definitions for the DOT
     * @param edges, edges for the DOT
     */
    private static void traverseITE(ITE ite, HashMap<String, Integer> counts, StringBuilder definitions, StringBuilder edges) {
        if (ite instanceof Value) return;

        Structure structure = (Structure) ite;
        String leftName = structure.left.getName();
        String rightName = structure.right.getName();

        String ID = ite.getName() + (counts.get(ite.getName()) - 1);

        // Add right subtree
        int rightCount = counts.getOrDefault(rightName, 0);
        counts.put(rightName, rightCount + 1);
        String rightID = rightName + rightCount;
        definitions.append(rightID).append("[label=\"").append(rightName).append("\"];\n");
        edges.append(ID).append("->").append(rightID).append("[label=0];\n");
        traverseITE(structure.right, counts, definitions, edges);

        // Add left subtree
        int leftCount = counts.getOrDefault(leftName, 0);
        counts.put(leftName, leftCount + 1);
        String leftID = leftName + leftCount;
        definitions.append(leftID).append("[label=\"").append(leftName).append("\"];\n");
        edges.append(ID).append("->").append(leftID).append("[label=1];\n");
        traverseITE(structure.left, counts, definitions, edges);
    }

    /**
     * ITE - ITE : combine using equations 9 & 11
     * Bool - ITE : return bool ? ITE : 0
     * Bool - Bool : return bool & bool
     *
     * @param a : Left subtree expression
     * @param b : Right subtree expression
     * @return : Expression for the combined subtrees
     */
    private ITE AND(ITE a, ITE b) {
        if (a instanceof Structure && b instanceof Structure) {
            Structure a1 = (Structure) a;
            Structure b1 = (Structure) b;

            //int compare = a1.x.compareTo(b1.x);
            int compare = ordering.compare(a1.x.x, b1.x.x);
            if (compare < 0) return AND_XY(a1, b1);
            else if (compare == 0) return AND_XX(a1, b1);
            return AND_XY(b1, a1);

        } else if (a instanceof Structure) {
            // ITE - bool
            Structure a1 = (Structure) a;
            Value b1 = (Value) b;
            return b1.x ? a1 : b1;
        } else if (b instanceof Structure) {
            // bool - ITE
            Value a1 = (Value) a;
            Structure b1 = (Structure) b;
            return a1.x ? b1 : a1;
        } else {
            // bool - bool
            Value a1 = (Value) a;
            Value b1 = (Value) b;
            return new Value(a1.x && b1.x);
        }
    }

    /**
     * Equation 9 for combining ITEs with same variable
     *
     * @param a : Left subtree expression
     * @param b : Right subtree expression
     * @return : Expression for the combined subtrees
     */
    private ITE AND_XX(Structure a, Structure b) {
        // ITE(x, (G1H1 + G1H2 + G2H1), G2H2)
        return new Structure(
                a.x,
                OR(OR(AND(a.left, b.left), AND(a.left, b.right)), AND(a.right, b.left)),
                AND(a.right, b.right)
        );
    }

    /**
     * Equation 11 for combining ITEs with different variable
     * Requires: a.x < b.x
     *
     * @param a : Left subtree expression
     * @param b : Right subtree expression
     * @return : Expression for the combined subtrees
     */
    private ITE AND_XY(Structure a, Structure b) {
        // ITE(y, H1, H2)
        ITE h = new Structure(b.x, b.left, b.right);
        // ITE(x, G1h, G2h)
        return new Structure(
                a.x,
                AND(a.left, h),
                AND(a.right, h)
        );
    }

    private ITE OR(ITE a, ITE b) {
        if (a instanceof Structure && b instanceof Structure) {
            Structure a1 = (Structure) a;
            Structure b1 = (Structure) b;

            int compare = ordering.compare(a1.x.x, b1.x.x);
            //int compare = a1.x.compareTo(b1.x);
            if (compare < 0) return OR_XY(a1, b1);
            else if (compare == 0) return OR_XX(a1, b1);
            return OR_XY(b1, a1);
        } else if (a instanceof Structure) {
            // ITE - bool
            Structure a1 = (Structure) a;
            Value b1 = (Value) b;
            return b1.x ? b1 : a1;
        } else if (b instanceof Structure) {
            // bool - ITE
            Value a1 = (Value) a;
            Structure b1 = (Structure) b;
            return a1.x ? a1 : b1;
        } else {
            // bool - bool
            Value a1 = (Value) a;
            Value b1 = (Value) b;
            return new Value(a1.x || b1.x);
        }
    }

    /**
     * Equation 10 for combining ITEs with the same variable
     *
     * @param a : Left subtree expression
     * @param b : Right subtree expression
     * @return : Expression for the combined subtrees
     */
    private ITE OR_XX(Structure a, Structure b) {
        // ITE(x, (G1 + H1), (G2 + H2))
        return new Structure(
                a.x,
                OR(a.left, b.left),
                OR(a.right, b.right)
        );
    }

    /**
     * Equation 12 for combining ITEs with different variable
     *
     * @param a : Left subtree expression
     * @param b : Right subtree expression
     * @return : Expression for the combined subtrees
     */
    private ITE OR_XY(Structure a, Structure b) {
        ITE h = new Structure(b.x, b.left, b.right);
        // ITE(x, G1, (G2 + h))
        return new Structure(
                a.x,
                a.left,
                OR(a.right, h)
        );
    }
}
