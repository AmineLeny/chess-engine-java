package com.chess.engine.Pieces;

import com.chess.engine.Alliance;
import com.chess.engine.Board.Board;
import com.chess.engine.Board.BoardPosition;
import com.chess.engine.Board.Move;

import java.util.Collection;
import java.util.List;

public abstract class Piece {

    protected final BoardPosition piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;


    Piece(final Alliance pieceAlliance, final BoardPosition piecePosition) {
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
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

}
