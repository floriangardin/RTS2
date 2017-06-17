package display;

import control.InputHandler;
import control.InputObject;
import control.KeyMapper.KeyEnum;
import main.Main;
import model.Game;

public class Camera {

	public static int Xcam;
	public static int Ycam;
	
	public static int minX, minY, maxX, maxY;
	
	public static boolean slidingCam;
	public static int objXcam;
	public static int objYcam;
	
	
	public static void update(InputObject im) {
		// Handle the display (camera movement & minimap)
		int player = Game.gameSystem.getCurrentPlayer().id;
		// camera movement
		if (InputHandler.getSelection(player).rectangleSelection == null && (!im.isDown(KeyEnum.LeftClick) || im.isOnMiniMap)) {
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
			if ((im.isDown(KeyEnum.Up)  || !im.isOnMiniMap && im.y < Ycam + 5) && Ycam > minY-Game.resY / 2) {
				Ycam -= (int) (80 * 30 / Main.framerate);
				slidingCam = false;
			}
			if ((im.isDown(KeyEnum.Down) || (!im.isOnMiniMap && im.y > Ycam + Game.resY - 5))
					&& Ycam < maxY - Game.resY / 2) {
				Ycam += (int) (80 * 30 / Main.framerate);
				slidingCam = false;
			}
			if ((im.isDown(KeyEnum.Left) ||(!im.isOnMiniMap && im.x < Xcam + 5)) && Xcam > minX-Game.resX / 2) {
				Xcam -= (int) (80 * 30 / Main.framerate);
				slidingCam = false;
			}
			if ((im.isDown(KeyEnum.Right) ||(!im.isOnMiniMap && im.x > Xcam + Game.resX - 5))
					&& Xcam < maxX - Game.resX / 2) {
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
	}
	
}
