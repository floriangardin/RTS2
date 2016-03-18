package IAProject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import main.Main;
import menu.Menu_TextScanner;
import model.Game;
import multiplaying.InputObject;
import ressources.Map;
import units.Character;

public class GameSimuProject extends Game{

	boolean inGame;
	Vector<ArmyCompo> armies;
	ArmyCompo currentArmy;
	Vector<Results> results = new Vector<Results>();
	
	int nbRound=0;
	int nbGame = 200;


	public GameSimuProject(float resX, float resY) {
		super(resX, resY);
		Main.framerate = 1000;
		// TODO Auto-generated constructor stub
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {

	}

	public void update(GameContainer gc, int t) throws SlickException {	
		//game init
		initGame();
		// main game loop
		if(inGame){
			InputObject im = new InputObject(this,currentPlayer,gc.getInput(),true);
			Vector<InputObject> ims = new Vector<InputObject>();
			ims.add(im);
			this.plateau.update(ims);
			this.plateau.updatePlateauState();
			nbRound++;
		}
		checkEndGame();
	}

	public void initGame() {
		if(!inGame){
			if(this.plateau==null)
				Map.initializePlateau(this, 2000f, 3000f);
			currentArmy = new ArmyCompo(5);
			this.initializePlayers();
			Map.createMapMicro(this, currentArmy);
			inGame = true;
		}
	}

	public void checkEndGame() {
		if(inGame){
			// check end of the game
			boolean one = true, two = true;
			for(Character c: this.plateau.characters){
				if(c.getTeam()==1)
					two = false;
				if(c.getTeam()==2)
					one = false;
			}
			if(one || two || nbRound>10000){
				inGame = false;
				System.out.print("fin du jeu "+nbGame+" en "+nbRound+" tours - gagnant: ");
				int w =0;
				if(one){
					w = 1;
					System.out.println("1");
				}else{
					w = 2;
					System.out.println("2");
				}
				this.results.addElement(new Results(currentArmy,w,nbRound));
				nbRound = 0;
				nbGame--;
				if(nbGame==0){
					this.printResults();
					this.app.exit();
				}
			}
		}
	}

	public void printResults(){
		String path = "C:/Users/Kévin/Documents/Polytechnique/Année 4/Probabilistic Graphics Model/projet/data/rts/";
		File repertoire = new File(path);
		File[] files=repertoire.listFiles();
		int l = files.length;
		try {
			FileWriter fw = new FileWriter (path+"X"+l+".txt");
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter fichierSortie = new PrintWriter (bw); 
			for(Results r : this.results){
				if(r.winner==2)
					r.switchArmies();
				fichierSortie.println (r);
			}
			fichierSortie.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}	
	}

	public class ArmyCompo{
		public static final int nbUnit = 4;
		//equipe 1
		public Vector<Integer> army1 = new Vector<Integer>();
		//equipe 2
		public Vector<Integer> army2 = new Vector<Integer>();

		public ArmyCompo(String s){
			// generate two armies from a string
			String[] tab = s.split(" ");
			for(int i=0; i<tab.length; i++){
				if(i<nbUnit){
					army1.add(Integer.parseInt(tab[i]));
				} else {
					army2.add(Integer.parseInt(tab[i]));
				}
			}
		}
		public ArmyCompo(int n){
			// generate two random armies of size n each
			for(int i=0; i<nbUnit; i++){
				army1.add(0);
				army2.add(0);
			}
			int t;
			for(int i=0; i<n; i++){
				t = (int)(Math.random()*nbUnit);
				army1.set(t, army1.get(t)+1);
				t = (int)(Math.random()*nbUnit);
				army2.set(t, army2.get(t)+1);
			}
		}
		public String toString(){
			String s = "";
			for(Integer i : army1){
				s+=i+" ";
			}
			for(Integer i : army2){
				s+=i+" ";
			}
			return s;
		}
	}

	public class Results{
		ArmyCompo army;
		int winner;
		int nbRound;

		public Results(ArmyCompo a, int w, int n){
			army = a;
			winner = w;
			nbRound = n;
		}
		public void switchArmies(){
			Vector<Integer> a = this.army.army1;
			this.army.army1 = this.army.army2;
			this.army.army2 = a;
		}

		public String toString(){
			String s="";
			s+="" + army;
			return s;
		}
	}
}
