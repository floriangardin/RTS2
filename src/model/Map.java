package model;

public class Map {

	public Map(){

	}

	public void createMap1(Plateau plateau){
		//Instantiate allies
		Character[] team = new Character[5];
		for(int i=0;i<5;i++){		
			team[i]=new Character(plateau,0,500f+10f*i,500f);
			plateau.g.players.get(0).groups.get(i).add(team[i]);
		}
		// Give equipement to team 
		// 0 : sword heavy armor, 1: Bow light armor , 2: Horse sword medium armor, 3: Bible no armor, 4:magician no armor
		team[0].collectWeapon(new Sword(plateau,team[0]));
		team[0].collectArmor(new HeavyArmor(team[0].getX(),team[0].getY(),plateau,team[0]));

		team[1].collectWeapon(new Bow(plateau,team[1]));
		team[1].collectArmor(new LightArmor(team[1].getX(),team[1].getY(),plateau,team[1]));

		team[2].collectWeapon(new Sword(plateau,team[2]));
		team[2].collectArmor(new MediumArmor(team[2].getX(),team[2].getY(),plateau,team[1]));
		team[2].collectHorse(new Horse(plateau, team[2]));

		team[3].collectWeapon(new Bible(plateau,team[3]));
		team[4].collectWeapon(new Balista(plateau,team[4]));


		for(int i = 0;i<9; i++){
			new Water(395f+32*i,570f,32f,32f,plateau);
		}
		for(int i = 0;i<3; i++){
			new Tree(368f,490f+32*i,plateau,4);
			new Tree(682f,490f+32*i,plateau,4);
		}
		new Tree(200f,400f,plateau,1);
		// Instantiate enemy generator :
		new EnemyGenerator(plateau,plateau.g,520f,100f);

	}
	
	public void createMap2(Plateau plateau){
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
		
		float x1,y1;
		for(int i=0; i<10; i++){
			x1 = (float)(Math.random()*(4f*plateau.maxX/6f-30f)+15f);
			y1 = (float)(Math.random()*(4f*plateau.maxY/6f-30f)+15f);
			new Tree(x1,y1,plateau,(int)(Math.random()*4f+1f));
		}
		
		// Give equipement to team 
		// 0 : sword heavy armor, 1: Bow light armor , 2: Horse sword medium armor, 3: Bible no armor, 4:magician no armor
		team[0].collectWeapon(new Sword(plateau,team[0]));
		team[0].collectArmor(new HeavyArmor(team[0].getX(),team[0].getY(),plateau,team[0]));

		team[1].collectWeapon(new Bow(plateau,team[1]));
		team[1].collectArmor(new LightArmor(team[1].getX(),team[1].getY(),plateau,team[1]));

		team[2].collectWeapon(new Sword(plateau,team[2]));
		team[2].collectArmor(new MediumArmor(team[2].getX(),team[2].getY(),plateau,team[1]));
		team[2].collectHorse(new Horse(plateau, team[2]));

		team[3].collectWeapon(new Bible(plateau,team[3]));
		team[4].collectWeapon(new Balista(plateau,team[4]));

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
		
		
		// Instantiate enemy generator :
		new EnemyGenerator(plateau,plateau.g,120f,plateau.maxY/2f-10f);

	}
}
