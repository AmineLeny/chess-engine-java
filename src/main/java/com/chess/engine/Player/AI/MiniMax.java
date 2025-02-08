package com.chess.engine.Player.AI;

import com.chess.engine.Board.Board;
import com.chess.engine.Board.Move;
import com.chess.engine.Player.MoveTransition;

public class MiniMax implements MoveStrategy {
    private final BoardEvaluator boardEvaluator;
    private int depth ;

    public MiniMax(int depth){
        this.boardEvaluator = new StandardBoardEvaluator();
        this.depth = depth;

    }
    @Override
    public String toString(){
        return "MiniMax";
    }

    @Override
    public Move execute(Board board) {

        final long startTime = System.currentTimeMillis();
        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.getCurrentPlayer() + " THINKING WITH DEPTH = "+ depth);
        int numMoves = board.getCurrentPlayer().getLegalMovesPlayer().size();
        for( final Move move : board.getCurrentPlayer().getLegalMovesPlayer()){
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                currentValue = board.getCurrentPlayer().getAlliance().isWhite()?
                        min(moveTransition.getTransitionBoard(), depth-1):
                        max(moveTransition.getTransitionBoard(), depth-1);
                if( board.getCurrentPlayer().getAlliance().isWhite() && currentValue > highestSeenValue ){
                    highestSeenValue = currentValue;
                    bestMove = move;

                }else if(board.getCurrentPlayer().getAlliance().isBlack() && currentValue < lowestSeenValue ){
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        final long endTime = System.currentTimeMillis() -startTime;
        return bestMove;
    }
    private boolean isEndGameScenario(Board board) {
        return board.getCurrentPlayer().isInCheckMate() || board.getCurrentPlayer().isInStalemate();

    }

    public int min(final Board board , final int depth) {
        if( depth == 0 || isEndGameScenario(board)/* || game over */ ) return this.boardEvaluator.evaluate(board,depth);
        int lowestSeenValue = Integer.MAX_VALUE;
        for( final Move move : board.getCurrentPlayer().getLegalMovesPlayer()){
            final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(moveTransition.getTransitionBoard(),depth-1 );
                if(currentValue <= lowestSeenValue){
                    lowestSeenValue = currentValue;
                }

            }
        }
        return lowestSeenValue;

    }



    public int max(final Board board , final int depth) {

       if ( depth == 0 || isEndGameScenario(board)) return this.boardEvaluator.evaluate(board,depth);
       int highestSeenValue = Integer.MIN_VALUE;
       for( final Move move : board.getCurrentPlayer().getLegalMovesPlayer()){
           final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
           if(moveTransition.getMoveStatus().isDone()) {
               final int currentValue = min(moveTransition.getTransitionBoard(),depth-1 );
               if(currentValue >= highestSeenValue){
                   highestSeenValue = currentValue;
               }
           }
       }
       return highestSeenValue;

    }


}
