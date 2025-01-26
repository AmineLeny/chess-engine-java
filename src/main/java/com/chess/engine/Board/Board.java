package com.chess.engine.Board;

import com.chess.engine.Alliance;
import com.chess.engine.Pieces.*;
import com.chess.engine.Player.BlackPlayer;
import com.chess.engine.Player.Player;
import com.chess.engine.Player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

public class Board {
    private final Map<BoardPosition, Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;

    final Collection<Move> whiteStandardMoves;
    final Collection<Move> blackStandardMoves;
    private final Player currentPlayer;

    private Board( final BoardBuilder builder) {
        this.gameBoard = createGameBoard(builder);
        whitePieces = calculateActivePieces(this.gameBoard , Alliance.WHITE);
        blackPieces = calculateActivePieces(this.gameBoard , Alliance.BLACK);
        whiteStandardMoves = calculateLegalMoves(this.whitePieces);
        blackStandardMoves = calculateLegalMoves(this.blackPieces);
        this.whitePlayer = new WhitePlayer(this,whiteStandardMoves,blackStandardMoves);
        this.blackPlayer = new BlackPlayer(this,blackStandardMoves,whiteStandardMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);
    }



    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }


    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                final String tileText = this.gameBoard.get(new BoardPosition(x, y)).toString();
                builder.append(String.format("%3s", tileText ));
            }
            builder.append("\n");
        }
        return builder.toString();
    }


    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }
    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }



    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final Piece piece : pieces) {
            legalMoves.addAll(piece.calculateLegalMove(this));
        }
    return ImmutableList.copyOf(legalMoves);

    }

    private static Collection<Piece> calculateActivePieces(final Map<BoardPosition, Tile> gameBoard, final Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();
        for (Tile tile : gameBoard.values()) {
            if (tile.isTileOccupied()) {
                final Piece piece = tile.getPiece();
                if (piece.getPieceAlliance() == alliance) {
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }



    private Map<BoardPosition, Tile> createGameBoard(BoardBuilder builder) {
        Map<BoardPosition, Tile> gameBoard = new HashMap<>();
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                BoardPosition position = new BoardPosition(i, j);
                gameBoard.put(position, Tile.createTile(i, j, builder.boardConfig.get(position)));
            }
        }
        return Collections.unmodifiableMap(gameBoard);
    }
    //create the standard chess board with its pieces
    public static Board createStandardBoard() {
        final BoardBuilder builder = new BoardBuilder();
        // Black Layout
        builder.setPiece(new Rook(Alliance.BLACK, new BoardPosition(0, 7),false));
        builder.setPiece(new Knight(Alliance.BLACK, new BoardPosition(1, 7),false));
        builder.setPiece(new Bishop(Alliance.BLACK, new BoardPosition(2, 7),false));
        builder.setPiece(new Queen(Alliance.BLACK, new BoardPosition(3, 7),false));
        builder.setPiece(new King(Alliance.BLACK, new BoardPosition(4, 7),false));
        builder.setPiece(new Bishop(Alliance.BLACK, new BoardPosition(5, 7),false));
        builder.setPiece(new Knight(Alliance.BLACK, new BoardPosition(6, 7),false));
        builder.setPiece(new Rook(Alliance.BLACK, new BoardPosition(7, 7),false));

        builder.setPiece(new Pawn(Alliance.BLACK, new BoardPosition(0, 6),false));
        builder.setPiece(new Pawn(Alliance.BLACK, new BoardPosition(1, 6),false));
        builder.setPiece(new Pawn(Alliance.BLACK, new BoardPosition(2, 6),false));
        builder.setPiece(new Pawn(Alliance.BLACK, new BoardPosition(3, 6),false));
        builder.setPiece(new Pawn(Alliance.BLACK, new BoardPosition(4, 6),false));
        builder.setPiece(new Pawn(Alliance.BLACK, new BoardPosition(5, 6),false));
        builder.setPiece(new Pawn(Alliance.BLACK, new BoardPosition(6, 6),false));
        builder.setPiece(new Pawn(Alliance.BLACK, new BoardPosition(7, 6),false));

// White Layout
        builder.setPiece(new Rook(Alliance.WHITE, new BoardPosition(0, 0),false));
        builder.setPiece(new Knight(Alliance.WHITE, new BoardPosition(1, 0),false));
        builder.setPiece(new Bishop(Alliance.WHITE, new BoardPosition(2, 0),false));
        builder.setPiece(new Queen(Alliance.WHITE, new BoardPosition(3, 0),false));
        builder.setPiece(new King(Alliance.WHITE, new BoardPosition(4, 0),false));
        builder.setPiece(new Bishop(Alliance.WHITE, new BoardPosition(5, 0),false));
        builder.setPiece(new Knight(Alliance.WHITE, new BoardPosition(6, 0),false));
        builder.setPiece(new Rook(Alliance.WHITE, new BoardPosition(7, 0),false));

        builder.setPiece(new Pawn(Alliance.WHITE, new BoardPosition(0, 1),false));
        builder.setPiece(new Pawn(Alliance.WHITE, new BoardPosition(1, 1),false));
        builder.setPiece(new Pawn(Alliance.WHITE, new BoardPosition(2, 1),false));
        builder.setPiece(new Pawn(Alliance.WHITE, new BoardPosition(3, 1),false));
        builder.setPiece(new Pawn(Alliance.WHITE, new BoardPosition(4, 1),false));
        builder.setPiece(new Pawn(Alliance.WHITE, new BoardPosition(5, 1),false));
        builder.setPiece(new Pawn(Alliance.WHITE, new BoardPosition(6, 1),false));
        builder.setPiece(new Pawn(Alliance.WHITE, new BoardPosition(7, 1),false));

        //set white to move first
        builder.setMoveMaker(Alliance.WHITE);

        //build the board
        return builder.build();


    }


    public Tile getTile(final BoardPosition tilePosition) {
        return gameBoard.get(tilePosition);
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(whiteStandardMoves,blackStandardMoves));
    }


    public static class BoardBuilder {
        Map<BoardPosition, Piece> boardConfig;
        Alliance nextMoveMaker;
        Pawn EnPassantPawn;
        public BoardBuilder() {
            boardConfig = new HashMap<>();
        }
        public BoardBuilder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public BoardBuilder setMoveMaker(final Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build() {
            return new Board(this);
        }


        public void setEnPassantPawn(Pawn movedPawn) {
        }
    }
}
