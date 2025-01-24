package com.chess.engine.Player;

import com.chess.engine.Alliance;
import com.chess.engine.Board.Board;
import com.chess.engine.Board.Move;
import com.chess.engine.Pieces.Piece;

import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player {
    public WhitePlayer(Board board, Collection<Move> whiteStandardMoves, Collection<Move> blackStandardMoves) {
        super(board,whiteStandardMoves,blackStandardMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {

        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }
}
