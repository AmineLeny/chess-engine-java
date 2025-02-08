package com.chess.engine.Player;

import com.chess.engine.Alliance;
import com.chess.engine.Board.Board;
import com.chess.engine.Board.BoardPosition;
import com.chess.engine.Board.Move;
import com.chess.engine.Board.Tile;
import com.chess.engine.Pieces.Piece;
import com.chess.engine.Pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WhitePlayer extends Player {
    public WhitePlayer(final Board board,final Collection<Move> whiteStandardMoves, final Collection<Move> blackStandardMoves) {
        super(board,whiteStandardMoves,blackStandardMoves);
    }


    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }
    @Override
    public String toString() {
        return "White";
    }
    @Override
    public Alliance getAlliance() {

        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }

    @Override
    public Collection<Move> calculateKingCastles(final Collection<Move> playerLegalMoves, final Collection<Move> opponentLegalMoves) {
        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.getIsInCheck()) {
            // King-side castling (short castling for white)
            Tile tile5 = board.getTile(new BoardPosition(5, 0));
            Tile tile6 = board.getTile(new BoardPosition(6, 0));
            Tile rookTileKingSide = board.getTile(new BoardPosition(7, 0));

            if (!tile5.isTileOccupied() && !tile6.isTileOccupied() &&
                    rookTileKingSide.isTileOccupied() && rookTileKingSide.getPiece().isFirstMove() &&
                    calculateAttackOnTile(new BoardPosition(5, 0), opponentLegalMoves).isEmpty() &&
                    calculateAttackOnTile(new BoardPosition(6, 0), opponentLegalMoves).isEmpty() &&
                    rookTileKingSide.getPiece().getPieceType().isRook()) {

                kingCastles.add(new Move.KingSideCastleMove(
                        this.board,
                        this.getPlayerKing(),
                        new BoardPosition(6, 0),
                        (Rook)rookTileKingSide.getPiece(),
                        rookTileKingSide.getPiece().getPiecePosition(),
                        new BoardPosition(5, 0)));
            }

            // Queen-side castling (long castling for white)
            Tile tile3 = board.getTile(new BoardPosition(3, 0));
            Tile tile2 = board.getTile(new BoardPosition(2, 0));
            Tile tile1 = board.getTile(new BoardPosition(1, 0));
            Tile rookTileQueenSide = board.getTile(new BoardPosition(0, 0));

            if (!tile3.isTileOccupied() && !tile2.isTileOccupied() && !tile1.isTileOccupied() &&
                    rookTileQueenSide.isTileOccupied() && rookTileQueenSide.getPiece().isFirstMove() &&
                    calculateAttackOnTile(new BoardPosition(3, 0), opponentLegalMoves).isEmpty() &&
                    calculateAttackOnTile(new BoardPosition(2, 0), opponentLegalMoves).isEmpty() &&
                    rookTileQueenSide.getPiece().getPieceType().isRook()) {

                kingCastles.add(new Move.QueenSideCastleMove(
                        this.board,
                        this.getPlayerKing(),
                        new BoardPosition(2, 0),
                        (Rook)rookTileQueenSide.getPiece(),
                        rookTileQueenSide.getPiece().getPiecePosition(),
                        new BoardPosition(3, 0)));
            }
        }

        return kingCastles;
    }



}
