package hu.codewars.pinterbeci.code.practice.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hu.codewars.pinterbeci.code.practice.data.repsentation.Fight;
import hu.codewars.pinterbeci.code.practice.data.repsentation.Robot;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Setter
@Getter
public class CodeWarsService {

    private Fight fight;

    private Robot winnerRobot;

    private Map<String, String> gameDataMap;


    public void start() throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final InputStream inputStream = classLoader.getResourceAsStream("game.txt");
        this.saveInputData(inputStream);
    }

    private void saveInputData(final InputStream inputStream) throws IOException {
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            StringBuilder stringBuilder;
            String currentFightElementData;

            this.gameDataMap = new HashMap<>();

            while ((line = br.readLine()) != null) {

                if (line.startsWith("robot")) {
                    stringBuilder = new StringBuilder();
                    final String currentRobotName = line.split(" ")[0];
                    stringBuilder.append("{");

                    while (!(line = br.readLine()).equals("}")) {
                        stringBuilder.append(line);
                    }

                    stringBuilder.append("}");
                    currentFightElementData = stringBuilder.toString();
                    this.gameDataMap.put(currentRobotName, currentFightElementData);
                } else if (line.startsWith("tactics")) {
                    stringBuilder = new StringBuilder();

                    final String tacticsKeyName = line.split(" ")[0];
                    stringBuilder.append("{");

                    while (!(line = br.readLine()).equals("}")) {
                        stringBuilder.append(line);
                    }

                    stringBuilder.append("}");
                    currentFightElementData = stringBuilder.toString();
                    this.gameDataMap.put(tacticsKeyName, currentFightElementData);

                } else if (line.startsWith("fight")) {
                    currentFightElementData = line.split("fight")[1];
                    this.gameDataMap.put("fight", currentFightElementData);
                }
            }
        }
        convertData(this.gameDataMap);
    }

    private void convertData(final Map<String, String> dataMap) {
        this.fight = new Fight();
        Gson gson;
        List<String> savedFightingDetails = new ArrayList<>();
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            if ((entry.getKey()).startsWith("robot")) {
                gson = new Gson();
                Robot robot = gson.fromJson(entry.getValue(), Robot.class);
                this.fight.getFighters().put(entry.getKey(), robot);
                savedFightingDetails.add(entry.getKey());
                this.fight.getFightersIdList().add(entry.getKey());
            } else if ("tactics".equals(entry.getKey())) {
                gson = new Gson();
                Type typeOfTacticMap = new TypeToken<Map<String, Integer>>() {
                }.getType();
                Map<String, Integer> currentTactic = gson.fromJson(entry.getValue(), typeOfTacticMap);
                this.fight.setTactic(currentTactic);
                savedFightingDetails.add(entry.getKey());
            } else if ("fight".equals(entry.getKey())) {
                if (!checkFightingDetails(entry.getValue(), savedFightingDetails.toArray(new String[]{}))) {
                    log.info("Data is not appropriate!");
                } else {
                    startTheGame();
                }
            }
        }
    }

    private boolean checkFightingDetails(final String fightingDetailsFromFile, final String... savedFightingDetails) {
        if (fightingDetailsFromFile == null || savedFightingDetails == null)
            return false;

        if (savedFightingDetails.length < 3)
            return false;

        return fightingDetailsFromFile.contains(savedFightingDetails[0]) && fightingDetailsFromFile.contains(savedFightingDetails[1])
                && fightingDetailsFromFile.contains(savedFightingDetails[2]);
    }

    public void startTheGame() {
        determineFightersAndStartTheFight();
    }

    private void determineFightersAndStartTheFight() {
        final Fight currentGame = this.fight;
        Robot fighter1 = new Robot();
        Robot fighter2 = new Robot();
        List<String> fightersIdList = currentGame.getFightersIdList();
        if (fightersIdList != null && fightersIdList.size() == 2) {
            fighter1 = currentGame.getFighters().get(fightersIdList.get(0));
            fighter2 = currentGame.getFighters().get(fightersIdList.get(1));
        }
        startFightUsingTactics(fighter1, fighter2);
    }

    private Robot getStarterRobot(final Robot fighter1, final Robot fighter2) {
        if (fighter1 != null) {
            int comparingValue = fighter1.compareTo(fighter2);
            return comparingValue > 0 ? fighter1 : fighter2;
        }
        return new Robot();
    }

    private void startFightUsingTactics(final Robot fighter1, final Robot fighter2) {
        Robot winner = null;
        final Robot starterRobot = getStarterRobot(fighter1, fighter2);
        final Robot secondFighter;

        if (starterRobot.equals(fighter1)) {
            secondFighter = fighter2;
        } else {
            secondFighter = fighter1;
        }

        int secondFighterCurrentTactic = 0;
        int usedTacticElement = 0;
        for (String starterRobotTactic : starterRobot.getTactics()) {

            int starterRobotTacticValue = this.fight.getTactic().get(starterRobotTactic);
            usedTacticElement++;
            secondFighter.setHealth(secondFighter.getHealth() - starterRobotTacticValue);

            String secondRobotCurrentTactic = secondFighter.getTactics().get(secondFighterCurrentTactic++);
            Integer secondRobotCurrentTacticValue = this.fight.getTactic().get(secondRobotCurrentTactic);
            starterRobot.setHealth(starterRobot.getHealth() - secondRobotCurrentTacticValue);

            winner = getTheWinner(usedTacticElement, starterRobot, secondFighter);
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

    private Robot getTheWinner(final int usedTacticElement, final Robot robot1, final Robot robot2) {
        if (robot1.getHealth() <= 0 && robot2.getHealth() > 0)
            return robot2;

        if (robot2.getHealth() <= 0 && robot1.getHealth() > 0)
            return robot1;

        if (robot1.getTactics().size() == usedTacticElement && robot2.getTactics().size() == usedTacticElement)
            return robot1.getHealth() == robot2.getHealth() ? new Robot("Equality", 0, 0, null) :
                    robot1.getHealth() > robot2.getHealth() ? robot1 : robot2;

        return null;
    }
}
