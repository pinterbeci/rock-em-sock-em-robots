package hu.codewars.pinterbeci.code.practice;

import hu.codewars.pinterbeci.code.practice.service.CodeWarsService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        final CodeWarsService codeWarsService = new CodeWarsService();
        codeWarsService.start();
    }
}
