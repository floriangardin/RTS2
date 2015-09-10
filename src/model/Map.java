package model;

import java.util.Vector;

public class Map {

	public Map(){

	}

	

	public void createMapLan(Plateau plateau,Vector<Player> players){
		plateau.maxX = 2000f;
		plateau.maxY = 3000f;
		Data data1 = players.get(1).data;
		Data data2 = players.get(2).data;
		data1.create(UnitsList.Crossbowman, plateau.maxX/2-1f, 300f);
		data1.create(UnitsList.Knight, plateau.maxX/2, 300f);
		data1.create(UnitsList.Inquisitor, plateau.maxX/2+1f, 300f);
		data1.create(UnitsList.Priest, plateau.maxX/2+2f, 300f);
		data1.create(UnitsList.Spearman, plateau.maxX/2+3f, 300f);
		
		new BuildingMill(plateau,plateau.g,150f,100f);
		new BuildingMine(plateau,plateau.g,plateau.maxX-200f,100f);
		new BuildingBarrack(plateau,plateau.g,plateau.maxX/2,100f);
		
		new BuildingStable(plateau,plateau.g,plateau.maxX/4,2*plateau.maxY/5);
		new BuildingAcademy(plateau,plateau.g,3*plateau.maxX/4,2*plateau.maxY/5);
		
		data2.create(UnitsList.Crossbowman, plateau.maxX/2-1f, plateau.maxY-300f);
		data2.create(UnitsList.Knight, plateau.maxX/2,  plateau.maxY-300f);
		data2.create(UnitsList.Inquisitor, plateau.maxX/2+1f,  plateau.maxY-300f);
		data2.create(UnitsList.Priest, plateau.maxX/2+2f,  plateau.maxY-300f);
		data2.create(UnitsList.Spearman, plateau.maxX/2+3f,  plateau.maxY-300f);
		
		new BuildingMill(plateau,plateau.g,150f,plateau.maxY-200f);
		new BuildingMine(plateau,plateau.g,plateau.maxX-200f,plateau.maxY-200f);
		new BuildingBarrack(plateau,plateau.g,plateau.maxX/2,plateau.maxY-200f);
		
		HeadQuarters team1h = new HeadQuarters(plateau,plateau.g,plateau.maxX/2,1*plateau.maxY/5,1);
		team1h.constructionPoints = team1h.maxLifePoints-1f;
		// Stables and academy 
		new BuildingStable(plateau,plateau.g,plateau.maxX/4, 3*plateau.maxY/5);
		new BuildingAcademy(plateau,plateau.g,3f*plateau.maxX/4, 3*plateau.maxY/5);
		
		// Barrack in the middle
		new BuildingMill(plateau,plateau.g,150f,plateau.maxY/2);
		new BuildingMine(plateau,plateau.g,plateau.maxX-200f,plateau.maxY/2);
		new BuildingBarrack(plateau,plateau.g,plateau.maxX/2,plateau.maxY/2);
		
		HeadQuarters team2h = new HeadQuarters(plateau,plateau.g,plateau.maxX/2,4*plateau.maxY/5,2);
		team2h.constructionPoints = team2h.maxLifePoints-1f;
		
	}
	public void createMap2(Plateau plateau){

		float x1,y1;
		for(int i=0; i<10; i++){
			x1 = (float)(Math.random()*(4f*plateau.maxX/6f-30f)+15f);
			y1 = (float)(Math.random()*(4f*plateau.maxY/6f-30f)+15f);
			new Tree(x1,y1,plateau,(int)(Math.random()*4f+1f));
		}

		//Bord de la map
		new Water(plateau.maxX/2f,-plateau.maxX/2f+32f,plateau.maxX,plateau.maxX,plateau);
		new Water(plateau.maxX/2f,plateau.maxY+plateau.maxX/2f-32f,plateau.maxX,plateau.maxX,plateau);
		new Water(-plateau.maxY/2f+32f,plateau.maxY/2f,plateau.maxY,plateau.maxY,plateau);
		new Water(plateau.maxX+plateau.maxY/2f-32f,plateau.maxY/2f,plateau.maxY,plateau.maxY,plateau);

		//Water
		new Water(plateau.maxX/12f,plateau.maxY/6f,plateau.maxX/6f,plateau.maxY*2f/6f,plateau);
		new Water(11f*plateau.maxX/12f,5f*plateau.maxY/6f,plateau.maxX/6f,plateau.maxY*2f/6f,plateau);
		new Water(plateau.maxX/12f,5f*plateau.maxY/6f,plateau.maxX/6f,plateau.maxY*2f/6f,plateau);
		new Water(11f*plateau.maxX/12f,plateau.maxY/6f,plateau.maxX/6f,plateau.maxY*2f/6f,plateau);


		//Instantiate allies
		Character[] team = new Character[5];
		for(int i=0;i<5;i++){		
			team[i]=new Character(plateau,0,500f+10f*i,500f);
			plateau.g.players.get(0).groups.get(i).add(team[i]);
		}
		team[0].setXY(plateau.maxX-120f+20f, plateau.maxY/2f);
		team[1].setXY(plateau.maxX-120f-20f, plateau.maxY/2f);
		team[2].setXY(plateau.maxX-120f, plateau.maxY/2f+20f);
		team[3].setXY(plateau.maxX-120f, plateau.maxY/2f-20f);
		team[4].setXY(plateau.maxX-120f, plateau.maxY/2f);



		// Give equipement to team 
		// 0 : sword heavy armor, 1: Bow light armor , 2: Horse sword medium armor, 3: Bible no armor, 4:magician no armor
		team[0].collectWeapon(new Sword(plateau,team[0]));
		
		team[1].collectWeapon(new Bow(plateau,team[1]));
		
		team[2].collectWeapon(new Sword(plateau,team[2]));
		team[2].collectHorse(new Horse(plateau, team[2]));

		team[3].collectWeapon(new Bible(plateau,team[3]));
		team[4].collectWeapon(new Wand(plateau,team[4]));


		// Instantiate enemy generator :
		new EnemyGenerator(plateau,plateau.g,120f,plateau.maxY/2f-10f);


	}


}
