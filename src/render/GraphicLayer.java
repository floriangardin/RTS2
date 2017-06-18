package render;


import java.awt.image.BufferedImage;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;

import display.Camera;
import ressources.Images;

public class GraphicLayer {

	private Image image;
	private ImageBuffer bi;
	private Graphics gf;
	private int renderMode;
	private boolean translated;
	
	private int resX, resY;
	
	public GraphicLayer(int resX, int resY, int renderMode, boolean translated){
		bi = new ImageBuffer(resX, resY);
		image = new Image(bi);
		this.renderMode = renderMode;
		this.translated = translated;
		try {
			gf = image.getGraphics();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.resX = resX;
		this.resY = resY;
	}
	
	public void mergeToGraphics(Graphics g, Camera camera){
		if(translated){
			gf.translate(camera.Xcam,camera.Ycam);
		}
		gf.flush();
		g.setDrawMode(renderMode);
		if(translated){
			g.drawImage(image.getSubImage(0, 0, camera.resX, camera.resY),0,0);			
		} else {
			g.drawImage(image.getSubImage(camera.Xcam, camera.Ycam, camera.resX, camera.resY),0,0);			
		}
//		g.drawImage(image,0,0);
	}
	
	public Graphics getGraphics(){
		return gf;
	}
	
	public void resetImage(Camera camera){
		gf.clear();
		gf.flush();
		if(translated){
			gf.translate(-camera.Xcam,-camera.Ycam);
		}
	}
	
}
