package IA;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import units.Character;

public class GeneticAlgorithm {
	//N_POP should be 0%4
	public int N_POP;
	public int N_PARAMS;
	public int N_FEATURES;
	public float proba_muta ;
	public float[][][] X ;
	public GameSimu game;
	public float[] objective;

	public boolean debug = false;

	public GeneticAlgorithm(int N_POP, int N_PARAMS, int N_FEATURES,float proba_muta,GameSimu game){
		this.N_POP = N_POP;
		this.N_PARAMS = N_PARAMS;
		this.N_FEATURES = N_FEATURES;
		this.X = new float[N_POP][N_PARAMS][N_FEATURES];
		this.objective = new float[N_POP];
		this.proba_muta = proba_muta;
		this.game = game;
	}

	public void init(){
		for(int i=0;i<N_POP;i++){
			for(int j = 0;j<N_PARAMS;j++){
				for(int k = 0;k<N_FEATURES;k++){
					X[i][j][k] = (float) ( Math.random()-0.5);
				}
			}
		}

	}


	public float[][] getResult(){

		float best = 0f;
		int idxBest = 0;
		for(int i = 0;i<N_POP;i++){
			if(this.objective[i]>=best){
				idxBest = i;
				best = this.objective[i];		
			}
		}
		// COPY BEST IN NEW MATRICES
		//COPY LINE OF X In Xnew;
		return copy(this.X[idxBest]);

	}

	public float[][] optimize(int n_generations){
		for(int i=0;i<=n_generations;i++){
			runOneGeneration(i);
		}
		return getResult();
	}
	public void runOneGeneration(int k ){

		if(debug){
		System.out.println("Before simulate");
		for(int i =0;i<N_POP;i++){
			System.out.println(this.objective[i]);
		}
		}
		
		this.evaluateObjective();
		
		if(debug){
		System.out.println("After simulate");
		for(int i =0;i<N_POP;i++){
			System.out.println(this.objective[i]);
		}
		}

		this.select();
		
		if(debug){
		System.out.println("After selection");
		for(int i =0;i<N_POP;i++){
			System.out.println(this.objective[i]);
		}
		}

		this.merge();
		
		if(debug){
		System.out.println("After merging");
		for(int i =0;i<N_POP;i++){
			System.out.println(this.objective[i]);
		}
		}

		this.mutate();
		
		if(debug){
		System.out.println("After mutation");
		for(int i =0;i<N_POP;i++){
			System.out.println(this.objective[i]);
		}
		}

		// EVALUATION BEST OBJECTIVE FOR THIS GENERATION
		float fobj = 0f;
		int best = 0;
		for(int i=0;i<N_POP;i++){
			if(this.objective[i]>=fobj){
				best = i;
				fobj = this.objective[i];		
			}
		}

		System.out.println("end generation "+ k);
		System.out.println("Best objective : " + fobj);
		shuffleArray();

	}
	public void evaluateObjective(){
		Simulation s;
		// RUN SIMU FOR ALL SAMPLES WITHOUT SIMULATION
		for(int i=0;i<N_POP;i++){

			if(this.objective[i]==0){
				//			System.out.println("i" + i);
				//			print(X[i]);
				s = new Simulation(game);
				s.armies.get(0).get(0).ia = new IAUnit(s.armies.get(0).get(0),copy(X[i]));
				s.simulate();
				//TODO CALL SIMULATION AND RETURN OBJECTIVE
				this.objective[i]=(s.victory%2)*(s.armies.get(0).get(0).lifePoints*2f);

				for(Character c: s.armies.get(1)){
					this.objective[i]+=3f*(c.maxLifePoints-c.lifePoints);
				}
			}
		}
	}

	public void evaluateAllObjective(){
		Simulation s;
		// RUN SIMU FOR THE NEW N/2 MATRICES
		for(int i=0;i<N_POP;i++){
			s = new Simulation(game);
			s.armies.get(0).get(0).ia = new IAUnit(s.armies.get(0).get(0),copy(X[i]));
			s.simulate();
			//TODO CALL SIMULATION AND RETURN OBJECTIVE
			this.objective[i]=(s.victory%2)*(s.armies.get(0).get(0).lifePoints*2f);
			for(Character c: s.armies.get(1)){
				this.objective[i]+=3f*(c.maxLifePoints-c.lifePoints);
			}
		}
	}

	public void select(){
		// SELECT N/2 best candidates
		//Sort
		int[] idx = new int[N_POP];
		float[][][] Xnew = new float[N_POP][N_PARAMS][N_FEATURES];
		float[] objectiveNew = new float[N_POP];

		for(int k = 0; k<(int)(N_POP/2) ; k++){
			float best = 0f;
			int idxBest = 0;
			for(int i = 0;i<N_POP;i++){
				if(this.objective[i]>=best){
					idxBest = i;
					best = this.objective[i];


				}
			}
			// COPY BEST IN NEW MATRICES
			//COPY LINE OF X In Xnew;
			objectiveNew[k] = this.objective[idxBest];
			this.objective[idxBest] = -1;
			Xnew[k] = copy(this.X[idxBest]);
		}
		X = Xnew;
		this.objective = objectiveNew;

	}

	public void merge(){
		//COMPLETE LAST N/2 
		int start = (int) N_POP/2;
		for(int i=0;i<(int) N_POP/2 ; i+=1){
			//TAKE TWO
			int j = i+1;
			float lambda = (float) Math.random();
			X[start+i]= sum(multiply(lambda,X[i]),multiply(1-lambda,X[j]));
		}


	}



	public void mutate(){
		for(int i=(int) N_POP/2;i<N_POP;i++){
			for(int j = 0;j<N_PARAMS;j++){
				for(int k = 0;k<N_FEATURES;k++){
					if(Math.random()<this.proba_muta)
						X[i][j][k] = (float) ( Math.random()-0.5);
				}
			}
		}

	}

	public static float[][] copy(float[][] X){
		float[][] result = new float[X.length][X[0].length];
		for(int i = 0;i<X.length;i++){
			for(int j = 0;j<X[0].length;j++){
				result[i][j]= X[i][j];
			}
		}
		return result;
	}

	public static float[][] multiply(float lambda,float[][] X){
		float[][] result = new float[X.length][X[0].length];
		for(int i = 0;i<X.length;i++){
			for(int j = 0;j<X[0].length;j++){
				result[i][j]= lambda*X[i][j];
			}
		}

		return result;
	}

	public static float[][] sum(float[][] X1,float[][] X2){
		float[][] result = new float[X1.length][X1[0].length];
		for(int i = 0;i<X1.length;i++){
			for(int j = 0;j<X1[0].length;j++){
				result[i][j]= X1[i][j]+X2[i][j];
			}
		}

		return result;
	}

	public static void print(float[][] X1){
		for(int i = 0;i<X1.length;i++){
			for(int j = 0;j<X1[0].length;j++){
				System.out.print(X1[i][j]+" ");
			}
			System.out.println();
		}
	}


	public void  shuffleArray()
	{
		// If running on Java 6 or older, use `new Random()` on RHS here
		Random rnd = new Random();
		for (int i = X.length - 1; i > 0; i--)
		{
			int index = rnd.nextInt(i + 1);
			// Simple swap
			float[][] a = X[index];
			float a2 = objective[index];
			X[index] = X[i];
			objective[index] = objective[i];
			X[i] = a;
			objective[i] = a2;
		}

	}
}
