package main;



public class mainTestCalculDegueu {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int nbUnit = 1; nbUnit<70;nbUnit++){
			int N=1;
			while((int)(Math.PI*N*(N-1)/2+1)<nbUnit)
				N++;
			System.out.println(nbUnit+" "+N+" "+4*Math.log(N));
		}


	}

}
