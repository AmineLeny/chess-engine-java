
import com.chess.engine.Board.*;
import com.chess.engine.Pieces.Piece;
import com.chess.engine.Player.AI.MiniMax;
import com.chess.engine.Player.AI.MoveStrategy;
import com.chess.engine.Player.MoveTransition;
import com.google.common.collect.Iterables;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestBoard {

    @Test
    public void initialBoard() {

        final Board board = Board.createStandardBoard();
        assertEquals(board.getCurrentPlayer().getLegalMovesPlayer().size(), 20);
        assertEquals(board.getCurrentPlayer().getOpponent().getLegalMovesPlayer().size(), 20);
        assertFalse(board.getCurrentPlayer().getIsInCheck());
        assertFalse(board.getCurrentPlayer().isInCheckMate());
        assertFalse(board.getCurrentPlayer().isCastled());
        assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());
        assertEquals(board.getCurrentPlayer().getOpponent(), board.getBlackPlayer());
        assertFalse(board.getCurrentPlayer().getOpponent().getIsInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        assertFalse(board.getCurrentPlayer().getOpponent().isCastled());
        assertTrue(board.getWhitePlayer().toString().equals("White"));
        assertTrue(board.getBlackPlayer().toString().equals("Black"));

        final Iterable<Piece> allPieces = Iterables.concat(board.getBlackPieces(), board.getWhitePieces());
        final Iterable<Move> allMoves = Iterables.concat(board.getWhitePlayer().getLegalMovesPlayer(), board.getBlackPlayer().getLegalMovesPlayer());
        for (final Move move : allMoves) {
            assertFalse(move.isAttack());
            assertFalse(move.isCastlingMove());
        }

        assertEquals(Iterables.size(allMoves), 40);
        assertEquals(Iterables.size(allPieces), 32);
    }

    @Test
    public void testFoolsMate() {
        final Board board = Board.createStandardBoard();

        // 1. White moves pawn to f3
        final MoveTransition t1 = board.getCurrentPlayer().makeMove(
                Move.MoveFactory.createMove(
                        board,
                        new BoardPosition(5, 1), // f2 (White pawn)
                        new BoardPosition(5, 2)) // f3
        );
        assertTrue(t1.getMoveStatus().isDone());

        // 2. Black moves pawn to e5
        final MoveTransition t2 = t1.getTransitionBoard().getCurrentPlayer().makeMove(
                Move.MoveFactory.createMove(
                        t1.getTransitionBoard(),
                        new BoardPosition(4, 6), // e7 (Black pawn)
                        new BoardPosition(4, 4)) // e5
        );
        assertTrue(t2.getMoveStatus().isDone());

        // 3. White moves pawn to g4
        final MoveTransition t3 = t2.getTransitionBoard().getCurrentPlayer().makeMove(
                Move.MoveFactory.createMove(
                        t2.getTransitionBoard(),
                        new BoardPosition(6, 1), // g2 (White pawn)
                        new BoardPosition(6, 3)) // g4
        );
        assertTrue(t3.getMoveStatus().isDone());

        // 4. Black plays Qh4# (checkmate)
        final MoveStrategy strategy = new MiniMax(4);
        final Move aiMove = strategy.execute(t3.getTransitionBoard());

        final Move bestMove = Move.MoveFactory.createMove(
                t3.getTransitionBoard(),
                new BoardPosition(3, 7), // d8 (Black Queen)
                new BoardPosition(7, 3)  // h4 (Checkmate)
        );

        // 5. Assert that AI selects the checkmate move
        assertEquals(bestMove, aiMove);
    }

}