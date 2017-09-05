package ressources;

import java.util.Vector;

import control.Player;
import plateau.Plateau;
import plateau.Character;
import plateau.Building;

public class MusicManager {

	
	
	
	public static boolean isInit = false;
	public static boolean firstRound = true;
	public static int cantChange = 0;
	public static int maxCantChange = 500;
	
	public enum MusicList{
		
		guerre_mort(4,0),
		guerre(3,0),
		harpe_inquiete(1,0),
		//heureux(-1,0),
		//mystere_decouverte(-1,0),
		piano(0,0),
		//tambourin_debut_guerre(2,0),
		tambourin_sans_wind(2,0),
		
		vent(0,0),
		
		
		vent_enclume(0,1);
		
		int intensite;
		int positivisme;
		
		private MusicList(int intensite, int positivisme){
			this.intensite = intensite;
			this.positivisme = positivisme ;
		}
		
		public static int getCurrentIntensite(){
			String name = Musics.getPlayingMusicName();
			if(name != null){
				try{					
					return valueOf(name).intensite;
				}catch(Exception e){
					return 0;
				}
			}else{
				return 0;
			}
		}
		
		public static int getCurrentPositivisme(){
			String name = Musics.getPlayingMusicName();
			if(name != null){
				try{					
					return valueOf(name).positivisme;
				}catch(Exception e){
					return 0;
				}
			}else{
				return 0;
			}
		}

		public static Vector<MusicList> getMusicsByIntensite(int intensite2) {
			// TODO Auto-generated method stub
			Vector<MusicList> res=  new Vector<MusicList>();
			for(MusicList m : values()){
				
				if(m.intensite==intensite2){
					res.add(m);
				}
			}
			return res;
		}
	}
	
	public static void init(){
		isInit = true;
	}
	public static void update(Plateau plateau){
		cantChange--;
		if(cantChange>0){
			return;
		}
		if(!MusicManager.isInit){
			return;
		}
		if(firstRound){
			Musics.playMusic(MusicList.vent.name());
			cantChange = maxCantChange;
			firstRound = false;
		}
		
		int intensite = calculateIntensite(plateau);
		int currentIntensite = MusicList.getCurrentIntensite();
		if(intensite==currentIntensite){
			// Maybe add random looping if intensite small
			return;
		}else{
			// Change music at random according to intensite
			Vector<MusicList> potentialMusics = MusicList.getMusicsByIntensite(intensite);
			int random = (int) Math.floor(potentialMusics.size()*Math.random());
			Musics.playMusic(potentialMusics.get(random).name());
			cantChange = maxCantChange;
		}
		
	}
	
	
	public static int calculateIntensite(Plateau plateau){
		// Count the number of ennemy visible
		int numberBuildingsVisible = (int)plateau.get().filter(x-> x instanceof Building && x.team.id!=Player.getTeamId() && x.team.id!=0 && plateau.isVisibleByTeam(Player.team, x)).count();
		int numberCharactersVisible = (int)plateau.get().filter(x-> x instanceof Character && x.team.id!=Player.getTeamId() && x.team.id!=0 && plateau.isVisibleByTeam(Player.team, x)).count();
		int numberEnnemyTargets = (int)plateau.get().filter(x-> x instanceof Character && x.team.id==Player.getTeamId() && x.getTarget(plateau)!=null && x.getTarget(plateau).team.id!=Player.team && x.getTarget(plateau).team.id!=0).count();
		int numberEnnemyVisibleTargettingYou = (int)plateau.get().filter(x-> x instanceof Character && x.team.id!=Player.getTeamId() && x.team.id!=0 && plateau.isVisibleByTeam(Player.team, x) && x.getTarget(plateau)!=null &&  x.getTarget(plateau).team.id==Player.team).count();
		
		int res = 0;
		if(numberBuildingsVisible>0){
			res++;
		}
		if(numberCharactersVisible>0 ){
			res++;
		}
		if(numberEnnemyTargets>0){
			res++;
		}
		if(numberEnnemyVisibleTargettingYou > 2){
			res++;
		}
		return res;
	}
}
