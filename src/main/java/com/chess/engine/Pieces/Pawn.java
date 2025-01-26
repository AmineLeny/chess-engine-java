package com.chess.engine.Pieces;

import com.chess.engine.Alliance;
import com.chess.engine.Board.*;
import com.chess.engine.Board.Move.AttackingMove;
import com.chess.engine.Board.Move.MajorMove;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(Alliance alliance, BoardPosition piecePosition,final boolean isFirstMove) {
        super(PieceType.PAWN,alliance, piecePosition, isFirstMove);
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


            for (int[] newPosition : CANDIDATE_MOVE_VECTOR_COORDINATES) {
                // since pawn can only move one tile
                int xPosition = this.piecePosition.x() + newPosition[0];
                int yPosition = this.piecePosition.y() + newPosition[1] * pieceDirection;
                BoardPosition candidatePosition = new BoardPosition(xPosition, yPosition);
                if (!BoardUtils.isPositionValid(candidatePosition)) {
                    continue;
                }

                Tile candidateTile = board.getTile(candidatePosition);
                if (!candidateTile.isTileOccupied()) {
                    // if tiles are not occupied we can only move forward
                    if(newPosition[0] == 0) {

                            legalMoves.add(new MajorMove(board, this, candidatePosition));

                            // check if we can do a jump move  2 tiles forward
                            if (
                                    (this.pieceAlliance == Alliance.WHITE && this.piecePosition.y() == 1)
                                            ||
                                            (this.pieceAlliance == Alliance.BLACK && this.piecePosition.y() == 6)

                                )
                            {
                                BoardPosition jumpPosition = new BoardPosition(xPosition, yPosition + pieceDirection);
                                if (
                                        // checking if we can do a jump move 2 tile should be unoccupied
                                        !(board.getTile(jumpPosition).isTileOccupied())
                                        && BoardUtils.isPositionValid(jumpPosition)

                                ) {
                                    legalMoves.add(new MajorMove(board, this, jumpPosition));
                                }
                            }

                    }


                }
                // we can move diagonally only if tiles are occupied by enemy pieces
                else {
                    if(
                            newPosition[0] != 0  // means that we're moving diagonally
                             &&
                            candidateTile.getPiece().pieceAlliance != this.pieceAlliance
                    ) {
                        legalMoves.add(new AttackingMove(board,this, candidatePosition,candidateTile.getPiece()));
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
}

