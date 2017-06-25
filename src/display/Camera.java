package display;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import data.Attributs;
import main.Main;
import plateau.Building;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import utils.Utils;

public class Camera {

	public int Xcam;
	public int Ycam;

	public int resX, resY;

	public int minX, minY, maxX, maxY;

	public boolean slidingCam;
	public int objXcam;
	public int objYcam;

	public Camera(int resX, int resY, int x, int y, int maxX, int maxY){
		this.resX = resX;
		this.resY = resY;
		this.Xcam = x;
		this.Ycam = y;
		this.maxX = maxX;
		this.maxY = maxY;
	}	
	public boolean visibleByCamera(float x, float y, float size){
		return x + size > Xcam && x - size < Xcam + resX && y + size > Ycam && y - size < Ycam + resY;
	}

	public boolean isVisibleByTeam(int team, Objet objet, Plateau plateau) {
		if (objet.getTeam() != null && objet.getTeam().id == team)
			return true;
		float r = Math.max(objet.getAttribut(Attributs.size),objet.getAttribut(Attributs.sizeX))/2;
		for (Character c : plateau.characters)
			if (c.getTeam().id == team && Utils.distance(c, objet) < c.getAttribut(Attributs.sight) + r)
				return true;
		for (Building b : plateau.buildings)
			if (b.getTeam().id == team && Utils.distance(b, objet) < b.getAttribut(Attributs.sight) + r)
				return true;
		return false;
	}


	public void update(InputObject im, boolean rectangleSelection) {
		// Handle the display (camera movement & minimap)
		// camera movement
		if (!rectangleSelection && (!im.isDown(KeyEnum.LeftClick) || im.isOnMiniMap)) {
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
			this.setSliding((int)im.x, (int)im.y);
		}
	}

	public void setSliding(int xObj, int yObj){
		this.slidingCam = true;
		this.objXcam = xObj-resX/2;
		this.objYcam = yObj-resY/2;
	}

}
