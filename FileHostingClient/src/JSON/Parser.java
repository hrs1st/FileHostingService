package JSON;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class Parser {
    public JSONObject parser (String jsonInput){
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonInput);
            return (JSONObject) new JSONParser().parse(jsonInput);
        } catch (ParseException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
