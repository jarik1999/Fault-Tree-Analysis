package Examples;

import Structures.AttackTree.AttackTree;
import Structures.AttackTree.Gate;
import Structures.AttackTree.Leaf;
import Structures.AttackTree.Type;
import AttackTree.AT;

public class AT_Examples {
    public static AT getAttackTree1() {
        // Fig1 from the paper

        Leaf a = new Leaf(0, "a", 5, 1);
        Leaf b = new Leaf(1,"b", 2, 1);
        Leaf c = new Leaf(2,"c", 2, 2);
        Leaf d = new Leaf(3,"d", 1, 3);

        Gate g2 = new Gate(4, "G2", Type.Or, a, b);
        Gate g3 = new Gate(5,"G3", Type.Or, a, c);
        Gate g1 = new Gate(6,"G1", Type.And, g2, g3);
        Gate top = new Gate(7,"TOP", Type.Or, g1, d);
        return new AT(top);
    }

    public static AT getAttackTree2() {
        // Simple ordering test
        Leaf a = new Leaf(0, "a", 1, 3);
        Leaf b = new Leaf(1,"b", 1, 4);
        Leaf c = new Leaf(2,"c", 1, 2);
        Leaf d = new Leaf(3,"d", 1, 1);
        Leaf e = new Leaf(4,"e", 1, 5);

        Gate g1 = new Gate(5,"G1", Type.Sand, a, b);
        Gate g2 = new Gate(6,"G2", Type.Sand, g1, c);
        Gate g3 = new Gate(7,"G3", Type.Sand, d, e);
        Gate top = new Gate(8,"TOP", Type.And, g2, g3);
        return new AT(top);
    }

    public static AT getAttackTree3() {
        // Tests the Fig 3 example of the paper

        AttackTree a = new Leaf(0, "a", 1, 2);
        AttackTree b = new Leaf(1, "b", 1, 1);
        AttackTree c = new Leaf(2, "c", 1, 2);
        AttackTree d = new Leaf(3, "d", 1, 1);
        AttackTree e = new Leaf(4, "e", 1, 1);

        AttackTree g4 = new Gate(5,"g4", Type.Or, d, e);
        AttackTree g3 = new Gate(6,"g3", Type.And, a, g4);
        AttackTree temp0 = new Gate(7,"temp0", Type.Or, b, c);
        AttackTree temp1 = new Gate(8,"temp1", Type.Or, temp0, d);
        AttackTree g1 = new Gate(9,"g1", Type.Or, temp1, g3);
        AttackTree g2 = new Gate(10,"g2", Type.Or, a, b);
        AttackTree top = new Gate(11,"top", Type.And, g1, g2);
        return new AT(top);
    }

    public static AT getAttackTree4() {
        // Example with Sand and DAG
        AttackTree a = new Leaf(0, "a", 1, 3);
        AttackTree b = new Leaf(1, "b", 9, 5);
        AttackTree c = new Leaf(2, "c", 3, 2);
        AttackTree d = new Leaf(3, "d", 2, 5);
        AttackTree e = new Leaf(4, "e", 1, 3);
        AttackTree f = new Leaf(5, "f", 4, 4);
        AttackTree g = new Leaf(6, "g", 2, 9);

        AttackTree g1 = new Gate(7, "g1", Type.Or, b, c);
        AttackTree g2 = new Gate(8, "g2", Type.And, d, e);
        AttackTree g3 = new Gate(9, "g3", Type.Sand, a, g1);
        AttackTree g4 = new Gate(10, "g4", Type.Or, g1, g2);
        AttackTree g5 = new Gate(11, "g5", Type.And, g3, g4);
        AttackTree g6 = new Gate(12, "g6", Type.Sand, f, g);
        AttackTree top = new Gate(13, "top", Type.Or, g6, g5);
        return new AT(top);
    }

    public static AT getAttackTree5() {
        // Example from Uppaal
        AttackTree bp = new Leaf(0, "bp", 2000, 15);
        AttackTree psc = new Leaf(1, "psc", 0, 7);
        AttackTree hh = new Leaf(2, "hh", 1000, 20);
        AttackTree sb = new Leaf(3, "sb", 0, 0);
        AttackTree heb = new Leaf(4, "heb", 0, 3);
        AttackTree hr = new Leaf(5, "hr", 4000, 10);
        AttackTree reb = new Leaf(6, "reb", 500, 3);
        AttackTree rfc = new Leaf(7, "rfc", 0, 0);
        AttackTree icp = new Leaf(8, "icp", 2000, 15);
        AttackTree dtm = new Leaf(9, "dtm", 1000, 5);

        AttackTree PR1 = new Gate(10, "PR1", Type.Sand, hr, reb);
        AttackTree PR = new Gate(11, "PR", Type.Sand, PR1, rfc);
        AttackTree NA1 = new Gate(12, "NA1", Type.Sand, hh, sb);
        AttackTree NA = new Gate(13, "NA", Type.Sand, heb, NA1);
        AttackTree BR = new Gate(14, "BR", Type.Sand, bp, psc);
        AttackTree SC1 = new Gate(15, "SC1", Type.Or, NA, BR);
        AttackTree SC = new Gate(16, "SC", Type.Or, PR, SC1);
        AttackTree FS1 = new Gate(17, "FS1", Type.Sand, SC, icp);
        AttackTree FSW = new Gate(18, "FSW", Type.Sand, FS1, dtm);
        return new AT(FSW);
    }
}
