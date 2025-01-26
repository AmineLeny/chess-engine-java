package com.chess.engine.Board;

import com.chess.engine.Alliance;
import com.chess.engine.Board.Board.BoardBuilder;
import com.chess.engine.Pieces.Pawn;
import com.chess.engine.Pieces.Piece;
import com.chess.engine.Pieces.Rook;

public abstract class Move {

    final Board board;

    final Piece movedPiece;
    final Alliance movedPieceAlliance;
    final BoardPosition destinationCoordinate;
    public static final Move NULL_MOVE = new NullMove();
    private Move(final Board board, final Piece movedPiece, final BoardPosition destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPieceAlliance = movedPiece.getPieceAlliance();
    }
    private Move(final Board board, final BoardPosition destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.movedPieceAlliance = null;
    }

    @Override
    public int hashCode() {
        return 31* (31* (1+ this.destinationCoordinate.hashCode())) + this.movedPiece.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if( !(other instanceof Move m) ) return false;
        return  m.destinationCoordinate.equals(this.destinationCoordinate) &&
                m.getCurrentCoordinate().equals(this.getCurrentCoordinate()) &&
                m.movedPiece.equals(this.getMovedPiece()) ;


    }


    public BoardPosition getDestinationCoordinate() {
        return destinationCoordinate;
    }

    public BoardPosition getCurrentCoordinate() {
        return movedPiece.getPiecePosition();
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public Alliance getMovedPieceAlliance() {
        return movedPieceAlliance;
    }

    public boolean isAttack(){
        return false;
    }
    public boolean isCastlingMove(){
        return false;
    }
    public Piece getAttackPiece(){
        return null;
    }

    public Board execute() {
        final BoardBuilder builder = new BoardBuilder();
        for ( final Piece piece :this.board.getCurrentPlayer().getActivePieces()) {
            // TO DO Override Hashcode and equals for piece
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


    // attacking move class
    public static class AttackingMove extends Move {
        final Piece attackedPiece ;

        public AttackingMove(final Board board, final Piece movedPiece,final BoardPosition destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
        }
        @Override
        public int hashCode() {
            return attackedPiece.hashCode()+super.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if( this == other ) return true;
            if ( ! (other instanceof  AttackingMove move) ) return false;
            return super.equals(move) && getAttackPiece().equals(move.getAttackPiece());
        }


        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackPiece() {
            return attackedPiece;
        }




    }
    // non-attacking move class
    public static final class MajorMove extends Move {
        public MajorMove(final Board board, final Piece movedPiece, final BoardPosition destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

       }


    public static final class PawnMove extends Move {
        public PawnMove(final Board board, final Piece movedPiece, final BoardPosition destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

    }
    public static  class PawnAttackMove extends AttackingMove {
        public PawnAttackMove(final Board board, final Piece movedPiece, final BoardPosition destinationCoordinate , final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate , attackedPiece);
        }

    }

    public static final class PawnEnPassantAttack extends PawnAttackMove {
        public PawnEnPassantAttack(final Board board, final Piece movedPiece, final BoardPosition destinationCoordinate , final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate , attackedPiece);
        }

    }

    public static final class PawnJump extends Move {
        public PawnJump(final Board board, final Piece movedPiece, final BoardPosition destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            final BoardBuilder builder = new BoardBuilder();
            for ( final Piece piece :this.board.getCurrentPlayer().getActivePieces()) {
                if(!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for ( final Piece piece :this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();


        }

    }

     static abstract class CastleMove extends Move {

        protected Rook castleRook;
        protected BoardPosition castleRookStart;
        protected BoardPosition castleRookDestination;
        public CastleMove(final Board board,
                          final Piece movedPiece,
                          final BoardPosition destinationCoordinate,
                          final Rook castleRook,
                          final BoardPosition castleRookStart,
                          final BoardPosition castleRookDestination
                          ) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

         public Rook getCastleRook() {
             return castleRook;
         }

         @Override
         public Board execute() {
            final BoardBuilder builder = new BoardBuilder();
            for ( final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if( ! this.movedPiece.equals(piece) && ! this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for ( final Piece piece :this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece( this.movedPiece.movePiece(this) );
            builder.setPiece( new Rook(this.castleRook.getPieceAlliance(),this.castleRookDestination,false));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
         }

         @Override
         public boolean isCastlingMove() {
            return true;
         }




     }

    public static final class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(final Board board, final Piece movedPiece,
                                  final BoardPosition destinationCoordinate,
                                  final Rook castleRook,
                                  final BoardPosition castleRookStart,
                                  final BoardPosition castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart,castleRookDestination);
        }

        @Override
        public String toString() {
            return "O-O";
        }

    }

    public static final class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(final Board board, final Piece movedPiece,
                                   final BoardPosition destinationCoordinate,
                                   final Rook castleRook,
                                   final BoardPosition castleRookStart,
                                   final BoardPosition castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart,castleRookDestination);
        }

        @Override
        public String toString() {
            return "O-O-O";
        }

    }

    public static final class NullMove extends Move {
        public NullMove() {
            super(null,null);
        }
        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute NullMove");
        }

    }

    public static class MoveFactory {


        private MoveFactory() {
            throw new RuntimeException("Non instantiable!");
        }

        public static Move createMove(final Board board, final BoardPosition currentCoordinate, final BoardPosition destinationCoordinate) {
            for ( Move move : board.getAllLegalMoves()) {
                if( move.getDestinationCoordinate().equals(destinationCoordinate) && move.getCurrentPosition().equals(currentCoordinate)) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

    private BoardPosition getCurrentPosition() {
        return this.movedPiece.getPiecePosition();
    }

}







