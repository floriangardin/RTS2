package IA;

import java.util.Vector;

import units.Character;
public class ArmyEstimator {


	public float numberUnits;
	public float attackSum;
	public float dpsSum;
	public float lifepointsSum;
	public float armorSum;
	public float minMobility;
	public float maxMobility;
	public float meanMobility;
	public float cacProportion;
	public float rangeProportion;
	public float spellCasterProportion;
	public float mountedProportion;
	public float maxVar;
	public float minVar;
	public float alignmentDegree;
	public float meanX;
	public float meanY;

	public float[][] covMatrix = new float[2][2];
	Vector<Character> units ;
	public ArmyEstimator(Vector<Character> units ){
		this.units = units;
		this.updateEstimator();
	}

	public void updateEstimator(){




		// CALCULTATE
		int n = units.size();

		numberUnits = n;
		for(Character u : units){
			meanX += u.x;
			meanY += u.y;
			attackSum+= u.damage ;
			dpsSum+= u.damage/u.chargeTime ;
			lifepointsSum+= u.lifePoints ;
			armorSum+= u.armor ;
			minMobility= (minMobility==0 ? u.maxVelocity:Math.min(minMobility, u.maxVelocity));
			maxMobility=  (maxMobility==0 ? u.maxVelocity:Math.max(maxMobility, u.maxVelocity)) ;
			meanMobility+= u.maxVelocity;;
			cacProportion+= (u.weapon == "sword" || u.weapon == "spear" ? 1 : 0);
			rangeProportion+=(u.weapon == "bow" || u.weapon == "wand" ? 1 : 0);
			spellCasterProportion+= (u.spells.size()>1 ? 1 : 0);
			mountedProportion+= (u.horse!=null ? 1 : 0); ;
		}
		// CALCULATE MEANS
		meanX /= 1f*n;
		meanY /= 1f*n;
		meanMobility/= 1f*n;
		cacProportion/= 1f*n;
		rangeProportion/= 1f*n;
		spellCasterProportion/= 1f*n;
		mountedProportion/= 1f*n;

		// CALCULATE 
		for (Character u : units){
			covMatrix[0][0] +=(u.x-meanX)*(u.x-meanX);
			covMatrix[1][1] +=(u.y-meanY)*(u.y-meanY);
			covMatrix[0][1] += (u.x-meanX)*(u.y-meanY);

		}

		covMatrix[0][0]/=n;
		covMatrix[0][1]/=n;
		covMatrix[1][1]/=n;

		covMatrix[1][0] = covMatrix[0][1];


		float delta = (covMatrix[0][0]+covMatrix[1][1])*(covMatrix[0][0]+covMatrix[1][1]) - 4*(covMatrix[1][1]*covMatrix[0][0]-covMatrix[1][0]*covMatrix[1][0]);
		// DIcovMatrix[1][0]ovMatrix[0][0]GONcovMatrix[1][0]ovMatrix[0][0]LISE 

		maxVar =  (covMatrix[0][0]+covMatrix[1][1] + (float) Math.sqrt((float) delta))/2;
		minVar  =  (covMatrix[0][0]+covMatrix[1][1] - (float) Math.sqrt((float) delta))/2;

		alignmentDegree = (n>1 ? (n>2? maxVar/minVar : 100000) :1);

	}

	public String toString(){
		String s ="";
		s += "numberUnits "+numberUnits+"\n";
		s += "attackSum "+attackSum+"\n";
		s += "dpsSum "+dpsSum+"\n";
		s += "lifepointsSum "+lifepointsSum+"\n";
		s += "armorSum "+armorSum+"\n";
		s += "minMobility "+minMobility+"\n";
		s += "maxMobility "+maxMobility+"\n";
		s += "meanMobility "+meanMobility+"\n";
		s += "cacProportion "+cacProportion+"\n";
		s += "rangeProportion "+rangeProportion+"\n";
		s += "spellCasterProportion "+spellCasterProportion+"\n";
		s += "mountedProportion "+mountedProportion+"\n";
		s += "maxVar "+maxVar+"\n";
		s += "minVar "+minVar+"\n";
		s += "alignmentDegree "+alignmentDegree+"\n";
		s += "meanX "+meanX+"\n";
		s += "meanY "+meanY+"\n";
		return s;
	}
}
