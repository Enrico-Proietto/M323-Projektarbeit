import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * This is EMAS (Einwohner Messung und Analyse System) it outputs the prognoses of the data we found in a useful way for the user.
 * @version 2.0
 * @auther Enrico Proietto & Kento Puleo
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
				case "1" -> {
					System.out.println("For which canton do you want the data?");
					printAveragePopulationData(checkCanton(scanner.next()));
				}
				case "2" -> printPercentageIncreaseOfPopulation();
				case "3" -> printPopulationInCountry();
				case "4" -> {
					System.out.println("Thank you for using EMAS.");
					active = false;
				}
				default -> {
					System.out.println("\033[1;31m" + "The method does not exist" + "\033[0m");
					Thread.sleep(1000);
					System.out.println();
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
	private static void printAveragePopulationData(String cantonName) {
		long begin = System.currentTimeMillis(); // For documentation
			if (!cantonName.equals("canton")) {
				List<Double> listOfPopulations = new ArrayList<>();
				listOfPopulations.add(YEAR_2020.has(cantonName) ? Double.parseDouble(YEAR_2020.get(cantonName).getAsString()) : 0.0);
				listOfPopulations.add(YEAR_2030.has(cantonName) ? Double.parseDouble(YEAR_2030.get(cantonName).getAsString()) : 0.0);
				listOfPopulations.add(YEAR_2040.has(cantonName) ? Double.parseDouble(YEAR_2040.get(cantonName).getAsString()) : 0.0);
				listOfPopulations.add(YEAR_2050.has(cantonName) ? Double.parseDouble(YEAR_2050.get(cantonName).getAsString()) : 0.0);
				double averagePopulation = listOfPopulations.stream()
															.map(population -> population / 4.0)
															.reduce(0.0, (a, b) -> a + b);
				String realCantonName = getCantonName(cantonName);
				System.out.format("The average population of %s is %.2f\n", realCantonName, averagePopulation);
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
	 * This  method shows the user the increase of population from the data file.
	 */
	private static void printPercentageIncreaseOfPopulation() {
		long begin = System.currentTimeMillis(); // For documentation
		List<String> listOfAllCantons = YEAR_2050.entrySet()
										.stream()
										.filter(entry -> !entry.getKey().equals("Schweiz"))
										.map(Map.Entry::getKey)
										.toList();
		System.out.format("%s\t\t%s\t\t%s\t\t%s\n","Canton","Change 2020-30","Change 2030-40","Change 2040-50");
		listOfAllCantons.forEach(canton -> {
				double percentageChange2020To30 = ((YEAR_2030.get(canton).getAsDouble() - YEAR_2020.get(canton).getAsDouble()) / YEAR_2020.get(canton).getAsDouble()) * 100;
				double percentageChange2030To40 = ((YEAR_2040.get(canton).getAsDouble() - YEAR_2030.get(canton).getAsDouble()) / YEAR_2030.get(canton).getAsDouble()) * 100;
				double percentageChange2040To50 = ((YEAR_2050.get(canton).getAsDouble() - YEAR_2040.get(canton).getAsDouble()) / YEAR_2040.get(canton).getAsDouble()) * 100;
				System.out.format("%s\t\t\t\t%.2f\t\t\t\t%.2f\t\t\t\t%.2f\n",canton, percentageChange2020To30, percentageChange2030To40, percentageChange2040To50);
		});
		System.out.println("All data are in percent.");
		long end = System.currentTimeMillis(); // For documentation
		long time = end-begin; // For documentation
		System.out.println("\033[0;36m" + "This  method needed " + time + "ms to output." + "\033[0m"); // For documentation
		System.out.println();
	}
	/**
	 * Outputs a table of every canton and its percentage population of the nation's percentage
	 */
	private static void printPopulationInCountry() {
		long begin = System.currentTimeMillis(); // For documentation
		List<String> cantons = YEAR_2050.entrySet()
				.stream()
				.filter(entry -> !entry.getKey().equals("Schweiz"))
				.map(Map.Entry::getKey)
				.toList();
		System.out.format("%s\t\t%.2f\t\t\t%.2f\t\t\t%.2f\t%.2f\n",
				SCHWEIZ,
				YEAR_2020.get(SCHWEIZ).getAsDouble(),
				YEAR_2030.get(SCHWEIZ).getAsDouble(),
				YEAR_2040.get(SCHWEIZ).getAsDouble(),
				YEAR_2050.get(SCHWEIZ).getAsDouble()
				);
		System.out.println();
		System.out.println("Cantons\t\t2020\t\t\t2030\t\t\t2040\t\t2050");
		cantons.forEach(canton -> System.out.format("%s\t\t\t%.2f\t\t\t%.2f\t\t\t%.2f\t\t%.2f\n",
				canton,
				((100 / YEAR_2020.get(SCHWEIZ).getAsDouble()) * YEAR_2020.get(canton).getAsDouble()),
				((100 / YEAR_2030.get(SCHWEIZ).getAsDouble()) * YEAR_2030.get(canton).getAsDouble()),
				((100 / YEAR_2040.get(SCHWEIZ).getAsDouble()) * YEAR_2040.get(canton).getAsDouble()),
				((100 / YEAR_2050.get(SCHWEIZ).getAsDouble()) * YEAR_2050.get(canton).getAsDouble())
		));
		System.out.println("All data are in percent.");
		long end = System.currentTimeMillis(); // For documentation
		long time = end-begin; // For documentation
		System.out.println("\033[0;36m" + "This  method needed " + time + "ms to output." + "\033[0m"); // For documentation
		System.out.println();
	}
	/**
	 * For better understanding we output the real canton name instead of the abbreviation.
	 * @param abbreviationOfCanton Abbreviation of a canton
	 * @Link getAveragePopulationData(String)
	 * @return The name of the abbreviation chosen by the user
	 */
	private static String getCantonName(String abbreviationOfCanton) {
		return switch (abbreviationOfCanton) {
			case "AG" -> "Aargau";
			case "AI" -> "Appenzell I.Rhoden";
			case "AR" -> "Appenzeller A.Rh";
			case "BE" -> "Bern";
			case "BL" -> "Basel-Landschaft";
			case "BS" -> "Basel-Stadt";
			case "FR" -> "Freiburg";
			case "GE" -> "Genf";
			case "GL" -> "Glarus";
			case "GR" -> "Graubünden";
			case "JU" -> "Jura";
			case "LU" -> "Luzern";
			case "NE" -> "Neuenburg";
			case "NW" -> "Nidwalden";
			case "OW" -> "Obwalden";
			case "SG" -> "St. Gallen";
			case "SH" -> "Schaffhausen";
			case "SO" -> "Solothurn";
			case "SZ" -> "Zürich";
			case "TG" -> "Thurgau";
			case "TI" -> "Tessin";
			case "UR" -> "Uri";
			case "VD" -> "Waadt";
			case "VS" -> "Wallis";
			case "ZH" -> "Zürich";
			case "ZG" -> "Zug";
			default -> "canton";
		};
	}

	/**
	 * Validates the input of the user
	 * @param canton Input from the user to look at a specific canton
	 * @return the abbreviation chosen by the user
	 */
	private static String checkCanton(String canton){
	return switch (canton){
			case "AG", "ag", "Aargau" ->  "AG";
			case "AI", "Appenzell I.Rhoden", "Appenzell Innerrhoden", "appenzell innerrhoden" -> "AI"; 
			case "AR", "ar","Appenzeller A.Rh", "Appenzeller Ausserrhoden", "appenzeller ausserrhoden" -> "AR"; 
			case "BE", "be", "Bern", "bern" -> "BE"; 
			case "BL", "bl", "Basel-Landschaft", "Baselland", "baselland" -> "BL"; 
			case "BS", "bs", "Basel-Stadt", "Baselstadt", "baselstadt" -> "BS"; 
			case "FR", "fr", "Freiburg", "freiburg" -> "FR"; 
			case "GE", "Genf", "genf" -> "GE"; 
			case "GL", "gl", "Glarus", "glarus" -> "GL"; 
			case "GR", "gr", "Graubünden", "graubünden" -> "GR"; 
			case "JU", "Jura", "jura" -> "JU"; 
			case "LU", "Luzern", "luzern" -> "LU"; 
			case "NE", "ne", "Neuenburg", "neuenburg" -> "NE"; 
			case "NW", "nw", "Nidwalden", "nidwalden" -> "NW"; 
			case "OW", "ow", "Obwalden", "obwalden" -> "OW"; 
			case "SG", "sg", "St. Gallen", "st. gallen" -> "SG"; 
			case "SH", "sh", "Schaffhausen", "schaffhausen" -> "SH"; 
			case "SO", "Solothurn", "solothurn" -> "SO"; 
			case "SZ","Zürich", "zürich", "zuerich" -> "SZ"; 
			case "TG", "tg", "Thurgau", "thurgau" -> "TG"; 
			case "TI", "ti", "Tessin", "tessin" -> "TI"; 
			case "UR", "ur", "Uri", "uri" -> "UR"; 
			case "VD", "vd", "Waadt", "waadt" -> "VD"; 
			case "VS" ,"vs", "Wallis", "wallis" -> "VS"; 
			case "ZG", "zg", "Zug", "zug" -> "ZG";
			default -> "canton";
		};
	}
}
