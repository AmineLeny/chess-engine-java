package com.chess.engine.Board;

public class BoardUtils {

    BoardUtils(){
        throw new RuntimeException("You can't instantiate this class");
    }

    public static boolean isPositionValid(final BoardPosition piecePosition ){
        return piecePosition.x() >=0 && piecePosition.x()<8 && piecePosition.y() >=0 && piecePosition.y()<8;
    }


}

