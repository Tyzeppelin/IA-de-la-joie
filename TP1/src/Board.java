import java.util.ArrayList;
import java.util.Arrays;

public class Board implements Cloneable {
	private int[][] blocks;
	private int N;
	private int zeroLine;
	private int zeroCol;
	
    public Board(int[][] blocks) {
    	this.N = blocks.length;
    	this.blocks = new int[this.N][this.N];
    	for(int i = 0; i < this.N; i++) {
    		for(int j = 0; j < this.N; j++) {
    			this.blocks[i][j] = blocks[i][j];
    			if (blocks[i][j] == 0) {
    				this.zeroLine = i;
    				this.zeroCol = j;
    			}
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
        return this.manhattan() == 0;
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
		final int prime = 31;
		int result = 1;
		result = prime * result + N;
		result = prime * result + Arrays.deepHashCode(blocks);
		result = prime * result + zeroCol;
		result = prime * result + zeroLine;
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
		if (zeroCol != other.zeroCol)
			return false;
		if (zeroLine != other.zeroLine)
			return false;
		return true;
	}

	public Iterable<Board> neighbors() {
		ArrayList<Board> returnList = new ArrayList<Board>();
		if(zeroLine == 0){
			// si on est sur la ligne du haut
			Board b = this.clone();
			b.switchBottom(zeroLine, zeroCol);
			returnList.add(b);
		} else if (zeroLine == N-1) {
			// si on est sur la ligne du bas
			Board b = this.clone();
			b.switchTop(zeroLine, zeroCol);
			returnList.add(b);
		} else {
			// sinon on est sur une ligne au milieu de la matrice
			Board b1 = this.clone();
			Board b2 = this.clone();
			b1.switchTop(zeroLine, zeroCol);
			b2.switchBottom(zeroLine, zeroCol);
			returnList.add(b1);
			returnList.add(b2);
		}
		// idem au dessus mais avec les colones
		if(zeroCol == 0) {
			Board b = this.clone();
			b.switchRight(zeroLine, zeroCol);
			returnList.add(b);
		} else if(zeroCol == N-1) {
			Board b = this.clone();
			b.switchLeft(zeroLine, zeroCol);
			returnList.add(b);
		} else {
			Board b1 = this.clone();
			Board b2 = this.clone();
			b1.switchLeft(zeroLine, zeroCol);
			b2.switchRight(zeroLine, zeroCol);
			returnList.add(b1);
			returnList.add(b2);
		}
		returnList.trimToSize();
        return returnList;
    }
	
	private void switchLeft(int line, int col) {
		int temp = this.blocks[line][col];
		this.blocks[line][col] = this.blocks[line][col-1];
		this.blocks[line][col-1] = temp;
		this.zeroCol--;
	}
	
	private void switchRight(int line, int col) {
		int temp = this.blocks[line][col];
		this.blocks[line][col] = this.blocks[line][col+1];
		this.blocks[line][col+1] = temp;
		this.zeroCol++;
	}
	
	private void switchTop(int line, int col) {
		int temp = this.blocks[line][col];
		this.blocks[line][col] = this.blocks[line-1][col];
		this.blocks[line-1][col] = temp;
		this.zeroLine--;
	}
	
	private void switchBottom(int line, int col) {
		int temp = this.blocks[line][col];
		this.blocks[line][col] = this.blocks[line+1][col];
		this.blocks[line+1][col] = temp;
		this.zeroLine++;
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