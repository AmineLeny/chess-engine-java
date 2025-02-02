package com.chess.engine.Pieces;

import com.chess.engine.Alliance;
import com.chess.engine.Board.*;
import com.chess.engine.Board.Move.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Alliance alliance, BoardPosition piecePosition,final boolean isFirstMove){
        super(PieceType.PAWN,alliance, piecePosition, isFirstMove);

    }
    public Pawn(Alliance alliance, BoardPosition piecePosition) {
        super(PieceType.PAWN,alliance, piecePosition, true);
    }

    private static final int[][] CANDIDATE_MOVE_VECTOR_COORDINATES = {
            {0, 1}, // move forward 1 tile
            {1, 1}, // move diagonally to the left 1 tile
            {-1,1}  // move diagonally to the right 1 tile
    };

    @Override
    public Collection<Move> calculateLegalMove(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        final int pieceDirection = this.getPieceAlliance().getDirection();

        // Forward and diagonal moves
        for (int[] newPosition : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int xPosition = this.piecePosition.x() + newPosition[0];
            int yPosition = this.piecePosition.y() + newPosition[1] * pieceDirection;
            BoardPosition candidatePosition = new BoardPosition(xPosition, yPosition);

            if (!BoardUtils.isPositionValid(candidatePosition)) {
                continue;
            }

            Tile candidateTile = board.getTile(candidatePosition);

            // Forward moves
            if (newPosition[0] == 0) {  // Moving straight
                if (!candidateTile.isTileOccupied()) {
                    if(this.pieceAlliance.isPawnPromotionSquare(candidatePosition)) {
                        legalMoves.add(new PawnPromotion(new PawnMove(board,this,candidatePosition)));
                    }
                    else {
                        legalMoves.add(new PawnMove(board, this, candidatePosition));
                    }

                    // Two-square first move
                    if ((this.pieceAlliance == Alliance.WHITE && this.piecePosition.y() == 1) ||
                            (this.pieceAlliance == Alliance.BLACK && this.piecePosition.y() == 6)) {

                        BoardPosition jumpPosition = new BoardPosition(xPosition, yPosition + pieceDirection);
                        if (BoardUtils.isPositionValid(jumpPosition) &&
                                !board.getTile(jumpPosition).isTileOccupied()) {
                            legalMoves.add(new PawnJump(board, this, jumpPosition));
                        }
                    }
                }
            }
            // Diagonal captures
            else if (candidateTile.isTileOccupied() &&
                    candidateTile.getPiece().pieceAlliance != this.pieceAlliance) {
                if(this.pieceAlliance.isPawnPromotionSquare(candidatePosition)) {
                    legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidatePosition,
                            candidateTile.getPiece())));
                }
                else {
                    legalMoves.add(new PawnAttackMove(board, this, candidatePosition,
                            candidateTile.getPiece()));
                }
            }
        }
        // En Passant (checked separately from regular moves)
        if (board.getEnPassantPawn() != null) {
            Pawn enPassantPawn = board.getEnPassantPawn();
            if (Math.abs(this.piecePosition.x() - enPassantPawn.getPiecePosition().x()) == 1) {
                int correctRow = this.pieceAlliance == Alliance.WHITE ? 4 : 3;
                if (this.piecePosition.y() == correctRow) {
                    BoardPosition capturePosition = new BoardPosition(
                            enPassantPawn.getPiecePosition().x() ,
                           ( this.piecePosition.y() +1)
                    );
                    if (BoardUtils.isPositionValid(capturePosition)) {
                       Move a = new PawnEnPassantAttack(
                                board,
                                this,
                                capturePosition,
                                enPassantPawn
                        );
                        legalMoves.add(new PawnEnPassantAttack(
                                board,
                                this,
                                capturePosition,
                                enPassantPawn
                        ));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Piece movePiece(Move move) {
        return new Pawn(move.getMovedPieceAlliance(), move.getDestinationCoordinate() , false);
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    public Piece getPromotionPiece(PawnPromotion pawnPromotion) {
        return new Queen(this.pieceAlliance,this.piecePosition,false);
    }
}

