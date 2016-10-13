public class Board {
    private static final int N = 4;
    private long tiles;
    private int blank;
    private int manhattanValue;
  //  private int patternValue;
    private static final long GOAL = 0xfedcba987654321L;
    private static final byte[] manhattanDeltas = new byte[N * N * N * N * N * N];
     
    static {
        for (int blank = 0; blank < N*N; blank++) {
            for (int move = 0; move < N*N; move++) {
                for (int tile = 1; tile < N*N; tile++) {
                    byte delta = 0;
                    int goalI = (int) (tile - 1) >>> 2;
                    int goalJ = (int) (tile - 1) & 3;
                    int tmp1 = goalJ - (move & 3);
                    int tmp2 = goalI - (move >>> 2);
                    delta -= (tmp1 >= 0 ? tmp1 : -tmp1) + (tmp2 >= 0 ? tmp2 : -tmp2);
                    tmp1 = goalJ - (blank & 3);
                    tmp2 = goalI - (blank >>> 2);
                    delta += (tmp1 >= 0 ? tmp1 : -tmp1) + (tmp2 >= 0 ? tmp2 : -tmp2);
                    manhattanDeltas[(blank << 8) + (move << 4) + tile] = delta;
                }       
            }
        }
        
    }
    
    public Board(int[][] blocks) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int tile = blocks[i][j];
                if (tile == 0) {
                    blank = i * N + j;
                }
                set(tile, i * N + j);
            }
        }
        manhattanValue = computeManhattan();
        //patternValue = PatternDatabaseHeuristic.heuristic(tiles);
    }
    
    public Board(long tiles, int blank) {
        this.tiles = tiles;
        this.blank = blank;
        manhattanValue = computeManhattan();
        //patternValue = PatternDatabaseHeuristic.heuristic(tiles);
    }
    
    private void set(long tile, int i) {
        tiles = (tiles & ~(0xFL << (i << 2))) | (tile << (i << 2));
    }    
    
    private int get(int i, int j) {
        return (int) ((tiles >>> ((i * N + j) << 2)) & 0xFL);
    }
        
    public int manhattan() {
        return manhattanValue;
    }
        
    private int computeManhattan() {
        int res = 0;
        for (int i = 0; i < N*N; i++) {
            int tile = (int) ((tiles >>> (i << 2)) & 0xFL);
            if (tile != 0) {
                int goalI = (tile - 1) / N;
                int goalJ = (tile - 1) % N;
                res += Math.abs(goalJ - i % N) + Math.abs(goalI - i / N);
            }
        }
        return res;
    }
    
    public long getTiles() {
        return tiles;
    }
        
    public boolean isGoal() {
        return tiles == GOAL;
    }
    
    public int blankPosition() {
        return blank;
    }
    
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (that == null)
            return false;
        if (this.getClass() != that.getClass())
            return false;
        Board other = (Board) that;
        return other.tiles == tiles;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", get(i, j)));
            }
            s.append("\n");
        }
        return s.toString();
    }
 
    public static long goalState() {
        return GOAL;
    }

    public static int nextManhattan(final long tiles, final int blank, final int manhattan, final int move) {
        /*int res = manhattan;
        long tile = ((tiles >>> (move << 2)) & 0xFL);
        int goalI = (int) (tile - 1) >>> 2;
        int goalJ = (int) (tile - 1) & 3;
        int tmp1 = goalJ - (move & 3);
        int tmp2 = goalI - (move >>> 2);
        res -= (tmp1 >= 0 ? tmp1 : -tmp1) + (tmp2 >= 0 ? tmp2 : -tmp2);
        tmp1 = goalJ - (blank & 3);
        tmp2 = goalI - (blank >>> 2);
        res += (tmp1 >= 0 ? tmp1 : -tmp1) + (tmp2 >= 0 ? tmp2 : -tmp2);
        return res;*/
        //int tile = (int)((tiles >>> (move << 2)) & 0xFL);
        return manhattan + manhattanDeltas[(blank << 8) + (move << 4) + (int)((tiles >>> (move << 2)) & 0xFL)];
    }

    public static long nextTiles(final long tiles, final int blank, final int move) {
        long res = tiles;
        int shiftMove = move << 2;
        int shiftBlank = blank << 2;
        long tile = ((tiles >>> shiftMove) & 0xFL);
        res = (tiles & ~(0xFL << shiftBlank)) | (tile << shiftBlank);
        res = (res & ~(0xFL << shiftMove));
        return res;
    }

    public static int moveLeft(final long tiles, final int blank) {
        if ((blank & 3) != 0) {
            return blank - 1;
        }
        return -1;
    }

    public static int moveDown(final long tiles, final int blank) {
        if (blank < 12) {
            return blank + 4;            
        }
        return -1;
    }

    public static int moveRight(final long tiles, final int blank) {
        if ((blank & 3) != 3) {
            return blank + 1; 
        }
        return -1;
    }

    public static int moveUp(final long tiles, final int blank) {
        if (blank >= 4) {
            return blank - 4;
        }
        return -1;
    }
 
    /*   
    public static void main(String[] args) {
        int[][] tiles = new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 0}};
        Board board = new Board(tiles);
        System.out.println(board);
        System.out.printf("is goal? %b\n", board.isGoal()); 
        System.out.println("manhattan: " + board.manhattanValue + " pattern: " + board.patternValue);
        tiles = new int[][]{{1, 5, 9, 13}, {2, 6, 10, 14}, {3, 7, 11, 15}, {4, 8, 12, 0}};
        board = new Board(tiles);
        System.out.println(board);        
        System.out.printf("is goal? %b\n", board.isGoal()); 
        System.out.println("manhattan: " + board.manhattanValue + " pattern: " + board.patternValue);
        tiles = new int[][]{{12, 8, 3, 13}, {10, 6, 15, 2}, {4, 5, 1, 9}, {11, 14, 7, 0}};
        board = new Board(tiles);
        System.out.println(board);        
        System.out.printf("is goal? %b\n", board.isGoal()); 
        System.out.println("manhattan: " + board.manhattanValue + " pattern: " + board.patternValue);
        tiles = new int[][]{{5, 4, 3, 8}, {9, 2, 6, 1}, {0, 13, 14, 7}, {15, 11, 10, 12}};
        board = new Board(tiles);
        System.out.println(board);        
        System.out.printf("is goal? %b\n", board.isGoal()); 
        System.out.println("manhattan: " + board.manhattanValue + " pattern: " + board.patternValue);
    }
*/
 
}