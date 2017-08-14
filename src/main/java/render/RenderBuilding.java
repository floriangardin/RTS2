package render;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import data.Attributs;
import model.Colors;
import plateau.Building;
import plateau.Objet;
import plateau.Plateau;
import ressources.Images;
import utils.ObjetsList;

public class RenderBuilding {
	
	public static int BUILDINGLAYER = 2;
	
	public static void render(Building b, Graphics g, Plateau plateau){
		render(b,g,plateau, true, true);
	}

	public static void render(Building b, Graphics g, Plateau plateau, boolean visibleByCurrentTeam, boolean isCurrentTeam){
		
		//TODO
		if(b.getAttribut(Attributs.newdesign)==0){
			drawBasicImageNewDesign(g,b,visibleByCurrentTeam);
			if((visibleByCurrentTeam || b.name.equals(ObjetsList.Headquarters)) && b.mouseOver){
				Color color = new Color(b.getTeam().color.getRed(),b.getTeam().color.getGreen(),b.getTeam().color.getBlue(),0.1f);
				drawFlash(g, b, color);
			}
		} else {
			drawBasicImage(g, b, visibleByCurrentTeam);
			if((visibleByCurrentTeam || b.name.equals(ObjetsList.Headquarters)) && b.mouseOver){
				Color color = new Color(b.getTeam().color.getRed(),b.getTeam().color.getGreen(),b.getTeam().color.getBlue(),0.1f);
				Images.get("building"+b.name+b.getTeam().colorName).drawFlash(b.x-b.getAttribut(Attributs.sizeX)/1.8f, b.y-b.getAttribut(Attributs.sizeY), 2*b.getAttribut(Attributs.sizeX)/1.8f, 3*b.getAttribut(Attributs.sizeY)/2,color);
			}
		}
		if(visibleByCurrentTeam)
			drawAnimation(g);
		
		g.setAntiAlias(false);
		g.setLineWidth(25f);
		// Construction points
		if(b.constructionPoints<b.getAttribut(Attributs.maxLifepoints) && visibleByCurrentTeam && b.constructionPoints>0){
			g.setColor(Color.black);
			//g.drawArc(this.getX()-sizeX/2-25,this.getY()-sizeY/2-25,sizeY+50,sizeY+50,0,360);
			g.fillRect(-1f+b.getX()-b.getAttribut(Attributs.sizeX)/4,-1f+b.getY()-3*b.getAttribut(Attributs.sizeY)/4,b.getAttribut(Attributs.sizeX)/2+2f,12f);
			float x = b.constructionPoints/b.getAttribut(Attributs.maxLifepoints);
			if(b.potentialTeam==1)
				g.setColor(Colors.team1);
			else if(b.potentialTeam==2)
				g.setColor(Colors.team2);
			else if(b.potentialTeam==0){
				g.setColor(Colors.team0);
			}
			//g.drawArc(this.getX()-sizeX/2-25,this.getY()-sizeY/2-25,sizeY+50,sizeY+50,0,x*360);
			g.fillRect(b.getX()-b.getAttribut(Attributs.sizeX)/4,b.getY()-3*b.getAttribut(Attributs.sizeY)/4,x*b.getAttribut(Attributs.sizeX)/2,10f);
		}
		g.setAntiAlias(true);
		// draw production
		if(b instanceof Building && isCurrentTeam){
			Building bp = ((Building) b);
			if(bp.queue.size()>0){
				float offsetY = Math.min(2*b.getAttribut(Attributs.sizeY)/3, bp.charge*(64*b.getAttribut(Attributs.sizeY))/b.getAttribut(bp.getProductionList(plateau).get(0),Attributs.prodTime));
				float opacity = 50*bp.charge/b.getAttribut(bp.queue.get(0),Attributs.prodTime);
				Image icone = Images.get("icon"+bp.getQueue().get(0)+"buildingsize");
				float r = (float) (Math.sqrt(2)*icone.getHeight()/2);
				g.setColor(new Color(0f,0f,0f,opacity));
				g.fillOval(b.x-r-10f, b.y-offsetY-r-10f, 2*r+20f, 2*r+20f);
				//g.setColor(new Color(0f,0f,0f,opacity));
				//g.fillOval(x-r-8f, y-offsetY-r-8f, 2*r+16f, 2*r+16f);
				//						g.setColor(Color.white);
				//						g.fillOval(x-r-2f, y-sizeY/2-r-2f, 2*r+4f, 2*r+4f);
				g.setColor(new Color(bp.getTeam().color.r,bp.getTeam().color.g,bp.getTeam().color.b,opacity));
				float startAngle = 270f;
				float sizeAngle = (float)(1f*bp.charge*(360f)/b.getAttribut(bp.getQueue().get(0),Attributs.prodTime));
				g.fillArc(b.x-r-8f, b.y-offsetY-r-8f, 2*r+16f, 2*r+16f, startAngle, startAngle+sizeAngle);
				g.setColor(new Color(0f,0f,0f,opacity));
				g.fillOval(b.x-r, b.y-offsetY-r, 2*r, 2*r);
				icone.setAlpha(opacity);
				g.drawImage(icone, b.x-b.sizeXIcon/2, b.y-offsetY-b.sizeXIcon/2);

			}
		}
		if(isCurrentTeam){
			Building bt = ((Building) b);
			if(bt.queueTechnology!=null){
				float offsetY = Math.min(2*b.getAttribut(Attributs.sizeY)/3, bt.charge*(64*b.getAttribut(Attributs.sizeY))/b.getAttribut(b.queueTechnology.objet, Attributs.prodTime));
				float opacity = 50*bt.charge/b.getAttribut(b.queueTechnology.objet, Attributs.prodTime);
				Image icone = Images.get(b.getAttributString(b.queueTechnology.objet, Attributs.nameIcon)+"buildingsize");
				float r = (float) (Math.sqrt(2)*icone.getHeight()/2);
				g.setColor(new Color(0f,0f,0f,opacity));
				g.fillOval(b.x-r-10f, b.y-offsetY-r-10f, 2*r+20f, 2*r+20f);
				g.setColor(new Color(bt.getTeam().color.r,bt.getTeam().color.g,bt.getTeam().color.b,opacity));
				float startAngle = 270f;
				float sizeAngle = (float)(1f*bt.charge*(360f)/b.getAttribut(b.queueTechnology.objet, Attributs.prodTime));
				g.fillArc(b.x-r-8f, b.y-offsetY-r-8f, 2*r+16f, 2*r+16f, startAngle, startAngle+sizeAngle);
				g.setColor(new Color(0f,0f,0f,opacity));
				g.fillOval(b.x-r, b.y-offsetY-r, 2*r, 2*r);
				icone.setAlpha(opacity);
				g.drawImage(icone, b.x-b.sizeXIcon/2, b.y-offsetY-b.sizeXIcon/2);

			}
		}


	}
	
	
	public static void renderSelection(Graphics g, Building b, Plateau plateau){
		g.setColor(Colors.selection);
		g.setLineWidth(2f);
		g.draw(b.collisionBox);
		drawRallyPoint(g, b, plateau);
		//g.draw(new Ellipse(this.getX(),this.getY()+4f*r/6f,r,r-5f));
	}	

	public static void drawBasicImage(Graphics g, Building b, boolean visibleByCurrentTeam){
		if(visibleByCurrentTeam || b.name.equals(ObjetsList.Headquarters)){
			g.drawImage(Images.get("building"+b.name+b.getTeam().colorName),b.x-b.getAttribut(Attributs.sizeX)/1.8f, b.y-b.getAttribut(Attributs.sizeY));
		}else{
			g.drawImage(Images.get("building"+b.name+"neutral"), b.x-b.getAttribut(Attributs.sizeX)/1.8f, b.y-b.getAttribut(Attributs.sizeY));
		}
	}
	public static void drawBasicImageNewDesign(Graphics g, Building b, boolean visibleByCurrentTeam){
		Image im = Images.get("building"+b.name+b.getTeam().colorName);
		if(visibleByCurrentTeam || b.name.equals(ObjetsList.Headquarters)){
			if(b.getTeam().id==0){
				if(b.constructionPoints*4<b.getAttribut(Attributs.maxLifepoints))
					im = Images.get("building"+b.name+"neutral");
				else if(b.constructionPoints*2<b.getAttribut(Attributs.maxLifepoints))
					im = Images.get("building"+b.name+"neutral_1");
				else if(b.constructionPoints*4<b.getAttribut(Attributs.maxLifepoints)*3)
					im = Images.get("building"+b+"neutral_2");
				else
					im = Images.get("building"+b.name+"neutral_3");
			} else {
				im = Images.get("building"+b.name+b.getTeam().colorName);
			}
		}else{
			im = Images.get("building"+b.name+"neutral");
		}
		g.drawImage(im,b.x-im.getWidth()/2f, b.y+b.getAttribut(Attributs.sizeY)/2-im.getHeight());
	}

	public static void drawFlash(Graphics g, Building b, Color color){
		Image im = Images.get("building"+b.name+"neutral");
		im.drawFlash(b.x-im.getWidth()/2f, b.y+b.getAttribut(Attributs.sizeY)/2-im.getHeight(), im.getWidth(), im.getHeight(), color);
	}
	

//
//	public static void drawAnimationTower(Graphics g){
//		float sizeX = getAttribut(Attributs.sizeX);
//		float sizeY = getAttribut(Attributs.sizeY);
//		if(getTeam().id==1){
//			g.drawImage(Images.get(this.animationBleu), this.x-(sizeX/1.8f)/3, this.y-sizeY,this.x+(sizeX/1.8f)/3, this.y-sizeY+sizeY*3f/8f, (int)(animationTower/30f)*100, 0, ((int)(animationTower/30f)+1)*100, 100);
//		}
//		if(getTeam().id==2){
//			g.drawImage(Images.get(this.animationRouge), this.x-(sizeX/1.8f)/3, this.y-sizeY,this.x+(sizeX/1.8f)/3, this.y-sizeY+sizeY*3f/8f, (int)(animationTower/30f)*100, 0, ((int)(animationTower/30f)+1)*100, 100);
//		}
//	}
	public static Graphics drawRallyPoint(Graphics g, Building b, Plateau plateau){
		Objet rallyPoint = b.getRallyPoint(plateau);
		g.setColor(Colors.team0);
		g.setAntiAlias(true);
		g.setLineWidth(2f);
		g.fill(new Circle(rallyPoint.x,rallyPoint.y,3f));
		g.draw(new Circle(rallyPoint.x,rallyPoint.y,10f));
		g.setAntiAlias(false);
		g.setLineWidth(1f);
		return g;
	}


	public static void drawAnimation(Graphics g){

	}


	
}
