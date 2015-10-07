package logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogFile {

    private static final LogFile LOG_FILE = new LogFile();
    private static final String LOGFILE_PATH = "/logs";
    private static final String HISTORY_PATH = LOGFILE_PATH + "/history";

    public static LogFile getLogFile() {
        return LOG_FILE;
    }

    /**
     * ログファイルの一覧を返す
     *
     * @return String型のList
     */
    public List<String> getLogFileNames(String path) {
        try {
            URL url = LogFile.class.getResource(LOGFILE_PATH + path);
            File directory = new File(url.toURI());
            return Stream.of(directory.list()).filter(name -> name.contains(".log")).collect(Collectors.toList());
        } catch (URISyntaxException | NullPointerException e) {
            return Collections.emptyList();
        }
    }

    /**
     * ログディレクトリの一覧を返す
     *
     * @return String型のList
     */
    public List<String> getLogDirectoryNames() {
        try {
            URL url = LogFile.class.getResource(HISTORY_PATH);
            File directory = new File(url.toURI());
            return Stream.of(directory.list()).filter(name -> !name.contains(".log")).collect(Collectors.toList());
        } catch (URISyntaxException | NullPointerException e) {
            return Collections.emptyList();
        }
    }

    /**
     * ファイル名から中身のテキストを返す
     *
     * @param filename ファイル名
     * @return 1行ごとのリスト
     */
    public List<String> getFileText(String filename) {
        try {
            URL url = LogFile.class.getResource(LOGFILE_PATH + "/" + filename);
            return Files.readAllLines(new File(url.toURI()).toPath());
        } catch (URISyntaxException | IOException | NullPointerException e) {
            return Collections.emptyList();
        }
    }

}
