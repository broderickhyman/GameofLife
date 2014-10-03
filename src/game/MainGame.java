package game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class MainGame {
	private static AppGameContainer app;
	public static int width = 1000;
	public static int height = 800;
	
	public static void main(String[] args) throws SlickException {
		startGame();
	}
	
	public static void startGame() throws SlickException{
		app = new AppGameContainer(new GameController("Base Builder"));
		app.setTargetFrameRate(60);
		app.setMinimumLogicUpdateInterval(20);
		app.setDisplayMode(width, height, false);
		app.setShowFPS(false);
		app.start();
	}
}
