package mybot;

public enum IAState {
	// SIMPLE STATE
	HEADQUARTERS(0),
	BARRACKS(1),
	MILL(2),
	MINE(3),
	SPEARMAN(4),
	CROSSBOWMAN(5),
	INQUISITOR(6),
	KNIGHT(7),
	PRIEST(8),
	GOLD(9),
	FOOD(10),
	POP(11),
	MAXPOP(12),
	TOWER(13),
	UNIVERSITY(14),
	// COMPLICATED STATE
	FREESPEARMAN(15),
	FREECROSSBOWMAN(16),
	FREEINQUISITOR(17),
	FREEKNIGHT(18),
	FREEPRIEST(19),
	FREEBARRACK(20),
	FREESTABLE(21),
	STABLES(22);

	public int value;
	private IAState(int value){
		this.value = value;
	}

	public IAState get(int i){
		return values()[i];
	}
	
	public static int size(){
		return values().length;
	}

}
