package main;

import com.chess.engine.Board.BoardPosition;
import com.chess.engine.Board.Tile;

public class Main {
    public static void main(String[] args) {

               boolean  a =  Tile.EMPTY_TILES_CACHE.get(new BoardPosition(7,5)).isTileOccupied();
               System.out.println(a);



    }
}
