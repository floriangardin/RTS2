package display;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import bonus.Bonus;
import control.InputObject;
import control.KeyMapper.KeyEnum;
import data.Attributs;
import main.Main;
import model.Colors;
import model.Game;
import model.Player;
import plateau.Building;
import plateau.Character;
import plateau.NaturalObjet;
import plateau.Objet;
import plateau.Plateau;
import plateau.Team;
import ressources.GraphicElements;
import ressources.Images;
import spells.Spell;
import system.Debug;
import utils.ObjetsList;
import utils.Utils;

public class Interface {

	public Plateau plateau;

	public Player player;

	public float ratioMinimapX = 1/6f;
	public float ratioSelectionX = 1/8f;
	public float ratioSpellX = 1/12f;
	public float ratioBarVertX = 1/32f;

	public float nbRoundInit = 3*Main.framerate;
	public float debut = nbRoundInit/4, duree = debut;

	// selection bar
	public float startXSelectionBar = 0;
	public float startYSelectionBar = Game.resY-Game.resX*this.ratioSelectionX;
	public float sizeXSelectionBar = Game.resX*this.ratioSelectionX;
	public float sizeYSelectionBar = sizeXSelectionBar;

	// action bar
	public float startXActionBar = 0;
	public float sizeXActionBar = this.ratioBarVertX*Game.resX;
	public float sizeYActionBar = 5*this.ratioBarVertX*Game.resX;
	public float startYActionBar = Game.resY - sizeYActionBar - this.sizeYSelectionBar;
	public float yActionBar = startYActionBar+sizeYActionBar;
	public boolean mouseOnActionBar;
	public int prodIconNbY = 5;
	public int prodIconNbX = 2;
	public boolean[][] toDrawDescription = new boolean[prodIconNbY][prodIconNbX];

	// top bar
	Image imageGold = Images.get("imagegolddisplayressources");
	Image imageFood = Images.get("imagefooddisplayressources");
	Image imageMadness = Images.get("iconeMadness");
	Image imageWisdom = Images.get("iconeWisdom");
	Image imagePop = Images.get("imagePop");
	Image imageTimer;
	private int gold, food;

	public float ratioSizeGoldX = 1/13f;
	public float ratioSizeTimerX = 1/12f;
	public float ratioSizeGoldY = 1/19f;
	public float ratioSizeTimerY = 1/15f;

	public float startYDescription = 0;
	public float ratioSizeDescriptionX = 1/3f;
	public float ratioSizeChoiceX = 1/6f;
	public boolean mouseOnTopBar;
	public Vector<Icon> iconChoice;

	private float debutC = nbRoundInit/4;
	private float debut1 = 3*nbRoundInit/8;
	private float debut2 = nbRoundInit/2;
	private float dureeDescente = nbRoundInit/8;

	// minimap
	public float startXMiniMap = Game.resX*(1-ratioMinimapX)+3; 
	public float startX2MiniMap = Game.resX*(1-ratioMinimapX)+3;
	public float offsetDrawX;
	public float sizeXMiniMap = Game.resX*ratioMinimapX-6, sizeYMiniMap = sizeXMiniMap;
	public float startYMiniMap, startY2MiniMap = Game.resY-ratioMinimapX*Game.resX+3;
	public float widthMiniMap;
	public float heightMiniMap;
	public float ratioWidthMiniMap;
	public float ratioHeightMiniMap;
	private float debutGlissade = nbRoundInit/4;
	private float dureeGlissade = nbRoundInit/4;

	//card choice
	private float startYCardChoiceBar;
	private float sizeXCardChoiceBar = sizeXActionBar;
	private float sizeYCardChoiceBar;
	public Vector<Icon> cardChoice = new Vector<Icon>();

	//killing spree offest
	public float offsetYkillingSpree = -150f;


	// Spell with click handling
	public boolean spellOk = true;
	public Character spellLauncher;
	public Objet spellTarget;
	public float spellX,spellY;
	public ObjetsList spellCurrent = null;


	public Interface(Plateau plateau, Player player){
		this.plateau = plateau;
		this.updateRatioMiniMap();
		this.player = player;
	}

	///////
	// Update mathods
	///////

	public void update(InputObject im){
		this.updateActionInterface(im.xOnScreen, im.yOnScreen);
		this.updateTopInterface(im.xOnScreen, im.yOnScreen);
		this.updateMinimap(im);
	}

	public void updateActionInterface(float xMouse, float yMouse){
		if (isMouseOnActionBar(xMouse, yMouse)) {
			int mouseOnItem = (int) ((yMouse - startYActionBar) / (sizeYActionBar / prodIconNbY));
			int yItem = xMouse>startXActionBar + sizeXActionBar? 1:0;
			for(int i = 0 ; i<prodIconNbY;i++){
				for(int j = 0 ; j<prodIconNbX; j++){
					toDrawDescription[i][j] = false;
				}
			}

			if (mouseOnItem >= 0 && mouseOnItem < prodIconNbY)
				toDrawDescription[mouseOnItem][yItem] = true;
		} else {
			for(int i = 0 ; i<prodIconNbY;i++){
				for(int j = 0 ; j<prodIconNbX; j++){
					toDrawDescription[i][j] = false;
				}
			}
		}
	}

	public void updateTopInterface(float xMouse, float yMouse){
		mouseOnTopBar = isMouseOnTopBar(xMouse, yMouse);
		if(iconChoice==null){
			return;
		}
		for(Icon icon : iconChoice){
			icon.update(xMouse, yMouse);
		}
	}

	public void updateMinimap(InputObject im){
		if(isMouseOnMiniMap(im.xOnScreen, im.yOnScreen)){
			im.isOnMiniMap = true;

			im.x = (int) Math.floor((im.xOnScreen-this.startXMiniMap)/this.ratioWidthMiniMap);
			im.y = (int) Math.floor((im.yOnScreen-this.startYMiniMap)/this.ratioHeightMiniMap);
		}
	}

	public void updateRatioMiniMap(){

		if(plateau.maxX>plateau.maxY){
			this.widthMiniMap = this.sizeXMiniMap;
			this.heightMiniMap = this.widthMiniMap*plateau.maxY/plateau.maxX;
			this.startXMiniMap = this.startX2MiniMap;
			this.startYMiniMap = this.startY2MiniMap + (this.sizeYMiniMap-heightMiniMap)/2;
		} else {
			this.heightMiniMap = this.sizeYMiniMap;			
			this.widthMiniMap = this.heightMiniMap*plateau.maxX/plateau.maxY;
			this.startXMiniMap = this.startX2MiniMap + (this.sizeXMiniMap-widthMiniMap)/2;
			this.startYMiniMap = this.startY2MiniMap;
		}
		ratioWidthMiniMap = widthMiniMap/plateau.maxX;
		ratioHeightMiniMap = heightMiniMap/plateau.maxY;
	}

	///////
	// Draw mathods
	///////

	public Graphics draw(Graphics g, Camera camera){
		// Draw Background :


		// Draw image according to size

		//g.drawImage(this.background,x,y-6f);

		// ACTIONS, Spells  and production
		this.drawActionInterface(g);
		this.drawSelectionInterface(g);
		this.drawTopInterface(g);
		this.drawMiniMap(g, camera);

		//spell.draw(g);


		return g;
	}

	public void drawSelectionInterface(Graphics g){

		float sizeXBar;
		float x = 0;
		if(plateau.round<nbRoundInit)
			startXSelectionBar = Math.max(-Game.resX-10, Math.min(0, Game.resX*(plateau.round-debut-duree)/duree));

		// variable de travail sizeVerticalBar
		g.setLineWidth(1f);
		float sVB = this.ratioBarVertX*Game.resX;


		// Draw building state
		Vector<Objet> selection = player.selection.selection;
		if(selection.size()>0 && selection.get(0) instanceof Building ){

			Building b = (Building) selection.get(0);

			sizeXBar = (Math.min(4,b.getQueue().size()+1))*(sVB+2)+3;
			Utils.drawNiceRect(g, player.getGameTeam().color, startXSelectionBar+sizeXSelectionBar-4, Game.resY-sVB, sizeXBar, sVB+4);
			Utils.drawNiceRect(g, player.getGameTeam().color, startXSelectionBar-4, startYSelectionBar, sizeXSelectionBar+4, sizeYSelectionBar+4);

			int compteur = 0;
			if(b.getQueue().size()>0){
				for(ObjetsList q : b.getQueue()){
					Image icone = Images.get("icon"+q.name());
					if(compteur ==0){
						//Show icons
						//Show production bar
						g.drawImage(icone,startXSelectionBar+this.sizeXSelectionBar/4, 
								startYSelectionBar+this.sizeYSelectionBar/4,
								startXSelectionBar+sizeXSelectionBar-5, startYSelectionBar + sizeYSelectionBar-5,0,0,512,512);
						g.setColor(Color.white);
						String s = player.getGameTeam().data.getAttributString(q, Attributs.printName);
						Float prodTime = player.getGameTeam().data.getAttribut(q, Attributs.prodTime);
						g.drawString(s, startXSelectionBar+sizeXSelectionBar/2-GraphicElements.font_main.getWidth(s)/2f, 
								startYSelectionBar+sizeYSelectionBar/8f-GraphicElements.font_main.getHeight(s)/2f);
						g.fillRect(startXSelectionBar+this.sizeXSelectionBar/16, 
								startYSelectionBar+this.sizeYSelectionBar/4 +10f, sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f);
						g.setColor(Color.gray);
						g.fillRect(startXSelectionBar+this.sizeXSelectionBar/16, 
								startYSelectionBar+this.sizeYSelectionBar/4 +10f, sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f);
						g.setColor(player.getGameTeam().color);
						g.fillRect(startXSelectionBar+this.sizeXSelectionBar/16, 
								startYSelectionBar+this.sizeYSelectionBar/4+10f+b.charge*(3*sizeYSelectionBar/4-20f)/prodTime, 
								sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f-b.charge*(3*sizeYSelectionBar/4-20)/prodTime);
					}
					else{
						g.drawImage(icone,this.sizeXSelectionBar+5+(sVB)*(compteur-1), 
								Game.resY-sVB+3f, 
								this.sizeXSelectionBar+(sVB)*(compteur), 
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
			////			this.sizeXBar = (b.queue.size()+1)*(sVB+2);
			////			Utils.drawNiceRect(g, game.currentPlayer.getGameTeam().color, startX+Game.resX-4, parent.p.g.resY-sVB, 5*(sVB+2), sVB+4);
			//			Utils.drawNiceRect(g,  player.getGameTeam().color, startX-4, startY, Game.resX+4, sizeY+4);
			//			if(b.getQueueTechnologie()!=null){
			//				Image icone = Images.get(b.getQueueTechnologie().getIcon());
			//				//Show icons
			//				//Show production bar
			//				g.drawImage(icone,startX+this.Game.resX/4, startY+this.sizeY/4,startX+Game.resX-5, startY + sizeY-5,0,0,512,512);
			//				g.setColor(Color.white);
			//				String s = b.getQueueTechnologie().getName();
			//
			//				g.drawString(s, startX+Game.resX/2-GraphicElements.font_main.getWidth(s)/2f, startY+sizeY/8f-GraphicElements.font_main.getHeight(s)/2f);
			//				g.fillRect(startX+this.Game.resX/16, startY+this.sizeY/4 +10f, Game.resX/8f,3*sizeY/4-20f);
			//				g.setColor(Color.gray);
			//				g.fillRect(startX+this.Game.resX/16, startY+this.sizeY/4 +10f, Game.resX/8f,3*sizeY/4-20f);
			//				g.setColor( player.getGameTeam().color);
			//				g.fillRect(startX+this.Game.resX/16, startY+this.sizeY/4+10f+b.charge*(3*sizeY/4-20f)/b.getAttribut(b.getQueueTechnologie().objet, Attributs.foodCost), Game.resX/8f,3*sizeY/4-20f-b.charge*(3*sizeY/4-20)/b.getAttribut(b.getQueueTechnologie().objet, Attributs.foodCost));
			//			} else {
			//				g.setColor(Color.white);
			//				String s = b.getAttributString(Attributs.printName);
			//				g.drawString(s, startX+Game.resX/2-GraphicElements.font_main.getWidth(s)/2f, startY+sizeY/8f-GraphicElements.font_main.getHeight(s)/2f);
			//			}
		}else if(selection.size()>0 && selection.get(0) instanceof Character ){

			Character c;
			int compteur = 0;
			int nb = selection.size()-1;

			sizeXBar = (Math.min(nb+1, 5))*(sVB+2)+2;
			Utils.drawNiceRect(g, player.getGameTeam().color, 
					startXSelectionBar+sizeXSelectionBar-4, Game.resY-sVB, sizeXBar, sVB+4);
			Utils.drawNiceRect(g, player.getGameTeam().color, 
					startXSelectionBar-4, startYSelectionBar, sizeXSelectionBar+4, sizeYSelectionBar+4);
			for(Objet a : selection){
				c = (Character) a;
				Image icone = Images.get(c.name+"blue");
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
					float x_temp = a.lifePoints/a.getAttribut(Attributs.maxLifepoints);
					g.setColor(new Color((1f-x_temp),x_temp,0));
					g.fillRect(startXSelectionBar+sizeXSelectionBar/16, 
							startYSelectionBar+sizeYSelectionBar/4+10f+(a.getAttribut(Attributs.maxLifepoints)-a.lifePoints)*(3*sizeYSelectionBar/4-20f)/a.getAttribut(Attributs.maxLifepoints), 
							sizeXSelectionBar/8f,
							3*sizeYSelectionBar/4-20f-(a.getAttribut(Attributs.maxLifepoints)-a.lifePoints)*(3*sizeYSelectionBar/4-20f)/a.getAttribut(Attributs.maxLifepoints));
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
					float x_temp = a.lifePoints/a.getAttribut(Attributs.maxLifepoints);
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
		} else {
			Utils.drawNiceRect(g,  player.getGameTeam().color, 
					startXSelectionBar-4, startYSelectionBar, sizeXSelectionBar+4, sizeYSelectionBar+4);
		}


	}

	public void drawActionInterface(Graphics g){
		float startY2;
		Image imageGold ;
		Image imageFood;
		float debut = nbRoundInit/4, duree = debut;
		float offset;
		// Production Bar
		float ratio =1f/prodIconNbY;

		offset = sizeXSelectionBar;
		float x = 0;
		startYActionBar = Game.resY - sizeYActionBar - this.sizeYSelectionBar;
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

		if(plateau.round<nbRoundInit)
			x = Math.max(-offset-10, Math.min(0, offset*(plateau.round-debut-duree)/duree));
		else
			x = 0;
		g.setLineWidth(1f);
		if(mouseOnActionBar && yActionBar>startYActionBar)
			yActionBar = startYActionBar+(yActionBar-startYActionBar)/5;
		if(!mouseOnActionBar && yActionBar<startY2)
			yActionBar = startY2+(yActionBar-startY2)/5;

		Utils.drawNiceRect(g,  player.getGameTeam().color, x-4, yActionBar-5, 2*sizeXActionBar+4, sizeYActionBar+9);
		g.setColor(Color.darkGray);
		for(int i=0; i<5; i++){
			g.setColor(Color.darkGray);
			g.fillRect(x+2f, yActionBar+2f + i*sizeXActionBar, -7f+sizeXActionBar, -7f+sizeXActionBar);
			g.fillRect(x+2f+sizeXActionBar, yActionBar+2f + i*sizeXActionBar, -7f+sizeXActionBar, -7f+sizeXActionBar);
		}
		g.setColor(Color.white);

		Vector<Objet> selection = player.selection.selection;

		// Draw Production/Effect Bar
		if(selection.size()>0 && selection.get(0) instanceof Building){
			mouseOnActionBar = true;
			Building b =(Building) selection.get(0);
			//Print building capacities
			Vector<ObjetsList> ul = b.getProductionList(plateau);
			int limit = Math.min(5, ul.size());
			Font f = g.getFont();
			for(int i=0; i<limit;i++){ 
				g.drawImage(Images.get("icon"+ul.get(i)), x+2f, yActionBar+2f + ratio*i*sizeYActionBar, x-5f+sizeXActionBar, yActionBar-5f+ratio*i*sizeYActionBar+sizeXActionBar, 0, 0, 512,512);
				g.setColor(Color.white);
				g.setColor( player.getGameTeam().color);
				g.drawRect(x+1f, yActionBar+1f + i*sizeXActionBar, -6f+sizeXActionBar, -6f+sizeXActionBar);
				if(ul.size()>i && toDrawDescription[i][0]){
					// GET PRICE
					g.translate(sizeXActionBar, 0f);
					Float foodPrice = getAttribut(ul.get(i), Attributs.foodCost);
					Float goldPrice = getAttribut(ul.get(i), Attributs.goldCost);
					Float faithPrice = getAttribut(ul.get(i), Attributs.faithCost);
					Float prodTime = getAttribut(ul.get(i), Attributs.prodTime);
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
			limit = Math.min(5, ul2.size());
			for(int i=0; i<limit;i++){
				float goldCost = getAttribut(b.getTechnologyList(plateau).get(i),Attributs.goldCost);
				float foodCost = getAttribut(b.getTechnologyList(plateau).get(i),Attributs.foodCost);
				float faithCost = getAttribut(b.getTechnologyList(plateau).get(i),Attributs.faithCost);
				float prodTime = getAttribut(b.getTechnologyList(plateau).get(i),Attributs.prodTime);
				String icon = getAttributString(b.getTechnologyList(plateau).get(i),Attributs.nameIcon);
				g.drawImage(Images.get(icon), x+2f, yActionBar+2f + ratio*i*sizeYActionBar, x-5f+sizeXActionBar, yActionBar-5f+ratio*i*sizeYActionBar+sizeXActionBar, 0, 0, 512,512);
				// CHANGE PUT PRICES
				g.setColor( player.getGameTeam().color);
				g.drawRect(x+1f, yActionBar+1f + i*sizeXActionBar, -6f+sizeXActionBar, -6f+sizeXActionBar);
				if(ul.size()>i && toDrawDescription[i][1]){
					g.setColor(Color.white);
					g.drawString(ul2.get(i).name(), x + ratio*sizeYActionBar+10f, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawImage(imageFood,x + 3.6f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawString(": "+(int)foodCost,x + 3.95f*(sizeXActionBar+400f)/7, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawImage(imageGold,x + 4.8f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawString(": "+(int)goldCost,x + 5.15f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawString("T: ",x + 6f*(sizeXActionBar+400f)/7, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawString(Integer.toString(((int)prodTime)),x + 6.35f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name())/2f);
				}
			}
			g.translate(-sizeXActionBar, 0f);
		}
		else if(selection.size()>0 && selection.get(0) instanceof Character){
			mouseOnActionBar = true;
			Character b =(Character) selection.get(0);
			//Print building capacities
			Vector<Spell> ul = b.getSpells();
			int limit = Math.min(5, ul.size());
			Vector<Float> state = b.getSpellsState();
			Font f = g.getFont();
			Image im;
			for(int i=0; i<limit;i++){ 
				if(state.get(i)==ul.get(i).getAttribut(Attributs.chargeTime)){
					g.setColor(Color.white);
				} else {
					g.setColor( player.getGameTeam().color);
				}
				if(spellCurrent==b.getSpells().get(i).name){
					g.setColor(Color.orange);
				}
				g.drawRect(x+1f, yActionBar+1f + i*sizeXActionBar, -6f+sizeXActionBar, -6f+sizeXActionBar);
				im = Images.get("spell"+ul.get(i).name);
				g.drawImage(im, x+2f, yActionBar+2f + ratio*i*sizeYActionBar, x-5f+sizeXActionBar, yActionBar-5f+ratio*i*sizeYActionBar+sizeXActionBar, 0, 0, 512,512);
				Color c =  player.getGameTeam().color;
				c.a = 0.8f;
				g.setColor(c);
				if(state.get(i)>10){
					float diffY = (int)((-5f+sizeXActionBar)*(state.get(i))/ul.get(i).getAttribut(Attributs.chargeTime));
					g.fillRect(x+2f, yActionBar+2f + ratio*i*sizeYActionBar+diffY, x-5f+sizeXActionBar, -5f+sizeXActionBar-diffY);
				}
				g.setColor(Color.white);

				if(ul.size()>i && toDrawDescription[i][0]){
					g.setColor(Color.white);
					if(ul.get(i).getAttribut(Attributs.chargeTime)>0)
						if(state.get(i)>=ul.get(i).getAttribut(Attributs.chargeTime))
							g.drawString(ul.get(i).name.name(), x + ratio*sizeYActionBar+10f, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name.name())/2f);
						else
							g.drawString(ul.get(i).name+" - "+(int)(100*state.get(i)/ul.get(i).getAttribut(Attributs.chargeTime))+"%", x + ratio*sizeYActionBar+10f, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name.name())/2f);
					else
						g.drawString(ul.get(i).name.name(), x + ratio*sizeYActionBar+10f, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name.name())/2f);
				}

			}
		} else {
			mouseOnActionBar = false;
		}

	}

	public void drawTopInterface(Graphics g){
		String s;
		float rX = Game.resX;
		float rY = Game.resY;
		float offset = ratioSizeTimerY*rY;
		float yCentral = Math.max(-offset-10,Math.min(0, offset*(plateau.round-debutC-dureeDescente)/dureeDescente));
		offset = ratioSizeGoldY*rY;
		float y1 = Math.max(-offset-10,Math.min(0, offset*(plateau.round-debut1-dureeDescente)/dureeDescente));
		float y2 = Math.max(-offset-10,Math.min(0, offset*(plateau.round-debut2-dureeDescente)/dureeDescente));

		if(food != player.getGameTeam().food)
			food += (player.getGameTeam().food-food)/5+Math.signum(player.getGameTeam().food-food);

		// pop
		Utils.drawNiceRect(g, player.getGameTeam().color,(1-ratioSizeTimerX)*rX/2-2*ratioSizeGoldX*rX,y1,ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		s = ""+player.getGameTeam().getPop() + "/" + player.getGameTeam().getMaxPop();
		if(player.getGameTeam().getPop()==player.getGameTeam().getMaxPop()){
			g.setColor(Color.red);
		}else{
			g.setColor(Color.white);
		}
		g.drawString(s, (1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX-10f-GraphicElements.font_main.getWidth(s), y1+ratioSizeGoldY*rY/2f-GraphicElements.font_main.getHeight("0")/2-3f);
		g.drawImage(this.imagePop, (1-ratioSizeTimerX)*rX/2-2*ratioSizeGoldX*rX+10, y1+ratioSizeGoldY*rY/2f-3-this.imageFood.getHeight()/2);

		// food
		Utils.drawNiceRect(g, player.getGameTeam().color,(1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX,y1,ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		s = ""+food;
		g.setColor(Color.white);
		g.drawString(s, (1-ratioSizeTimerX)*rX/2-10f-GraphicElements.font_main.getWidth(s), y1+ratioSizeGoldY*rY/2f-GraphicElements.font_main.getHeight("0")/2-3f);
		g.drawImage(this.imageFood, (1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX+10, y1+ratioSizeGoldY*rY/2f-3-this.imageFood.getHeight()/2);



		// timer
		Utils.drawNiceRect(g, player.getGameTeam().color,(1-ratioSizeTimerX)*rX/2,yCentral,ratioSizeTimerX*rX,ratioSizeTimerY*rY);
		g.setColor(Color.white);
		s = "todo : timer";
		//		s = ""+Utils.displayTime((int) ((System.currentTimeMillis()-Game.gameSystem.startTime)/1000));
		g.drawString(s, rX/2-GraphicElements.font_main.getWidth(s)/2f, yCentral+2*ratioSizeTimerY*rY/3f-GraphicElements.font_main.getHeight(s)/2f);

		// timer kill
		Team gt = player.getGameTeam();
		float opacity = 255f;
		float centerx = 70, centery = 70;
		float r = 25f;
		if(gt.nbKill>0){
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
			g.setColor(new Color(gt.color.r,gt.color.g,gt.color.b,opacity));
			float startAngle = 270f;
			float sizeAngle = (float)(1f*gt.timerKill*(360f)/gt.timerMaxKill);
			g.fillArc(centerx-r-8f, centery+offsetYkillingSpree-r-8f, 2*r+16f, 2*r+16f, startAngle, startAngle+sizeAngle);
			g.setColor(new Color(0f,0f,0f,opacity));
			g.fillOval(centerx-r, centery+offsetYkillingSpree-r, 2*r, 2*r);
			g.setColor(new Color(1f,1f,1f,opacity));
			g.drawString(""+gt.nbKill, centerx-GraphicElements.font_main.getWidth(""+gt.nbKill)/2, centery+offsetYkillingSpree-GraphicElements.font_main.getHeight(""+gt.nbKill)/2);
		} else {
			offsetYkillingSpree = -150f;
		}

	}

	public void drawMiniMap(Graphics g, Camera camera){
		this.offsetDrawX = Math.max(0, Math.min(sizeXMiniMap+10, -sizeXMiniMap*(plateau.round-debutGlissade-dureeGlissade)/dureeGlissade));
		Utils.drawNiceRect(g,  player.getGameTeam().color,startX2MiniMap+offsetDrawX-3, startY2MiniMap-3, sizeXMiniMap+9, sizeYMiniMap+9);
		g.setColor(Color.black);
		g.fillRect(this.startX2MiniMap+offsetDrawX, this.startY2MiniMap, this.sizeXMiniMap, this.sizeYMiniMap);
		// Find the high left corner
		float hlx = Math.max(startXMiniMap,startXMiniMap+ratioWidthMiniMap*camera.Xcam);
		float hly = Math.max(startYMiniMap,startYMiniMap+ratioHeightMiniMap*camera.Ycam);
		float brx = Math.min(startXMiniMap+widthMiniMap,startXMiniMap+ratioWidthMiniMap*(camera.Xcam+Game.resX));
		float bry = Math.min(startYMiniMap+heightMiniMap,startYMiniMap+ratioHeightMiniMap*(camera.Ycam+Game.resY));
		// Find the bottom right corner

		// Draw background
		g.setColor(new Color(0.1f,0.4f,0.1f));
		g.drawImage(Images.get("islandTexture"),startXMiniMap+offsetDrawX, startYMiniMap, startXMiniMap+offsetDrawX+widthMiniMap, startYMiniMap+heightMiniMap,0,0,Images.get("islandTexture").getWidth(),Images.get("islandTexture").getHeight());
		for(NaturalObjet q : plateau.naturalObjets){
			g.setColor(Color.green);
			g.fillRect(startXMiniMap+offsetDrawX+ratioWidthMiniMap*q.x-ratioWidthMiniMap*q.sizeX/2f, startYMiniMap+ratioHeightMiniMap*q.y-ratioHeightMiniMap*q.sizeY/2f,ratioWidthMiniMap*q.sizeX , ratioHeightMiniMap*q.sizeY);
		}
		// Draw units on camera 
		g.setAntiAlias(true);
		for(Character c : plateau.characters){		
			if(c.getTeam().id==2){
				if(plateau.isVisibleByTeam(player.getTeam(), c)){
					g.setColor(Colors.team2);
					float r = c.collisionBox.getBoundingCircleRadius();
					g.fillOval(startXMiniMap+offsetDrawX+ratioWidthMiniMap*c.x-ratioWidthMiniMap*r, startYMiniMap+ratioHeightMiniMap*c.y-ratioHeightMiniMap*r, 2f*ratioWidthMiniMap*r, 2f*ratioHeightMiniMap*r);
				}
			}
			else if(c.getTeam().id==1){
				if(plateau.isVisibleByTeam(player.getTeam(), c)){
					g.setColor(Colors.team1);
					float r = c.collisionBox.getBoundingCircleRadius();
					g.fillOval(startXMiniMap+offsetDrawX+ratioWidthMiniMap*c.x-ratioWidthMiniMap*r, startYMiniMap+ratioHeightMiniMap*c.y-ratioHeightMiniMap*r, 2f*ratioWidthMiniMap*r, 2f*ratioHeightMiniMap*r);
				}
			}
		}


		for(Bonus c : plateau.bonus){
			if(c.getTeam().id==0){
				g.setColor(Colors.team0);

			}
			if(c.getTeam().id==2){
				if(plateau.isVisibleByTeam(player.getTeam(), c)){
					g.setColor(Colors.team2);
				} else {
					g.setColor(Colors.team0);

				}
			}
			else if(c.getTeam().id==1){
				if(plateau.isVisibleByTeam(player.getTeam(), c)){
					g.setColor(Colors.team1);
				} else {
					g.setColor(Colors.team0);

				}
			}
			g.fillOval(startXMiniMap+offsetDrawX+ratioWidthMiniMap*(c.x-c.getAttribut(Attributs.size)/2f), 
					startYMiniMap+ratioHeightMiniMap*(c.y-c.getAttribut(Attributs.size)/2f), 
					ratioWidthMiniMap*c.getAttribut(Attributs.size), 
					ratioHeightMiniMap*c.getAttribut(Attributs.size));
		}
		g.setAntiAlias(false);
		for(Building c : plateau.buildings){
			if(c.getTeam().id==0){
				g.setColor(Colors.team0);

			}
			if(c.getTeam().id==2){
				if(plateau.isVisibleByTeam(player.getTeam(), c) || Debug.debugFog){
					g.setColor(Colors.team2);
				} else {
					g.setColor(Colors.team0);

				}
			}
			else if(c.getTeam().id==1){
				if(plateau.isVisibleByTeam(player.getTeam(), c) || Debug.debugFog){
					g.setColor(Colors.team1);
				} else {
					g.setColor(Colors.team0);

				}
			}
			g.fillRect(startXMiniMap+offsetDrawX+ratioWidthMiniMap*c.x-ratioWidthMiniMap*c.getAttribut(Attributs.sizeX)/2f, startYMiniMap+ratioHeightMiniMap*c.y-ratioHeightMiniMap*c.getAttribut(Attributs.sizeY)/2f, ratioWidthMiniMap*c.getAttribut(Attributs.sizeX), ratioHeightMiniMap*c.getAttribut(Attributs.sizeY));

			if(c.constructionPoints<c.getAttribut(Attributs.maxLifepoints) && (plateau.isVisibleByTeam(player.getTeam(), c) || Debug.debugFog)){
				float ratio = c.constructionPoints/c.getAttribut(Attributs.maxLifepoints); 
				if(c.potentialTeam==1){
					g.setColor(Colors.team1);
				}
				else if(c.potentialTeam==2){
					g.setColor(Colors.team2);
				}
				g.fillRect(startXMiniMap+offsetDrawX+ratioWidthMiniMap*c.x-ratioWidthMiniMap*c.getAttribut(Attributs.sizeX)/2f, startYMiniMap+ratioHeightMiniMap*c.y-ratioHeightMiniMap*c.getAttribut(Attributs.sizeY)/2f, ratio*(ratioWidthMiniMap*c.getAttribut(Attributs.sizeX)), ratioHeightMiniMap*c.getAttribut(Attributs.sizeY));
			}
		}

		// Draw rect of camera 
		g.setColor(Color.white);
		g.drawRect(hlx+offsetDrawX,hly,brx-hlx,bry-hly );
	}



	//////
	// Utils
	//////

	public Float getAttribut(ObjetsList o, Attributs a){
		return player.getGameTeam().data.getAttribut(o, a);
	}

	public String getAttributString(ObjetsList o, Attributs a){
		return player.getGameTeam().data.getAttributString(o, a);
	}

	public boolean isMouseOnActionBar(float xMouse, float yMouse){
		return xMouse > startXActionBar && xMouse < startXActionBar + 2*sizeXActionBar
				&& yMouse > startYActionBar && yMouse < startYActionBar + sizeYActionBar;
	}

	public boolean isMouseOnTopBar(float xMouse, float yMouse){
		return xMouse > (1-ratioSizeTimerX)*Game.resX/2-2*ratioSizeGoldX*Game.resX 
				&& xMouse < (1+ratioSizeTimerX)*Game.resX/2+2*ratioSizeGoldX*Game.resX
				&& yMouse >0 && yMouse < ratioSizeTimerY*Game.resY;
	}

	public boolean isMouseOnMiniMap(float xMouse, float yMouse){
		return xMouse > startXMiniMap + offsetDrawX && xMouse < startXMiniMap+ widthMiniMap
				&& yMouse > startYMiniMap && yMouse < startYMiniMap + heightMiniMap;
	}

	///////
	// Icone
	///////

	public class Icon{
		private float x, y, sizeX, sizeY;
		private String image;
		private Vector<String> texte;
		private int foodCost, popCost;
		private int quartdecran;
		private float startXBulle, startYBulle, sizeXBulle, sizeYBulle;

		private static final float ratioSizeX=1/4f;
		private static final float ratioSizeY=1/8f;

		public boolean isMouseOnIt;

		private Image imagetemp;

		public Icon(float x, float y, float sizeX, float sizeY, String image, Vector<String> texte, int foodCost, int popCost){
			this.x = x;
			this.y = y;
			this.sizeX = sizeX;
			this.sizeY = sizeY;
			this.image = image;
			this.texte = texte;
			this.foodCost = foodCost;
			this.popCost = popCost;
			if(x<Game.resX/2){
				if(y<Game.resY/2){
					quartdecran = 7;
				} else {
					quartdecran = 1;					
				}
			} else {
				if(y<Game.resY/2){
					quartdecran = 9;
				} else {
					quartdecran = 3;					
				}
			}
			sizeXBulle = ratioSizeX*Game.resX;
			sizeYBulle = ratioSizeY*Game.resY;
			startXBulle = x-(quartdecran%6>1?sizeXBulle:0);
			startYBulle = y-(quartdecran/6>1?0:sizeYBulle);
		}

		public void changeXY(float x, float y){
			this.x = x;
			this.y = y;
			this.startXBulle = x-(quartdecran%6>1?sizeXBulle:0);
			this.startYBulle = y-(quartdecran/6>1?sizeYBulle:0);
		}

		public void update(float xMouse, float yMouse){
			isMouseOnIt = xMouse>x-sizeX/2 && xMouse<x+sizeX/2 && yMouse>y-sizeY/2 && yMouse<y+sizeY/2;
		}

		public void draw(Graphics g){
			if(isMouseOnIt){
				g.setColor(Color.darkGray);
			} else {
				g.setColor(Color.white);
			}
			g.fillRect(x-sizeX/2, y-sizeY/2, sizeX, sizeY);
			imagetemp = Images.get(image);
			g.drawImage(imagetemp, x-sizeX/2, y-sizeY/2, x+sizeX/2, y+sizeY/2, 
					0, 0, imagetemp.getWidth(), imagetemp.getHeight());


		}

		public void drawAfter(Graphics g){
			if(isMouseOnIt){
				Utils.drawNiceRect(g,  player.getGameTeam().color, startXBulle, startYBulle, sizeXBulle, sizeYBulle);
				g.setColor(Color.darkGray);
				g.fillRect(startXBulle+5, startYBulle+5, sizeYBulle-10, sizeYBulle-10);
				g.drawImage(imagetemp, startXBulle+10, startYBulle+10, startXBulle+sizeYBulle-10, startYBulle+sizeYBulle-10, 
						0, 0, imagetemp.getWidth(), imagetemp.getHeight());
				g.setColor(Color.white);
				int i=0;
				for(String s : this.texte){
					g.drawString(s, startXBulle+sizeYBulle+10, startYBulle+5+i*g.getFont().getHeight("Hj"));
					i+=1;
				}
			}
		}
	}
}
