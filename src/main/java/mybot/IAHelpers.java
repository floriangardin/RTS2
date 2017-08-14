package mybot;

import java.util.Vector;

import bot.IA;
import utils.ObjetsList;

public class IAHelpers {

	public static float[] getOutputVector(float[][] boMatrix, float[] state, float[] bias){
		float[] output = new float[boMatrix.length];
		for(int i=0; i<boMatrix.length; i++){
			int temp = 0;
			for(int j=0; j<state.length; j++){
				temp+= boMatrix[i][j]*state[j];
			}
			output[i] = temp+bias[i];
		}
		return output;
	}
	
	public static float[] getState(int sizeState, IA ia){
		float[] res = new float[sizeState];
//		// TODO : 
//		res[IAState.FOOD.value] = ia.getFood();
//		res[IAState.POP.value] = ia.getPop();
//		res[IAState.MAXPOP.value] = ia.getMaxPop();
//		res[IAState.BARRACKS.value] = ia.getMyUnits(ObjetsList.Barracks).size();
//		res[IAState.STABLES.value] = ia.getMyUnits(ObjetsList.Stable).size();
//		res[IAState.TOWER.value] = ia.getMyUnits(ObjetsList.Tower).size();
//		res[IAState.MILL.value] = ia.getMyUnits(ObjetsList.Mill).size();
//		res[IAState.MINE.value] = ia.getMyUnits(ObjetsList.Mine).size();
//		res[IAState.UNIVERSITY.value] = ia.getMyUnits(ObjetsList.University).size();
//		res[IAState.SPEARMAN.value] = ia.getMyUnits(ObjetsList.Spearman).size();
//		res[IAState.CROSSBOWMAN.value] = ia.getMyUnits(ObjetsList.Crossbowman).size();
//		res[IAState.INQUISITOR.value] = ia.getMyUnits(ObjetsList.Inquisitor).size();
//		res[IAState.PRIEST.value] = ia.getMyUnits(ObjetsList.Priest).size();
//		res[IAState.KNIGHT.value] = ia.getMyUnits(ObjetsList.Knight).size();
//		
//		// COMPLICATED STATE
//		res[IAState.FREEBARRACK.value] = ia.getMyFreeUnits(ObjetsList.Barracks).size();
//		res[IAState.FREESTABLE.value] = ia.getMyFreeUnits(ObjetsList.Stable).size();
//		res[IAState.FREESPEARMAN.value] = ia.getMyFreeUnits(ObjetsList.Barracks).size();
//		res[IAState.FREESPEARMAN.value] = ia.getMyFreeUnits(ObjetsList.Spearman).size();
//		res[IAState.FREECROSSBOWMAN.value] = ia.getMyFreeUnits(ObjetsList.Crossbowman).size();
//		res[IAState.FREEINQUISITOR.value] = ia.getMyFreeUnits(ObjetsList.Inquisitor).size();
//		res[IAState.FREEPRIEST.value] = ia.getMyFreeUnits(ObjetsList.Priest).size();
//		res[IAState.FREEKNIGHT.value] = ia.getMyFreeUnits(ObjetsList.Knight).size();
//		
//		// ATTACKED
//		int toleranceAttack =50;
//		res[IAState.ATTACKEDBARRACK.value] = ia.getMyAttackedUnits(ObjetsList.Barracks, toleranceAttack).size();
//		res[IAState.ATTACKEDSTABLES.value] = ia.getMyAttackedUnits(ObjetsList.Stable, toleranceAttack).size();
//		res[IAState.ATTACKEDSPEARMAN.value] = ia.getMyAttackedUnits(ObjetsList.Barracks, toleranceAttack).size();
//		res[IAState.ATTACKEDSPEARMAN.value] = ia.getMyAttackedUnits(ObjetsList.Spearman, toleranceAttack).size();
//		res[IAState.ATTACKEDCROSSBOWMAN.value] = ia.getMyAttackedUnits(ObjetsList.Crossbowman, toleranceAttack).size();
//		res[IAState.ATTACKEDINQUISITOR.value] = ia.getMyAttackedUnits(ObjetsList.Inquisitor, toleranceAttack).size();
//		res[IAState.ATTACKEDPRIEST.value] = ia.getMyAttackedUnits(ObjetsList.Priest, toleranceAttack).size();
//		res[IAState.ATTACKEDKNIGHT.value] = ia.getMyAttackedUnits(ObjetsList.Knight, toleranceAttack).size();
//		res[IAState.ATTACKEDTOWER.value] = ia.getMyAttackedUnits(ObjetsList.Tower, toleranceAttack).size();
//		res[IAState.ATTACKEDHEADQUARTERS.value] = ia.getMyAttackedUnits(ObjetsList.Headquarters, toleranceAttack).size();
//		
//		//ENNEMIES
//		// VISIBLE
//		res[IAState.VISIBLEBARRACK.value] = ia.getEnnemies(ObjetsList.Barracks).size();
//		res[IAState.VISIBLESTABLES.value] = ia.getEnnemies(ObjetsList.Stable).size();
//		res[IAState.VISIBLESPEARMAN.value] = ia.getEnnemies(ObjetsList.Barracks).size();
//		res[IAState.VISIBLESPEARMAN.value] = ia.getEnnemies(ObjetsList.Spearman).size();
//		res[IAState.VISIBLECROSSBOWMAN.value] = ia.getEnnemies(ObjetsList.Crossbowman).size();
//		res[IAState.VISIBLEINQUISITOR.value] = ia.getEnnemies(ObjetsList.Inquisitor).size();
//		res[IAState.VISIBLEPRIEST.value] = ia.getEnnemies(ObjetsList.Priest).size();
//		res[IAState.VISIBLEKNIGHT.value] = ia.getEnnemies(ObjetsList.Knight).size();
//		res[IAState.VISIBLETOWER.value] = ia.getEnnemies(ObjetsList.Tower).size();
//		res[IAState.VISIBLEHEADQUARTERS.value] = ia.getEnnemies(ObjetsList.Headquarters).size();
//
		return res;
	}
	public static int getMaxOutput(float[] output){
		float max = 0;
		int maxIdx = 0;
		for(int i = 0; i<output.length; i++){
			if(output[i]>=max){
				max= output[i];
				maxIdx = i;
			}
		}
		return maxIdx;
	}
	public static float objectiveFunction(int[] state, IA ia){ // fitness fonction
		// Estimate the actual position given plateau
		float score = 0;
		score += state[IAState.FOOD.value];
		score += Math.abs(state[IAState.MAXPOP.value]-state[IAState.POP.value]);
		//score += Math.abs(state[IAState.]);
		return score;
	}
}
