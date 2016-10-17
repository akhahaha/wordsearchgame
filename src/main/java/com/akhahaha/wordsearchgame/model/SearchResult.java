package com.akhahaha.wordsearchgame.model;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    private final List<String> wordsFound;
    private final int bestScore;

    public static SearchResult newInstance(List<String> wordsFound, int bestScore) {
        return new SearchResult(wordsFound, bestScore);
    }

    private SearchResult(List<String> wordsFound, int bestScore) {
        if (wordsFound == null) {
            wordsFound = new ArrayList<>();
        }
        this.wordsFound = wordsFound;
        this.bestScore = bestScore;
    }

    public List<String> getWordsFound() {
        return wordsFound;
    }

    public int getBestScore() {
        return bestScore;
    }
}
