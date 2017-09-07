package pathfinding;
import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.geom.Rectangle;

import data.Attributs;
import pathfinding.Case.IdTerrain;
import plateau.Building;
import plateau.Character;
import plateau.NaturalObjet;
import ressources.Map;
import utils.Utils;

public strictfp class MapGrid implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6785502878817282410L;

	public float maxX, maxY;

	public int idCase = 0;
	public Vector<Integer> casesWater = new Vector<Integer>();

	public Vector<Vector<Case>> grid;
	public HashMap<Integer, Case> idcases;
	public Vector<Float> Xcoord;
	public Vector<Float> Ycoord;

	public MapGrid(float sizeX, float sizeY){
		this.maxX = sizeX;
		this.maxY = sizeY;
		idcases = new HashMap<Integer, Case>();
		grid = new Vector<Vector<Case>>();
		grid.add(new Vector<Case>());
		Case c = new Case(true,idCase,this);
		grid.get(0).add(c);
		idcases.put(c.id, c);
		idCase++;
		grid.get(0).get(0).update(0, 0, maxX, maxY);
		Xcoord = new Vector<Float>();
		Xcoord.add(0f);
		Xcoord.add(maxX);
		Ycoord = new Vector<Float>();
		Ycoord.add(0f);
		Ycoord.add(maxY);
		for(float mg = Map.stepGrid; mg<this.maxX; mg+=Map.stepGrid){
			insertNewX(mg);
		}
		for(float mg = Map.stepGrid; mg<this.maxY; mg+=Map.stepGrid){
			insertNewY(mg);
		}
		updateSurroundingChars();
	}

	public void insertNewX(float f){
		//		System.out.println(this);
		if(Xcoord.contains(f) || f<0f || f>Xcoord.get(Xcoord.size()-1)){
			return;
		}
		int i=0;
		while(i<Xcoord.size() && Xcoord.get(i)<f)
			i++;
		float f1 = Xcoord.get(i-1);
		float f2 = Xcoord.get(i);
		Xcoord.insertElementAt(f, i);
		grid.insertElementAt(new Vector<Case>(), i-1);
		Case c;
		for(int j=0; j<Ycoord.size()-1; j++){
			c = new Case(grid.get(i).get(j).ok,idCase,this);
			grid.get(i-1).add(c);
			idcases.put(c.id, c);
			idCase++;
			grid.get(i-1).get(j).updateX(f1, f);
			grid.get(i-1).get(j).updateY(Ycoord.get(j),Ycoord.get(j+1));
			grid.get(i).get(j).updateX(f, f2);
		}
		updateIndices();
		//		for(int k =0; k<this.grid.size(); k++)
		//			System.out.println(grid.get(k).get(0));
	}

	public void insertNewY(float f){
		//		System.out.println(this);
		if(Ycoord.contains(f) || f<0f || f>Ycoord.get(Ycoord.size()-1)){
			return;
		}
		int j=0;
		while(j<Ycoord.size() && Ycoord.get(j)<f)
			j++;
		float f1 = Ycoord.get(j-1);
		float f2 = Ycoord.get(j);
		Ycoord.insertElementAt(f, j);
		Case c;
		for(int i=0; i<Xcoord.size()-1; i++){
			c = new Case(grid.get(i).get(j-1).ok,idCase,this);
			grid.get(i).insertElementAt(c, j-1);
			idcases.put(c.id, c);
			idCase++;
			grid.get(i).get(j-1).updateX(Xcoord.get(i),Xcoord.get(i+1));
			grid.get(i).get(j-1).updateY(f1,f);
			grid.get(i).get(j).updateY(f, f2);
		}
		updateIndices();
		//		for(int k =0; k<this.grid.get(0).size(); k++)
		//			System.out.println(grid.get(0).get(k));
	}

	public String toString(){
		String s ="";
		s+="Xcoord : ";
		for(Float f: Xcoord)
			s+= f+" ";
		s+="\nYcoord : ";
		for(Float f: Ycoord)
			s+= f+" ";
		s+="\n";
		for(int j=0; j<grid.get(0).size(); j++){
			for(int i=0; i<grid.size();i++){
				s+=(grid.get(i).get(j).ok ? "O " : "X ");
			}
			s+="\n";
		}
		return s;
	}

	public void addBuilding(Building b){
		for(int i = b.i;i<b.i + (int)(b.getAttribut(Attributs.sizeX)/Map.stepGrid);i++ )
			for(int j = b.j; j<b.j + (int)(b.getAttribut(Attributs.sizeY)/Map.stepGrid); j++)
				if(i>=0 && i<grid.size() && j>=0 && j<grid.get(0).size())
					grid.get(i).get(j).building = b;
		update();
	}
	
	public void addNaturalObject(NaturalObjet n){
		Case c = this.getCase(n.getX(), n.getY());
		n.setIdCase(c.id);
		c.naturesObjet.add(n);
		update();
	}
	
	public void removeBuilding(Building b){
		for(int i = b.i;i<b.i + (int)b.getAttribut(Attributs.sizeX)/Map.stepGrid;i++ )
			for(int j = b.j; j<b.j + (int)b.getAttribut(Attributs.sizeY)/Map.stepGrid; j++)
				if(i>=0 && i<grid.size() && j>=0 && j<grid.get(0).size())
					grid.get(i).get(j).building = null;
		update();
	}
	
	public void removeNaturalObject(NaturalObjet n){
		Case c = this.getCase(n.getX(), n.getY());
		c.naturesObjet.remove(n);
		update();
	}
		
	public void update(){
		casesWater.clear();
		for(Case c: this.idcases.values()){
			c.update();
			if(c.getIdTerrain()==IdTerrain.WATER){
				casesWater.add(c.id);
			}
		}
	}

	public Case getCase(float x, float y){
		if(x<0 || x>=maxX || y<0||y>=maxY)
			return null;
		int i=0, j=0;
		while(x>Xcoord.get(i+1))
			i++;
		while(y>Ycoord.get(j+1))
			j++;
		return grid.get(i).get(j);
	}
	
	public Case getCase(int id){
		if(idcases.containsKey(id)){			
			return idcases.get(id);
		} else {
			return null;
		}
	}
	
	public void updateIndices(){
		for(int i=0; i<grid.size(); i++){
			for(int j=0; j<grid.get(0).size(); j++){
				grid.get(i).get(j).i = i;
				grid.get(i).get(j).j = j;
			}
		}
	}
	
	public Vector<Integer> pathfinding(float xStart, float yStart, float xEnd, float yEnd){
		xEnd = StrictMath.min(StrictMath.max(1, xEnd), this.maxX-1);
		yEnd = StrictMath.min(StrictMath.max(1, yEnd), this.maxY-1);
		//		System.out.println("MapGrid line 124: calcul d'un chemin");
		Vector<Integer> path = new Vector<Integer>();
		int iStart=0, jStart=0, iEnd=0, jEnd=0;
		while(xStart>Xcoord.get(iStart+1))
			iStart++;
		while(yStart>Ycoord.get(jStart+1))
			jStart++;
		while(xEnd>Xcoord.get(iEnd+1))
			iEnd++;
		while(yEnd>Ycoord.get(jEnd+1))
			jEnd++;
		Vector<Point> closedList = new Vector<Point>();
		Vector<Point> openList = new Vector<Point>();
		Point depart = new Point(iStart,jStart,this);
		depart.computeDistance(iEnd, jEnd);
		openList.add(depart);
		Point u;
		Point v;
		int indice;
		int iTrav=0, jTrav=0;
		int nbIt = 0;
		while(openList.size()>0){
			nbIt++;
			u = openList.firstElement();
			openList.remove(0);
			// voisin de droite
			for(int k=0;k<4;k++){
//				for(int k=0;k<8;k++){
				switch(k){
				case 0: //� droite
					iTrav = u.i+1; jTrav = u.j;break;
				case 1: //� gauche
					iTrav = u.i-1; jTrav = u.j;break;
				case 2: //en bas
					iTrav = u.i; jTrav = u.j+1;break;
				case 3: //en haut
					iTrav = u.i; jTrav = u.j-1;break;
				case 4: //en bas � droite
					iTrav = u.i+1; jTrav = u.j+1;break;
				case 5: //en bas � gauche
					iTrav = u.i-1; jTrav = u.j+1;break;
				case 6: //en haut � droite
					iTrav = u.i+1; jTrav = u.j-1;break;
				case 7: //en haut � gauche
					iTrav = u.i-1; jTrav = u.j-1;break;
				}
				if(iTrav==iEnd && jTrav==jEnd){
					path.add(grid.get(iTrav).get(jTrav).id);
					while(u!=null){
						path.insertElementAt(grid.get(u.i).get(u.j).id,0);
						u = u.comeFrom;
					}
					return path;
				}
				if(iTrav>=0&& iTrav<grid.size() && jTrav>=0&& jTrav<grid.get(0).size() && grid.get(iTrav).get(jTrav).ok){
					if(k<4 || (grid.get(iTrav).get(u.j).ok && grid.get(u.i).get(jTrav).ok)){

						v = new Point(iTrav,jTrav,this,u,(k<4?1f:1.5f));
						boolean doNothing = false;
						Point toDelete = null;
						for(Point p : closedList){
							if(p.id==v.id){
								if(p.cost<v.cost)
									doNothing =true;
								else
									toDelete = p;
							}
						}
						if(toDelete!=null)
							closedList.remove(toDelete);
						if(doNothing)
							continue;
						toDelete = null;
						for(Point p : openList){
							if(p.id==v.id){
								if(p.cost<v.cost)
									doNothing =true;
								else
									toDelete = p;
							}
						}

						if(toDelete!=null)
							openList.remove(toDelete);
						if(doNothing)
							continue;
						v.computeDistance(iEnd, jEnd);
						v.heuristique = v.cost + v.dist;
						indice= 0;
						while(indice<openList.size() && openList.get(indice).heuristique<v.heuristique)
							indice++;
						openList.insertElementAt(v,indice);
					}
				}
			}
			closedList.addElement(u);

		}

		return path;
	}
	
	public Vector<Integer> pathfinding(float xStart, float yStart, Rectangle r){
//		System.out.println("MapGrid line 230: calcul d'un chemin");
		Vector<Integer> path = new Vector<Integer>();
		int iStart=0, jStart=0, iEnd1=0, jEnd1=0, iEnd2=0, jEnd2=0;
		float xEnd1, xEnd2, yEnd1, yEnd2;
		xEnd1 = r.getMinX()+0.1f;
		yEnd1 = r.getMinY()+0.1f;
		xEnd2 = r.getMaxX()-0.1f;
		yEnd2 = r.getMaxY()-0.1f;
		while(xStart>Xcoord.get(iStart+1))
			iStart++;
		while(yStart>Ycoord.get(jStart+1))
			jStart++;
		while((iEnd1+1)<Xcoord.size() && xEnd1>Xcoord.get(iEnd1+1))
			iEnd1++;
		while((jEnd1+1)<Ycoord.size() && yEnd1>Ycoord.get(jEnd1+1))
			jEnd1++;
		while((iEnd2+1)<Xcoord.size() && xEnd2>Xcoord.get(iEnd2+1))
			iEnd2++;
		while((jEnd2+1)<Ycoord.size() && yEnd2>Ycoord.get(jEnd2+1))
			jEnd2++;
		
		Vector<Point> closedList = new Vector<Point>();
		Vector<Point> openList = new Vector<Point>();
		Point depart = new Point(iStart,jStart,this);
		depart.computeDistance(iEnd1, jEnd1, iEnd2, jEnd2);
		openList.add(depart);
		Point u;
		Point v;
		int indice;
		int iTrav=0, jTrav=0;
		int nbIt = 0;
		while(openList.size()>0){
			nbIt++;
			u = openList.firstElement();
			openList.remove(0);
			// voisin de droite
			for(int k=0;k<8;k++){
				switch(k){
				case 0: //� droite
					iTrav = u.i+1; jTrav = u.j;break;
				case 1: //� gauche
					iTrav = u.i-1; jTrav = u.j;break;
				case 2: //en bas
					iTrav = u.i; jTrav = u.j+1;break;
				case 3: //en haut
					iTrav = u.i; jTrav = u.j-1;break;
				case 4: //en bas � droite
					iTrav = u.i+1; jTrav = u.j+1;break;
				case 5: //en bas � gauche
					iTrav = u.i-1; jTrav = u.j+1;break;
				case 6: //en haut � droite
					iTrav = u.i+1; jTrav = u.j-1;break;
				case 7: //en haut � gauche
					iTrav = u.i-1; jTrav = u.j-1;break;
				}
				if(iTrav>=iEnd1 && jTrav>=jEnd1 && iTrav<=iEnd2 && jTrav<=jEnd2){
					path.add(grid.get(iTrav).get(jTrav).id);
					while(u!=null){
						path.insertElementAt(grid.get(u.i).get(u.j).id,0);
						u = u.comeFrom;
					}
					return path;
				}
				if(iTrav>=0&& iTrav<grid.size() && jTrav>=0&& jTrav<grid.get(0).size() && grid.get(iTrav).get(jTrav).ok){
					if(k<4 || (grid.get(iTrav).get(u.j).ok && grid.get(u.i).get(jTrav).ok)){

						v = new Point(iTrav,jTrav,this,u,(k<4?1f:1.5f));
						boolean doNothing = false;
						Point toDelete = null;
						for(Point p : closedList){
							if(p.id==v.id){
								if(p.cost<v.cost)
									doNothing =true;
								else
									toDelete = p;
							}
						}
						if(toDelete!=null)
							closedList.remove(toDelete);
						if(doNothing)
							continue;
						toDelete = null;
						for(Point p : openList){
							if(p.id==v.id){
								if(p.cost<v.cost)
									doNothing =true;
								else
									toDelete = p;
							}
						}

						if(toDelete!=null)
							openList.remove(toDelete);
						if(doNothing)
							continue;
						v.computeDistance(iEnd1, jEnd1, iEnd2, jEnd2);
						v.heuristique = v.cost + v.dist;
						indice= 0;
						while(indice<openList.size() && openList.get(indice).heuristique<v.heuristique)
							indice++;
						openList.insertElementAt(v,indice);
					}
				}
			}
			closedList.addElement(u);

		}

		return path;
	}

	public void printPath(Vector<Case> path){
		Case c;
		for(int j=0; j<grid.get(0).size();j++){
			for(int i=0; i<grid.size(); i++){
				c = grid.get(i).get(j);
				if(c.ok){
					if(path.contains(c))
						System.out.print("& ");
					else
						System.out.print("O ");
				} else {
					if(path.contains(c))
						System.out.print("? ");
					else
						System.out.print("X ");

				}
			}
			System.out.println("");
		}
	}

	public strictfp class Point {
		public int i;
		public int j;
		public int id;
		public float cost, heuristique;
		public float dist;
		public Point comeFrom;
		public MapGrid map;
		public Point(int i, int j, MapGrid map){
			this.i = i;
			this.j = j;
			this.map = map;
			this.id = map.grid.get(i).get(j).id;
		}
		public Point(int i, int j, MapGrid map, Point comeFrom, float addCost){
			this.i = i;
			this.j = j;
			this.map = map;
			this.id = map.grid.get(i).get(j).id;
			this.cost=comeFrom.cost+addCost;
			this.comeFrom = comeFrom;
		}
		public void computeDistance(int iEnd, int jEnd){
			this.dist = (float)StrictMath.sqrt((i-iEnd)*(i-iEnd)+(j-jEnd)*(j-jEnd));
		}
		public void computeDistance(int iEnd1, int jEnd1, int iEnd2, int jEnd2){
			if(i<=iEnd2 && i>=iEnd1)
				this.dist = StrictMath.min(StrictMath.abs(j-jEnd1),StrictMath.abs(j-jEnd2));
			else if(j<=jEnd2 && j>=jEnd1)
				this.dist = StrictMath.min(StrictMath.abs(i-iEnd1),StrictMath.abs(i-iEnd2));
			else{
				int jd = StrictMath.min(StrictMath.abs(j-jEnd1),StrictMath.abs(j-jEnd2));
				int id = StrictMath.min(StrictMath.abs(i-iEnd1),StrictMath.abs(i-iEnd2));
				this.dist = (float)StrictMath.sqrt((id)*(id)+(jd)*(jd));
			}
		}

	}

	public Vector<Case> isLineOk(float x1, float y1, float x2, float y2){
		if(Utils.distance(x1, y1, x2, y2)>1000f)
			return new Vector<Case>();
		boolean ok = true;
		if(x1<0 || x1>=maxX || y1<0||y1>=maxY)
			return new Vector<Case>();
		if(x2<0 || x2>=maxX || y2<0||y2>=maxY)
			return new Vector<Case>();
		int i=0, j=0;
		float x = x1, y = y1;
		while(x>Xcoord.get(i+1))
			i++;
		while(y>Ycoord.get(j+1))
			j++;
		float a,xint,yint,aint;
		String dir;
		Case arrival = this.getCase(x2, y2);
		Vector<Case> cases = new Vector<Case>();
		//System.out.println(this.toString());
		while(grid.get(i).get(j).id!=arrival.id && grid.get(i).get(j).ok){
			if(cases.contains(grid.get(i).get(j))){
				return new Vector<Case>();
			}
			cases.add(grid.get(i).get(j));
			//System.out.println(x+" "+y+" "+i+" "+j+" "+x2+" "+y2);
			a = (y2-y)/(x2-x+0.0001f);
			if(y>y2){
				if(x<=x2){
					xint = Xcoord.get(i+1);
					yint = Ycoord.get(j);
					aint = (yint-y)/(xint-x+0.0001f);
					if(a>aint)
						dir = "RIGHT";
					else
						dir = "UP";
				} else {
					xint = Xcoord.get(i);
					yint = Ycoord.get(j);
					aint = (yint-y)/(xint-x+0.0001f);
					if(a<aint)
						dir = "LEFT";
					else
						dir = "UP";					
				}
			} else {
				if(x<=x2){
					xint = Xcoord.get(i+1);
					yint = Ycoord.get(j+1);
					aint = (yint-y)/(xint-x+0.0001f);
					if(a>aint)
						dir = "DOWN";
					else
						dir = "RIGHT";
				} else {
					xint = Xcoord.get(i);
					yint = Ycoord.get(j+1);
					aint = (yint-y)/(xint-x+0.0001f);
					if(a<aint)
						dir = "DOWN";
					else
						dir = "LEFT";					
				}
			}
			float newx=-1f,newy=-1f;
			switch(dir){
			case "UP": j-=1; newy=Ycoord.get(j+1)-0.1f; newx = x+(newy-y)/a; break;
			case "DOWN": j+=1; newy=Ycoord.get(j)+0.1f; newx = x+(newy-y)/a; break;
			case "RIGHT": i+=1; newx=Xcoord.get(i)+0.1f; newy = a*(newx-x)+y; break;
			case "LEFT": i-=1; newx=Xcoord.get(i+1)-0.1f; newy = a*(newx-x)+y; break;
			default:
			}
			x = newx;
			y = newy;
			if(i<0 ||j<0 || i>=grid.size() || j>=grid.get(0).size()){
				cases.clear();
				return cases;
			}
		}
		if(grid.get(i).get(j).id==arrival.id)
			cases.add(grid.get(i).get(j));
		else
			cases.clear();
		
		return cases;
	}
	
	public Vector<Character> getSurroundingChars(Case c){
		Vector<Character> chars = new Vector<Character>();
		chars.addAll(c.characters);
		boolean leftisok = c.x>0;
		boolean upisok = c.y>0;
		boolean rightisok = c.x+c.sizeX<this.maxX;
		boolean downisok = c.y+c.sizeY<this.maxY;
		if(leftisok)
			chars.addAll(grid.get(c.i-1).get(c.j).characters);
		if(rightisok)
			chars.addAll(grid.get(c.i+1).get(c.j).characters);
		if(upisok)
			chars.addAll(grid.get(c.i).get(c.j-1).characters);
		if(downisok)
			chars.addAll(grid.get(c.i).get(c.j+1).characters);
		if(leftisok && upisok)
			chars.addAll(grid.get(c.i-1).get(c.j-1).characters);
		if(leftisok && downisok)
			chars.addAll(grid.get(c.i-1).get(c.j+1).characters);
		if(rightisok && upisok)
			chars.addAll(grid.get(c.i+1).get(c.j-1).characters);
		if(rightisok && downisok)
			chars.addAll(grid.get(c.i+1).get(c.j+1).characters);
		return chars;
	}
	
	public Vector<NaturalObjet> getSurroundingNaturalObjet(Case c){
		Vector<NaturalObjet> chars = new Vector<NaturalObjet>();
		chars.addAll(c.naturesObjet);
		boolean leftisok = c.x>0;
		boolean upisok = c.y>0;
		boolean rightisok = c.x+c.sizeX<this.maxX;
		boolean downisok = c.y+c.sizeY<this.maxY;
		if(leftisok)
			chars.addAll(grid.get(c.i-1).get(c.j).naturesObjet);
		if(rightisok)
			chars.addAll(grid.get(c.i+1).get(c.j).naturesObjet);
		if(upisok)
			chars.addAll(grid.get(c.i).get(c.j-1).naturesObjet);
		if(downisok)
			chars.addAll(grid.get(c.i).get(c.j+1).naturesObjet);
		if(leftisok && upisok)
			chars.addAll(grid.get(c.i-1).get(c.j-1).naturesObjet);
		if(leftisok && downisok)
			chars.addAll(grid.get(c.i-1).get(c.j+1).naturesObjet);
		if(rightisok && upisok)
			chars.addAll(grid.get(c.i+1).get(c.j-1).naturesObjet);
		if(rightisok && downisok)
			chars.addAll(grid.get(c.i+1).get(c.j+1).naturesObjet);
		return chars;
	}
	

	public void updateSurroundingChars(){
		for(int i=0; i<grid.size(); i++){
			for(int j=0; j<grid.get(0).size(); j++){
				grid.get(i).get(j).surroundingChars = getSurroundingChars(grid.get(i).get(j));
			}
		}
	}
}


