package Evaluation;

import AttackTree.AT;
import Structures.AttackTree.Gate;
import Structures.AttackTree.Type;

class Helpers {
    static AT combine(AT x, AT y, Type type) {
        int n = x.getTotalNodes(x.getAttackTree());
        int m = x.getTotalNodes(y.getAttackTree());

        y.incrementIDs(n);

        Gate at = new Gate(n + m, "Combine", type, x.getAttackTree(), y.getAttackTree());
        return new AT("Combined AT", at);
    }
}
