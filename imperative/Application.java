import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Application {
    private static final String filePath = "Data/Data.json";

    public static void main(String[] args) throws InterruptedException {
        boolean active = true;
        System.out.println("Welcome to EMAS");
        Thread.sleep(1000);
        while (active) {
            System.out.println("What function do you want to use?");
            Thread.sleep(500);
            System.out.println("1 - Average Population for a canton.");
            Thread.sleep(500);
            System.out.println("2 - Percent of changes of all cantons.");
            Thread.sleep(500);
            System.out.println("3 - Percentage of canton population.");
            Thread.sleep(500);
            System.out.println("4 - Exit");
            System.out.println();
            Scanner scanner = new Scanner(System.in);
            switch (scanner.next()) {
                case "1" -> {
                    System.out.println("For which state do you want the data?");
                    printAveragePopulationData(checkCanton(scanner.next()));
                }
                case "2" -> percentageIncreaseOfPopulation();
                case "3" -> getPopulationInCountry();
                case "4" -> {
                    System.out.println("Thank you for using EMAS.");
                    active = false;
                }
            }
        }
    }

    private static JsonObject getData() throws IOException {
        FileReader fileReader = new FileReader(filePath);
        JsonElement jsonElement = JsonParser.parseReader(fileReader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        fileReader.close();
        return jsonObject;
    }

    private static void printAveragePopulationData(String nameOfState) {
        try {
            JsonObject jsonObject = getData();

            Map<String, Integer> cantonTotalPopulation = new HashMap<>();
            Map<String, Integer> cantonYearCount = new HashMap<>();

            for (int year = 2020; year <= 2050; year += 10) {
                String yearKey = String.valueOf(year);

                if (jsonObject.has(yearKey)) {
                    JsonObject yearData = jsonObject.getAsJsonObject(yearKey);

                    for (String location : yearData.keySet()) {
                        String populationString = yearData.get(location).getAsString();
                        try {
                            int population = NumberFormat.getNumberInstance().parse(populationString).intValue();
                            String cantonName = location.split(" ")[0];

                            cantonTotalPopulation.put(cantonName, cantonTotalPopulation.getOrDefault(cantonName, 0) + population);
                            cantonYearCount.put(cantonName, cantonYearCount.getOrDefault(cantonName, 0) + 1);
                        } catch (ParseException e) {
                            System.err.println("Error parsing population for " + location);
                        }
                    }
                }
            }

            for (String canton : cantonTotalPopulation.keySet()) {
                if (canton.equals(nameOfState)) {
                    int totalPopulation = cantonTotalPopulation.get(canton);
                    int yearCount = cantonYearCount.get(canton);
                    double averagePopulation = (double) totalPopulation / yearCount;
                    String realCantonName = getCantonName(nameOfState);
                    System.out.println("The average population of " + realCantonName + " is " + averagePopulation + ".");
                    System.out.println("The average is in thousands.");
                    System.out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void percentageIncreaseOfPopulation() {
        try {

            JsonObject jsonObject = getData();
            Map<String, Map<String, Integer>> statePopulations = new HashMap<>();

            for (int year = 2020; year <= 2050; year += 10) {
                String yearKey = String.valueOf(year);
                if (jsonObject.has(yearKey)) {
                    JsonObject yearData = jsonObject.getAsJsonObject(yearKey);

                    for (String location : yearData.keySet()) {
                        String populationString = yearData.get(location).getAsString();
                        try {
                            int population = NumberFormat.getNumberInstance().parse(populationString).intValue();
                            String stateName = location.split(" ")[0];

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
            System.out.println("Cantons\t\tchange 2020-30\t\tchange 2030-40\t\tchange 2040-50");

            for (String state : statePopulations.keySet()) {
                Map<String, Integer> stateData = statePopulations.get(state);
                double population2020 = stateData.get("2020");
                double population2030 = stateData.get("2030");
                double population2040 = stateData.get("2040");
                double population2050 = stateData.get("2050");

                double percentageChange2020to2030 = ((population2030 - population2020) / population2020) * 100;
                double percentageChange2030to2040 = ((population2040 - population2030) / population2030) * 100;
                double percentageChange2040to2050 = ((population2050 - population2040) / population2040) * 100;
                if (!state.equals("Schweiz")) {
                    System.out.format("%s\t\t\t\t%.2f\t\t\t\t%.2f\t\t\t\t%.2f\n",
                            state,
                            percentageChange2020to2030,
                            percentageChange2030to2040,
                            percentageChange2040to2050
                    );
                }
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getPopulationInCountry() {
        try {

            JsonObject jsonObject = getData();
            Map<String, Map<String, Double>> cantonPopulations = new HashMap<>();

            for (int year = 2020; year <= 2050; year += 10) {
                String yearKey = String.valueOf(year);
                if (jsonObject.has(yearKey)) {
                    JsonObject yearData = jsonObject.getAsJsonObject(yearKey);

                    for (String location : yearData.keySet()) {
                        String populationString = yearData.get(location).getAsString();
                        try {
                            int population = NumberFormat.getNumberInstance().parse(populationString).intValue();
                            String stateName = location.split(" ")[0];

                            if (!cantonPopulations.containsKey(stateName)) {
                                cantonPopulations.put(stateName, new HashMap<>());
                            }
                            cantonPopulations.get(stateName).put(yearKey, (double) population);
                        } catch (ParseException e) {
                            System.err.println("Error parsing population for " + location);
                        }
                    }
                }
            }
            Map<String, Double> countryPopulation = cantonPopulations.get("Schweiz");

            System.out.println("Cantons\t\t2020\t\t\t2030\t\t\t2040\t\t2050");
            System.out.format("%s\t\t%.2f\t\t\t%.2f\t\t\t%.2f\t%.2f\n",
                    "Schweiz",
                    countryPopulation.get("2020"),
                    countryPopulation.get("2030"),
                    countryPopulation.get("2040"),
                    countryPopulation.get("2050")
            );

            for (String canton : cantonPopulations.keySet()) {
                if (!canton.equals("Schweiz")) {
                    System.out.format("%s\t\t\t%.2f\t\t\t%.2f\t\t\t%.2f\t\t%.2f\n",
                            canton,
                            (100 / countryPopulation.get("2020")) * cantonPopulations.get(canton).get("2020"),
                            (100 / countryPopulation.get("2030")) * cantonPopulations.get(canton).get("2030"),
                            (100 / countryPopulation.get("2040")) * cantonPopulations.get(canton).get("2040"),
                            (100 / countryPopulation.get("2050")) * cantonPopulations.get(canton).get("2050")
                            );
                }
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCantonName(String name) {
        switch (name) {
            case "AG" -> { return "Aargau"; }
            case "AI" -> { return "Appenzell I.Rhoden"; }
            case "AR" -> { return "Appenzeller A.Rh"; }
            case "BE" -> { return "Bern"; }
            case "BL" -> { return "Basel-Landschaft"; }
            case "BS" -> { return "Basel-Stadt"; }
            case "FR" -> { return "Freiburg"; }
            case "GE" -> { return "Genf"; }
            case "GL" -> { return "Glarus"; }
            case "GR" -> { return "Graubünden"; }
            case "JU" -> { return "Jura"; }
            case "LU" -> { return "Luzern"; }
            case "NE" -> { return "Neuenburg"; }
            case "NW" -> { return "Nidwalden"; }
            case "OW" -> { return "Obwalden"; }
            case "SG" -> { return "St. Gallen"; }
            case "SH" -> { return "Schaffhausen"; }
            case "SO" -> { return "Solothurn"; }
            case "SZ" -> { return "Zürich"; }
            case "TG" -> { return "Thurgau"; }
            case "TI" -> { return "Tessin"; }
            case "UR" -> { return "Uri"; }
            case "VD" -> { return "Waadt"; }
            case "VS" -> { return "Wallis"; }
            case "ZH" -> { return "Zürich"; }
            case "ZG" -> { return "Zug"; }
        }
        return "state";
    }
    private static String checkCanton(String canton){
        switch (canton){
            case "AG", "ag", "Aargau" -> { return "AG"; }
            case "AI", "Appenzell I.Rhoden", "Appenzell Innerrhoden", "appenzell innerrhoden" -> { return "AI"; }
            case "AR", "ar","Appenzeller A.Rh", "Appenzeller Ausserrhoden", "appenzeller ausserrhoden" -> { return "AR"; }
            case "BE", "be", "Bern", "bern" -> { return "BE"; }
            case "BL", "bl", "Basel-Landschaft", "Baselland", "baselland" -> { return "BL"; }
            case "BS", "bs", "Basel-Stadt", "Baselstadt", "baselstadt" -> { return "BS"; }
            case "FR", "fr", "Freiburg", "freiburg" -> { return "FR"; }
            case "GE", "Genf", "genf" -> { return "GE"; }
            case "GL", "gl", "Glarus", "glarus" -> { return "GL"; }
            case "GR", "gr", "Graubünden", "graubünden" -> { return "GR"; }
            case "JU", "Jura", "jura" -> { return "JU"; }
            case "LU", "Luzern", "luzern" -> { return "LU"; }
            case "NE", "ne", "Neuenburg", "neuenburg" -> { return "NE"; }
            case "NW", "nw", "Nidwalden", "nidwalden" -> { return "NW"; }
            case "OW", "ow", "Obwalden", "obwalden" -> { return "OW"; }
            case "SG", "sg", "St. Gallen", "st. gallen" -> { return "SG"; }
            case "SH", "sh", "Schaffhausen", "schaffhausen" -> { return "SH"; }
            case "SO", "Solothurn", "solothurn" -> { return "SO"; }
            case "SZ","Zürich", "zürich", "zuerich" -> { return "SZ"; }
            case "TG", "tg", "Thurgau", "thurgau" -> { return "TG"; }
            case "TI", "ti", "Tessin", "tessin" -> { return "TI"; }
            case "UR", "ur", "Uri", "uri" -> { return "UR"; }
            case "VD", "vd", "Waadt", "waadt" -> { return "VD"; }
            case "VS" ,"vs", "Wallis", "wallis" -> { return "VS"; }
            case "ZG", "zg", "Zug", "zug" -> { return "ZG"; }
        }
        return "bern";
    }
=======
>>>>>>> 2ff4d02a520c14094416d23f347df02be7a18235
}
