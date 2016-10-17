package com.akhahaha.wordsearchgame.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GameParameters {
    private int rowCount;
    private int columnCount;
    private char[][] gameBoard;
    private List<String> wordList;
    private Map<Character, Integer> letterPoints;
    private int wordFindTime;
    private int letterIdentifyTime;
    private int maxGameTime;

    private GameParameters(int rowCount, int columnCount, char[][] gameBoard, List<String> wordList,
                           Map<Character, Integer> letterPoints, int wordFindTime, int letterIdentifyTime, int maxGameTime) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.gameBoard = gameBoard;
        this.wordList = wordList;
        this.letterPoints = letterPoints;
        this.wordFindTime = wordFindTime;
        this.letterIdentifyTime = letterIdentifyTime;
        this.maxGameTime = maxGameTime;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public char[][] getGameBoard() {
        return gameBoard;
    }

    public List<String> getWordList() {
        return wordList;
    }

    public Map<Character, Integer> getLetterPoints() {
        return letterPoints;
    }

    public int getWordFindTime() {
        return wordFindTime;
    }

    public int getLetterIdentifyTime() {
        return letterIdentifyTime;
    }

    public int getMaxGameTime() {
        return maxGameTime;
    }

    @Override
    public String toString() {
        return "GameParameters{" +
                "rowCount=" + rowCount +
                ", columnCount=" + columnCount +
                ", gameBoard=" + Arrays.toString(gameBoard) +
                ", wordList=" + wordList +
                ", letterPoints=" + letterPoints +
                ", wordFindTime=" + wordFindTime +
                ", letterIdentifyTime=" + letterIdentifyTime +
                ", maxGameTime=" + maxGameTime +
                '}';
    }

    public static class Builder {
        private int rowCount;
        private int columnCount;
        private char[][] gameBoard;
        private List<String> wordList;
        private Map<Character, Integer> letterPoints;
        private int wordFindTime;
        private int letterIdentifyTime;
        private int maxGameTime;

        public Builder setRowCount(int rowCount) {
            this.rowCount = rowCount;
            return this;
        }

        public Builder setColumnCount(int columnCount) {
            this.columnCount = columnCount;
            return this;
        }

        public Builder setGameBoard(char[][] gameBoard) {
            this.gameBoard = gameBoard;
            return this;
        }

        public Builder setWordList(List<String> wordList) {
            this.wordList = wordList;
            return this;
        }

        public Builder setLetterPoints(Map<Character, Integer> letterPoints) {
            this.letterPoints = letterPoints;
            return this;
        }

        public Builder setWordFindTime(int wordFindTime) {
            this.wordFindTime = wordFindTime;
            return this;
        }

        public Builder setLetterIdentifyTime(int letterIdentifyTime) {
            this.letterIdentifyTime = letterIdentifyTime;
            return this;
        }

        public Builder setMaxGameTime(int maxGameTime) {
            this.maxGameTime = maxGameTime;
            return this;
        }

        public GameParameters build() {
            return new GameParameters(rowCount, columnCount, gameBoard, wordList, letterPoints, wordFindTime,
                    letterIdentifyTime, maxGameTime);
        }
    }
}
