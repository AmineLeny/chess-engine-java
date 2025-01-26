package com.chess.engine.Pieces;

import com.chess.engine.Alliance;
import com.chess.engine.Board.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends Piece {

    public King(final Alliance pieceAlliance, final BoardPosition piecePosition, final boolean isFirstMove ) {
        super ( PieceType.KING,pieceAlliance , piecePosition , isFirstMove );
    }

    private static final int[][] LEGAL_CANDIDATE_MOVES = {
            {1,0}, // RIGHT
            {-1,0},  // LEFT
            {0,1}, // UP
            {0,-1}, // DOWN
            {1,1}, // UP-RIGHT
            {-1,1}, // UP-LEFT
            {1,-1}, // DOWN-RIGHT
            {-1,-1}, // DOWN-LEFT
           
    };


    @Override
    public Collection<Move> calculateLegalMove(Board board) {

        List<Move> legalMoves = new ArrayList<>();

        for ( int[] currentCandidate : LEGAL_CANDIDATE_MOVES) {

            final BoardPosition candidateDestinationPosition =  new BoardPosition(piecePosition.x() + currentCandidate[0],piecePosition.y() + currentCandidate[1]);

            if(  BoardUtils.isPositionValid(candidateDestinationPosition)  ) {

                Tile candidateDestinationTile = board.getTile(candidateDestinationPosition);

                if( !candidateDestinationTile.isTileOccupied() )  {
                    legalMoves.add(new Move.MajorMove(board, this , candidateDestinationPosition) );
                }

                else {
                    final Piece candidatePiece = candidateDestinationTile.getPiece();
                    final Alliance candidatePieceAlliance = candidatePiece.getPieceAlliance();
                    if( this.pieceAlliance != candidatePieceAlliance )  {
                        legalMoves.add(new Move.AttackingMove(board,this,candidateDestinationPosition,candidatePiece));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);

    }

    @Override
    public Piece movePiece(Move move) {
        return new King(move.getMovedPieceAlliance(), move.getDestinationCoordinate(),false);
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }
}
