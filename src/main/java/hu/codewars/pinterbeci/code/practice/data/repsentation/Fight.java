package hu.codewars.pinterbeci.code.practice.data.repsentation;

import lombok.Data;

import java.util.Map;

@Data
public class Fight {
    private Robot fighter1;
    private Robot fighter2;
    private Map<String, Integer> tactic;
}
