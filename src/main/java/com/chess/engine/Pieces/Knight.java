package com.chess.engine.Pieces;
import com.chess.engine.Alliance;
import com.chess.engine.Board.*;
import com.chess.engine.Board.Move.AttackingMove;
import com.chess.engine.Board.Move.MajorMove;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Piece {
    private static final int[][] LEGAL_CANDIDATE_MOVES = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {-1, 2}, {1, -2}, {-1, -2}
    };
    public Knight( final Alliance pieceAlliance,final BoardPosition piecePosition )  {
        super(PieceType.KNIGHT,pieceAlliance, piecePosition);
    }


    @Override
    public Collection<Move> calculateLegalMove (final Board board) {

            List<Move> legalMoves = new ArrayList<>();

        for ( int[] currentCandidate : LEGAL_CANDIDATE_MOVES) {
            final int xCandidateDestinationPosition = piecePosition.x() + currentCandidate[0] ;
            final int yCandidateDestinationPosition = piecePosition.y() + currentCandidate[1] ;

            BoardPosition candidateDestinationPosition =  new BoardPosition(xCandidateDestinationPosition,yCandidateDestinationPosition);

            if(  BoardUtils.isPositionValid(candidateDestinationPosition)  ) {

            Tile candidateDestinationTile = board.getTile(candidateDestinationPosition);

                if( !candidateDestinationTile.isTileOccupied() )  {
                    legalMoves.add(new MajorMove(board, this , candidateDestinationPosition) );
                }

                else {
                    final Piece candidatePiece = candidateDestinationTile.getPiece();
                    final Alliance candidatePieceAlliance = candidatePiece.getPieceAlliance();
                    if( this.pieceAlliance != candidatePieceAlliance )  {
                        legalMoves.add(new AttackingMove(board,this,candidateDestinationPosition,candidatePiece));
                    }
                }
        }
    }
        return ImmutableList.copyOf(legalMoves);

}

    @Override
    public Piece movePiece(Move move) {

        return new Knight(move.getMovedPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

}
