package IA;

import java.util.Vector;

import org.newdawn.slick.geom.Point;

import model.Checkpoint;
import units.Character;

public class IAUnit {
	
	public Character unit;
	
	static int n_features = 6;
	float[][] X = new float[4][n_features];
	Vector<Vector<Float>> features;
	
	int m_enemies;
	Vector<Character> enemies = new Vector<Character>();
	
	float[] directions; 
	
	public IAUnit(Character c, float[][] Xc){
		this.cleanFeatures();
		this.unit =c;
		this.initializeBehavior(Xc);
		
	}
	
	public Checkpoint moveInBattle(Vector<Character> enemies){
		this.computeEnnemies(enemies);
		this.computeDirection();
		//return this.computeVector();
		return null;
	}
	
	// Initializing the matrix
 	public void initializeBehavior(float[][] Xc){
		if(Xc.length != X.length || Xc[0].length != Xc[0].length){
			return;
		}
		for(int i=0;i<X.length;i++){
			for(int j=0; j<X[0].length; j++){
				this.X[i][j] = Xc[i][j];
			}
		}
	}
	
	// Auxiliary computation functions
	public void computeEnnemies(Vector<Character> enemies){
		this.enemies = enemies;
		this.m_enemies = enemies.size();
		this.cleanFeatures();
		for(Character c:enemies){
			this.addFeatures(c);
		}
	}
	public void computeDirection(){
		directions = new float [4];
		/*
		 * directions[0] = bas
		 * directions[1] = gauche
		 * directions[2] = droite
		 * directions[3] = haut
		 */
		float sum = 0f;
		for(int dir = 0; dir<4; dir++){
			sum = 0f;
			for(int en =0; en<this.m_enemies; en++){
				for(int feat=0; feat<n_features; feat++){
					sum+=X[dir][feat]*features.get(feat).get(en);
				}
			}
			directions[dir] = sum;
//			System.out.print(sum+" ");
		}
//		System.out.println();
	}
//	public Checkpoint computeVector(){
//		Checkpoint p = new Checkpoint(unit.x, unit.y);
//		Checkpoint v = new Checkpoint(directions[2]-directions[1],directions[0]-directions[3]);
//		float vNorm = (float)Math.sqrt(v.getX()*v.getX()+v.getY()*v.getY());
//		p.x=(p.getX()+10f*v.getX()/vNorm);
//		p.y=(p.getY()+10f*v.getY()/vNorm);
//		return p;
//	}

	// Handling the features
	public void cleanFeatures(){
		features = new Vector<Vector<Float>>();
		for(int i=0; i<n_features; i++){
			features.add(new Vector<Float>());
		}
	}
	public void addFeatures(Character c){
		// distance according to x
		features.get(0).add(c.x-unit.x);
		// distance according to y
		features.get(1).add(c.y-unit.y);
		// relative to the boundary
		features.get(2).add(1/(c.p.maxX-c.x));
		features.get(3).add(1/c.x);
		features.get(4).add(1/(c.p.maxY-c.y));
		features.get(5).add(1/c.y);
	}
	
	
	
}
