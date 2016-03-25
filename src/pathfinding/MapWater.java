package pathfinding;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import model.Game;
import ressources.Map;
import tests.FatalGillesError;

public class MapWater {

	/** int idTerrain
	 *  (0:earth, 1:sand, 2:water) 
	 */

	public int sizeX, sizeY;
	public int[][] idTerrain;
	public Image waterImage, water2Image;
	public Vector<Sand> sands;


	public MapWater(int sizeX, int sizeY){
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.idTerrain = new int[sizeX][sizeY];
		this.sands = new Vector<Sand>();
		for(int i=0; i<sizeX; i++){
			for(int j=0; j<sizeY; j++){
				this.idTerrain[i][j] = (int)(Math.random()*1.1)*2;
			}
		}
		this.update();
		this.waterImage = Game.g.images.get("waterTile");
		this.water2Image = Game.g.images.get("waterTile2");
	}

	public void setTerrain(int x, int y, int terrain){
		if(x<0 || x>=sizeX ||y<0 ||y>=sizeY || terrain<0 || terrain>2)
			return;
		this.idTerrain[x][y] = terrain;
	}
	
	public int getTerrain(int x, int y){
		if(x<0 || x>=sizeX ||y<0 ||y>=sizeY)
			return 2;
		else 
			return idTerrain[x][y];
	}

	private class Sand {
		public int x, y;
		public Vector<Image> images;

		public Sand(MapWater parent, int x, int y){
			this.x = x;
			this.y = y;
			images = new Vector<Image>();
			Vector<Integer> terrainsVoisins = new Vector<Integer>();
			terrainsVoisins.add(parent.getTerrain(x-1, y));
			terrainsVoisins.add(parent.getTerrain(x-1, y-1));
			terrainsVoisins.add(parent.getTerrain(x, y-1));
			terrainsVoisins.add(parent.getTerrain(x+1, y-1));
			terrainsVoisins.add(parent.getTerrain(x+1, y));
			terrainsVoisins.add(parent.getTerrain(x+1, y+1));
			terrainsVoisins.add(parent.getTerrain(x, y+1));
			terrainsVoisins.add(parent.getTerrain(x-1, y+1));
			for(int i=0; i<4; i++){
				String s = (i==0?"A":"")+(i==1?"B":"")+(i==2?"C":"")+(i==3?"D":"");
				s+=terrainsVoisins.firstElement();
				terrainsVoisins.add(terrainsVoisins.remove(0));
				s+=terrainsVoisins.firstElement();
				terrainsVoisins.add(terrainsVoisins.remove(0));
				s+=terrainsVoisins.firstElement();
				this.images.addElement(Game.g.images.getSand(s));
				if(this.images.lastElement()==null)
					System.out.println(s +" : "+this.images.lastElement());
			}
		}

		public void draw(Graphics g, int stepGrid){
			Image im;
			g.drawImage(images.get(0), x*stepGrid, y*stepGrid);
			im = images.get(1);
//			im.rotate(90);
			g.drawImage(im, (x+1f/2f)*stepGrid+1, y*stepGrid);
//			im.rotate(-90);
			im = images.get(2);
//			im.rotate(180);
			g.drawImage(im, (x+1f/2f)*stepGrid+1, (y+1f/2f)*stepGrid+1);
//			im.rotate(-180);
			im = images.get(3);
//			im.rotate(270);
			g.drawImage(im, x*stepGrid, (y+1f/2f)*stepGrid+1);
//			im.rotate(-270);
		}
//		public void draw(Graphics g){
//			g.drawImage(images.get(0), x*Map.stepGrid, y*Map.stepGrid);
//			g.drawImage(images.get(1), (x+1/2)*Map.stepGrid, y*Map.stepGrid);
//			g.drawImage(images.get(2), (x+1/2)*Map.stepGrid, (y+1/2)*Map.stepGrid);
//			g.drawImage(images.get(3), x*Map.stepGrid, (y+1/2)*Map.stepGrid);
//		}
	}

	public void update(){
		for(int i=0; i<sizeX; i++){
			for(int j=0; j<sizeY; j++){
				if(this.getTerrain(i,j)==2)
					continue;
				if(getTerrain(i-1,j-1)==2 || getTerrain(i-1,j)==2 || getTerrain(i-1,j+1)==2 ||
						getTerrain(i,j-1)==2 || getTerrain(i,j+1)==2 ||
						getTerrain(i+1,j-1)==2 || getTerrain(i+1,j)==2 || getTerrain(i+1,j+1)==2){
					this.idTerrain[i][j] = 1;
				} else {
					this.idTerrain[i][j] = 0;
				}
			}
		}	
		this.sands.clear();
		for(int i=0; i<sizeX; i++){
			for(int j=0; j<sizeY; j++){
				if(getTerrain(i,j)==1)
					sands.addElement(new Sand(this,i,j));
			}
		}	
	}


	public void draw(Graphics g, float stepGrid, int Xcam, int Ycam, int maxX, int maxY){
		int xmin = (int) (Math.max(0,Xcam) / stepGrid);
		int xmax = (int) Math.min(maxX,(Xcam+Game.g.resX)/stepGrid+1);
		int ymin = (int) (Math.max(0,Ycam) / stepGrid);
		int ymax = (int) Math.min(maxY,(Ycam+Game.g.resY) / stepGrid+1);
		//waterImage = waterImage.getScaledCopy((int)(2*stepGrid), (int)(2*stepGrid));
		//water2Image = water2Image.getScaledCopy((int)(stepGrid), (int)(stepGrid));
//		for(int x = xmin; x<xmax; x++){
//			for(int y = ymin; y<ymax; y++){
//				if(idTerrain[x][y]==2){
//					g.drawImage(waterImage,(x-1f/2f)*stepGrid,(y-1f/2f)*stepGrid);
//				}
//			}
//		}
//		for(int y=(ymin==0?-10:ymin); y<(ymax==maxY?maxY+10:ymax);y++){
//			for(int x=(xmin==0?-10:xmin); x<(xmax==maxX?maxX+10:xmax);x++){
//				g.drawImage(waterImage,(x-1f/2f)*stepGrid,(y-1f/2f)*stepGrid);
//			}
//		}
//		for(int y=(ymin==0?-10:ymin); y<(ymax==maxY?maxY+10:ymax);y++){
//			for(int x=(xmin==0?-10:xmin); x<(xmax==maxX?maxX+10:xmax);x++){
//				g.drawImage(water2Image,(x)*stepGrid,(y)*stepGrid);
//			}
//		}
		water2Image = Game.g.images.get("waterTile2");
		Image e = Game.g.images.getSand("E");
		for(int x = xmin; x<xmax; x++){
			for(int y = ymin; y<ymax; y++){
				if(idTerrain[x][y]==0){
					g.drawImage(e,(x)*stepGrid,(y)*stepGrid);
				}
			}
		}
//		g.setColor(Color.green);
//		for(int x = xmin; x<xmax; x++){
//			for(int y = ymin; y<ymax; y++){
//				if(idTerrain[x][y]==0){
//					g.fillRect(x*stepGrid, y*stepGrid,stepGrid,stepGrid);
//				}
//			}
//		}
//		g.setColor(Color.cyan);
//		for(int x = xmin; x<xmax; x++){
//			for(int y = ymin; y<ymax; y++){
//				if(idTerrain[x][y]==2){
//					g.fillRect(x*stepGrid, y*stepGrid,stepGrid,stepGrid);
//				}
//			}
//		}
		for(int x = xmin; x<xmax; x++){
			for(int y = ymin; y<ymax; y++){
				if(idTerrain[x][y]==2){
					g.drawImage(water2Image,(x)*stepGrid,(y)*stepGrid);
				}
			}
		}
		System.out.println(this.sands.size());
		for(Sand s : this.sands){
			if(s.x>=xmin && s.x<=xmax && s.y>=ymin && s.y<=ymax){
				s.draw(g,(int)stepGrid);
			}
		}
//		g.setLineWidth(3);
//		for(int x = xmin; x<xmax; x++){
//			for(int y = ymin; y<ymax; y++){
//				if(idTerrain[x][y]==2){
//					g.setColor(Color.blue);
//				} else if(idTerrain[x][y]==1){
//					g.setColor(Color.black);
//				} else {
//					g.setColor(Color.red);					
//				}
//				g.drawRect((x)*stepGrid,(y)*stepGrid, stepGrid, stepGrid);
//			}
//		}
//		g.setLineWidth(1);
	}
}
