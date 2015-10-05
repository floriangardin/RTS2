package IA;

import java.util.Vector;

import units.Character;

public class ArmyComparator {


	public ArmyEstimator ally;
	public ArmyEstimator ennemy;
	
	//NUMBER OF PARAMETERS
	static int numberParameters = 41;

	//COMMON PARAMETERS
	public float distance;
	public float concave1;
	public float concave2;
	public float contrePotential1;
	public float contrePotential2;
	
	//FINAL OBSERVATION
	public float[] obs = new float[numberParameters];

	public ArmyComparator(ArmyEstimator a1, ArmyEstimator a2){
		ally = a1;
		ennemy = a2;
		if(a1!=null && a2!=null)
			this.computeCommonParameters();
	}

	public void computeCommonParameters(){
		distance = (float) Math.sqrt((ally.meanX-ennemy.meanX)*(ally.meanX-ennemy.meanX)+(ally.meanY-ennemy.meanY)*(ally.meanY-ennemy.meanY));
		float maxAngle1=0f, minAngle1=0f, maxAngle2=0f, minAngle2=0f;
		float ax, ay, bx, by, cx, cy, normBC, normAC;
		float angle;
		if(this.ennemy.units.size()<=1){
			concave2 = 0;
		} else {
			cx = ally.meanX;
			cy = ally.meanY;
			bx = ennemy.meanX;
			by = ennemy.meanY;
			normBC = (float) Math.sqrt((cx-bx)*(cx-bx)+(cy-by)*(cy-by));
			for(Character c : this.ennemy.units){
				ax = c.getX();
				ay = c.getY();
				normAC = (float) Math.sqrt((cx-ax)*(cx-ax)+(cy-ay)*(cy-ay));
				angle = (float) (Math.signum((cy-by)*(cx-ax)-(cx-bx)*(cy-ay))*((float) Math.acos(((cx-bx)*(cx-ax)+(cy-by)*(cy-ay))/(normBC*normAC)))/(2f*Math.PI));
				maxAngle1 = Math.max(maxAngle1, angle);
				minAngle1 = Math.min(minAngle1, angle);
			}
			concave2 = Math.abs(maxAngle1 - minAngle1);
		}
		if(this.ally.units.size()<=1){
			concave1 = 0;
		} else {
			bx = ally.meanX;
			by = ally.meanY;
			cx = ennemy.meanX;
			cy = ennemy.meanY;
			normBC = (float) Math.sqrt((cx-bx)*(cx-bx)+(cy-by)*(cy-by));
			for(Character c : this.ally.units){
				ax = c.getX();
				ay = c.getY();
				normAC = (float) Math.sqrt((cx-ax)*(cx-ax)+(cy-ay)*(cy-ay));
				angle = (float) (Math.signum((cy-by)*(cx-ax)-(cx-bx)*(cy-ay))*((float) Math.acos(((cx-bx)*(cx-ax)+(cy-by)*(cy-ay))/(normBC*normAC)))/(2f*Math.PI));
				maxAngle1 = Math.max(maxAngle1, angle);
				minAngle1 = Math.min(minAngle1, angle);
			}
			concave1 = Math.abs(maxAngle1 - minAngle1);
		}
		for(Character c1 : this.ally.units){
			for(Character c2: this.ennemy.units){
				contrePotential1 += (c1.encounters(c2) ? 1 : 0);
				contrePotential2 += (c2.encounters(c1) ? 1 : 0);
			}
		}
		contrePotential1/=this.ally.units.size();
		contrePotential2/=this.ennemy.units.size();

	}

	public ArmyComparator(Vector<Character> ally, Vector<Character> ennemy){
		this.ally = new ArmyEstimator(ally);
		this.ennemy = new ArmyEstimator(ennemy);
		this.computeCommonParameters();
	}



	public  float[] attribute2Matrix(){
		//List of attributes in exact order
		/*
		 * concerning army 1
		 * 
		 * 0  numberUnits;
		 * 1  attackSum;
		 * 2  dpsSum;
		 * 3  lifepointsSum;
		 * 4  armorSum;
		 * 5  minMobility;
		 * 6  maxMobility;
		 * 7  meanMobility;
		 * 8  cacProportion;
		 * 9  rangeProportion;
		 * 10 spellCasterProportion;
		 * 11 mountedProportion;
		 * 12 maxVar;
		 * 13 minVar;
		 * 14 distanceToEnnemy;
		 * 15 alignmentDegree;
		 * 16 meanX;
		 * 17 meanY;
		 * 
		 * concerning army 2
		 * 
		 * 18 public float numberUnits;
		 * 19 attackSum;
		 * 20 dpsSum;
		 * 21 lifepointsSum;
		 * 22 armorSum;
		 * 23 minMobility;
		 * 24 maxMobility;
		 * 25 meanMobility;
		 * 26 cacProportion;
		 * 27 rangeProportion;
		 * 28 spellCasterProportion;
		 * 29 mountedProportion;
		 * 30 maxVar;
		 * 31 minVar;
		 * 32 distanceToEnnemy;
		 * 33 alignmentDegree;
		 * 34 meanX;
		 * 35 meanY;
		 * 
		 * concerning both armies
		 * 
		 * 36 distance;
		 * 37 concave1;
		 * 38 concave2;
		 * 39 contrePotential1;
		 * 40 contrePotential2;
		 */
		try{
		obs[0] =  ally.numberUnits;
		obs[1] =  ally.attackSum;
		obs[2] =  ally.dpsSum;
		obs[3] =  ally.lifepointsSum;
		obs[4] =  ally.armorSum;
		obs[5] =  ally.minMobility;
		obs[6] =  ally.maxMobility;
		obs[7] =  ally.meanMobility;
		obs[8] =  ally.cacProportion;
		obs[9] =  ally.rangeProportion;
		obs[10] = ally.spellCasterProportion;
		obs[11] = ally.mountedProportion;
		obs[12] = ally.maxVar;
		obs[13] = ally.minVar;
		obs[14] = ally.distanceToEnnemy;
		obs[15] = ally.alignmentDegree;
		obs[16] = ally.meanX;
		obs[17] = ally.meanY;
		obs[18] = ennemy.numberUnits;
		obs[19] = ennemy.attackSum;
		obs[20] = ennemy.dpsSum;
		obs[21] = ennemy.lifepointsSum;
		obs[22] = ennemy.armorSum;
		obs[23] = ennemy.minMobility;
		obs[24] = ennemy.maxMobility;
		obs[25] = ennemy.meanMobility;
		obs[26] = ennemy.cacProportion;
		obs[27] = ennemy.rangeProportion;
		obs[28] = ennemy.spellCasterProportion;
		obs[29] = ennemy.mountedProportion;
		obs[30] = ennemy.maxVar;
		obs[31] = ennemy.minVar;
		obs[32] = ennemy.distanceToEnnemy;
		obs[33] = ennemy.alignmentDegree;
		obs[34] = ennemy.meanX;
		obs[35] = ennemy.meanY;
		obs[36] = distance;
		obs[37] = concave1;
		obs[38] = concave2;
		obs[39] = contrePotential1;
		obs[40] = contrePotential2;
		}catch(ArrayIndexOutOfBoundsException e){
			
		}
		return obs;
	}
}
