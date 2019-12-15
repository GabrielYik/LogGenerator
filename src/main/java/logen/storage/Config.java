package logen.storage;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

public class Config {
    private static final String ROOT = ".";
    private static final String ROOT_JAR = "../..";
    private static final String SCENARIO_DIR_NAME = "scenarios";
    public static final String SCENARIO_FILE_PREFIX = "scenario";

    public static final String SCENARIO_DIR_PATH = ROOT + File.separator + SCENARIO_DIR_NAME + File.separator;
    public static final String SCENARIO_DIR_PATH_JAR = ROOT_JAR + File.separator + SCENARIO_DIR_NAME + File.separator;
    public static final String LOG_DIR_PATH = ROOT + File.separator + "logs" + File.separator;
    public static final String LOG_DIR_PATH_JAR = ROOT_JAR + File.separator + "logs" + File.separator;

    public static final String FILE_EXTENSION = ".json";

    public static final DirectoryStream.Filter<Path> scenarioFileFilter =
        p -> p.getFileName().toString().substring(0, 8).equals(SCENARIO_FILE_PREFIX);
}
