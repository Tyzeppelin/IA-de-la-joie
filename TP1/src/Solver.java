import java.util.LinkedList;
import java.util.PriorityQueue;

public class Solver {
	
	private PriorityQueue<SearchNode> morpheus;

    private static class SearchNode implements Comparable<SearchNode> {
    	private Board node;
    	private SearchNode vader;
    	private int distance;
		private int manhattanKaboul;
    	public SearchNode(Board node, SearchNode pere, int distance) {
			super();
			this.node = node;
			this.vader = pere;
			this.distance = distance;
			this.manhattanKaboul = node.manhattan();
		}
    	
    	public SearchNode getFather() {
    		return this.vader;
    	}
    	
    	private int getPriority() {
    		return this.distance + this.manhattanKaboul;
    	}
    	
    	public boolean isFinish() {
    		return this.manhattanKaboul == 0;
    	}
    	
    	public Iterable<Board> getNeighbors() {
    		return this.node.neighbors();
    	}
    	
		@Override
        public int compareTo(SearchNode searchNode) {
            return this.getPriority() - searchNode.getPriority();
        }

		@Override
		public boolean equals(Object obj) {
			return this.node.equals(node);
		}
    }

    public Solver(Board initial) {
    	morpheus = new PriorityQueue<SearchNode>();
    	morpheus.add(new SearchNode(initial, null, 0));
    }

    private void aStar() {
    	while (!morpheus.peek().isFinish()){
    		SearchNode vader = morpheus.poll();
    		int dis = vader.distance;
    		for (Board neigh: vader.getNeighbors()) {
    			if (vader.vader == null || !neigh.equals(vader.vader.node)) {
    				morpheus.add(new SearchNode(neigh, vader,  dis+1));
    			}
    		}
    	}
    }

    public boolean isSolvable() {
        return true;
    }

    public int moves() {
    	return morpheus.peek().node.manhattan();
    }

    public Iterable<Board> solution() {
    	aStar();
    	LinkedList<Board> anakin = new LinkedList<Board>();
    	SearchNode aqueuse = morpheus.peek();
    	while(aqueuse.getFather() != null) {
    		anakin.addFirst(aqueuse.node);
    		aqueuse = aqueuse.getFather();
    	}
    	return anakin;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            long startTime = System.nanoTime();
           
            for (Board board : solver.solution()){
                StdOut.println(board);
            }
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            System.out.println("duration time: " + duration / 1000000);
        }
    }
}
