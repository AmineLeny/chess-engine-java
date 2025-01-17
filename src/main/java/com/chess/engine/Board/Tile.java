package com.chess.engine.Board;
import com.chess.engine.Pieces.Piece;

public abstract class Tile {

    int tileCoordinate;
    Tile(int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isTileOccupied();
    public abstract Piece getPiece();

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
