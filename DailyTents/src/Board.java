import java.util.Arrays;
import java.util.prefs.BackingStoreException;

public class Board {

	private BoardObject[][] boardDataMatrix   = null;
	private int columnTentCounts[]            = null;
	private int rowTentCounts[]               = null;
	private int boardSize = 0;
	
	protected int totalTentCount = 0;	
	protected int currentTentCount = 0;

	private boolean availabilityMatrix[][] = null;
	private static final boolean AVAILABLE = true;
	private static final boolean UNAVAILABLE = false;
	
	public Board(int size,int tentCount){
		this.boardSize = size;
		this.totalTentCount = tentCount;
		
		reset();
	}
	
	protected void reset(){
		availabilityMatrix = new boolean[this.boardSize][this.boardSize];
		boardDataMatrix = new BoardObject[this.boardSize][this.boardSize];

		columnTentCounts = new int[this.boardSize];
		rowTentCounts = new int[this.boardSize];
		
		currentTentCount = 0;
		
		initializeMarkMatrix();
	}
	
	protected Board cloneTentlessBoard(){
		Board clone = new Board(boardSize,totalTentCount);
		clone.columnTentCounts = Arrays.copyOf(columnTentCounts, columnTentCounts.length);
		clone.rowTentCounts = Arrays.copyOf(rowTentCounts, rowTentCounts.length);
		
		return copyTreesTo(clone);
	}
	
	private Board copyTreesTo(Board clone){
		for(int i = 0; i<boardSize;i++){
			for(int j = 0; j<boardSize;j++){
				if(this.checkLocationForTree(i, j)){
					clone.addTreeTo(i, j);
				}
			}
		}
		
		return clone;
	}
	
	private void initializeMarkMatrix(){
		for(int i = 0; i<this.boardSize; i++){
			for(int j = 0; j< this.boardSize; j++){
				availabilityMatrix[i][j] = AVAILABLE;
			}
		}
	}
	
	protected int getBoardSize(){
		return this.boardSize;
	}
	
	protected BoardObject getBoardObjectAt(int x,int y){
		return boardDataMatrix[x][y];
	}
	
	protected int[] getRowTentCounts(){
		return this.rowTentCounts;
	}
	
	protected int[] getColumnTentCounts(){
		return this.columnTentCounts;
	}
	
	protected void removeTentFrom(int x, int y){
		if(checkLocationForTent(x, y)){
			this.boardDataMatrix[x][y] = null;
		}
	}
	
	protected boolean checkForAvailability(int x,int y){
		try {
			if(boardDataMatrix[x][y] != null) return false;
		} catch (Exception e) {
			return false;
		}
		
		return availabilityMatrix[x][y] == AVAILABLE;			
	}

	protected void addTentTo(int x,int y){
		currentTentCount ++;
		Tent additionTent = new Tent(x,y);
		boardDataMatrix[x][y] = additionTent;
		markAreaUnavailable(x,y);
	}
	
	private void markAreaUnavailable(int x,int y){
		markLocationUnavailable(x-1,y-1);
		markLocationUnavailable(x,y-1);
		markLocationUnavailable(x+1,y-1);
		markLocationUnavailable(x-1,y);
		markLocationUnavailable(x+1,y);
		markLocationUnavailable(x-1,y+1);
		markLocationUnavailable(x,y+1);
		markLocationUnavailable(x+1,y+1);
		markLocationUnavailable(x,y);
		}
	
	private void markLocationUnavailable(int x,int y){
		try {
			availabilityMatrix[x][y] = UNAVAILABLE;
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}
	
	protected boolean checkLocationForTent(int x,int y){
		BoardObject currentObject = null;
		try {
			currentObject = boardDataMatrix[x][y];
		} catch (Exception e) {
			return false;
		}
		if(currentObject instanceof Tent){
			return true;
		}
		return false;
	}
	
	protected boolean checkLocationForTree(int x,int y){
		BoardObject currentObject = null;
		try {
			currentObject = boardDataMatrix[x][y];
		} catch (Exception e) {
			return false;
		}
		if(currentObject instanceof Tree){
			return true;
		}
		return false;
	}
	
	protected boolean isLocationEmpty(int x,int y){
		if(getBoardObjectAt(x, y) == null){
			return true;
		}	
		return false;
	}
	
	protected int getAvailableLocationCount(){
		int unmarkedLocationCount = 0;
		
		for(int i=0; i<boardSize; i++){
			for(int j=0; j<boardSize; j++){
				if(availabilityMatrix[i][j] == AVAILABLE){
					unmarkedLocationCount ++;
				}
			}
		}
		
		return unmarkedLocationCount;
	}

	protected void prepeareTentCountArrays(){
		for(int i = 0; i<boardSize; i++){
			for(int j = 0; j<boardSize; j++){
				checkLocationAndIncreaseTentCount(i, j);
			}
		}
	}
	
	private void checkLocationAndIncreaseTentCount(int x,int y){	
		if(checkLocationForTent(x, y)){
			rowTentCounts[y] ++;
			columnTentCounts[x] ++;
		}
	}
	
	protected boolean addTreeTo(int x,int y,Tent tent){
		if(x >= 0 && x< boardSize && y>= 0 && y < boardSize){
			if(isLocationEmpty(x,y)){
				Tree tree = new Tree(x,y);
				tent.attachTree(tree);
				boardDataMatrix[x][y] = tree;
				return true;
			}
		}
		
		return false;
	}
	
	protected boolean addTreeTo(int x,int y){
		if(x >= 0 && x< boardSize && y>= 0 && y < boardSize){
			if(isLocationEmpty(x,y)){
				Tree tree = new Tree(x,y);
				boardDataMatrix[x][y] = tree;
				return true;
			}
		}
		
		return false;
	}
	
	protected boolean equals(Board board){
		for(int i = 0; i<board.getBoardSize();i++){
			for(int j= 0; j<board.getBoardSize();j++){
				if(compareBoardElements(i,j,board) == false) return false;
			}
		}
		return true;
	}
	
	protected boolean compareBoardElements(int x,int y,Board board){
		if(board.getBoardObjectAt(x, y) instanceof Tree && getBoardObjectAt(x, y) instanceof Tree){
			return true;
		}
		if(board.getBoardObjectAt(x, y) instanceof Tent && getBoardObjectAt(x, y) instanceof Tent){
			return true;
		}
		if(board.getBoardObjectAt(x, y) == null && getBoardObjectAt(x, y) == null){
			return true;
		}
		
		return false;
	}

}
