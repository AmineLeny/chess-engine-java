package com.chess.engine.Player;

import com.chess.engine.Alliance;
import com.chess.engine.Board.Board;
import com.chess.engine.Board.Move;
import com.chess.engine.Pieces.King;
import com.chess.engine.Pieces.Piece;

import java.util.Collection;

public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;

    Player(Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;

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
        return false;
    }

    public boolean isInCheckMate() {
        return false;
    }

    public boolean isCastled(){
        return false;
    }

    public MoveTransition makeMove(final Move move) {
        return null;
    }

    public abstract Collection<Piece>  getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
}
