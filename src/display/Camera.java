package display;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import control.Player;
import data.Attributs;
import main.Main;
import plateau.Building;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import utils.Utils;

public class Camera {

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
		return x + size > Xcam && x - size < Xcam + resX && y + size > Ycam && y - size < Ycam + resY;
	}




	public static void update(InputObject im) {
		// Handle the display (camera movement & minimap)
		// camera movement
		if (!Player.hasRectangleSelection() && (!im.isDown(KeyEnum.LeftClick) || im.isOnMiniMap)) {
			// Handling sliding
			if(slidingCam==true){
				int deltaX = (int) (objXcam-Xcam);
				int deltaY = (int) (objYcam-Ycam);
				Xcam += deltaX/5;
				Ycam += deltaY/5;
				if(Math.abs(deltaX)<2)
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

			// TODO : Centrer la selection sur un groupe d'unité
			//			for (int to = 0; to < 10; to++) {
			//				if (im.isPressed(KeyEnum.valueOf("Key"+to)) && players.get(player).groupSelection == to
			//						&& this.selection.get(player).size() > 0) {
			//					float xmoy = this.selection.get(player).get(0).getX();
			//					float ymoy = this.selection.get(player).get(0).getY();
			//					this.Xcam = (int) Math.min(maxX - g.resX / 2f, Math.max(-g.resX / 2f, xmoy - g.resX / 2f));
			//					this.Ycam = (int) Math.min(maxY - g.resY / 2f, Math.max(-g.resY / 2f, ymoy - g.resY / 2f));
			//				}
			//			}
		}
		if(im.isOnMiniMap && im.isPressed(KeyEnum.LeftClick)){
			setSliding((int)im.x, (int)im.y);
		}
	}

	public static void setSliding(int xObj, int yObj){
		slidingCam = true;
		objXcam = xObj-resX/2;
		objYcam = yObj-resY/2;
	}

}
