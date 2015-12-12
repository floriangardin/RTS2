package mapeditor;

import java.util.Vector;

public class EditorAction {

	public String type;
	public EditorObject objet;
	public float x1;
	public float y1;
	public float x2;
	public float y2;
	public Vector<EditorObject> depot;
	public boolean performed;
	public EditorPlateau plateau;

	public EditorAction() {
	}

	public static EditorAction getDeplacement(EditorPlateau plateau, EditorObject objet, float ancienX, float ancienY, float nouveauX, float nouveauY, Vector<EditorObject> depot){
		EditorAction e = new EditorAction();
		e.plateau = plateau;
		e.type = "Deplacement";
		e.objet = objet;
		e.x1 = ancienX;
		e.y1 = ancienY;
		e.x2 = nouveauX;
		e.y2 = nouveauY;
		e.depot = depot;
		return e;
	}

	public static EditorAction getSuppression(EditorPlateau plateau, EditorObject objet, Vector<EditorObject> depot){
		EditorAction e = new EditorAction();
		e.plateau = plateau;
		e.type = "Suppression";
		e.objet = objet;
		e.depot = depot;
		return e;
	}

	public static EditorAction getCreation(EditorPlateau plateau, EditorObject objet, float nouveauX, float nouveauY, Vector<EditorObject> depot){
		EditorAction e = new EditorAction();
		e.plateau = plateau;
		e.type = "Creation";
		e.objet = objet;
		e.x1 = nouveauX;
		e.y1 = nouveauY;
		e.depot = depot;
		return e;
	}

	public void moveBackward(){
		if(this.performed){
			switch(type){
			case "Deplacement":
				this.plateau.setCollision(this.objet, false);
				this.objet.x = this.x1;
				this.objet.y = this.y1;
				this.plateau.setCollision(this.objet, true);
				break;
			case "Suppression":
				this.plateau.setCollision(this.objet, true);
				if(this.depot!=null)
					this.depot.addElement(this.objet);
				break;
			case "Creation":
				this.plateau.setCollision(this.objet, false);
				if(this.depot!=null)
					this.depot.remove(this.objet);
				break;
			}
			this.performed = false;
		}
	}
	public void moveForward(){
		if(!this.performed){
			switch(type){
			case "Deplacement":
				this.plateau.setCollision(this.objet, false);
				this.objet.x = this.x2;
				this.objet.y = this.y2;
				this.plateau.setCollision(this.objet, true);
				break;
			case "Creation":
				this.plateau.setCollision(this.objet, true);
				if(this.depot!=null)
					this.depot.addElement(this.objet);
				break;
			case "Suppression":
				this.plateau.setCollision(this.objet, false);
				if(this.depot!=null)
					this.depot.remove(this.objet);
				break;
			}
			this.performed = true;
		}
	}
}
