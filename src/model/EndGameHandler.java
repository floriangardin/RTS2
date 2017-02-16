package model;

import java.net.SocketException;

import ressources.Map;

public class EndGameHandler {
	
	Building destroyedHQ;
	boolean movementPhase = true;
	int timerBeforeEnd = 1;
	
	public EndGameHandler(){
		
	}
	
	public void init(Building destroyedHQ){
		this.destroyedHQ = destroyedHQ;
		Game.g.cameraMovementAllowed = false;
	}
	
	public void update(){
		if(movementPhase){
			this.handleCameraMovement();
		} else {
			
		}
		if(timerBeforeEnd<=0){
			this.updateEndScreen();
		}
	}
	
	public void handleCameraMovement(){
		int deltaX = (int) (destroyedHQ.getX()-Game.g.Xcam);
		int deltaY = (int) (destroyedHQ.getY()-Game.g.Ycam);
		Game.g.Xcam += deltaX/15;
		Game.g.Ycam += deltaY/15;
		if(Math.abs(deltaX)<2)
			movementPhase = false;
	}
	
	public void updateEndScreen(){
		// handle victory / defeat screen
		if(Game.g.victory && Game.g.musicPlaying!=Game.g.musics.get("themeVictory")){
			Game.g.musicPlaying.stop();
			Game.g.musicPlaying = Game.g.musics.get("themeVictory");
			Game.g.musicPlaying.play(1f, Game.g.options.musicVolume);
		} else if(!Game.g.victory && Game.g.musicPlaying!=Game.g.musics.get("themeDefeat")) {
			Game.g.musicPlaying.stop();
			Game.g.musicPlaying = Game.g.musics.get("themeDefeat");
			Game.g.musicPlaying.play(1f, Game.g.options.musicVolume);
		} 
		//Print victory !!
		Game.g.victoryTime --;
		if(Game.g.victoryTime <0){
			Game.g.musicPlaying.stop();
			Game.g.victoryTime = 120;
			Game.g.isInMenu = true;
			Game.g.hasAlreadyPlay = true;
			Game.g.endGame = false;
			try {
				Game.g.server.setBroadcast(true);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			Map.initializePlateau(Game.g, 1, 1);
			Game.g.setMenu(Game.g.menuIntro);
		}
		
	}
	

}
