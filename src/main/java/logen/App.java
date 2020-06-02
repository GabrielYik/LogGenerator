package logen;

import static logen.Config.SCENARIO_DIR_PATH;
import static logen.Config.SCENARIO_DIR_PATH_JAR;
import static logen.util.PathUtil.SCENARIO_FILE_FILTER;

import logen.util.PathUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        List<String> scenariosFileNames = fetchScenariosFileNames();
        displayScenarioChoices(scenariosFileNames);
        UserInputHandler.handle(scenariosFileNames);
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
        System.out.println("Scenarios Available");
        for (int listing = 1; listing <= scenariosFileNames.size(); listing++) {
            System.out.println("(" + listing + ") " + scenariosFileNames.get(listing - 1));
        }
        System.out.println();
    }
}
