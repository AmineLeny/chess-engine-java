package com.chess.engine.Board;
import com.chess.engine.Pieces.Piece;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

public abstract class Tile {

    protected final int tileCoordinate;

    private static final Map<Integer, EmptyTile > EMPTY_TILES = createAllPossibleEmptyTiles();


    // create all possible Empty Tiles to utilise Immutability
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        Map<Integer, EmptyTile> emptyTiles = new HashMap<Integer, EmptyTile>();
        for ( int i = 0 ; i < 64 ; ++i )  {
            emptyTiles.put(i, new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTiles);

    }

    // factory methode to generate Tiles
    public static Tile CreateTile(int tileCoordinate , Piece piece) {
        return piece == null ? EMPTY_TILES.get(tileCoordinate) : new OccupiedTile(tileCoordinate, piece);
    }

    private Tile(final int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();
    public abstract Piece getPiece();


    // Empty tile class - static keyword is used to prevent this class from inheriting from the enclosing class
    // it's treated as independent, logical components of the Tile class. we can instantiate EmptyTile and OccupiedTile without needing an instance of the enclosing Tile class.
    public static final class EmptyTile extends Tile {
        EmptyTile(int coordinate) {
            super(coordinate);
        }
        @Override
        public boolean isTileOccupied() {
            return false;
        }
        @Override
        public Piece getPiece() {
            return null;
        }
    }


    public static final class OccupiedTile extends Tile {
        Piece pieceOnTile;
        OccupiedTile(int coordinate , Piece piece) {
            super(coordinate);
            this.pieceOnTile = piece;
        }
        @Override
        public boolean isTileOccupied() {
            return true;
        }
        @Override
        public Piece getPiece() {
            return this.pieceOnTile ;
        }
    }


}
