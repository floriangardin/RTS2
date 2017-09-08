package display;

import java.util.Vector;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import control.Player;
import data.Attributs;
import main.Main;
import model.Game;
import plateau.Building;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import utils.Utils;

public strictfp class Camera {

	public static int Xcam;
	public static int Ycam;

	public static int resX, resY;

	public static int minX, minY, maxX, maxY;

	public static boolean slidingCam;
	public static int objXcam;
	public static int objYcam;

	public static void init(int resX, int resY, int x, int y, int maxX, int maxY){
		Camera.resX = resX;
		Camera.resY = resY;
		Camera.Xcam = x;
		Camera.Ycam = y;
		Camera.maxX = maxX;
		Camera.maxY = maxY;
	}	
	public static boolean visibleByCamera(float x, float y, float size){
		return (x + size)*Game.ratioX > Xcam && (x - size)*Game.ratioX < Xcam + resX 
				&& (y + size)*Game.ratioY > Ycam && (y - size)*Game.ratioY < Ycam + resY;
	}

	
	public static int getCenterX(){
		return Xcam + resX/2;
	}
	public static int getCenterY(){
		return Ycam + resY/2;
	}

	public static void update(final InputObject im) {
		// Handle the display (camera movement & minimap)
		// camera movement
		
		if (!Player.hasRectangleSelection() && (!im.isDown(KeyEnum.LeftClick) || im.isOnMiniMap)) {
			// Handling sliding
			if(slidingCam==true){
				int deltaX = (int) (objXcam-Xcam);
				int deltaY = (int) (objYcam-Ycam);
				Xcam += deltaX/5;
				Ycam += deltaY/5;
				if(StrictMath.abs(deltaX)<2)
					slidingCam = false;
			}
			// Move camera according to inputs :
			if ((im.isDown(KeyEnum.Up)  || !im.isOnMiniMap && im.yOnScreen < 5) && Ycam > minY-resY / 2) {
				Ycam -= (int) (80 * 30 / Main.framerate);
				slidingCam = false;
			}
			if ((im.isDown(KeyEnum.Down) || (!im.isOnMiniMap && im.yOnScreen > resY - 5))
					&& Ycam < maxY - resY / 2) {
				Ycam += (int) (80 * 30 / Main.framerate);
				slidingCam = false;
			}
			if ((im.isDown(KeyEnum.Left) ||(!im.isOnMiniMap && im.xOnScreen < 5)) && Xcam > minX-resX / 2) {
				Xcam -= (int) (80 * 30 / Main.framerate);
				slidingCam = false;
			}
			if ((im.isDown(KeyEnum.Right) ||(!im.isOnMiniMap && im.xOnScreen > resX - 5))
					&& Xcam < maxX - resX / 2) {
				Xcam += (int) (80 * 30 / Main.framerate);
				slidingCam = false;
			}
		}
		if(im.isOnMiniMap && im.isPressed(KeyEnum.LeftClick)){
			setSliding((int)(im.x*Game.resX/1920), (int)(im.y*Game.resY/1080));
		}
	}

	public static void setSliding(int xObj, int yObj){
		slidingCam = true;
		objXcam = xObj-resX/2;
		objYcam = yObj-resY/2;
	}
	public static void reset() {
		Camera.resX = 0;
		Camera.resY = 0;
		Camera.Xcam = 0;
		Camera.Ycam = 0;
		Camera.maxX = 0;
		Camera.maxY = 0;
	}	

}
