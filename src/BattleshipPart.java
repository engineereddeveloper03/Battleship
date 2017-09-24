import gridworld.actor.Actor;

import gridworld.grid.Grid;
import gridworld.grid.Location;

/**
 * Contains the information on a single section of the battleship.
 * 
 * @author Phillip Sturtevant
 * @version 05/24/2017
 */
public class BattleshipPart extends Actor
{
    private String battleshipSuffix;
    
    /**
     * Constructs a battleship part with no color and the default image.
     */
    public BattleshipPart()
    {
        battleshipSuffix = "";
        setColor(null);
    }
    
    /**
     * Overrides the act method in the Actor class to do nothing.
     */
    public void act() {};
    
    /**
     * Sets up the appropriate Battleship part image
     * @param segmentTypeInput the type of image in which to set the Battleship part
     */ 
    public void setImage(int segmentTypeInput)
    {
        switch (segmentTypeInput)
        {
            case 0:  battleshipSuffix = "top";
                     break;
            case 1:  battleshipSuffix = "middle";
                     break;
            case 2:  battleshipSuffix = "bottom";
                     break;
            case 3:  battleshipSuffix = "sunkT";
                     break;
            case 4:  battleshipSuffix = "sunkM";
                     break;
            case 5:  battleshipSuffix = "sunkB";
                     break;
            case 6:  battleshipSuffix = "hit";
                     break;
            case 7:  battleshipSuffix = "miss";
                     break;
            default: battleshipSuffix = "";
                     break;
        }
    }

    /**
     * Changes how GridWorld reads file names when loading images into the game
     * @return the String name GridWorld will look for when loading an image
     */ 
    public String getImageSuffix()
    {
        return "_" + battleshipSuffix;
    }
    
    /**
     * Moves the Battleship part on the grid in a given direction
     * @param direction the direction to move the Battleship part
     */ 
    public void move(int direction)
    {
        Grid<Actor> gr = getGrid();
        if (gr == null)
            return;
        Location loc = getLocation();
        Location next = loc.getAdjacentLocation(direction);
        if (gr.isValid(next))
            moveTo(next);
    }
}
