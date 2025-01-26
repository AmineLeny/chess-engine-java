package com.chess.gui;

import com.chess.engine.Board.Board;
import com.chess.engine.Board.BoardPosition;
import com.chess.engine.Board.Tile;

import java.util.ArrayList;
import java.util.List;
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

        BoardPanel(){
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();

                for (int i = 7; i >=0 ; --i) {
                    for (int j = 0 ; j < 8 ; ++j) {
                        final TilePanel tilePanel = new TilePanel(this , new BoardPosition(i,j));
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
                  final BoardPosition tileId
                  ) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor(tileId );
            validate();

        }

        private void assignTileColor(BoardPosition tileId) {
            boolean isDarkTile = (tileId.x()+ tileId.y()) % 2 == 0;
            setBackground(isDarkTile ? greenTileColor : whiteTileColor);
        }

    }
}
