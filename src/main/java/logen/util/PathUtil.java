package logen.util;

import static logen.Config.SCENARIO_FILE_EXTENSION;
import static logen.Config.SCENARIO_FILE_PREFIX;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

public class PathUtil {
    public static final DirectoryStream.Filter<Path> SCENARIO_FILE_FILTER =
        p -> p.getFileName().toString().startsWith(SCENARIO_FILE_PREFIX);

    public static String toFileNameWithoutExtension(Path absolutePath) {
        String fileNameWithExtension = absolutePath.getFileName().toString();
        return fileNameWithExtension.substring(0, fileNameWithExtension.length() - SCENARIO_FILE_EXTENSION.length());
    }
}
