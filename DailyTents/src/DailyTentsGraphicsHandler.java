import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DailyTentsGraphicsHandler extends JPanel {
	Board gameBoard = null;
	int[] columnTentCounts = null;
	int[] rowTentCounts = null;
	
	JFrame mainFrame = null;
	
	protected final static int[] MENU_WINDOW_SIZE = {400,120};
	protected final static int[] DEFAULT_GAME_WINDOW_SIZE = {100,180};
	protected final static int[] END_WINDOW_SIZE =  {900,900};
	
	protected final static int[] BOARD_START_COORDINATES = {30,90}; 
	protected final static int BOARD_CELL_LENGTH = 40;
	
	protected int drawMode = 0;
	protected static final int DRAW_MENU = 0;
	protected static final int DRAW_GAME = 1;
	protected static final int DRAW_WIN  = 2;
	protected static final int DRAW_LOSE = 3;
	
	protected EButton[]   menuButtons = null;
	protected EButton[]   gameButtons = null;	
	protected EButton[]   winButtons  = null;
	protected EButton[]   loseButtons = null;
	protected EButton[][] cellButtons = null;
	
	BufferedImage tentImage      = null;
	BufferedImage treeImage      = null;
	BufferedImage loseBackground = null;
	BufferedImage winBackground  = null;
	
	public DailyTentsGraphicsHandler(){
		mainFrame = new JFrame();
		mainFrame.add(this);
		mainFrame.setSize(MENU_WINDOW_SIZE[0], MENU_WINDOW_SIZE[1]);
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle("Daily Tents");
		
		initializeMenuButtons();
		initializeGameButtons();
		initializeWinButtons();
		initializeLoseButtons();
		loadImages();
	}
	
	private void initializeMenuButtons(){
		menuButtons = new EButton[4];
		menuButtons[0] = new EButton(20,20,50,30,"8x8");
		menuButtons[1] = new EButton(80,20,80,30,"12x12");
		menuButtons[2] = new EButton(170,20,80,30,"16x16");
		menuButtons[3] = new EButton(260,20,80,30,"20x20");
		
	}
	
    private void initializeGameButtons(){
		gameButtons = new EButton[3];
		gameButtons[0] = new EButton(30,20,100,30,"Restart");
		gameButtons[1] = new EButton(140,20,65,30,"Menu");
		gameButtons[2] = new EButton(215,20,75,30,"Check");
	}
    
    private void initializeWinButtons(){
    	winButtons = new EButton[2];
    	winButtons[0] = new EButton(330,700,110,30,"Restart");
    	winButtons[1] = new EButton(460,700,90,30,"Menu");
	}
    
    private void initializeLoseButtons(){
		loseButtons = new EButton[2];
		loseButtons[0] = new EButton(330,750,110,30,"Restart");
    	loseButtons[1] = new EButton(460,750,70,30,"Menu");
   	}
    
    private void loadImages(){
		this.loseBackground = loadImage("data/scaryface.png");
		this.treeImage = loadImage("data/tree.png");
		this.tentImage = loadImage("data/tent.png");
		this.winBackground = loadImage("data/win.png");
    }
    
    private BufferedImage loadImage(String fileName){
    	BufferedImage img = null;
    	try {
    	    img = ImageIO.read(new File(fileName));
    	} catch (IOException e) {
    		System.out.println("Failed to load Image");
    	}
    	return img;
    }
    
    public void paint(Graphics g){
    	int style = Font.BOLD | Font.ITALIC;
    	g.setFont(new Font("Courier", style , 18));
    	
    	switch(drawMode){
    		case DRAW_GAME : paintGame(g); break;
    		case DRAW_MENU : paintMenu(g); break;
    		case DRAW_WIN  : paintWin(g);  break;
    		case DRAW_LOSE : paintLose(g); break;
    	}
    }
    
    private void paintGame(Graphics g){
    	this.mainFrame.setSize(DEFAULT_GAME_WINDOW_SIZE[0] + gameBoard.getBoardSize()*BOARD_CELL_LENGTH
    			, DEFAULT_GAME_WINDOW_SIZE[1] + gameBoard.getBoardSize()*BOARD_CELL_LENGTH);
    	paintGameButtons(g);
    	paintBoardColumnData(g);
    	paintBoardRowData(g);
    	paintBoard(g);
    }

	private void paintGameButtons(Graphics g) {
		for(int i = 0; i< gameButtons.length; i++){
    		paintButton(g,gameButtons[i]);
    	}
	}
	
    private void paintButton(Graphics g,EButton button){
    	if(button.isMouseOnButton){
    		g.setColor(Color.YELLOW);
    	}
    	else{
    		g.setColor(Color.CYAN);
    	}
    	
    	g.fillRect(button.x, button.y, button.xSize, button.ySize);
    	g.setColor(Color.RED);
    	g.drawRect(button.x, button.y, button.xSize, button.ySize);  
    	g.drawString(button.getButtonText(), button.x+button.xSize/7, (int)(button.y+button.ySize/1.5));
    }
    
    private void paintBoardColumnData(Graphics g){
    	for(int i = 1; i<columnTentCounts.length+1; i++){
    		g.setColor(Color.cyan);
    		g.fillRect(BOARD_START_COORDINATES[0] + BOARD_CELL_LENGTH*i, BOARD_START_COORDINATES[1], BOARD_CELL_LENGTH, BOARD_CELL_LENGTH);
    		g.setColor(Color.RED);
    		g.drawRect(BOARD_START_COORDINATES[0] + BOARD_CELL_LENGTH*i, BOARD_START_COORDINATES[1], BOARD_CELL_LENGTH, BOARD_CELL_LENGTH);
    		g.drawString(Integer.toString(columnTentCounts[(i-1)]), BOARD_START_COORDINATES[0] + BOARD_CELL_LENGTH*i +10
    				, (int)(BOARD_START_COORDINATES[1]+BOARD_CELL_LENGTH/2));
    	}
    }
    
    private void paintBoardRowData(Graphics g){
    	for(int i = 1; i<rowTentCounts.length+1; i++){
    		g.setColor(Color.cyan);
    		g.fillRect(BOARD_START_COORDINATES[0], BOARD_START_COORDINATES[1] + BOARD_CELL_LENGTH*i, BOARD_CELL_LENGTH, BOARD_CELL_LENGTH);
    		g.setColor(Color.RED);
    		g.drawRect(BOARD_START_COORDINATES[0], BOARD_START_COORDINATES[1] + BOARD_CELL_LENGTH*i, BOARD_CELL_LENGTH, BOARD_CELL_LENGTH);
    		g.drawString(Integer.toString(rowTentCounts[(i-1)]), BOARD_START_COORDINATES[0] + BOARD_CELL_LENGTH/3
    				, (int)(BOARD_START_COORDINATES[1]+ + BOARD_CELL_LENGTH*i +BOARD_CELL_LENGTH/2));
    	}
    }
    
    private void paintBoard(Graphics g){
    	for(int y = 0; y<gameBoard.getBoardSize(); y++){
    		for(int x = 0; x<gameBoard.getBoardSize(); x++){
    			paintCell(g,x,y);
    		}
    	}
    }
    
    private void paintCell(Graphics g,int x,int y){  	
    	if(gameBoard.checkLocationForTent(x, y)){
    		paintTentCell(g, x, y);
    	}
    	else if(gameBoard.checkLocationForTree(x, y)){
    		paintTreeCell(g, x, y);
    	}
    	else{
    		EButton cellButton = cellButtons[x][y];
    		paintButton(g,cellButton);
    	}
    }

	private void paintTentCell(Graphics g, int x, int y) {
		EButton cellButton = cellButtons[x][y];
		
		g.drawImage(tentImage, BOARD_START_COORDINATES[0] + BOARD_CELL_LENGTH * (x+1)+1,
				BOARD_START_COORDINATES[1] + BOARD_CELL_LENGTH * (y+1)+1,
				null);
		g.setColor(Color.RED);
		g.drawRect(cellButton.x, cellButton.y, cellButton.xSize, cellButton.ySize);  
		
		if(cellButton.isMouseOnButton){
			Color tempColor = new Color(255,0,0,60);
			g.setColor(tempColor);
			g.fillRect(cellButton.x, cellButton.y, cellButton.xSize, cellButton.ySize); 
		}
	}

	private void paintTreeCell(Graphics g, int x, int y) {
		EButton cellButton = cellButtons[x][y];
		
		g.drawImage(treeImage, BOARD_START_COORDINATES[0] + BOARD_CELL_LENGTH * (x+1)+1,
				BOARD_START_COORDINATES[1] + BOARD_CELL_LENGTH * (y+1)+1,
				null);
		g.setColor(Color.RED);
		g.drawRect(cellButton.x, cellButton.y, cellButton.xSize, cellButton.ySize);  
		
		if(cellButton.isMouseOnButton){
			Color tempColor = new Color(0,0,0,200);
			g.setColor(tempColor);
			g.fillRect(cellButton.x, cellButton.y, cellButton.xSize, cellButton.ySize); 
		}
	}
    
    private void paintMenu(Graphics g){
    	this.mainFrame.setSize(MENU_WINDOW_SIZE[0], MENU_WINDOW_SIZE[1]);
    	for(int i = 0; i< menuButtons.length; i++){
    		paintButton(g,menuButtons[i]);
    	}
    }
    
    private void paintWin(Graphics g){
    	this.mainFrame.setSize(END_WINDOW_SIZE[0], END_WINDOW_SIZE[1]);
    	g.drawImage(winBackground, 0, 0, null);
    	
    	for(int i = 0; i< winButtons.length; i++){
    		paintButton(g,winButtons[i]);
    	}
    	
    	Font newFont = new Font("Courier",Font.BOLD | Font.ITALIC,35);
    	g.setFont(newFont);
    	g.drawString("You Win", 325, 830);
    }
    
    private void paintLose(Graphics g){
    	this.mainFrame.setSize(END_WINDOW_SIZE[0], END_WINDOW_SIZE[1]);
    	g.drawImage(loseBackground, 0, 0, null);
    	
    	for(int i = 0; i< loseButtons.length; i++){
    		paintButton(g,loseButtons[i]);
    	}
    	
    	Font newFont = new Font("Courier",Font.BOLD | Font.ITALIC,35);
    	g.setFont(newFont);
    	g.drawString("You Lost", 325, 830);
    }
    
	public void updateBoard(Board board){
		this.gameBoard = board;
		this.columnTentCounts = board.getColumnTentCounts();
		this.rowTentCounts = board.getRowTentCounts();
	}
	
	protected void switchToDrawGameMode(){
		this.drawMode = DRAW_GAME;
		this.cellButtons = new EButton[gameBoard.getBoardSize()][gameBoard.getBoardSize()];
		fillCellButtons();
	}
	
	protected void fillCellButtons(){
		for(int i = 0; i<cellButtons.length; i++){
			for(int j = 0; j<cellButtons.length; j++){
				cellButtons[i][j] = new EButton(
						BOARD_START_COORDINATES[0] + BOARD_CELL_LENGTH * (i+1),
						BOARD_START_COORDINATES[1] + BOARD_CELL_LENGTH * (j+1),
						BOARD_CELL_LENGTH,
						BOARD_CELL_LENGTH,
						"");
			}
		}
	}
} 

