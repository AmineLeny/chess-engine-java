package main;

import com.chess.engine.Board.BoardPosition;
import com.chess.engine.Board.Tile;

public class Main {
    public static void main(String[] args) {

               Tile.EMPTY_TILES_CACHE.forEach((key, value) -> System.out.println(key.x() + "," + key.y() + " -> " + value.isTileOccupied()));




    }
}
