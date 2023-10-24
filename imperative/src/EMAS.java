package src;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is src.EMAS (Einwohner Messung und Analyse System) it outputs the prognoses of the data we found in a useful way for the user.
 * @version 1.0
 * @auther Kento Puleo & Enrico Proietto
 */
public class EMAS {
    private static final String FILE_PATH = "Data/Data.json";
    private static final String SCHWEIZ = "Schweiz";
    private static final JsonObject JSON_OBJECT = getData();
    private static final JsonObject YEAR_2020 = JSON_OBJECT.getAsJsonObject("2020");
    private static final JsonObject YEAR_2030 = JSON_OBJECT.getAsJsonObject("2030");
    private static final JsonObject YEAR_2040 = JSON_OBJECT.getAsJsonObject("2040");
    private static final JsonObject YEAR_2050 = JSON_OBJECT.getAsJsonObject("2050");
    /**
     * main method for the user interaction
     * @param args Normal String array for the main method.
     * @throws InterruptedException Stops the output for some milliseconds
	 */
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
            System.out.println("3 - Percentage of population for all cantons.");
            Thread.sleep(500);
            System.out.println("4 - Exit");
            System.out.println();
            Scanner scanner = new Scanner(System.in);
            switch (scanner.next()) {
                case "1" : {
                    System.out.println("For which canton do you want the data?");
                    getAveragePopulationData(checkCanton(scanner.next()));
                    break;
                }
                case "2" : {
                    getPercentageIncreaseOfPopulation();
                    break;
                }
                case "3" : {
                    getPopulationInCountry();
                    break;
                }
                case "4" : {
                    System.out.println("Thank you for using EMAS.");
                    active = false;
                    break;
                }
                default : {
                    System.out.println("\033[1;31m" + "The method does not exist" + "\033[0m");
                    Thread.sleep(1000);
                    System.out.println();
                    break;
                }
            }
        }
    }

    /**
     * The data are prognoses of population increase for the years 2020 to 2050. Find more about the data here: <a href="https://www.bfs.admin.ch/bfs/de/home/statistiken/bevoelkerung/zukuenftige-entwicklung/kantonale-szenarien.html">...</a>
     * @return JSON data from the Swiss Federal Office for Statistics
     * @Link Data/Data.json
     */
    private static JsonObject getData() {
        try {
            FileReader fileReader = new FileReader(FILE_PATH);
            JsonElement jsonElement = JsonParser.parseReader(fileReader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            fileReader.close();
            return jsonObject;
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
        }
        return null;
    }

    /**
     * This method shows the average population of a canton of his choice.
     * @param cantonName The name of a specific canton chosen by the user.
     * @Link getData()
     */
    private static void getAveragePopulationData(String cantonName) {
        long begin = System.currentTimeMillis(); // For documentation
        if (!cantonName.equals("canton")) {
            List<Double> listOfPopulation = new ArrayList<>();
            double result = 0;
            listOfPopulation.add(YEAR_2020.get(cantonName).getAsDouble());
            listOfPopulation.add(YEAR_2030.get(cantonName).getAsDouble());
            listOfPopulation.add(YEAR_2040.get(cantonName).getAsDouble());
            listOfPopulation.add(YEAR_2050.get(cantonName).getAsDouble());
            for (double population : listOfPopulation){
                result += population;
            }
            result = result/4;
            String realCantonName = getCantonName(cantonName);
            System.out.format("The average population of %s is %.2f\n", realCantonName, result);
            System.out.println("The average is in thousands.");
            long end = System.currentTimeMillis(); // For documentation
            long time = end-begin; // For documentation
            System.out.println("\033[0;36m" + "This method needed " + time + "ms to output." + "\033[0m"); // For documentation
            System.out.println();
        } else {
            System.out.println( "\033[0;31m" + "This canton does not exist. Please be sure you enter a correct canton name." + "\033[0m");
            System.out.println();
        }
    }

    /**
     * This method shows the user the increase of population from the data file.
     */
    private static void getPercentageIncreaseOfPopulation() {
        long begin = System.currentTimeMillis(); // For documentation
        List<String> listOfAllCantons = new ArrayList<>();
        for (String canton : YEAR_2020.keySet()){
            if (!canton.equals("Schweiz")){
                listOfAllCantons.add(canton);
            }
        }
        System.out.format("%s\t\t%s\t\t%s\t\t%s\n","Canton","Change 2020-30","Change 2030-40","Change 2040-50");
        for (String canton : listOfAllCantons){
            System.out.format("%s\t\t\t\t%.2f\t\t\t\t%.2f\t\t\t\t%.2f\n",
                    canton,
                    ((YEAR_2030.get(canton).getAsDouble() - YEAR_2020.get(canton).getAsDouble()) / YEAR_2020.get(canton).getAsDouble()) * 100,
                    ((YEAR_2040.get(canton).getAsDouble() - YEAR_2030.get(canton).getAsDouble()) / YEAR_2030.get(canton).getAsDouble()) * 100,
                    ((YEAR_2050.get(canton).getAsDouble() - YEAR_2040.get(canton).getAsDouble()) / YEAR_2040.get(canton).getAsDouble()) * 100
            );
        };
        System.out.println("All data are in percent.");
        long end = System.currentTimeMillis(); // For documentation
        long time = end-begin; // For documentation
        System.out.println( "\033[0;36m" + "This method needed " + time + "ms to output." + "\033[0m" ); // For documentation
        System.out.println();
    }


    /**
     * Outputs a table of every canton and its percentage population of the nation's percentage
     */
    private static void getPopulationInCountry(){
        long begin = System.currentTimeMillis(); // For documentation
        List<String> cantons = new ArrayList<>();
        for (String canton : YEAR_2020.keySet()){
            if (!canton.equals("Schweiz")){
                cantons.add(canton);
            }
        }
        System.out.format("%s\t\t%.2f\t\t\t%.2f\t\t\t%.2f\t%.2f\n",
                SCHWEIZ,
                YEAR_2020.get(SCHWEIZ).getAsDouble(),
                YEAR_2030.get(SCHWEIZ).getAsDouble(),
                YEAR_2040.get(SCHWEIZ).getAsDouble(),
                YEAR_2050.get(SCHWEIZ).getAsDouble()
        );
        System.out.println();
        System.out.println("Cantons\t\t2020\t\t\t2030\t\t\t2040\t\t2050");
        for (String canton : cantons){
            System.out.format("%s\t\t\t%.2f\t\t\t%.2f\t\t\t%.2f\t\t%.2f\n",
                    canton,
                    ((100 / YEAR_2020.get(SCHWEIZ).getAsDouble()) * YEAR_2020.get(canton).getAsDouble()),
                    ((100 / YEAR_2030.get(SCHWEIZ).getAsDouble()) * YEAR_2030.get(canton).getAsDouble()),
                    ((100 / YEAR_2040.get(SCHWEIZ).getAsDouble()) * YEAR_2040.get(canton).getAsDouble()),
                    ((100 / YEAR_2050.get(SCHWEIZ).getAsDouble()) * YEAR_2050.get(canton).getAsDouble())
            );
        }
        System.out.println("All data are in percent.");
        long end = System.currentTimeMillis(); // For documentation
        long time = end-begin; // For documentation
        System.out.println( "\033[0;36m" + "This method needed " + time + "ms to output." + "\033[0m" ); // For documentation
        System.out.println();
    }

    /**
     * For better understanding we output the real canton name instead of the abbreviation.
     * @param abbreviationOfCanton Abbreviation of a canton
     * @Link getAveragePopulationData(String)
     * @return The name of the abbreviation chosen by the user
     */
    private static String getCantonName(String abbreviationOfCanton) {
        switch (abbreviationOfCanton) {
            case "AG" : { return "Aargau"; }
            case "AI" : { return "Appenzell I.Rhoden"; }
            case "AR" : { return "Appenzeller A.Rh"; }
            case "BE" : { return "Bern"; }
            case "BL" : { return "Basel-Landschaft"; }
            case "BS" : { return "Basel-Stadt"; }
            case "FR" : { return "Freiburg"; }
            case "GE" : { return "Genf"; }
            case "GL" : { return "Glarus"; }
            case "GR" : { return "Graubünden"; }
            case "JU" : { return "Jura"; }
            case "LU" : { return "Luzern"; }
            case "NE" : { return "Neuenburg"; }
            case "NW" : { return "Nidwalden"; }
            case "OW" : { return "Obwalden"; }
            case "SG" : { return "St. Gallen"; }
            case "SH" : { return "Schaffhausen"; }
            case "SO" : { return "Solothurn"; }
            case "SZ" : { return "Zürich"; }
            case "TG" : { return "Thurgau"; }
            case "TI" : { return "Tessin"; }
            case "UR" : { return "Uri"; }
            case "VD" : { return "Waadt"; }
            case "VS" : { return "Wallis"; }
            case "ZH" : { return "Zürich"; }
            case "ZG" : { return "Zug"; }
        }
        return "canton";
    }

    /**
     * Validates the input of the user
     * @param canton Input from the user to look at a specific canton
     * @return the abbreviation chosen by the user
     */
    private static String checkCanton(String canton){
        switch (canton){
            case "AG", "ag", "Aargau" : { return "AG"; }
            case "AI", "Appenzell I.Rhoden", "Appenzell Innerrhoden", "appenzell innerrhoden" : { return "AI"; }
            case "AR", "ar","Appenzeller A.Rh", "Appenzeller Ausserrhoden", "appenzeller ausserrhoden" : { return "AR"; }
            case "BE", "be", "Bern", "bern" : { return "BE"; }
            case "BL", "bl", "Basel-Landschaft", "Baselland", "baselland" : { return "BL"; }
            case "BS", "bs", "Basel-Stadt", "Baselstadt", "baselstadt" : { return "BS"; }
            case "FR", "fr", "Freiburg", "freiburg" : { return "FR"; }
            case "GE", "Genf", "genf" : { return "GE"; }
            case "GL", "gl", "Glarus", "glarus" : { return "GL"; }
            case "GR", "gr", "Graubünden", "graubünden" : { return "GR"; }
            case "JU", "Jura", "jura" : { return "JU"; }
            case "LU", "Luzern", "luzern" : { return "LU"; }
            case "NE", "ne", "Neuenburg", "neuenburg" : { return "NE"; }
            case "NW", "nw", "Nidwalden", "nidwalden" : { return "NW"; }
            case "OW", "ow", "Obwalden", "obwalden" : { return "OW"; }
            case "SG", "sg", "St. Gallen", "st. gallen" : { return "SG"; }
            case "SH", "sh", "Schaffhausen", "schaffhausen" : { return "SH"; }
            case "SO", "Solothurn", "solothurn" : { return "SO"; }
            case "SZ","Zürich", "zürich", "zuerich" : { return "SZ"; }
            case "TG", "tg", "Thurgau", "thurgau" : { return "TG"; }
            case "TI", "ti", "Tessin", "tessin" : { return "TI"; }
            case "UR", "ur", "Uri", "uri" : { return "UR"; }
            case "VD", "vd", "Waadt", "waadt" : { return "VD"; }
            case "VS" ,"vs", "Wallis", "wallis" : { return "VS"; }
            case "ZG", "zg", "Zug", "zug" : { return "ZG"; }
        }
        return "canton";
    }
}
