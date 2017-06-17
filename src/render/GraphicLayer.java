package render;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GraphicLayer {

	private Image image;
	private Graphics gf;
	private int renderMode;
	
	public GraphicLayer(int resX, int resY, int renderMode){
		try {
			image = new Image((int) (resX), (int) (resY));
			gf = image.getGraphics();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void mergeToGraphics(Graphics g){
		gf.flush();
		g.setDrawMode(renderMode);
		g.drawImage(image,0,0);
	}
	
	public Graphics getGraphics(){
		return gf;
	}
	
	public void resetImage(){
		gf.setColor(new Color(0, 0, 0));
		gf.fillRect(0, 0, image.getWidth(), image.getHeight());
	}
	
}
