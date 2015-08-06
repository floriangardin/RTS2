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
			new Water(395f+32*i,570f,plateau);
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


		for(int i=0; i<(int)(plateau.maxX/32f+32f); i++){
			new Water(32*i,16f,plateau);
			new Water(32*i,plateau.maxY-16f,plateau);
		}
		for(int i=0; i<(int)(plateau.maxY/32f+32f); i++){
			new Water(16f,32*i,plateau);
			new Water(plateau.maxX-16f,32*i,plateau);
		}
		for(int i = 0;i<3; i++){
			new Tree(368f,490f+32*i,plateau,4);
			new Tree(682f,490f+32*i,plateau,4);
		}
		new Tree(200f,400f,plateau,1);
		// Instantiate enemy generator :
		new EnemyGenerator(plateau,plateau.g,520f,150f);

	}
}
