package hu.codewars.pinterbeci.code.practice.reader;

import hu.codewars.pinterbeci.code.practice.service.CodeWarsService;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


@Slf4j
public class DataReader {

    private final CodeWarsService codeWarsService;

    public DataReader() {
        this.codeWarsService = new CodeWarsService();
    }

    private String readFromInputStream(final InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public void readDataFromFile(final String fileName) {
        try {
            final ClassLoader classLoader = getClass().getClassLoader();
            final InputStream inputStream = classLoader.getResourceAsStream(fileName);
            final String data = readFromInputStream(inputStream);
            this.codeWarsService.getRobotsFromGame(data);
            this.codeWarsService.startTheGame();
        } catch (final Exception exception) {
            log.debug("An exception occurs: ", exception);
        }
    }

}
