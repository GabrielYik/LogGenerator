package logen;

import static logen.storage.Config.FILE_EXTENSION;
import static logen.storage.Config.LOG_DIR_PATH;
import static logen.storage.Config.SCENARIO_DIR_PATH;
import static logen.storage.Config.SCENARIO_DIR_PATH_JAR;
import static logen.storage.Config.scenarioFileFilter;
import static logen.util.PathUtil.SCENARIO_FILE_FILTER;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import logen.generation.LogGenerator;
import logen.log.Log;
import logen.storage.Scenario;
import logen.util.PathUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final int EXIT_COMMAND = -1;
    private static final int SCENARIO_OUT_OF_BOUNDS = -2;
    private static final int UNKNOWN_INPUT = -3;

    private static final int MIN_SCENARIO_CHOICE = 1;

    public static void main(String[] args) throws IOException {
        List<String> scenariosFileNames = fetchScenariosFileNames();
        displayScenarioChoices(scenariosFileNames);
        int scenarioChoice = 0;
        boolean toContinue = true;
        do {
            String userInput = getUserInput();
            int result = handleUserInput(userInput, MIN_SCENARIO_CHOICE, scenariosFileNames.size());
            if (result == EXIT_COMMAND) {
                System.exit(0);
            } else if (result == SCENARIO_OUT_OF_BOUNDS || result == UNKNOWN_INPUT) {
                // loop again
            } else {
                scenarioChoice = result;
                toContinue = false;
            }
        } while (toContinue);
        Scenario scenario = readScenario(scenariosFileNames.get(scenarioChoice - 1));

        LogGenerator logGenerator = new LogGenerator(scenario);
        List<Log> logs = logGenerator.generate();

        List<String> headers = scenario.getHeaders();
        writeAsCsv(headers, logs);
    }

    private static List<String> fetchScenariosFileNames() throws IOException {
        try {
            return fetchScenariosFileNames(SCENARIO_DIR_PATH);
        } catch (NoSuchFileException e) {
            try {
                return fetchScenariosFileNames(SCENARIO_DIR_PATH_JAR);
            } catch (NoSuchFileException f) {
                System.out.println("No folder named scenarios");
                System.out.println("Creating folder now");
                System.out.println("...");
                Files.createDirectory(Paths.get(SCENARIO_DIR_PATH_JAR));
                System.out.println("Folder created");
                return Collections.emptyList();
            }
        }
    }

    private static List<String> fetchScenariosFileNames(String scenarioDirectoryPath) throws IOException {
        List<String> scenarios = new ArrayList<>();
        Path scenarioDirPath = new File(scenarioDirectoryPath).toPath();
        Files.newDirectoryStream(scenarioDirPath, SCENARIO_FILE_FILTER)
            .forEach(p -> scenarios.add(PathUtil.toFileName(p)));
        return scenarios;
    }

    private static void displayScenarioChoices(List<String> scenariosFileNames) {
        Collections.sort(scenariosFileNames);
        System.out.println();
        System.out.println("Scenarios Available");
        for (int listing = 1; listing <= scenariosFileNames.size(); listing++) {
            System.out.println(
                "("
                    + listing
                    + ") "
                    + scenariosFileNames.get(listing - 1));
        }
    }

    private static String getUserInput() {
        System.out.println();
        System.out.println("Options");
        System.out.println("- select a scenario file to use by entering its number");
        System.out.println("- exit the program by entering \"exit\"");

        System.out.println();
        System.out.print("Enter input: ");
        System.out.flush();

        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    private static int handleUserInput(String userInput, int minScenarioChoice, int maxScenarioChoice) {
        if (userInput.equalsIgnoreCase("exit")) {
            return EXIT_COMMAND;
        } else {
            try {
                int scenarioChoice = Integer.parseInt(userInput);
                if (scenarioChoice < minScenarioChoice || scenarioChoice > maxScenarioChoice) {
                    System.out.println("No such scenario");
                    return SCENARIO_OUT_OF_BOUNDS;
                } else {
                    return scenarioChoice;
                }
            } catch (NumberFormatException e) {
                System.out.println("Unknown input");
                return UNKNOWN_INPUT;
            }
        }
    }

    private static Scenario readScenario(String scenarioFileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File scenarioFile = new File(SCENARIO_DIR_PATH + scenarioFileName + FILE_EXTENSION);
        return objectMapper.readValue(scenarioFile, Scenario.class);
    }

    private static void writeAsCsv(List<String> headers, List<Log> logs) throws IOException {
        if (!Files.exists(Paths.get(LOG_DIR_PATH))) {
            Files.createDirectory(Paths.get(LOG_DIR_PATH));
        }
        FileWriter writer = new FileWriter(LOG_DIR_PATH + "log.csv");
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.EXCEL.withHeader(headers.toArray(new String[]{})));
        logs.forEach(log -> {
            try {
                printer.printRecord(log.toArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        printer.flush();
        printer.close();
    }
}
