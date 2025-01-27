package com.chess.engine.Board;
import com.chess.engine.Alliance;
import com.chess.engine.Pieces.Piece;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

public abstract class Tile {


    protected final BoardPosition tileCoordinate;

    public static final Map<BoardPosition, EmptyTile > EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();


    // create all possible Empty Tiles to utilise Immutability
    private static Map<BoardPosition, EmptyTile> createAllPossibleEmptyTiles() {
        Map<BoardPosition, EmptyTile> emptyTiles = new HashMap<>();
        for ( int i = 0 ; i < 8 ; ++i )  {
            for ( int j = 0 ; j < 8 ; ++j )  {
                emptyTiles.put(new BoardPosition(i,j), new EmptyTile(i,j));
            }

        }
        return ImmutableMap.copyOf(emptyTiles);

    }

    public BoardPosition getTileCoordinate() {
        return tileCoordinate;
    }


    // factory methode to generate Tiles
    public static Tile createTile(final int xCoordinate , final int yCoordinate , Piece piece) {
        return piece == null ? EMPTY_TILES_CACHE.get(new BoardPosition(xCoordinate,yCoordinate)) : new OccupiedTile(xCoordinate,yCoordinate , piece);
    }

    private Tile(final int xCoordinate , final int yCoordinate) {
        this.tileCoordinate = new BoardPosition(xCoordinate, yCoordinate);
    }

    public abstract boolean isTileOccupied();
    public abstract Piece getPiece();


    // Empty tile class - static keyword is used to prevent this class from inheriting from the enclosing class
    // it's treated as independent, logical components of the Tile class. we can instantiate EmptyTile and OccupiedTile without needing an instance of the enclosing Tile class.
    public static final class EmptyTile extends Tile {
        private EmptyTile(final int xCoordinate , final int yCoordinate) {
            super(xCoordinate, yCoordinate);
        }

        @Override
        public String toString() {
            return "-";

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
        private OccupiedTile(final int xCoordinate , final int yCoordinate , Piece piece) {
            super(xCoordinate, yCoordinate);
            this.pieceOnTile = piece;
        }

        @Override
        public String toString() {
            String pieceString = getPiece().toString();  // Get the piece's string representation
            // write lower
            return this.getPiece().getPieceAlliance().isWhite() ? pieceString.toLowerCase() : pieceString;
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
