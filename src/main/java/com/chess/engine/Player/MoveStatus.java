package com.chess.engine.Player;

import com.chess.engine.Board.Move;

public enum MoveStatus {
    DONE {
        @Override
        boolean isDone() {
            return true;
        }
    },
    ILLEGAL_MOVE{
        @Override
        boolean isDone() {
            return false;
        }
    },
    LEAVES_PLAYER_IN_CHECK {
        @Override
        boolean isDone() {
            return false;
        }
    }
    ;
    abstract boolean isDone();






}
