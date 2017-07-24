package mybot;

import java.util.Vector;

import bot.IA;
import bot.IAAllyObject;
import plateau.Team;
import utils.ObjetsList;

public class IAInputs extends IA {

	final static int sizeState = IAState.size();
	final static int sizeOutput = IAOutput.size();
	int[] state = new int[sizeState];
	float[][] boMatrix = new float[sizeOutput][sizeState];
	float[] bias = new float[sizeOutput];
	float[] output = new float[sizeOutput];
	IAOutput mission;
	boolean newMission = false;

	public IAInputs(int teamid) {
		super(teamid);
	}
	
	public IAInputs(int teamid, float[][] boMatrix){
		super(teamid);
		this.boMatrix = boMatrix;
	}

	@Override
	public Vector<IAAllyObject> select() throws Exception {
		state = IAHelpers.getState(sizeState, this);
		output = IAHelpers.getOutputVector(boMatrix, state);
		// Check that macro mission is realisable
		// If yes select units to perform mission
		setMission();
		return selectForMission();
	}
	@Override
	public void update(Vector<IAAllyObject> selection) throws Exception {
		clickForMission(selection);
	}

	public void setMission(){
		// GET MAX MISSION THAT IS NOT CURRENT MISSION
		int idxMax = IAHelpers.getMaxOutput(output, mission);
		IAOutput newMission = IAOutput.get(idxMax);
		mission = newMission;
	}

	public Vector<IAAllyObject> selectForMission(){
		Vector<IAAllyObject> selection = new Vector<IAAllyObject>();
		// TODO
		switch(mission){
		case attackBarrack:
			
			break;
		case attackCrossBowman:
			break;
		case attackHeadQuarters:
			break;
		case attackInquisitor:
			break;
		case attackKnight:
			break;
		case attackMill:
			break;
		case attackMine:
			break;
		case attackPriest:
			break;
		case attackSpearman:
			break;
		case attackStable:
			break;
		case attackTower:
			break;
		case getBarracks:
			break;
		case getMill:
			break;
		case getMine:
			break;
		case getStables:
			break;
		case produceAge:
			break;
		case produceCrossBowman:
			break;
		case produceInquisitor:
			break;
		case produceKnight:
			break;
		case producePriest:
			break;
		case produceSpearman:
			break;

		default:
			break;
		
		}
		return selection;
	}

	public void clickForMission(Vector<IAAllyObject> selection){
		switch(mission){
		case attackBarrack:
			break;
		case attackCrossBowman:
			break;
		case attackHeadQuarters:
			break;
		case attackInquisitor:
			break;
		case attackKnight:
			break;
		case attackMill:
			break;
		case attackMine:
			break;
		case attackPriest:
			break;
		case attackSpearman:
			break;
		case attackStable:
			break;
		case attackTower:
			break;
		case getBarracks:
			break;
		case getMill:
			break;
		case getMine:
			break;
		case getStables:
			break;
		case produceAge:
			break;
		case produceCrossBowman:
			break;
		case produceInquisitor:
			break;
		case produceKnight:
			break;
		case producePriest:
			break;
		case produceSpearman:
			break;
		default:
			break;

		}
	}





}
