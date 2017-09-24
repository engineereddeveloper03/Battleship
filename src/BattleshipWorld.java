import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import gridworld.actor.Actor;
import gridworld.actor.ActorWorld;

import gridworld.grid.Grid;
import gridworld.grid.Location;

import javax.swing.AbstractAction;
import javax.swing.Timer;

import java.awt.event.ActionEvent;

/**
 * A class to extend ActorWorld to work with the Battleship game.  Places the ships setup by the player,
 * handles the key presses and mouse clicks on the world, and checks if the player hit their opponent's
 * ship.
 * 
 * @author Phillip Sturtevant 
 * @version 09/14/2017
 */
public class BattleshipWorld extends ActorWorld
{
	private int shipQuantity;
    private int playerNum;
    private ArrayList<Battleship> playerShips;
    private ArrayList<Location> shipLocations;
    private Game game;
    
    private Location clickLocation;   // Stores player's click location
    private boolean gameStarted = false;
    private boolean hitShip = false;   // set to whether player/computer hit ship in the previous turn
    private boolean canMove = false;   // set whether player can move their Battleship
    
    private int sizeCounter = 0;
    private int shipSize = 5;  // first ship will always have a size of 5
    private int shipCount = 0; // counts how many ships have been spawned
    
    private Battleship ship;  // Player's setup ship
    
    /**
     * Constructs a Player game world with a given grid, player number, number of ships inputed, and the gameInput.
     * @param grid the grid for this world
     * @param playerNumInput the player's ID number
     * @param gameInput the main game controller
     */
    public BattleshipWorld(Grid<Actor> grid, int playerNumInput, Game gameInput)
    {
    	playerShips = null;
    	shipLocations = null;
    	playerNum = playerNumInput;
    	game = gameInput;
    }
    
    /**
     * Constructs a Player game world with a given grid, player number, and number of ships inputed.
     * @param grid the grid for this world
     * @param playerNumInput the player's ID number
     */
    public BattleshipWorld(Grid<Actor> grid, int playerNumInput)
    {
    	playerShips = null;
    	shipLocations = null;
    	playerNum = playerNumInput;
    }
    
    /**
     * Constructs a setup world with a given grid, battleship and location array, player number, and quantity of ships.
     * @param grid the grid for this world
     * @param shipInput the battleships the player owns
     * @param locInput the locations of all the player's battleships
     * @param playerNumInput the player's ID number
     * @param shipNumInput the number of ships in the game
     * @param gameInput the main game controller
     */
    public BattleshipWorld(Grid<Actor> grid, ArrayList<Battleship> shipInput, ArrayList<Location> locInput, int playerNumInput, int shipNumInput, Game gameInput)
    {
        super(grid);
        playerShips = shipInput;
        shipLocations = locInput;
        playerNum = playerNumInput;
        shipQuantity = shipNumInput;
        game = gameInput;
    }
    
    /**
     * Gets the player's number
     * @return the player number
     */
    public int getPlayerNum()
    {
        return playerNum;
    }
    
    /**
     * Gets what location was clicked in the world
     * @return the location clicked in the world
     */
    public Location getClickLocation()
    {
        return clickLocation;
    }
    
    /**
     * Gets whether Computer has hit a Battleship
     * @return true if the Computer hit the Battleship, false if the Computer did not hit the Battleship
     */
    public boolean getHitShip()
    {
        return hitShip;
    }
    
    /**
     * Sets whether it is the Player's turn and they can click squares
     * @param start true if it is the Player's turn, false if it is not the Player's turn
     */
    public void setGameStart(boolean start)
    {
        gameStarted = start;
    }
    
    /**
     * Sets whether Computer hit the Battleship
     * @param hitShip true if the Computer hit the Battleship, false if they did not
     */ 
    public void setHitShip(boolean hitShip)
    {
        this.hitShip = hitShip;
    }
    
    /**
     * Stores the click location of the Computer or Player if they clicked same square twice
     * @param compClick click location of the Computer
     */ 
    public void setClickLocation(Location clickInput)
    {
        clickLocation = clickInput;
    }
    
    /**
     * Overrides original method.  Checks if Player/Computer clicked a grid location. If so and it was their turn, sets their ability to click to false.
     * @param loc Location in the grid that was clicked.
     * @return true if the Player clicked the grid and click is consumed by the world
     */ 
    public boolean locationClicked(Location loc)
    {
        if (gameStarted && clickLocation == null)
        {
        	clickLocation = loc;
        	game.addClickLocation(this);
        }
        return true;
    }
    
    /**
     * Handles the key presses when placing Battleships
     * @param description the String description of the key press
     * @param loc 
     */
    @Override
    public boolean keyPressed(String description, Location loc)
    {
    	if (canMove)
    	{
    		// resets the instruction message every time a key is pressed (replaces error messages)
        	setMessage("Battleship Set-up: Player " + playerNum + "\nUse WASD to move, Q to rotate left, E to rotate right, and press SPACE to place your ship.");
        	
    		if (description.equals("W"))
    		{
    			ship.move(0);
    			return true;
    		}
    		if (description.equals("A"))
            {
            	ship.move(270);
            	return true;
            }
    		if (description.equals("S"))
    		{
    			ship.move(180);
    			return true;
    		}
    		if (description.equals("D"))
    		{
    			ship.move(90);
    			return true;
    		}
    		if (description.equals("E"))
    		{
    			ship.rotateRight(shipLocations);
    			displayMessage();
    			return true;
    		}
    		if (description.equals("Q"))
    		{
    			ship.rotateLeft(shipLocations);
    			displayMessage();
    			return true;
    		}
    		if (description.equals("SPACE"))
    		{
    			// checks if ship is placed in column 11
                for(int i = 0; i < ship.getShipParts().size(); i++)
                {
                    if (ship.getShipParts().get(i).getLocation().getCol() == 10)
                    {
                        ship.setGridError(true);
                    }
                    
                    if (ship.getShipParts().get(i).getLocation().getRow() == 10)
                    {
                        ship.setGridError(true);
                    }
                }
                
                // Registers SPACE press to save ship location if no placement error occurred
                if (!ship.getGridError())
                {
                    shipPlacer();
                    
                    if (shipCount != shipQuantity) // keep making ships to place
                    {
                    	shipSpawner();
                    }
                    else    // setup next player or start the game
                    {
                    	canMove = false;
                    	this.getFrame().dispose();
                    	
                    	// sets up the other player or starts the game if both are set up
                    	if (playerNum == 1)
                    	{
                    		try {
								game.setupGame(2);
							} catch (IOException e) {
								e.printStackTrace();
							}
                    	}
                    	else if (playerNum == 2)
                    	{
                    		game.startGame();
                    	}
                    }
                }
                displayMessage();                
    			return true;
    		}		
    	}
    	return false;
    }
    
    /**
     * Displays setup messages or errors during Battleship setup.  Resets error variable after error message prints.
     */
	public void displayMessage()
	{
		if (ship.getGridError())
        {
            setMessage("Battleship Set-up: Player " + playerNum + "\nError: Cannot place ship in the last column or row!");
            ship.setGridError(false);
        }
		else if (ship.getRotateErrorBat())
	    {
			setMessage("Battleship Set-up: Player " + playerNum + "\nError: Cannot rotate over another ship!");
	        ship.setRotateErrorBat(false);
	    }
	    else if (ship.getRotateErrorBound())
	    {
	        setMessage("Battleship Set-up: Player " + playerNum + "\nError: Battleship too close to boundary to rotate!");
	        ship.setRotateErrorBound(false);
	    }
	    else if (ship.getRotationError()) 
	    {
	    	setMessage("Battleship Set-up: Player " + playerNum + "\nError: Cannot use rotations in tandem.  Use same rotation to return.");
	    	ship.setRotationError(false);
	    }
	}
    
    /**
     * Spawns a Battleship into the world and allows it to move.
     */ 
    public void shipSpawner()
    {
    	// setup block checkers
        int blockedCol = 8;
        int countCol = 0;
        int blockedRow = 7;
    	int countRow = 0;
    	
    	shipCount++;
    	
        boolean setCruiserName = false;
        
        // sets to true to set Cruiser name apart from Submarine
        if (shipCount == 3)
        {
            setCruiserName = true;
        }
    	
        // checks if user blocked themselves in vertically or horizontally
        for (int i = 0; i < shipLocations.size(); i++)
        {
        	if (shipLocations.get(i).getCol() == 9)
        	{
        		countCol++;
        	}
        	
        	if (shipLocations.get(i).getRow() == 9)
        	{
        		countRow++;
        	}
        }
        
        // Builds the Battleship to be placed
        Battleship temp = new Battleship(shipSize, setCruiserName);
        
        if (countCol < blockedCol)
        {
        	temp.buildShip(this,  0, 10);
        }
        else if (countRow < blockedRow)
        {
        	temp.setHorizontal(true);
        	temp.buildShip(this, 10, 0);
        }
        else
        {
        	temp.buildShip(this, 0, 0);
        }
        
        
        ship = temp;
        canMove = true;
    	
    	if (shipCount != 3)
        {
            shipSize--;
        }
    }
    
    /**
     * Handles the placing of each ship.
     */ 
    public void shipPlacer() 
    {   
    	playerShips.add(ship);
        
        // Storing locations of all ship parts in separate array
        for (int j = sizeCounter; j < playerShips.size(); j++)
        {
            // Only adds locations for new ships at end of array
            for (int k = 0; k < playerShips.get(j).getShipParts().size(); k++)
            {
            	shipLocations.add(playerShips.get(j).getShipParts().get(k).getLocation());
            }
        }
        
        sizeCounter = playerShips.size();
    }
    
    /**
     * Checks if the Player click location hit or missed the opponent's Battleships.
     * @param playerShip a list of the opponent's Battleships
     * @param compPlayer the Computer Player
     * @param playerNum the identifying number of the Player/Computer
     * @return true if the Player hit the Battleship, false if the Player missed the Battleship
     */ 
    public boolean hitCheck(ArrayList<Battleship> playerShip, Computer compPlayer)
    {
    	int sleepTimer = 2000;
        BattleshipPart hitOrMissIcon = new BattleshipPart();
        
        // checking if player clicked correct location and setting message and icon appropriately
        for (int i = 0; i < playerShip.size(); i++)
        {
            for (int j = 0; j < playerShip.get(i).getShipParts().size(); j++)
            {
                if (clickLocation.getRow() == playerShip.get(i).getShipParts().get(j).getLocation().getRow() && 
                    clickLocation.getCol() == playerShip.get(i).getShipParts().get(j).getLocation().getCol())
                {
                	// Show message and graphics for shot HIT
                    hitOrMissIcon.setImage(6);
                    hitShip = true;
                    setGraphics(1, sleepTimer, hitOrMissIcon, playerShip, compPlayer);
                    
                    // adds hit to individual ship
                    playerShip.get(i).setHitCount();
                
                    return true;
                }
            }
        }
        
        // Show message and graphics for shot MISS
        hitOrMissIcon.setImage(7);
        setGraphics(2, sleepTimer, hitOrMissIcon, playerShip, compPlayer);
        
        // prompt computer to check other side of ship & cardinal directions if fails to hit
        // change back if does not work: playerNum == 2 && compPlayer != null && compPlayer.getHitLocCurrent() != null
        if (playerNum == 2 && compPlayer != null)
        {
            hitShip = false;
        }
        
        return false;
    }
    
    /**
     * Checks the status of all Battleships to see if they are alive.
     * @param shipList a list of the opponent's Battleships
     * @param compPlayer the Computer Player
     */ 
    public void shipStatus(ArrayList<Battleship> shipList, Computer compPlayer) 
    {
        for(int i = 0; i < shipList.size(); i++)
        {
            if (shipList.get(i).shipSunk())
            {
            	setMessage("Player " + playerNum + "\nYou sunk their " + shipList.get(i).getShipName() + "!");
                shipList.get(i).buildBrokenShip(this);
                
                // stops Computer from looking for ship if destroyed
                if (compPlayer != null && playerNum == 2)
                {
                    hitShip = false;
                    compPlayer.setHitLocBase(null);
                    compPlayer.setHitLocCurrent(null);
                }
            }
        }
    }
    
    /**
     * Shows paced message and image displays for plays and turnovers
     * @param messageType controls which graphics are updated by function
     * @param sleepTimer the length of time the program will pause (in milliseconds)
     * @param hitOrMissIcon the hit or missed BattleshipPart object
     * @param playerShip a list of the opponent's Battleships
     * @param compPlayer the Computer Player
     */ 
    public void setGraphics(int messageType, int sleepTimer, BattleshipPart hitOrMissIcon, ArrayList<Battleship> playerShip, Computer compPlayer)
    {
    	// setup hit messages
    	String playerName = "Player " + playerNum;
    	
    	// change player name if VS Computer
    	if (playerNum == 2 && compPlayer != null)
    	{
    		playerName = "Computer";
    	}
    	
        String message1 = playerName + "\nYour shot is a... ";
        String message2 = playerName + "\nYour shot is a...Hit!";
        String message3 = playerName + "\nYour shot is a...Miss!";
        BattleshipWorld currentWorld = this;
        
    	// Print hit message only if checking for hits
        if (messageType < 3)
        {
        	setMessage(message1);
        }

        // create timer to display hit status, check if a ship is sunk, or change player turns
        // help with this code from users on Stack Overflow
    	Timer timer = new Timer(sleepTimer, new AbstractAction() {
    	    /**
			 * AbstractAction serialID
			 */
			private static final long serialVersionUID = 7329041588411304413L;

			@Override
    	    public void actionPerformed(ActionEvent ae) {
    	    	
    	    	switch (messageType)
    	    	{
    	    		case 1: setMessage(message2);
    	    				add(clickLocation, hitOrMissIcon);
    	    				setGraphics(3, 1000, hitOrMissIcon, playerShip, compPlayer);
    	    				break;
    	    		
    	    		case 2: setMessage(message3);
    	    				add(clickLocation, hitOrMissIcon);
    	    				setGraphics(4, 2000, hitOrMissIcon, playerShip, compPlayer);
    	    				break;
    	    	
    	    		case 3: shipStatus(playerShip, compPlayer);
    	    				setGraphics(4, 1000, hitOrMissIcon, playerShip, compPlayer);
    	    				break; 
    	    	
    	    		// Setup next turn
    	    		case 4: clickLocation = null;
    	    				game.setupNextTurn(currentWorld, playerNum);
    	    				break;
    	    				
    	    		default: System.out.println("Something went wrong at method: setGraphics.");
    	    				 break;
    	    	}
    	    }
    	});
    	
    	timer.setRepeats(false);  //the timer should only go off once
    	timer.start();
    }
}

