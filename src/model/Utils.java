package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.Sound;

import units.Character;

// Class for static methods
public class Utils {




	public static float distance_2(Objet a ,Objet b){
		return (a.getX()-b.getX())*(a.getX()-b.getX()) + (a.getY()-b.getY())*(a.getY()-b.getY()) ;

	}

	public static Sound getRandomSound (Vector<Sound> v) {
		if(v.size()==1){
			return v.get(0);
		}
		Random generator = new Random();
		int rnd = generator.nextInt(v.size() - 1);
		return v.get(rnd); // Cast the vector value into a String object
	}

	public static float distance(Objet a ,Objet b){
		if(a== null || b == null){
			return -1f;
		}
		return (float) Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX()) + (a.getY()-b.getY())*(a.getY()-b.getY()) );

	}

	public static float distance(Objet a ,float x , float y){
		return (float) Math.sqrt((a.getX()-x)*(a.getX()-x) + (a.getY()-y)*(a.getY()-y) );

	}

	public static float distance(float x1, float y1, float x2, float y2){
		return (float) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2) );

	}
	public static Objet nearestObject(Vector<Objet> close, Objet caller){
		float ref_dist = 100000f;
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

	public static Image mergeImages(Image a, Image b){
		if(b==null)
			return a;
		int maxW = Math.max(a.getWidth(), b.getWidth()), maxH = Math.max(a.getHeight(), b.getHeight());
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

	public static void triY(Vector<Objet> liste){
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
			y1 = liste1.firstElement().id;
			y2 = liste2.firstElement().id;
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
		if(p.players==null)
			System.out.println("-> bug: players est null");
		else{
			for(Player c:p.players)
				System.out.println(c.toString());
		}
		System.out.println("currentplayer: " + p.currentPlayer);
		System.out.println();System.out.println("========================================");
		//		System.out.println();
		//		System.out.println("** Characters");
		//		if(p.characters==null)
		//			System.out.println("-> bug: characters est null");
		//		else{
		//			for(Character c:p.characters)
		//				if(c.target!=null)
		//					System.out.println(c.name+" "+ c.x+ " " +c.y + " " +c.id +" "+c.getTeam() +" "+c.lifePoints+" t:"+c.target.x);
		//				else
		//					System.out.println(c.name+" "+ c.x+ " " +c.y + " " +c.id +" "+c.getTeam() +" "+c.lifePoints);
		//		}
		//		System.out.println();
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

	public static void triName(Vector<ActionObjet> liste){
		if(liste.size()<=1)
			return;
		Vector<ActionObjet> liste1 = new Vector<ActionObjet>(), liste2= new Vector<ActionObjet>();
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
			y1 = liste1.firstElement().name;
			y2 = liste2.firstElement().name;
			if(y1.compareTo(y2)>=0){
				liste.add(liste1.firstElement());
				liste1.remove(0);
			} else {
				liste.add(liste2.firstElement());
				liste2.remove(0);
			}
		}
	}

	public static void switchTriName(Vector<ActionObjet> liste){
		if(liste == null || liste.size()==0){
			return;
		}
		String name = liste.get(0).name;
		boolean useful = false;
		for(ActionObjet a: liste)
			if(!a.name.equals(name))
				useful = true;
		ActionObjet buffer;
		if(!useful){
			buffer = liste.get(0);
			liste.remove(0);
			liste.add(buffer);
			return;
		}
		while(liste.get(0).name.equals(name)){
			buffer = liste.get(0);
			liste.remove(0);
			liste.add(buffer);
		}

	}

	public static String gameTime(long startTime){
		String result = "";
		result+=((int) ((System.currentTimeMillis()-startTime)/1000))/60;
		result+=":";
		if((((System.currentTimeMillis()-startTime)/1000)%60)<10){
			result+="0";
		}
		result+=((System.currentTimeMillis()-startTime)/1000)%60;

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
}
