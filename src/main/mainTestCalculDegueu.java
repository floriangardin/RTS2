package main;



public class mainTestCalculDegueu {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		float ax, ay, bx, by, cx, cy, normBC, normAC;
		float angle;
		cx = 1;
		cy = 0;
		bx = 0;
		by = 0;
		normBC = (float) Math.sqrt((cx-bx)*(cx-bx)+(cy-by)*(cy-by));
		ax = 0;
		ay = -1;
		normAC = (float) Math.sqrt((cx-ax)*(cx-ax)+(cy-ay)*(cy-ay));
		angle = (float) Math.acos(((cx-bx)*(cx-ax)+(cy-by)*(cy-ay))/(normBC*normAC));
		angle *= Math.signum((cy-by)*(cx-ax)-(cx-bx)*(cy-ay))*180/Math.PI;
		System.out.println(angle);

	}

}
