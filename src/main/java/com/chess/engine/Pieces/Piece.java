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







    Piece(final PieceType pieceType ,final Alliance pieceAlliance, final BoardPosition piecePosition, final boolean isFirstMove) {
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.pieceType = pieceType;
        // work to do here ! ! !
        this.isFirstMove = isFirstMove;
    }
    public boolean isFirstMove() {
        return isFirstMove;
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

    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }


    public enum PieceType {

        BISHOP("B",300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K",10000) {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N",300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        PAWN("P",100) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        QUEEN("Q", 900) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R",500) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        };
        private final String pieceType;
        private final int pieceValue;
        PieceType(final String pieceType,  final int pieceValue) {
            this.pieceType = pieceType;
            this.pieceValue = pieceValue;
        }
        @Override
        public String toString() {
            return pieceType.toString();
        }

        public abstract boolean isKing();

        public abstract boolean isRook();

        public int getPieceValue() {
            return this.pieceValue;
         }
    }

}
