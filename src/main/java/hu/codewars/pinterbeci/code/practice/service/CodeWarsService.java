package hu.codewars.pinterbeci.code.practice.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hu.codewars.pinterbeci.code.practice.data.repsentation.Fight;
import hu.codewars.pinterbeci.code.practice.data.repsentation.Robot;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Setter
@Getter
public class CodeWarsService {

    private List<Robot> savedRobots;

    private Map<String, Integer> tactic;

    private Fight fight;

    private Robot winnerRobot;

    public void getGameDataFromFile(final String data) {
        final String regex = "robot\\d\\s=|tactics\\s=\\s|fight";
        final String[] splittedData = data.split(regex);

        final List<Robot> validRobots = new ArrayList<>();

        for (String currentLine : splittedData) {
            final Gson gson = new Gson();
            if (!currentLine.isEmpty()) {
                try {
                    if (this.isValidRobotStr(currentLine)) {
                        final Robot robot = gson.fromJson(currentLine, Robot.class);
                        validRobots.add(robot);
                    } else if (this.isValidTacticStr(currentLine)) {
                        Type typeOfTacticMap = new TypeToken<Map<String, Integer>>() {
                        }.getType();
                        this.tactic = gson.fromJson(currentLine, typeOfTacticMap);
                    }
                } catch (final Exception exception) {
                    log.debug(exception.getMessage());
                }
            }
        }
        this.setSavedRobots(validRobots);
        setFightDetails();
    }

    private boolean isValidRobotStr(final String currentLine) {
        return (currentLine.contains("name") && currentLine.contains("health")
                && currentLine.contains("speed") && currentLine.contains("tactics"));
    }

    private boolean isValidTacticStr(final String currentLine) {
        return (currentLine.contains("punch") && currentLine.contains("laser")
                && currentLine.contains("missile"));
    }

    private void setFightDetails() {
        if (this.savedRobots != null && this.savedRobots.size() == 2) {
            this.fight = new Fight();
            this.fight.setFighter1(this.savedRobots.get(0));
            this.fight.setFighter2(this.savedRobots.get(1));
        }
        if (this.tactic != null) {
            this.fight.setTactic(this.tactic);
        }
    }

    public void startTheGame() {
        startFightUsingTactics();
    }


    private Robot getStarterRobot() {
        final Fight currentGame = this.fight;
        if (currentGame.getFighter1() != null) {
            int comparingValue = currentGame.getFighter1().compareTo(currentGame.getFighter2());
            return comparingValue > 0 ? currentGame.getFighter1() : currentGame.getFighter2();
        }
        return new Robot();
    }

    private void startFightUsingTactics() {
        final Map<String, Integer> tacticsOfFight = this.fight.getTactic();
        Robot winner = null;
        final Robot starterRobot = getStarterRobot();
        final Robot secondFighter;

        if (starterRobot.equals(this.fight.getFighter1())) {
            secondFighter = this.fight.getFighter2();
        } else {
            secondFighter = this.fight.getFighter1();
        }
        int secondFighterCurrentTactic = 0;
        int usedTacticElement = 0;
        for (String starterRobotTactic : starterRobot.getTactics()) {

            int starterRobotTacticValue = this.tactic.get(starterRobotTactic);
            usedTacticElement++;
            secondFighter.setHealth(secondFighter.getHealth() - starterRobotTacticValue);

            String secondRobotCurrentTactic = secondFighter.getTactics().get(secondFighterCurrentTactic++);
            Integer secondRobotCurrentTacticValue = this.tactic.get(secondRobotCurrentTactic);
            starterRobot.setHealth(starterRobot.getHealth() - secondRobotCurrentTacticValue);

            winner = getTheWinner(this.tactic, usedTacticElement, starterRobot, secondFighter);
            if (winner != null) {
                break;
            }
        }
        printTheResultToConsole(winner);
    }

    private void printTheResultToConsole(final Robot winner) {
        if (winner != null) {
            if ("Equality".contains(winner.getName()))
                System.out.println("The fight was a draw.");
            else
                System.out.printf("%s has won the fight!", winner.getName());
        }
    }

    private Robot getTheWinner(final Map<String, Integer> tactics, final int usedTacticElement, final Robot robot1, final Robot robot2) {
        if (robot1.getHealth() <= 0 && robot2.getHealth() > 0)
            return robot2;

        if (robot2.getHealth() <= 0 && robot1.getHealth() > 0)
            return robot1;

        if (tactics.entrySet().size() == usedTacticElement)
            return robot1.getHealth() == robot2.getHealth() ? new Robot("Equality", 0, 0, null) :
                    robot1.getHealth() > robot2.getHealth() ? robot1 : robot2;

        return null;
    }

}
