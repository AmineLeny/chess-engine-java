package com.chess.engine.Player;

import com.chess.engine.Board.Board;
import com.chess.engine.Board.Move;

import java.util.concurrent.Future;

public class MoveTransition {


    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(Board transitionBoard, Move move, MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public Board getTransitionBoard() {
        return this.transitionBoard;
    }
    public MoveStatus getMoveStatus() {
        return moveStatus;
    }
}
