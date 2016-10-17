package com.akhahaha.wordsearchgame.model;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private final int rowCount;
    private final int columnCount;
    private final List<GameTile> tiles = new ArrayList<>();

    public static GameBoard newInstance(int rowCount, int columnCount, char[][] board) {
        return new GameBoard(rowCount, columnCount, board);
    }

    private GameBoard(int rowCount, int columnCount, char[][] board) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                tiles.add(GameTile.newInstance(this, row, column, board[row][column]));
            }
        }
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public boolean isPositionValid(int row, int column) {
        return row >= 0 && row < rowCount && column >= 0 && column < columnCount;
    }

    private int getIndex(int row, int column) {
        return row * columnCount + column;
    }

    public GameTile getTile(int row, int column) {
        if (!isPositionValid(row, column)) {
            return null;
        }

        return tiles.get(getIndex(row, column));
    }

    public List<GameTile> getTiles() {
        return tiles;
    }
}
