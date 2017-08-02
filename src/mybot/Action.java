package mybot;

import java.util.List;

import bot.IAUnit;

public class Action {
	List<IAUnit> sujet;
	Action parent;
	public Verb verb;
	public enum Verb{
		attack,
		produce,
		build,
		protect;
	}
	List<IAUnit> complement;
	public Action(List<IAUnit> units, Action parent){
		this.sujet = units;
	}
	public Action(List<IAUnit> units){
		this.sujet = units;
	}
	public Action attack(){
		return null;
	}
	public Action protect(){
		return null;
	}
	public Action produce(){
		return null;
	}
	public Action build(){
		return null;
	}
}
