package Tests;

import AttackTree.Ordering;
import BDD.BDD;
import Examples.AT_Examples;
import AttackTree.AT;


public class BDD_Test {
    public static void main(String[] args) {
        AT at = AT_Examples.getAttackTree3();

        System.out.println(at.toDOT());
        System.out.println("----");

        Ordering ordering = new Ordering(new int[] {1, 0, 4, 3, 2});
        BDD bdd = new BDD(at, ordering, false);
        System.out.println(bdd.toDOT());
    }
}
