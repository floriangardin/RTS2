package mapeditor;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import ressources.Map;

public class EditorObject {
	public String name;
	public int team;
	public Image image;
	public float x;
	public float y;
	public int sizeX, sizeY;
	public float stepGrid = 50f;
	public int clas;

	public EditorObject(){
		
	}
	
	public EditorObject(String name, int team, int clas, Image image, int x, int y, ObjectBar o, int sizeX, int sizeY) {
		this.initialize(name, team, clas, image, x, y, sizeX, sizeY);
		switch(clas){
		case 0 :this.image = image.getSubImage(0, 0, image.getWidth()/5, image.getHeight()/4).getScaledCopy((o.sizeY/7f)/(image.getHeight()/4));break;
		case 1 :this.image = image.getScaledCopy((o.sizeX/2.2f)/(image.getWidth()));break;
		case 2 :this.image = image.getSubImage(0, 0, image.getWidth()/5, image.getHeight());break;
		case 3 :this.image = image.getScaledCopy((o.sizeX/2.2f)/(image.getWidth()));break;
		default:
		}
	}
	public EditorObject(String name, int team, int clas, Image image, float x, float y, int sizeX, int sizeY) {
		this.initialize(name, team, clas, image, x, y, sizeX, sizeY);
		switch(clas){
		case 0 :this.image = image.getSubImage(0, 0, image.getWidth()/5, image.getHeight()/4).getScaledCopy(50f/(image.getHeight()/4));break;
		case 1 :this.image = image.getScaledCopy((100f)/(image.getWidth()));break;
		case 2 :this.image = image.getSubImage(0, 0, image.getWidth()/5, image.getHeight());break;
		case 3 :this.image = image;break;
		default:
		}
	}
	public EditorObject(String name, float x, float y){
		this.initialize(name, 0, 9, null, x, y, 1, 1);
	}
	public static EditorObject createFromObjectBar(EditorObject e){
		EditorObject o = new EditorObject();
		o.initialize(e.name,e.team,e.clas,e.image,e.x,e.y,e.sizeX,e.sizeY);
		o.image = e.image;
		return o;
	}
	public void initialize(String name, int team, int clas, Image image, float x, float y, int sizeX, int sizeY) {
		this.name = name;
		this.team = team;
		this.clas = clas;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics gc){
		if(this.name.contains("cam")){
			gc.setColor(Color.white);
			switch(this.name){
			case "Zcam" : 	gc.drawRect(x*stepGrid, y*stepGrid, stepGrid, stepGrid);
			gc.drawString("Z", x*stepGrid+stepGrid/4, y*stepGrid+stepGrid/4);
			break;
			case "Scam" : 	gc.drawRect(x*stepGrid, y*stepGrid, stepGrid, stepGrid);
			gc.drawString("S", x*stepGrid+stepGrid/4, y*stepGrid+stepGrid/4);
			break;
			case "Qcam" : 	gc.drawRect(x*stepGrid, y*stepGrid, stepGrid, stepGrid);
			gc.drawString("Q", x*stepGrid+stepGrid/4, y*stepGrid+stepGrid/4);
			break;
			case "Dcam" : 	gc.drawRect(x*stepGrid, y*stepGrid, stepGrid, stepGrid);
			gc.drawString("D", x*stepGrid+stepGrid/4, y*stepGrid+stepGrid/4);
			break;
			}
		}
		else if(this.sizeX==1 && this.sizeY==1){
			gc.drawImage(this.image,x*stepGrid+stepGrid/2f-this.image.getWidth()/2f,y*stepGrid-this.image.getHeight()+this.sizeY*stepGrid);			
		} else {
			gc.drawImage(this.image,x*stepGrid,y*stepGrid-this.image.getHeight()+this.sizeY*stepGrid);
		}
	}
	public void draw(Graphics gc, float x, float y){
		gc.drawImage(this.image,x-this.image.getWidth()/2f,y-this.image.getHeight()/2f);
	}
}
