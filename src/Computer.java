import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.ArrayList;

import gridworld.grid.BoundedGrid;
import gridworld.grid.Location;

import gridworld.actor.Actor;

/**
 * Sets up computer's battleship locations and sets how to pick ships on their turn
 * 
 * @author Phillip Sturtevant 
 * @version 09/14/2017
 */
public class Computer
{
    private ArrayList<Location> compLocations;
    private ArrayList<Battleship> compShips;
    private Location hitLocBase;   // first hit location on current targeted battleship
    private Location hitLocCurrent; // the Computer's second/third/etc. location on the current Battleship target
    private int shipQuantity;
    
    /**
     * Constructs a Computer with 3 default ships.
     */
    public Computer()
    {
        shipQuantity = 3;
        hitLocBase = null;
    }
    
    /**
     * Constructs a Computer with a given number of battleships
     * @param quantityInput Input quantity of ships declared in main menu.
     */
    public Computer(int quantityInput)
    {
        compLocations = new ArrayList<Location>();
        compShips = new ArrayList<Battleship>();
        shipQuantity = quantityInput;
    }
    
    /**
     * Gets the locations of the Battleship parts.
     * @return the locations of the Battleship parts
     */
    public ArrayList<Location> getComputerLocations()
    {
        return compLocations;
    }
    
    /**
     * Gets the Computer's Battleship objects.
     * @return the list of the Computer's Battleship objects
     */
    public ArrayList<Battleship> getComputerShips()
    {
        return compShips;
    }
    
    /**
     * Gets the Computer's second/third/etc. location on the current Battleship target.
     * @return the Computer's second/third/etc. location on the current Battleship target
     */
    public Location getHitLocCurrent()
    {
        return hitLocCurrent;
    }
    
    /**
     * Sets the computer's first hit location on a battleship.
     * @param hitLocation Hit location detected from BattleshipWorld click.
     */
    public void setHitLocBase(Location hitLocation)
    {
        hitLocBase = hitLocation;
    }
    
    /**
     * Sets the second/third/etc location on the current battleship target.
     * @param hitLocation Hit location detected from BattleshipWorld click.
     */
    public void setHitLocCurrent(Location hitLocation)
    {
        hitLocCurrent = hitLocation;
    }
    
    /**
     * Computer chooses which set-up of ships it will pick from the external file of ship locations.
     * @throws IOException 
     */
    public void pickSetup()
           throws IOException
    {
    	// 
    	BufferedReader inputStream;
        int locationNum = 0;
    	
        if (shipQuantity == 3)
        {
            inputStream = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/threeshiplocations.txt")));
            locationNum = 12;
        }
        else if (shipQuantity == 4)
        {
            inputStream = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/fourshiplocations.txt")));
            locationNum = 15;
        }
        else
        {
            inputStream = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/fiveshiplocations.txt")));
            locationNum = 17;
        }
        
        // how many lines to read before recording values to location ArrayList
        int readBegin = randChoice(2) * 2;
        for(int i = 0; i < readBegin; i++)
        {
        	inputStream.readLine();
        }
        
        // Begin reading values into ArrayList
        for (int i = 0; i < locationNum; i++)
        {
        	// read ASCII and change to integer
        	int row = inputStream.read() - 48;
        	inputStream.skip(2);   // skip 2 spaces
        	int col = inputStream.read() - 48;
        	inputStream.skip(3);   // skip 3 spaces
            
            Location temp = new Location(row, col);
            compLocations.add(temp);
        }
        inputStream.close();
    }
    
    /**
     * Takes the location data from the file, sets it to the location in BattleshipPart, and stores each
     * part in a complete Battleship object
     */ 
    public void readShipLocations()
    {
        int counter = 0;
        int shipSize = 0;
        int shipLoc = 0;
        
        for (int i = 0; i < shipQuantity; i++)
        {
            BattleshipWorld tempWorld = new BattleshipWorld(new BoundedGrid<Actor>(10, 10), 3);
            
            // Setting correct ship size
            if (i == 0)
            {
                counter = 5;
            }
            else if (i == 1)
            {
                counter = 4;
            }
            else if (i == 2 || i == 3)
            {
                counter = 3;
            }
            else
            {
                counter = 2;
            }
            
            // sets to true to set Cruiser name apart from Submarine
            boolean setCruiserName = false;
            if (i == 2)
            {
                setCruiserName = true;
            }
            
            Battleship tempShip = new Battleship(counter, setCruiserName);
            
            // Setting orientation of battleship based on location
            if (compLocations.get(shipLoc).getRow() == compLocations.get(shipLoc + 1).getRow())
            {
                tempShip.setShipRotation(true);
            }
            
            // Pulling locations from ArrayList, setting to individual parts, and placing parts inside ships
            shipSize += counter;
            for(int j = shipLoc; j < shipSize; j++)
            {
                BattleshipPart tempPart = new BattleshipPart();
                tempWorld.add(tempPart);
                tempPart.moveTo(compLocations.get(j));
                tempShip.setShipParts(tempPart);
            }
            compShips.add(tempShip);
            
            shipLoc += counter;
        }
    }
    
    /**
     * Randomly guesses a click location.  If a location is hit, guesses near that location until
     * the battleship is sunk.
     * @param playerWorld the play world of the Computer
     * @param clickLocations previous Computer click locations
     * @return returns the location guessed by the Computer
     */
    public Location takeTurn(BattleshipWorld playerWorld, ArrayList<Location> clickLocations)
    {
    	Location guessLoc = null;   // guess click location of the Computer
    	
    	// setting new hit location if hit previous turn
        if (hitLocBase == null && playerWorld.getHitShip())
        {
            hitLocBase = clickLocations.get(clickLocations.size() - 1);
        }
        else if (hitLocBase != null && playerWorld.getHitShip())   // extending for computer to guess off of
        {
            hitLocCurrent = clickLocations.get(clickLocations.size() - 1);
        }
    	
    	if (hitLocBase != null && hitLocCurrent == null)   // Clicks one of the cardinal directions from hit
        {
            guessLoc = cardinalClick(playerWorld, clickLocations);
        }
        else if (playerWorld.getHitShip() && hitLocCurrent != null)  // pattern guess in line after second part is found
        {
            guessLoc = patternLocCheck(playerWorld, clickLocations);
            
            if(guessLoc == null)   // If hit the world boundary, jump to the last pattern guess
            {
                guessLoc = compareHitLoc(playerWorld, clickLocations);
            }
        }
        else if (!playerWorld.getHitShip() && hitLocCurrent != null) // If reached world boundary/end of ship, checks the other side of original hit
        {
            guessLoc = compareHitLoc(playerWorld, clickLocations);
        }
        
    	// Computer sets guess to world to setup hit/miss image
        if (guessLoc != null)   // educated guess
        {
        	playerWorld.setClickLocation(guessLoc);
        }
        else    // random guess
        {
        	guessLoc = playerWorld.getRandomEmptyLocation();
            playerWorld.setClickLocation(guessLoc);
        }
        
        return guessLoc;
    }
    
    /**
     * Computer randomly selects a cardinal direction away from first Battleship hit location.
     * @param player the Computer's play area
     * @param clickLocations an array of the Computer's previous click locations
     * @return the location of the next place the Computer will guess
     */ 
    public Location cardinalClick(BattleshipWorld player, ArrayList<Location> clickLocations)
    {
        int row = 0;
        int col = 0;
        int randDirection = 0;
        boolean north = true;
        boolean east = true;
        boolean south = true;
        boolean west = true;
        boolean validLoc = false;
                
        // Checks for a valid location to click around found battleship
        while ((north || east || south || west ) && !validLoc)
        {
            // gets last row and column clicked and resets row and column for re-check
            row = hitLocBase.getRow();
            col = hitLocBase.getCol();
            
            // sets a random direction to check
            randDirection = randChoice(4);
               
            if (randDirection == 0 && north)
            {
                row--;
                north = false;
            }
            else if (randDirection == 1 && east)
            {
                col++;
                east = false;
            }
            else if (randDirection == 2  && south)
            {
                row++;
                south = false;
            }
            else if (randDirection == 3 && west)
            {
                col--;
                west = false;
            }
            
            // Checks if computer click is out of bounds
            if (row >= 0 && row < 10 && col >= 0 && col < 10)
            {
                validLoc = true;
            }
            else
            {
                validLoc = false;
            }
            
            // Checks if computer click has already been selected
            for (int i = 0; i < clickLocations.size(); i++)
            {
                if (row == clickLocations.get(i).getRow() && col == clickLocations.get(i).getCol())
                {
                    validLoc = false;
                }
            }
        }
        
        if (validLoc)
        {
            return new Location(row, col);
        }
        else      // should not be reached, but backup in case something unexpected happens
        {
            player.setHitShip(false);
            System.out.println("Something went wrong in method: cardinalClick");
            return null;
        }
    }
    
    /**
     * Computer checks for second/third/etc. Battleship part on same Battleship target.
     * @param player the Computer's play area
     * @param clickLocations previous Computer click locations
     * @return the location of the next place the Computer will guess
     */ 
    public Location patternLocCheck(BattleshipWorld player, ArrayList<Location> clickLocations)
    {
        Location tempLoc;     // setting temporary location to return at end
        
        // first checks if ship is vertical
        if (hitLocCurrent.getRow() > hitLocBase.getRow() || hitLocCurrent.getRow() < hitLocBase.getRow())
        {
            if (hitLocCurrent.getRow() > hitLocBase.getRow())
            {
                tempLoc = new Location((hitLocCurrent.getRow() + 1), hitLocCurrent.getCol());
            }
            else
            {
                tempLoc = new Location((hitLocCurrent.getRow() - 1), hitLocCurrent.getCol());
            }
        }
        else     // performs if ship is horizontal
        {
            if (hitLocCurrent.getCol() > hitLocBase.getCol())
            {
                tempLoc = new Location(hitLocCurrent.getRow(), (hitLocCurrent.getCol() + 1));
            }
            else
            {
                tempLoc = new Location(hitLocCurrent.getRow(), (hitLocCurrent.getCol() - 1));
            }
        }
        
        // making sure guess does not go out of bounds
        if (tempLoc.getRow() < 0 || tempLoc.getRow() > 9 || tempLoc.getCol() < 0 || tempLoc.getCol() > 9)
        {
            return null;
        }
        
        // make sure computer does not guess already guessed location
        for (int i = 0; i < clickLocations.size(); i++)
        {
        	if (tempLoc.getRow() == clickLocations.get(i).getRow() && tempLoc.getCol() == clickLocations.get(i).getCol())
        	{
        		return null;
        	}
        }
        
        return tempLoc;
    }
    
    /**
     * Computer checks opposite side of hitLocBase for remainder of ship to destroy.
     * @param player the Computer's play area
     * @param clickLocations previous Computer click locations
     * @return the location of the next place the Computer will guess 
     */ 
    public Location compareHitLoc(BattleshipWorld player, ArrayList<Location> clickLocations)
    {
        Location tempLoc;     // setting temporary location to return at end
        
        if (hitLocBase.getRow() > hitLocCurrent.getRow() || hitLocBase.getRow() < hitLocCurrent.getRow())
        {
            if (hitLocBase.getRow() > hitLocCurrent.getRow())
            {
                tempLoc = new Location((hitLocBase.getRow() + 1), hitLocBase.getCol());
            }
            else
            {
                tempLoc = new Location((hitLocBase.getRow() - 1), hitLocBase.getCol());
            }
        }
        else
        {
            if (hitLocBase.getCol() > hitLocCurrent.getCol())
            {
                tempLoc = new Location(hitLocBase.getRow(), (hitLocBase.getCol() + 1));
            }
            else
            {
                tempLoc = new Location(hitLocBase.getRow(), (hitLocBase.getCol() - 1));
            }
        }
        
        // making sure guess does not go out of bounds
        if (tempLoc.getRow() < 0 || tempLoc.getRow() >= 10 || tempLoc.getCol() < 0 || tempLoc.getCol() >= 10)
        {
            player.setHitShip(false);
            return null;
        }
        
        // make sure computer does not guess already guessed location
        for (int i = 0; i < clickLocations.size(); i++)
        {
        	if (tempLoc.getRow() == clickLocations.get(i).getRow() && tempLoc.getCol() == clickLocations.get(i).getCol())
        	{
        		return null;
        	}
        }
        
        return tempLoc;
    }
    
    /**
     * Creates a random number within the given range starting from 0.
     * @param range a selected range of values to pick from
     * @return the random integer between the given range
     */ 
    public int randChoice(int range)
    {
        // Give a random number from the range
        return (int)(range*Math.random());
    }
}
