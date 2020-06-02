package logen;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import logen.scenario.Scenario;
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

import static logen.Config.SCENARIO_DIR_PATH;
import static logen.Config.SCENARIO_DIR_PATH_JAR;
import static logen.Config.SCENARIO_FILE_EXTENSION;
import static logen.util.PathUtil.SCENARIO_FILE_FILTER;

public class ScenarioStorage {
    private ScenarioStorage() {

    }

    public static List<String> fetchScenariosFileNames() throws IOException {
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

    public static Scenario readScenario(String scenarioFileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper
                .configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true)
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);
        File scenarioFile = new File(SCENARIO_DIR_PATH + scenarioFileName + SCENARIO_FILE_EXTENSION);
        return objectMapper.readValue(scenarioFile, Scenario.class);
    }
}
