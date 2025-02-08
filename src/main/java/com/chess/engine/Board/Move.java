package com.chess.engine.Board;

import com.chess.engine.Alliance;
import com.chess.engine.Board.Board.BoardBuilder;
import com.chess.engine.Pieces.Pawn;
import com.chess.engine.Pieces.Piece;
import com.chess.engine.Pieces.Piece.PieceType;
import com.chess.engine.Pieces.Rook;

public abstract class Move {

    final Board board;

    final Piece movedPiece;
    final Alliance movedPieceAlliance;
    final BoardPosition destinationCoordinate;
    final boolean isFirstMove;
    public static final Move NULL_MOVE = new NullMove();
    private Move(final Board board, final Piece movedPiece, final BoardPosition destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPieceAlliance = movedPiece.getPieceAlliance();
        this.isFirstMove = movedPiece.isFirstMove();
    }
    private Move(final Board board, final BoardPosition destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.movedPieceAlliance = null;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode() {
        return 31*(31* (31* (1+ this.destinationCoordinate.hashCode())) + this.movedPiece.hashCode())+this.movedPiece.getPiecePosition().hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if( !(other instanceof Move m) ) return false;
        return  m.destinationCoordinate.equals(this.destinationCoordinate) &&
                m.getCurrentCoordinate().equals(this.getCurrentCoordinate()) &&
                m.movedPiece.equals(this.getMovedPiece()) ;


    }

    String disambiguationFile() {
        for(final Move move : this.board.getCurrentPlayer().getLegalMovesPlayer()) {
            if(move.getDestinationCoordinate() == this.destinationCoordinate && !this.equals(move) &&
                    this.movedPiece.getPieceType().equals(move.getMovedPiece().getPieceType())) {
                return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1);
            }
        }
        return "";
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
        for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
        // Clear en passant pawn unless this is a pawn jump move
        if (!(this instanceof PawnJump)) {
            builder.setEnPassantPawn(null);
        }
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
        public int hashCode() {
            return attackedPiece.hashCode()+super.hashCode();
        }

        @Override
        public BoardPosition getDestinationCoordinate() {
            return attackedPiece.getPiecePosition();
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
        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()) + disambiguationFile() + "x" +
                    BoardUtils.getPositionAtCoordinate(this.attackedPiece.getPiecePosition());
        }




    }

    public static class MajorAttackMove
            extends AttackingMove {

        public MajorAttackMove(final Board board,
                               final Piece pieceMoved,
                               final BoardPosition destinationCoordinate,
                               final Piece pieceAttacked) {
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof MajorAttackMove && super.equals(other);

        }

        @Override
        public String toString() {
            return movedPiece.getPieceType() + disambiguationFile() + "x" +
                    BoardUtils.getPositionAtCoordinate(this.attackedPiece.getPiecePosition());
        }
        @Override
        public Board execute() {
            final BoardBuilder builder = new BoardBuilder();
            for ( final Piece piece :this.board.getCurrentPlayer().getActivePieces()) {
                if( ! this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }

            }
            for ( final Piece piece :this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                if(!this.attackedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }

            //move the moved piece

            builder.setPiece( movedPiece.movePiece(this) );
            builder.setMoveMaker(this.board.getOpponentPlayer().getAlliance());


            return builder.build();
        }

    }


    // non-attacking move class
    public static final class MajorMove extends Move {
        public MajorMove(final Board board, final Piece movedPiece, final BoardPosition destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
        @Override
        public boolean equals(final Object other) {
            if( this == other ) return true;
            if ( ! (other instanceof  MajorMove move) ) return false;
            return super.equals(other);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate) ;
        }


       }
    public static class PawnPromotion
            extends Move {

        final Move decoratedMove;
        final Pawn promotedPawn;
        public PawnPromotion(final Move decoratedMove){
            super(decoratedMove.getBoard(),decoratedMove.getMovedPiece(),decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
        }


        @Override
        public boolean equals(Object o) {
            return this==o || o instanceof PawnPromotion && super.equals(o);
        }

        @Override
        public int hashCode() {
            return decoratedMove.hashCode() + 31 * (promotedPawn.hashCode());
        }

        @Override
        public Board execute() {
            final Board pawnMovedBoard = this.decoratedMove.execute();
            final BoardBuilder builder = new BoardBuilder();

            System.out.println("Before filtering - Active pieces: " +
                    pawnMovedBoard.getCurrentPlayer().getActivePieces().size());
            if (decoratedMove.getAttackPiece() == null) {
                for ( final Piece piece : pawnMovedBoard.getCurrentPlayer().getActivePieces()){
                    if(!this.promotedPawn.equals(piece)) {
                        builder.setPiece(piece);
                    }
                }
                for( final Piece piece : pawnMovedBoard.getCurrentPlayer().getOpponent().getActivePieces()){
                    builder.setPiece(piece);
                }

                builder.setPiece(this.promotedPawn.getPromotionPiece(this).movePiece(this));

            }else {
                for ( final Piece piece : pawnMovedBoard.getCurrentPlayer().getActivePieces()){
                    if(!this.promotedPawn.equals(piece)) {
                        builder.setPiece(piece);
                    }
                }
                for( final Piece piece : pawnMovedBoard.getCurrentPlayer().getOpponent().getActivePieces()){
                  if(!decoratedMove.getAttackPiece().equals(piece))  builder.setPiece(piece);
                }

                builder.setPiece(this.promotedPawn.getPromotionPiece(this).movePiece(this));
            }


            builder.setMoveMaker(pawnMovedBoard.getCurrentPlayer().getAlliance());
            return builder.build();
        }

        @Override
        public boolean isAttack() {
            return this.decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackPiece() {
            return this.decoratedMove.getAttackPiece();
        }

        @Override
        public String toString() {
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()) + "-" +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate) + "=" + PieceType.QUEEN.toString();
        }

    }



    private Board getBoard() {
        return this.board;
    }


    public static class PawnMove extends Move {
        public PawnMove(final Board board, final Piece movedPiece, final BoardPosition destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnMove && super.equals(other);
        }
        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }


    }
    public static  class PawnAttackMove extends AttackingMove {
        public PawnAttackMove(final Board board, final Piece movedPiece, final BoardPosition destinationCoordinate , final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate , attackedPiece);
        }
        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }



    }

    public static final class PawnEnPassantAttack extends PawnAttackMove {
        public PawnEnPassantAttack(final Board board, final Piece movedPiece, final BoardPosition destinationCoordinate , final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate , attackedPiece);
        }
        @Override
        public boolean equals(final Object other) {
            return this==other || other instanceof PawnEnPassantAttack && super.equals(other);
        }
        @Override
        public BoardPosition getDestinationCoordinate(){
            return this.destinationCoordinate;
        }

        @Override
        public Board execute() {
            final BoardBuilder builder = new BoardBuilder();
            for ( final Piece piece :this.board.getCurrentPlayer().getActivePieces()) {
                if( ! this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for ( final Piece piece :this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                if(!this.attackedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(movedPiece.movePiece(this) );
            builder.setMoveMaker(this.board.getOpponentPlayer().getAlliance());
            return builder.build();
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
        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate) ;
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
            builder.setPiece( new Rook(this.castleRook.getPieceAlliance(),this.castleRookDestination));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
         }

         @Override
         public boolean isCastlingMove() {
            return true;
         }



          @Override
          public int hashCode() {
              final int prime = 31;
              int result = super.hashCode();
              result = prime * result + this.castleRook.hashCode();
              result = prime * result + this.castleRookDestination.hashCode();
              return result;
          }

          @Override
          public boolean equals(final Object other) {
              if (this == other) {
                  return true;
              }
              if (!(other instanceof CastleMove otherCastleMove)) {
                  return false;
              }
              return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
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
        @Override
        public boolean isCastlingMove() {
            return true;
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
        @Override
        public boolean isCastlingMove() {
            return true;
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







