package bot;

public class Utils {

	public static float distance2(IAUnit x, IAUnit y){
		return (x.getX()-y.getX())*(x.getX()-y.getX())+ (x.getY()-y.getY())*(x.getY()-y.getY());
	}
}
