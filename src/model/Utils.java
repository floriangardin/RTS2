package model;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;

import units.Character;

// Class for static methods
public class Utils {




	public static float distance_2(Objet a ,Objet b){
		return (a.getX()-b.getX())*(a.getX()-b.getX()) + (a.getY()-b.getY())*(a.getY()-b.getY()) ;

	}

	public static float distance(Objet a ,Objet b){
		return (float) Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX()) + (a.getY()-b.getY())*(a.getY()-b.getY()) );

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

	public static void printCurrentState(Plateau p){
		System.out.println("DEBUG MODE");
		System.out.println();
		System.out.println("========================================");
		System.out.println();
		System.out.println("** Characters");
		if(p.characters==null)
			System.out.println("-> bug: characters est null");
		else{
			for(Character c:p.characters)
				System.out.println(c+" " + c.x+ " " +c.y + " " +c.id);
		}
		System.out.println();
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
		if(!useful){
			return;
		}
		ActionObjet buffer;
		while(liste.get(0).name.equals(name)){
			buffer = liste.get(0);
			liste.remove(0);
			liste.add(buffer);
		}
			
	}
}
