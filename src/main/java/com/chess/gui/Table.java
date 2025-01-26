package com.chess.gui;

import com.chess.engine.Alliance;
import com.chess.engine.Board.Board;
import com.chess.engine.Board.BoardPosition;
import com.chess.engine.Board.Tile;
import com.chess.engine.Pieces.Piece;
import com.chess.engine.Pieces.Piece.PieceType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Table {
    private static final Dimension OUTER_FRAM_DIMENSION = new Dimension(600,600) ;
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350) ;
    private static final Dimension TILE_PANEL_DIMENSION =  new Dimension(10,10);
    private final Color greenTileColor = Color.decode("#769656");
    private final Color whiteTileColor = Color.decode("#EEEED2");

    private final JFrame gameFrame;
    private BoardPanel boardPanel;

    public Table() {
        this.gameFrame = new JFrame("Chess");

        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAM_DIMENSION);
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
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

    private class BoardPanel extends JPanel {

        final List<TilePanel> boardTiles ;
        final Board board;
        BoardPanel(){

            super(new GridLayout(8,8));
             board = Board.createStandardBoard();
            this.boardTiles = new ArrayList<>();

                for (int i = 7; i >=0 ; --i) {
                    for (int j = 0 ; j < 8 ; ++j) {
                        final TilePanel tilePanel = new TilePanel(this , new BoardPosition(j,i),board);

                        this.boardTiles.add(tilePanel);
                        this.add(tilePanel);
                    }
                }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
            repaint();
        }

    }

    private class TilePanel extends JPanel {

        private final BoardPosition tileId;

        TilePanel(final BoardPanel boardPanel,
                  final BoardPosition tileId,
                  final Board board
                  ) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor(tileId );
            assignTilePieceIcon(board);
            validate();

        }

        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            if (board.getTile(this.tileId).isTileOccupied()) {
                Piece piece = board.getTile(this.tileId).getPiece();
                PieceType pieceType = piece.getPieceType();
                Alliance pieceAlliance = piece.getPieceAlliance();
                String piecePath ;
                if(pieceAlliance.isWhite()) {
                   piecePath = switch (pieceType) {
                        case PAWN ->  "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\BP.gif";
                        case KING ->   "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\BK.gif" ;
                        case ROOK ->   "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\BR.gif";
                        case QUEEN ->  "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\BQ.gif";
                        case BISHOP -> "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\BB.gif";
                        case KNIGHT -> "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\BN.gif" ;

                    };
                }
                else {
                    piecePath = switch (pieceType) {
                        case PAWN ->  "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\WP.gif";
                        case KING ->  "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\WK.gif" ;
                        case ROOK ->  "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\WR.gif";
                        case QUEEN -> "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\WQ.gif";
                        case BISHOP ->"C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\WB.gif";
                        case KNIGHT -> "C:\\Users\\amine\\Documents\\Chess Engine\\Chess Engine\\art\\fancy\\WN.gif" ;

                    };
                }


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



        private void assignTileColor(BoardPosition tileId) {
            boolean isDarkTile = (tileId.x()+ tileId.y()) % 2 == 0;
            setBackground(isDarkTile ? greenTileColor : whiteTileColor);
        }

    }
}
