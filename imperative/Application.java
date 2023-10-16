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
        /*printAveragePopulationData("Bern");*/
        printDataOf();
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

    private static void printDataOf(){
        try {
            // Read the JSON data from the file
            FileReader fileReader = new FileReader(filePath);
            JsonElement jsonElement = JsonParser.parseReader(fileReader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            fileReader.close();

            // Create a map to store the population for each state in each year
            Map<String, Map<String, Integer>> statePopulations = new HashMap<>();

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

                            // Update the population for the state and year
                            if (!statePopulations.containsKey(stateName)) {
                                statePopulations.put(stateName, new HashMap<>());
                            }
                            statePopulations.get(stateName).put(yearKey, population);
                        } catch (ParseException e) {
                            System.err.println("Error parsing population for " + location);
                        }
                    }
                }
            }

            // Calculate and print the percentage change for each state
            for (String state : statePopulations.keySet()) {
                Map<String, Integer> stateData = statePopulations.get(state);
                int population2030 = stateData.get("2030");
                int population2040 = stateData.get("2040");
                int population2050 = stateData.get("2050");

                double percentageChange2030to2040 = ((double) (population2040 - population2030) / population2030) * 100;
                double percentageChange2040to2050 = ((double) (population2050 - population2040) / population2040) * 100;

                System.out.println(state + " - Population Change 2030-2040: " + percentageChange2030to2040 + "%");
                System.out.println(state + " - Population Change 2040-2050: " + percentageChange2040to2050 + "%");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
