package hu.codewars.pinterbeci.code.practice.data.repsentation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Tactics {
    PUNCH("punch", 20),
    LASER("laser", 30),
    MISSILE("missile", 35);

    private final String name;
    private final int value;
}
