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

public class BlackPlayer extends Player {
    public BlackPlayer(final Board board,final Collection<Move> blackStandardMoves, final Collection<Move> whiteStandardMoves) {
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
    @Override
    public String toString() {
        return "Black";
    }

    @Override
    public Collection<Move> calculateKingCastles(final Collection<Move> playerLegalMoves, final Collection<Move> opponentLegalMoves) {
        final List<Move> kingCastles = new ArrayList<>();

        if (this.playerKing.isFirstMove() && !this.getIsInCheck()) {
            // King-side castling (short castling for black)
            final Tile tile5 = board.getTile(new BoardPosition(5, 7));
            final Tile tile6 = board.getTile(new BoardPosition(6, 7));
            final Tile rookTileKingSide = board.getTile(new BoardPosition(7, 7));

            if (!tile5.isTileOccupied() && !tile6.isTileOccupied() &&
                    rookTileKingSide.isTileOccupied() && rookTileKingSide.getPiece().isFirstMove() &&
                    calculateAttackOnTile(new BoardPosition(5, 7), opponentLegalMoves).isEmpty() &&
                    calculateAttackOnTile(new BoardPosition(6, 7), opponentLegalMoves).isEmpty() &&
                    rookTileKingSide.getPiece().getPieceType().isRook()) {

                kingCastles.add(new Move.KingSideCastleMove(
                        this.board,
                        this.getPlayerKing(),
                        new BoardPosition(6, 7),
                        (Rook) rookTileKingSide.getPiece(),
                        rookTileKingSide.getPiece().getPiecePosition(),
                        new BoardPosition(5, 7)
                ));
            }

            // Queen-side castling (long castling for black)
            final Tile tile3 = board.getTile(new BoardPosition(3, 7));
            final Tile tile2 = board.getTile(new BoardPosition(2, 7));
            final Tile tile1 = board.getTile(new BoardPosition(1, 7));
            final Tile rookTileQueenSide = board.getTile(new BoardPosition(0, 7));

            if (!tile3.isTileOccupied() && !tile2.isTileOccupied() && !tile1.isTileOccupied() &&
                    rookTileQueenSide.isTileOccupied() && rookTileQueenSide.getPiece().isFirstMove() &&
                    calculateAttackOnTile(new BoardPosition(3, 7), opponentLegalMoves).isEmpty() &&
                    calculateAttackOnTile(new BoardPosition(2, 7), opponentLegalMoves).isEmpty() &&
                    rookTileQueenSide.getPiece().getPieceType().isRook()) {

                kingCastles.add(new Move.QueenSideCastleMove(
                        this.board,
                        this.getPlayerKing(),
                        new BoardPosition(2, 7),
                        (Rook) rookTileQueenSide.getPiece(),
                        rookTileQueenSide.getPiece().getPiecePosition(),
                        new BoardPosition(3, 7)
                ));
            }
        }

        return kingCastles;
    }


}
