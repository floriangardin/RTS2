package render;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import control.Player;
import display.Camera;
import events.EventHandler;
import events.EventNames;
import main.Main;
import model.Game;
import plateau.Building;
import plateau.Plateau;
import ressources.Images;
import ressources.Musics;
import system.ClassSystem;

public class EndSystem extends ClassSystem{

	private Building destroyedHQ;
	private Plateau plateau;
	private boolean victory;
	private int time = 0;
	private int time_period;
	
	private int timeDropBackground = Main.framerate/4;
	private int timeFadeTitle = Main.framerate;
	private int timeWaiting = 7*Main.framerate;
	private int timeBlackFade = 1*Main.framerate;
	
	private int period_length = Main.framerate*2/3;
	private boolean period_fading = true;
	
	private Image image_background;
	private Image image_text;
	private Image image_texture;
	
	private float sizeBandes = 0f;
	
	private Vector<Rond> vector;
	
	public EndSystem(Plateau plateau){
		time = 0;
		image_background = Images.get("victoire_fond").getScaledCopy(3f);
		if(plateau.teamLooser!=Player.getTeamId()){
			victory = true;
			image_text = Images.get("victoire_texte");
			image_texture = Images.get("victoire_fond_texture");
		} else{
			victory = false;
			image_text = Images.get("defaite_texte");
			image_texture = Images.get("defaite_fond_texture");
		}
		image_text.setAlpha(0f);
		image_texture.setAlpha(0f);
		destroyedHQ = (Building)plateau.getById(plateau.teams.get(plateau.teamLooser).hq);
		EventHandler.addEvent(EventNames.DestructionHQ, destroyedHQ, plateau);
		vector = new Vector<Rond>();
		this.plateau = plateau;
	}
	
	public void update(GameContainer gc, int arg1){
		// moving Camera
		int Xcam = Camera.Xcam, Ycam = Camera.Ycam;
		float resX = Camera.resX, resY = Camera.resY;
		sizeBandes = Math.min(sizeBandes+2, Camera.resY/7f);
		if((destroyedHQ.x-Xcam-resX/2)*(destroyedHQ.x-Xcam-resX/2)+(destroyedHQ.y-Ycam-resY/2)*(destroyedHQ.y-Ycam-resY/2)>450f){
			Camera.Xcam = (int) ((15*(Xcam+resX/2)+destroyedHQ.x)/16-resX/2);
			Camera.Ycam = (int) ((15*(Ycam+resY/2)+destroyedHQ.y)/16-resY/2);
			return;
		} 
		time ++;
		if(time<=timeDropBackground){
			
		} else if(time<=timeDropBackground+timeFadeTitle){

		} else if(time<=timeDropBackground+timeFadeTitle+timeWaiting+timeBlackFade){
			// launching music
			if(victory && Musics.musicPlaying!=Musics.get("themeVictory")){
				Musics.stopMusic();
				Musics.playMusic("themeVictory");
			} else if(!victory && Musics.musicPlaying!=Musics.get("themeDefeat")){
				Musics.stopMusic();
				Musics.playMusic("themeDefeat");
			} 
			updateRonds();
		} else if(time<=timeDropBackground+timeFadeTitle+timeWaiting+timeBlackFade+Main.framerate*2){
			Musics.musicPlaying.fade(1500, 0f, true);
		} else {
//			Game.g.isInMenu = true;
//			Game.g.hasAlreadyPlay = true;
//			Game.g.endGame = false;
//			try {
//				Game.g.normalServer.setBroadcast(true);
//			} catch (SocketException e) {
//				e.printStackTrace();
//			}
//			Map.initializePlateau(Game.g, 1, 1);
//			Game.g.setMenu(Game.g.menuIntro);
			Game.system = Game.menuSystem;
		}
	}
	
	public void render(GameContainer gc, Graphics g){
		RenderEngine.render(g, plateau);
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
		float ratio = 1f*(time-timeDropBackground-timeFadeTitle-timeWaiting)/(timeBlackFade);
		g.setColor(Color.black);
		g.fillRect(0, 0, Camera.resX, sizeBandes);
		g.fillRect(0, Camera.resY, Camera.resX, -sizeBandes);
		Color color = new Color(0f,0f,0f,2*ratio);
		g.setColor(color);
		g.fillOval(Camera.resX*(0.5f-ratio*0.75f), Camera.resY*0.5f-Camera.resX*ratio*0.75f, Camera.resX*ratio*1.5f, Camera.resX*ratio*1.5f);
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
	
	private class Rond{
		Image image;
		float vx,vy;
		float x,y;
		float alpha;
		float delta_alpha = 0.01f;
		public Rond(boolean victory){
			float scale = (float) (Math.random()+0.2f)/1.5f;
			if(victory){
				image = Images.get("victoire_rond").getScaledCopy(scale);
			} else {
				image = Images.get("defaite_rond").getScaledCopy(scale);
			}
			x = Camera.resX/2f;
			y = Camera.resY/2f;
			vx = (float) (Math.random()*2f-1f)*4f;
			vy = (float) (Math.random()*2f-1f)*0.7f;
			alpha = (float) (0.5f+Math.random());
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


}