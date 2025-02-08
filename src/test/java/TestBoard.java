
import com.chess.engine.Alliance;
import com.chess.engine.Board.*;
import com.chess.engine.Pieces.Piece;
import com.google.common.collect.Iterables;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestBoard {

    @Test
    public void initialBoard() {

        final Board board = Board.createStandardBoard();
        assertEquals(board.getCurrentPlayer().getLegalMoves().size(), 20);
        assertEquals(board.getCurrentPlayer().getOpponent().getLegalMoves().size(), 20);
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
        final Iterable<Move> allMoves = Iterables.concat(board.getWhitePlayer().getLegalMoves(), board.getBlackPlayer().getLegalMoves());
        for (final Move move : allMoves) {
            assertFalse(move.isAttack());
            assertFalse(move.isCastlingMove());
        }

        assertEquals(Iterables.size(allMoves), 40);
        assertEquals(Iterables.size(allPieces), 32);
    }
}