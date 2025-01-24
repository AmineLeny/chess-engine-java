package com.chess.engine.Pieces;

import com.chess.engine.Alliance;
import com.chess.engine.Board.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen extends Piece {


    private static final int[][] CANDIDATE_MOVE_VECTOR_COORDINATES = {
            {1, 1}, // UP-RIGHT
            {1, -1}, // DOWN-RIGHT
            {-1, 1}, // UP-LEFT
            {-1, -1},
            {0, 1}, // UP
            {0, -1}, // DOWN
            {1, 0}, // RIGHT
            {-1, 0} }; // DOWN-LEFT


    public Queen (Alliance alliance , BoardPosition piecePosition) {
        super( PieceType.QUEEN,alliance , piecePosition);
    }
    @Override
    public Collection<Move> calculateLegalMove(Board board) {



        List<Move> legalMoves = new ArrayList<>();

        for (int[] currentCandidate : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateX = this.piecePosition.x();
            int candidateY = this.piecePosition.y();

            while (true) {

                candidateX += currentCandidate[0];
                candidateY += currentCandidate[1];
                BoardPosition candidateDestinationPosition = new BoardPosition(candidateX, candidateY);

                if (!BoardUtils.isPositionValid(candidateDestinationPosition)) {
                    break; //out of bounds
                }
                else {
                    Tile candidateDestinationTile = board.getTile(candidateDestinationPosition);

                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationPosition));
                    } else {
                        final Piece candidatePiece = candidateDestinationTile.getPiece();
                        final Alliance candidatePieceAlliance = candidatePiece.getPieceAlliance();
                        if (this.pieceAlliance != candidatePieceAlliance) {
                            legalMoves.add(new Move.AttackingMove(board, this, candidateDestinationPosition, candidatePiece));
                        }

                        break; // the path is blocked either by an enemy-piece or an ally-piece so we break out of the loop
                    }

                }


            }


        }
        return ImmutableList.copyOf(legalMoves);




    }

    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }
}
