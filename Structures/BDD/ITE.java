package Structures.BDD;

import java.util.HashMap;

public interface ITE {
    String toString();
    String toDOT(HashMap<String, Integer> counts);
    String getName();
}
