package com.chess.engine;

import com.chess.engine.Board.BoardPosition;
import com.chess.engine.Player.BlackPlayer;
import com.chess.engine.Player.Player;
import com.chess.engine.Player.WhitePlayer;

public enum Alliance {

    WHITE("W") {


        @Override
        public int getDirection(){
            return 1;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public boolean isPawnPromotionSquare(BoardPosition position) {
            return position.y() == 7;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return whitePlayer;
        }
        @Override
        public String toString(){
            return "W";
        }
    },
    BLACK("B") {
        @Override
        public int getDirection(){
            return -1;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public boolean isPawnPromotionSquare(BoardPosition position) {
            return position.y() == 0;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return blackPlayer;
        }

    };

    private final String colorChar;
    Alliance(String colorChar){
        this.colorChar = colorChar;
    }
    @Override
    public String toString(){
        return colorChar;
    }







    public abstract int getDirection();

    public abstract boolean isWhite();
    public abstract boolean isBlack();
    public abstract boolean isPawnPromotionSquare(BoardPosition position);
    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
