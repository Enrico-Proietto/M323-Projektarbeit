import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Application {
    private static final String filePath = "Data/Data.json";
    public static void main(String[] args) {
        printAveragePopulationData("Bern");
    }

    private static void printAveragePopulationData(String nameOfState) {
        try {
            // Read the JSON data from the file
            FileReader fileReader = new FileReader(filePath);
            JsonElement jsonElement = JsonParser.parseReader(fileReader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            fileReader.close();

            // Create a map to store the total population for each state
            Map<String, Integer> stateTotalPopulation = new HashMap<>();
            Map<String, Integer> stateYearCount = new HashMap<>();

            // Iterate through the years (2020, 2030, 2040, 2050)
            for (int year = 2020; year <= 2050; year += 10) {
                String yearKey = String.valueOf(year);

                if (jsonObject.has(yearKey)) {
                    JsonObject yearData = jsonObject.getAsJsonObject(yearKey);

                    // Iterate through locations within the year
                    for (String location : yearData.keySet()) {
                        String populationString = yearData.get(location).getAsString();
                        try {
                            // Parse the population string to an integer
                            int population = NumberFormat.getNumberInstance().parse(populationString).intValue();
                            // Extract the state name from the location
                            String stateName = location.split(" ")[0];

                            // Update the total population and year count for the state
                            stateTotalPopulation.put(stateName, stateTotalPopulation.getOrDefault(stateName, 0) + population);
                            stateYearCount.put(stateName, stateYearCount.getOrDefault(stateName, 0) + 1);
                        } catch (ParseException e) {
                            System.err.println("Error parsing population for " + location);
                        }
                    }
                }
            }

            for (String state : stateTotalPopulation.keySet()) {
                if (state.equals(nameOfState)) {
                    int totalPopulation = stateTotalPopulation.get(state);
                    int yearCount = stateYearCount.get(state);
                    double averagePopulation = (double) totalPopulation / yearCount;
                    System.out.println("The average population of " + nameOfState + " is " + averagePopulation+".");
                    System.out.println("The average is in thousands.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
