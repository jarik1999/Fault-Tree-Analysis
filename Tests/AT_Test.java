package Tests;

import AttackTree.AT;
import AttackTree.Ordering;
import BDD.BDD;
import Examples.AT_Examples;

public class AT_Test {
    public static void main(String[] args) {
        AT at = AT_Examples.openJSON("Uppaal_Example.json");
        System.out.println(at.toDOT());
        System.out.println(at.toUPPAAL());

        Ordering ordering = at.attackTreeOrdering();
        BDD bdd = new BDD(at, ordering, false);
        System.out.println(bdd.toDOT());


    }
}
