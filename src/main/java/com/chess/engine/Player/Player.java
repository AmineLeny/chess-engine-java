package com.chess.engine.Player;

import com.chess.engine.Alliance;
import com.chess.engine.Board.Board;
import com.chess.engine.Board.BoardPosition;
import com.chess.engine.Board.Move;
import com.chess.engine.Pieces.King;
import com.chess.engine.Pieces.Piece;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Player {
    protected final Board board;
    protected final King playerKing;


    protected final Collection<Move> legalMoves;
    protected final boolean isInCheck;

    Player(Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;
        this.isInCheck = ! this.calculateAttackOnTile(this.playerKing.getPiecePosition() , opponentMoves).isEmpty();

    }


    public Collection<Move> getLegalMoves() {
        return legalMoves;
    }

    private Collection<Move> calculateAttackOnTile(BoardPosition piecePosition, Collection<Move> opponentMoves) {

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
        return legalMoves.contains(move);
    }
// don't forget to implement these methods

    public boolean isIncheck() {
        return this.isInCheck;
    }

    public boolean isInCheckMate() {
        return this.isInCheck && ! hasEscapeMoves() ;
    }
    public boolean isInStalemate() {
        return !isInCheck && ! hasEscapeMoves() ;
    }

    protected boolean hasEscapeMoves() {
        for( final Move move : legalMoves) {
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
        if(! isMoveLegal(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();
        final Collection<Move> kingAttacks = new ArrayList<>();
    }

    public abstract Collection<Piece>  getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
}
