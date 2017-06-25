package plateau;



public abstract class NaturalObjet extends Objet {

	public NaturalObjet(Plateau plateau) {
		super(plateau);
		plateau.addNaturalObjets(this);
		// TODO Auto-generated constructor stub
	}
	public float sizeX;
	public float sizeY;
	


}
