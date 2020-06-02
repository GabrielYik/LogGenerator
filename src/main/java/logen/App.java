package logen;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        List<String> scenariosFileNames = ScenarioStorage.fetchScenariosFileNames();
        displayScenarioChoices(scenariosFileNames);
        UserInputHandler.handle(scenariosFileNames);
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
