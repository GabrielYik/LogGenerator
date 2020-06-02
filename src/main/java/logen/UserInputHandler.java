package logen;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import logen.generation.LogGenerator;
import logen.log.Log;
import logen.scenario.Scenario;

public class UserInputHandler {
    private static final String USER_OPTIONS_MESSAGE =
        "Options\n"
            + "- select a scenario file to use by entering its number\n"
            + "- select multiple scenario files to use by entering \"all\"\n"
            + "- exit the program by entering \"exit\"\n";
    private static final String USER_PROMPT_MESSAGE = "Enter input: ";
    private static final String EXIT_MESSAGE = "Goodbye";
    private static final String SCENARIO_OUT_OF_BOUNDS_MESSAGE = "Scenario choice does not exist. Pick another.";
    private static final String UNKNOWN_INPUT_MESSAGE = "Not a valid input. Give another.";
    private static final String FILE_GENERATION_SUCCESS_MESSAGE = "File generated";

    private static final String EXIT_CHOICE = "exit";
    private static final String ALL_CHOICE = "all";

    private static final int EXIT_COMMAND = -1;
    private static final int ALL_COMMAND = -2;
    private static final int SCENARIO_OUT_OF_BOUNDS = -3;
    private static final int UNKNOWN_INPUT = -4;

    private static final int MIN_SCENARIO_CHOICE = 1;

    private UserInputHandler() {

    }

    public static void handle(List<String> scenariosFileNames) throws IOException {
        boolean toContinueHandlingInput = true;
        do {
            String userInput = getUserInput();
            int userCommand = handleUserInput(userInput, scenariosFileNames.size());

            switch (userCommand) {
            case EXIT_COMMAND:
                System.out.println(EXIT_MESSAGE);
                toContinueHandlingInput = false;
                break;
            case ALL_COMMAND:
                for (String scenarioFileName : scenariosFileNames) {
                    generateLogs(scenarioFileName);
                }
            case SCENARIO_OUT_OF_BOUNDS:
                System.out.println(SCENARIO_OUT_OF_BOUNDS_MESSAGE);
                break;
            case UNKNOWN_INPUT:
                System.out.println(UNKNOWN_INPUT_MESSAGE);
                break;
            default:
                int scenarioIndex = userCommand - 1;
                String scenarioFileName = scenariosFileNames.get(scenarioIndex);
                generateLogs(scenarioFileName);
            }
        } while (toContinueHandlingInput);
    }

    private static String getUserInput() {
        System.out.println(USER_OPTIONS_MESSAGE);
        System.out.println(USER_PROMPT_MESSAGE);

        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

    private static int handleUserInput(String userInput, int maxScenarioChoice) {
        switch (userInput.toLowerCase().trim()) {
        case EXIT_CHOICE:
            return EXIT_COMMAND;
        case ALL_CHOICE:
            return ALL_COMMAND;
        default:
            try {
                int scenarioChoice = Integer.parseInt(userInput);
                if (scenarioChoice < MIN_SCENARIO_CHOICE || scenarioChoice > maxScenarioChoice) {
                    return SCENARIO_OUT_OF_BOUNDS;
                } else {
                    return scenarioChoice;
                }
            } catch (NumberFormatException e) {
                return UNKNOWN_INPUT;
            }
        }
    }

    private static void generateLogs(String scenarioFileName) throws IOException {
        Scenario scenario = ScenarioStorage.readScenario(scenarioFileName);

        LogGenerator logGenerator = new LogGenerator(scenario);
        List<Log> logs = logGenerator.generate();
        List<String> headers = scenario.getHeaders();

        LogStorage.writeAsCsv(headers, logs, scenarioFileName);
        System.out.println(FILE_GENERATION_SUCCESS_MESSAGE);
    }
}
