package com.chess.engine.Player;

import com.chess.engine.Alliance;
import com.chess.engine.Board.Board;
import com.chess.engine.Board.Move;
import com.chess.engine.Pieces.Piece;

import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player {
    public BlackPlayer(Board board, Collection<Move> whiteStandardMoves, Collection<Move> blackStandardMoves) {
        super(board, blackStandardMoves,whiteStandardMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {

        // until now
     return this.board.getWhitePlayer();
    }
}
