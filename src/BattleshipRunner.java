import java.io.FileNotFoundException;

/** 
 * This class runs the the classic game of Battleship.
 * 
 * @author Phillip Sturtevant
 * @version 09/14/2017
 */
public class BattleshipRunner
{
	static MainMenu menu = new MainMenu();
	
    /**
     * Main method to run the Battleship game.
     * @param args Common main string argument.
     */
    public static void main(String[] args) 
    {
    	menu.setVisible(true);
    }
}
