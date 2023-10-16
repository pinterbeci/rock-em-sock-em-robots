package hu.codewars.pinterbeci.code.practice.service;

import com.google.gson.Gson;
import hu.codewars.pinterbeci.code.practice.data.repsentation.Robot;
import hu.codewars.pinterbeci.code.practice.data.repsentation.Tactic;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class CodeWarsService {

    private List<Robot> savedRobots;

    private Tactic tactic;

    public void getRobotsFromGame(final String data) {
        final String replacedData = data.replaceAll("\\s", "");
        final String regex = "robot\\d=|tactics=|fight";
        final String[] splittedData = replacedData.split(regex);
        final List<Robot> validRobots = new ArrayList<>();
        for (String currentLine : splittedData) {
            final Gson gson = new Gson();
            if (!currentLine.isEmpty()) {
                if (this.isValidRobotStr(currentLine)) {
                    final Robot robot = gson.fromJson(currentLine, Robot.class);
                    validRobots.add(robot);
                } else if (!this.isValidTacticStr(currentLine)) {
                    this.tactic.setValue(null);
                }
            }
            this.setSavedRobots(validRobots);
        }
    }

    private boolean isValidRobotStr(final String currentLine) {
        return (currentLine.contains("name") && currentLine.contains("health")
                && currentLine.contains("speed") && currentLine.contains("tactics"));
    }

    private boolean isValidTacticStr(final String currentLine) {
        return (currentLine.contains("punch") && currentLine.contains("laser")
                && currentLine.contains("missile"));
    }

    public void startTheGame(){
        //az kezd először akinek a nagyobb a sebessége
        //a robotok felváltva támadnak, sorendben alkalmazva a taktikákat
        //a harc akkor ér végét, ha valamelyik robot 'health'-e 0 vagy kevesebb
        // ha mindkét robot végrehajtja a taktikát, akkor az nyer, akinek több 'health'-e marad, ha valamelyiknek
        // 0 a 'health'-e, a másik nyer, akkor -> '{Name} has won the fight!'
        // ah elfogy a taktika, és a 'health' egyenlő, akkor 'The fight was a draw.'
    }

}
