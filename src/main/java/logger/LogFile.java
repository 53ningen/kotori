package logger;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogFile {
    private static final LogFile logFile = new LogFile();
    private final String LINE_SEPARATOR = System.getProperty("line.separator");
    private final String LOGFILE_PATH;

    public LogFile() {
        LOGFILE_PATH = "/logs";
    }

    public static LogFile getLogFile() {
        return logFile;
    }

    /**
     * ログファイルの一覧を返す
     * @return String型のList
     */
    public List<String> getLogFileNames() {
        try {
            URL url = LogFile.class.getResource(LOGFILE_PATH);
            File directory = new File(url.toURI());
            return Stream.of(directory.list()).collect(Collectors.toList());
        } catch (URISyntaxException e) {
            return Collections.emptyList();
        }
    }

    /**
     * ファイル名から中身のテキストを返す
     * @param filename ファイル名
     * @return Optionalなテキスト
     */
    public Optional<String> getFileText(String filename) {
        try {
            URL url = LogFile.class.getResource(LOGFILE_PATH + "/" + filename);
            File file = new File(url.toURI());
            BufferedReader br = new BufferedReader(new FileReader(file));
            String tmp, str = "<pre>";
            while((tmp = br.readLine()) != null) {
                str += tmp + LINE_SEPARATOR;
            }
            str += "</pre>";
            br.close();
            return Optional.of(str);
        } catch (URISyntaxException | IOException e) {
            return Optional.empty();
        }
    }
}
