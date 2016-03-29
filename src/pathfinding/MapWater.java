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
				this.idTerrain[i][j] = (int)(Math.random()*1.1);
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
			return 1;
		else 
			return idTerrain[x][y];
	}

	private class Sand {
		public int x, y;
		public String im;

		public Sand(MapWater parent, int x, int y){
			this.x = x;
			this.y = y;
			int left = parent.getTerrain(x-1, y);
			int upleft = parent.getTerrain(x-1, y-1);
			int up = parent.getTerrain(x, y-1);
			int upright = parent.getTerrain(x+1, y-1);
			int right = parent.getTerrain(x+1, y);
			int downright = parent.getTerrain(x+1, y+1);
			int down = parent.getTerrain(x, y+1);
			int downleft = parent.getTerrain(x-1, y+1);
			im = getSand(left, upleft, up, upright, right, downright, down, downleft);
		}

		public void draw(Graphics g, int stepGrid){
			g.drawImage(Game.g.images.getSand(im), x*stepGrid, y*stepGrid);
		}

		//		public void draw(Graphics g){
		//			g.drawImage(images.get(0), x*Map.stepGrid, y*Map.stepGrid);
		//			g.drawImage(images.get(1), (x+1/2)*Map.stepGrid, y*Map.stepGrid);
		//			g.drawImage(images.get(2), (x+1/2)*Map.stepGrid, (y+1/2)*Map.stepGrid);
		//			g.drawImage(images.get(3), x*Map.stepGrid, (y+1/2)*Map.stepGrid);
		//		}

		private String getSand(int left, int upleft, int up, int upright, int right, int downright, int down, int downleft){
			// earth = 0 ; water = 1 ; sand >= 2
			int a,b,c,d;
			try{
				a = getIdCorner(left, upleft, up);
				b = getIdCorner(up, upright, right);
				c = getIdCorner(right, downright, down);
				d = getIdCorner(down, downleft, left);
				return a+""+b+""+c+""+d;
			} catch (FatalGillesError e){
				System.out.println( left+" "+ upleft+" "+ up+" "+ upright+" "+ right+" "+ downright+" "+ down+" "+ downleft);
				e.printStackTrace();
			}
			return "";

		}

		private int getIdCorner(int left, int upleft, int up) throws FatalGillesError{
			// earth = 0 ; water = 1 ; sand >= 2
			if(left==0){
				if(up==0){
					return 7;
				} else if(up>=2){
					return 6;
				}
			} else if (left==1){
				if(up==1){
					return 3;
				} else if(up>=2){
					return 2;
				}
			} else {
				if(up==0){
					return 5;
				} else if(up==1){
					return 1;
				} else {
					if(upleft==1){
						return 4;
					} else {
						return 8;
					}
				}
			}
			throw new FatalGillesError("Sand tile invalide : "+left+" "+upleft+" "+up);
		}
	}

	public void update(){
		// 0 earth, 1 water, 2 sand
		for(int i=0; i<sizeX; i++){
			for(int j=0; j<sizeY; j++){
				if(this.getTerrain(i,j)==1)
					continue;
				if(getTerrain(i-1,j-1)==1 || getTerrain(i-1,j)==1 || getTerrain(i-1,j+1)==1 ||
						getTerrain(i,j-1)==1 || getTerrain(i,j+1)==1 ||
						getTerrain(i+1,j-1)==1 || getTerrain(i+1,j)==1 || getTerrain(i+1,j+1)==1){
					this.idTerrain[i][j] = 2;
				} else {
					this.idTerrain[i][j] = 0;
				}
			}
		}	
		this.sands.clear();
		for(int i=0; i<sizeX; i++){
			for(int j=0; j<sizeY; j++){
				if(getTerrain(i,j)>=2)
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
				if(idTerrain[x][y]==0){
					g.drawImage(water2Image,(x)*stepGrid,(y)*stepGrid);
				}
			}
		}
//		System.out.println(this.sands.size());
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
