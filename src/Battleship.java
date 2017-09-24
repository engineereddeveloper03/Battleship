import java.util.ArrayList;

import gridworld.actor.Actor;

import gridworld.grid.Grid;
import gridworld.grid.Location;

/**
 * A class to contain all information on the complete Battleship.
 * 
 * @author Phillip Sturtevant
 * @version 09/13/2017
 */

public class Battleship extends Actor
{
    private ArrayList<BattleshipPart> completeShip = new ArrayList<BattleshipPart>();
    private int size;
    private int hitCount;
    private String name;
    private boolean horizontal = false;
    private boolean rotLeft = false;   // prevents right rotation when is rotated left
    private boolean rotRight = false;   // prevents left rotation when is rotated right
    private boolean sunk = false;
    
    // Grid Errors
    private boolean gridError = false;
    private boolean rotateErrorBat = false;
    private boolean rotateErrorBound = false;
    private boolean rotationError = false;
    
    /**
     * Constructs a vertical Battleship of class Destroyer with a size of 2.
     */
    public Battleship()
    {
        size = 2;
        hitCount = 0;
        name = "Destroyer";
        horizontal = false;
    }
    
    /**
     * Constructs a Battleship with a given size and name.
     * @param sizeInput the size of the Battleship
     * @param setCruiserName true if Battleship is a Cruiser, false if it is not
     */
    public Battleship(int sizeInput, boolean setCruiserName)
    {
        size = sizeInput;
        hitCount = 0;
        horizontal = false;
        
        // Setting name of battleship based on size
        if (sizeInput == 5)
        {
            name = "Carrier";
        }
        else if (sizeInput == 4)
        {
            name = "Battleship";
        }
        else if (setCruiserName)
        {
            name = "Cruiser";
        }
        else if (sizeInput == 3)
        {
            name =  "Submarine";
        }
        else
        {
            name = "Destroyer";
        }
    }
    
    /**
     * Gets the name of the Battleship.
     * @return the name of the Battleship
     */
    public String getShipName()
    {
        return name;
    }
    
    /**
     * Gets the hit count on the Battleship.
     * @return the hit count on the Battleship
     */
    public int getHitCount()
    {
        return hitCount;
    }
    
    /**
     * Gets the size of the Battleship.
     * @return the size of the Battleship
     */
    public int getSize()
    {
        return size;
    }
    
    /**
     * Gets whether Player attempted to place Battleship in column 11.
     * @return true if Player set Battleship in column 11, false if Player did not set Battleship in column 11
     */
    public boolean getGridError()
    {
        return gridError;
    }
    
    /**
     * Gets whether Player rotated the Battleship over another Battleship
     * @return true if Player rotated Battleship over another Battleship, false if they did not
     */
    public boolean getRotateErrorBat()
    {
        return rotateErrorBat;
    }
    
    /**
     * Gets whether the Player rotated the Battleship over the grid boundary
     * @return true if Player rotated the Battleship over the grid boundary, false if they did not
     */
    public boolean getRotateErrorBound()
    {
        return rotateErrorBound;
    }
    
    /**
     * Gets whether the Player rotated the Battleship when left/right when right/left
     * @return true if Player rotated left/right when already rotated right/left, false if they did not
     */
    public boolean getRotationError()
    {
        return rotationError;
    }
    
    /**
     * Gets an ArrayList of the BattleshipParts contained in the Battleship.
     * @return the ArrayList of BattleshipParts contained in the Battleship
     */
    public ArrayList<BattleshipPart> getShipParts()
    {
        return completeShip;
    }
    
    /**
     * Change the orientation of battleship for setup.
     * @param horizInput true to set ship horizontal, false to set it vertical
     */
    public void setHorizontal(boolean horizInput)
    {
        horizontal = horizInput;
    }
    
    /**
     * Increases the hits on the Battleship by 1.
     */
    public void setHitCount()
    {
        hitCount += 1;
    }
    
    /**
     * Adds the Battleship parts to the Battleship for the Computer.
     * @param updatedPart BattleshipPart to add to the ArrayList
     */
    public void setShipParts(BattleshipPart updatedPart)
    {
        completeShip.add(updatedPart);
    }
    
    /**
     * Sets the Battleship rotation (for image orientation) for the Computer based on input locations from file.
     * @param horizontal true if Battleship is horizontal, false if Battleship is vertical
     */ 
    public void setShipRotation(boolean horizontal)
    {
        this.horizontal = horizontal;
        rotRight = true;
    }
    
    /**
     * Sets whether the Player placed a Battleship in column 11.
     * @param errorInput true if Player placed Battleship in column 11, false if they did not
     */
    public void setGridError(boolean errorInput)
    {
        gridError = errorInput;
    }
    
    /**
     * Sets whether the Player rotated a Battleship over another Battleship
     * @param errorInput true if Player rotated a Battleship over another Battleship, false if they did not
     */
    public void setRotateErrorBat(boolean errorInput)
    {
        rotateErrorBat = errorInput;
    }
    
    /**
     * Sets whether Player rotated a Battleship over the grid boundary
     * @param errorInput true if Player rotated a Battleship over the grid boundary, false if they did not
     */
    public void setRotateErrorBound(boolean errorInput)
    {
        rotateErrorBound = errorInput;
    }
    
    /**
     * Sets whether Player rotated a Battleship over the grid boundary
     * @param errorInput true if Player rotated a Battleship over the grid boundary, false if they did not
     */
    public void setRotationError(boolean errorInput)
    {
        rotationError = errorInput;
    }
       
    /**
     * Checks if the Battleship is sunk.
     * @return true if the Battleship is sunk, false if the Battleship is alive
     */ 
    public boolean shipSunk()
    {
        if (hitCount == size)
        {
            hitCount = 0;
            sunk = true;
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Builds the Battleship and images and adds it to the setup world.
     * @param worldInput Player world to build the Battleship image
     * @param row input spawn row
     * @param col input spawn column
     */ 
    public void buildShip(BattleshipWorld worldInput, int row, int col)
    {
    	// setting correct rotation if spawned horizontal
    	if (horizontal)
    	{
    		rotRight = true;
    	}
    	
        for (int i = 0; i < size; i++)
        {
            completeShip.add(new BattleshipPart());
            setSegmentType(i);
            worldInput.add(new Location(row, col), completeShip.get(i));
            
            // changing spawn position based whether player blocked themselves in
            if (horizontal)
            {
            	col++;
            }
            else
            {
            	row++;
            }
        }
        
        // if spawned horizontal, rotate images to correct position
        if (horizontal)
        {
        	horizontal = false;
        	imageRotator(true);
        	horizontal = true;
        }
    }
    
    /**
     * Builds the broken Battleship and images and adds it to the play world.
     * @param worldInput Player world to build the broken Battleship image
     */ 
    public void buildBrokenShip (BattleshipWorld worldInput)
    {
        for (int i = 0; i < size; i++)
        {
            completeShip.add(new BattleshipPart());
            setSegmentType(i);
            worldInput.add(new Location(completeShip.get(i).getLocation().getRow(), 
                                        completeShip.get(i).getLocation().getCol()), completeShip.get(i+size));
        }
        
        // Flip definition to reuse imageRotator method for sunken ships as well.
        if (horizontal)
        {
            horizontal = false;
        }
        else
        {
            horizontal = true;
        }
        
        // rotating depending on left or right rotation
        if (rotRight)
        {
        	imageRotator(true);
        }
        else
        {
        	imageRotator(false);
        }
    }
    
    /**
     * Sets the image of the Battleship part in its ArrayList.
     * @param index BattleshipPart location in the ArrayList
     */ 
    public void setSegmentType(int index)
    {
        int middle = size - 2 - index;  // determines if current segment is middle
        int imageIncr = 0;  // variable to help increment image if sunk or alive
        
        if (sunk)
        {
            imageIncr += 3;
            index += size;
        }
        
        if (index == 0 || imageIncr > 0 && index == size)
        {
            completeShip.get(index).setImage(imageIncr);
        }
        else if (middle >= 0)
        {
            completeShip.get(index).setImage(imageIncr+1);
        }
        else 
        {
            completeShip.get(index).setImage(imageIncr+2);
        }
    }
    
    /**
     * Overrides the act method to do nothing.
     */ 
    public void act(){};
    
    /**
     * Prevents the Battleship from moving outside the world boundary.
     * @param direction the direction the Player intends to move ship
     * @return true if the Battleship can be moved to the next location, false if the Battleship cannot be moved to the next location
     */ 
    public boolean canMove(int direction)
    {
        // Preventing battleship from moving past GridWorld boundary
        for (int i = 0; i < completeShip.size(); i++)
        {
            Grid<Actor> gr = completeShip.get(i).getGrid();
            if (gr == null)
            {
                return false;
            }
            
            Location loc = completeShip.get(i).getLocation();
            Location next = loc.getAdjacentLocation(direction);
            
            if (!gr.isValid(next))
            {
                return false;
            }
            
            // check if battleship tries to move over another placed battleship
            Actor neighbor = gr.get(next);
            if (neighbor != null && !horizontal)
            {
                if (direction == 90 || direction == 270)
                {
                    return false;
                }
                if (i == 0 && direction == 0)
                {
                    return false;
                }
                if (i == (completeShip.size() - 1) && direction == 180)
                {
                    return false;
                }
                
            }
            if (neighbor != null && horizontal)
            {
                if (direction == 0 || direction == 180)
                {
                    return false;
                }
                // handles right rotation
                if (i == 0 && direction == 270 && rotRight)
                {
                    return false;
                }
                if (i == (completeShip.size() - 1) && direction == 90 && rotRight)
                {
                    return false;
                }
                // handles left rotation
                if (i == 0 && direction == 90 && rotLeft)
                {
                    return false;
                }
                if (i == (completeShip.size() - 1) && direction == 270 && rotLeft)
                {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Moves the Battleship in the given direction.
     * @param direction the direction in which the Player chose to move their Battleship
     */ 
    public void move(int direction)
    {
        if (canMove(direction))
        {
            // Controlling ship movement based on orientation preventing segment destruction
        	if ((!horizontal && direction == 180) || (horizontal && direction == 90 && rotRight) || (horizontal && direction == 270 && rotLeft))
            {
        		for (int i = size-1; i >= 0; i--)
        		{
        			completeShip.get(i).move(direction);
                }
            }
        	else
            {
        		for (int i = 0; i < size; i++)
                {
        			completeShip.get(i).move(direction);
                }
        	}
        }
    }
    /**
     * Rotates the ship clockwise if it is not within proximity of either another Battleship or the world boundary.
     * @param shipLocations the locations of all other placed ships in the Player's setup world
     */ 
    public void rotateLeft(ArrayList<Location> shipLocations)
    {
    	// checks if row or length is greater than given grid size
        int shipRowLength = completeShip.get(0).getLocation().getRow() + (size - 1); 
        int shipColLength = completeShip.get(0).getLocation().getCol() - (size - 1); // changed for left rotation
        
        // prevents horizontal-to-vertical rotation on top of another battleship
        for (int i = 0; i < shipLocations.size(); i++)
        {
            if (horizontal && shipRowLength >= shipLocations.get(i).getRow() &&
            	completeShip.get(0).getLocation().getCol() == shipLocations.get(i).getCol() && 
                completeShip.get(0).getLocation().getRow() < shipLocations.get(i).getRow())
            {
                rotateErrorBat = true;
                return;
            }
        }
        
        // prevents vertical-to-horizontal rotation on top of another battleship
        for (int i = 0; i < shipLocations.size(); i++)
        {
            if (!horizontal && shipColLength <= shipLocations.get(i).getCol() &&
            	completeShip.get(0).getLocation().getRow() == shipLocations.get(i).getRow() && 
                completeShip.get(0).getLocation().getCol() > shipLocations.get(i).getCol())
            {
                rotateErrorBat = true;
                return;
            }
        }
        
        // handles rotate method differently if ship is horizontal or vertical
        if (horizontal && shipRowLength <= 10 && !rotRight)
        {
            for (int i = 1; i < completeShip.size(); i++)
            {
                int newRowLoc = completeShip.get(i).getLocation().getRow() + i;
                int newColLoc = completeShip.get(i).getLocation().getCol() + i;
                completeShip.get(i).moveTo(new Location(newRowLoc, newColLoc));
            }
            
            imageRotator(false);
            horizontal = false;
            rotLeft = false;
        }
        else if (!horizontal && shipColLength >= 0)
        {
            for (int i = 1; i < completeShip.size(); i++)
            {
                int newRowLoc = completeShip.get(i).getLocation().getRow() - i;
                int newColLoc = completeShip.get(i).getLocation().getCol() - i;
                completeShip.get(i).moveTo(new Location(newRowLoc, newColLoc));
            }
            
            imageRotator(false);
            horizontal = true;
            rotLeft = true;
        }
        else
        {
        	if (rotRight)
        	{
        		rotationError = true;
        	}
        	else
        	{
        		rotateErrorBound = true;
        	}
        }
    }
    
    /**
     * Rotates the ship counter clockwise if it is not within proximity of either another Battleship or the world boundary.
     * @param shipLocations the locations of all other placed ships in the Player's setup world
     */ 
    public void rotateRight(ArrayList<Location> shipLocations)
    {
    	// checks if row or length is greater than given grid size
        int shipRowLength = completeShip.get(0).getLocation().getRow() + (size - 1);
        int shipColLength = completeShip.get(0).getLocation().getCol() + (size - 1);
        
        // prevents horizontal-to-vertical rotation on top of another battleship
        for (int i = 0; i < shipLocations.size(); i++)
        {
            if (horizontal && completeShip.get(0).getLocation().getCol() == shipLocations.get(i).getCol() && 
                completeShip.get(0).getLocation().getRow() < shipLocations.get(i).getRow() && 
                shipRowLength >= shipLocations.get(i).getRow())
            {
                rotateErrorBat = true;
                return;
            }
        }
        
        // prevents vertical-to-horizontal rotation on top of another battleship
        for (int i = 0; i < shipLocations.size(); i++)
        {
            if (!horizontal && completeShip.get(0).getLocation().getRow() == shipLocations.get(i).getRow() && 
                completeShip.get(0).getLocation().getCol() < shipLocations.get(i).getCol() && 
                shipColLength >= shipLocations.get(i).getCol())
            {
                rotateErrorBat = true;
                return;
            }
        }
        
        // handles rotate method differently if ship is horizontal or vertical
        if (horizontal && shipRowLength <= 10 && !rotLeft)
        {
            for (int i = 1; i < completeShip.size(); i++)
            {
                int newRowLoc = completeShip.get(i).getLocation().getRow() + i;
                int newColLoc = completeShip.get(i).getLocation().getCol() - i;
                completeShip.get(i).moveTo(new Location(newRowLoc, newColLoc));
            }
            
            imageRotator(true);
            rotRight = false;
            horizontal = false;
        }
        else if (!horizontal && shipColLength <= 10)
        {
            for (int i = 1; i < completeShip.size(); i++)
            {
                int newRowLoc = completeShip.get(i).getLocation().getRow() - i;
                int newColLoc = completeShip.get(i).getLocation().getCol() + i;
                completeShip.get(i).moveTo(new Location(newRowLoc, newColLoc));
            }
            
            imageRotator(true);
            rotRight = true;
            horizontal = true;
        }
        else
        {
        	if (rotLeft)
        	{
        		rotationError = true;
        	}
        	else
        	{
        		rotateErrorBound = true;
        	}
        }
    }
    
    /**
     * Rotates the BattleshipPart images.
     * @param rotRight true if rotating right, false if rotating left
     */
    public void imageRotator(boolean rotRight)
    {
        int direction;
        
        // in this case, think of horizontal as vertical (ship rotating from horizontal to vertical)
        if (horizontal)
        {
            direction = 0;
        }
        else if (rotRight)
        {
            direction = 270;
        }
        else
        {
        	direction = 90;
        }

        for (int i = 0; i < completeShip.size(); i++)
        {
            completeShip.get(i).setDirection(direction);
        }
    }
}
