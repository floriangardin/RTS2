package stats;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import control.Player;
import model.Colors;
import model.Game;
import ressources.GraphicElements;
import ressources.Images;
import system.ClassSystem;
import utils.ObjetsList;

public strictfp class StatsSystem extends ClassSystem{

	public static float startX, startY, sizeX, sizeY;

	public StatsSystem(){
		startX = Game.resX/10;
		sizeX = Game.resX*4f/5f;
		startY = Game.resY/6;
		sizeY = Game.resY*2f/3f;
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {

	}

	public static void render(Graphics g){
		g.setLineWidth(1f);
		g.setColor(new Color(86,86,86,186));
		g.fillRect(StatsSystem.startX, StatsSystem.startY, StatsSystem.sizeX, StatsSystem.sizeY);
		g.setColor(Color.white);
		g.drawRect(StatsSystem.startX, StatsSystem.startY, StatsSystem.sizeX, StatsSystem.sizeY);
		// damages
		float sizeXdamage = sizeX/2f;
		float startXdamage = startX+sizeXdamage/8f;
		sizeXdamage = sizeXdamage*3f/4f;
		float sizeYdamage = sizeY;
		float startYdamage = startY + sizeYdamage/16f;
		sizeYdamage = sizeYdamage*7f/8f;
		renderDamage(g, startXdamage, startYdamage, sizeXdamage, sizeYdamage);
		
		// kill / tower count
		float sizeXcounters = sizeX/2f;
		float startXcounters = startX + sizeX - sizeXcounters;
		float sizeYcounters = sizeY;
		float startYcounters = startY;
		renderCounters(g, startXcounters, startYcounters, sizeXcounters, sizeYcounters);
	}
	
	public static void renderDamage(Graphics g, float startXdamage, float startYdamage, float sizeXdamage, float sizeYdamage){
		Vector<ObjetsList> vol = ObjetsList.getUnits();
		vol.addElement(ObjetsList.Tower);
		int nbLine = 2+vol.size();
		g.setColor(Color.white);
		Image icone;
		int imageWidth;
		int imageHeight;
		int xImage, yImage;
		int total;
		String s = "Total";
		ObjetsList ol;
		for(int i=0; i<nbLine; i++){
			g.setLineWidth(1f);
			if(i==0){
				g.drawRect(startXdamage+sizeXdamage/3f, startYdamage+i*sizeYdamage/nbLine, sizeXdamage*2f/3f, sizeYdamage/nbLine);
				for(int j=1; j<3;j++){
					if(Player.team==j){
						g.setColor(Color.white);
					} else {
						g.setColor(Color.black);
					}
					g.fillRect(startXdamage+sizeXdamage/12f+j*sizeXdamage/3f, 
							startYdamage+sizeYdamage/nbLine/4f, 
							sizeXdamage/6f, 
							1*sizeYdamage/nbLine/2f);
					g.setColor(Colors.getTeamColor(j));
					g.fillRect(startXdamage+sizeXdamage/12f+j*sizeXdamage/3f+3f, 
							startYdamage+sizeYdamage/nbLine/4f+3f, 
							sizeXdamage/6f-6f, 
							1*sizeYdamage/nbLine/2f-6f);
				}
			} else if(i<nbLine-1){
				ol = vol.get(i-1);
				if(ol!=ObjetsList.Tower){
					icone = Images.get(ol.name()+"blue");
					imageWidth = icone.getWidth()/5;
					imageHeight = icone.getHeight()/4;
				} else {
					icone = Images.get("building"+ol.name()+"blue");
					imageWidth = icone.getWidth();
					imageHeight = icone.getHeight();
				}
				xImage = (int) (startXdamage+sizeXdamage/6f-sizeYdamage/(2*nbLine*imageHeight)*imageWidth);
				yImage = (int) (startYdamage+i*sizeYdamage/nbLine);
				g.drawImage(icone,xImage, 
						yImage,
						xImage + imageWidth*sizeYdamage/nbLine/imageHeight, 
						yImage + sizeYdamage/nbLine,
						0,0,imageWidth,imageHeight);
				g.setColor(Color.white);
				g.drawRect(startXdamage, startYdamage+i*sizeYdamage/nbLine, sizeXdamage, sizeYdamage/nbLine);
				for(int j=1; j<3; j++){
					try{
						s = ""+(int)(float)(StatsHandler.damages.get(j).get(ol));
					}catch(Exception e){
						s = "0";
					}
					if(s.length()>3){
						s = s.substring(0, s.length()-3)+"."+s.substring(s.length()-3);
					}
					GraphicElements.font_big.drawString(startXdamage+j*sizeXdamage/3f+sizeXdamage/6f-GraphicElements.font_big.getWidth(s)/2, 
							startYdamage+(i+0.5f)*sizeYdamage/nbLine-GraphicElements.font_big.getHeight(s)/2, 
							s);
				}
			} else {
				g.setColor(Color.white);
				s = "Total";
				g.drawRect(startXdamage, startYdamage+i*sizeYdamage/nbLine, sizeXdamage, sizeYdamage/nbLine);
				GraphicElements.font_big.drawString(startXdamage+sizeXdamage/6f-GraphicElements.font_big.getWidth(s)/2, 
						startYdamage+(i+0.5f)*sizeYdamage/nbLine-GraphicElements.font_big.getHeight(s)/2, 
						s);
				for(int j=1; j<3; j++){
					total = 0;
					try{
						for(Float f : StatsHandler.damages.get(j).values()){
							total += (int)(float)f;
						}
					}catch(Exception e){}
					s = ""+total;
					if(s.length()>3){
						s = s.substring(0, s.length()-3)+"."+s.substring(s.length()-3);
					}
					GraphicElements.font_big.drawString(startXdamage+j*sizeXdamage/3f+sizeXdamage/6f-GraphicElements.font_big.getWidth(s)/2, 
							startYdamage+(i+0.5f)*sizeYdamage/nbLine-GraphicElements.font_big.getHeight(s)/2, 
							s);
				}
			}

		}
		g.setColor(Color.white);
		g.drawLine(startXdamage+sizeXdamage/3f, startYdamage, startXdamage+sizeXdamage/3f, startYdamage+sizeYdamage);
		g.drawLine(startXdamage+2*sizeXdamage/3f, startYdamage, startXdamage+2*sizeXdamage/3f, startYdamage+sizeYdamage);
	}

	public static void renderCounters(Graphics g, float startXcounters, float startYcounters, float sizeXcounters, float sizeYcounters){
		g.setLineWidth(1f);
		String s = "Décompte des morts";
		GraphicElements.font_big.drawString(startXcounters+sizeXcounters/2f-GraphicElements.font_big.getWidth(s)/2f, startYcounters+sizeYcounters/6f, s);
		// kill counts 1
		g.setColor(Colors.team1);
		g.fillRect(startXcounters + sizeXcounters/4f, startYcounters+1.5f*sizeYcounters/6f, sizeXcounters/4f-10f, sizeYcounters/6f-10f);
		g.setColor(Color.black);
		g.fillRect(startXcounters + sizeXcounters/4f+10f, startYcounters+1.5f*sizeYcounters/6f+10f, sizeXcounters/4f-30f, sizeYcounters/6f-30f);
		s = ""+StatsHandler.nbKills.get(1);
		GraphicElements.font_big.drawString(startXcounters + 3*sizeXcounters/8f-5f-GraphicElements.font_big.getWidth(s)/2, 
				startYcounters + sizeYcounters/3f-5f-GraphicElements.font_big.getHeight(s)/2, s);
		// kill counts 2
		g.setColor(Colors.team2);
		g.fillRect(startXcounters + 2*sizeXcounters/4f+10f, startYcounters+1.5f*sizeYcounters/6f, sizeXcounters/4f-10f, sizeYcounters/6f-10f);
		g.setColor(Color.black);
		g.fillRect(startXcounters + 2*sizeXcounters/4f+20f, startYcounters+1.5f*sizeYcounters/6f+10f, sizeXcounters/4f-30f, sizeYcounters/6f-30f);
		s = ""+StatsHandler.nbKills.get(2);
		GraphicElements.font_big.drawString(startXcounters + 5*sizeXcounters/8f-5f-GraphicElements.font_big.getWidth(s)/2, 
				startYcounters + sizeYcounters/3f-5f-GraphicElements.font_big.getHeight(s)/2, s);
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		// TODO Auto-generated method stub

	}
}
