package com.chess.engine.Pieces;

import com.chess.engine.Alliance;
import com.chess.engine.Board.Board;
import com.chess.engine.Board.BoardPosition;
import com.chess.engine.Board.Move;

import java.util.Collection;
import java.util.List;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final BoardPosition piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;

    @Override
    public boolean equals(final Object other) {
        if( this == other ) return true;
        if(!(other instanceof Piece p)) return false;  // pattern matching used here : Automatically casts other to Piece if it's an instance
        return this.pieceAlliance == p.pieceAlliance && this.piecePosition.equals(p.piecePosition) && this.isFirstMove == p.isFirstMove;
    }
    @Override
    public int hashCode() {
        return 31*(31* (31* (31* pieceType.hashCode() + pieceAlliance.hashCode()))+ piecePosition.hashCode())+(isFirstMove ? 1 : 0);

    }





    Piece(final PieceType pieceType ,final Alliance pieceAlliance, final BoardPosition piecePosition) {
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.pieceType = pieceType;
        // work to do here ! ! !
        this.isFirstMove = false;
    }

    public BoardPosition getPiecePosition() {
        return piecePosition;
    }

    public Alliance getPieceAlliance() {
        return pieceAlliance;
    }

    public abstract Collection<Move> calculateLegalMove (final Board board);
    public abstract Piece movePiece(Move move);


    public PieceType getPieceType() {
        return pieceType;
    }


    public enum PieceType {

        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
        };
        private final String pieceType;
        PieceType(final String pieceType) {
            this.pieceType = pieceType;
        }
        @Override
        public String toString() {
            return pieceType;
        }

        public abstract boolean isKing();

    }

}
