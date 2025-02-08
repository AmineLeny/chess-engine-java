package com.chess.gui;

import com.chess.engine.Alliance;
import com.chess.engine.Board.Board;
import com.chess.engine.Board.BoardPosition;
import com.chess.engine.Board.Move;
import com.chess.engine.Board.Move.MoveFactory;
import com.chess.engine.Board.Tile;
import com.chess.engine.Pieces.Piece;
import com.chess.engine.Pieces.Piece.PieceType;
import com.chess.engine.Player.AI.MiniMax;
import com.chess.engine.Player.AI.MoveStrategy;
import com.chess.engine.Player.MoveTransition;
import com.google.common.collect.Lists;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table extends Observable {
    private static final Dimension OUTER_FRAM_DIMENSION = new Dimension(600,600) ;
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350) ;
    private static final Dimension TILE_PANEL_DIMENSION =  new Dimension(10,10);
    private final Color greenTileColor = Color.decode("#769656");
    private final Color whiteTileColor = Color.decode("#EEEED2");


    private GameSetup gameSetup;



    private final JFrame gameFrame;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private BoardPanel boardPanel;
    private final MoveLog moveLog;
    private Board board;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private boolean highLightLegalMoves;

    private static final Table INSTANCE = new Table();
    private Move computerMove;

    public static Table getInstance() {
        return INSTANCE;
    }


    private Table()   {
        this.gameFrame = new JFrame("Chess");

        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAM_DIMENSION);
        this.board = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.boardDirection = BoardDirection.NORMAL;
        this.highLightLegalMoves = false;
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
        this.gameFrame.setVisible(true);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    enum PlayerType {
        HUMAN,
        COMPUTER
    }
    public void show() {
        Table.getInstance().getMoveLog().clear();
        Table.getInstance().getGameHistoryPanel().redo(board, Table.getInstance().getMoveLog());
        Table.getInstance().getTakenPiecesPanel().redo(Table.getInstance().getMoveLog());
        Table.getInstance().getBoardPanel().drawBoard(Table.getInstance().getGameBoard());
    }

    private GameSetup getGameSetup() {
        return this.gameSetup;
    }
    private Board getGameBoard() {
        return this.board;
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createPreferencesMenu());
        menuBar.add(createOptionsMenu());
        return menuBar;
    }
    private JMenu createOptionsMenu() {
        final JMenu optionMenu = new JMenu("Options");
        final JMenuItem setupGameMenuItem = new JMenuItem("Setup Game");
        setupGameMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Table.getInstance().getGameSetup().promptUser()
;
            Table.getInstance().setupUpdate(Table.getInstance().getGameSetup());

            }
        });

        optionMenu.add(setupGameMenuItem);
        return optionMenu;
    }

    private void setupUpdate(final GameSetup gameSetup) {
        setChanged();
        notifyObservers(gameSetup);
    }
    private static class TableGameAIWatcher implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            if( Table.getInstance().getGameSetup().isAIPlayer(Table.getInstance().getGameBoard().getCurrentPlayer()) &&
                    !Table.getInstance().getGameBoard().getCurrentPlayer().isInCheckMate() &&
                    !Table.getInstance().getGameBoard().getCurrentPlayer().isInStalemate()
                    ) {
                // create an AI thread
                // execute  AI  work
                final AIThinkTank thinkTank = new AIThinkTank();
                thinkTank.execute();
            }
            if(Table.getInstance().getGameBoard().getCurrentPlayer().isInCheckMate()){
                JOptionPane.showMessageDialog(Table.getInstance().getBoardPanel(), "You are in Check Mate");

            }
            if(Table.getInstance().getGameBoard().getCurrentPlayer().isInStalemate()){
                JOptionPane.showMessageDialog(Table.getInstance().getBoardPanel(), "You are in Stalemate");

            }
        }
    }
    private static class AIThinkTank extends SwingWorker<Move,String> {
        private AIThinkTank() {

        }

        @Override
        protected Move doInBackground() throws Exception {
            final MoveStrategy miniMax = new MiniMax(4);
            final Move bestMove = miniMax.execute(Table.getInstance().getGameBoard());
            return bestMove;
        }
        @Override
        protected void done() {
            try {
                final Move bestMove = get();
                Table.getInstance().updateComputerMove(bestMove);
                Table.getInstance().updateGameBoard(Table.getInstance().getGameBoard().getCurrentPlayer().makeMove(bestMove).getTransitionBoard());
                Table.getInstance().getMoveLog().addMove(bestMove);
                Table.getInstance().getGameHistoryPanel().redo(Table.getInstance().getGameBoard(),Table.getInstance().getMoveLog());
                Table.getInstance().getTakenPiecesPanel().redo(Table.getInstance().getMoveLog());
                Table.getInstance().getBoardPanel().drawBoard(Table.getInstance().getGameBoard());
                Table.getInstance().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveMadeUpdate(PlayerType playerType) {
    setChanged();
    notifyObservers(playerType);
    }

    private TakenPiecesPanel getTakenPiecesPanel() {
        return this.takenPiecesPanel;
    }

    private GameHistoryPanel getGameHistoryPanel() {
        return this.gameHistoryPanel;
    }

    private void updateComputerMove(final Move move) {
        this.computerMove = move;
    }

    private MoveLog getMoveLog() {
        return this.moveLog;
    }

    private void updateGameBoard(Board transitionBoard) {
        this.board = transitionBoard;
    }

    private BoardPanel getBoardPanel() {
        return this.boardPanel;
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
                                moveLog.addMove(move);
                            } else {
                                System.out.println("Invalid move attempted!");
                            }

                            // Always reset selection after move attempt
                            destinationTile = null;
                            sourceTile = null;
                            humanMovedPiece = null;
                        }

                        SwingUtilities.invokeLater(() -> {
                            gameHistoryPanel.redo(board, moveLog);
                            takenPiecesPanel.redo(moveLog);
                            if(gameSetup.isAIPlayer(board.getCurrentPlayer())){
                                Table.getInstance().moveMadeUpdate(PlayerType.HUMAN);
                            }

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
            if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance().equals(board.getCurrentPlayer().getAlliance())) {
                Collection<Move> legalMoves = new ArrayList<>(humanMovedPiece.calculateLegalMove(board));
                legalMoves.addAll(board.getCurrentPlayer().calculateKingCastles(
                        board.getCurrentPlayer().getLegalMovesPlayer(),
                        board.getOpponentPlayer().getLegalMovesPlayer()
                ));
                return legalMoves;
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
