package main;

import com.chess.engine.Board.Board;
import com.chess.engine.Board.BoardPosition;
import com.chess.engine.Board.Tile;

public class Main {
    public static void main(String[] args) {

        Board board = Board.createStandardBoard();

        System.out.println(board.toString());


    }
}
