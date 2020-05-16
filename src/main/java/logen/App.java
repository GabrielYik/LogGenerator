package logen;

import static logen.storage.Config.LOG_FILE_EXTENSION;
import static logen.storage.Config.SCENARIO_FILE_EXTENSION;
import static logen.storage.Config.LOG_DIR_PATH;
import static logen.storage.Config.SCENARIO_DIR_PATH;
import static logen.storage.Config.SCENARIO_DIR_PATH_JAR;
import static logen.storage.Config.SCENARIO_FILE_PREFIX;
import static logen.util.PathUtil.SCENARIO_FILE_FILTER;

import com.fasterxml.jackson.core.JsonParser;
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
    private static final int ALL_COMMAND = -2;
    private static final int SCENARIO_OUT_OF_BOUNDS = -3;
    private static final int UNKNOWN_INPUT = -4;

    private static final int MIN_SCENARIO_CHOICE = 1;

    public static void main(String[] args) throws IOException {
        List<String> scenariosFileNames = fetchScenariosFileNames();
        displayScenarioChoices(scenariosFileNames);
        handleUserRequests(scenariosFileNames);
    }

    private static List<String> fetchScenariosFileNames() throws IOException {
        try {
            return fetchScenariosFileNames(SCENARIO_DIR_PATH);
        } catch (NoSuchFileException e) {
            try {
                return fetchScenariosFileNames(SCENARIO_DIR_PATH_JAR);
            } catch (NoSuchFileException f) {
                createEmptyScenariosFolder();
                return Collections.emptyList();
            }
        }
    }

    private static List<String> fetchScenariosFileNames(String scenarioDirectoryPath) throws IOException {
        List<String> scenarios = new ArrayList<>();
        Path scenarioDirPath = new File(scenarioDirectoryPath).toPath();
        Files.newDirectoryStream(scenarioDirPath, SCENARIO_FILE_FILTER)
            .forEach(p -> scenarios.add(PathUtil.toFileNameWithoutExtension(p)));
        return scenarios;
    }

    private static void createEmptyScenariosFolder() throws IOException {
        System.out.println("No folder named \"scenarios\"");
        System.out.println("Creating folder now");
        System.out.println("...");
        Files.createDirectory(Paths.get(SCENARIO_DIR_PATH_JAR));
        System.out.println("Folder created");
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

    private static void handleUserRequests(List<String> scenariosFileNames) throws IOException {
        boolean toContinue = true;
        do {
            String userInput = getUserInput();
            int result = handleUserInput(userInput, scenariosFileNames.size());

            switch (result) {
            case EXIT_COMMAND:
                System.out.println("Goodbye");
                toContinue = false;
                break;
            case ALL_COMMAND:
                for (String scenarioFileName : scenariosFileNames) {
                    Scenario scenario = readScenario(scenarioFileName);

                    LogGenerator logGenerator = new LogGenerator(scenario);
                    List<Log> logs = logGenerator.generate();
                    List<String> headers = scenario.getHeaders();

                    writeAsCsv(headers, logs, scenarioFileName);
                    System.out.println("File generated.");
                }
            case SCENARIO_OUT_OF_BOUNDS:
                System.out.println("Scenario choice does not exist. Pick another.");
                break;
            case UNKNOWN_INPUT:
                System.out.println("Not a valid input. Give another.");
                break;
            default:
                int scenarioIndex = result - 1;
                String scenarioFileName = scenariosFileNames.get(scenarioIndex);
                Scenario scenario = readScenario(scenarioFileName);

                LogGenerator logGenerator = new LogGenerator(scenario);
                List<Log> logs = logGenerator.generate();
                List<String> headers = scenario.getHeaders();

                writeAsCsv(headers, logs, scenarioFileName);
                System.out.println("File generated.");
            }
        } while (toContinue);
    }

    private static String getUserInput() {
        System.out.println();
        System.out.println("Options");
        System.out.println("- select a scenario file to use by entering its number");
        System.out.println("- select multiple scenario files to use by entering \"all\"");
        System.out.println("- exit the program by entering \"exit\"");

        System.out.println();
        System.out.print("Enter input: ");
        System.out.flush();

        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    private static int handleUserInput(String userInput, int maxScenarioChoice) {
        if (userInput.equalsIgnoreCase("exit")) {
            return EXIT_COMMAND;
        } else if (userInput.equalsIgnoreCase("all")) {
            return ALL_COMMAND;
        } else {
            try {
                int scenarioChoice = Integer.parseInt(userInput);
                if (scenarioChoice < MIN_SCENARIO_CHOICE || scenarioChoice > maxScenarioChoice) {
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
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        File scenarioFile = new File(SCENARIO_DIR_PATH + scenarioFileName + SCENARIO_FILE_EXTENSION);
        return objectMapper.readValue(scenarioFile, Scenario.class);
    }

    private static void writeAsCsv(List<String> headers, List<Log> logs, String scenarioFileName)
        throws IOException {
        if (!Files.exists(Paths.get(LOG_DIR_PATH))) {
            Files.createDirectory(Paths.get(LOG_DIR_PATH));
        }
        FileWriter writer = new FileWriter(LOG_DIR_PATH + toPrettyFileName(scenarioFileName) + LOG_FILE_EXTENSION);
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

    private static String toPrettyFileName(String fileName) {
        return "log_" + fileName.substring(SCENARIO_FILE_PREFIX.length() + 1);
    }
}
