package logen.storage;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import logen.scenario.Scenario;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScenarioStorage {
    private static final String SCENARIO_DIR_NAME = "scenarios";
    private static final String SCENARIO_DIR_PATH = StorageConfig.ROOT + File.separator + SCENARIO_DIR_NAME + File.separator;
    private static final String SCENARIO_DIR_PATH_JAR = StorageConfig.ROOT_JAR + File.separator + SCENARIO_DIR_NAME + File.separator;

    public static final String SCENARIO_FILE_PREFIX = "scenario_";
    public static final String SCENARIO_FILE_EXTENSION = ".json";

    private static final DirectoryStream.Filter<Path> SCENARIO_FILE_FILTER =
            path -> path.getFileName().toString().startsWith(SCENARIO_FILE_PREFIX);

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
                .forEach(path -> scenarios.add(toFileNameWithoutExtension(path)));
        return scenarios;
    }

    public static String toFileNameWithoutExtension(Path absolutePath) {
        String fileNameWithExtension = absolutePath.getFileName().toString();
        return fileNameWithExtension.substring(0, fileNameWithExtension.length() - SCENARIO_FILE_EXTENSION.length());
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
        Scenario scenario = objectMapper.readValue(scenarioFile, Scenario.class);
        scenario.completeConfiguration();
        return scenario;
    }
}
