import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class DailyTentsController{
	BoardHandler boardHandler = null;
	Board board = null;
	Board gameBoard = null;
	DailyTentsGraphicsHandler boardPainter = null;
	
	static final private ArrayList<Integer> avaibleSizes = new ArrayList<Integer>(Arrays.asList(8,12,16,20));
	static final private ArrayList<Integer> tentCountsForSizes = new ArrayList<Integer>(Arrays.asList(12,28,51,80));	
	
	public DailyTentsController() {
		this.boardPainter = new DailyTentsGraphicsHandler();
		InputHandler inputHandler = new InputHandler(this,boardPainter);
		this.boardPainter.addMouseListener(inputHandler);
		this.boardPainter.addMouseMotionListener(inputHandler);
	}
	
	public void displayMenu(){
	}
	
	public void initializeGame(String size){
		switch(size){
		case "8x8":   initializeGame(8);  break;
		case "12x12": initializeGame(12); break;
		case "16x16": initializeGame(16); break;
		case "20x20": initializeGame(20); break;
		}
	}
	
	public void initializeGame(int size){
		int tentCount = tentCountsForSizes.get(avaibleSizes.indexOf(size));
		
		this.board = new Board(size,tentCount);
		this.boardHandler = new BoardHandler(this.board);
		
		this.boardHandler.fillBoard();
		this.gameBoard = boardHandler.getGameBoard();
		
		this.boardPainter.columnTentCounts = this.board.getColumnTentCounts();
		this.boardPainter.rowTentCounts = this.board.getRowTentCounts();
		
		this.boardPainter.updateBoard(this.gameBoard);
		this.boardPainter.switchToDrawGameMode();
		this.boardPainter.repaint();
	}
	
	protected void runCellFunction(EButton cellButton){
		int x = cellButton.getX();
		x -= DailyTentsGraphicsHandler.BOARD_START_COORDINATES[0];
		x /= DailyTentsGraphicsHandler.BOARD_CELL_LENGTH;
		x--;
		
		int y = cellButton.getY();
		y -= DailyTentsGraphicsHandler.BOARD_START_COORDINATES[1];
		y /= DailyTentsGraphicsHandler.BOARD_CELL_LENGTH;
		y--;
		
		BoardObject currentObject = gameBoard.getBoardObjectAt(x, y);
		
		if(currentObject == null) addTentTo(x,y);
		else if(currentObject instanceof Tent) removeTentFrom(x,y);
	}
	
	private void addTentTo(int x, int y){
		gameBoard.addTentTo(x, y);
	}
	
	private void removeTentFrom(int x,int y){
		gameBoard.removeTentFrom(x, y);
	}

	private void displayWin(){
		this.boardPainter.drawMode = DailyTentsGraphicsHandler.DRAW_WIN;
	}
	
	private void displayLose(){
		this.boardPainter.drawMode = DailyTentsGraphicsHandler.DRAW_LOSE;
	}
	
	
	protected void runButtonFunction(EButton button){
		String actionName = button.getButtonText();
		switch(actionName){
		case "Restart": restartGame(); break;
		case "Menu": returnMenu(); break;
		case "Check": checkGameSituation(); break;
		}
	}
	
	private void restartGame(){
		this.boardHandler.fillBoard();
		this.gameBoard = this.boardHandler.getGameBoard();
		this.boardPainter.updateBoard(this.gameBoard);
		this.boardPainter.columnTentCounts = this.gameBoard.getColumnTentCounts();
		this.boardPainter.rowTentCounts = this.gameBoard.getRowTentCounts();
		this.boardPainter.fillCellButtons();
		this.boardPainter.repaint();
		this.boardPainter.switchToDrawGameMode();
	}	
	
	private void checkGameSituation(){
		if(gameBoard.equals(board)){
			displayWin();
		}
		else{
			displayLose();
			playSound("data/scarysound.wav");
		}
	}
	
	  private void playSound(String fileName){	    	
			try {
				String soundFile = fileName;
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFile).getAbsoluteFile());
				Clip clip;
				clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
			} catch (UnsupportedAudioFileException e) {
				System.out.println("Unsupportedformat");
			} catch (IOException e) {
				System.out.println("cant find");
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		
  }
	
	private void returnMenu(){
		this.boardPainter.drawMode = DailyTentsGraphicsHandler.DRAW_MENU;
	}
}
