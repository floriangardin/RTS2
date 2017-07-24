package mybot;

public enum IAOutput {
	//ACTIONS OUTPUT
	produceSpearman(0),
	produceCrossBowman(1),
	produceInquisitor(2),
	produceKnight(3),
	producePriest(4),
	produceAge(5),
	getBarracks(6),
	getStables(7),
	getMill(8),
	getMine(9),
	attackTower(10),
	attackBarrack(11),
	attackMill(12),
	attackMine(13),
	attackStable(14),
	attackSpearman(15),
	attackCrossBowman(16),
	attackInquisitor(17),
	attackKnight(18),
	attackPriest(19),
	attackHeadQuarters(20);

	public int value;
	private IAOutput(int value){
		this.value = value;
	}

	public static IAOutput get(int i){
		return values()[i];
	}
	
	public static int size(){
		return values().length;
	}
	
	
	//MAIN FOR TEST
	public static void main(String[] args){
		System.out.println(IAOutput.get(9));
		System.out.println(IAOutput.attackHeadQuarters.value);
	}
}
