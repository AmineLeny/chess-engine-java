package com.chess.engine.Player;

import com.chess.engine.Alliance;
import com.chess.engine.Board.Board;
import com.chess.engine.Board.BoardPosition;
import com.chess.engine.Board.Move;
import com.chess.engine.Pieces.King;
import com.chess.engine.Pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Player {
    protected final Board board;



    protected final King playerKing;


    protected final Collection<Move> legalMovesPlayer;
    protected final boolean isInCheck;

    Player(Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMovesPlayer = ImmutableList.copyOf(Iterables.concat(legalMoves, this.calculateKingCastles(legalMoves,opponentMoves)));
        this.isInCheck = ! this.calculateAttackOnTile(this.playerKing.getPiecePosition() , opponentMoves).isEmpty();

    }

    public King getPlayerKing() {
        return playerKing;
    }

    public Collection<Move> getLegalMovesPlayer() {
        return legalMovesPlayer;
    }

     public Collection<Move> calculateAttackOnTile(BoardPosition piecePosition, Collection<Move> opponentMoves) {

            Collection<Move> attackingMoves = new ArrayList<>();
            for ( Move move : opponentMoves) {
                if(move.getDestinationCoordinate().equals(piecePosition)) {
                        attackingMoves.add(move);
                }
            }
            return ImmutableList.copyOf(attackingMoves);

    }

    private King establishKing() {
        for ( final Piece piece : getActivePieces()) {
            if(piece.getPieceType().isKing()){
                return (King) piece;
            }

        }
        throw new RuntimeException("Not a valid Board");

    }

    public boolean isMoveLegal(final Move move) {

        return legalMovesPlayer.contains(move);
    }
// don't forget to implement these methods

    public boolean getIsInCheck() {
        return this.isInCheck;
    }

    public boolean isInCheckMate() {
        return this.isInCheck && ! hasEscapeMoves() ;
    }
    public boolean isInStalemate() {
        return !isInCheck && ! hasEscapeMoves() ;
    }

    protected boolean hasEscapeMoves() {
        for( final Move move : legalMovesPlayer) {
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
            }
        return false;
        }


    public boolean isCastled(){
        return false;
    }

    public MoveTransition makeMove(final Move move) {
         if( ! this.legalMovesPlayer.contains(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();
        final Collection<Move> kingAttacks = this.calculateAttackOnTile(transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.getCurrentPlayer().getLegalMovesPlayer());
        if(! kingAttacks.isEmpty() ) return new MoveTransition(transitionBoard, move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
        return new MoveTransition(transitionBoard,move, MoveStatus.DONE);

    }

    public abstract Collection<Piece>  getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    public abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegalMoves , Collection<Move> opponentLegalMoves);
}
