package com.chess.engine.Board;

import com.chess.engine.Alliance;
import com.chess.engine.Board.Board.BoardBuilder;
import com.chess.engine.Pieces.Piece;

public abstract class Move {

    final Board board;


    final Piece movedPiece;


    final Alliance movedPieceAlliance;
    final BoardPosition destinationCoordinate;

    private Move(final Board board, final Piece movedPiece, final BoardPosition destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPieceAlliance = movedPiece.getPieceAlliance();
    }
    public BoardPosition getDestinationCoordinate() {
        return destinationCoordinate;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public Alliance getMovedPieceAlliance() {
        return movedPieceAlliance;
    }


    public abstract Board execute();


    // attacking move class
    public static final class AttackingMove extends Move {
        final Piece attackedPiece ;

        public AttackingMove(final Board board, final Piece movedPiece,final BoardPosition destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
        @Override
        public Board execute() {
            final BoardBuilder builder = new BoardBuilder();
            for ( final Piece piece :this.board.getCurrentPlayer().getActivePieces()) {
                // TO DO Overrid Hashcode and equals for piece
                if( ! this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }

            }
            for ( final Piece piece :this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                    builder.setPiece(piece);
            }

            //move the moved piece
            builder.setPiece( this.movedPiece.movePiece(this) );
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());

            return builder.build();
        }



    }
    // non-attacking move class
    public static final class MajorMove extends Move {
        public MajorMove(final Board board, final Piece movedPiece, final BoardPosition destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            return null;
        }

    }






}
