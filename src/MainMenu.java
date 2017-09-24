/*
*Text generated by Simple GUI Extension for BlueJ
*/
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;

/**
 * A basic menu to setup the Battleship game.
 * 
 * @author Phillip Sturtevant
 * @version 09/14/2017
 */
public class MainMenu extends JFrame
{
	/**
	 * MainMenu serialID
	 */
	private static final long serialVersionUID = -3887518614427938219L;
	private JButton startButton;
	private JLabel titleLabel;
	private JLabel gameTypeLabel;
	private JLabel playerLabel;
	private JLabel shipNumberLabel;
	private JRadioButton twoPlayerRButton;
	private JRadioButton computerRButton;
	private JRadioButton timeLimitGameRButton;
	private JRadioButton classicGameRButton;
	private JRadioButton moveTargetGameRButton;
	private JRadioButton landGameRButton;
	private JRadioButton threeShipRButton;
	private JRadioButton fourShipRButton;
	private JRadioButton fiveShipRButton;
	
	// initializing to current selected options in menu
	private boolean twoPlayers = true;
	private int gameType = 0;
	private int numOfShips = 3;
	
	// initializing game object to start game when ready
	Game game = new Game(this);
	

	/**
	 * Constructs a main menu for user to select game types, players and Battleship quantity.
	 */
	public MainMenu()
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
		contentPane.setPreferredSize(new Dimension(400,350));
		contentPane.setBackground(new Color(57,191,239));
		
		// Start Button
		startButton = new JButton("Start Game!");
		startButton.setBounds(150,298,100,35);
		startButton.setBackground(new Color(214,217,223));
		startButton.setForeground(new Color(0,0,0));
		startButton.setEnabled(true);
		startButton.setFont(new Font("sansserif",0,12));
		startButton.setVisible(true);
		startButton.addActionListener(new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent event) 
		    {
		    	try {
					game.setupGame(1);
				} catch (IOException e) {
					e.printStackTrace();
				}
		        dispose();
		    }
		});
		
		// Labels
		titleLabel = new JLabel("Battleship");
		titleLabel.setBounds(125,20,150,35);
		titleLabel.setBackground(new Color(214,217,223));
		titleLabel.setForeground(new Color(0,0,0));
		titleLabel.setEnabled(true);
		titleLabel.setFont(new Font("SansSerif",1,30));
		titleLabel.setVisible(true);

		gameTypeLabel = new JLabel();
		gameTypeLabel.setBounds(5,144,90,35);
		gameTypeLabel.setBackground(new Color(214,217,223));
		gameTypeLabel.setForeground(new Color(0,0,0));
		gameTypeLabel.setEnabled(true);
		gameTypeLabel.setFont(new Font("SansSerif",1,16));
		gameTypeLabel.setText("Game Type");
		gameTypeLabel.setVisible(true);

		playerLabel = new JLabel("Players");
		playerLabel.setBounds(5,62,90,35);
		playerLabel.setBackground(new Color(214,217,223));
		playerLabel.setForeground(new Color(0,0,0));
		playerLabel.setEnabled(true);
		playerLabel.setFont(new Font("SansSerif",1,16));
		playerLabel.setVisible(true);

		shipNumberLabel = new JLabel("Number of Battleships");
		shipNumberLabel.setBounds(5,218,175,35);
		shipNumberLabel.setBackground(new Color(214,217,223));
		shipNumberLabel.setForeground(new Color(0,0,0));
		shipNumberLabel.setEnabled(true);
		shipNumberLabel.setFont(new Font("SansSerif",1,16));
		shipNumberLabel.setVisible(true);
		
		// Player Radio Buttons	
		twoPlayerRButton = new JRadioButton("2-Player");
		twoPlayerRButton.setSelected(true);
		twoPlayerRButton.setBounds(5,100,70,35);
		twoPlayerRButton.setOpaque(false);
		twoPlayerRButton.setForeground(new Color(0,0,0));
		twoPlayerRButton.setEnabled(true);
		twoPlayerRButton.setFont(new Font("sansserif",0,12));
		twoPlayerRButton.setVisible(true);
		twoPlayerRButton.addActionListener(new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent event) 
		    {
		        twoPlayers = true;
		    }
		});

		computerRButton = new JRadioButton("Computer");
		computerRButton.setBounds(85,100,80,35);
		computerRButton.setOpaque(false);
		computerRButton.setForeground(new Color(0,0,0));
		computerRButton.setEnabled(true);
		computerRButton.setFont(new Font("sansserif",0,12));
		computerRButton.setVisible(true);
		computerRButton.addActionListener(new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent event) 
		    {
		        twoPlayers = false;
		    }
		});
		
		// Grouping buttons so only one is selected at a time
		ButtonGroup playerGroup = new ButtonGroup();
		playerGroup.add(twoPlayerRButton);
		playerGroup.add(computerRButton);
		
		// Game Type Radio Buttons
		classicGameRButton = new JRadioButton("Classic");
		classicGameRButton.setSelected(true);
		classicGameRButton.setBounds(3,180,70,35);
		classicGameRButton.setOpaque(false);
		classicGameRButton.setForeground(new Color(0,0,0));
		classicGameRButton.setEnabled(true);
		classicGameRButton.setFont(new Font("sansserif",0,12));
		classicGameRButton.setVisible(true);
		classicGameRButton.addActionListener(new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent event) 
		    {
		        gameType = 0;
		    }
		});

		timeLimitGameRButton = new JRadioButton("Time Limit");
		timeLimitGameRButton.setBounds(85,180,90,35);
		timeLimitGameRButton.setOpaque(false);
		timeLimitGameRButton.setForeground(new Color(0,0,0));
		timeLimitGameRButton.setEnabled(false);
		timeLimitGameRButton.setFont(new Font("sansserif",0,12));
		timeLimitGameRButton.setVisible(true);
		timeLimitGameRButton.addActionListener(new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent event) 
		    {
		        gameType = 1;
		    }
		});
		
		moveTargetGameRButton = new JRadioButton("Moving Target");
		moveTargetGameRButton.setBounds(185,180,110,35);
		moveTargetGameRButton.setOpaque(false);
		moveTargetGameRButton.setForeground(new Color(0,0,0));
		moveTargetGameRButton.setEnabled(false);
		moveTargetGameRButton.setFont(new Font("sansserif",0,12));
		moveTargetGameRButton.setVisible(true);
		moveTargetGameRButton.addActionListener(new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent event) 
		    {
		        gameType = 2;
		    }
		});

		landGameRButton = new JRadioButton("Land Mass");
		landGameRButton.setBounds(302,180,90,35);
		landGameRButton.setOpaque(false);
		landGameRButton.setForeground(new Color(0,0,0));
		landGameRButton.setEnabled(false);
		landGameRButton.setFont(new Font("sansserif",0,12));
		landGameRButton.setVisible(true);
		landGameRButton.addActionListener(new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent event) 
		    {
		        gameType = 3;
		    }
		});
		
		// Grouping buttons so only one is selected at a time
		ButtonGroup gameTypeGroup = new ButtonGroup();
		gameTypeGroup.add(classicGameRButton);
		gameTypeGroup.add(timeLimitGameRButton);
		gameTypeGroup.add(moveTargetGameRButton);
		gameTypeGroup.add(landGameRButton);
		
		// Number of ships radio buttons
		threeShipRButton = new JRadioButton("3");
		threeShipRButton.setSelected(true);
		threeShipRButton.setBounds(5,250,35,35);
		threeShipRButton.setOpaque(false);
		threeShipRButton.setForeground(new Color(0,0,0));
		threeShipRButton.setEnabled(true);
		threeShipRButton.setFont(new Font("sansserif",0,12));
		threeShipRButton.setVisible(true);
		threeShipRButton.addActionListener(new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent event) 
		    {
		        numOfShips = 3;
		    }
		});

		fourShipRButton = new JRadioButton("4");
		fourShipRButton.setBounds(63,250,35,35);
		fourShipRButton.setOpaque(false);
		fourShipRButton.setForeground(new Color(0,0,0));
		fourShipRButton.setEnabled(true);
		fourShipRButton.setFont(new Font("sansserif",0,12));
		fourShipRButton.setVisible(true);
		fourShipRButton.addActionListener(new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent event) 
		    {
		        numOfShips = 4;
		    }
		});

		fiveShipRButton = new JRadioButton("5");
		fiveShipRButton.setBounds(120,250,35,35);
		fiveShipRButton.setOpaque(false);
		fiveShipRButton.setForeground(new Color(0,0,0));
		fiveShipRButton.setEnabled(true);
		fiveShipRButton.setFont(new Font("sansserif",0,12));
		fiveShipRButton.setVisible(true);
		fiveShipRButton.addActionListener(new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent event) 
		    {
		        numOfShips = 5;
		    }
		});
		
		// Grouping buttons so only one is selected at a time
		ButtonGroup shipNumGroup = new ButtonGroup();
		shipNumGroup.add(threeShipRButton);
		shipNumGroup.add(fourShipRButton);
		shipNumGroup.add(fiveShipRButton);
		
		//adding components to contentPane panel
		contentPane.add(startButton);
		contentPane.add(titleLabel);
		contentPane.add(gameTypeLabel);
		contentPane.add(playerLabel);
		contentPane.add(shipNumberLabel);
		contentPane.add(twoPlayerRButton);
		contentPane.add(computerRButton);
		contentPane.add(timeLimitGameRButton);
		contentPane.add(classicGameRButton);
		contentPane.add(moveTargetGameRButton);
		contentPane.add(landGameRButton);
		contentPane.add(threeShipRButton);
		contentPane.add(fourShipRButton);
		contentPane.add(fiveShipRButton);

		//adding panel to JFrame and setting of window position and close operation
		this.add(contentPane);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.pack();
	}
	/**
	 * Gets whether the player is against another player or the computer
	 * @return true if 2-Player game, false if Player VS Computer game
	 */
	public boolean getTwoPlayers()
	{
	    return twoPlayers;
	}
	
	/**
	 * Gets the type of game selected in the main menu
	 * @return the integer value of the game type selected in the main menu
	 */
	public int getGameType()
	{
	    return gameType;
	}
	
	/**
	 * Gets the number of ships selected in the main menu
	 * @return the number of ships selected in the main menu
	 */
	public int getNumOfShips()
	{
	    return numOfShips;
	    
	}
}