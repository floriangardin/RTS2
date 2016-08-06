package IA;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import data.Attributs;
import model.Game;
import model.Plateau;
import pathfinding.MapGrid;
import units.Character;
public class Simulation {

	public boolean render= true;

	public Vector<Vector<Character>> armies;
	public JPanel panel;
	public JFrame window;
	public Vector<Vector<Character>> armiesInitial;
	public Plateau p;
	public boolean end;
	public int victory;
	public Report report;
	public Game game;
	public int framerate ;
	public float sizeX = 1000;
	public float sizeY = 1000;

	// BUFFER 
	public final int BUFFERS = 2;
	public  BufferStrategy bufferStrategy;

	public int sizeArmy1;
	public int sizeArmy2;
	public int timeout;
	public float ratio ;

	public boolean sleep = false;

	public Simulation (Game game){

		// creating the simulation environment
		this.game = game;
		this.initializeSimulation();

		// creating the simulation characters
		//this.createBasicSimu();




	}

	public void simulate(){
		//Call update until victory or timeout
		end = false;
		while(!end){

			update();
			if(render)
				render();
			//Utils.printCurrentState(p);
		}

		// if victory call report function
		if(end){
			System.out.println("victoire de l'�quipe: "+victory);
			report = new Report(victory,armies.get(victory-1).size());
			if(render)
				this.window.setVisible(false);
		}

	}

	public void render(){
		Graphics2D g;

		g= (Graphics2D) bufferStrategy.getDrawGraphics();



		g.setColor(Color.gray);
		g.fillRect(0, 0, (int) (sizeX*ratio),(int) (sizeY*ratio));
		for(Character c : this.p.characters){
			if(c.getTeam() ==1 ){
				g.setColor(Color.blue);
			}
			if(c.getTeam() == 2 ){
				g.setColor(Color.red);	
			}

			//g.fillRect((int) c.x*ratio, (int)c.y*ratio, 10, 10);
			g.fillOval((int) ((c.x-c.collisionBox.getBoundingCircleRadius())*ratio),(int) ((c.y-c.collisionBox.getBoundingCircleRadius())*ratio),(int) ((c.collisionBox.getBoundingCircleRadius())*ratio*2), (int)((c.collisionBox.getBoundingCircleRadius()*ratio)*2));

			g.setColor(Color.BLACK);
			g.drawString(c.id+ ""+c.getAttributString(Attributs.weapon), (int) c.x*ratio,(int) c.y*ratio);
			if(c.getTarget()!=null){
				g.drawLine((int) (c.x*ratio), (int) (c.y*ratio), (int) (c.getTarget().x*ratio), (int) (c.getTarget().y*ratio));
			}


			// DRAW LIFE
			g.setColor(Color.RED);
			g.drawLine((int)((c.x-2*c.getAttribut(Attributs.size))*ratio),(int)((c.y-2*c.getAttribut(Attributs.size))*ratio), (int)((c.x+2*c.getAttribut(Attributs.size))*ratio), (int)((c.y-2*c.getAttribut(Attributs.size))*ratio));
			g.setColor(Color.GREEN);
			g.drawLine((int)((c.x-2*c.getAttribut(Attributs.size))*ratio),(int)((c.y-2*c.getAttribut(Attributs.size))*ratio), (int)((c.x-2*c.getAttribut(Attributs.size)+4*c.getAttribut(Attributs.size)*c.lifePoints/c.getAttribut(Attributs.maxLifepoints))*ratio), (int)((c.y-2*c.getAttribut(Attributs.size))*ratio));
		}

		bufferStrategy.show();
		g.dispose();

	}
	public void update(){
		//Call action on each character until end of fight
		this.p.collision();
		this.p.clean();
		this.p.action();


		// Remove characters if death
		Vector<Character> toRemove = new Vector<Character>();
		boolean victory1 = false, victory2 =false;
		victory1 = true;
		victory2 = true;
		for(Vector<Character> cs : armies){
			for(Character c : cs){
				if(c.lifePoints>0){
					if(c.getTeam()==1){
						victory2=false;
					}
					if(c.getTeam()==2){
						victory1=false;
					}
				}
			}
		}

		//Update end
		if(victory2){
			end = true;
			victory = 2;
		}
		else if(victory1){
			end = true;
			victory = 1;
		}
		if(sleep){
			try{
				Thread.sleep(16);
			}catch(InterruptedException e){

			}
		}
	}

	public void report(){
		//Call action on each character until end of fight
	}

	public void initializeSimulation(){
		// INIT GAME AND PLATEAU
		this.framerate = 60;
		this.ratio = 800f/sizeX;
		end = false;
		this.p = new Plateau(sizeX,sizeY,this.game);
		this.game.plateau = this.p;


		//INIT ARMIES
		armies = new Vector<Vector<Character>>();
		armies.add(new Vector<Character>());
		armies.add(new Vector<Character>());
		//Utils.printCurrentState(p);
		// INIT GRID
		p.mapGrid = new MapGrid(0f, p.maxX,0f, p.maxY);
		//INIT RENDER
		if(render){
			window = new JFrame();
			panel = new JPanel();

			window.setSize((int) (sizeX*ratio), (int) (sizeY*ratio));
			window.setLocationRelativeTo(null);
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setContentPane(panel);
			window.setVisible(true);
			this.window.createBufferStrategy(2);
			this.bufferStrategy = this.window.getBufferStrategy();
		}
	}
//
//	public Character generateRandomUnit(int team){
//		int n_units = 5;
//		Character c;
//		int i = (int) (Math.random()*n_units);
//		switch(i){
//		case 0:
//			c =  new UnitSpearman(this.p,this.game.plateau.getTeamById(team),this.game.plateau.getTeamById(team).data);	
//
//			break;
//		case 1:
//			c = new UnitKnight(this.p,this.game.plateau.getTeamById(team),this.game.plateau.getTeamById(team).data);	
//
//			break;
//		case 2:
//			c =  new UnitPriest(this.p,this.game.plateau.getTeamById(team),this.game.plateau.getTeamById(team).data);
//
//			break;	
//		case 3:
//			c =  new UnitCrossbowman(this.p,this.game.plateau.getTeamById(team),this.game.plateau.getTeamById(team).data);
//
//			break;	
//		case 4:
//			c =  new UnitInquisitor(this.p,this.game.plateau.getTeamById(team),this.game.plateau.getTeamById(team).data);
//
//			break;
//		case 5:
//			c = new UnitArchange(this.p,this.game.plateau.getTeamById(team),this.game.plateau.getTeamById(team).data);
//
//			break;
//
//		default:
//			c = new UnitSpearman(this.p,this.game.plateau.getTeamById(team),this.game.plateau.getTeamById(team).data);	
//		}
//		return c;
//
//	}
//
//	// types of simu
//	public void createRandomSimu(){
//		//INIT SIZE ARMY
//		this.sizeArmy1= (int)(Math.random()*10);
//		this.sizeArmy2 = (int)(((float)Math.random()-0.5f)*5)+this.sizeArmy1;
//		float x, y;
//		// GENERATE RANDOM ARMY
//		for(int i=0 ;i<this.sizeArmy1;i++){
//			Character charact = generateRandomUnit(1);
//			x = (float)Math.random()*this.sizeX/2f+this.sizeX/4f;
//			y = (float)Math.random()*this.sizeY/2f+this.sizeY/4f;
//			Character c = this.p.getTeamById(1).data.create(charact.type, x, y);
//			this.armies.get(0).add(c);
//
//		}
//		for(int i=0 ;i<this.sizeArmy2;i++){
//			Character charact = generateRandomUnit(2);
//			x = (float)Math.random()*this.sizeX/2f+this.sizeX/4f;
//			y = (float)Math.random()*this.sizeY/2f+this.sizeY/4f;
//			Character c = this.p.getTeamById(2).data.create(charact.type, x, y);
//			this.armies.get(1).add(c);
//		}
//
//	}	
//	public void createBasicSimu(){
//
//		//INIT SIZE ARMY
//		this.sizeArmy1= 1;
//		this.sizeArmy2 = 2;
//
//		float[][] Xc = new float[4][IAUnit.n_features];
//		for(int i=0; i<4;i++){
//			for(int j=0; j<IAUnit.n_features; j++){
//				Xc[i][j] = (float)(Math.random()-0.5);
//			}
//		}
//
//		// GENERATE ARMY
//		Character c = this.p.getTeamById(1).data.create(UnitsList.Crossbowman, sizeX/2f, sizeY/2f);
//		c.ia = new IAUnit(c,Xc);
//		this.armies.get(0).add(c);
//
//		c = this.p.getTeamById(2).data.create(UnitsList.Spearman, sizeX/2f-150f, sizeY/2f-150f);
//		this.armies.get(1).add(c);
////		c = this.p.g.players.get(2).create(UnitsList.Spearman, sizeX/2f+150f, sizeY/2f+150f);
////		this.armies.get(1).add(c);
//
//	}

}
