package IA;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import buildings.Building;
import bullets.Bullet;
import model.Game;
import model.Plateau;
import pathfinding.MapGrid;
import spells.SpellEffect;
import units.Character;
import units.UnitArchange;
import units.UnitCrossbowman;
import units.UnitInquisitor;
import units.UnitKnight;
import units.UnitPriest;
import units.UnitSpearman;
public class Simulation {


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
	public float sizeX = 400;
	public float sizeY = 400;

	// BUFFER 
	public final int BUFFERS = 2;
	public  BufferStrategy bufferStrategy;

	public int sizeArmy1;
	public int sizeArmy2;
	public int timeout;
	public int ratio ;
	public Simulation (Game game){
		//INIT SIZE ARMY
		this.sizeArmy1= (int)(Math.random()*10);
		this.sizeArmy2 = (int)(((float)Math.random()-0.5f)*5)+this.sizeArmy1;
		this.ratio = 2;
		// INIT GAME AND PLATEAU
		this.framerate = 60;
		this.game = game;

		end = false;
		this.p = new Plateau(sizeX,sizeY,this.game);
		this.game.plateau = this.p;

		
		//INIT ARMIES
		armies = new Vector<Vector<Character>>();
		armies.add(new Vector<Character>());
		armies.add(new Vector<Character>());

		float x, y;
		// GENERATE RANDOM ARMY
		for(int i=0 ;i<this.sizeArmy1;i++){
			Character charact = generateRandomUnit(1);
			x = (float)Math.random()*this.sizeX/2f+this.sizeX/4f;
			y = (float)Math.random()*this.sizeY/2f+this.sizeY/4f;
			Character c = this.p.g.players.get(1).create(charact.type, x, y);
			this.armies.get(0).add(c);

		}
		for(int i=0 ;i<this.sizeArmy2;i++){
			Character charact = generateRandomUnit(2);
			x = (float)Math.random()*this.sizeX/2f+this.sizeX/4f;
			y = (float)Math.random()*this.sizeY/2f+this.sizeY/4f;
			Character c = this.p.g.players.get(2).create(charact.type, x, y);
			this.armies.get(1).add(c);
		}



		// ADD ARMY IN PLATEAU
		for(Vector<Character> cs : armies){
			for(Character c : cs){
				new Character(c,c.x,c.y);

			}
		}

		System.out.println("new configuration:");
		System.out.println("army 1: "+armies.get(0).size());
		System.out.println("army 2: "+armies.get(1).size());
		//Utils.printCurrentState(p);
		// INIT GRID
		p.mapGrid = new MapGrid(0f, p.maxX,0f, p.maxY);
		//INIT RENDER
		window = new JFrame();
		panel = new JPanel();

		window.setSize((int) sizeX*ratio, (int) sizeY*ratio);

		//Nous demandons maintenant à notre objet de se positionner au centre

		window.setLocationRelativeTo(null);

		//Termine le processus lorsqu'on clique sur la croix rouge

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		window.setContentPane(panel);
		window.setVisible(true);
		// BUFFER STRATEGY
		this.window.createBufferStrategy(2);
		this.bufferStrategy = this.window.getBufferStrategy();
		

	}

	public void simulate(){
		//Call update until victory or timeout
		end = false;
		while(!end){

			update();
			render();
			//Utils.printCurrentState(p);
		}

		// if victory call report function
		if(end){
			System.out.println(victory+" "+armies.get(victory-1).size());
			report = new Report(victory,armies.get(victory-1).size());
			this.window.setVisible(false);
		}

	}

	public void render(){
		Graphics2D g;

		g= (Graphics2D) bufferStrategy.getDrawGraphics();

		

		g.setColor(Color.gray);
		g.fillRect(0, 0, (int) sizeX*ratio,(int) sizeY*ratio);
		for(Character c : this.p.characters){
			if(c.team ==1 ){
				g.setColor(Color.blue);
			}
			if(c.team == 2 ){
				g.setColor(Color.red);	
			}

			//g.fillRect((int) c.x*ratio, (int)c.y*ratio, 10, 10);
			g.fillOval((int) (c.x-c.collisionBox.getBoundingCircleRadius())*ratio,(int) (c.y-c.collisionBox.getBoundingCircleRadius())*ratio,(int) c.collisionBox.getBoundingCircleRadius()*ratio*2, (int)(c.collisionBox.getBoundingCircleRadius()*ratio*2));

			g.setColor(Color.BLACK);
			g.drawString(c.id+ ""+c.weapon, (int) c.x*ratio,(int) c.y*ratio);
			if(c.getTarget()!=null){
				g.drawLine((int) c.x*ratio, (int) c.y*ratio, (int) c.getTarget().x*ratio, (int) c.getTarget().y*ratio);
			}
			// DRAW SIGHTBOX 

			g.setColor(Color.BLACK);
			g.drawOval((int) (c.x-c.sightBox.radius)*ratio,(int) (c.y-c.sightBox.radius)*ratio,(int) c.sightBox.radius*ratio*2, (int)(c.sightBox.radius*ratio*2));


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
		for(Vector<Character> cs : armies){
			for(Character c : cs){
				if(c.lifePoints<=0){
					toRemove.add(c);
					System.out.println("death: "+c.team);
				}

			}
		}

		for(Character c : toRemove){
			if(armies.get(0).contains(c)){
				armies.get(0).remove(c);
			}
			if(armies.get(1).contains(c)){
				armies.get(1).remove(c);
			}
		}

		//Update end
		if(this.armies.get(0).size()==0){
			end = true;
			victory = 2;
		}
		else if(this.armies.get(1).size()==0){
			end = true;
			victory = 1;
		}

	}

	public void report(){
		//Call action on each character until end of fight
	}

	public Character generateRandomUnit(int team){
		int n_units = 5;
		Character c;
		int i = (int) (Math.random()*n_units);
		switch(i){
		case 0:
			c =  new UnitSpearman(this.p,this.game.players.get(team),this.game.players.get(team).data);	

			break;
		case 1:
			c = new UnitKnight(this.p,this.game.players.get(team),this.game.players.get(team).data);	

			break;
		case 2:
			c =  new UnitPriest(this.p,this.game.players.get(team),this.game.players.get(team).data);

			break;	
		case 3:
			c =  new UnitCrossbowman(this.p,this.game.players.get(team),this.game.players.get(team).data);

			break;	
		case 4:
			c =  new UnitInquisitor(this.p,this.game.players.get(team),this.game.players.get(team).data);

			break;
		case 5:
			c = new UnitArchange(this.p,this.game.players.get(team),this.game.players.get(team).data);

			break;

		default:
			c = new UnitSpearman(this.p,this.game.players.get(team),this.game.players.get(team).data);	
		}
		return c;

	}



}
