package com.chess.engine.Board;

import java.util.*;

public class BoardUtils {

    public static Map<BoardPosition,String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public static Map<String, Integer> POSITION_TO_COORDINATE  =initializePositionToCoordinateMap();

    BoardUtils(){
        throw new RuntimeException("You can't instantiate this class");
    }

    public static boolean isPositionValid(final BoardPosition piecePosition ){
        return piecePosition.x() >=0 && piecePosition.x()<8 && piecePosition.y() >=0 && piecePosition.y()<8;
    }


    public static Integer getCoordinateAtPosition(final String position){
        return POSITION_TO_COORDINATE.get(position);
    }

    private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>(64);

        // Iterate through all board positions in the algebraic notation map
        for (Map.Entry<BoardPosition, String> entry : ALGEBRAIC_NOTATION.entrySet()) {
            BoardPosition pos = entry.getKey();
            String algebraic = entry.getValue();

            // Convert BoardPosition (x,y) to linear coordinate (0-63)
            int coordinate = pos.y() * 8 + pos.x();
            positionToCoordinate.put(algebraic, coordinate);
        }

        return Collections.unmodifiableMap(positionToCoordinate);
    }

    private static Map<BoardPosition, String> initializeAlgebraicNotation() {
        Map<BoardPosition, String> notationMap = new LinkedHashMap<>(64);

        // Create algebraic notation mapping based on BoardPosition coordinates
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                char file = (char) ('a' + x);
                int rank = 8 - y;
                notationMap.put(new BoardPosition(x, y), String.format("%c%d", file, rank));
            }
        }

        return Collections.unmodifiableMap(notationMap);
    }


    public static String getPositionAtCoordinate(BoardPosition destinationCoordinate) {
        return ALGEBRAIC_NOTATION.get(destinationCoordinate);
    }
}

