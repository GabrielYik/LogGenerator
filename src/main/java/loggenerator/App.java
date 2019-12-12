package loggenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import loggenerator.model.Log;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class App {
    public static final String SCENARIO_FILE_PATH = "." + File.separator + "many_failed_login_attempts.json";
    public static final String CSV_FILE_PATH = "." + File.separator + "logs.csv";

    public static void main(String[] args) throws IOException {
        Scenario scenario = readScenario();
        LogGenerator logGenerator = new LogGenerator(scenario);
        List<Log> logs = logGenerator.generate();
        List<String> headers = scenario.getHeaders();
        writeAsCsv(headers, logs);
    }

    private static Scenario readScenario() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File scenarioFile = new File(SCENARIO_FILE_PATH);
        return objectMapper.readValue(scenarioFile, Scenario.class);
    }

    private static void writeAsCsv(List<String> headers, List<Log> logs) throws IOException {
        FileWriter writer = new FileWriter(CSV_FILE_PATH);
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
