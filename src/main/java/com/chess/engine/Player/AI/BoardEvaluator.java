package com.chess.engine.Player.AI;

import com.chess.engine.Board.Board;

public interface BoardEvaluator {

    int evaluate(Board board, int depth);

}