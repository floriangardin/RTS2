package system;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;

import control.KeyMapper;
import events.EventHandler;
import main.Main;
import model.Game;
import model.Options;
import multiplaying.ChatHandler;
import ressources.GraphicElements;
import ressources.Images;
import ressources.Musics;
import ressources.Sounds;
import ressources.Taunts;

public class IntroSystem extends ClassSystem{
	

	public Image background;
	public int nbLoadedThing;
	public boolean waitingLoading;
	public String lastThing;
	public DeferredResource nextResource;

	public boolean hasPressKey = false;

	public int cooldownTotal = 920;
	public int cooldownMid = 820;
	public int cooldownMusic = 880;
	public int cooldownIntro = cooldownTotal;
	



	//////////////////////////////
	// INITIALIZING GAME ENGINE //
	//////////////////////////////

	public boolean thingsLoaded = false;
	public boolean plateauLoaded = false;
	public boolean allLoaded = false;
	private boolean special = false;
	private boolean gilles = false;
	private boolean rate = false;
	private int gillesPasse = -1;
	public Image loadingSpearman;
	public Image loadingGilles;
	public Image loadingTitle;
	public Image loadingBackground;
	public float toGoTitle = -0f;
	public int animationLoadingSpearman=0;
	private boolean waitLoading;
	String adviceToDisplay;


	public IntroSystem(){
		
		double rdm = Math.random();
		try{
			if(rdm<0.20){
				this.loadingSpearman = new Image("ressources/images/unit/spearmanBlue.png");			
			} else if (rdm<0.40){
				this.loadingSpearman = new Image("ressources/images/unit/crossbowmanBlue.png");			
			} else if (rdm<0.60){
				this.loadingSpearman = new Image("ressources/images/unit/knightBlue.png");			
			} else if (rdm<0.80 ){
				this.loadingSpearman = new Image("ressources/images/unit/inquisitorBlue.png");			
			} else if (rdm<0.99 || !Debug.gillesSurCentEnable){
				this.loadingSpearman = new Image("ressources/images/unit/priestBlue.png");			
			} else {
				this.loadingSpearman = new Image("ressources/images/danger/gilles.png");					
				this.special = true;
			}
			this.loadingGilles = new Image("ressources/images/danger/gilles.png");
			this.loadingTitle = new Image("ressources/images/menu/menuTitle01.png").getScaledCopy(0.35f*Game.resY/650);
			this.loadingBackground = new Image("ressources/images/backgroundMenu.png").getScaledCopy(0.35f*Game.resY/650);
		} catch(SlickException e){}
		if(Debug.conseilChargementEnable){
			String fichier ="././ressources/conseils.txt";
			//lecture du fichier texte	
			try{
				InputStream ips=new FileInputStream(fichier); 
				InputStreamReader ipsr=new InputStreamReader(ips);
				BufferedReader br=new BufferedReader(ipsr);
				String ligne;
				Vector<String> lignes = new Vector<String>();
				while ((ligne=br.readLine())!=null){
					lignes.add(ligne);
				}
				br.close(); 
				this.adviceToDisplay = lignes.get((int)(Math.random()*lignes.size()));
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		LoadingList.setDeferredLoading(true);
		Musics.init();
		EventHandler.init();
		ChatHandler.init();
		Sounds.init();
		GraphicElements.init();
		Images.init();
		Options.init();
		KeyMapper.init();
		Taunts.init();
		
		nbLoadedThing = LoadingList.get().getRemainingResources();
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.resX, Game.resY);
		float toGoTitle2 = Math.max(0f,toGoTitle);
		UnicodeFont font = GraphicElements.font_main;
		if(lastThing!=null && toGoTitle2==0f){
			int startBarX = (int) (Game.resX/10);
			int startBarY = (int) (18*Game.resY/20);
			int sizeBarX = (int) (Game.resX - 2*startBarX);
			int sizeBarY = (int)(Game.resY/40);
			g.setColor(Color.white);
			g.fillRect(startBarX-2, startBarY-2,sizeBarX+4, sizeBarY+4);
			if(Debug.conseilChargementEnable)
				g.drawString(adviceToDisplay, Game.resX/2-font.getWidth(adviceToDisplay)/2, Game.resY-20-font.getHeight(adviceToDisplay));
			g.setColor(Color.black);
			g.fillRect(startBarX, startBarY,sizeBarX, sizeBarY);
			float x = 1f*(nbLoadedThing-LoadingList.get().getRemainingResources())/nbLoadedThing;
			g.setColor(new Color(1f-x,0f,x));
			g.fillRect(startBarX, startBarY,sizeBarX*(nbLoadedThing-LoadingList.get().getRemainingResources())/nbLoadedThing, sizeBarY);
			if(LoadingList.get().getRemainingResources() > 0){
				g.setColor(Color.white);
				g.drawString(""+lastThing, startBarX+20f, startBarY+sizeBarY/2-font.getHeight("Hg")/2);
			}
			int xanimation = startBarX + sizeBarX*(nbLoadedThing-LoadingList.get().getRemainingResources())/nbLoadedThing;
			if(special){
				int height = this.loadingSpearman.getHeight();
				int width = this.loadingSpearman.getWidth();
				g.drawImage(this.loadingSpearman,xanimation-width/2,startBarY-sizeBarY-height);					
			} else {
				this.animationLoadingSpearman++;
				this.animationLoadingSpearman=this.animationLoadingSpearman%(Main.framerate/2);
				int height = this.loadingSpearman.getHeight()/4;
				int width = this.loadingSpearman.getWidth()/5;
				int w = animationLoadingSpearman*8/Main.framerate+1;
				g.drawImage(this.loadingSpearman.getSubImage(w*width,height,width,height),xanimation-width/2,startBarY-sizeBarY-height);
			}	
			if(this.rate){
				g.setColor(Color.white);
				String s;
				if(this.gilles){
					s = "Trop tard";
				} else {
					s = "Trop tôt";
				}
				g.drawString(s, Game.resX-10-font.getWidth(s), Game.resY-20-font.getHeight(s));
			}
			//g.drawImage(this.loadingSpearman.getSubImage(((w+2)%4)*width,height,width,height),startBarX/2-width/2,startBarY+sizeBarY/2-height/2);
			//				g.setColor(Color.white);
			//				String s = "Chargement...";
			//				g.drawString(s, 7*resX/8,16*resY/20+height/2-font.getHeight(s)/2);
		}
		Image temp = this.loadingBackground;
		temp = this.loadingTitle.getScaledCopy(0.5f);
		temp.setAlpha(1-toGoTitle2);
		float xTitle = (Game.resX/2-temp.getWidth()/2) ;
		float yTitle = (Game.resY/3);
		g.drawImage(temp, xTitle, yTitle);
		return;

	}

	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		
		if (LoadingList.get().getRemainingResources() > 0) { 
			if(waitLoading == false){
				waitLoading = true;
				try {Thread.sleep(10);} catch (InterruptedException e) {}
				return;
			}
			nextResource = LoadingList.get().getNext(); 
			try {
				nextResource.load();
				lastThing = nextResource.getDescription();
				updateGillesBonus();
			} catch (IOException e) {
				e.printStackTrace();
			}
			waitLoading = false;
			return;
		} else if(toGoTitle<1f) {
			if(toGoTitle>0)
				toGoTitle+=0.02f;
			else
				toGoTitle+=0.002f;
			return;
		} else if(thingsLoaded==false){
			this.handleEndLoading();
			thingsLoaded = true;
			return;
		}
	}
	
	public void updateGillesBonus(){
		// pressing espace makes gilles appear (bonus)
		if(nextResource.getDescription().contains("gilles") ){
			if(!rate)
				gilles = true;
			gillesPasse+=3;
		}
		if(gillesPasse>0)
			gillesPasse--;
		if(Debug.gillesEspaceEnable && Game.app.getInput().isKeyPressed(Input.KEY_SPACE) && !this.rate){
			if(gillesPasse>0 ){
				this.loadingSpearman = this.loadingGilles;
				this.special = true;
			} else {
				this.rate = true;
			}
		}
	}
	
	public void handleEndLoading(){
		Game.app.setMinimumLogicUpdateInterval(1000/Main.framerate);
		Game.app.setMaximumLogicUpdateInterval(1000/Main.framerate);
		Game.app.setTargetFrameRate(Main.framerate);
		LoadingList.setDeferredLoading(false);
		
//		Lobby.init();
//		Game.gameSystem = new WholeGame();
//		Game.system = Game.gameSystem;
		Game.menuSystem = new MenuSystem();
		Game.system = Game.menuSystem;
	}

	
}
