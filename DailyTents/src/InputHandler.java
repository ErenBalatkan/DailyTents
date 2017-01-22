import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class InputHandler implements MouseListener,MouseMotionListener{
	DailyTentsController controller = null;
	DailyTentsGraphicsHandler graphicsHandler = null;
	
	public InputHandler(DailyTentsController controller,DailyTentsGraphicsHandler graphicsHandler){
		this.controller = controller;
		this.graphicsHandler = graphicsHandler;
	}
	
	public void mousePressed(MouseEvent e) {
		switch(graphicsHandler.drawMode){
		
		case DailyTentsGraphicsHandler.DRAW_MENU:
			menuMousePressed(e); break;
			
		case DailyTentsGraphicsHandler.DRAW_GAME:
			gameMousePressed(e); break;
			
		case DailyTentsGraphicsHandler.DRAW_LOSE:
			loseMousePressed(e); break;
			
		case DailyTentsGraphicsHandler.DRAW_WIN:
			winMousePressed(e); break;
		}
	}
	
	public void menuMousePressed(MouseEvent e){
		EButton[] menuButtons = graphicsHandler.menuButtons;
		
		for(int i = 0; i<menuButtons.length; i++){
			if(menuButtons[i].isClicked(e.getX(),e.getY())){
				controller.initializeGame(menuButtons[i].getButtonText());
			}
		}
		graphicsHandler.repaint();
	}
	
	public void gameMousePressed(MouseEvent e){
		EButton[] gameButtons = graphicsHandler.gameButtons;
		EButton[][] cellButtons = graphicsHandler.cellButtons;
		
		for(int i = 0; i<gameButtons.length; i++){
			if(gameButtons[i].isClicked(e.getX(),e.getY())){
				controller.runButtonFunction(gameButtons[i]);
			}
		}
		
		for(int i = 0; i<cellButtons[0].length; i++){
			for(int j = 0; j<cellButtons.length; j++){
				if(cellButtons[i][j].isClicked(e.getX(),e.getY())){
					this.controller.runCellFunction(cellButtons[i][j]);
				}
			}
		}
		
		graphicsHandler.repaint();
	}
	
	public void loseMousePressed(MouseEvent e){
		EButton[] loseButtons = graphicsHandler.loseButtons;
		
		for(int i = 0; i<loseButtons.length; i++){
			if(loseButtons[i].isClicked(e.getX(),e.getY())){
				controller.runButtonFunction(loseButtons[i]);
			}
		}
		graphicsHandler.repaint();
	}
	
	public void winMousePressed(MouseEvent e){
		EButton[] winButtons = graphicsHandler.winButtons;
		
		for(int i = 0; i<winButtons.length; i++){
			if(winButtons[i].isClicked(e.getX(),e.getY())){
				controller.runButtonFunction(winButtons[i]);
			}
		}
		graphicsHandler.repaint();
	}

	public void mouseMoved(MouseEvent e) {
		switch(graphicsHandler.drawMode){

		case DailyTentsGraphicsHandler.DRAW_MENU:
			menuMouseMoved(e); break;

		case DailyTentsGraphicsHandler.DRAW_GAME:
			gameMouseMoved(e); break;

		case DailyTentsGraphicsHandler.DRAW_LOSE:
			loseMouseMoved(e); break;

		case DailyTentsGraphicsHandler.DRAW_WIN:
			winMouseMoved(e); break;
		}
	}
	
	public void menuMouseMoved(MouseEvent e){
		EButton[] menuButtons = graphicsHandler.menuButtons;
		
		for(int i = 0; i<menuButtons.length; i++){
			if(menuButtons[i].isClicked(e.getX(),e.getY())){
				menuButtons[i].isMouseOnButton = true;
			}
			else{
				menuButtons[i].isMouseOnButton = false;
			}
		}
		graphicsHandler.repaint();
	}
	
	public void gameMouseMoved(MouseEvent e){
		EButton[] gameButtons = graphicsHandler.gameButtons;
		EButton[][] cellButtons = graphicsHandler.cellButtons;
		
		for(int i = 0; i<gameButtons.length; i++){
			if(gameButtons[i].isClicked(e.getX(),e.getY())){
				gameButtons[i].isMouseOnButton = true;
			}
			else{
				gameButtons[i].isMouseOnButton = false;
			}
		}
		
		for(int i = 0; i<cellButtons[0].length; i++){
			for(int j = 0; j<cellButtons.length; j++){
				if(cellButtons[i][j].isClicked(e.getX(),e.getY())){
					cellButtons[i][j].isMouseOnButton = true;
				}
				else{
					cellButtons[i][j].isMouseOnButton = false;
				}
			}
		}
		graphicsHandler.repaint();
	}
	
	public void loseMouseMoved(MouseEvent e){
		EButton[] loseButtons = graphicsHandler.loseButtons;
		
		for(int i = 0; i<loseButtons.length; i++){
			if(loseButtons[i].isClicked(e.getX(),e.getY())){
				loseButtons[i].isMouseOnButton = true;
			}
			else{
				loseButtons[i].isMouseOnButton = false;
			}
		}
		graphicsHandler.repaint();
	}
	
	public void winMouseMoved(MouseEvent e){
		EButton[] winButtons = graphicsHandler.winButtons;
		
		for(int i = 0; i<winButtons.length; i++){
			if(winButtons[i].isClicked(e.getX(),e.getY())){
				winButtons[i].isMouseOnButton = true;
			}
			else{
				winButtons[i].isMouseOnButton = false;
			}
		}
		graphicsHandler.repaint();
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}

	

}