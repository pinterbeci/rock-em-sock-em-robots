package hu.codewars.pinterbeci.code.practice.data.repsentation;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Fight {
    private Map<String, Integer> tactic;
    private Map<String, Robot> fighters;
    private List<String> fightersIdList;

    {
        this.tactic = new HashMap<>();
        this.fighters = new HashMap<>();
        this.fightersIdList = new ArrayList<>();
    }
}
