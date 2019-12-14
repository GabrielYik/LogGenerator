package logen;

import static logen.storage.Config.FILE_EXTENSION;
import static logen.storage.Config.LOG_DIR_PATH;
import static logen.storage.Config.SCENARIO_DIR_PATH;
import static logen.util.PathUtil.SCENARIO_FILE_FILTER;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import logen.model.Log;
import logen.storage.Scenario;
import logen.util.PathUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws IOException {
        List<String> scenariosFileNames = fetchScenariosFileNames();
        displayScenarioChoices(scenariosFileNames);
        int scenarioChoice = getScenarioChoice();
        Scenario scenario = readScenario(scenariosFileNames.get(scenarioChoice - 1));

        LogGenerator logGenerator = new LogGenerator(scenario);
        List<Log> logs = logGenerator.generate();

        List<String> headers = scenario.getHeaders();
        writeAsCsv(headers, logs);
    }

    private static List<String> fetchScenariosFileNames() throws IOException {
        Path scenarioDirPath = new File(SCENARIO_DIR_PATH).toPath();
        List<String> scenarios = new ArrayList<>();
        Files.newDirectoryStream(scenarioDirPath, SCENARIO_FILE_FILTER)
            .forEach(p -> scenarios.add(PathUtil.toFileName(p)));
        return scenarios;
    }

    private static void displayScenarioChoices(List<String> scenariosFileNames) {
        int currentListing = 1;
        for (int i = currentListing; i <= scenariosFileNames.size(); i++) {
            System.out.println(
                "Scenario "
                    + i
                    + ": "
                    + scenariosFileNames.get(currentListing - 1));
        }
    }

    private static int getScenarioChoice() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
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
