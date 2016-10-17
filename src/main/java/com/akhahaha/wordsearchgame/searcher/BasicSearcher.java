package com.akhahaha.wordsearchgame.searcher;

import com.akhahaha.wordsearchgame.model.GameBoard;
import com.akhahaha.wordsearchgame.model.GameParameters;
import com.akhahaha.wordsearchgame.model.GameTile;
import com.akhahaha.wordsearchgame.model.SearchResult;

import java.util.*;

public class BasicSearcher implements Searcher {
    private final GameParameters gameParameters;
    private final GameBoard gameBoard;
    private final Map<Character, List<GameTile>> tileCharacterMap;
    private final int maxWordLength;
    private final List<String> wordsFound;
    private final int bestScore;
    private final SearchResult searchResult;

    public static SearchResult search(GameParameters gameParameters) {
        return BasicSearcher.newInstance(gameParameters).getSearchResult();
    }

    public static BasicSearcher newInstance(GameParameters gameParameters) {
        return new BasicSearcher(gameParameters);
    }

    private BasicSearcher(GameParameters gameParameters) {
        this.gameParameters = gameParameters;
        this.gameBoard = GameBoard.newInstance(gameParameters.getRowCount(), gameParameters.getColumnCount(),
                gameParameters.getGameBoard());
        this.tileCharacterMap = constructTileCharacterMap();
        this.maxWordLength = calculateMaxWordSize();
        this.wordsFound = findWords();
        this.bestScore = determineBestScore(wordsFound);
        this.searchResult = SearchResult.newInstance(wordsFound, bestScore);
    }

    public GameParameters getGameParameters() {
        return gameParameters;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    private Map<Character, List<GameTile>> constructTileCharacterMap() {
        Map<Character, List<GameTile>> tileCharacterMap = new HashMap<>();
        // Initialize tile character map for all characters of the alphabet
        for (char c = 'a'; c <= 'z'; c++) {
            tileCharacterMap.put(c, new ArrayList<GameTile>());
        }

        // Populate tile character map
        for (GameTile tile : gameBoard.getTiles()) {
            tileCharacterMap.get(tile.getValue()).add(tile);
        }

        return tileCharacterMap;
    }

    public Map<Character, List<GameTile>> getTileCharacterMap() {
        return tileCharacterMap;
    }

    private int calculateMaxWordSize() {
        return 1 + ((gameParameters.getMaxGameTime() - gameParameters.getWordFindTime()) /
                gameParameters.getLetterIdentifyTime());
    }

    public int getMaxWordLength() {
        return maxWordLength;
    }

    private List<String> findWords() {
        Set<String> possibleWords = new HashSet<>();
        // Perform initial filter by length (and remove duplicates via set)
        for (String word : gameParameters.getWordList()) {
            if (word.length() > maxWordLength || word.isEmpty()) {
                continue;
            }

            possibleWords.add(word.toLowerCase());
        }

        // Filter by character availability and then search words if the word is present using the rarest character
        List<String> wordsFound = new ArrayList<>();
        for (String word : possibleWords) {
            // Construct word histogram
            Map<Character, Integer> wordHistogram = new HashMap<>();
            for (char c : word.toCharArray()) {
                if (!wordHistogram.containsKey(c)) {
                    wordHistogram.put(c, 1);
                } else {
                    wordHistogram.put(c, wordHistogram.get(c) + 1);
                }
            }

            // Filter for character availability and determine the rarest character
            boolean isEnoughCharacters = true;
            for (Map.Entry<Character, Integer> entry : wordHistogram.entrySet()) {
                char c = entry.getKey();
                int charWordCount = entry.getValue();
                int charBoardCount = tileCharacterMap.get(c).size();

                // Not enough characters present in board
                if (charWordCount > charBoardCount) {
                    isEnoughCharacters = false;
                    break;
                }
            }

            if (isEnoughCharacters) {
                boolean isWordPresent = false;
                // Search for the word starting either from the beginning or the end of the world,
                // whichever character is rarest
                List<GameTile> startCharTiles = tileCharacterMap.get(word.charAt(0));
                List<GameTile> endCharTiles = tileCharacterMap.get(word.charAt(word.length() - 1));
                boolean headFirst = startCharTiles.size() < endCharTiles.size();
                String reversedWord = new StringBuilder(word).reverse().toString();
                for (GameTile tile : headFirst ? startCharTiles : endCharTiles) {
                    isWordPresent = isWordPresent(tile, headFirst ? word : reversedWord);
                    if (isWordPresent) {
                        break;
                    }
                }
                if (!isWordPresent) {
                    for (GameTile tile : !headFirst ? startCharTiles : endCharTiles) {
                        isWordPresent = isWordPresent(tile, !headFirst ? word : reversedWord);
                        if (isWordPresent) {
                            break;
                        }
                    }
                }

                if (isWordPresent) {
                    wordsFound.add(word);
                }
            }
        }

        return wordsFound;
    }

    private boolean isWordPresent(GameTile startTile, String word) {
        Stack<GameTile> tileStack = new Stack<>();
        Stack<GameTile> pathStack = new Stack<>();
        tileStack.push(startTile);

        while (!tileStack.isEmpty()) {
            GameTile currTile = tileStack.pop();
            char c = currTile.getValue();

            if (c == word.charAt(pathStack.size())) {
                pathStack.push(currTile);

                // Check if word found
                if (pathStack.size() == word.length()) {
                    return true;
                }

                // Add neighbors to stack
                // TODO: Prioritize search towards center of the board
                for (GameTile tile : currTile.getNeighbors()) {
                    if (tile.getValue() == word.charAt(pathStack.size()) && !pathStack.contains(tile)) {
                        tileStack.push(tile);
                    }
                }
            } else {
                // Backtrack
                if (pathStack.isEmpty()) {
                    return false;
                } else {
                    pathStack.pop();
                }
            }
        }

        return false;
    }

    public List<String> getWordsFound() {
        return wordsFound;
    }

    private int determineBestScore(List<String> words) {
        // https://en.wikipedia.org/wiki/Knapsack_problem#0.2F1_knapsack_problem
        // TODO: Use 1-d array to minimize space
        int n = words.size();
        int W = gameParameters.getMaxGameTime();

        // Initialize values
        int[] v = new int[n];
        int[] w = new int[n];
        int[][] m = new int[n][W + 1];
        for (int i = 0; i < n; i++) {
            v[i] = calculateWordScore(words.get(i));
            w[i] = calculateWordCost(words.get(i));
        }
        for (int j = 0; j < W; j++) {
            m[0][j] = 0;
        }

        for (int i = 1; i < n; i++) {
            for (int j = 0; j <= W; j++) {
                if (w[i - 1] > j) {
                    m[i][j] = m[i - 1][j];
                } else {
                    m[i][j] = Math.max(m[i - 1][j], m[i - 1][j - w[i - 1]] + v[i - 1]);
                }
            }
        }

        return m[n - 1][W];
    }

    private int calculateWordScore(String word) {
        int score = 0;
        for (char c : word.toCharArray()) {
            if (gameParameters.getLetterPoints().containsKey(c)) {
                score += gameParameters.getLetterPoints().get(c);
            } else {
                score++;
            }
        }

        return score;
    }

    private int calculateWordCost(String word) {
        return gameParameters.getWordFindTime() + (word.length() - 1) * gameParameters.getLetterIdentifyTime();
    }

    public int getBestScore() {
        return bestScore;
    }

    @Override
    public SearchResult getSearchResult() {
        return searchResult;
    }
}
