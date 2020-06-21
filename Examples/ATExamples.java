package Examples;

import Structures.AttackTree.AttackTree;
import Structures.AttackTree.Gate;
import Structures.AttackTree.Leaf;
import Structures.AttackTree.Type;
import AttackTree.AT;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class ATExamples {
    /**
     * Create an AT from a JSON file.
     * @param location, name of the JSON file in the JSON folder.
     * @return AT of the AT represented in the JSON file
     */
    public static AT openJSON(String location) {
        JSONParser parser = new JSONParser();
        try {
            String path = System.getProperty("user.dir") + "/src/JSON/" + location;
            JSONObject json = (JSONObject) parser.parse(new FileReader(path));

            int n = ((Long) json.get("totalNodes")).intValue();
            AttackTree[] ats = new AttackTree[n];

            JSONArray nodes = (JSONArray) json.get("nodes");
            for (Object obj: nodes) {
                JSONObject node = (JSONObject) obj;

                int ID = ((Long) node.get("ID")).intValue();
                String name = (String) node.get("name");
                String type = (String) node.get("type");
                if (type.equals("Leaf")) {
                    int cost = ((Long) node.get("cost")).intValue();
                    int time = ((Long) node.get("time")).intValue();

                    ats[ID] = new Leaf(ID, name, cost, time);
                } else {
                    int left = ((Long) node.get("left")).intValue();
                    int right = ((Long) node.get("right")).intValue();

                    if (type.equals("Or")) ats[ID] = new Gate(ID, name, Type.Or, ats[left], ats[right]);
                    else if (type.equals("And")) ats[ID] = new Gate(ID, name, Type.And, ats[left], ats[right]);
                    else if (type.equals("Sand")) ats[ID] = new Gate(ID, name, Type.Sand, ats[left], ats[right]);
                }
            }

            return new AT(location, ats[n-1]);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
