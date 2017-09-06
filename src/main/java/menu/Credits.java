package menu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import control.InputObject;
import control.KeyMapper.KeyEnum;
import model.Game;
import ressources.GraphicElements;
import system.MenuSystem.MenuNames;

public class Credits extends Menu{

	public Vector<String> texts;
	
	
	public float currentHeight;
	public float stepLine;

	
	public Credits(){
		super();
		String fichier ="ressources/credits.txt";
		//lecture du fichier texte	
			InputStream ips;
			try {
				ips = new FileInputStream(fichier);
				InputStreamReader ipsr=new InputStreamReader(ips);
				BufferedReader br=new BufferedReader(ipsr);
				String ligne;
				texts = new Vector<String>();
				while ((ligne=br.readLine())!=null){
					if(ligne.length()==0 || ligne.charAt(0)!='#'){
						texts.add(ligne);
					}
				}
				br.close(); 
				stepLine = GraphicElements.font_main.getHeight("Tj")*2f;
			} catch (IOException e) {
				e.printStackTrace();
			} 
	}
	
	public void init(){
		this.currentHeight = Game.resY;
	}
	
	public void update(InputObject io){
		this.currentHeight--;
		if(this.currentHeight+texts.size()*stepLine+100<0 || io.isPressed(KeyEnum.Escape))
			Game.menuSystem.setMenu(MenuNames.MenuIntro);
	}
	public void draw(Graphics g){
//		g.drawImage(this.backGround, 0,0,Game.resX,Game.resY,0,0,this.backGround.getWidth(),this.backGround.getHeight()-60f,new Color(10,10,10,1f));
		g.setColor(Color.white);
		for(int i = 0; i<texts.size(); i++){
			if(currentHeight+i*stepLine<Game.resY && currentHeight+(i+1)*stepLine>0){
				g.drawString(texts.get(i),Game.resX/2-GraphicElements.font_main.getWidth(texts.get(i))/2,currentHeight+i*stepLine);
			}
		}
	}
}
