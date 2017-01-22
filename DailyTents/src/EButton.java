
public class EButton extends BoardObject {
	protected int xSize,ySize;
	private String buttonText = null;
	
	protected boolean isMouseOnButton = false;
	
	public EButton(int x,int y,int xSize,int ySize,String buttonText){
		super(x,y);
		this.xSize = xSize;
		this.ySize = ySize;
		this.buttonText = buttonText;
	}
	
	public String getButtonText(){
		return this.buttonText;
	}
	
	protected boolean isClicked(int x,int y){
		if(x > this.x && x < this.x + this.xSize &&
				y > this.y && y < this.y + this.ySize) return true;
		
		return false;
	}

}
