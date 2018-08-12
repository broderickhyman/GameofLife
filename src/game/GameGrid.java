package game;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;

@SuppressWarnings("unused")
public class GameGrid {
	private Input input;

	private int oldRow = 0;
	private int oldColumn = 0;

	public static String popupMessage = "";
	private int popupX = MainGame.width / 5;
	private int popupY = MainGame.height/5;
	private int popupWidth = MainGame.width * 3 / 5;
	private int popupHeight = MainGame.height * 3 / 5;
	
	private int offset = 25;
	private int spacing = 5;
	private int cellSize = 10;

	private int timeDelta = 0;
	private boolean paused = true;
	private int lastId = -1;
	private int step = 0;
	
	private int rows = 50;
	private int columns = 60;
	private int[][] currentGrid = new int[columns][rows];
	private int[][] nextGrid = new int[columns][rows];
	private int[][] neighborGrid = new int[columns][rows];
	private int neighbors = 0;
	

	public static enum ButtonType {
		HOUSE,MINE;
		public int getValue() {
			return this.ordinal();
		}
	}
	public static ButtonType selectedButton = ButtonType.MINE;
	
	public GameGrid() {
//		currentGrid[3][3] = 1;
//		currentGrid[4][3] = 1;
//		currentGrid[5][3] = 1;
//		boardReset();
	}

    public void boardReset() {
        boardReset(false);
    }

    public void boardReset(boolean clear){
		paused = true;
		step = 0;
		currentGrid = new int[columns][rows];
		nextGrid = new int[columns][rows];
		neighborGrid = new int[columns][rows];
		if(!clear) {
            Random rand = new Random();
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (rand.nextBoolean()) {
                        currentGrid[j][i] = 1;
                    }
                }
            }
        }
	}

	public void mouseReleased(int button, int x, int y) {
//		System.out.println("mouseclick "+button);
//		System.out.println(currentGrid[0][0]);
//		step();
		if(paused){
			int row = (y-offset)/(cellSize+spacing);
			int col = (x-offset)/(cellSize+spacing);
//			System.out.println(row+" "+col);
			if(row > rows-1){
				row = rows-1;
			}
			if(col > columns-1){
				col = columns-1;
			}
			if(currentGrid[col][row] == 1){
				currentGrid[col][row] = 0;
			}
			else{
				currentGrid[col][row] = 1;
			}
		}
	}

	public void mouseMoved(int oldx, int oldy, int newx, int newy) {

	}

	public void render(Graphics g) {
		g.setColor(Color.blue);
		g.fillRect(0, 0, MainGame.width, MainGame.height);
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				if(currentGrid[j][i] == 1){
					g.setColor(Color.black);
				}
				else{
					g.setColor(Color.white);
				}
				g.fillRect(offset+j*(cellSize+spacing),offset+i*(cellSize+spacing),cellSize,cellSize);
//				g.setColor(Color.red);
//				g.drawString(Integer.toString(neighborGrid[j][i]), 30+j*45, 30+i*45);
			}
		}
		g.setColor(Color.red);
		g.drawString(Integer.toString(step), 10, 10);
	}

	public void update(GameContainer gc, int delta) {
		timeDelta += delta;
		if(!paused && timeDelta > 0){
			step();
			timeDelta = 0;
		}
		if(gc.getInput().isKeyPressed(Input.KEY_ENTER)){
			step();
		}
		else if(gc.getInput().isKeyPressed(Input.KEY_P)){
			paused = !paused;
		}
		else if(gc.getInput().isKeyPressed(Input.KEY_SPACE)){
			boardReset();
		}
		else if(gc.getInput().isKeyPressed(Input.KEY_S)){
			save();
		}
		else if(gc.getInput().isKeyPressed(Input.KEY_L)){
			load();
		}
		else if(gc.getInput().isKeyPressed(Input.KEY_C)){
		    boardReset(true);
        }
	}
	
	public void save(){
		paused = true;

	    BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("save.txt"));
			for(int i = 0; i < rows; i++){
				for(int j = 0; j < columns; j++){
					writer.write(currentGrid[j][i]+",");
				}
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void load(){
		paused = true;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("save.txt"));
			String line = reader.readLine();
			int rowCounter = 0;
			int colCounter = 0;
//			int maxColumn = 0;
			while(line != null){
				line = line.substring(0, line.length()-1);
//				System.out.println(line);
				String[] set = line.split(",");
				for(int i = 0; i < set.length; i++){
					currentGrid[i][rowCounter] = Integer.parseInt(set[i]);
				}
				rowCounter++;
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void step(){
		step++;
		int aMin = -1;
		int bMin = -1;
		int aMax = 1;
		int bMax = 1;
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				neighbors = 0;
				aMin = -1;
				bMin = -1;
				aMax = 1;
				bMax = 1;
				if(i < 1){
					bMin = 0;
				}
				else if(i > rows-2){
					bMax = 0;
				}
				if(j < 1){
					aMin = 0;
				}
				else if(j > columns-2){
					aMax = 0;
				}
//				if(i > 0 && j > 0 && i < rows-1 && j < columns-1){
					for(int a = aMin; a <= aMax; a++){
						for(int b = bMin; b <= bMax; b++){
							if(a == 0 && b == 0){}
							else{
//								System.out.println(j+" "+a+" "+i+" "+b);
								if(currentGrid[j+a][i+b] == 1){
									neighbors++;
								}
							}
						}
					}
//				}
				neighborGrid[j][i] = neighbors;
				if(currentGrid[j][i] == 1){
					if(neighbors < 2){
						nextGrid[j][i] = 0;
					}
					else if(neighbors > 3){
						nextGrid[j][i] = 0;
					}
					else{
						nextGrid[j][i] = 1;
					}
				}
				else if(currentGrid[j][i] == 0){
					if(neighbors == 3){
						nextGrid[j][i] = 1;
					}
				}
			}
		}
		currentGrid = nextGrid;
		nextGrid = new int[columns][rows];
	}
}
