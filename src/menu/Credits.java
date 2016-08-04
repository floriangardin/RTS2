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

public class Credits extends Menu{

	public Vector<String> texts;
	
	public Game game;
	
	public float currentHeight;
	public float stepLine;

	public Image backGround;
	
	public Credits(Game game){
		super(game);
		this.game = game;
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
				this.backGround = game.menuIntro.backGround;
				stepLine = game.font.getHeight("Tj")*2f;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	public void initialize(){
		this.currentHeight = game.resY;
	}
	public void update(InputObject io){
		this.currentHeight--;
		if(this.currentHeight+texts.size()*stepLine+100<0 || io.isPressed(KeyEnum.Escape))
			this.game.setMenu(this.game.menuIntro);
	}
	public void draw(Graphics g){
		g.drawImage(this.backGround, 0,0,this.game.resX,this.game.resY,0,0,this.backGround.getWidth(),this.backGround.getHeight()-60f,new Color(10,10,10,1f));
		g.setColor(Color.white);
		for(int i = 0; i<texts.size(); i++){
			if(currentHeight+i*stepLine<game.resY && currentHeight+(i+1)*stepLine>0){
				g.drawString(texts.get(i),game.resX/2-game.font.getWidth(texts.get(i))/2,currentHeight+i*stepLine);
			}
		}
	}
}
