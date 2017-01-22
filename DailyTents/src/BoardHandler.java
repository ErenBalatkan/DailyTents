import java.util.Random;

public class BoardHandler {	
	private Board board;
	static final int TENT_PLACEMENT_CHANCE = 40;	
	private static Random RANDOM_GENERATOR = new Random();
	
	public BoardHandler(Board board){
		this.board = board;
	}
	
	protected Board getBoard(){
		return board;
	}
	
	protected Board getGameBoard(){
		return board.cloneTentlessBoard();
	}
	
	
	protected void fillBoard(){
		board.reset();
		addTentsUntilBoardIsFull();
		board.prepeareTentCountArrays();
		addTreesToBoard();
	}

	private void addTentsUntilBoardIsFull() {
		while(true){
			if(board.getAvailableLocationCount() <= 0){
				board.reset();
			}
			
			addTentsRandomlyToBoard();
			
			if(board.currentTentCount >= board.totalTentCount){
				break;
			}
		}
	}

	private void addTentsRandomlyToBoard(){
		for(int i = 0; i<board.getBoardSize(); i++){
			for(int j = 0; j<board.getBoardSize(); j++){
				addTentWithChance(i, j);
			}
		}	
	}
	
	private void addTentWithChance(int x,int y){
		int randomValue = RANDOM_GENERATOR.nextInt(100);
		
		if(randomValue < TENT_PLACEMENT_CHANCE){
			if(board.currentTentCount >= board.totalTentCount){
				return;
			}
			if(board.checkForAvailability(x, y)){
				board.addTentTo(x, y);
			}
		}
	}

	private void addTreesToBoard(){
		for(int i = 0; i<board.getBoardSize(); i++){
			for(int j = 0; j<board.getBoardSize(); j++){
				addTreeToTentAt(i, j);
			}
		}
	}
	
	private void addTreeToTentAt(int x,int y){
		boolean isTent = board.checkLocationForTent(x, y);
		
		if(isTent == false)
			return;
		
		BoardObject tent = board.getBoardObjectAt(x, y);
		addTreeAttachedTo((Tent)tent);	
	}
	
	private void addTreeAttachedTo(Tent tent){
		boolean additionCheck = false;
		
		while(additionCheck == false){
			
			int randomValue = RANDOM_GENERATOR.nextInt(4);
			
			switch(randomValue){
			case 0 : additionCheck = board.addTreeTo(tent.getX()-1,tent.getY(),tent); break;
			case 1 : additionCheck = board.addTreeTo(tent.getX()+1,tent.getY(),tent); break;
			case 2 : additionCheck = board.addTreeTo(tent.getX(),tent.getY()-1,tent); break;
			case 3 : additionCheck = board.addTreeTo(tent.getX(),tent.getY()+1,tent); break;
			}
		}
	}
	
}
