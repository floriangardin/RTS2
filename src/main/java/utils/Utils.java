package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;

import com.google.gson.Gson;

import plateau.Building;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;

// Class for static methods
public class Utils {

	public static Gson gson = new Gson();
	public static String[] contactWeapon = new String[]{"spear","sword"};
	
	public Vector<Objet> get(HashMap<String,Objet> req){
		Vector<Objet> result = new Vector<Objet>();
		result.stream().map((Objet o )->  o.getId());
		
		return result;
	}

	public static HashMap<String,String> preParse(String s){
		String[] u = s.split(";");
		HashMap<String,String> hs = new HashMap<String,String>();
		//		if(u.length<=1){
		//			return hs;
		//		}
		for(int i=0;i<u.length;i++){
			String[] r = u[i].split("\\:");
			if(r.length>1){
				hs.put(r[0], r[1]);
			}
			else{
				hs.put(r[0],"");
			}

		}
		return hs;
	}

	public static float distance_2(Objet a ,Objet b){
		return (a.getX()-b.getX())*(a.getX()-b.getX()) + (a.getY()-b.getY())*(a.getY()-b.getY()) ;

	}
	public static float distance_2(Objet a ,float x, float y){
		return (a.getX()-x)*(a.getX()-x) + (a.getY()-y)*(a.getY()-y) ;

	}
	public static float distance_2_selectionBox(Objet a ,float x, float y){
		return (a.selectionBox.getCenterX()-x)*(a.selectionBox.getCenterX()-x) + (a.selectionBox.getCenterY()-y)*(a.selectionBox.getCenterY()-y) ;

	}

	public static float distance(Objet a ,Objet b){
		if(a== null || b == null){
			return -1f;
		}
		return (float) StrictMath.sqrt((a.getX()-b.getX())*(a.getX()-b.getX()) + (a.getY()-b.getY())*(a.getY()-b.getY()) );

	}

	public static float distance(Objet a ,float x , float y){
		return (float) StrictMath.sqrt((a.getX()-x)*(a.getX()-x) + (a.getY()-y)*(a.getY()-y) );

	}

	public static float distance(float x1, float y1, float x2, float y2){
		return (float) StrictMath.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2) );

	}
	public static Objet nearestObject(Vector<Objet> close, Objet caller){
		float ref_dist = 10000000000f;
		Objet closest = null;
		for(Objet o : close){
			float dist = Utils.distance_2(o,caller);
			if(dist < ref_dist){
				ref_dist = dist;
				closest = o;
			}
		}
		return closest;
	}
	public static Objet nearestObject(Vector<Objet> close, float x, float y){
		float ref_dist = 10000000000f;
		Objet closest = null;
		for(Objet o : close){
			float dist = Utils.distance_2(o, x, y);
			if(dist < ref_dist){
				ref_dist = dist;
				closest = o;
			}
		}
		return closest;
	}
	public static Objet nearestObjectToSelectionBox(List<Objet> close, float x, float y){
		float ref_dist = 10000000000f;
		Objet closest = null;
		for(Objet o : close){
			float dist = Utils.distance_2_selectionBox(o, x, y);
			if(dist < ref_dist){
				ref_dist = dist;
				closest = o;
			}
		}
		return closest;
	}


	public static Character nearestObject(Vector<Character> close, Character caller){
		float ref_dist = 1000000000f;
		Character closest = null;
		for(Character o : close){
			float dist = Utils.distance_2(o,caller);
			if(dist < ref_dist){
				ref_dist = dist;
				closest = o;
			}
		}
		return closest;

	}


	public static Image mergeImages(Image a, Image b){
		if(b==null)
			return a;
		int maxW = StrictMath.max(a.getWidth(), b.getWidth()), maxH = StrictMath.max(a.getHeight(), b.getHeight());
		ImageBuffer bimage = new ImageBuffer(maxW,maxH);
		Color ca;
		for(int i=0; i<maxW; i++){
			for(int j=0; j<maxH;j++){
				if(i<a.getWidth() && j<a.getHeight()){
					ca = a.getColor(i, j);
					bimage.setRGBA(i, j, (int)(ca.r*255), (int)(ca.g*255), (int)(ca.b*255), (int)(ca.a*255));
				}
				if(i<b.getWidth() && j<b.getHeight()){
					ca = b.getColor(i, j);
					if(ca.a>0)
						bimage.setRGBA(i, j, (int)(ca.r*255), (int)(ca.g*255), (int)(ca.b*255), (int)(ca.a*255));
				}
			}
		}
		return bimage.getImage();
	}
	public static Image mergeHorse(Image horse, Image charac){
		int maxW = 96, maxH = 192;
		ImageBuffer bimage = new ImageBuffer(maxW,maxH);
		Color ca;
		int decalage = 0;
		for(int colonne=0; colonne<3; colonne+=1){
			for(int ligne=0; ligne<4; ligne+=1){
				if(ligne==0){
					for(int i=0; i<32; i+=1){
						for(int j=0; j<32; j+=1){
							ca = charac.getColor(i+32, j);
							if(ca.a>0)
								bimage.setRGBA(colonne*32+i, j, (int)(ca.r*255), (int)(ca.g*255), (int)(ca.b*255), (int)(ca.a*255));
						}
					}
					for(int i=0; i<32; i+=1){
						for(int j=0; j<32; j+=1){
							if(j>15 || (i>9 && i<24)){
								ca = horse.getColor(colonne*32+i, j);
								if(ca.a>0)
									bimage.setRGBA(colonne*32+i, 15+j, (int)(ca.r*255), (int)(ca.g*255), (int)(ca.b*255), (int)(ca.a*255));
							}
						}
					}
				} else {
					for(int i=0; i<32; i+=1){
						for(int j=0; j<32; j+=1){
							ca = horse.getColor(colonne*32+i, ligne*32+j);
							if(ca.a>0)
								bimage.setRGBA(colonne*32+i, 15+ligne*48+j, (int)(ca.r*255), (int)(ca.g*255), (int)(ca.b*255), (int)(ca.a*255));
						}
					}
					switch(ligne){
					case 1:
						decalage = 5;
						break;
					case 2:
						decalage = -5;
						break;
					case 3:
						decalage = 0;
						break;
					default:
					}
					for(int i=0; i<32; i+=1){
						for(int j=0; j<32; j+=1){
							ca = charac.getColor(i+32, j+32*ligne);
							if(ca.a>0 && i+decalage>0 && i+decalage<31)
								bimage.setRGBA(colonne*32+i+decalage, ligne*48+j, (int)(ca.r*255), (int)(ca.g*255), (int)(ca.b*255), (int)(ca.a*255));
						}
					}
				}

			}
		}
		return bimage.getImage();
	}

	public static Vector<Objet> triY(Vector<Objet> liste){
		if(liste.size()<=1)
			return liste;
		Vector<Objet> liste1 = new Vector<Objet>(), liste2= new Vector<Objet>();
		for(int i=0;i<liste.size();i++){
			if(i<liste.size()/2)
				liste1.add(liste.get(i));
			else
				liste2.add(liste.get(i));
		}
		liste.clear();
		triY(liste1);
		triY(liste2);
		float y1=0f,y2=0f;
		boolean b1=true, b2=true;
		while(true){
			b1 = !liste1.isEmpty();
			b2 = !liste2.isEmpty();
			if(!b1 && !b2)
				break;
			if(!b1){
				liste.add(liste2.firstElement());
				liste2.remove(0);
				continue;
			}
			if(!b2){
				liste.add(liste1.firstElement());
				liste1.remove(0);
				continue;
			}
			y1 = liste1.firstElement().y;
			y2 = liste2.firstElement().y;
			if(y1<y2){
				liste.add(liste1.firstElement());
				liste1.remove(0);
			} else {
				liste.add(liste2.firstElement());
				liste2.remove(0);
			}
		}
		return liste;
	}
	
	

	public static void triId(Vector<Character> liste){
		if(liste.size()<=1)
			return;
		Vector<Character> liste1 = new Vector<Character>(), liste2= new Vector<Character>();
		for(int i=0;i<liste.size();i++){
			if(i<liste.size()/2)
				liste1.add(liste.get(i));
			else
				liste2.add(liste.get(i));
		}
		liste.clear();
		triId(liste1);
		triId(liste2);
		float y1=0f,y2=0f;
		boolean b1=true, b2=true;
		while(true){
			b1 = !liste1.isEmpty();
			b2 = !liste2.isEmpty();
			if(!b1 && !b2)
				break;
			if(!b1){
				liste.add(liste2.firstElement());
				liste2.remove(0);
				continue;
			}
			if(!b2){
				liste.add(liste1.firstElement());
				liste1.remove(0);
				continue;
			}
			y1 = liste1.firstElement().getId();
			y2 = liste2.firstElement().getId();
			if(y1<y2){
				liste.add(liste1.firstElement());
				liste1.remove(0);
			} else {
				liste.add(liste2.firstElement());
				liste2.remove(0);
			}
		}

	}


	public static void triIdActionObjet(Vector<Objet> liste){
		if(liste.size()<=1)
			return;
		Vector<Objet> liste1 = new Vector<Objet>(), liste2= new Vector<Objet>();
		for(int i=0;i<liste.size();i++){
			if(i<liste.size()/2)
				liste1.add(liste.get(i));
			else
				liste2.add(liste.get(i));
		}
		liste.clear();
		triIdActionObjet(liste1);
		triIdActionObjet(liste2);
		float y1=0f,y2=0f;
		boolean b1=true, b2=true;
		while(true){
			b1 = !liste1.isEmpty();
			b2 = !liste2.isEmpty();
			if(!b1 && !b2)
				break;
			if(!b1){
				liste.add(liste2.firstElement());
				liste2.remove(0);
				continue;
			}
			if(!b2){
				liste.add(liste1.firstElement());
				liste1.remove(0);
				continue;
			}
			y1 = liste1.firstElement().getId();
			y2 = liste2.firstElement().getId();
			if(y1<y2){
				liste.add(liste1.firstElement());
				liste1.remove(0);
			} else {
				liste.add(liste2.firstElement());
				liste2.remove(0);
			}
		}

	}

	public static void printCurrentState(Plateau p){
		System.out.println("DEBUG MODE");
		System.out.println();
		System.out.println("========================================");
		System.out.println();
		System.out.println("** Players");
//		if(Game.g.players==null)
//			System.out.println("-> bug: players est null");
//		else{
//			for(Player c:Game.g.players)
//				System.out.println(c.toString());
//		}
//		System.out.println("currentplayer: " + Game.g.currentPlayer);
		System.out.println();System.out.println("========================================");
		System.out.println();
		System.out.println("** Characters");
		if(p.getCharacters()==null)
			System.out.println("-> bug: characters est null");
		else{
//			for(Character c:p.characters)
//				if(c.getTarget()!=null)
//					System.out.println(c.name+" "+ c.x+ " " +c.y + " " +c.id +" "+c.getTeam() +" "+c.lifePoints+" t:"+c.getTarget().x);
//				else
//					System.out.println(c.name+" "+ c.x+ " " +c.y + " " +c.id +" "+c.getTeam() +" "+c.lifePoints);
		}
		System.out.println();
		//		System.out.println("========================================");
		//		System.out.println();
		//		System.out.println("** Bullets");
		//		if(p.bullets==null)
		//			System.out.println("-> bug: bullets est null");
		//		else{
		//			for(Bullet c:p.bullets)
		//				System.out.println(c.name+" "+ c.x+ " " +c.y + " " +c.id +" "+c.getTeam() +" "+c.lifePoints);
		//		}
		System.out.println();
		//		System.out.println();
		//		System.out.println("** Spells");
		//		if(p.spells==null)
		//			System.out.println("-> bug: spells est null");
		//		else{
		//			for(SpellEffect c:p.spells)
		//				System.out.println(c+" " + c.x+ " " +c.y + " " +c.id);
		//		}
		//		System.out.println();
		//		System.out.println("========================================");
		//		System.out.println();
		//		System.out.println("** Natural Objets");
		//		if(p.naturalObjets==null)
		//			System.out.println("-> bug: characters est null");
		//		else{
		//			for(NaturalObjet c:p.naturalObjets)
		//				System.out.println(c+" " + c.x+ " " +c.y);
		//		}
	}

	public static String[] split(String s, char c){
		Vector<String> v = new Vector<String>();
		int indice=0;
		for(int i=0; i<s.length(); i++){
			if(s.charAt(i)==c){
				v.add(s.substring(indice,i));
				indice = i+1;			
			}
		}
		v.add(s.substring(indice,s.length()));
		String[] t = new String[v.size()];
		for(int i=0; i<v.size(); i++){
			t[i] = v.get(i);
		}
		return t;
	}

	public static void triName(Vector<Objet> liste){
		if(liste.size()<=1)
			return;
		Vector<Objet> liste1 = new Vector<Objet>(), liste2= new Vector<Objet>();
		for(int i=0;i<liste.size();i++){
			if(i<liste.size()/2)
				liste1.add(liste.get(i));
			else
				liste2.add(liste.get(i));
		}
		liste.clear();
		triName(liste1);
		triName(liste2);
		String y1="",y2="";
		boolean b1=true, b2=true;
		while(true){
			b1 = !liste1.isEmpty();
			b2 = !liste2.isEmpty();
			if(!b1 && !b2)
				break;
			if(!b1){
				liste.add(liste2.firstElement());
				liste2.remove(0);
				continue;
			}
			if(!b2){
				liste.add(liste1.firstElement());
				liste1.remove(0);
				continue;
			}
			y1 = liste1.firstElement().name.name();
			y2 = liste2.firstElement().name.name();
			if(y1.compareTo(y2)>=0){
				liste.add(liste1.firstElement());
				liste1.remove(0);
			} else {
				liste.add(liste2.firstElement());
				liste2.remove(0);
			}
		}
	}
	public static void sortByQueue(Vector<Building> liste){
		if(liste.size()<=1)
			return;
		Vector<Building> liste1 = new Vector<Building>(), liste2= new Vector<Building>();
		for(int i=0;i<liste.size();i++){
			if(i<liste.size()/2)
				liste1.add(liste.get(i));
			else
				liste2.add(liste.get(i));
		}
		liste.clear();
		sortByQueue(liste1);
		sortByQueue(liste2);
		int y1=0, y2=0;
		boolean b1=true, b2=true;
		while(true){
			b1 = !liste1.isEmpty();
			b2 = !liste2.isEmpty();
			if(!b1 && !b2)
				break;
			if(!b1){
				liste.add(liste2.firstElement());
				liste2.remove(0);
				continue;
			}
			if(!b2){
				liste.add(liste1.firstElement());
				liste1.remove(0);
				continue;
			}
			y1 = liste1.firstElement().getQueue().size();
			y2 = liste2.firstElement().getQueue().size();
			if(y1<=y2){
				liste.add(liste1.firstElement());
				liste1.remove(0);
			} else {
				liste.add(liste2.firstElement());
				liste2.remove(0);
			}
		}
	}

	public static void switchTriName(Vector<Integer> liste, Plateau plateau){
		if(liste == null || liste.size()==0){
			return;
		}
		String name = plateau.getById(liste.get(0)).name.name();
		boolean useful = false;
		for(Integer a: liste)
			if(!plateau.getById(a).name.name().equals(name))
				useful = true;
		Integer buffer;
		if(!useful){
			buffer = liste.get(0);
			liste.remove(0);
			liste.add(buffer);
			return;
		}
		while(plateau.getById(liste.get(0)).name.name().equals(name)){
			buffer = liste.get(0);
			liste.removeElementAt(0);
			liste.add(buffer);
		}

	}

	public static String displayTime(int nbSecond){
		String result = "";
		if(nbSecond<0){
			result ="-";
			nbSecond *= -1;
		}
		result+=nbSecond/60;
		result+=":";
		if((nbSecond%60)<10){
			result+="0";
		}
		result+=nbSecond%60;

		return result;
	}
	
	

	public static float[][] loadFloatMatrix(String path){
		float[][] matrix = null;
		Vector<float[]> buffer = new Vector<float[]>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(
					new InputStreamReader(new FileInputStream(path)));
			String line;
			String[] tab;
			float[] tab1;
			while ((line = br.readLine()) != null) {
				tab = Utils.split(line, ' ');
				tab1 = new float[tab.length-1];
				for(int i=0; i<tab.length-1;i++){
					tab1[i] =Float.parseFloat(tab[i]);
				}
				buffer.add(tab1);
			}
			matrix = new float[buffer.size()][buffer.get(0).length];
			for(int i=0; i<buffer.size();i++){
				matrix[i] = buffer.get(i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(br!=null){
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return matrix;
	}

	public static void drawNiceRect(Graphics g, Color c, float x, float y, float sizeX, float sizeY){
		g.setColor(Color.black);
		g.fillRect(x-6, y-6, sizeX+12, sizeY+12);
		g.setColor(Color.white);
		g.fillRect(x-4, y-4, sizeX+8, sizeY+8);
		g.setColor(c);
		g.fillRect(x-3, y-3, sizeX+6, sizeY+6);
		g.setColor(Color.white);
		g.fillRect(x-1, y-1, sizeX+2, sizeY+2);
		g.setColor(Color.black);
		g.fillRect(x,y,sizeX,sizeY);
	}
	
	public static HashMap<String, String> loadRepertoire(String nameRepertoire, Vector<String> extensionsFichier){
		HashMap<String, String> toReturn = new HashMap<String, String>();
		File repertoire = new File(nameRepertoire);
		File[] files=repertoire.listFiles();
		String s, si;
		for(int i=0; i<files.length; i++){
			s = files[i].getName();
			for(String ext : extensionsFichier){
				if(s.contains("."+ext)){
					// on load l'image
					toReturn.put(s.substring(0, s.length()-ext.length()-1).toLowerCase(), nameRepertoire+s);
					continue;
				} 
			}
			if (!s.contains(".")){
				// nouveau répertoire
				toReturn.putAll(loadRepertoire(nameRepertoire+s+"/", extensionsFichier));
			}
		} 
		return toReturn;
	}
	
	public static HashMap<String, String> loadRepertoire(String nameRepertoire, String extension){
		Vector<String> extensions = new Vector<String>();
		extensions.add(extension);
		return loadRepertoire(nameRepertoire, extensions);
	}
}
