package render;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import data.Attributs;
import display.Camera;
import model.Game;
import plateau.Building;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import ressources.Images;
import utils.Utils;

public class RenderEngine {

	public static Vector<GraphicLayer> layers ;
	
	public static final int FOGOFWARLAYER = 3;
	public static final int BACKGROUNDLAYER = 0;
	public static final int NORMALLAYER = 2;

	public static void init(Camera camera){
		layers = new Vector<GraphicLayer>();
		for(int i =0; i<5; i++){
			layers.add(new GraphicLayer(camera.resX, camera.resY, i==FOGOFWARLAYER ? Graphics.MODE_NORMAL : Graphics.MODE_COLOR_MULTIPLY));
		}
		
	}
	public static void render(Graphics g, Plateau plateau, Camera camera){
		g.setColor(Color.black);
		g.translate(-camera.Xcam,-camera.Ycam);
		
		for(GraphicLayer layer : layers){
			layer.resetImage();
		}
		
		Vector<Objet> objets = new Vector<Objet>();
		for(Objet o : plateau.objets.values()){
			objets.add(o);
		}
		objets = Utils.triY(objets);
		Vector<Objet> visibleObjets = new Vector<Objet>();
		for(Objet o : objets){
			if(camera.visibleByCamera(o.x, o.y, o.getAttribut(Attributs.sight))){
				visibleObjets.add(o);
			}
		}
		for(Objet o : objets){
			renderObjet(o, layers, plateau);
		}
		renderDomain(plateau, layers, camera, visibleObjets);
		for(GraphicLayer layer : layers){
			layer.mergeToGraphics(g);
		}
		
	}
	
	public static void renderDomain(Plateau plateau, Vector<GraphicLayer> gl, Camera camera, Vector<Objet> visibleObjets){
		// draw background
		gl.get(BACKGROUNDLAYER).getGraphics().drawImage(Images.get("seaBackground"), -plateau.maxX, -plateau.maxY,
				2*plateau.maxX, 2*plateau.maxY, 0, 0, Images.get("seaBackground").getWidth(),Images.get("seaBackground").getHeight());
		gl.get(BACKGROUNDLAYER).getGraphics().drawImage(Images.get("islandTexture"),0, 0, plateau.maxX, plateau.maxY,
				0, 0, Images.get("islandTexture").getWidth(),  Images.get("islandTexture").getHeight());
		
		// draw fog of war
		Graphics gf = gl.get(FOGOFWARLAYER).getGraphics();

		gf.setColor(new Color(255, 255, 255));
		gf.fillRect(-plateau.maxX, -plateau.maxY, plateau.maxX + camera.resX, plateau.maxY + camera.resX);
		gf.setColor(new Color(50, 50, 50));
		float xmin = Math.max(-plateau.maxX, -plateau.maxX - camera.Xcam);
		float ymin = Math.max(-plateau.maxY, -plateau.maxY - camera.Ycam);
		float xmax = Math.min(camera.resX + plateau.maxX, 2 * plateau.maxX - camera.Xcam);
		float ymax = Math.min(camera.resY + plateau.maxY, 2 * plateau.maxY - camera.Ycam);
		gf.fillRect(xmin, ymin, xmax - xmin, ymax - ymin);
		gf.setColor(Color.white);
		for (Objet o : visibleObjets) {
			float sight = o.getAttribut(Attributs.sight);
			gf.fillOval(o.x - camera.Xcam - sight, o.y - camera.Ycam - sight, sight * 2f, sight * 2f);
		}
	}
	
	public static void renderObjet(Objet o, Vector<GraphicLayer> gl, Plateau plateau){
		if(o instanceof Character){
			RenderCharacter.render((Character)o, gl, plateau);
		} else if(o instanceof Building){
			RenderBuilding.render((Building)o, gl, plateau);
		}
	}


//	// g repr�sente le pinceau
//	//g.setColor(Color.black);
//	g.translate(-camera.Xcam,-camera.Ycam);
//	//Draw background
//
//	// Draw ground effects
//	for(SpellEffect e : plateau.spells){
//		if(e.visibleByCurrentTeam && e.toDrawOnGround)
//			e.draw(g);
//	}
//	// Draw the selection of your team
//	for(Objet o: this.inputsHandler.getSelection(currentPlayer.id).selection){
//
//		if(o.getTarget()!=null && o instanceof Checkpoint){
//			Checkpoint c = (Checkpoint) o.getTarget();
//			c.toDraw = true;
//		}
//		o.drawIsSelected(g);
//		if(Game.debugGroup){
//			if(o instanceof Character && ((Character) o).getGroup()!=null){
//				for(Character c : ((Character)o).getGroup()){
//					g.setColor(Color.white);
//					g.fillRect(c.getX()-50f, c.getY()-50f, 100f, 100f);
//				}
//			}
//		}
//	}
//	//Draw spells cosmetic
//	if(this.spellCurrent!=null){
//		int i = spellLauncher.getSpellsName().indexOf(spellCurrent);
//		boolean ok = spellLauncher.spellsState.get(i)>=Game.g.data.spells.get(spellCurrent).getAttribut(Attributs.chargeTime);
//		this.currentPlayer.getGameTeam().data.spells.get(this.spellCurrent).draw(g,this.spellTarget,this.spellX,this.spellY,this.spellLauncher, ok);
//	}
//
//	//Creation of the drawing Vector
//	Vector<Objet> toDraw = new Vector<Objet>();
//	Vector<Objet> toDrawAfter = new Vector<Objet>();
//	// Draw the Action Objets
//	for(Character o : plateau.characters){
//		//o.draw(g);
//		if(o.visibleByCurrentTeam || debugFog || marcoPolo)
//			toDrawAfter.add(o);
//
//	}
//
//	//
//
//	//Draw bonuses
//	for(Bonus o : plateau.bonus){
//		//o.draw(g);
//		o.draw(g);
//	}
//	// Draw the natural Objets
//
//	for(NaturalObjet o : plateau.naturalObjets){
//		if(o.visibleByCurrentTeam || debugFog || marcoPolo)
//			toDrawAfter.add(o);
//		else
//			toDraw.add(o);
//	}
//	// Draw the buildings
//	for(Building e : plateau.buildings){
//		if(e.visibleByCurrentTeam || marcoPolo)
//			toDrawAfter.add(e);
//		else
//			toDraw.add(e);
//	}
//	for(SpellEffect e : plateau.spells){
//		if(!e.toDrawOnGround){
//			if(e.visibleByCurrentTeam || debugFog || marcoPolo)
//				toDrawAfter.add(e);
//			else
//				toDraw.add(e);
//		}
//	}
//	for(Bullet b : plateau.bullets){
//		if(b.visibleByCurrentTeam || debugFog || marcoPolo)
//			toDrawAfter.add(b);
//		else
//			toDraw.add(b);
//	}
//
//
//	Utils.triY(toDraw);
//	Utils.triY(toDrawAfter);
//	// determine visible objets
//	for(Objet o: toDraw)
//		o.draw(g);
//	// draw fog of war
//	if(!debugFog && !Game.g.marcoPolo){
//		drawFogOfWar(g);
//	}
//
//	for(Objet o: toDrawAfter){
//		o.draw(g);
//		if(o instanceof Character && this.inputsHandler.getSelection(currentPlayer.id).selection.contains(o)){
//			((Character)o).drawLifePoints(g, 60f*Main.ratioSpace);
//		}
//	}
//
//
//	//handleDrawUnderBuilding(g);
//
//	// Draw the selection :
//	if(cosmetic.selection!=null){
//		g.setColor(Colors.selection);
//		cosmetic.draw(g);
//	}
//	// Render Graphics Events
//	events.render(g);
//
//	// Draw and handle display ressources
//	Vector<DisplayRessources> toRemove = new Vector<DisplayRessources>();
//	for(DisplayRessources dr : this.displayRessources ){
//		dr.update();
//		if(dr.isDead())
//			toRemove.add(dr);
//		else
//			dr.draw(g);
//	}
//	this.displayRessources.removeAll(toRemove);
//	toRemove.clear();




}
