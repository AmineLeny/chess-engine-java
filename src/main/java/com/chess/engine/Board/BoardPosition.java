package com.chess.engine.Board;

public record BoardPosition(Integer x, Integer y) {

    @Override
    public String toString() {
        return "BoardPosition{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
