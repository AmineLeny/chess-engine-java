package com.chess.gui;

import com.chess.engine.Alliance;
import com.chess.engine.Board.Board;
import com.chess.engine.Board.BoardPosition;
import com.chess.engine.Board.Move;
import com.chess.engine.Board.Move.MoveFactory;
import com.chess.engine.Board.Tile;
import com.chess.engine.Pieces.Piece;
import com.chess.engine.Pieces.Piece.PieceType;
import com.chess.engine.Player.MoveTransition;
import com.google.common.collect.Lists;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    private static final Dimension OUTER_FRAM_DIMENSION = new Dimension(600,600) ;
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350) ;
    private static final Dimension TILE_PANEL_DIMENSION =  new Dimension(10,10);
    private final Color greenTileColor = Color.decode("#769656");
    private final Color whiteTileColor = Color.decode("#EEEED2");



    private final JFrame gameFrame;
    private BoardPanel boardPanel;
    private Board board;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private boolean highLightLegalMoves;


    public Table() {
        this.gameFrame = new JFrame("Chess");

        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAM_DIMENSION);
        this.board = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.boardDirection = BoardDirection.NORMAL;
        this.highLightLegalMoves = false;
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createPreferencesMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                System.out.println("Opening PGN File...");
            }
        });
        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        fileMenu.add(openPGN);
        fileMenu.add(exitMenuItem);
        return fileMenu;

    }
    private JMenu createPreferencesMenu(){
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(board);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        preferencesMenu.addSeparator();
        final JCheckBoxMenuItem legalMoveHighLighterCheckBox = new JCheckBoxMenuItem("Highlight Legal Moves",false);
        legalMoveHighLighterCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                highLightLegalMoves = legalMoveHighLighterCheckBox.isSelected();
            }
        });
        preferencesMenu.add(legalMoveHighLighterCheckBox);
        return preferencesMenu;




    }

    private class BoardPanel extends JPanel {

        final List<TilePanel> boardTiles ;

        BoardPanel(){

            super(new GridLayout(8,8));

            this.boardTiles = new ArrayList<>();

                for (int i = 7; i >=0 ; --i) {
                    for (int j = 0 ; j < 8 ; ++j) {
                        final TilePanel tilePanel = new TilePanel(this , new BoardPosition(j,i));

                        this.boardTiles.add(tilePanel);
                        this.add(tilePanel);
                    }
                }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
            repaint();
        }

        public void drawBoard(final Board board) {
            removeAll();
            for(final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }

    }
    public static class MoveLog {
        private final List<Move> moves;
        MoveLog(){
            this.moves = new ArrayList<>();
        }
        public List<Move> getMoves(){
            return this.moves;
        }
        public void addMove(final Move move){
            this.moves.add(move);
        }
        public int size(){
            return this.moves.size();
        }
        public void clear(){
            this.moves.clear();
        }
        public boolean removeMove(final Move move){
            return this.moves.remove(move);

        }
        public Move removeMove(int index){
            return this.moves.remove(index);

        }




    }

    private class TilePanel extends JPanel {

        private final BoardPosition tileId;

        TilePanel(final BoardPanel boardPanel,
                  final BoardPosition tileId
                  ) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor(tileId);
            assignTilePieceIcon(board);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (isRightMouseButton(e)) {
                        // Reset selection
                        sourceTile = null;
                        humanMovedPiece = null;
                        destinationTile = null;
                    }
                    else if (isLeftMouseButton(e)) {
                        // First click - selecting a piece
                        if (sourceTile == null) {
                            sourceTile = board.getTile(tileId);
                            // Debug print
                            System.out.println("Selected tile at: " + tileId);

                            if (sourceTile.isTileOccupied()) {
                                humanMovedPiece = sourceTile.getPiece();
                                // Debug prints
                                System.out.println("Selected piece: " + humanMovedPiece.getPieceType() +
                                        " of color: " + humanMovedPiece.getPieceAlliance());
                                System.out.println("Current player's turn: " +
                                        board.getCurrentPlayer().getAlliance());

                                // Check if the selected piece belongs to the current player
                                if (!humanMovedPiece.getPieceAlliance().equals(
                                        board.getCurrentPlayer().getAlliance())) {
                                    System.out.println("Wrong player's piece selected!");
                                    sourceTile = null;
                                    humanMovedPiece = null;
                                    return;
                                }
                            }

                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        }
                        // Second click - making a move
                        else {
                            destinationTile = board.getTile(tileId);
                            // Debug print
                            System.out.println("Attempting move to: " + tileId);

                            final Move move = MoveFactory.createMove(
                                    board,
                                    sourceTile.getPiece().getPiecePosition(),
                                    tileId
                            );

                            final MoveTransition transition = board.getCurrentPlayer().makeMove(move);

                            // Debug print
                            System.out.println("Move status: " + transition.getMoveStatus());

                            if (transition.getMoveStatus().isDone()) {
                                board = transition.getTransitionBoard();
                                System.out.println("Move completed. New turn: " +
                                        board.getCurrentPlayer().getAlliance());
                            } else {
                                System.out.println("Invalid move attempted!");
                            }

                            // Always reset selection after move attempt
                            destinationTile = null;
                            sourceTile = null;
                            humanMovedPiece = null;
                        }

                        SwingUtilities.invokeLater(() -> {
                            boardPanel.drawBoard(board);
                        });
                    }


                    }



                @Override
                public void mousePressed(final MouseEvent e) {

                }

                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                @Override
                public void mouseExited(final MouseEvent e) {

                }
            });


            validate();





        }
        public void drawTile(final Board board){
            assignTileColor(tileId);
            assignTilePieceIcon(board);
            highlightLegals(board);
            validate();
            repaint();
        }

        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            if (board.getTile(this.tileId).isTileOccupied()) {
                Piece piece = board.getTile(this.tileId).getPiece();
                PieceType pieceType = piece.getPieceType();
                Alliance pieceAlliance = piece.getPieceAlliance();
                // I've the path Dynamic
                String piecePath  = "art/fancy/" + pieceAlliance.toString() +pieceType.toString() + ".gif" ;

                // I hardcoded it first
//                if(pieceAlliance.isWhite()) {
//                   piecePath = switch (pieceType) {
//                        case PAWN ->  "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\BP.gif";
//                        case KING ->   "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\BK.gif" ;
//                        case ROOK ->   "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\BR.gif";
//                        case QUEEN ->  "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\BQ.gif";
//                        case BISHOP -> "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\BB.gif";
//                        case KNIGHT -> "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\BN.gif" ;
//
//                    };
//                }
//                else {
//                    piecePath = switch (pieceType) {
//                        case PAWN ->  "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\WP.gif";
//                        case KING ->  "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\WK.gif" ;
//                        case ROOK ->  "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\WR.gif";
//                        case QUEEN -> "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\WQ.gif";
//                        case BISHOP ->"C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\WB.gif";
//                        case KNIGHT -> "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\WN.gif" ;
//
//                    };
//                }


                try{
                    final BufferedImage image = ImageIO.read(new File(
                                    piecePath
                            )
                    );
                    this.add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        private void highlightLegals(final Board board) {

            if(highLightLegalMoves) {

                for( final Move move : pieceLegalMoves(board)) {
                    if(move.getDestinationCoordinate().equals(this.tileId)) {
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board board) {
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance().equals(board.getCurrentPlayer().getAlliance())) {
                return humanMovedPiece.calculateLegalMove(board);
            }
            return Collections.emptyList();
        }





        private void assignTileColor(BoardPosition tileId) {
            boolean isDarkTile = (tileId.x()+ tileId.y()) % 2 == 0;
            setBackground(isDarkTile ? greenTileColor : whiteTileColor);
        }

    }
    public enum BoardDirection {
        NORMAL {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();
    }
}
