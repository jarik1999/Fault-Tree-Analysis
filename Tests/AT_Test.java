package Tests;

import AttackTree.AT;
import AttackTree.Ordering;
import BDD.BDD;
import Examples.AT_Examples;

public class AT_Test {
    public static void main(String[] args) {
        // Fig1 from the paper
        AT at = AT_Examples.getAttackTree5();
        System.out.println(at.toDOT());
        System.out.println(at.toUPPAAL());

        Ordering ordering = at.attackTreeOrdering();
        BDD bdd = new BDD(at, ordering);
        System.out.println(bdd.toDOT());


    }
}
