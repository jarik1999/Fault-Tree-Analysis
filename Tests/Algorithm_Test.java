package Tests;

import Algorithms.Algorithm;
import AttackTree.AT;
import Examples.AT_Examples;
import Structures.Traversal.Entry;

import java.util.ArrayList;

public class Algorithm_Test {
    public static void main(String[] args) {
        AT at = AT_Examples.getAttackTree5();
        Algorithm.evaluate(at, true);


    }


}
