package model;

public class Map {

	public Map(){

	}

	public void createMap1(Plateau plateau){
		//Instantiate allies
		Character[] team = new Character[5];
		for(int i=0;i<5;i++){
			switch(i){
			case 0: team[i]=Character.createSpearman(plateau,1,500f+10f*i,500f);break;
			case 1: team[i]=Character.createBowman(plateau,1,500f+10f*i,500f);break;
			case 2: team[i]=Character.createKnight(plateau,1,500f+10f*i,500f);break;
			case 3: team[i]=Character.createPriest(plateau,1,500f+10f*i,500f);break;
			case 4: team[i]=Character.createWizard(plateau,1,500f+10f*i,500f);break;
			}
			plateau.g.players.get(0).groups.get(i).add(team[i]);
		}
		Character.createBowman(plateau, 2, 101f, 100f);
		Character.createBowman(plateau, 2, 102f, 100f);
		Character.createBowman(plateau, 2, 100f, 100f);
		Character.createBowman(plateau, 2, 103f, 100f);
//	
//		for(int i = 0;i<9; i++){
//			new Water(395f+32*i,570f,32f,32f,plateau);
//		}
		for(int i = 0;i<3; i++){
			new Tree(368f,490f+32*i,plateau,4);
			//new Tree(682f,490f+32*i,plateau,4);
		}
		new Tree(200f,400f,plateau,1);
		// Instantiate enemy generator :

		
		new BuildingMill(plateau,plateau.g,1200f,300f);
		new BuildingMine(plateau,plateau.g,900f,800f);


		new Academy(plateau,plateau.g,520f,700f);
		new Stable(plateau,plateau.g,520f,1000f);
		//Barrack test = new Barrack(plateau,plateau.g,720f,700f);
		//test.team = 2;
	}

	public void createMapLan(Plateau plateau){
		plateau.maxX = 2000f;
		plateau.maxY = 3000f;

		Character.createBowman(plateau, 1, plateau.maxX/2-1f, 300f);
		Character.createKnight(plateau, 1, plateau.maxX/2, 300f);
		Character.createPriest(plateau, 1, plateau.maxX/2+1f, 300f);
		Character.createWizard(plateau, 1, plateau.maxX/2+2f, 300f);
		Character.createSpearman(plateau, 1, plateau.maxX/2+3f, 300f);
		
		new BuildingMill(plateau,plateau.g,150f,100f);
		new BuildingMine(plateau,plateau.g,plateau.maxX-200f,100f);
		new Barrack(plateau,plateau.g,plateau.maxX/2,100f);
		
		new Stable(plateau,plateau.g,plateau.maxX/4,2*plateau.maxY/5);
		new Academy(plateau,plateau.g,3*plateau.maxX/4,2*plateau.maxY/5);

		Character.createBowman(plateau, 2, plateau.maxX/2-1f, plateau.maxY-300f);
		Character.createKnight(plateau, 2, plateau.maxX/2, plateau.maxY-300f);
		Character.createPriest(plateau, 2, plateau.maxX/2+1f, plateau.maxY-300f);
		Character.createWizard(plateau, 2, plateau.maxX/2+2f, plateau.maxY-300f);
		Character.createSpearman(plateau, 2, plateau.maxX/2+3f, plateau.maxY-300f);
		
		new BuildingMill(plateau,plateau.g,150f,plateau.maxY-200f);
		new BuildingMine(plateau,plateau.g,plateau.maxX-200f,plateau.maxY-200f);
		new Barrack(plateau,plateau.g,plateau.maxX/2,plateau.maxY-200f);
		
		// Stables and academy 
		new Stable(plateau,plateau.g,plateau.maxX/4, 3*plateau.maxY/5);
		new Academy(plateau,plateau.g,3f*plateau.maxX/4, 3*plateau.maxY/5);
		
		// Barrack in the middle
		new BuildingMill(plateau,plateau.g,150f,plateau.maxY/2);
		new BuildingMine(plateau,plateau.g,plateau.maxX-200f,plateau.maxY/2);
		new Barrack(plateau,plateau.g,plateau.maxX/2,plateau.maxY/2);

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

	public void createMapVersus(Plateau plateau){
		//Instantiate allies
		Character[][] team = new Character[2][5];
		for(int k = 0;k<2;k++){
			for(int i=0;i<5;i++){		
				team[k][i]=new Character(plateau,k,500f+10f*i,500f);
				plateau.g.players.get(k).groups.get(i).add(team[k][i]);
			}
			team[k][0].setXY((k+1)*plateau.maxX/3f+20f-200f , plateau.maxY/2f);
			team[k][1].setXY((k+1)*plateau.maxX/3f-20f-200f, plateau.maxY/2f);
			team[k][2].setXY((k+1)*plateau.maxX/3f-200f, plateau.maxY/2f+20f);
			team[k][3].setXY((k+1)*plateau.maxX/3f-200f, plateau.maxY/2f-20f);
			team[k][4].setXY((k+1)*plateau.maxX/3f-200f, plateau.maxY/2f);


			// Give equipement to team 
			// 0 : sword heavy armor, 1: Bow light armor , 2: Horse sword medium armor, 3: Bible no armor, 4:magician no armor
			team[k][0].collectWeapon(new Sword(plateau,team[k][0]));
			
			team[k][1].collectWeapon(new Bow(plateau,team[k][1]));
			
			team[k][2].collectWeapon(new Sword(plateau,team[k][2]));
			team[k][2].collectHorse(new Horse(plateau, team[k][2]));

			team[k][3].collectWeapon(new Bible(plateau,team[k][3]));
			team[k][4].collectWeapon(new Wand(plateau,team[k][4]));

		}


	}
}
