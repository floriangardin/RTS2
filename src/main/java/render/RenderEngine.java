package render;

import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import control.Player;
import data.Attributs;
import display.Camera;
import display.Interface;
import events.EventHandler;
import model.Game;
import multiplaying.ChatHandler;
import pathfinding.Case;
import pathfinding.Case.IdTerrain;
import plateau.Building;
import plateau.Bullet;
import plateau.Character;
import plateau.NaturalObjet;
import plateau.Objet;
import plateau.Plateau;
import ressources.GraphicElements;
import ressources.Images;
import ressources.Map;
import utils.Utils;

public class RenderEngine {

	private static boolean isReady = false;
	private static boolean hasChecked = false;

	public static float xmouse, ymouse;


	// Draw Background
	public static Image imageBackgroundNeutral;
	public static Graphics graphicBackgroundNeutral;
	public static Image imageBackground;
	public static Graphics graphicBackground;
	
	// Waves
	public static Vector<Wave> waves;

	public static int i;

	public static boolean isReady(){
		if(!hasChecked){
			checkIfReady();
			waves = new Vector<Wave>();
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

	public static void initBackground(Plateau plateau){

		int sizeTile = 120;
		Image im = Images.get("grasstile");
		HashMap<String, Image> temp = new HashMap<String, Image>();
		try {
			imageBackground = new Image(plateau.maxX*2,plateau.maxY*2);
			graphicBackground = imageBackground.getGraphics();
			imageBackgroundNeutral = new Image(plateau.maxX*2,plateau.maxY*2);
			graphicBackgroundNeutral = imageBackgroundNeutral.getGraphics();

		} catch (SlickException e) {}
		for(IdTerrain it : IdTerrain.getSortedIT()){
			boolean b4, b7, b8, b9, b6, b3, b2, b1;
			Case ctemp;
			temp.clear();
			im = Images.get(it.name()+"border");
			temp.put("1", im.getSubImage(sizeTile*0, sizeTile*2, sizeTile, sizeTile));
			temp.put("2", im.getSubImage(sizeTile*1, sizeTile*2, sizeTile, sizeTile));
			temp.put("3", im.getSubImage(sizeTile*2, sizeTile*2, sizeTile, sizeTile));
			temp.put("4", im.getSubImage(sizeTile*0, sizeTile*1, sizeTile, sizeTile));
			temp.put("6", im.getSubImage(sizeTile*2, sizeTile*1, sizeTile, sizeTile));
			temp.put("7", im.getSubImage(sizeTile*0, sizeTile*0, sizeTile, sizeTile));
			temp.put("8", im.getSubImage(sizeTile*1, sizeTile*0, sizeTile, sizeTile));
			temp.put("9", im.getSubImage(sizeTile*2, sizeTile*0, sizeTile, sizeTile));
			temp.put("11", im.getSubImage(sizeTile*4, sizeTile*0, sizeTile, sizeTile));
			temp.put("13", im.getSubImage(sizeTile*3, sizeTile*0, sizeTile, sizeTile));
			temp.put("17", im.getSubImage(sizeTile*4, sizeTile*1, sizeTile, sizeTile));
			temp.put("19", im.getSubImage(sizeTile*3, sizeTile*1, sizeTile, sizeTile));
			for(Case c : plateau.mapGrid.idcases.values()){
				if(c.getIdTerrain()!=it){
					continue;
				}
				String s = c.getIdTerrain().name().toLowerCase()+"tile";
				i = 0;
				while(Math.random()>0.8){
					if(Images.exists(s+(i+1))){
						i+=1;
					} else {
						break;
					}
				}
				if(it != IdTerrain.WATER){
					graphicBackgroundNeutral.drawImage(Images.get(s+i).getScaledCopy((int)c.sizeX,(int)c.sizeY), plateau.maxX/2+c.x,plateau.maxY/2+c.y);
				}
				ctemp = plateau.mapGrid.getCase(c.x-10, c.y+10);
				b4 = ctemp!=null && ctemp.getIdTerrain()!= c.getIdTerrain();
				ctemp = plateau.mapGrid.getCase(c.x-10, c.y-10);
				b7 = ctemp!=null && ctemp.getIdTerrain()!= c.getIdTerrain();
				ctemp = plateau.mapGrid.getCase(c.x+10, c.y-10);
				b8 = ctemp!=null && ctemp.getIdTerrain()!= c.getIdTerrain();
				ctemp = plateau.mapGrid.getCase(c.x+10+c.sizeX, c.y-10);
				b9 = ctemp!=null && ctemp.getIdTerrain()!= c.getIdTerrain();
				ctemp = plateau.mapGrid.getCase(c.x+10+c.sizeX, c.y+10);
				b6 = ctemp!=null && ctemp.getIdTerrain()!= c.getIdTerrain();
				ctemp = plateau.mapGrid.getCase(c.x+10+c.sizeX, c.y+10+c.sizeY);
				b3 = ctemp!=null && ctemp.getIdTerrain()!= c.getIdTerrain();
				ctemp = plateau.mapGrid.getCase(c.x+10, c.y+10+c.sizeY);
				b2 = ctemp!=null && ctemp.getIdTerrain()!= c.getIdTerrain();
				ctemp = plateau.mapGrid.getCase(c.x-10, c.y+10+c.sizeY);
				b1 = ctemp!=null && ctemp.getIdTerrain()!= c.getIdTerrain();
				if(b1||b2||b3||b4||b6||b7||b8||b9){
					if(b4){
						if(b6){
							if(b8){
								if(b2){
									graphicBackgroundNeutral.drawImage(temp.get("7").getSubImage(0, 0, sizeTile/2, sizeTile/2), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
									graphicBackgroundNeutral.drawImage(temp.get("9").getSubImage(sizeTile/2, 0, sizeTile/2, sizeTile/2), plateau.maxX/2+c.x+c.sizeX/2,plateau.maxY/2+c.y-10);
									graphicBackgroundNeutral.drawImage(temp.get("1").getSubImage(0, sizeTile/2, sizeTile/2, sizeTile/2), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y+c.sizeY/2);
									graphicBackgroundNeutral.drawImage(temp.get("3").getSubImage(sizeTile/2, sizeTile/2, sizeTile/2, sizeTile/2), plateau.maxX/2+c.x+c.sizeX/2,plateau.maxY/2+c.y+c.sizeY/2);
								} else {
									graphicBackgroundNeutral.drawImage(temp.get("7").getSubImage(0, 0, sizeTile/2, sizeTile), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
									graphicBackgroundNeutral.drawImage(temp.get("9").getSubImage(sizeTile/2, 0, sizeTile, sizeTile), plateau.maxX/2+c.x+c.sizeX/2,plateau.maxY/2+c.y-10);
								}
							} else {
								if(b2){
									graphicBackgroundNeutral.drawImage(temp.get("1").getSubImage(0, 0, sizeTile/2, sizeTile), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
									graphicBackgroundNeutral.drawImage(temp.get("3").getSubImage(sizeTile/2, 0, sizeTile/2, sizeTile), plateau.maxX/2+c.x+c.sizeX/2,plateau.maxY/2+c.y-10);
								} else {
									graphicBackgroundNeutral.drawImage(temp.get("4").getSubImage(0, 0, sizeTile/2, sizeTile), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
									graphicBackgroundNeutral.drawImage(temp.get("6").getSubImage(sizeTile/2, 0, sizeTile/2, sizeTile), plateau.maxX/2+c.x+c.sizeX/2,plateau.maxY/2+c.y-10);
								}
							}
						} else {
							if(b8){
								if(b2){
									graphicBackgroundNeutral.drawImage(temp.get("7").getSubImage(0, 0, sizeTile, sizeTile/2), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
									graphicBackgroundNeutral.drawImage(temp.get("1").getSubImage(0, sizeTile/2, sizeTile, sizeTile/2), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y+c.sizeY/2);
								} else {
									graphicBackgroundNeutral.drawImage(temp.get("7"), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
								}
							} else {
								if(b2){
									graphicBackgroundNeutral.drawImage(temp.get("1"), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
								} else {
									graphicBackgroundNeutral.drawImage(temp.get("4"), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
								}
							}
						}
					} else if(b6){
						if(b8){
							if(b2){
								graphicBackgroundNeutral.drawImage(temp.get("9").getSubImage(0, 0, sizeTile, sizeTile/2), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
								graphicBackgroundNeutral.drawImage(temp.get("3").getSubImage(0, sizeTile/2, sizeTile, sizeTile/2), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y+c.sizeY/2);
							} else {
								graphicBackgroundNeutral.drawImage(temp.get("9"), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
							}
						} else {
							if(b2){
								graphicBackgroundNeutral.drawImage(temp.get("3"), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
							} else {
								graphicBackgroundNeutral.drawImage(temp.get("6"), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
							}
						}
					} else {
						if(b8){
							if(b2){
								graphicBackgroundNeutral.drawImage(temp.get("8").getSubImage(0, 0, sizeTile, sizeTile/2), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
								graphicBackgroundNeutral.drawImage(temp.get("2").getSubImage(0, sizeTile/2, sizeTile, sizeTile/2), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y+c.sizeY/2);
							} else {
								graphicBackgroundNeutral.drawImage(temp.get("8"), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
							}
						} else {
							if(b2){
								graphicBackgroundNeutral.drawImage(temp.get("2"), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
							}
						}
					}
					if(!b4 && ! b8 && b7){
						graphicBackgroundNeutral.drawImage(temp.get("17"), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
					}
					if(!b8 && !b6 && b9){
						graphicBackgroundNeutral.drawImage(temp.get("19"), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
					}
					if(!b6 && ! b2 && b3){
						graphicBackgroundNeutral.drawImage(temp.get("13"), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
					}
					if(!b2 && !b4 && b1){
						graphicBackgroundNeutral.drawImage(temp.get("11"), plateau.maxX/2+c.x-10,plateau.maxY/2+c.y-10);
					}
				}
			}
		}
		graphicBackgroundNeutral.flush();
		graphicBackground.drawImage(imageBackgroundNeutral,0,0);
		graphicBackground.flush();
	}


	public static void renderBackground(Graphics g, Plateau plateau){
		if(imageBackground==null){
			initBackground(plateau);
		}
		g.drawImage(Images.get("watertile0"),-plateau.maxX/2,-plateau.maxY/2,plateau.maxX*2,plateau.maxY*2,0,0,100,100);
		// Handling waves
		float x, y;
		Case c;
		boolean b;
		for(int i=0; i<2; i++){
			do{
				b = false;
				x = (float) (Math.random()*2*plateau.maxX-plateau.maxX/2);
				y = (float) (Math.random()*2*plateau.maxY-plateau.maxY/2);
				c = plateau.mapGrid.getCase(x-50f, y);
				b = b || (c!=null && c.getIdTerrain()!=IdTerrain.WATER);
				c = plateau.mapGrid.getCase(x, y);
				b = b || (c!=null && c.getIdTerrain()!=IdTerrain.WATER);
				c = plateau.mapGrid.getCase(x+100f, y);
				b = b || (c!=null && c.getIdTerrain()!=IdTerrain.WATER);
				c = plateau.mapGrid.getCase(x+150f, y);
				b = b || (c!=null && c.getIdTerrain()!=IdTerrain.WATER);
			} while(b);
			waves.add(new Wave(x,y));
		}
		Vector<Wave> toRemove = new Vector<Wave>();
		for(Wave w : waves){
			if(!w.play(g, plateau)){
				toRemove.add(w);
			}
		}
		waves.removeAll(toRemove);
		g.drawImage(imageBackground,-plateau.maxX/2, -plateau.maxY/2);

	}
	public static void renderDomain(Plateau plateau, Graphics g, Vector<Objet> visibleObjets){
		// draw fog of war
		Graphics g1 = GraphicElements.graphicFogOfWar;
		g1.setColor(new Color(255, 255, 255));
		g1.fillRect(0, 0, Camera.resX, Camera.resX);
		g1.setColor(new Color(50, 50, 50));
//		float xmin = Math.max(0, -Camera.Xcam);
//		float ymin = Math.max(0, -Camera.Ycam);
//		float xmax = Math.min(Camera.resX, plateau.maxX*Game.ratioX - Camera.Xcam);
//		float ymax = Math.min(Camera.resY, plateau.maxY*Game.ratioY - Camera.Ycam);
		float xmin = 0;
		float xmax = Camera.resX;
		float ymin = 0;
		float ymax = Camera.resY;
		g1.fillRect(xmin, ymin, xmax - xmin, ymax - ymin);
		g1.setColor(new Color(255, 255, 255));
		for (Objet o : visibleObjets) {
			float sight = o.getAttribut(Attributs.sight);
			if(sight>5){
				g1.fillOval((o.x - sight)*Game.ratioX - Camera.Xcam, (o.y - sight)*Game.ratioY - Camera.Ycam, sight * 2f * Game.ratioX, sight * 2f * Game.ratioY);
			}
		}
		g1.flush();
		g.scale(1920f/Game.resX, 1080f/Game.resY);
		g.translate(Camera.Xcam, Camera.Ycam);
		g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
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
