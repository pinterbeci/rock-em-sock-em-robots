package hu.codewars.pinterbeci.code.practice;

import hu.codewars.pinterbeci.code.practice.reader.DataReader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        log.debug("Main class ready to run!");
        final DataReader dataReader = new DataReader();
        log.debug("DataReader initialized, and start read data!");
        dataReader.prepareData("game.txt");
        log.debug("Reading was succesfully!");
    }
}
