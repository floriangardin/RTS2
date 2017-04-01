package events;

import data.Attributs;
import model.Game;
import model.Objet;

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
	Meditation;
	
	
	public Event createEvent(Objet parent){
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
			return new EventDeath(parent);
		case Immolation:
			sound = "fire";
			break;
		case Meditation:
			sound = "meditation";
			break;
		case MoveAttack:
			return new EventDefault(parent, Game.g.sounds.getRandomSoundUnit(parent.name.name(), "attack"));
		case MoveTarget:
			return new EventDefault(parent, Game.g.sounds.getRandomSoundUnit(parent.name.name(), "target"));
		case BuildingSelected:
			sound = "selection"+parent.name;
			break;
		case CharacterSelected:
			return new EventDefault(parent, Game.g.sounds.getRandomSoundUnit(parent.name.name(), "selection"));
		case Attack:
			sound = parent.getAttributString(Attributs.weapon);
		case Dash:
			return new EventDash(parent);
		case Blood:
			return new EventBlood(parent);
		default:
			sound = "arrow";
		}
		return new EventDefault(parent, sound);
	}
}
