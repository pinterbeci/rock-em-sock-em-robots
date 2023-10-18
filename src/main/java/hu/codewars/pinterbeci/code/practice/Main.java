package hu.codewars.pinterbeci.code.practice;

import hu.codewars.pinterbeci.code.practice.service.CodeWarsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        final CodeWarsService codeWarsService = new CodeWarsService();
        try {
            codeWarsService.start();
        } catch (final Exception exception) {
            log.info(exception.getMessage());
            log.info("The game was unsuppressed!");
        }


    }
}
