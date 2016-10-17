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
        // TODO: Find best possible score
        this.searchResult = SearchResult.newInstance(wordsFound, 0);
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

    @Override
    public SearchResult getSearchResult() {
        return searchResult;
    }
}
