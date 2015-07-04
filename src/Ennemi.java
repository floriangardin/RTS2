import org.newdawn.slick.Color;


public class Ennemi extends Personnage {
	
	public Ennemi(Plateau p, float x_origin, float y_origin,int camps,int framerate) {
		
		super(p,x_origin,y_origin,20f,10f,3f,camps,framerate);
		this.w = 20f;
		this.color = Color.red;
		// TODO Auto-generated constructor stub
	}
	public void update(){
		this.move();
		if (Math.random()>1){
			this.tirer();
		}
	}
	public void move(){
		
		int dx = Math.random()>0.5 ? 1:0;
		int dy = Math.random()>0.5 ? 1:0;
		int sx = Math.random()>0.5 ? -1:1;
		int sy = Math.random()>0.5 ? -1:1;
		super.move(sx*dx, sy*dy);
		
		
		
	}
	public void tirer(){
		
		int dx = Math.random()>0.5 ? 1:0;
		int dy = Math.random()>0.5 ? 1:0;
		int sx = Math.random()>0.5 ? -1:1;
		int sy = Math.random()>0.5 ? -1:1;
		
		super.tirer(sx*dx,sy*dy);
		
		
		
	}
	

}
