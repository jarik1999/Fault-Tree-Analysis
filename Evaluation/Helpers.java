package Evaluation;

import AttackTree.AT;
import Structures.AttackTree.AttackTree;
import Structures.AttackTree.Gate;
import Structures.AttackTree.Type;

public class Helpers {
    public static AT combine(AT x, AT y, Type type) {
        int n = x.getTotalNodes(x.getAttackTree());
        int m = x.getTotalNodes(y.getAttackTree());

        y.incrementIDs(n);

        AttackTree at = new Gate(n + m, "Combine", type, x.getAttackTree(), y.getAttackTree());
        x.getAttackTree().addParent((Gate) at);
        y.getAttackTree().addParent((Gate) at);
        return new AT(at);
    }
}
