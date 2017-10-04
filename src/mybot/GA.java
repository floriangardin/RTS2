package mybot;

public strictfp class GA {
	
	public final static int RATIO = 10;
	
	
	
	public static float[][] generate(int nbLines, int nbColumns){
		float[][] res = new float[nbLines][nbColumns];
		for(int i = 0; i<nbLines; i++){
			for(int j  =0; j<nbColumns; j++){
				res[i][j] = (float) StrictMath.random()*RATIO;
			}
		}
		return res;
	}
	
	public static float[] generateBias(int nbLines){
		float[] res = new float[nbLines];
		for(int i = 0; i<nbLines; i++){
			res[i] = (float) StrictMath.random()*RATIO;	
		}
		return res;
	}
	public static float[][] merge(float[][] m1 , float[][] m2){
		float[][] res = new float[m1.length][m1[0].length];
		for(int i = 0; i<m1.length; i++){
			for(int j  =0; j<m1[0].length; j++){
				float coeff = (float) StrictMath.random();
				res[i][j] = coeff*m1[i][j]+(1-coeff)*m2[i][j];
			}
		}
		return res;
	}
	public static float[] mergeBias(float[] m1 , float[] m2){
		float[] res = new float[m1.length];
		for(int i = 0; i<m1.length; i++){
			float coeff = (float) StrictMath.random();
			res[i] = coeff*m1[i]+(1-coeff)*m2[i];
		
		}
		return res;
	}
	
	public static float[][] mutate(float[][] m1){
		float[][] res = new float[m1.length][m1[0].length];
		for(int i = 0; i<m1.length; i++){
			for(int j  =0; j<m1[0].length; j++){
				float proba = (float) StrictMath.random();
				if(proba<0.01){
					float coeff = (float) StrictMath.random()-1;
					res[i][j] = m1[i][j]+ coeff;
				}
			}
		}
		return res;
	}
	
	public static float[] mutateBias(float[]m1){
		float[] res = new float[m1.length];
		for(int i = 0; i<m1.length; i++){
			float proba = (float) StrictMath.random();
			if(proba<0.01){
				float coeff = (float) (StrictMath.random()-1)*RATIO;
				res[i] = m1[i]+ coeff;
			}
			
		}
		return res;
	}
}
