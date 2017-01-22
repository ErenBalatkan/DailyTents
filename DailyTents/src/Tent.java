
public class Tent extends BoardObject{
	Tree attachedTree = null;
	
	public Tent(int x,int y){
		super(x,y);
	}
	
	protected boolean hasAdjectant(){
		if(attachedTree == null){
			return false;
		}
		return true;
	}
	
	protected void attachTree(Tree tree){
		int xAxisDistance = Math.abs(this.getX() - tree.getX());
		int yAxisDistance = Math.abs(this.getY() - tree.getY());
		
		if(xAxisDistance == 0 && yAxisDistance == 0){
			System.out.println("Failed to adject Tree");
			return;
		}
		
		if(xAxisDistance > 1 || yAxisDistance > 1){
			System.out.println("Failed to adject Tree");
			return;
		}
		
		this.attachedTree = tree;
	}

}
