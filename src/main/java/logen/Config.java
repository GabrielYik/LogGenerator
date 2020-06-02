package logen;

import java.io.File;

public class Config {
    private static final String ROOT = ".";
    private static final String ROOT_JAR = "../..";

    private static final String SCENARIO_DIR_NAME = "scenarios";

    public static final String SCENARIO_FILE_PREFIX = "scenario_";
    public static final String LOG_FILE_PREFIX = "log_";

    public static final String SCENARIO_DIR_PATH = ROOT + File.separator + SCENARIO_DIR_NAME + File.separator;
    public static final String SCENARIO_DIR_PATH_JAR = ROOT_JAR + File.separator + SCENARIO_DIR_NAME + File.separator;
    public static final String LOG_DIR_PATH = ROOT + File.separator + "logs" + File.separator;

    public static final String SCENARIO_FILE_EXTENSION = ".json";
    public static final String LOG_FILE_EXTENSION = ".csv";

    private Config() {

    }
}
