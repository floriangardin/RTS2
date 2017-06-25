package events;

import data.Attributs;
import display.Camera;
import model.Game;
import plateau.Objet;
import plateau.Plateau;
import ressources.Sounds;

public enum EventNames {

	ArrowLaunched,
	FireBallLaunched, 
	Dash, 
	BonusTaken, 
	Death, 
	Immolation, 
	MoveAttack, 
	MoveTarget, 
	CharacterSelected, 
	BuildingSelected, 
	Attack,
	Blood,
	Meditation, 
	AttackDamageNormal, 
	DestructionTower;
	
	
	public Event createEvent(Objet parent, Plateau plateau, Camera camera){
		String sound;
		switch(this){
		case ArrowLaunched:
			sound = "arrow";
			break;
		case FireBallLaunched:
			sound = "fireball";
			break;
		case BonusTaken:
			sound = "bonusTaken";
			break;
		case Death:
			return new EventDeath(parent, plateau, camera);
		case Immolation:
			sound = "fire";
			break;
		case Meditation:
			sound = "meditation";
			break;
		case MoveAttack:
			return new EventDefault(parent, Sounds.getRandomSoundUnit(parent.name.name(), "attack"), plateau, camera);
		case MoveTarget:
			return new EventDefault(parent, Sounds.getRandomSoundUnit(parent.name.name(), "target"), plateau, camera);
		case BuildingSelected:
			sound = "selection"+parent.name;
			break;
		case CharacterSelected:
			return new EventDefault(parent, Sounds.getRandomSoundUnit(parent.name.name(), "selection"), plateau, camera);
		case Attack:
			sound = parent.getAttributString(Attributs.weapon);
		case Dash:
			return new EventDash(parent, plateau, camera);
		case Blood:
			return new EventBlood(parent, plateau, camera);
		case DestructionTower:
			return new EventDestructionTour(parent, plateau, camera);
		default:
			sound = "arrow";
		}
		return new EventDefault(parent, sound, plateau, camera);
	}
}
