package logen.util;

import static logen.storage.Config.FILE_EXTENSION;
import static logen.storage.Config.SCENARIO_FILE_PREFIX;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

public class PathUtil {
    public static final DirectoryStream.Filter<Path> SCENARIO_FILE_FILTER =
        p -> p.getFileName().toString().substring(0, 8).equals(SCENARIO_FILE_PREFIX);

    public static String toFileName(Path absolutePath) {
        String fileNameWithExtension = absolutePath.getFileName().toString();
        return fileNameWithExtension.substring(0, fileNameWithExtension.length() - FILE_EXTENSION.length());
    }
}
