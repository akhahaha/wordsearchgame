package com.akhahaha.wordsearchgame;

import com.akhahaha.wordsearchgame.model.GameParameters;

import javax.json.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordSearchGame {
    public static void main(String... args) throws FileNotFoundException {
        // Read from file if filename provided
        if (args.length > 0) {
            System.setIn(new FileInputStream(args[0]));
        }

        InputStreamReader stdinStream = new InputStreamReader(System.in);

        // Parse JSON
        JsonReader reader = Json.createReader(stdinStream);
        GameParameters gameParameters = parseJson((JsonObject) reader.read());
    }

    private static GameParameters parseJson(JsonObject gameParametersJson) {
        int rowCount = gameParametersJson.getInt("rowCount");
        int columnCount = gameParametersJson.getInt("columnCount");
        char[][] gameBoard = new char[rowCount][columnCount];
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                gameBoard[r][c] = gameParametersJson.getJsonArray("gameBoard").getJsonArray(r).getString(c)
                        .toLowerCase().charAt(0);
            }
        }
        List<String> wordList = new ArrayList<>();
        JsonArray wordJsonList = gameParametersJson.getJsonArray("wordList");
        for (int i = 0; i < wordJsonList.size(); i++) {
            wordList.add(wordJsonList.getString(i).toLowerCase());
        }
        Map<Character, Integer> letterPoints = new HashMap<>();
        JsonObject letterPointsJson = gameParametersJson.getJsonObject("letterPoints");
        for (String key : letterPointsJson.keySet()) {
            letterPoints.put(key.toLowerCase().charAt(0), letterPointsJson.getInt(key));
        }
        int wordFindTime = gameParametersJson.getInt("wordFindTime");
        int letterIdentifyTime = gameParametersJson.getInt("letterIdentifyTime");
        int maxGameTime = gameParametersJson.getInt("maxGameTime");

        return new GameParameters.Builder()
                .setRowCount(rowCount)
                .setColumnCount(columnCount)
                .setGameBoard(gameBoard)
                .setWordList(wordList)
                .setLetterPoints(letterPoints)
                .setWordFindTime(wordFindTime)
                .setLetterIdentifyTime(letterIdentifyTime)
                .setMaxGameTime(maxGameTime)
                .build();
    }
}
