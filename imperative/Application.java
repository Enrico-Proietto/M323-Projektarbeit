import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Application {
    private static final String filePath = "Data/Data.json";
    public static void main(String[] args) {

    }

    private JsonArray getData() throws IOException {
        FileReader fileReader = new FileReader(filePath);
        JsonElement jsonElement = JsonParser.parseReader(fileReader);
        fileReader.close();

        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            System.out.println(jsonObject);
        } else if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            return jsonArray;
        } else { System.out.println("JsonElement is not a JsonArray or JsonObject"); }
        return null;
    }

}
