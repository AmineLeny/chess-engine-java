package com.chess.engine.Board;

import com.chess.engine.Pieces.Piece;

public abstract class Move {

    final Board board;
    final Piece movedPiece;


    final BoardPosition destinationCoordinate;

    private Move(final Board board, final Piece movedPiece, final BoardPosition destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }
    public BoardPosition getDestinationCoordinate() {
        return destinationCoordinate;
    }



    // attacking move class
    public static final class AttackingMove extends Move {
        final Piece attackedPiece ;

        public AttackingMove(final Board board, final Piece movedPiece,final BoardPosition destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

    }
    // non-attacking move class
    public static final class MajorMove extends Move {
        public MajorMove(final Board board, final Piece movedPiece, final BoardPosition destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

    }






}
