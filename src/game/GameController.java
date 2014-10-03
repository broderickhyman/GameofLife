package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GameController extends StateBasedGame {
	public static enum ScreenType {
		GAME;

		public int getValue() {
			return this.ordinal();
		}
	}

	// PUBLIC VALUES

	public GameController(String title) {
		super(title);

		this.addState(new Game());
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		enterState(ScreenType.GAME.getValue());
	}
}
