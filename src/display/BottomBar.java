package display;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import data.Attributs;
import main.Main;
import model.Building;
import model.Character;
import model.Game;
import model.Objet;
import spells.Spell;
import utils.ObjetsList;
import utils.Utils;

public class BottomBar extends Bar {

	public int resX,resY;
	public MinimapInterface minimap;

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
	
	// ressource bar
	public float startXRessources = 0;
	public float sizeXRessources = this.ratioBarVertX*Game.g.resX;
	public float sizeYRessources = 5*this.ratioBarVertX*Game.g.resX;
	public float startYRessources= Game.g.resY - sizeYActionBar - this.sizeYSelectionBar;
	Image imageGold = Game.g.images.get("imagegolddisplayressources");
	Image imageFood = Game.g.images.get("imagefooddisplayressources");
	Image imageSpecial = Game.g.images.get("imageSpecial");
	Image imagePop = Game.g.images.get("imagePop");
	Image imageTimer;
	private int gold, food;

	public float ratioSizeGoldX = 1/13f;
	public float ratioSizeTimerX = 1/12f;
	public float ratioSizeGoldY = 1/22f;
	public float ratioSizeTimerY = 1/16f;
	
	private float debutC = Game.nbRoundInit/4;
	private float debut1 = 3*Game.nbRoundInit/8;
	private float debut2 = Game.nbRoundInit/2;
	private float dureeDescente = Game.nbRoundInit/8;


	public BottomBar(){

		this.update(resX, resY);
	}

	public void update(int resX, int resY){
		/**
		 * Tres fortement utile et bienvenue
		 */
		this.resX = (int) Game.g.resX;
		this.resY = (int) Game.g.resY;
		this.sizeX = resX;
		this.sizeY = Game.g.relativeHeightBottomBar*resY;
		this.x = 0;
		this.y = (1f-Game.g.relativeHeightBottomBar)*resY;

		this.minimap = new MinimapInterface(this);
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
		

		// MINIMAP :
		minimap.draw(g);

		//spell.draw(g);



		return g;
	}

	public void drawSelectionInterface(Graphics g){
		
		float sizeXBar;
		float x = 0;
		if(Game.g.round<Game.nbRoundInit)
			startXSelectionBar = Math.max(-sizeX-10, Math.min(0, sizeX*(Game.g.round-debut-duree)/duree));

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
								startXSelectionBar+sizeX-5, startYSelectionBar + sizeYSelectionBar-5,0,0,512,512);
						g.setColor(Color.white);
						String s = this.player.getGameTeam().data.getAttributString(q, Attributs.printName);
						Float prodTime = this.player.getGameTeam().data.getAttribut(q, Attributs.prodTime);
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
						g.drawImage(icone,this.x+this.sizeXSelectionBar+5+(sVB)*(compteur-1), 
								Game.g.resY-sVB+3f, 
								this.x+this.sizeXSelectionBar+(sVB)*(compteur), 
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
			////			Utils.drawNiceRect(g, game.currentPlayer.getGameTeam().color, startX+sizeX-4, parent.p.g.resY-sVB, 5*(sVB+2), sVB+4);
			//			Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color, startX-4, startY, sizeX+4, sizeY+4);
			//			if(b.getQueueTechnologie()!=null){
			//				Image icone = Game.g.images.get(b.getQueueTechnologie().getIcon());
			//				//Show icons
			//				//Show production bar
			//				g.drawImage(icone,startX+this.sizeX/4, startY+this.sizeY/4,startX+sizeX-5, startY + sizeY-5,0,0,512,512);
			//				g.setColor(Color.white);
			//				String s = b.getQueueTechnologie().getName();
			//
			//				g.drawString(s, startX+sizeX/2-Game.g.font.getWidth(s)/2f, startY+sizeY/8f-Game.g.font.getHeight(s)/2f);
			//				g.fillRect(startX+this.sizeX/16, startY+this.sizeY/4 +10f, sizeX/8f,3*sizeY/4-20f);
			//				g.setColor(Color.gray);
			//				g.fillRect(startX+this.sizeX/16, startY+this.sizeY/4 +10f, sizeX/8f,3*sizeY/4-20f);
			//				g.setColor(Game.g.currentPlayer.getGameTeam().color);
			//				g.fillRect(startX+this.sizeX/16, startY+this.sizeY/4+10f+b.charge*(3*sizeY/4-20f)/b.getAttribut(b.getQueueTechnologie().objet, Attributs.foodCost), sizeX/8f,3*sizeY/4-20f-b.charge*(3*sizeY/4-20)/b.getAttribut(b.getQueueTechnologie().objet, Attributs.foodCost));
			//			} else {
			//				g.setColor(Color.white);
			//				String s = b.getAttributString(Attributs.printName);
			//				g.drawString(s, startX+sizeX/2-Game.g.font.getWidth(s)/2f, startY+sizeY/8f-Game.g.font.getHeight(s)/2f);
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
		x = 0;
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
				g.drawImage(Game.g.images.get("icon"+ul.get(i)), x+2f, yActionBar+2f + ratio*i*sizeYActionBar, x-5f+sizeXActionBar, yActionBar-5f+ratio*i*sizeYActionBar, 0, 0, 512,512);
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
		
		if(gold != Game.g.currentPlayer.getGameTeam().gold)
			gold += (Game.g.currentPlayer.getGameTeam().gold-gold)/5+Math.signum(Game.g.currentPlayer.getGameTeam().gold-gold);
		if(food != Game.g.currentPlayer.getGameTeam().food)
			food += (Game.g.currentPlayer.getGameTeam().food-food)/5+Math.signum(Game.g.currentPlayer.getGameTeam().food-food);
		

		// pop
		Utils.drawNiceRect(g,Game.g.currentPlayer.getGameTeam().color,(1-ratioSizeTimerX)*rX/2-2*ratioSizeGoldX*rX,y2,ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		s = ""+Game.g.currentPlayer.getGameTeam().pop + "/" + Game.g.currentPlayer.getGameTeam().maxPop;
		if(Game.g.currentPlayer.getGameTeam().pop==Game.g.currentPlayer.getGameTeam().maxPop){
			g.setColor(Color.red);
		}else{
			g.setColor(Color.white);
		}
		g.drawString(s, (1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX-10f-Game.g.font.getWidth(s), y2+ratioSizeGoldY*rY/2f-Game.g.font.getHeight("0")/2-3f);
		g.drawImage(this.imagePop, (1-ratioSizeTimerX)*rX/2-2*ratioSizeGoldX*rX+10, y2+ratioSizeGoldY*rY/2f-3-this.imageFood.getHeight()/2);

		// food
		Utils.drawNiceRect(g,Game.g.currentPlayer.getGameTeam().color,(1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX,y1,ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		s = ""+food;
		g.setColor(Color.white);
		g.drawString(s, (1-ratioSizeTimerX)*rX/2-10f-Game.g.font.getWidth(s), y1+ratioSizeGoldY*rY/2f-Game.g.font.getHeight("0")/2-3f);
		g.drawImage(this.imageFood, (1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX+10, y1+ratioSizeGoldY*rY/2f-3-this.imageFood.getHeight()/2);

		// faith
		Utils.drawNiceRect(g,Game.g.currentPlayer.getGameTeam().color,(1+ratioSizeTimerX)*rX/2+ratioSizeGoldX*rX-4,y2,ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		s = ""+Game.g.currentPlayer.getGameTeam().special;
		g.setColor(Color.white);
		g.drawString(s, (1+ratioSizeTimerX)*rX/2+2*ratioSizeGoldX*rX-10f-Game.g.font.getWidth(s), y2+ratioSizeGoldY*rY/2f-Game.g.font.getHeight("0")/2-3f);
		g.drawImage(this.imageSpecial, (1+ratioSizeTimerX)*rX/2+10+ratioSizeGoldX*rX, y2+ratioSizeGoldY*rY/2f-3-this.imageGold.getHeight()/2);

		// gold
		Utils.drawNiceRect(g,Game.g.currentPlayer.getGameTeam().color,(1+ratioSizeTimerX)*rX/2-4,y1,ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		s = ""+gold;
		g.setColor(Color.white);
		g.drawString(s, (1+ratioSizeTimerX)*rX/2+ratioSizeGoldX*rX-10f-Game.g.font.getWidth(s), y1+ratioSizeGoldY*rY/2f-Game.g.font.getHeight("0")/2-3f);
		g.drawImage(this.imageGold, (1+ratioSizeTimerX)*rX/2+10, y1+ratioSizeGoldY*rY/2f-3-this.imageGold.getHeight()/2);

		// timer
		Utils.drawNiceRect(g,Game.g.currentPlayer.getGameTeam().color,(1-ratioSizeTimerX)*rX/2,yCentral,ratioSizeTimerX*rX,ratioSizeTimerY*rY);
		g.setColor(Color.white);
		if(Game.g.startTime+2000/Main.framerate>System.currentTimeMillis()){
			s = ""+Utils.displayTime((int) ((System.currentTimeMillis()-Game.g.startTime)/1000));
		} else {
			s = ""+Game.g.plateau.getCurrentAct().getDisplayName();
			g.drawString(s, rX/2-Game.g.font.getWidth(s)/2f, yCentral+ratioSizeTimerY*rY/3f-Game.g.font.getHeight(s)/2f);
			s = ""+Utils.displayTime((int) Game.g.plateau.getCurrentActTime());
		}
		g.drawString(s, rX/2-Game.g.font.getWidth(s)/2f, yCentral+2*ratioSizeTimerY*rY/3f-Game.g.font.getHeight(s)/2f);


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
}
