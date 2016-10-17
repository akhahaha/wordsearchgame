package com.akhahaha.wordsearchgame.model;

import java.util.ArrayList;
import java.util.List;

public class GameTile {
    private final GameBoard board;
    private final int row;
    private final int column;
    private final char value;
    // Lazy loaded members
    private boolean isTileAboveCached = false;
    private boolean isTileTopRightCached = false;
    private boolean isTileRightCached = false;
    private boolean isTileBottomRightCached = false;
    private boolean isTileBelowCached = false;
    private boolean isTileBottomLeftCached = false;
    private boolean isTileLeftCached = false;
    private boolean isTileTopLeftCached = false;
    private GameTile tileAbove;
    private GameTile tileTopRight;
    private GameTile tileRight;
    private GameTile tileBottomRight;
    private GameTile tileBelow;
    private GameTile tileBottomLeft;
    private GameTile tileLeft;
    private GameTile tileTopLeft;
    private List<GameTile> neighbors;

    public static GameTile newInstance(GameBoard board, int row, int column, char value) {
        return new GameTile(board, row, column, value);
    }

    private GameTile(GameBoard board, int row, int column, char value) {
        this.board = board;
        this.row = row;
        this.column = column;
        this.value = value;
    }

    public GameBoard getBoard() {
        return board;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public char getValue() {
        return value;
    }

    public GameTile getTileAbove() {
        if (!isTileAboveCached) {
            tileAbove = board.getTile(row - 1, column);
            isTileAboveCached = true;
        }

        return tileAbove;
    }

    public GameTile getTileTopRight() {
        if (!isTileTopRightCached) {
            tileTopRight = board.getTile(row - 1, column + 1);
            isTileTopRightCached = true;
        }

        return tileTopRight;
    }

    public GameTile getTileRight() {
        if (!isTileRightCached) {
            tileRight = board.getTile(row, column + 1);
            isTileRightCached = true;
        }

        return tileRight;
    }

    public GameTile getTileBottomRight() {
        if (!isTileBottomRightCached) {
            tileBottomRight = board.getTile(row + 1, column + 1);
            isTileBottomRightCached = true;
        }

        return tileBottomRight;
    }

    public GameTile getTileBelow() {
        if (!isTileBelowCached) {
            tileBelow = board.getTile(row + 1, column);
            isTileBelowCached = true;
        }

        return tileBelow;
    }

    public GameTile getTileBottomLeft() {
        if (!isTileBottomLeftCached) {
            tileBottomLeft = board.getTile(row + 1, column - 1);
            isTileBottomLeftCached = true;
        }

        return tileBottomLeft;
    }

    public GameTile getTileLeft() {
        if (!isTileLeftCached) {
            tileLeft = board.getTile(row, column - 1);
            isTileLeftCached = true;
        }

        return tileLeft;
    }

    public GameTile getTileTopLeft() {
        if (!isTileTopLeftCached) {
            tileTopLeft = board.getTile(row - 1, column - 1);
            isTileTopLeftCached = true;
        }

        return tileTopLeft;
    }

    public List<GameTile> getNeighbors() {
        if (neighbors == null) {
            neighbors = new ArrayList<>();
            if (getTileAbove() != null) neighbors.add(getTileAbove());
            if (getTileTopRight() != null) neighbors.add(getTileTopRight());
            if (getTileRight() != null) neighbors.add(getTileRight());
            if (getTileBottomRight() != null) neighbors.add(getTileBottomRight());
            if (getTileBelow() != null) neighbors.add(getTileBelow());
            if (getTileBottomLeft() != null) neighbors.add(getTileBottomLeft());
            if (getTileLeft() != null) neighbors.add(getTileLeft());
            if (getTileTopLeft() != null) neighbors.add(getTileTopLeft());
        }

        return neighbors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameTile gameTile = (GameTile) o;

        if (row != gameTile.row) return false;
        if (column != gameTile.column) return false;

        return value == gameTile.value;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        result = 31 * result + (int) value;
        return result;
    }
}
