package IA;

public class GeneticAlgorithm {
	//N_POP should be 0%4
	public int N_POP;
	public int N_PARAMS;
	public int N_FEATURES;
	public float proba_muta ;
	public float[][][] X ;

	public float[] objective;


	public void GeneticAlgorithm(int N_POP, int N_PARAMS, int N_FEATURES,float proba_muta){
		this.N_POP = N_POP;
		this.N_PARAMS = N_PARAMS;
		this.N_FEATURES = N_FEATURES;
		this.X = new float[N_POP][N_PARAMS][N_FEATURES];
		this.objective = new float[N_POP];
		this.proba_muta = proba_muta;

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
		for(int i=0;i<n_generations;i++){
			runOneGeneration();
		}
		return getResult();
	}
	public void runOneGeneration(){
		this.evaluateObjective();
		this.select();
		this.merge();
		this.mutate();
	}
	public void evaluateObjective(){
		//CALL SIMULATOR HERE
		// RUN SIMU FOR THE NEW N/2 MATRICES
		for(int i=N_POP/2;i<N_POP/2;i++){
			//TODO CALL SIMULATION AND RETURN OBJECTIVE
			this.objective[i]=0f;
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
			this.objective[k] = this.objective[idxBest];
			this.objective[idxBest]=-1;
			Xnew[k] = copy(this.X[idxBest]);
		}
		X = Xnew;

	}

	public void merge(){
		//COMPLETE LAST N/2 
		int start = (int) N_POP/2;
		for(int i=0;i<(int) N_POP/2 ; i+=2){
			//TAKE TWO
			int j = i+1;
			float lambda = (float) Math.random();
			X[start+i]= sum(multiply(lambda,X[i]),multiply(1-lambda,X[j]));
		}
	}

	public void mutate(){
		for(int i=(int) N_POP/2;i<N_POP/2;i++){
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
}
