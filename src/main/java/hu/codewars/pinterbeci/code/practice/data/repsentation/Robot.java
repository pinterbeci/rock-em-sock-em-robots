package hu.codewars.pinterbeci.code.practice.data.repsentation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Robot {
    private String name;
    private int health;
    private int speed;
    private List<String> tactics;
}
