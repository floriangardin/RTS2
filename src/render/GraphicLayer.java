package render;


import java.awt.image.BufferedImage;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;

import ressources.Images;

public class GraphicLayer {

	private Image image;
	private ImageBuffer bi;
	private Graphics gf;
	private int renderMode;
	
	private int resX, resY;
	
	public GraphicLayer(int resX, int resY, int renderMode){
		bi = new ImageBuffer(resX, resY);
		image = new Image(bi);
		this.renderMode = renderMode;
		try {
			gf = image.getGraphics();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.resX = resX;
		this.resY = resY;
	}
	
	public void mergeToGraphics(Graphics g){
		gf.flush();
		g.setDrawMode(renderMode);
		g.drawImage(image,0,0);
//		g.drawImage(image,0,0);
	}
	
	public Graphics getGraphics(){
		return gf;
	}
	
	public void resetImage(){
		gf.clear();
		gf.flush();
	}
	
}
