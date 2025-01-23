package com.chess.engine.Pieces;
import com.chess.engine.Alliance;
import com.chess.engine.Board.*;
import com.chess.engine.Board.Move.AttackingMove;
import com.chess.engine.Board.Move.MajorMove;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rook extends Piece {


    public Rook(Alliance alliance, BoardPosition piecePosition){
        super(alliance, piecePosition);
    }

    private static final int[][] CANDIDATE_MOVE_VECTOR_COORDINATES = {
            {0, 1}, // UP
            {0, -1}, // DOWN
            {1, 0}, // RIGHT
            {-1, 0}// LEFT
    };


    @Override
    public Collection<Move> calculateLegalMove(final Board board) {

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
                        legalMoves.add(new MajorMove(board, this, candidateDestinationPosition));
                    } else {
                        final Piece candidatePiece = candidateDestinationTile.getPiece();
                        final Alliance candidatePieceAlliance = candidatePiece.getPieceAlliance();
                        if (this.pieceAlliance != candidatePieceAlliance) {
                            legalMoves.add(new AttackingMove(board, this, candidateDestinationPosition, candidatePiece));
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
        return PieceType.ROOK.toString();
    }

}
