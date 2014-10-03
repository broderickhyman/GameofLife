package game;

import java.util.Random;

import game.GameController.ScreenType;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

@SuppressWarnings("unused")
public class Game extends BasicGameState {
	public static GameGrid gameGrid = null;
	public static Random rand;
	
	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
		rand = new Random();
		gameGrid = new GameGrid();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) {
//		g.setColor(Color.blue);
//		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		gameGrid.render(g);
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		Input input = gc.getInput();
		if(input.isKeyPressed(Input.KEY_ESCAPE)){
			gc.exit();
		}
		gameGrid.update(gc, delta);
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		gameGrid.mouseReleased(button, x, y);
	}
	
	@Override 
	public void mousePressed(int button, int x, int y) {
		
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy){
//		System.out.println(newx+" - "+newy);
		gameGrid.mouseMoved(oldx, oldy, newx, newy);
	}

	@Override
	public int getID() {
		return ScreenType.GAME.getValue();
	}

}
