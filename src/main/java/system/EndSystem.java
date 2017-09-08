package system;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import control.InputObject;
import control.Player;
import display.Camera;
import events.EventHandler;
import events.EventNames;
import main.Main;
import model.Game;
import model.GameClient;
import model.GameServer;
import plateau.Building;
import plateau.Plateau;
import render.RenderEngine;
import ressources.Images;
import ressources.Musics;
import stats.StatsSystem;

public strictfp class EndSystem extends ClassSystem{

	private Building destroyedHQ;
	private Plateau plateau;
	private boolean victory;
	private int time = 0;
	private int othertime = 0;
	
	private int timeDropBackground = Main.framerate/4;
	private int timeFadeTitle = Main.framerate/2;
	private int timeWaiting = 2*Main.framerate;
	private int timeBlackFade = 1*Main.framerate;
	
	
	private Image image_background;
	private Image image_text;
	private Image image_texture;
	
	private float sizeBandes = 0f;
	
	private Vector<Rond> vector;
	
	public EndSystem(Plateau plateau){
		image_background = Images.get("victoire_fond").getScaledCopy(3f*Game.resX/1920);
		if(plateau.getTeamLooser()!=Player.getTeamId()){
			victory = true;
			image_text = Images.get("victoire_texte").getScaledCopy(1f*Game.resX/1920);;
			image_texture = Images.get("victoire_fond_texture").getScaledCopy(1f*Game.resX/1920);;
		} else{
			victory = false;
			image_text = Images.get("defaite_texte").getScaledCopy(1f*Game.resX/1920);;
			image_texture = Images.get("defaite_fond_texture").getScaledCopy(1f*Game.resX/1920);;
		}
		othertime = 0;
		time = 0;
		image_text.setAlpha(0f);
		image_texture.setAlpha(0f);
		destroyedHQ = (Building)plateau.getHQ(plateau.getTeams().get(plateau.getTeamLooser()));
		EventHandler.addEvent(EventNames.DestructionHQ, destroyedHQ, plateau);
		vector = new Vector<Rond>();
		this.plateau = plateau;
	}
	
	public void update(GameContainer gc, int arg1){
		// moving Camera
		int Xcam = Camera.Xcam, Ycam = Camera.Ycam;
		float resX = Camera.resX, resY = Camera.resY;
		sizeBandes = StrictMath.min(sizeBandes+2, Camera.resY/7f);
		othertime++;
		if(othertime>200 && (destroyedHQ.getX()*Game.ratioX-Xcam-resX/2)*(destroyedHQ.getX()*Game.ratioX-Xcam-resX/2)+(destroyedHQ.getY()*Game.ratioY-Ycam-resY/2)*(destroyedHQ.getY()*Game.ratioY-Ycam-resY/2)>450f){
			Camera.Xcam = (int) ((15*(Camera.Xcam+resX/2)+destroyedHQ.getX()*Game.ratioX)/16-resX/2);
			Camera.Ycam = (int) ((15*(Camera.Ycam+resY/2)+destroyedHQ.getY()*Game.ratioY)/16-resY/2);
			return;
		} 
		time ++;
		InputObject io = new InputObject(gc.getInput());
		if(time<=timeDropBackground){
			
		} else if(time<=timeDropBackground+timeFadeTitle){

		} else if(time<=timeDropBackground+timeFadeTitle+timeWaiting){
			// launching music
			if(victory && Musics.musicPlaying!=Musics.get("themeVictory")){
				Musics.stopMusic();
				Musics.playMusic("themeVictory");
			} else if(!victory && Musics.musicPlaying!=Musics.get("themeDefeat")){
				Musics.stopMusic();
				Musics.playMusic("themeDefeat");
			} 
			updateRonds();
		} else {
			if(io.pressed.size()>0){
				if(Musics.musicPlaying!=null){
					Musics.musicPlaying.fade(1500, 0f, true);
				}
				Game.menuSystem.init();
				Camera.reset();
				GameClient.close();
				GameServer.close();
				Game.system = Game.menuSystem;
			}
		}
	}
	
	public void render(GameContainer gc, Graphics g){
		RenderEngine.renderPlateau(g, plateau, false);
		if(time==0){
			g.setColor(Color.black);
			g.fillRect(0, 0, Camera.resX, sizeBandes);
			g.fillRect(0, Camera.resY, Camera.resX, -sizeBandes);
			return;
		}
		if(time<=timeDropBackground){
			image_background = Images.get("victoire_fond").getScaledCopy(3f-2f*time/timeDropBackground);
		} else if(time<=timeDropBackground+timeFadeTitle){
			image_text.setAlpha(1f*(time-timeDropBackground)/(timeFadeTitle));
			image_texture.setAlpha(0.5f*(time-timeDropBackground)/(timeFadeTitle));
		}
		for(Rond r : vector){
			r.render(g);
		}
		g.drawImage(image_background, Camera.resX/2-image_background.getWidth()/2, 
				Camera.resY/2-image_background.getHeight()/2);
		g.drawImage(image_texture, Camera.resX/2-image_texture.getWidth()/2, 
				Camera.resY/2-image_texture.getHeight()/2);
		g.drawImage(image_text, Camera.resX/2-image_text.getWidth()/2, 
				Camera.resY/2-image_text.getHeight()/2);
		if(time>timeDropBackground+timeFadeTitle+timeWaiting){
			StatsSystem.render(g);
		}
		float ratio = 1f*(time-timeDropBackground-timeFadeTitle-timeWaiting)/(timeBlackFade);
		g.setColor(Color.black);
		g.fillRect(0, 0, Camera.resX, sizeBandes);
		g.fillRect(0, Camera.resY, Camera.resX, -sizeBandes);
//		Color color = new Color(0f,0f,0f,2*ratio);
//		g.setColor(color);
//		g.fillOval(Camera.resX*(0.5f-ratio*0.75f), Camera.resY*0.5f-Camera.resX*ratio*0.75f, Camera.resX*ratio*1.5f, Camera.resX*ratio*1.5f);
	}
	
	private void updateRonds(){
		vector.add(new Rond(victory));
		Vector<Rond> toRemove = new Vector<Rond>();
		for(Rond r : vector){
			if(!r.isAlive()){
				toRemove.add(r);
			}
		}
		for(Rond r : toRemove){
			vector.remove(r);
		}
		toRemove.clear();
	}
	
	private strictfp class Rond{
		Image image;
		float vx,vy;
		float x,y;
		float alpha;
		float delta_alpha = 0.01f;
		public Rond(boolean victory){
			float scale = (float) (StrictMath.random()+0.2f)/1.5f;
			if(victory){
				image = Images.get("victoire_rond").getScaledCopy(scale*Game.ratioX);
			} else {
				image = Images.get("defaite_rond").getScaledCopy(scale*Game.ratioY);
			}
			x = Camera.resX/2f;
			y = Camera.resY/2f;
			vx = (float) (StrictMath.random()*2f-1f)*4f;
			vy = (float) (StrictMath.random()*2f-1f)*0.7f;
			alpha = (float) (0.5f+StrictMath.random());
		}
		
		public void render( Graphics g){
			x += vx;
			y += vy;
			alpha -= delta_alpha;
			image.setAlpha(alpha);
			g.drawImage(image, x-image.getWidth()/2f, y-image.getHeight()/2f);
		}
		
		public boolean isAlive(){
			return alpha>0;
		}
	}

	public static void reset() {
		// TODO Auto-generated method stub
		
	}


}
