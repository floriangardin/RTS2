package main;

import org.newdawn.slick.util.LogSystem;

public strictfp class NullLogSystem implements LogSystem {
	
	public void debug(String arg0) {}
	public void error(Throwable arg0) {}
	public void error(String arg0) {}
	public void error(String arg0, Throwable arg1) {}
	public void info(String arg0) {}
	public void warn(String arg0) {}
	public void warn(String arg0, Throwable arg1) {}
}
