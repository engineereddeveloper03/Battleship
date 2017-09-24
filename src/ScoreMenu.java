import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Creates a menu to update the score of the players as the game progresses
 * 
 * @author Phillip Sturtevant
 * @version 09/14/2017
 */
public class ScoreMenu extends JFrame
{
	/**
	 * ScoreMenu serialID
	 */
	private static final long serialVersionUID = -1650506338601000336L;
	private JLabel titleLabel;
	private JLabel player1Label;
	private JLabel player1Score;
	private JLabel player2Label;
	private JLabel player2Score;
	
	// screen resolution variables
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int scoreWidth = 300;
	int scoreHeight = 175;
	
	/**
	 * Constructs a score menu to display current score between players during the play session.
	 */
	public ScoreMenu()
	{
		this.setTitle("Battleship");
		this.setResizable(false);
		this.setSize(400,350);
		
		// Changes icon image.
		URL iconURL = getClass().getResource("BattleshipIcon.png");
		// iconURL is null when not found
		ImageIcon icon = new ImageIcon(iconURL);
		this.setIconImage(icon.getImage());
		
		//pane with null layout
		JPanel contentPane = new JPanel(null);
		contentPane.setPreferredSize(new Dimension(scoreWidth, scoreHeight));
		contentPane.setBackground(new Color(57,191,239));
		
		// Labels
		titleLabel = new JLabel("Battleship Scores");
		titleLabel.setBounds(20,20,260,35);
		titleLabel.setBackground(new Color(214,217,223));
		titleLabel.setForeground(new Color(0,0,0));
		titleLabel.setEnabled(true);
		titleLabel.setFont(new Font("SansSerif",1,30));
		titleLabel.setVisible(true);

		player1Label = new JLabel("Player 1: ");
		player1Label.setBounds(5,75,90,35);
		player1Label.setBackground(new Color(214,217,223));
		player1Label.setForeground(new Color(0,0,0));
		player1Label.setEnabled(true);
		player1Label.setFont(new Font("SansSerif",1,16));
		player1Label.setVisible(true);

		player1Score = new JLabel("0");
		player1Score.setBounds(100,75,90,35);
		player1Score.setBackground(new Color(214,217,223));
		player1Score.setForeground(new Color(0,0,0));
		player1Score.setEnabled(true);
		player1Score.setFont(new Font("SansSerif",1,16));
		player1Score.setVisible(true);

		player2Label = new JLabel("Player 2:");
		player2Label.setBounds(5,125,175,35);
		player2Label.setBackground(new Color(214,217,223));
		player2Label.setForeground(new Color(0,0,0));
		player2Label.setEnabled(true);
		player2Label.setFont(new Font("SansSerif",1,16));
		player2Label.setVisible(true);
		
		player2Score = new JLabel("0");
		player2Score.setBounds(100,125,175,35);
		player2Score.setBackground(new Color(214,217,223));
		player2Score.setForeground(new Color(0,0,0));
		player2Score.setEnabled(true);
		player2Score.setFont(new Font("SansSerif",1,16));
		player2Score.setVisible(true);
		
		//adding components to contentPane panel
		contentPane.add(titleLabel);
		contentPane.add(player1Label);
		contentPane.add(player1Score);
		contentPane.add(player2Label);
		contentPane.add(player2Score);
		
		//adding panel to JFrame and setting of window position and close operation
		this.add(contentPane);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation((screenSize.width/2) - (scoreWidth/2), 25);
		this.pack();
		this.setVisible(false);
	}
	
	/**
	 * Changes player display name depending on game type chosen.
	 * @param twoPlayer decides whether the second player is a player or computer
	 */
	
	public void playerDisplay(boolean twoPlayer)
	{
		if (!twoPlayer)
		{
			player2Label.setText("Computer: ");
		}
	}
	
	/**
	 * Updates the score on the score menu
	 * @param playerNum the player whose score needs updating
	 * @param score the current score to place in the score menu
	 */
	public void updateScoreMenu(int score1, int score2)
	{
			player1Score.setText(String.valueOf(score1));
			player2Score.setText(String.valueOf(score2));
	}
	
	/**
	 * Closes the window when user exits/restarts the game.
	 */
	public void closeWindow()
	{
		this.dispose();
	}
}
