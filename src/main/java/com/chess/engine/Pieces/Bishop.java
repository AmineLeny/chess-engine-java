package com.chess.engine.Pieces;

import com.chess.engine.Alliance;
import com.chess.engine.Board.*;
import com.chess.engine.Board.Move.AttackingMove;
import com.chess.engine.Board.Move.MajorMove;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(Alliance alliance, BoardPosition piecePosition) {
        super(alliance, piecePosition);
    }

    private final int[][] CANDIDATE_MOVE_VECTOR_COORDINATES = {
            {1, 1}, // UP-RIGHT
            {1, -1}, // DOWN-RIGHT
            {-1, 1}, // UP-LEFT
            {-1, -1}}; // DOWN-LEFT


    @Override
    public Collection<Move> calculateLegalMove(final Board board) {

        List<Move> legalMoves = new ArrayList<>();

        for (int[] currentCandidate : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int xCandidateDestinationPosition = this.piecePosition.x();
            int yCandidateDestinationPosition = this.piecePosition.y();

            while (true) {

                xCandidateDestinationPosition += currentCandidate[0];
                yCandidateDestinationPosition += currentCandidate[1];
                BoardPosition candidateDestinationPosition = new BoardPosition(xCandidateDestinationPosition, yCandidateDestinationPosition);

                if (!BoardUtils.isPositionValid(candidateDestinationPosition)) {
                    break; //out of bounds
                }
                else {
                    Tile candidateDestinationTile = board.getTile(candidateDestinationPosition);

                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new MajorMove(board, this, candidateDestinationPosition));
                    } else {
                        final Piece candidatePiece = candidateDestinationTile.getPiece();
                        final Alliance candidatePieceAlliance = candidatePiece.getPieceAlliance();
                        if (this.pieceAlliance != candidatePieceAlliance) {
                            legalMoves.add(new AttackingMove(board, this, candidateDestinationPosition, candidatePiece));
                        }

                        // the path is blocked either by an enemy-piece or an ally-piece so we break out of the loop
                        break;
                    }

                }


            }


        }
        return ImmutableList.copyOf(legalMoves);
    }

}
