package render;

import java.net.SocketException;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import events.EventDestructionHQ;
import main.Main;
import model.Building;
import model.Game;
import ressources.Map;

public class EndRender {

	private static Building destroyedHQ;
	private static boolean victory;
	private static int time = 0;
	private static int time_period;
	
	private static int timeDropBackground = Main.framerate/2;
	private static int timeFadeTitle = Main.framerate;
	private static int timeWaiting = 7*Main.framerate;
	private static int timeBlackFade = 1*Main.framerate;
	
	private static int period_length = Main.framerate*2/3;
	private static boolean period_fading = true;
	
	private static Image image_background;
	private static Image image_text;
	private static Image image_texture;
	
	private static Vector<Rond> vector;
	
	public static void initEnd(int loosingTeamId){
		Game.g.endGame = true;
		time = 0;
		image_background = Game.g.images.get("victoire_fond").getScaledCopy(3f);
		if(loosingTeamId!=Game.g.currentPlayer.getTeam()){
			victory = true;
			image_text = Game.g.images.get("victoire_texte");
			image_texture = Game.g.images.get("victoire_fond_texture");
		} else{
			victory = false;
			image_text = Game.g.images.get("defaite_texte");
			image_texture = Game.g.images.get("defaite_fond_texture");
		}
		image_text.setAlpha(0f);
		image_texture.setAlpha(0f);
		destroyedHQ = Game.g.teams.get(loosingTeamId).hq;
		Game.g.getEvents().addEvent(new EventDestructionHQ(destroyedHQ));
		vector = new Vector<Rond>();
	}
	
	public  static void updateEnd(){
		// moving camera
		int Xcam = Game.g.Xcam, Ycam = Game.g.Ycam;
		float resX = Game.g.resX, resY = Game.g.resY;
		if((destroyedHQ.x-Xcam-resX/2)*(destroyedHQ.x-Xcam-resX/2)+(destroyedHQ.y-Ycam-resY/2)*(destroyedHQ.y-Ycam-resY/2)>450f){
			Game.g.Xcam = (int) ((15*(Xcam+resX/2)+destroyedHQ.x)/16-resX/2);
			Game.g.Ycam = (int) ((15*(Ycam+resY/2)+destroyedHQ.y)/16-resY/2);
			Game.g.plateau.updateVisibility();
			return;
		} 
		time ++;
		if(time<=timeDropBackground){
			
		} else if(time<=timeDropBackground+timeFadeTitle){
			
		} else if(time<=timeDropBackground+timeFadeTitle+timeWaiting+timeBlackFade){
			// launching music
			if(victory && Game.g.musicPlaying!=Game.g.musics.get("themeVictory")){
				Game.g.musicPlaying.stop();
				Game.g.musicPlaying = Game.g.musics.get("themeVictory");
				Game.g.musicPlaying.play(1f, Game.g.options.musicVolume);
			} else if(!victory && Game.g.musicPlaying!=Game.g.musics.get("themeDefeat")) {
				Game.g.musicPlaying.stop();
				Game.g.musicPlaying = Game.g.musics.get("themeDefeat");
				Game.g.musicPlaying.play(1f, Game.g.options.musicVolume);
			} 
			updateRonds();
		} else if(time<=timeDropBackground+timeFadeTitle+timeWaiting+timeBlackFade+Main.framerate*2){
			Game.g.musicPlaying.fade(1500, 0f, true);
		} else {
			Game.g.isInMenu = true;
			Game.g.hasAlreadyPlay = true;
			Game.g.endGame = false;
			try {
				Game.g.normalServer.setBroadcast(true);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			Map.initializePlateau(Game.g, 1, 1);
			Game.g.setMenu(Game.g.menuIntro);
		}
	}
	
	public static void render(Graphics g){
		if(time==0){
			return;
		}
		if(time<=timeDropBackground){
			image_background = Game.g.images.get("victoire_fond").getScaledCopy(3f-2f*time/timeDropBackground);
		} else if(time<=timeDropBackground+timeFadeTitle){
			image_text.setAlpha(1f*(time-timeDropBackground)/(timeFadeTitle));
			image_texture.setAlpha(0.5f*(time-timeDropBackground)/(timeFadeTitle));
		} else if(time<=timeDropBackground+timeFadeTitle+timeWaiting){
			// launching music
			if(victory && Game.g.musicPlaying!=Game.g.musics.get("themeVictory")){
				Game.g.musicPlaying.stop();
				Game.g.musicPlaying = Game.g.musics.get("themeVictory");
				Game.g.musicPlaying.play(1f, Game.g.options.musicVolume);
			} else if(!victory && Game.g.musicPlaying!=Game.g.musics.get("themeDefeat")) {
				Game.g.musicPlaying.stop();
				Game.g.musicPlaying = Game.g.musics.get("themeDefeat");
				Game.g.musicPlaying.play(1f, Game.g.options.musicVolume);
			} 
			
		}
		for(Rond r : vector){
			r.render(g);
		}
		g.drawImage(image_background, Game.g.resX/2-image_background.getWidth()/2, 
				Game.g.resY/2-image_background.getHeight()/2);
		g.drawImage(image_texture, Game.g.resX/2-image_texture.getWidth()/2, 
				Game.g.resY/2-image_texture.getHeight()/2);
		g.drawImage(image_text, Game.g.resX/2-image_text.getWidth()/2, 
				Game.g.resY/2-image_text.getHeight()/2);
		float ratio = 1f*(time-timeDropBackground-timeFadeTitle-timeWaiting)/(timeBlackFade);
		Color color = new Color(0f,0f,0f,2*ratio);
		g.setColor(color);
		g.fillOval(Game.g.resX*(0.5f-ratio*0.75f), Game.g.resY*0.5f-Game.g.resX*ratio*0.75f, Game.g.resX*ratio*1.5f, Game.g.resX*ratio*1.5f);
	}
	
	private static void updateRonds(){
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
	
	private static class Rond{
		Image image;
		float vx,vy;
		float x,y;
		float alpha;
		float delta_alpha = 0.01f;
		public Rond(boolean victory){
			float scale = (float) (Math.random()+0.2f)/1.5f;
			if(victory){
				image = Game.g.images.get("victoire_rond").getScaledCopy(scale);
			} else {
				image = Game.g.images.get("defaite_rond").getScaledCopy(scale);
			}
			x = Game.g.resX/2f;
			y = Game.g.resY/2f;
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
