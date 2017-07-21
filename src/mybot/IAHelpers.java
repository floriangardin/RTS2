package mybot;

import bot.IA;
import utils.ObjetsList;

public class IAHelpers {

	
	public static float[] getOutputVector(float[][] boMatrix, int[] state){
		float[] output = new float[boMatrix.length];
		for(int i=0; i<boMatrix.length; i++){
			int temp = 0;
			for(int j=0; j<state.length; j++){
				temp+= boMatrix[i][j]*state[j];
			}
			output[i] = temp;
		}
		return output;
	}
	
	public static int[] getState(int sizeState, IA ia){
		int[] res = new int[sizeState];
		// TODO : 
		res[IAState.FOOD.value] = ia.getFood();
		res[IAState.POP.value] = ia.getPop();
		res[IAState.MAXPOP.value] = ia.getMaxPop();
		res[IAState.BARRACKS.value] = ia.getMyUnits(ObjetsList.Barracks).size();
		res[IAState.STABLES.value] = ia.getMyUnits(ObjetsList.Stable).size();
		res[IAState.TOWER.value] = ia.getMyUnits(ObjetsList.Tower).size();
		res[IAState.MILL.value] = ia.getMyUnits(ObjetsList.Mill).size();
		res[IAState.MINE.value] = ia.getMyUnits(ObjetsList.Mine).size();
		res[IAState.UNIVERSITY.value] = ia.getMyUnits(ObjetsList.University).size();
		res[IAState.SPEARMAN.value] = ia.getMyUnits(ObjetsList.Spearman).size();
		res[IAState.CROSSBOWMAN.value] = ia.getMyUnits(ObjetsList.Crossbowman).size();
		res[IAState.INQUISITOR.value] = ia.getMyUnits(ObjetsList.Inquisitor).size();
		res[IAState.PRIEST.value] = ia.getMyUnits(ObjetsList.Priest).size();
		res[IAState.KNIGHT.value] = ia.getMyUnits(ObjetsList.Knight).size();
		
		// COMPLICATED STATE
		res[IAState.FREEBARRACK.value] = ia.getMyFreeUnits(ObjetsList.Barracks).size();
		res[IAState.FREESTABLE.value] = ia.getMyFreeUnits(ObjetsList.Stable).size();
		res[IAState.FREESPEARMAN.value] = ia.getMyFreeUnits(ObjetsList.Barracks).size();
		res[IAState.FREESPEARMAN.value] = ia.getMyFreeUnits(ObjetsList.Spearman).size();
		res[IAState.FREECROSSBOWMAN.value] = ia.getMyFreeUnits(ObjetsList.Crossbowman).size();
		res[IAState.FREEINQUISITOR.value] = ia.getMyFreeUnits(ObjetsList.Inquisitor).size();
		res[IAState.FREEPRIEST.value] = ia.getMyFreeUnits(ObjetsList.Priest).size();
		res[IAState.FREEKNIGHT.value] = ia.getMyFreeUnits(ObjetsList.Knight).size();
		
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
	public static int objectiveFunction(int[] state){
		// Estimate the actual position
		return 0;
	}
}
