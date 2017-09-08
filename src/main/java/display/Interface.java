package display;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import bonus.Bonus;
import control.InputObject;
import control.KeyMapper.KeyEnum;
import control.Player;
import data.Attributs;
import main.Main;
import model.Colors;
import model.Game;
import pathfinding.Case;
import plateau.Building;
import plateau.Character;
import plateau.NaturalObjet;
import plateau.Objet;
import plateau.Plateau;
import plateau.Spell;
import plateau.Team;
import ressources.GraphicElements;
import ressources.Images;
import system.Debug;
import utils.ObjetsList;
import utils.Utils;

public strictfp class Interface {

	public static float ratioMinimapX = 1/6f;
	public static float ratioSelectionX = 1/8f;
	public static float ratioSpellX = 1/12f;
	public static float ratioBarVertX = 1/32f;

	public static float nbRoundInit = 3*Main.framerate;
	public static float debut = nbRoundInit/4, duree = debut;

	// selection bar
	public static float startXSelectionBar = 0;
	public static float startYSelectionBar = Game.resY-Game.resX*ratioSelectionX;
	public static float sizeXSelectionBar = Game.resX*ratioSelectionX;
	public static float sizeYSelectionBar = sizeXSelectionBar;

	// action bar
	public static float startXActionBar = 0;
	public static float sizeXActionBar = ratioBarVertX*Game.resX;
	public static float sizeYActionBar = 5*ratioBarVertX*Game.resX;
	public static float startYActionBar = Game.resY - sizeYActionBar - sizeYSelectionBar;
	public static float yActionBar = startYActionBar+sizeYActionBar;
	public static boolean mouseOnActionBar;
	public static int prodIconNbY = 5;
	public static int prodIconNbX = 2;
	public static boolean[][] toDrawDescription = new boolean[prodIconNbY][prodIconNbX];

	// top bar

	private static int food;

	public static float ratioSizeGoldX = 1/13f;
	public static float ratioSizeTimerX = 1/12f;
	public static float ratioSizeGoldY = 1/19f;
	public static float ratioSizeTimerY = 1/15f;

	public static float startYDescription = 0;
	public static float ratioSizeDescriptionX = 1/3f;
	public static float ratioSizeChoiceX = 1/6f;
	public static boolean mouseOnTopBar;

	private static float debutC = nbRoundInit/4;
	private static float debut1 = 3*nbRoundInit/8;
	private static float debut2 = nbRoundInit/2;
	private static float dureeDescente = nbRoundInit/8;

	// minimap
	public static float startXMiniMap = Game.resX*(1-ratioMinimapX)+3; 
	public static float startX2MiniMap = Game.resX*(1-ratioMinimapX)+3;
	public static float offsetDrawX;
	public static float sizeXMiniMap = Game.resX*ratioMinimapX-6, sizeYMiniMap = sizeXMiniMap;
	public static float startYMiniMap, startY2MiniMap = Game.resY-ratioMinimapX*Game.resX+3;
	public static float widthMiniMap;
	public static float heightMiniMap;
	public static float ratioWidthMiniMap;
	public static float ratioHeightMiniMap;
	private static float debutGlissade = nbRoundInit/4;
	private static float dureeGlissade = nbRoundInit/4;


	//killing spree offest
	public static float offsetYkillingSpree = -150f;


	// Spell with click handling
	public static boolean spellOk = true;
	private static boolean spellRelease = false;
	public static Integer spellLauncher;
	public static Integer spellTarget;
	public static float spellX,spellY;
	public static ObjetsList spellCurrent = null;


	///////
	// Update mathods
	///////

	public static void update(InputObject im, Plateau plateau){
		updateActionInterface(im, plateau);
		updateTopInterface(im.xOnScreen, im.yOnScreen, plateau);
		if(Player.rectangleSelection ==null){			
			updateMinimap(im, plateau);
		}
	}

	public static void updateActionInterface(InputObject im, Plateau plateau){
		for(int i = 0 ; i<prodIconNbY;i++){
			for(int j = 0 ; j<prodIconNbX; j++){
				toDrawDescription[i][j] = false;
			}
		}
		float xMouse = im.xOnScreen;
		float yMouse = im.yOnScreen;
		// draw items descriptions
		if (isMouseOnActionBar(xMouse, yMouse)) {
			int mouseOnItem = (int) ((yMouse - startYActionBar) / (sizeYActionBar / prodIconNbY));
			int yItem = xMouse>startXActionBar + sizeXActionBar? 1:0;
			if (mouseOnItem >= 0 && mouseOnItem < prodIconNbY){
				toDrawDescription[mouseOnItem][yItem] = true;
				if(im.isPressed(KeyEnum.LeftClick)){
					im.pressed.remove(KeyEnum.LeftClick);
					if(Player.selection.size()>0 && plateau.getById(Player.selection.get(0)) instanceof Character){
						Character c = (Character) plateau.getById(Player.selection.get(0)); 
						Spell s = c.getSpell(mouseOnItem);
						if(s != null && s.getAttribut(Attributs.needToClick)>0 && c.canLaunch(mouseOnItem)){
							spellLauncher = c.getId();
							spellCurrent = s.name;
						}
					} else {
						if(yItem==0){
							im.pressProd(mouseOnItem);
						} else if (yItem==1){
							im.pressTech(mouseOnItem);
						}
					}
				}
				if(im.isDown(KeyEnum.LeftClick)){
					im.down.remove(KeyEnum.LeftClick);
				}
			}
		}

		// handle spell
		if(Player.selection.size()>0 && plateau.getById(Player.selection.get(0)) instanceof Character){
			for(int i = 0; i<prodIconNbY; i++){
				if(im.isPressedProd(i)){
					Character c = (Character) plateau.getById(Player.selection.get(0)); 
					Spell s = c.getSpell(i);

					if(s!=null && s.getAttribut(Attributs.needToClick)>0 && c.canLaunch(i)){
						spellLauncher = c.getId();
						spellCurrent = s.name;

					}
					im.pressed.remove(KeyEnum.valueOf("Prod"+i));
				}
			}
		}
		if(spellCurrent!=null){
			spellX = im.x;
			spellY = im.y;
		}
		if(im.pressed.size()>0 && spellCurrent!=null ){
			if(im.isPressed(KeyEnum.LeftClick) && Player.selection.size()>0 && plateau.getById(Player.selection.get(0)) instanceof Character){
				// check if launch spell
				Character c = (Character) plateau.getById(Player.selection.get(0)); 
				if(c.getSpell(spellCurrent)!=null && c.getSpellState(spellCurrent)>=c.getSpell(spellCurrent).getAttribut(Attributs.chargeTime)){
					im.spell = spellCurrent;
					im.idSpellLauncher = spellLauncher;
//					if(spellTarget!=null){
//						im.idObjetMouse = spellTarget;
//					} else {
//						im.idObjetMouse = -1;
//					} 
					Integer buffer = Player.selection.get(0);
					Player.selection.removeElementAt(0);
					Player.selection.add(buffer);
					resetCurrentSpell();
				}
				im.pressed.remove(KeyEnum.LeftClick);
				spellRelease = true;
			} else {
				resetCurrentSpell();
			}

		}
		if(im.isDown(KeyEnum.LeftClick)){

		}
		//System.out.println(spellRelease);
		if(spellRelease==true){
			Player.rectangleSelection = null;
			if(im.down.contains(KeyEnum.LeftClick)){
				im.down.remove(KeyEnum.LeftClick);
			} else {
				spellRelease = false;
			}
			if(im.pressed.contains(KeyEnum.LeftClick)){
				im.pressed.remove(KeyEnum.LeftClick);
			}
		}
	}

	public static void updateTopInterface(float xMouse, float yMouse, Plateau plateau){
		mouseOnTopBar = isMouseOnTopBar(xMouse, yMouse);
	}

	public static void updateMinimap(InputObject im, Plateau plateau){
		if(isMouseOnMiniMap(im.xOnScreen, im.yOnScreen)){
			im.isOnMiniMap = true;

			im.x = (int) StrictMath.floor((im.xOnScreen-startXMiniMap)/ratioWidthMiniMap);
			im.y = (int) StrictMath.floor((im.yOnScreen-startYMiniMap)/ratioHeightMiniMap);
		}
	}

	public static void updateRatioMiniMap(Plateau plateau){

		if(plateau.getMaxX()>plateau.getMaxY()){
			widthMiniMap = sizeXMiniMap;
			heightMiniMap = widthMiniMap*plateau.getMaxY()/plateau.getMaxX();
			startXMiniMap = startX2MiniMap;
			startYMiniMap = startY2MiniMap + (sizeYMiniMap-heightMiniMap)/2;
		} else {
			heightMiniMap = sizeYMiniMap;			
			widthMiniMap = heightMiniMap*plateau.getMaxX()/plateau.getMaxY();
			startXMiniMap = startX2MiniMap + (sizeXMiniMap-widthMiniMap)/2;
			startYMiniMap = startY2MiniMap;
		}
		ratioWidthMiniMap = widthMiniMap/plateau.getMaxX();
		ratioHeightMiniMap = heightMiniMap/plateau.getMaxY();
	}

	///////
	// Draw mathods
	///////

	public static Graphics draw(Graphics g, Plateau plateau){
		// Draw Background :


		// Draw image according to size

		//g.drawImage(background,x,y-6f);

		// ACTIONS, Spells  and production
		drawSpell(g, plateau);
		drawActionInterface(g, plateau);
		drawSelectionInterface(g, plateau);
		drawTopInterface(g, plateau);
		drawMiniMap(g, plateau);

		//spell.draw(g);


		return g;
	}

	public static void drawSelectionInterface(Graphics g, Plateau plateau){

		float sizeXBar;
		float x = 0;
		if(plateau.getRound()<nbRoundInit)
			startXSelectionBar = StrictMath.max(-Game.resX-10, StrictMath.min(0, Game.resX*(plateau.getRound()-debut-duree)/duree));
		else
			startXSelectionBar = 0;

		// variable de travail sizeVerticalBar
		g.setLineWidth(1f);
		float sVB = ratioBarVertX*Game.resX;

		Team team = Player.getTeam(plateau);

		// Draw building state
		Vector<Integer> selection = Player.selection;
		if(selection.size()>0 && plateau.getById(selection.get(0)) instanceof Building ){

			Building b = (Building) plateau.getById(selection.get(0));

			sizeXBar = (StrictMath.min(4,b.getQueue().size()+1))*(sVB+2)+3;
			Utils.drawNiceRect(g, team.color, startXSelectionBar+sizeXSelectionBar-4, Game.resY-sVB, sizeXBar, sVB+4);
			Utils.drawNiceRect(g, team.color, startXSelectionBar-4, startYSelectionBar, sizeXSelectionBar+4, sizeYSelectionBar+4);

			int compteur = 0;
			if(b.getQueue().size()>0){
				for(ObjetsList q : b.getQueue()){
					Image icone = Images.get("icon"+q.name());
					if(compteur ==0){
						//Show icons
						//Show production bar
						g.drawImage(icone,startXSelectionBar+sizeXSelectionBar/4, 
								startYSelectionBar+sizeYSelectionBar/4,
								startXSelectionBar+sizeXSelectionBar-5, startYSelectionBar + sizeYSelectionBar-5,0,0,512,512);
						g.setColor(Color.white);
						String s = team.data.getAttributString(q, Attributs.printName);
						Float prodTime = team.data.getAttribut(q, Attributs.prodTime);
						g.drawString(s, startXSelectionBar+sizeXSelectionBar/2-GraphicElements.font_main.getWidth(s)/2f, 
								startYSelectionBar+sizeYSelectionBar/8f-GraphicElements.font_main.getHeight(s)/2f);
						g.fillRect(startXSelectionBar+sizeXSelectionBar/16, 
								startYSelectionBar+sizeYSelectionBar/4 +10f, sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f);
						g.setColor(Color.gray);
						g.fillRect(startXSelectionBar+sizeXSelectionBar/16, 
								startYSelectionBar+sizeYSelectionBar/4 +10f, sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f);
						g.setColor(team.color);
						g.fillRect(startXSelectionBar+sizeXSelectionBar/16, 
								startYSelectionBar+sizeYSelectionBar/4+10f+b.charge*(3*sizeYSelectionBar/4-20f)/prodTime, 
								sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f-b.charge*(3*sizeYSelectionBar/4-20)/prodTime);
					}
					else{
						g.drawImage(icone,sizeXSelectionBar+5+(sVB)*(compteur-1), 
								Game.resY-sVB+3f, 
								sizeXSelectionBar+(sVB)*(compteur), 
								Game.resY-1,0f,0f,512f,512f);
					}
					compteur ++;
				}
			} else {
				g.setColor(Color.white);
				String s = b.getAttributString(Attributs.printName);
				g.drawString(s, startXSelectionBar+sizeXSelectionBar/2-GraphicElements.font_main.getWidth(s)/2f, 
						startYSelectionBar+sizeYSelectionBar/8f-GraphicElements.font_main.getHeight(s)/2f);
			}
			//		} else if(selection.size()>0 && selection.get(0) instanceof Building  ){
			//			Building b = (Building) selection.get(0);
			////			sizeXBar = (b.queue.size()+1)*(sVB+2);
			////			Utils.drawNiceRect(g, game.currentPlayer.getGameTeam().color, startX+Game.resX-4, parent.p.g.resY-sVB, 5*(sVB+2), sVB+4);
			//			Utils.drawNiceRect(g,  Selection.getTeam().color, startX-4, startY, Game.resX+4, sizeY+4);
			//			if(b.getQueueTechnologie()!=null){
			//				Image icone = Images.get(b.getQueueTechnologie().getIcon());
			//				//Show icons
			//				//Show production bar
			//				g.drawImage(icone,startX+Game.resX/4, startY+sizeY/4,startX+Game.resX-5, startY + sizeY-5,0,0,512,512);
			//				g.setColor(Color.white);
			//				String s = b.getQueueTechnologie().getName();
			//
			//				g.drawString(s, startX+Game.resX/2-GraphicElements.font_main.getWidth(s)/2f, startY+sizeY/8f-GraphicElements.font_main.getHeight(s)/2f);
			//				g.fillRect(startX+Game.resX/16, startY+sizeY/4 +10f, Game.resX/8f,3*sizeY/4-20f);
			//				g.setColor(Color.gray);
			//				g.fillRect(startX+Game.resX/16, startY+sizeY/4 +10f, Game.resX/8f,3*sizeY/4-20f);
			//				g.setColor( Selection.getTeam().color);
			//				g.fillRect(startX+Game.resX/16, startY+sizeY/4+10f+b.charge*(3*sizeY/4-20f)/b.getAttribut(b.getQueueTechnologie().objet, Attributs.foodCost), Game.resX/8f,3*sizeY/4-20f-b.charge*(3*sizeY/4-20)/b.getAttribut(b.getQueueTechnologie().objet, Attributs.foodCost));
			//			} else {
			//				g.setColor(Color.white);
			//				String s = b.getAttributString(Attributs.printName);
			//				g.drawString(s, startX+Game.resX/2-GraphicElements.font_main.getWidth(s)/2f, startY+sizeY/8f-GraphicElements.font_main.getHeight(s)/2f);
			//			}
		}else if(selection.size()>0 && plateau.getById(selection.get(0)) instanceof Character ){

			Character c;
			int compteur = 0;
			int nb = selection.size()-1;

			sizeXBar = (StrictMath.min(nb+1, 5))*(sVB+2)+2;
			Utils.drawNiceRect(g, team.color, 
					startXSelectionBar+sizeXSelectionBar-4, Game.resY-sVB, sizeXBar, sVB+4);
			Utils.drawNiceRect(g, team.color, 
					startXSelectionBar-4, startYSelectionBar, sizeXSelectionBar+4, sizeYSelectionBar+4);
			for(Integer id : selection){
				Character a = (Character) plateau.getById(id);
				c = (Character) plateau.getById(id);
				if(c!=null){

					Image icone = Images.get(c.getName()+c.team.colorName);
					int imageWidth = icone.getWidth()/5;
					int imageHeight = icone.getHeight()/4;
					//float r = a.collisionBox.getBoundingCircleRadius();
					if(compteur ==0){
						//Show icons
						//Show production bar
						g.setColor(Color.darkGray);
						g.fillRect(startXSelectionBar+sizeXSelectionBar/4, startYSelectionBar+sizeYSelectionBar/4,
								3*sizeXSelectionBar/4-5, 3*sizeYSelectionBar/4-5);
						g.setColor(Color.white);
						g.drawRect(startXSelectionBar+sizeXSelectionBar/4, startYSelectionBar+sizeYSelectionBar/4,
								3*sizeXSelectionBar/4-5, 3*sizeYSelectionBar/4-5);
						g.drawImage(icone,startXSelectionBar+sizeXSelectionBar/4, startYSelectionBar+sizeYSelectionBar/4,
								startXSelectionBar+sizeXSelectionBar-5, startYSelectionBar + sizeYSelectionBar-5,
								imageWidth*c.animation,0,imageWidth*c.animation+imageWidth,imageHeight);
						g.setColor(Color.white);
						String s = a.getAttributString(Attributs.printName);
						g.drawString(s, startXSelectionBar+sizeXSelectionBar/2-GraphicElements.font_main.getWidth(s)/2f, 
								startYSelectionBar+sizeYSelectionBar/8f-GraphicElements.font_main.getHeight(s)/2f);
						g.fillRect(startXSelectionBar+sizeXSelectionBar/16, startYSelectionBar+sizeYSelectionBar/4 +10f,
								sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f);
						g.setColor(Color.darkGray);
						g.fillRect(startXSelectionBar+sizeXSelectionBar/16, startYSelectionBar+sizeYSelectionBar/4 +10f, 
								sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f);
						float x_temp = a.getLifePoints()/a.getAttribut(Attributs.maxLifepoints);
						g.setColor(new Color((1f-x_temp),x_temp,0));
						g.fillRect(startXSelectionBar+sizeXSelectionBar/16, 
								startYSelectionBar+sizeYSelectionBar/4+10f+(a.getAttribut(Attributs.maxLifepoints)-a.getLifePoints())*(3*sizeYSelectionBar/4-20f)/a.getAttribut(Attributs.maxLifepoints), 
								sizeXSelectionBar/8f,
								3*sizeYSelectionBar/4-20f-(a.getAttribut(Attributs.maxLifepoints)-a.getLifePoints())*(3*sizeYSelectionBar/4-20f)/a.getAttribut(Attributs.maxLifepoints));
						g.setColor(Color.white);
						g.drawRect(startXSelectionBar+sizeXSelectionBar/16, startYSelectionBar+sizeYSelectionBar/4 +10f,
								sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f);

					}
					else{
						int x1,y1,x2,y2;
						if(nb>5){
							x1 = (int) (x+sizeXSelectionBar+5+(sVB)*(compteur-1)*4/(nb-1));
							y1 = (int) (Game.resY-sVB+3f);
							x2 = (int) (x1+sVB);
							y2 = (int) (y1+sVB);						
						} else {
							x1 = (int) (x+sizeXSelectionBar+5+(sVB)*(compteur-1));
							y1 = (int) (Game.resY-sVB+3f);
							x2 = (int) (x1+sVB);
							y2 = (int) (y1+sVB);
						}
						float x_temp = a.getLifePoints()/a.getAttribut(Attributs.maxLifepoints);
						g.setColor(Color.darkGray);
						g.fillRect(x1, y1, x2-x1, y2-y1);
						g.setColor(new Color((1f-x_temp),x_temp,0));
						float diff = x_temp*(y2-y1);
						g.fillRect(x1, y1-diff+(y2-y1), (x2-x1)/5, diff);
						g.drawImage(icone,x1, y1, x2, y2, imageWidth*c.animation, 0, imageWidth*c.animation+imageWidth, imageHeight);
						g.setColor(Color.white);
						g.drawRect(x1, y1, x2-x1, y2-y1);
					}
					compteur ++;
				}
			}
		} else {
			Utils.drawNiceRect(g,  team.color, 
					startXSelectionBar-4, startYSelectionBar, sizeXSelectionBar+4, sizeYSelectionBar+4);
		}
	}

	public static void drawActionInterface(Graphics g, Plateau plateau){
		float startY2;
		Image imageGold ;
		Image imageFood;
		float debut = nbRoundInit/4, duree = debut;
		float offset;
		// Production Bar
		float ratio =1f/prodIconNbY;
		Team team = Player.getTeam(plateau);
		offset = sizeXSelectionBar;
		float x = 0;
		startYActionBar = Game.resY - sizeYActionBar - sizeYSelectionBar;
		startY2 = Game.resY - sizeYSelectionBar;
		//		for(int i= 0 ; i<prodIconNbY; i++){
		//			for(int j= 0 ; j<prodIconNbX; j++){
		//				toDrawDescription[i][j] = false;
		//			}
		//		}

		imageGold = Images.get("imagegolddisplayressources");
		imageFood = Images.get("imagefooddisplayressources");

		// Draw the potential actions
		// Draw Separation (1/3 1/3 1/3) : 

		if(plateau.getRound()<nbRoundInit)
			x = StrictMath.max(-offset-10, StrictMath.min(0, offset*(plateau.getRound()-debut-duree)/duree));
		else
			x = 0;
		g.setLineWidth(1f);
		if(mouseOnActionBar && yActionBar>startYActionBar)
			yActionBar = startYActionBar+(yActionBar-startYActionBar)/5;
		if(!mouseOnActionBar && yActionBar<startY2)
			yActionBar = startY2+(yActionBar-startY2)/5;

		Utils.drawNiceRect(g,  team.color, x-4, yActionBar-5, 2*sizeXActionBar+4, sizeYActionBar+9);
		g.setColor(Color.darkGray);
		for(int i=0; i<5; i++){
			g.setColor(Color.darkGray);
			g.fillRect(x+2f, yActionBar+2f + i*sizeXActionBar, -7f+sizeXActionBar, -7f+sizeXActionBar);
			g.fillRect(x+2f+sizeXActionBar, yActionBar+2f + i*sizeXActionBar, -7f+sizeXActionBar, -7f+sizeXActionBar);
		}
		g.setColor(Color.white);
		Vector<Integer> selection = Player.selection;
		// Draw Production/Effect Bar
		if(selection.size()>0 && plateau.getById(selection.get(0)) instanceof Building){
			mouseOnActionBar = true;
			Building b =(Building) plateau.getById(selection.get(0));
			//Print building capacities
			Vector<ObjetsList> ul = b.getProductionList(plateau);
			int limit = StrictMath.min(5, ul.size());
			Font f = g.getFont();
			for(int i=0; i<limit;i++){ 
				g.drawImage(Images.get("icon"+ul.get(i)), x+2f, yActionBar+2f + ratio*i*sizeYActionBar, x-5f+sizeXActionBar, yActionBar-5f+ratio*i*sizeYActionBar+sizeXActionBar, 0, 0, 512,512);
				g.setColor(Color.white);
				g.setColor( team.color);
				g.drawRect(x+1f, yActionBar+1f + i*sizeXActionBar, -6f+sizeXActionBar, -6f+sizeXActionBar);
				if(ul.size()>i && toDrawDescription[i][0]){
					// GET PRICE
					g.translate(sizeXActionBar, 0f);
					Float foodPrice = team.data.getAttribut(ul.get(i), Attributs.foodCost);
					Float goldPrice = team.data.getAttribut(ul.get(i), Attributs.goldCost);
					Float faithPrice = team.data.getAttribut(ul.get(i), Attributs.faithCost);
					Float prodTime = team.data.getAttribut(ul.get(i), Attributs.prodTime);
					g.setColor(Color.white);
					g.drawString(ul.get(i).name(), x + ratio*sizeYActionBar+10f, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name())/2f);
					g.drawImage(imageFood,x + 3.6f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name())/2f);
					g.drawString(": "+foodPrice,x + 3.95f*(sizeXActionBar+400f)/7, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name())/2f);
					g.drawImage(imageGold,x + 4.8f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name())/2f);
					g.drawString(": "+goldPrice,x + 5.15f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name())/2f);
					g.drawString("T: ",x + 6f*(sizeXActionBar+400f)/7, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name())/2f);
					g.drawString(Float.toString(prodTime),x + 6.35f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name())/2f);
					g.translate(-sizeXActionBar, 0f);
				}
			}


			g.translate(sizeXActionBar, 0f);

			//Print building capacities
			Vector<ObjetsList> ul2 = b.getTechnologyList(plateau);
			limit = StrictMath.min(5, ul2.size());
			for(int i=0; i<limit;i++){
				float goldCost = team.data.getAttribut(b.getTechnologyList(plateau).get(i),Attributs.goldCost);
				float foodCost = team.data.getAttribut(b.getTechnologyList(plateau).get(i),Attributs.foodCost);
				float faithCost = team.data.getAttribut(b.getTechnologyList(plateau).get(i),Attributs.faithCost);
				float prodTime = team.data.getAttribut(b.getTechnologyList(plateau).get(i),Attributs.prodTime);
				String icon = team.data.getAttributString(b.getTechnologyList(plateau).get(i),Attributs.nameIcon);
				g.drawImage(Images.get(icon), x+2f, yActionBar+2f + ratio*i*sizeYActionBar, x-5f+sizeXActionBar, yActionBar-5f+ratio*i*sizeYActionBar+sizeXActionBar, 0, 0, 512,512);
				// CHANGE PUT PRICES
				g.setColor( team.color);
				g.drawRect(x+1f, yActionBar+1f + i*sizeXActionBar, -6f+sizeXActionBar, -6f+sizeXActionBar);
				if(ul2.size()>i && toDrawDescription[i][1]){
					g.setColor(Color.white);
					g.drawString(ul2.get(i).name(), x + ratio*sizeYActionBar+10f, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawImage(imageFood,x + 3.6f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawString(": "+(int)foodCost,x + 3.95f*(sizeXActionBar+400f)/7, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawImage(imageGold,x + 4.8f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawString(": "+(int)goldCost,x + 5.15f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawString("T: ",x + 6f*(sizeXActionBar+400f)/7, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawString(Integer.toString(((int)prodTime)),x + 6.35f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
				}
			}
			g.translate(-sizeXActionBar, 0f);
		}
		else if(selection.size()>0 && plateau.getById(selection.get(0)) instanceof Character){
			mouseOnActionBar = true;
			Character b =(Character) plateau.getById(selection.get(0));
			//Print building capacities
			Vector<Spell> ul = b.getSpells();
			int limit = StrictMath.min(5, ul.size());
			Vector<Float> state = b.getSpellsState();
			Font f = g.getFont();
			Image im;
			for(int i=0; i<limit;i++){ 
				if(state.get(i)==ul.get(i).getAttribut(Attributs.chargeTime)){
					g.setColor(Color.white);
				} else {
					g.setColor( team.color);
				}
				if(spellCurrent==b.getSpells().get(i).name){
					g.setColor(Color.orange);
				}
				g.drawRect(x+1f, yActionBar+1f + i*sizeXActionBar, -6f+sizeXActionBar, -6f+sizeXActionBar);
				im = Images.get("spell"+ul.get(i).name);
				g.drawImage(im, x+2f, yActionBar+2f + ratio*i*sizeYActionBar, x-5f+sizeXActionBar, yActionBar-5f+ratio*i*sizeYActionBar+sizeXActionBar, 0, 0, 512,512);
				Color c =  team.color;
				c.a = 0.8f;
				g.setColor(c);
				if(state.get(i)>10){
					float diffY = (int)((-5f+sizeXActionBar)*(state.get(i))/ul.get(i).getAttribut(Attributs.chargeTime));
					g.fillRect(x+2f, yActionBar+2f + ratio*i*sizeYActionBar+diffY, x-5f+sizeXActionBar, -5f+sizeXActionBar-diffY);
				}
				g.setColor(Color.white);

				if(ul.size()>i && toDrawDescription[i][0]){
					g.translate(sizeXActionBar, 0f);
					g.setColor(Color.white);
					if(ul.get(i).getAttribut(Attributs.chargeTime)>0)
						if(state.get(i)>=ul.get(i).getAttribut(Attributs.chargeTime))
							g.drawString(ul.get(i).name.name(), x + ratio*sizeYActionBar+10f, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name.name())/2f);
						else
							g.drawString(ul.get(i).name+" - "+(int)(100*state.get(i)/ul.get(i).getAttribut(Attributs.chargeTime))+"%", x + ratio*sizeYActionBar+10f, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name.name())/2f);
					else
						g.drawString(ul.get(i).name.name(), x + ratio*sizeYActionBar+10f, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name.name())/2f);
					g.translate(-sizeXActionBar, 0f);
				}

			}
		} else {
			mouseOnActionBar = false;
		}

	}

	public static void drawTopInterface(Graphics g, Plateau plateau){
		String s;
		float rX = Game.resX;
		float rY = Game.resY;
		float offset = ratioSizeTimerY*rY;
		float yCentral = StrictMath.max(-offset-10,StrictMath.min(0, offset*(plateau.getRound()-debutC-dureeDescente)/dureeDescente));
		offset = ratioSizeGoldY*rY;
		float y1 = StrictMath.max(-offset-10,StrictMath.min(0, offset*(plateau.getRound()-debut1-dureeDescente)/dureeDescente));
		float y2 = StrictMath.max(-offset-10,StrictMath.min(0, offset*(plateau.getRound()-debut2-dureeDescente)/dureeDescente));
		Team team = Player.getTeam(plateau);
		if(food != team.food){
			food += (team.food-food)/5+StrictMath.signum(team.food-food);
		}

		// pop
		Utils.drawNiceRect(g, team.color,(1-ratioSizeTimerX)*rX/2-2*ratioSizeGoldX*rX,y1,ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		s = ""+team.getPop(plateau) + "/" + team.getMaxPop(plateau);
		if(team.getPop(plateau)==team.getMaxPop(plateau)){
			g.setColor(Color.red);
		}else{
			g.setColor(Color.white);
		}
		g.drawString(s, (1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX-10f-GraphicElements.font_main.getWidth(s), y1+ratioSizeGoldY*rY/2f-GraphicElements.font_main.getHeight("0")/2-3f);
		g.drawImage(Images.get("imagePop"), (1-ratioSizeTimerX)*rX/2-2*ratioSizeGoldX*rX+10, y1+ratioSizeGoldY*rY/2f-3-Images.get("imagefooddisplayressources").getHeight()/2);

		// food
		Utils.drawNiceRect(g, team.color,(1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX,y1,ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		s = ""+food;
		g.setColor(Color.white);
		g.drawString(s, (1-ratioSizeTimerX)*rX/2-10f-GraphicElements.font_main.getWidth(s), y1+ratioSizeGoldY*rY/2f-GraphicElements.font_main.getHeight("0")/2-3f);
		g.drawImage(Images.get("imagefooddisplayressources"), (1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX+10, y1+ratioSizeGoldY*rY/2f-3-Images.get("imagefooddisplayressources").getHeight()/2);

		// timer
		Utils.drawNiceRect(g, team.color,(1-ratioSizeTimerX)*rX/2,yCentral,ratioSizeTimerX*rX,ratioSizeTimerY*rY);
		g.setColor(Color.white);
		s = "todo : timer";
		//		s = ""+Utils.displayTime((int) ((System.currentTimeMillis()-Game.gameSystem.startTime)/1000));
		g.drawString(s, rX/2-GraphicElements.font_main.getWidth(s)/2f, yCentral+2*ratioSizeTimerY*rY/3f-GraphicElements.font_main.getHeight(s)/2f);

		// timer kill
		float opacity = 255f;
		float centerx = 70, centery = 70;
		float r = 25f;
		if(team.nbKill>0){
			if(offsetYkillingSpree!=0){
				offsetYkillingSpree*=0.5f;
				if(offsetYkillingSpree>-1){
					offsetYkillingSpree = 0f;
				}
			}
			g.setColor(new Color(0f,0f,0f,opacity));
			g.fillOval(centerx-r-10, centery+offsetYkillingSpree-r-10, 2*r+20f, 2*r+20f);
			//g.setColor(new Color(0f,0f,0f,opacity));
			//g.fillOval(x-r-8f, y-offsetY-r-8f, 2*r+16f, 2*r+16f);
			//						g.setColor(Color.white);
			//						g.fillOval(x-r-2f, y-sizeY/2-r-2f, 2*r+4f, 2*r+4f);
			g.setColor(new Color(team.color.r,team.color.g,team.color.b,opacity));
			float startAngle = 270f;
			float sizeAngle = (float)(1f*team.timerKill*(360f)/team.timerMaxKill);
			g.fillArc(centerx-r-8f, centery+offsetYkillingSpree-r-8f, 2*r+16f, 2*r+16f, startAngle, startAngle+sizeAngle);
			g.setColor(new Color(0f,0f,0f,opacity));
			g.fillOval(centerx-r, centery+offsetYkillingSpree-r, 2*r, 2*r);
			g.setColor(new Color(1f,1f,1f,opacity));
			g.drawString(""+team.nbKill, centerx-GraphicElements.font_main.getWidth(""+team.nbKill)/2, centery+offsetYkillingSpree-GraphicElements.font_main.getHeight(""+team.nbKill)/2);
		} else {
			offsetYkillingSpree = -150f;
		}

	}

	public static void drawMiniMap(Graphics g, Plateau plateau){
		Team team = Player.getTeam(plateau);
		offsetDrawX = StrictMath.max(0, StrictMath.min(sizeXMiniMap+10, -sizeXMiniMap*(plateau.getRound()-debutGlissade-dureeGlissade)/dureeGlissade));
		Utils.drawNiceRect(g,  team.color,startX2MiniMap+offsetDrawX-3, startY2MiniMap-3, sizeXMiniMap+9, sizeYMiniMap+9);
		g.setColor(Color.black);
		g.fillRect(startX2MiniMap+offsetDrawX, startY2MiniMap, sizeXMiniMap, sizeYMiniMap);
		// Find the high left corner
		float hlx = StrictMath.max(startXMiniMap,startXMiniMap+ratioWidthMiniMap*Camera.Xcam/Game.ratioX);
		float hly = StrictMath.max(startYMiniMap,startYMiniMap+ratioHeightMiniMap*Camera.Ycam/Game.ratioY);
		float brx = StrictMath.min(startXMiniMap+widthMiniMap,startXMiniMap+ratioWidthMiniMap*(Camera.Xcam+Game.resX)/Game.ratioX);
		float bry = StrictMath.min(startYMiniMap+heightMiniMap,startYMiniMap+ratioHeightMiniMap*(Camera.Ycam+Game.resY)/Game.ratioY);
		// Find the bottom right corner

		// Draw background
		g.setColor(new Color(0.1f,0.4f,0.1f));
		Case ca;
		for(int i=0; i<plateau.getMapGrid().grid.size(); i++){
			for(int j=0; j<plateau.getMapGrid().grid.get(0).size(); j++){
				ca = plateau.getMapGrid().grid.get(i).get(j);
				g.drawImage(Images.get(ca.getIdTerrain().name()+"tile0"),
						startXMiniMap+offsetDrawX+ratioWidthMiniMap*ca.x,
						startYMiniMap+ratioHeightMiniMap*ca.y);
			}
		}
		for(NaturalObjet q : plateau.getNaturalObjets()){
			g.setColor(Color.green);
			g.fillRect(startXMiniMap+offsetDrawX+ratioWidthMiniMap*q.getX()-ratioWidthMiniMap*q.sizeX/2f, startYMiniMap+ratioHeightMiniMap*q.getY()-ratioHeightMiniMap*q.sizeY/2f,ratioWidthMiniMap*q.sizeX , ratioHeightMiniMap*q.sizeY);
		}
		// Draw units on Camera 
		g.setAntiAlias(true);
		for(Character c : plateau.getCharacters()){		
			if(c.getTeam().id==2){
				if(plateau.isVisibleByTeam(Player.getTeamId(), c)){
					g.setColor(Colors.team2);
					float r = c.getAttribut(Attributs.size)*2f;
					g.fillOval(startXMiniMap+offsetDrawX+ratioWidthMiniMap*c.getX()-ratioWidthMiniMap*r, startYMiniMap+ratioHeightMiniMap*c.getY()-ratioHeightMiniMap*r, 2f*ratioWidthMiniMap*r, 2f*ratioHeightMiniMap*r);
				}
			}
			else if(c.getTeam().id==1){
				if(plateau.isVisibleByTeam(Player.getTeamId(), c)){
					g.setColor(Colors.team1);
					float r = c.getAttribut(Attributs.size)*2f;
					g.fillOval(startXMiniMap+offsetDrawX+ratioWidthMiniMap*c.getX()-ratioWidthMiniMap*r, startYMiniMap+ratioHeightMiniMap*c.getY()-ratioHeightMiniMap*r, 2f*ratioWidthMiniMap*r, 2f*ratioHeightMiniMap*r);
				}
			}
		}


		for(Bonus c : plateau.getBonus()){
			if(c.getTeam().id==0){
				g.setColor(Colors.team0);

			}
			if(c.getTeam().id==2){
				if(plateau.isVisibleByTeam(Player.getTeamId(), c)){
					g.setColor(Colors.team2);
				} else {
					g.setColor(Colors.team0);

				}
			}
			else if(c.getTeam().id==1){
				if(plateau.isVisibleByTeam(Player.getTeamId(), c)){
					g.setColor(Colors.team1);
				} else {
					g.setColor(Colors.team0);

				}
			}
			g.fillOval(startXMiniMap+offsetDrawX+ratioWidthMiniMap*(c.getX()-c.getAttribut(Attributs.size)/2f), 
					startYMiniMap+ratioHeightMiniMap*(c.getY()-c.getAttribut(Attributs.size)/2f), 
					ratioWidthMiniMap*c.getAttribut(Attributs.size), 
					ratioHeightMiniMap*c.getAttribut(Attributs.size));
		}
		g.setAntiAlias(false);
		for(Building c : plateau.getBuildings()){
			if(c.getTeam().id==0){
				g.setColor(Colors.team0);

			}
			if(c.getTeam().id==2){
				if(plateau.isVisibleByTeam(Player.getTeamId(), c) || Debug.debugFog){
					g.setColor(Colors.team2);
				} else {
					g.setColor(Colors.team0);

				}
			}
			else if(c.getTeam().id==1){
				if(plateau.isVisibleByTeam(Player.getTeamId(), c) || Debug.debugFog){
					g.setColor(Colors.team1);
				} else {
					g.setColor(Colors.team0);

				}
			}
			g.fillRect(startXMiniMap+offsetDrawX+ratioWidthMiniMap*c.getX()-ratioWidthMiniMap*c.getAttribut(Attributs.sizeX)/2f, startYMiniMap+ratioHeightMiniMap*c.getY()-ratioHeightMiniMap*c.getAttribut(Attributs.sizeY)/2f, ratioWidthMiniMap*c.getAttribut(Attributs.sizeX), ratioHeightMiniMap*c.getAttribut(Attributs.sizeY));

			if(c.constructionPoints<c.getAttribut(Attributs.maxLifepoints) && (plateau.isVisibleByTeam(Player.getTeamId(), c) || Debug.debugFog)){
				float ratio = c.constructionPoints/c.getAttribut(Attributs.maxLifepoints); 
				if(c.potentialTeam==1){
					g.setColor(Colors.team1);
				}
				else if(c.potentialTeam==2){
					g.setColor(Colors.team2);
				}
				g.fillRect(startXMiniMap+offsetDrawX+ratioWidthMiniMap*c.getX()-ratioWidthMiniMap*c.getAttribut(Attributs.sizeX)/2f, startYMiniMap+ratioHeightMiniMap*c.getY()-ratioHeightMiniMap*c.getAttribut(Attributs.sizeY)/2f, ratio*(ratioWidthMiniMap*c.getAttribut(Attributs.sizeX)), ratioHeightMiniMap*c.getAttribut(Attributs.sizeY));
			}
		}

		// Draw rect of Camera 
		g.setColor(Color.white);
		g.drawRect(hlx+offsetDrawX,hly,brx-hlx,bry-hly );
		// Fill rec on side
		g.setColor(Color.black);
		g.fillRect(startX2MiniMap+offsetDrawX, startY2MiniMap, startXMiniMap-startX2MiniMap, sizeYMiniMap);
		g.fillRect(2*startX2MiniMap+offsetDrawX+sizeXMiniMap-startXMiniMap, startY2MiniMap, startXMiniMap-startX2MiniMap, sizeYMiniMap);
		
	}

	public static void drawSpell(Graphics g, Plateau plateau){
		if(spellCurrent!=null){
			Character characterSpellLauncher = (Character) plateau.getById(spellLauncher);
			g.translate(-Camera.Xcam, -Camera.Ycam);
			g.scale(Game.resX/1920f, Game.resY/1080f);
			if(characterSpellLauncher!=null){
				
				Spell s = characterSpellLauncher.getSpell(spellCurrent);
				if(spellTarget!=null){	
					s.drawCast(g, plateau.getById(spellTarget), spellX, spellY, characterSpellLauncher, true, plateau);
				}else{
					s.drawCast(g, null, spellX, spellY, characterSpellLauncher, true, plateau);	
				}
			}
			g.scale(1920f/Game.resX, 1080f/Game.resY);
			g.translate(Camera.Xcam, Camera.Ycam);
		}
	}
	//////
	// Utils
	//////

	public static boolean isMouseOnActionBar(float xMouse, float yMouse){
		return xMouse > startXActionBar && xMouse < startXActionBar + 2*sizeXActionBar
				&& yMouse > startYActionBar && yMouse < startYActionBar + sizeYActionBar;
	}

	public static boolean isMouseOnTopBar(float xMouse, float yMouse){
		return xMouse > (1-ratioSizeTimerX)*Game.resX/2-2*ratioSizeGoldX*Game.resX 
				&& xMouse < (1+ratioSizeTimerX)*Game.resX/2+2*ratioSizeGoldX*Game.resX
				&& yMouse >0 && yMouse < ratioSizeTimerY*Game.resY;
	}

	public static boolean isMouseOnMiniMap(float xMouse, float yMouse){
		return xMouse > startXMiniMap + offsetDrawX && xMouse < startXMiniMap+ widthMiniMap
				&& yMouse > startYMiniMap && yMouse < startYMiniMap + heightMiniMap;
	}

	public static void resetCurrentSpell(){
		spellCurrent = null;
		spellOk = false;
		spellLauncher = null;
		spellX = 0;
		spellY = 0;
	}

	///////
	// Icone
	///////



	public static void init(Plateau plateau) {
		updateRatioMiniMap(plateau);
	}
}
