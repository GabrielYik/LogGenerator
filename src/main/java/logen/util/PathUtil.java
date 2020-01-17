package logen.util;

import static logen.storage.Config.SCENARIO_FILE_EXTENSION;
import static logen.storage.Config.SCENARIO_FILE_PREFIX;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

public class PathUtil {
    public static final DirectoryStream.Filter<Path> SCENARIO_FILE_FILTER =
        p -> p.getFileName().toString().substring(0, SCENARIO_FILE_PREFIX.length()).equals(SCENARIO_FILE_PREFIX);

    public static String toFileNameWithoutExtension(Path absolutePath) {
        String fileNameWithExtension = absolutePath.getFileName().toString();
        return fileNameWithExtension.substring(0, fileNameWithExtension.length() - SCENARIO_FILE_EXTENSION.length());
    }
}
