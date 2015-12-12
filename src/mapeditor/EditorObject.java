package mapeditor;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import model.Map;

public class EditorObject {
	public String name;
	public int team;
	public Image image;
	public float x;
	public float y;
	public int sizeX, sizeY;
	public float stepGrid = 50f;
	public int clas;

	public EditorObject(String name, int team, int clas, Image image, int x, int y, ObjectBar o, int sizeX, int sizeY) {
		this.name = name;
		this.team = team;
		this.clas = clas;
		switch(clas){
		case 0 :this.image = image.getSubImage(0, 0, image.getWidth()/5, image.getHeight()/4).getScaledCopy((o.sizeY/7f)/(image.getHeight()/4));break;
		case 1 :this.image = image.getScaledCopy((o.sizeX/2.2f)/(image.getWidth()));break;
		case 2 :this.image = image.getSubImage(0, 0, image.getWidth()/5, image.getHeight());break;
		case 3 :this.image = image;break;
		default:
		}
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		
		this.x = x;
		this.y = y;
	}
	public EditorObject(String name, int team, int clas, Image image, float x, float y, int sizeX, int sizeY) {
		this.name = name;
		this.team = team;
		this.clas = clas;
		switch(clas){
		case 0 :this.image = image.getSubImage(0, 0, image.getWidth()/5, image.getHeight()/4).getScaledCopy(50f/(image.getHeight()/4));break;
		case 1 :this.image = image.getScaledCopy((100f)/(image.getWidth()));break;
		case 2 :this.image = image.getSubImage(0, 0, image.getWidth()/5, image.getHeight());break;
		case 3 :this.image = image;break;
		default:
		}
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics gc){
		gc.drawImage(this.image,x*stepGrid,y*stepGrid-this.image.getHeight()+this.sizeY*stepGrid);
	}

	public void draw(Graphics gc, float x, float y){
		gc.drawImage(this.image,x-this.image.getWidth()/2f,y-this.image.getHeight()/2f);
	}
}
