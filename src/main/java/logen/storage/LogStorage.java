package logen.storage;

import logen.log.Log;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static logen.Config.LOG_DIR_PATH;
import static logen.Config.LOG_FILE_EXTENSION;
import static logen.Config.LOG_FILE_PREFIX;
import static logen.Config.SCENARIO_FILE_PREFIX;

public class LogStorage {
    private LogStorage() {

    }

    public static void writeAsCsv(List<String> headers, List<Log> logs, String scenarioFileName)
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
        return LOG_FILE_PREFIX + fileName.substring(SCENARIO_FILE_PREFIX.length());
    }
}
