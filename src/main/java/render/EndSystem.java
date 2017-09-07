package render;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import control.Player;
import data.Attributs;
import display.Camera;
import events.EventHandler;
import events.EventNames;
import main.Main;
import model.Game;
import model.GameServer;
import plateau.Building;
import plateau.NaturalObjet;
import plateau.Objet;
import plateau.Plateau;
import ressources.Images;
import ressources.Musics;
import system.ClassSystem;
import system.MenuSystem;
import utils.Utils;

public strictfp class EndSystem extends ClassSystem{

	private Building destroyedHQ;
	private Plateau plateau;
	private boolean victory;
	private int time = 0;
	
	private int timeDropBackground = Main.framerate/4;
	private int timeFadeTitle = Main.framerate;
	private int timeWaiting = 7*Main.framerate;
	private int timeBlackFade = 1*Main.framerate;
	
	
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
		destroyedHQ = (Building)plateau.getHQ(plateau.teams.get(plateau.teamLooser));
		EventHandler.addEvent(EventNames.DestructionHQ, destroyedHQ, plateau);
		vector = new Vector<Rond>();
		this.plateau = plateau;
	}
	
	public void update(GameContainer gc, int arg1){
		// moving Camera
		int Xcam = Camera.Xcam, Ycam = Camera.Ycam;
		float resX = Camera.resX, resY = Camera.resY;
		sizeBandes = StrictMath.min(sizeBandes+2, Camera.resY/7f);
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
			if(Musics.musicPlaying!=null){
				Musics.musicPlaying.fade(1500, 0f, true);
			}
		} else {
			Game.menuSystem.init();
			Camera.reset();
			GameServer.close();
			Game.system = Game.menuSystem;
		}
	}
	
	public void render(GameContainer gc, Graphics g){
		g.translate(-Camera.Xcam, -Camera.Ycam);
		// Draw background
		RenderEngine.renderBackground(g, plateau);
		// Draw first layer of event
		EventHandler.render(g, plateau, false);
		Vector<Objet> objets = new Vector<Objet>();
		for(Objet o : plateau.getObjets().values()){
			objets.add(o);
		}
		objets = Utils.triY(objets);
		Vector<Objet> visibleObjets = new Vector<Objet>();
		for(Objet o : objets){
			if(Camera.visibleByCamera(o.x, o.y, o.getAttribut(Attributs.sight)) && o.team.id==Player.getTeamId()){
				visibleObjets.add(o);
			}
		}		
		// 2) Draw Objects
		for(Objet o : objets){
			if(Camera.visibleByCamera(o.x, o.y, StrictMath.max(o.getAttribut(Attributs.size),o.getAttribut(Attributs.sizeX)))){
				RenderEngine.renderObjet(o, g, plateau);
			}
		}
		// Draw second layer of event
		EventHandler.render(g, plateau, true);
		g.translate(Camera.Xcam, Camera.Ycam);
		
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
	
	private strictfp class Rond{
		Image image;
		float vx,vy;
		float x,y;
		float alpha;
		float delta_alpha = 0.01f;
		public Rond(boolean victory){
			float scale = (float) (StrictMath.random()+0.2f)/1.5f;
			if(victory){
				image = Images.get("victoire_rond").getScaledCopy(scale);
			} else {
				image = Images.get("defaite_rond").getScaledCopy(scale);
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


}
