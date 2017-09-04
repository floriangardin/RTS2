package render;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import control.Player;
import data.Attributs;
import display.Camera;
import display.Interface;
import events.EventHandler;
import model.Game;
import multiplaying.ChatHandler;
import pathfinding.Case;
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

	private static boolean isReady = false;
	private static boolean hasChecked = false;
	
	public static float xmouse, ymouse;

	public static int i;
	
	public static boolean isReady(){
		if(!hasChecked){
			checkIfReady();
		}
		return isReady;
	}
	
	private static void checkIfReady(){
		boolean b = true;
		b = b && Images.isInitialized();
		isReady = b;
		hasChecked = true;
	}

	public static void render(Graphics g, Plateau plateau){

		g.translate(-Camera.Xcam, -Camera.Ycam);
		g.scale(Game.resX/1920f, Game.resY/1080f);
		//g.scale(0.5f,0.5f);
		// Draw background
		renderBackground(g, plateau);

		// Draw first layer of event
		EventHandler.render(g, plateau, false);
		
		Vector<Objet> objets = new Vector<Objet>();
		for(Objet o : plateau.getObjets().values()){
			objets.add(o);
		}
		objets = Utils.triY(objets);
		Vector<Objet> visibleObjets = new Vector<Objet>();
		for(Objet o : objets){
			if(Camera.visibleByCamera(o.x, o.y, o.getAttribut(Attributs.sight)) && o.team.id==Player.getTeamId()){
				visibleObjets.add(o);
			}
		}
		// 1) Draw selection
		for(Integer o: Player.selection){
			renderSelection(plateau.getById(o), g, plateau);
		}
		
		// 2) Draw Neutral Objects
		for(Objet o : objets){
			if(Camera.visibleByCamera(o.x, o.y, Math.max(o.getAttribut(Attributs.size),o.getAttribut(Attributs.sizeX)))){
				if (o instanceof Building || o instanceof NaturalObjet){
					if(o.getTeam().id!=Player.getTeamId() && !plateau.isVisibleByTeam(Player.getTeamId(), o)){
						renderObjet(o, g, plateau, false);
					}
				}
			}
		}
		// Draw fog of war
		renderDomain(plateau, g, visibleObjets);
		// 2) Draw Objects
		for(Objet o : objets){
			if(Camera.visibleByCamera(o.x, o.y, Math.max(o.getAttribut(Attributs.size),o.getAttribut(Attributs.sizeX)))){
				if(o.getTeam().id==Player.getTeamId() || plateau.isVisibleByTeam(Player.getTeamId(), o)){
					renderObjet(o, g, plateau);
				}
			}
		}
		// Draw second layer of event
		EventHandler.render(g, plateau, true);
//		// draw mapgrid
//		for(Case c : plateau.mapGrid.idcases.values()){
//			g.setLineWidth(1);
//			g.setColor(c.ok ? new Color(100,100,100,25) : new Color(255,0,0,100));
//			g.fillRect(c.x, c.y, c.sizeX, c.sizeY);
//			g.setColor(Color.darkGray);
//			g.drawRect(c.x, c.y, c.sizeX, c.sizeY);
//			
//		}
		// Draw interface
		g.scale(1920f/Game.resX, 1080f/Game.resY);
		g.translate(Camera.Xcam, Camera.Ycam);
		Interface.draw(g, plateau);
		ChatHandler.draw(g);
	}
	
	

	public static void renderBackground(Graphics g, Plateau plateau){
		g.drawImage(Images.get("islandTexture"),0, 0, plateau.maxX, plateau.maxY,
				0, 0, Images.get("islandTexture").getWidth(),  Images.get("islandTexture").getHeight());

	}
	public static void renderDomain(Plateau plateau, Graphics g, Vector<Objet> visibleObjets){
		// draw fog of war
		Graphics g1 = GraphicElements.graphicFogOfWar;
		g1.setColor(new Color(255, 255, 255));
		g1.fillRect(0, 0, Camera.resX, Camera.resX);
		g1.setColor(new Color(50, 50, 50));
		float xmin = Math.max(0, -Camera.Xcam);
		float ymin = Math.max(0, -Camera.Ycam);
		float xmax = Math.min(Camera.resX, plateau.maxX*Game.ratioX - Camera.Xcam);
		float ymax = Math.min(Camera.resY, plateau.maxY*Game.ratioY - Camera.Ycam);
		g1.fillRect(xmin, ymin, xmax - xmin, ymax - ymin);
		g1.setColor(new Color(255, 255, 255));
		for (Objet o : visibleObjets) {
			float sight = o.getAttribut(Attributs.sight);
			if(sight>5){
				g1.fillOval((o.x - sight)*Game.ratioX - Camera.Xcam, (o.y - sight)*Game.ratioY - Camera.Ycam, sight * 2f * Game.ratioX, sight * 2f * Game.ratioY);
			}
		}
		g1.flush();
		g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
		g.scale(1920f/Game.resX, 1080f/Game.resY);
		g.translate(Camera.Xcam, Camera.Ycam);
		g.drawImage(GraphicElements.imageFogOfWar,0,0);
		g.translate(-Camera.Xcam, -Camera.Ycam);
		g.scale(Game.resX/1920f, Game.resY/1080f);

		// draw rectangle of selection
		g.setDrawMode(Graphics.MODE_NORMAL);
		if(Player.rectangleSelection != null){
			g.setLineWidth(1f);
			g.setColor(Color.green);
			g.drawRect(Player.rectangleSelection.getMinX(),
					Player.rectangleSelection.getMinY(),
					Player.rectangleSelection.getWidth(),
					Player.rectangleSelection.getHeight());	
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
	//	g.translate(-Camera.Xcam,-Camera.Ycam);
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
