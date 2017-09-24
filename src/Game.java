import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import gridworld.grid.Location;
import gridworld.grid.BoundedGrid;

import gridworld.actor.Actor;

import javax.swing.JOptionPane;

/**
 * Handles the setup of each Player's battleships, the running of turns between players, and
 * keeps track their scores.
 * 
 * @author Phillip Sturtevant
 * @version 09/14/2017
 */
public class Game 
{
	// Initializing score window, scores, and menu
	private ScoreMenu scoreWindow = new ScoreMenu();
	private int score1 = 0;
    private int score2 = 0;
	private MainMenu menu;
	
	// screen resolution variables
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	// Creating a new Battleship and Location ArrayLists for the Players	
	private ArrayList<Battleship> player1Ships = new ArrayList<Battleship>();
	private ArrayList<Battleship> player2Ships = new ArrayList<Battleship>(); 
	private ArrayList<Location> player1Locations = new ArrayList<Location>();
	private ArrayList<Location> player2Locations = new ArrayList<Location>();
    
    // Creating ArrayLists to hold the Player's clicks
	private ArrayList<Location> player1Clicks = new ArrayList<Location>();
	private ArrayList<Location> player2Clicks = new ArrayList<Location>();
    
    // Creating the playable worlds
	private BattleshipWorld player1World = new BattleshipWorld(new BoundedGrid<Actor>(10, 10), 1, this);
	private BattleshipWorld player2World = new BattleshipWorld(new BoundedGrid<Actor>(10, 10), 2, this);
    
    // Computer player and name setup
	private Computer compPlayer;
	private String player2Name;
	
	// toggle between Player 1 and Player 2 turn
	private boolean player1Turn = true;
	
	/**
	 * Constructs a new game with the saved data selected from the main menu
	 * @param menuInput main menu of the game
	 */
	public Game(MainMenu menuInput)
	{
		menu = menuInput;
	}

	public BattleshipWorld getPlayer1World()
	{
		return player1World;
	}
	
	public BattleshipWorld getPlayer2World()
	{
		return player2World;
	}
	
	public ArrayList<Location> getPlayer1Clicks()
	{
		return player1Clicks;
	}
	
	public ArrayList<Location> getPlayer2Clicks()
	{
		return player2Clicks;
	}
	
	// TODO delete exception if it doesn't work
	/**
	 * Sets up the world before the game begins
	 * @param currentPlayer current player setting up their world
	 * @throws IOException 
	 */
	public void setupGame(int currentPlayer) 
			throws IOException
	{
		// setup second player's name
		if (menu.getTwoPlayers())
        {
        	player2Name = "Player 2";
        }
        else
        {
        	player2Name = "Computer";
        }
		
		// setup for Player 1 and 2
		if (menu.getTwoPlayers() || currentPlayer == 1)
		{
			battleshipSetup(currentPlayer);
			
		}
		else     // setup of Computer
		{
			compPlayer = new Computer(menu.getNumOfShips());
			// set score window label to "Computer:"
        	scoreWindow.playerDisplay(menu.getTwoPlayers());
            
            // computer picking setup of battleships and locations
            compPlayer.pickSetup();
            compPlayer.readShipLocations();
            
            // setup battleships for Computer
            for (int i = 0; i < compPlayer.getComputerLocations().size(); i++)
            {
            	player2Locations.add(compPlayer.getComputerLocations().get(i));
            }
            
            for (int i = 0; i < compPlayer.getComputerShips().size(); i++)
            {
            	player2Ships.add(compPlayer.getComputerShips().get(i));
            	startGame();
            }
		}
	}
	
    /**
     * Creates a setup Battleship window when the game starts.
     * @param playerNum the Player/Computer identifying number
     */
    public void battleshipSetup(int playerNum)
    {
    	int setupWorldWidth = 579;
    	int setupWorldHeight = 670;
    	
    	BattleshipWorld setupWorld;
    	
        if (playerNum == 1)
        {
        	setupWorld = new BattleshipWorld(new BoundedGrid<Actor>(11, 11), player1Ships, player1Locations, playerNum, menu.getNumOfShips(), this);
        }
        else
        {
        	setupWorld = new BattleshipWorld(new BoundedGrid<Actor>(11, 11), player2Ships, player2Locations, playerNum, menu.getNumOfShips(), this);
        }
    	
        setupWorld.setMessage("Battleship Set-up: Player " + playerNum + "\nUse WASD to move, Q to rotate left, E to rotate right, and press SPACE to place your ship.");
        setupWorld.show();
        setupWorld.getFrame().setLocation((screenSize.width/2) - (setupWorldWidth/2), (screenSize.height/2) - (setupWorldHeight/2));
        setupWorld.getFrame().setSize(setupWorldWidth, setupWorldHeight);
        setupWorld.shipSpawner();
    }
	
    /**
     * Sets the window titles for the players and starts the main game loop (turnUpdate).
     */
    public void startGame()
    {
    	// set start message
        player1World.setMessage("Player 1");
        player2World.setMessage(player2Name);
    	
    	// show worlds and score window
    	player1World.show();
        player2World.show();
        scoreWindow.setVisible(true);
        
        // setup window location
        int worldWidth = player1World.getFrame().getWidth();
    	int worldHeight = (player1World.getFrame().getHeight() / 2) - 25;
    	
        player1World.getFrame().setLocation((screenSize.width/2) - worldWidth, (screenSize.height/2) - worldHeight);
        player2World.getFrame().setLocation((screenSize.width/2), (screenSize.height/2) -  worldHeight);
        
        
    
        turnUpdate();
    }
    
    /**
     * Main game loop where Players take turns.
     */
    public void turnUpdate()
    {
        // updates player's scores on the menu
        scoreWindow.updateScoreMenu(score1, score2);     
        
        // checks if player won the game
        if (score1 == player2Locations.size())
        {
        	JOptionPane.showMessageDialog(null, "Player 1 sunk all of " + player2Name + "'s Battleships!");
            
            player1World.setGameStart(false);
            player2World.setGameStart(false);
            playAgain();
        }
        else if (score2 == player1Locations.size())
        {
        	JOptionPane.showMessageDialog(null, player2Name + " sunk all of Player 1's Battleships!");
        	
            player1World.setGameStart(false);
            player2World.setGameStart(false);
            playAgain();
        }
        else  // No players won.  Keep running turns.
        {
        	runTurn();
        } 
    }
    
    /**
     * Runs a turn for a single player.
     */ 
    public void runTurn()
    {
    	// set reference object for player interaction
        BattleshipWorld player = player1World;
        
        if (!player1Turn)
        {
        	player = player2World;
        }
        
        // If player turn, set message and let them click a location
        if (player1Turn || menu.getTwoPlayers())   
        {
        	player.setMessage("Player " + player.getPlayerNum() + "\nYour turn.  Select a square target of attack.");
        	player.setGameStart(true);
        }
        else     // Computer runs their turn
        {
        	addClickLocation(player);
        }
    }
    
    /**
     * After click validated, adds click location to proper array
     * @param playerWorld  the player's BattleshipWorld
     */ 
    public void addClickLocation(BattleshipWorld playerWorld)
    {
        // checking if player clicked same square in the game round
        if (playerWorld.getPlayerNum() == 1 && doubleClick(player1Clicks, playerWorld.getClickLocation()) ||
        	menu.getTwoPlayers() && playerWorld.getPlayerNum() == 2 && doubleClick(player2Clicks, playerWorld.getClickLocation()))
        {
        	playerWorld.setMessage("Player " + playerWorld.getPlayerNum() + "\nError: Area already bombed.  Select another location.");
        	playerWorld.setClickLocation(null);
        }
        else  // if not same square, saves its location
        {
        	// Adds player/computer click location to proper arrayList
        	if (playerWorld.getPlayerNum() == 1)
            {
            	player1Clicks.add(playerWorld.getClickLocation());
            }
            else if (playerWorld.getPlayerNum() == 2 && menu.getTwoPlayers())
            {
            	player2Clicks.add(playerWorld.getClickLocation());
            }
            else   // Computer takes its turn
            {
            	player2Clicks.add(compPlayer.takeTurn(playerWorld, player2Clicks));
            }
            
            // checks if player hit the ship
            if (playerWorld.getPlayerNum() == 1 && playerWorld.hitCheck(player2Ships, compPlayer))
            {
                score1++;
            }
            if (playerWorld.getPlayerNum() > 1 && playerWorld.hitCheck(player1Ships, compPlayer))
            {
            	score2++;
            }
       	}
    }
    
    /**
     * Turn off player ability to click, toggle whose turn it is, and run the next turn
     * @param playerWorld  the player's BattleshipWorld
     * @param playerNum    the player's identifying number
     */ 
    public void setupNextTurn(BattleshipWorld playerWorld, int playerNum)
    {
    	if (playerNum == 1)
        {
        	playerWorld.setMessage("Player 1");
        }
        else
        {
        	playerWorld.setMessage(player2Name);
        }
        
        // Turn off player ability to click, toggle whose turn it is, and run the next turn
        playerWorld.setGameStart(false);
        player1Turn = !player1Turn;
        turnUpdate();
    }

    /**
     * Checks if the Player clicked same location twice.
     * @param pastClickLocations a list of the Player's click locations
     * @param clickLocation the click location to compare against previous clicks
     * @return true if Player clicked the same square, false if the Player clicked a new square
     */ 
    public boolean doubleClick(ArrayList<Location> pastClickLocations, Location clickLocation)
    {
        for (int i = 0; i < pastClickLocations.size(); i++)
        {
            if (pastClickLocations.get(i).getRow() == clickLocation.getRow() &&
                pastClickLocations.get(i).getCol() == clickLocation.getCol())
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Pops up a window asking if the player would like to play the game again.  If so, pops up a new MainMenu to select
     * from.
     */ 
    public void playAgain()
    {
    	MainMenu menu = new MainMenu();
    	
    	int playAgain = JOptionPane.showOptionDialog(null, "Would you like to play again?", "Play Again?", JOptionPane.YES_NO_OPTION, 
    												 JOptionPane.QUESTION_MESSAGE, null, null, null);
    	
    	if (playAgain == JOptionPane.YES_OPTION)
    	{
    		menu.setVisible(true);
    	}
    	
    	player1World.getFrame().dispose();
    	player2World.getFrame().dispose();
    	scoreWindow.closeWindow();
    }
}
