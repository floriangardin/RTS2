package mybot;

public class GA {

	public static float[][] merge(float[][] m1 , float[][] m2){
		float[][] res = new float[m1.length][m1[0].length];
		for(int i = 0; i<m1.length; i++){
			for(int j  =0; j<m1[0].length; j++){
				float coeff = (float) Math.random();
				res[i][j] = coeff*m1[i][j]+(1-coeff)*m2[i][j];
			}
		}
		return res;
	}
	public static float[][] mutate(float[][] m1){
		float[][] res = new float[m1.length][m1[0].length];
		for(int i = 0; i<m1.length; i++){
			for(int j  =0; j<m1[0].length; j++){
				float proba = (float) Math.random();
				if(proba<0.01){
					float coeff = (float) Math.random()-1;
					res[i][j] = m1[i][j]+ coeff;
				}
			}
		}
		return res;
	}
}
