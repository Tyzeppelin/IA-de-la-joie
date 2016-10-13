
public class IterativeDeepeningSolver {
    private final Board board;
    private int nbMoves;
    private int limit;
    private boolean solved;
    private java.util.Deque<Board> solution;

    public IterativeDeepeningSolver(Board initial) {
        this.board = initial;
        solution = new java.util.ArrayDeque<Board>();
        idaStar();
    }
         
    private void idaStar() { 
        final int manhattan = board.manhattan();
        final int blankPosition = board.blankPosition();
        limit = manhattan;
        final long tiles = board.getTiles();
        System.out.println("initial estimate: " + manhattan);
        while (true) {
            System.out.println("limit: " + limit);
            aStar(tiles, blankPosition, manhattan, '_', 0);
            if (solved) return;
            limit += 2;
        }
    }
    
    private void aStar(final long tiles, final int blankPosition, final int manhattan, final char previousDirection, final int depth) {        
        if (tiles == Board.goalState()) {
            solution.push(new Board(tiles, blankPosition));
            solved = true;
            nbMoves = depth;
            return;
        }
        if (previousDirection != 'R') {
            final int move = Board.moveLeft(tiles, blankPosition);
            if (move != -1) {
                final int manhattanForNewPosition = Board.nextManhattan(tiles, blankPosition, manhattan, move);
                if (manhattanForNewPosition + depth + 1 <= limit) {
                    aStar(Board.nextTiles(tiles, blankPosition, move), move, manhattanForNewPosition, 'L', depth + 1);
                }
                if (solved) {
                    solution.push(new Board(tiles, blankPosition));
                    return;
                }
            }
        }
        if (previousDirection != 'D') {
            final int move = Board.moveUp(tiles, blankPosition);
            if (move != -1) {
                final int manhattanForNewPosition = Board.nextManhattan(tiles, blankPosition, manhattan, move);
                if (manhattanForNewPosition + depth + 1 <= limit) {
                    aStar(Board.nextTiles(tiles, blankPosition, move), move, manhattanForNewPosition, 'U', depth + 1);
                }
                if (solved) {
                    solution.push(new Board(tiles, blankPosition));
                    return;
                }
            }
        }
        if (previousDirection != 'L') {
            final int move = Board.moveRight(tiles, blankPosition);
            if (move != -1) {
                final int manhattanForNewPosition = Board.nextManhattan(tiles, blankPosition, manhattan, move);
                if (manhattanForNewPosition + depth + 1 <= limit) {
                    aStar(Board.nextTiles(tiles, blankPosition, move), move, manhattanForNewPosition, 'R', depth + 1);
                }
                if (solved) {
                    solution.push(new Board(tiles, blankPosition));
                    return;
                }
            }
        }
        if (previousDirection != 'U') {
            final int move = Board.moveDown(tiles, blankPosition);
            if (move != -1) {
                final int manhattanForNewPosition = Board.nextManhattan(tiles, blankPosition, manhattan, move);
                if (manhattanForNewPosition + depth + 1 <= limit) {
                    aStar(Board.nextTiles(tiles, blankPosition, move), move, manhattanForNewPosition, 'D', depth + 1);
                }
                if (solved) {
                    solution.push(new Board(tiles, blankPosition));
                }
            }
        }
    }
    
    public int moves() {
        return nbMoves;
    }

    public Iterable<Board> solution() {        
        return solution;
    }

    public static void main(String[] args) {
        for (int k = 0; k < args.length; k++) {
            // create initial board from file
            In in = new In(args[k]);
            int N = in.readInt();
            int[][] blocks = new int[N][N];
            for (int i = 0; i < N; i++)
                for (int j = 0; j < N; j++)
                    blocks[i][j] = in.readInt();
            Board initial = new Board(blocks);

            // solve the puzzle
            IterativeDeepeningSolver solver =
                    new IterativeDeepeningSolver(initial);

            // print solution to standard output
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
