package render;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import data.Attributs;
import display.Camera;
import display.Interface;
import model.Player;
import plateau.Building;
import plateau.Bullet;
import plateau.Character;
import plateau.NaturalObjet;
import plateau.Objet;
import plateau.Plateau;
import ressources.GraphicElements;
import ressources.Images;
import utils.Utils;

public class RenderEngine {

	public static final int FOGOFWARLAYER = 3;
	public static final int BACKGROUNDLAYER = 0;
	public static final int SELECTIONLAYER = 4;
	public static final int INTERFACELAYER = 5;
	public static final int NORMALLAYER = 2;
	public static final int GROUNDLAYER = 1;

	public static int i;

	public static void init(Plateau plateau){

	}
	public static void render(Graphics g, Plateau plateau, Camera camera, Player player, Interface bottombar){

		g.translate(-camera.Xcam, -camera.Ycam);
		renderBackground(g, plateau);
		Vector<Objet> objets = new Vector<Objet>();
		for(Objet o : plateau.objets.values()){
			objets.add(o);
		}
		objets = Utils.triY(objets);
		Vector<Objet> visibleObjets = new Vector<Objet>();
		for(Objet o : objets){
			if(camera.visibleByCamera(o.x, o.y, o.getAttribut(Attributs.sight)) && o.team==player.getGameTeam()){
				visibleObjets.add(o);
			}
		}
		// 1) Draw selection
		for(Objet o: player.selection.selection){
			renderSelection(o, g, plateau);
		}
		// 2) Draw Objects
		for(Objet o : objets){
			if(camera.visibleByCamera(o.x, o.y, Math.max(o.getAttribut(Attributs.size),o.getAttribut(Attributs.sizeX)))){
				if(o.getTeam().id==player.getTeam()){
					renderObjet(o, g, plateau);
				} else if (plateau.isVisibleByTeam(player.getTeam(), o)){
					renderObjet(o, g, plateau);
				} else if (o instanceof Building || o instanceof NaturalObjet){
					renderObjet(o, g, plateau, false);
				}
			}
		}
		// 3) Draw fog of war
		renderDomain(plateau, g, camera, visibleObjets, player, bottombar);
		g.translate(camera.Xcam, camera.Ycam);
		// draw interface
		// 4) Draw bottom bar
		bottombar.draw(g, camera);
	}

	public static void renderBackground(Graphics g, Plateau plateau){
		g.drawImage(Images.get("islandTexture"),0, 0, plateau.maxX, plateau.maxY,
				0, 0, Images.get("islandTexture").getWidth(),  Images.get("islandTexture").getHeight());

	}
	public static void renderDomain(Plateau plateau, Graphics g, Camera camera, Vector<Objet> visibleObjets, Player player, Interface bottombar){
		// draw fog of war
		Graphics g1 = GraphicElements.graphicFogOfWar;
		g1.setColor(new Color(255, 255, 255));
		g1.fillRect(0, 0, camera.resX, camera.resX);
		g1.setColor(new Color(50, 50, 50));
		float xmin = Math.max(0, -camera.Xcam);
		float ymin = Math.max(0, -camera.Ycam);
		float xmax = Math.min(camera.resX, plateau.maxX - camera.Xcam);
		float ymax = Math.min(camera.resY, plateau.maxY - camera.Ycam);
		g1.fillRect(xmin, ymin, xmax - xmin, ymax - ymin);
		g1.setColor(new Color(255, 255, 255));
		for (Objet o : visibleObjets) {
			float sight = o.getAttribut(Attributs.sight);
			if(sight>5){
				g1.fillOval(o.x - sight - camera.Xcam, o.y - sight - camera.Ycam, sight * 2f, sight * 2f);
			}
		}
		g1.flush();
		g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
		g.translate(camera.Xcam, camera.Ycam);
		g.drawImage(GraphicElements.imageFogOfWar,0,0);
		g.translate(-camera.Xcam, -camera.Ycam);

		// draw rectangle of selection
		g.setDrawMode(Graphics.MODE_NORMAL);
		if(player.selection.rectangleSelection != null){
			g.setLineWidth(1f);
			g.setColor(Color.green);
			g.drawRect(player.selection.rectangleSelection.getMinX(),
					player.selection.rectangleSelection.getMinY(),
					player.selection.rectangleSelection.getWidth(),
					player.selection.rectangleSelection.getHeight());	
		}


	}

	public static void renderObjet(Objet o, Graphics g, Plateau plateau){
		if(o instanceof Character){
			RenderCharacter.render((Character)o, g, plateau);
		} else if(o instanceof Building){
			RenderBuilding.render((Building)o, g, plateau);
		} else if(o instanceof NaturalObjet){
			RenderNaturalObjet.render((NaturalObjet) o, g, plateau);
		} else if(o instanceof Bullet){
			RenderBullet.render((Bullet) o, g, plateau);
		}
	}
	
	public static void renderObjet(Objet o, Graphics g, Plateau plateau, boolean visible){
		if(o instanceof Building){
			RenderBuilding.render((Building)o, g, plateau, false, false);
		} else if(o instanceof NaturalObjet){
			RenderNaturalObjet.render((NaturalObjet) o, g, plateau);
		} 
	}

	public static void renderSelection(Objet o, Graphics g, Plateau plateau){
		if(o instanceof Character){
			RenderCharacter.renderSelection(g,(Character)o,  plateau);
		} else if(o instanceof Building){
			RenderBuilding.renderSelection(g, (Building)o, plateau);
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
