package display;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import bonus.Bonus;
import data.Attributs;
import madness.ActCard;
import madness.ActRule;
import main.Main;
import model.Building;
import model.Character;
import model.Colors;
import model.Game;
import model.GameTeam;
import model.NaturalObjet;
import model.Objet;
import spells.Spell;
import utils.ObjetsList;
import utils.Utils;

public class BottomBar {

	public int resX,resY;

	public float ratioMinimapX = 1/6f;
	public float ratioSelectionX = 1/8f;
	public float ratioSpellX = 1/12f;
	public float ratioBarVertX = 1/32f;

	public float debut = Game.nbRoundInit/4, duree = debut;

	// selection bar
	public float startXSelectionBar = 0;
	public float startYSelectionBar = Game.g.resY-Game.g.resX*this.ratioSelectionX;
	public float sizeXSelectionBar = Game.g.resX*this.ratioSelectionX;
	public float sizeYSelectionBar = sizeXSelectionBar;

	// action bar
	public float startXActionBar = 0;
	public float sizeXActionBar = this.ratioBarVertX*Game.g.resX;
	public float sizeYActionBar = 5*this.ratioBarVertX*Game.g.resX;
	public float startYActionBar = Game.g.resY - sizeYActionBar - this.sizeYSelectionBar;
	public float yActionBar = startYActionBar+sizeYActionBar;
	public boolean mouseOnActionBar;
	public int prodIconNbY = 5;
	public int prodIconNbX = 2;
	public boolean[][] toDrawDescription = new boolean[prodIconNbY][prodIconNbX];

	// top bar
	Image imageGold = Game.g.images.get("imagegolddisplayressources");
	Image imageFood = Game.g.images.get("imagefooddisplayressources");
	Image imageMadness = Game.g.images.get("iconeMadness");
	Image imageWisdom = Game.g.images.get("iconeWisdom");
	Image imagePop = Game.g.images.get("imagePop");
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

	private float debutC = Game.nbRoundInit/4;
	private float debut1 = 3*Game.nbRoundInit/8;
	private float debut2 = Game.nbRoundInit/2;
	private float dureeDescente = Game.nbRoundInit/8;

	// minimap
	public float startXMiniMap = Game.g.resX*(1-ratioMinimapX)+3; 
	public float startX2MiniMap = Game.g.resX*(1-ratioMinimapX)+3;
	public float offsetDrawX;
	public float sizeXMiniMap = Game.g.resX*ratioMinimapX-6, sizeYMiniMap = sizeXMiniMap;
	public float startYMiniMap, startY2MiniMap = Game.g.resY-ratioMinimapX*Game.g.resX+3;
	public float widthMiniMap;
	public float heightMiniMap;
	public float ratioWidthMiniMap;
	public float ratioHeightMiniMap;
	private float debutGlissade = Game.nbRoundInit/4;
	private float dureeGlissade = Game.nbRoundInit/4;

	//card choice
	private float startYCardChoiceBar;
	private float sizeXCardChoiceBar = sizeXActionBar;
	private float sizeYCardChoiceBar;
	public Vector<Icon> cardChoice = new Vector<Icon>();
	
	//killing spree offest
	public float offsetYkillingSpree = -150f;


	public BottomBar(){
		this.resX = (int) Game.g.resX;
		this.resY = (int) Game.g.resY;
		this.updateRatioMiniMap();

	}

	///////
	// Update mathods
	///////

	public void update(float xMouse, float yMouse){
		this.updateActionInterface(xMouse, yMouse);
		this.updateTopInterface(xMouse, yMouse);
		this.updateCardChoiceInterface(xMouse, yMouse);
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

	public void updateCardChoiceInterface(float xMouse, float yMouse){
		for(Icon icon : cardChoice){
			icon.update(xMouse, yMouse);
		}
	}

	public void updateRatioMiniMap(){

		if(Game.g.plateau.maxX>Game.g.plateau.maxY){
			this.widthMiniMap = this.sizeXMiniMap;
			this.heightMiniMap = this.widthMiniMap*Game.g.plateau.maxY/Game.g.plateau.maxX;
			this.startXMiniMap = this.startX2MiniMap;
			this.startYMiniMap = this.startY2MiniMap + (this.sizeYMiniMap-heightMiniMap)/2;
		} else {
			this.heightMiniMap = this.sizeYMiniMap;			
			this.widthMiniMap = this.heightMiniMap*Game.g.plateau.maxX/Game.g.plateau.maxY;
			this.startXMiniMap = this.startX2MiniMap + (this.sizeXMiniMap-widthMiniMap)/2;
			this.startYMiniMap = this.startY2MiniMap;
		}
		ratioWidthMiniMap = widthMiniMap/Game.g.plateau.maxX;
		ratioHeightMiniMap = heightMiniMap/Game.g.plateau.maxY;
	}

	///////
	// Draw mathods
	///////

	public Graphics draw(Graphics g){
		// Draw Background :


		// Draw image according to size

		//g.drawImage(this.background,x,y-6f);

		// ACTIONS, Spells  and production
		this.drawActionInterface(g);
		this.drawSelectionInterface(g);
		this.drawTopInterface(g);
		this.drawCardChoices(g);
		this.drawMiniMap(g);

		//spell.draw(g);



		return g;
	}

	public void drawSelectionInterface(Graphics g){

		float sizeXBar;
		float x = 0;
		if(Game.g.round<Game.nbRoundInit)
			startXSelectionBar = Math.max(-Game.g.resX-10, Math.min(0, Game.g.resX*(Game.g.round-debut-duree)/duree));

		// variable de travail sizeVerticalBar
		g.setLineWidth(1f);
		float sVB = this.ratioBarVertX*Game.g.resX;


		// Draw building state
		if(Game.g.currentPlayer.selection.size()>0 && Game.g.currentPlayer.selection.get(0) instanceof Building ){

			Building b = (Building) Game.g.currentPlayer.selection.get(0);

			sizeXBar = (Math.min(4,b.getQueue().size()+1))*(sVB+2)+3;
			Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color, startXSelectionBar+sizeXSelectionBar-4, Game.g.resY-sVB, sizeXBar, sVB+4);
			Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color, startXSelectionBar-4, startYSelectionBar, sizeXSelectionBar+4, sizeYSelectionBar+4);

			int compteur = 0;
			if(b.getQueue().size()>0){
				for(ObjetsList q : b.getQueue()){
					Image icone = Game.g.images.get("icon"+q.name());
					if(compteur ==0){
						//Show icons
						//Show production bar
						g.drawImage(icone,startXSelectionBar+this.sizeXSelectionBar/4, 
								startYSelectionBar+this.sizeYSelectionBar/4,
								startXSelectionBar+sizeXSelectionBar-5, startYSelectionBar + sizeYSelectionBar-5,0,0,512,512);
						g.setColor(Color.white);
						String s = Game.g.currentPlayer.getGameTeam().data.getAttributString(q, Attributs.printName);
						Float prodTime = Game.g.currentPlayer.getGameTeam().data.getAttribut(q, Attributs.prodTime);
						g.drawString(s, startXSelectionBar+sizeXSelectionBar/2-Game.g.font.getWidth(s)/2f, 
								startYSelectionBar+sizeYSelectionBar/8f-Game.g.font.getHeight(s)/2f);
						g.fillRect(startXSelectionBar+this.sizeXSelectionBar/16, 
								startYSelectionBar+this.sizeYSelectionBar/4 +10f, sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f);
						g.setColor(Color.gray);
						g.fillRect(startXSelectionBar+this.sizeXSelectionBar/16, 
								startYSelectionBar+this.sizeYSelectionBar/4 +10f, sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f);
						g.setColor(Game.g.currentPlayer.getGameTeam().color);
						g.fillRect(startXSelectionBar+this.sizeXSelectionBar/16, 
								startYSelectionBar+this.sizeYSelectionBar/4+10f+b.charge*(3*sizeYSelectionBar/4-20f)/prodTime, 
								sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f-b.charge*(3*sizeYSelectionBar/4-20)/prodTime);
					}
					else{
						g.drawImage(icone,this.sizeXSelectionBar+5+(sVB)*(compteur-1), 
								Game.g.resY-sVB+3f, 
								this.sizeXSelectionBar+(sVB)*(compteur), 
								Game.g.resY-1,0f,0f,512f,512f);
					}
					compteur ++;
				}
			} else {
				g.setColor(Color.white);
				String s = b.getAttributString(Attributs.printName);
				g.drawString(s, startXSelectionBar+sizeXSelectionBar/2-Game.g.font.getWidth(s)/2f, 
						startYSelectionBar+sizeYSelectionBar/8f-Game.g.font.getHeight(s)/2f);
			}
			//		} else if(Game.g.currentPlayer.selection.size()>0 && Game.g.currentPlayer.selection.get(0) instanceof Building  ){
			//			Building b = (Building) Game.g.currentPlayer.selection.get(0);
			////			this.sizeXBar = (b.queue.size()+1)*(sVB+2);
			////			Utils.drawNiceRect(g, game.currentPlayer.getGameTeam().color, startX+Game.g.resX-4, parent.p.g.resY-sVB, 5*(sVB+2), sVB+4);
			//			Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color, startX-4, startY, Game.g.resX+4, sizeY+4);
			//			if(b.getQueueTechnologie()!=null){
			//				Image icone = Game.g.images.get(b.getQueueTechnologie().getIcon());
			//				//Show icons
			//				//Show production bar
			//				g.drawImage(icone,startX+this.Game.g.resX/4, startY+this.sizeY/4,startX+Game.g.resX-5, startY + sizeY-5,0,0,512,512);
			//				g.setColor(Color.white);
			//				String s = b.getQueueTechnologie().getName();
			//
			//				g.drawString(s, startX+Game.g.resX/2-Game.g.font.getWidth(s)/2f, startY+sizeY/8f-Game.g.font.getHeight(s)/2f);
			//				g.fillRect(startX+this.Game.g.resX/16, startY+this.sizeY/4 +10f, Game.g.resX/8f,3*sizeY/4-20f);
			//				g.setColor(Color.gray);
			//				g.fillRect(startX+this.Game.g.resX/16, startY+this.sizeY/4 +10f, Game.g.resX/8f,3*sizeY/4-20f);
			//				g.setColor(Game.g.currentPlayer.getGameTeam().color);
			//				g.fillRect(startX+this.Game.g.resX/16, startY+this.sizeY/4+10f+b.charge*(3*sizeY/4-20f)/b.getAttribut(b.getQueueTechnologie().objet, Attributs.foodCost), Game.g.resX/8f,3*sizeY/4-20f-b.charge*(3*sizeY/4-20)/b.getAttribut(b.getQueueTechnologie().objet, Attributs.foodCost));
			//			} else {
			//				g.setColor(Color.white);
			//				String s = b.getAttributString(Attributs.printName);
			//				g.drawString(s, startX+Game.g.resX/2-Game.g.font.getWidth(s)/2f, startY+sizeY/8f-Game.g.font.getHeight(s)/2f);
			//			}
		}else if(Game.g.currentPlayer.selection.size()>0 && Game.g.currentPlayer.selection.get(0) instanceof Character ){

			Character c;
			int compteur = 0;
			int nb = Game.g.currentPlayer.selection.size()-1;

			sizeXBar = (Math.min(nb+1, 5))*(sVB+2)+2;
			Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color, 
					startXSelectionBar+sizeXSelectionBar-4, Game.g.resY-sVB, sizeXBar, sVB+4);
			Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color, 
					startXSelectionBar-4, startYSelectionBar, sizeXSelectionBar+4, sizeYSelectionBar+4);
			for(Objet a : Game.g.currentPlayer.selection){
				c = (Character) a;
				Image icone = Game.g.images.get(c.name+"blue");
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
					g.drawString(s, startXSelectionBar+sizeXSelectionBar/2-Game.g.font.getWidth(s)/2f, 
							startYSelectionBar+sizeYSelectionBar/8f-Game.g.font.getHeight(s)/2f);
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
						y1 = (int) (Game.g.resY-sVB+3f);
						x2 = (int) (x1+sVB);
						y2 = (int) (y1+sVB);						
					} else {
						x1 = (int) (x+sizeXSelectionBar+5+(sVB)*(compteur-1));
						y1 = (int) (Game.g.resY-sVB+3f);
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
			Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color, 
					startXSelectionBar-4, startYSelectionBar, sizeXSelectionBar+4, sizeYSelectionBar+4);
		}


	}

	public void drawActionInterface(Graphics g){
		float startY2;
		Image imageGold ;
		Image imageFood;
		float debut = Game.nbRoundInit/4, duree = debut;
		float offset;
		// Production Bar
		float ratio =1f/prodIconNbY;

		offset = sizeXSelectionBar;
		float x = 0;
		startYActionBar = Game.g.resY - sizeYActionBar - this.sizeYSelectionBar;
		startY2 = Game.g.resY - sizeYSelectionBar;

		//		for(int i= 0 ; i<prodIconNbY; i++){
		//			for(int j= 0 ; j<prodIconNbX; j++){
		//				toDrawDescription[i][j] = false;
		//			}
		//		}

		imageGold = Game.g.images.get("imagegolddisplayressources");
		imageFood = Game.g.images.get("imagefooddisplayressources");


		// Draw the potential actions
		// Draw Separation (1/3 1/3 1/3) : 

		if(Game.g.round<Game.nbRoundInit)
			x = Math.max(-offset-10, Math.min(0, offset*(Game.g.round-debut-duree)/duree));
		else
			x = 0;
		g.setLineWidth(1f);
		if(mouseOnActionBar && yActionBar>startYActionBar)
			yActionBar = startYActionBar+(yActionBar-startYActionBar)/5;
		if(!mouseOnActionBar && yActionBar<startY2)
			yActionBar = startY2+(yActionBar-startY2)/5;

		Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color, x-4, yActionBar-5, 2*sizeXActionBar+4, sizeYActionBar+9);
		g.setColor(Color.darkGray);
		for(int i=0; i<5; i++){
			g.setColor(Color.darkGray);
			g.fillRect(x+2f, yActionBar+2f + i*sizeXActionBar, -7f+sizeXActionBar, -7f+sizeXActionBar);
			g.fillRect(x+2f+sizeXActionBar, yActionBar+2f + i*sizeXActionBar, -7f+sizeXActionBar, -7f+sizeXActionBar);
		}
		g.setColor(Color.white);


		// Draw Production/Effect Bar
		if(Game.g.currentPlayer.selection.size()>0 && Game.g.currentPlayer.selection.get(0) instanceof Building){
			mouseOnActionBar = true;
			Building b =(Building) Game.g.currentPlayer.selection.get(0);
			//Print building capacities
			Vector<ObjetsList> ul = b.getProductionList();
			int limit = Math.min(5, ul.size());
			Font f = g.getFont();
			for(int i=0; i<limit;i++){ 
				g.drawImage(Game.g.images.get("icon"+ul.get(i)), x+2f, yActionBar+2f + ratio*i*sizeYActionBar, x-5f+sizeXActionBar, yActionBar-5f+ratio*i*sizeYActionBar+sizeXActionBar, 0, 0, 512,512);
				g.setColor(Color.white);
				g.setColor(Game.g.currentPlayer.getGameTeam().color);
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
			Vector<ObjetsList> ul2 = b.getTechnologyList();
			limit = Math.min(5, ul2.size());
			for(int i=0; i<limit;i++){
				float goldCost = getAttribut(b.getTechnologyList().get(i),Attributs.goldCost);
				float foodCost = getAttribut(b.getTechnologyList().get(i),Attributs.foodCost);
				float faithCost = getAttribut(b.getTechnologyList().get(i),Attributs.faithCost);
				float prodTime = getAttribut(b.getTechnologyList().get(i),Attributs.prodTime);
				String icon = getAttributString(b.getTechnologyList().get(i),Attributs.nameIcon);
				g.drawImage(Game.g.images.get(icon), x+2f, yActionBar+2f + ratio*i*sizeYActionBar, x-5f+sizeXActionBar, yActionBar-5f+ratio*i*sizeYActionBar+sizeXActionBar, 0, 0, 512,512);
				// CHANGE PUT PRICES
				g.setColor(Game.g.currentPlayer.getGameTeam().color);
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
		else if(Game.g.currentPlayer.selection.size()>0 && Game.g.currentPlayer.selection.get(0) instanceof Character){
			mouseOnActionBar = true;
			Character b =(Character) Game.g.currentPlayer.selection.get(0);
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
					g.setColor(Game.g.currentPlayer.getGameTeam().color);
				}
				if(Game.g.spellCurrent==b.getSpells().get(i).name){
					g.setColor(Color.orange);
				}
				g.drawRect(x+1f, yActionBar+1f + i*sizeXActionBar, -6f+sizeXActionBar, -6f+sizeXActionBar);
				im = Game.g.images.get("spell"+ul.get(i).name);
				g.drawImage(im, x+2f, yActionBar+2f + ratio*i*sizeYActionBar, x-5f+sizeXActionBar, yActionBar-5f+ratio*i*sizeYActionBar+sizeXActionBar, 0, 0, 512,512);
				Color c = Game.g.currentPlayer.getGameTeam().color;
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
		float rX = Game.g.resX;
		float rY = Game.g.resY;
		float offset = ratioSizeTimerY*rY;
		float yCentral = Math.max(-offset-10,Math.min(0, offset*(Game.g.round-debutC-dureeDescente)/dureeDescente));
		offset = ratioSizeGoldY*rY;
		float y1 = Math.max(-offset-10,Math.min(0, offset*(Game.g.round-debut1-dureeDescente)/dureeDescente));
		float y2 = Math.max(-offset-10,Math.min(0, offset*(Game.g.round-debut2-dureeDescente)/dureeDescente));

		if(food != Game.g.currentPlayer.getGameTeam().food)
			food += (Game.g.currentPlayer.getGameTeam().food-food)/5+Math.signum(Game.g.currentPlayer.getGameTeam().food-food);

		// écran de description de l'acte / choix des cartes
		if(Game.g.plateau.getCurrentAct()!=null){
			if(Game.g.currentPlayer.getGameTeam().currentChoices.size() == 0){
				// On dessine l'écran de description de l'acte - il n'y a pas de choix à faire
				Vector<String> v = new Vector<String>();
				v.add(Game.g.plateau.getCurrentAct().getDisplayName());
				for(ActRule rule : Game.g.plateau.getCurrentAct().rules){
					v.add("- "+rule.description);
				}
				if(Game.g.plateau.getCurrentAct().rules.length==0){
					v.add("- Aucune règle spéciale");
				}
				float sizeY = 15+v.size()*g.getFont().getHeight("Jj")*1.3f;
				if(mouseOnTopBar){
					if(startYDescription<ratioSizeGoldY*rY){
						startYDescription += (ratioSizeGoldY*rY-startYDescription)/5f;
					}
				} else {
					if(startYDescription>-sizeY){
						startYDescription -= (sizeY+10+startYDescription)/5f;
					}
				}
				Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color,(1-ratioSizeDescriptionX)*rX/2,startYDescription, 
						ratioSizeDescriptionX*rX, sizeY);
				g.setColor(Color.white);
				int i=0;
				for(String str_temp : v){
					g.drawString(str_temp, (1-ratioSizeDescriptionX)*rX/2+10f, startYDescription+15+i*g.getFont().getHeight("Jj")*1.3f);
					i+=1;
				}
			} else {
				// on dessine l'écran de choix des cartes, il y a un choix à faire
				if(startYDescription<ratioSizeGoldY*rY){
					startYDescription += (ratioSizeGoldY*rY-startYDescription)/5f;
				}
				float sizeY = 15+3f*g.getFont().getHeight("Jj");
				if(iconChoice==null){
					iconChoice = new Vector<Icon>();
					float xIcon, yIcon, sizeXIcon, sizeYIcon;
					Vector<String> texte;
					sizeYIcon = 2f*g.getFont().getHeight("Jj");
					sizeXIcon = sizeYIcon;
					yIcon = startYDescription+15+0.5f*g.getFont().getHeight("Jj")+sizeYIcon/2;
					int i = 0;
					Icon icon;
					for(ActCard card : Game.g.currentPlayer.getGameTeam().currentChoices.get(0)){
						xIcon = Game.g.resX/2
								-(1f*(Game.g.currentPlayer.getGameTeam().currentChoices.get(0).size()-1)*(sizeXIcon+10)/2f+5)
								+i*(sizeXIcon+10);
						icon = new Icon(xIcon,yIcon,sizeXIcon,sizeYIcon,card.getIcon(),null,0,0);
						icon.texte = card.getTexte(icon.sizeXBulle-icon.sizeYBulle, Game.g.font);
						iconChoice.add(icon);
						i+=1;
					}
				} else {
					for(Icon icon : iconChoice){
						icon.changeXY(icon.x, startYDescription+15+0.5f*g.getFont().getHeight("Jj")+icon.sizeY/2);
					}
				}
				Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color,
						(1-ratioSizeChoiceX)*rX/2,startYDescription, 
						ratioSizeChoiceX*rX, sizeY);
				for(Icon icon : iconChoice){
					icon.draw(g);
				}
				for(Icon icon : iconChoice){
					icon.drawAfter(g);
				}

			}
		}
		// pop
		Utils.drawNiceRect(g,Game.g.currentPlayer.getGameTeam().color,(1-ratioSizeTimerX)*rX/2-2*ratioSizeGoldX*rX,y1,ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		s = ""+Game.g.currentPlayer.getGameTeam().getPop() + "/" + Game.g.currentPlayer.getGameTeam().getMaxPop();
		if(Game.g.currentPlayer.getGameTeam().getPop()==Game.g.currentPlayer.getGameTeam().getMaxPop()){
			g.setColor(Color.red);
		}else{
			g.setColor(Color.white);
		}
		g.drawString(s, (1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX-10f-Game.g.font.getWidth(s), y1+ratioSizeGoldY*rY/2f-Game.g.font.getHeight("0")/2-3f);
		g.drawImage(this.imagePop, (1-ratioSizeTimerX)*rX/2-2*ratioSizeGoldX*rX+10, y1+ratioSizeGoldY*rY/2f-3-this.imageFood.getHeight()/2);

		// food
		Utils.drawNiceRect(g,Game.g.currentPlayer.getGameTeam().color,(1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX,y1,ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		s = ""+food;
		g.setColor(Color.white);
		g.drawString(s, (1-ratioSizeTimerX)*rX/2-10f-Game.g.font.getWidth(s), y1+ratioSizeGoldY*rY/2f-Game.g.font.getHeight("0")/2-3f);
		g.drawImage(this.imageFood, (1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX+10, y1+ratioSizeGoldY*rY/2f-3-this.imageFood.getHeight()/2);

		// madness
		Utils.drawNiceRect(g,Game.g.currentPlayer.getGameTeam().color,(1+ratioSizeTimerX)*rX/2-4,y1,2*ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		int madness = Game.g.currentPlayer.getGameTeam().civ.madness;
		g.setColor(Color.white);
		int[] objective = new int[]{};
		Color color = Color.black;
		Color color2 = Color.black;
		Color color3 = Color.black;
		Image image = null;
		float x;
		float sizeXjauge = 1.2f*ratioSizeGoldX*rX;
		float xJauge = (1+ratioSizeTimerX)*rX/2+ratioSizeGoldX*rX-sizeXjauge/2, yJauge = y1+2*ratioSizeGoldY*rY/3f;
		float sizeYjauge = ratioSizeGoldY*rY/4f, sizeYcurseur = ratioSizeGoldY*rY/3f;
		String etat;
		if(madness==0){
			// neutre
			etat = "Etat : Neutre";
			g.drawString(etat, (1+ratioSizeTimerX)*rX/2+ratioSizeGoldX*rX-Game.g.font.getWidth(etat)/2, y1+ratioSizeGoldY*rY/2f-Game.g.font.getHeight("0j")/2-3f);
		} else if(madness>0){
			// madness
			color = new Color(255,79,0);
			color2 = new Color(235,59,0);
			color3 = new Color(255,119,30);
			etat = "Etat : Folie";
			g.drawString(etat, (1+ratioSizeTimerX)*rX/2+ratioSizeGoldX*rX-Game.g.font.getWidth(etat)/2, y1+ratioSizeGoldY*rY/4f-Game.g.font.getHeight("0j")/2-3f);
			objective = Game.g.currentPlayer.getGameTeam().civ.objectiveMadness.objective;
			image = this.imageMadness;
		} else if(madness<0){
			//wisdom
			madness*=-1;
			color = new Color(0,210,255);
			color2 = new Color(0,180,215);
			color3 = new Color(30,240,255);
			etat = "Etat : Sagesse";
			g.drawString(etat, (1+ratioSizeTimerX)*rX/2+ratioSizeGoldX*rX-Game.g.font.getWidth(etat)/2, y1+ratioSizeGoldY*rY/4f-Game.g.font.getHeight("0j")/2-3f);
			objective = Game.g.currentPlayer.getGameTeam().civ.objectiveWisdom.objective;
			image = this.imageWisdom;
		}
		float sizeXcurseur=sizeXjauge/(6*objective.length);
		if(image!=null){
			g.drawImage(image, (1+ratioSizeTimerX)*rX/2+(ratioSizeGoldX*rX-sizeXjauge/2)/2-image.getWidth()/2, y1+2*ratioSizeGoldY*rY/3f-image.getHeight()/2);
		}
		s = ""+madness;
		g.setColor(Color.white);
		try{
			//TODO : acts
//			g.drawString(s+" / "+objective[Game.g.plateau.currentAct], (1+ratioSizeTimerX)*rX/2+ratioSizeGoldX*rX+sizeXjauge/2+10, yJauge-Game.g.font.getHeight("Hj")/2);
		} catch(Exception e){}
		// affichage de la jauge
		for(int i=-1; i<objective.length; i++){
			if(i>-1){
				x = xJauge+(i)*sizeXjauge/(objective.length);
				g.setColor(color);
				g.fillRect(x, yJauge-sizeYjauge/2, (Math.max(0,Math.min(objective[i], madness)-(i>0 ? objective[i-1] : 0)))*(sizeXjauge/(objective.length))/(objective[i]-(i>0 ? objective[i-1] : 0)), sizeYjauge);
				g.setColor(color2);
				g.fillRect(x, yJauge+sizeYjauge/6, (Math.max(0,Math.min(objective[i], madness)-(i>0 ? objective[i-1] : 0)))*(sizeXjauge/(objective.length))/(objective[i]-(i>0 ? objective[i-1] : 0)), sizeYjauge/3);
				g.setColor(color3);
				g.fillRect(x, yJauge-sizeYjauge/2, (Math.max(0,Math.min(objective[i], madness)-(i>0 ? objective[i-1] : 0)))*(sizeXjauge/(objective.length))/(objective[i]-(i>0 ? objective[i-1] : 0)), sizeYjauge/5);
//				g.setColor(Color.white);
//				g.drawRect(x, yJauge-sizeYjauge/2, (Math.max(0,Math.min(objective[i], madness)-(i>0 ? objective[i-1] : 0)))*(sizeXjauge/(objective.length))/(objective[i]-(i>0 ? objective[i-1] : 0)), sizeYjauge);
			}
		}
		for(int i=-1; i<objective.length; i++){
			g.setColor(madness>0 ? Color.white : Color.gray);
			if(i>-1){
				g.setColor(madness>=objective[i] ? Color.white : Color.gray);
			}
			x = xJauge+(i+1)*sizeXjauge/objective.length;
			g.fillRect(x-sizeXcurseur/2, yJauge-sizeYcurseur/2, sizeXcurseur, sizeYcurseur);
			g.setColor(Color.black);
			g.drawRect(x-sizeXcurseur/2, yJauge-sizeYcurseur/2, sizeXcurseur, sizeYcurseur);
		}
		g.setColor(Color.darkGray);
		g.setLineWidth(3f);
		if(madness>0){
			g.drawRect(xJauge-sizeXcurseur/2, yJauge-sizeYcurseur/2, sizeXjauge+sizeXcurseur, sizeYcurseur);
			g.setLineWidth(1f);
		}


		// timer
		Utils.drawNiceRect(g,Game.g.currentPlayer.getGameTeam().color,(1-ratioSizeTimerX)*rX/2,yCentral,ratioSizeTimerX*rX,ratioSizeTimerY*rY);
		g.setColor(Color.white);
		if(Game.g.plateau.getCurrentAct()==null || Game.g.startTime+2000/Main.framerate>System.currentTimeMillis()){
			s = ""+Utils.displayTime((int) ((System.currentTimeMillis()-Game.g.startTime)/1000));
		} else {
			try{
				s = ""+Game.g.plateau.getCurrentAct().getDisplayName();
				g.drawString(s, rX/2-Game.g.font.getWidth(s)/2f, yCentral+ratioSizeTimerY*rY/3f-Game.g.font.getHeight(s)/2f);
				s = ""+Utils.displayTime((int) Game.g.plateau.getCurrentActTime());
				if(Game.g.plateau.getCurrentActTime()<15){
					g.setColor(Color.red);
				}
			}catch(Exception e){

			}
		}
		g.drawString(s, rX/2-Game.g.font.getWidth(s)/2f, yCentral+2*ratioSizeTimerY*rY/3f-Game.g.font.getHeight(s)/2f);

		// timer kill
		GameTeam gt = Game.g.currentPlayer.getGameTeam();
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
			System.out.println("mythe" + gt.nbKill+ "  "+offsetYkillingSpree);
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
			g.drawString(""+gt.nbKill, centerx-Game.g.font.getWidth(""+gt.nbKill)/2, centery+offsetYkillingSpree-Game.g.font.getHeight(""+gt.nbKill)/2);
		} else {
			offsetYkillingSpree = -150f;
		}
		
	}

	public void drawMiniMap(Graphics g){
		this.offsetDrawX = Math.max(0, Math.min(sizeXMiniMap+10, -sizeXMiniMap*(Game.g.round-debutGlissade-dureeGlissade)/dureeGlissade));
		Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color,startX2MiniMap+offsetDrawX-3, startY2MiniMap-3, sizeXMiniMap+9, sizeYMiniMap+9);
		g.setColor(Color.black);
		g.fillRect(this.startX2MiniMap+offsetDrawX, this.startY2MiniMap, this.sizeXMiniMap, this.sizeYMiniMap);
		// Find the high left corner
		float hlx = Math.max(startXMiniMap,startXMiniMap+ratioWidthMiniMap*Game.g.Xcam);
		float hly = Math.max(startYMiniMap,startYMiniMap+ratioHeightMiniMap*Game.g.Ycam);
		float brx = Math.min(startXMiniMap+widthMiniMap,startXMiniMap+ratioWidthMiniMap*(Game.g.Xcam+Game.g.resX));
		float bry = Math.min(startYMiniMap+heightMiniMap,startYMiniMap+ratioHeightMiniMap*(Game.g.Ycam+Game.g.resY));
		// Find the bottom right corner

		// Draw background
		g.setColor(new Color(0.1f,0.4f,0.1f));
		g.drawImage(Game.g.images.get("islandTexture"),startXMiniMap+offsetDrawX, startYMiniMap, startXMiniMap+offsetDrawX+widthMiniMap, startYMiniMap+heightMiniMap,0,0,Game.g.images.get("islandTexture").getWidth(),Game.g.images.get("islandTexture").getHeight());
		for(NaturalObjet q : Game.g.plateau.naturalObjets){
			g.setColor(Color.green);
			g.fillRect(startXMiniMap+offsetDrawX+ratioWidthMiniMap*q.x-ratioWidthMiniMap*q.sizeX/2f, startYMiniMap+ratioHeightMiniMap*q.y-ratioHeightMiniMap*q.sizeY/2f,ratioWidthMiniMap*q.sizeX , ratioHeightMiniMap*q.sizeY);
		}
		// Draw units on camera 
		g.setAntiAlias(true);
		for(Character c : Game.g.plateau.characters){		
			if(c.getTeam()==2){
				if(Game.g.plateau.isVisibleByTeam(Game.g.currentPlayer.getTeam(), c)){
					g.setColor(Colors.team2);
					float r = c.collisionBox.getBoundingCircleRadius();
					g.fillOval(startXMiniMap+offsetDrawX+ratioWidthMiniMap*c.x-ratioWidthMiniMap*r, startYMiniMap+ratioHeightMiniMap*c.y-ratioHeightMiniMap*r, 2f*ratioWidthMiniMap*r, 2f*ratioHeightMiniMap*r);
				}
			}
			else if(c.getTeam()==1){
				if(Game.g.plateau.isVisibleByTeam(Game.g.currentPlayer.getTeam(), c)){
					g.setColor(Colors.team1);
					float r = c.collisionBox.getBoundingCircleRadius();
					g.fillOval(startXMiniMap+offsetDrawX+ratioWidthMiniMap*c.x-ratioWidthMiniMap*r, startYMiniMap+ratioHeightMiniMap*c.y-ratioHeightMiniMap*r, 2f*ratioWidthMiniMap*r, 2f*ratioHeightMiniMap*r);
				}
			}
		}


		for(Bonus c : Game.g.plateau.bonus){
			if(c.getTeam()==0){
				g.setColor(Colors.team0);

			}
			if(c.getTeam()==2){
				if(Game.g.plateau.isVisibleByTeam(Game.g.currentPlayer.getTeam(), c)){
					g.setColor(Colors.team2);
				} else {
					g.setColor(Colors.team0);

				}
			}
			else if(c.getTeam()==1){
				if(Game.g.plateau.isVisibleByTeam(Game.g.currentPlayer.getTeam(), c)){
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
		for(Building c : Game.g.plateau.buildings){
			if(c.getTeam()==0){
				g.setColor(Colors.team0);

			}
			if(c.getTeam()==2){
				if(Game.g.plateau.isVisibleByTeam(Game.g.currentPlayer.getTeam(), c) || Game.debugFog){
					g.setColor(Colors.team2);
				} else {
					g.setColor(Colors.team0);

				}
			}
			else if(c.getTeam()==1){
				if(Game.g.plateau.isVisibleByTeam(Game.g.currentPlayer.getTeam(), c) || Game.debugFog){
					g.setColor(Colors.team1);
				} else {
					g.setColor(Colors.team0);

				}
			}
			g.fillRect(startXMiniMap+offsetDrawX+ratioWidthMiniMap*c.x-ratioWidthMiniMap*c.getAttribut(Attributs.sizeX)/2f, startYMiniMap+ratioHeightMiniMap*c.y-ratioHeightMiniMap*c.getAttribut(Attributs.sizeY)/2f, ratioWidthMiniMap*c.getAttribut(Attributs.sizeX), ratioHeightMiniMap*c.getAttribut(Attributs.sizeY));

			if(c.constructionPoints<c.getAttribut(Attributs.maxLifepoints) && (Game.g.plateau.isVisibleByTeam(Game.g.currentPlayer.getTeam(), c) || Game.debugFog)){
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

	public void drawCardChoices(Graphics g){
		float debut = Game.nbRoundInit/4, duree = debut;
		float offset = sizeXSelectionBar;
		// Production Bar
		float ratio =1f/prodIconNbY;
		float x;
		sizeYCardChoiceBar = Game.g.currentPlayer.getGameTeam().choices.size()*sizeXCardChoiceBar;

		startYCardChoiceBar = Game.g.resY - sizeYMiniMap - sizeYCardChoiceBar-5;

		if(Game.g.round<Game.nbRoundInit)
			x = Game.g.resX+Math.min(10, Math.max(-sizeXCardChoiceBar, 10-offset*(Game.g.round-debut-duree)/duree));
		else
			x = Game.g.resX-sizeXCardChoiceBar;
		g.setLineWidth(1f);
		Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color, x-4, startYCardChoiceBar-5, sizeXCardChoiceBar+4, sizeYCardChoiceBar+9);
		g.setColor(Color.darkGray);
		for(int i=0; i<5; i++){
			g.setColor(Color.darkGray);
			g.fillRect(x+2f, startYCardChoiceBar+2f + i*sizeXCardChoiceBar, -7f+sizeXCardChoiceBar, -7f+sizeXCardChoiceBar);
			g.fillRect(x+2f+sizeXCardChoiceBar, startYCardChoiceBar+2f + i*sizeXCardChoiceBar, -7f+sizeXCardChoiceBar, -7f+sizeXCardChoiceBar);
		}
		g.setColor(Color.white);

		// Draw Production/Effect Bar
		for(Icon icon : this.cardChoice){
			icon.draw(g);
		}
		for(Icon icon : this.cardChoice){
			icon.drawAfter(g);
		}
	}


	//////
	// Utils
	//////

	public Float getAttribut(ObjetsList o, Attributs a){
		return Game.g.currentPlayer.getGameTeam().data.getAttribut(o, a);
	}

	public String getAttributString(ObjetsList o, Attributs a){
		return Game.g.currentPlayer.getGameTeam().data.getAttributString(o, a);
	}

	public boolean isMouseOnActionBar(float xMouse, float yMouse){
		return xMouse > startXActionBar && xMouse < startXActionBar + 2*sizeXActionBar
				&& yMouse > startYActionBar && yMouse < startYActionBar + sizeYActionBar;
	}

	public boolean isMouseOnTopBar(float xMouse, float yMouse){
		return xMouse > (1-ratioSizeTimerX)*Game.g.resX/2-2*ratioSizeGoldX*Game.g.resX 
				&& xMouse < (1+ratioSizeTimerX)*Game.g.resX/2+2*ratioSizeGoldX*Game.g.resX
				&& yMouse >0 && yMouse < ratioSizeTimerY*Game.g.resY;
	}

	public void addCardChoice(ActCard card){
		startYCardChoiceBar = startYCardChoiceBar-sizeXCardChoiceBar;
		Icon icon = new Icon(Game.g.resX-sizeXCardChoiceBar/2, startYCardChoiceBar+sizeXCardChoiceBar/2f, 
				sizeXCardChoiceBar-5f, sizeXCardChoiceBar-5f, card.getIcon(), null, 0, 0);
		icon.texte = card.getTexte(icon.sizeXBulle-icon.sizeYBulle, Game.g.font);
		this.cardChoice.add(icon);		
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
			if(x<Game.g.resX/2){
				if(y<Game.g.resY/2){
					quartdecran = 7;
				} else {
					quartdecran = 1;					
				}
			} else {
				if(y<Game.g.resY/2){
					quartdecran = 9;
				} else {
					quartdecran = 3;					
				}
			}
			sizeXBulle = ratioSizeX*Game.g.resX;
			sizeYBulle = ratioSizeY*Game.g.resY;
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
			imagetemp = Game.g.images.get(image);
			g.drawImage(imagetemp, x-sizeX/2, y-sizeY/2, x+sizeX/2, y+sizeY/2, 
					0, 0, imagetemp.getWidth(), imagetemp.getHeight());


		}

		public void drawAfter(Graphics g){
			if(isMouseOnIt){
				Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color, startXBulle, startYBulle, sizeXBulle, sizeYBulle);
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
