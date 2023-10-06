package hu.codewars.pinterbeci.code.practice.reader;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class DataReader {

    public InputStream fileToInputStream(final String filePath ) throws IOException{
        final File inputData = new File(filePath);
        final InputStream inputDataStream = new DataInputStream(new FileInputStream(inputData));

        return inputDataStream;
    }

    private String readFromInputStream(final InputStream inputStream)
            throws IOException {
       final StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
