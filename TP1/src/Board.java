import java.util.ArrayList;
import java.util.Arrays;

public class Board implements Cloneable {
	private int[][] blocks;
	private int N;
	
    public Board(int[][] blocks) {
    	this.N = blocks.length;
    	this.blocks = new int[this.N][this.N];
    	for(int i = 0; i < this.N; i++) {
    		for(int j = 0; j < this.N; j++) {
    			this.blocks[i][j] = blocks[i][j];
    		}
    	}
    }
    
    @Override
    public Board clone() {   
    	int[][] blocks = new int[this.N][this.N];
    	for(int i = 0; i < this.N; i++) {
    		for(int j = 0; j < this.N; j++) {
    			blocks[i][j] = this.blocks[i][j];
    		}
    	}
    	return new Board(blocks);
    }

    public int dimension() {
    	return N;
    }

    public int hamming() {
    	int distance = 0;
    	for(int i = 0; i < this.N; i++) {
    		for(int j = 0; j < this.N; j++) {
    			int value = blocks[i][j];
    			if ( value != 0 ){
        			int line = (value - 1) / N;
        			int col = (value - 1) % N;
        			if (Math.abs(i - line) + Math.abs(j - col) != 0) {
        				distance ++;
        			}
    			}
    		}
    	}
        return distance;
    }

    public int manhattan() {
    	int distance = 0;
    	for(int i = 0; i < this.N; i++) {
    		for(int j = 0; j < this.N; j++) {
    			int value = blocks[i][j];
    			if ( value != 0 ){
        			int line = (value - 1) / N;
        			int col = (value - 1) % N;
        			distance += Math.abs(i - line) + Math.abs(j - col);
    			}
    		}
    	}
        return distance;
    }

    public boolean isGoal() {
        return this.hamming() == 0;
    }

    public Board twin() {
    	int[][] newBlocks = new int[N][N];
    	for(int i = 0; i < this.N; i++) {
    		for(int j = 0; j < this.N; j++) {
    			newBlocks[i][j] = this.blocks[i][j];
    		}
    	}
    	if (newBlocks[0][0] == 0 || newBlocks[0][1] == 0){
    		int temp = newBlocks[1][0];
    		newBlocks[1][0] = newBlocks[1][1];
    		newBlocks[1][1] = temp;
    	} else {
    		int temp = newBlocks[0][0];
    		newBlocks[0][0] = newBlocks[0][1];
    		newBlocks[0][1] = temp;
    	}
        return new Board(newBlocks);
    }

	@Override
	public int hashCode() {
		final int prime = 2147483647;
		int result = 1;
		result = prime * result + N;
		result = prime * result + Arrays.deepHashCode(blocks);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (N != other.N)
			return false;
		if (!Arrays.deepEquals(blocks, other.blocks))
			return false;
		return true;
	}

	public Iterable<Board> neighbors() {
		ArrayList<Board> returnList = new ArrayList<Board>();
		int zeroLine = 0;
		int zeroCol = 0;
		boolean find = false;
		for(int i = 0; i < this.N && !find; i++) {
    		for(int j = 0; j < this.N && !find; j++) {
    			if (this.blocks[i][j] == 0) {
    				zeroLine = i;
    				zeroCol = j;
    				find = true;
    			}
    		}
    	}
		Board b1 = this.clone();
		Board b2 = this.clone();
		if(zeroLine == 0){
			if(zeroCol == 0) {
				// 0, 0
				b1.switchRight(zeroLine, zeroCol);
				b2.switchBottom(zeroLine, zeroCol);
			} else if (zeroCol == N-1) {
				// 0, N-1
				b1.switchLeft(zeroLine, zeroCol);
				b2.switchBottom(zeroLine, zeroCol);
			} else {
				// 0, X
				Board b3 = this.clone();
				b1.switchLeft(zeroLine, zeroCol);
				b2.switchRight(zeroLine, zeroCol);
				b3.switchBottom(zeroLine, zeroCol);
				returnList.add(b3);
			}
		} else if (zeroLine == N-1) {
			if(zeroCol == 0) {
				// N-1, 0
				b1.switchRight(zeroLine, zeroCol);
				b2.switchTop(zeroLine, zeroCol);
			} else if (zeroCol == N-1) {
				// N-1, N-1
				b1.switchLeft(zeroLine, zeroCol);
				b2.switchTop(zeroLine, zeroCol);
			} else {
				// N-1, X
				Board b3 = this.clone();
				b1.switchLeft(zeroLine, zeroCol);
				b2.switchRight(zeroLine, zeroCol);
				b3.switchTop(zeroLine, zeroCol);
				returnList.add(b3);
			}
		} else {
			Board b3 = this.clone();
			if(zeroCol == 0) {
				// X, 0
				b1.switchRight(zeroLine, zeroCol);
				b2.switchBottom(zeroLine, zeroCol);
				b3.switchTop(zeroLine, zeroCol);
			} else if (zeroCol == N-1) {
				// X, N-1
				b1.switchLeft(zeroLine, zeroCol);
				b2.switchBottom(zeroLine, zeroCol);
				b3.switchTop(zeroLine, zeroCol);
			} else {
				// X, X
				Board b4 = this.clone();
				b1.switchLeft(zeroLine, zeroCol);
				b2.switchRight(zeroLine, zeroCol);
				b3.switchBottom(zeroLine, zeroCol);
				b4.switchTop(zeroLine, zeroCol);
				returnList.add(b4);
			}
			returnList.add(b3);
		}
		returnList.add(b1);
		returnList.add(b2);
        return returnList;
    }
	
	private void switchLeft(int line, int col) {
		int temp = this.blocks[line][col];
		this.blocks[line][col] = this.blocks[line-1][col];
		this.blocks[line-1][col] = temp;
	}
	
	private void switchRight(int line, int col) {
		int temp = this.blocks[line][col];
		this.blocks[line][col] = this.blocks[line+1][col];
		this.blocks[line+1][col] = temp;
	}
	
	private void switchTop(int line, int col) {
		int temp = this.blocks[line][col];
		this.blocks[line][col] = this.blocks[line][col-1];
		this.blocks[line][col-1] = temp;
	}
	
	private void switchBottom(int line, int col) {
		int temp = this.blocks[line][col];
		this.blocks[line][col] = this.blocks[line][col+1];
		this.blocks[line][col+1] = temp;
	}

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", (int)blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}