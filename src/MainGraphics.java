import java.awt.EventQueue;
import java.awt.Transparency;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.CardLayout;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.border.BevelBorder;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import javax.swing.JSeparator;
import java.awt.SystemColor;
import javax.swing.JDesktopPane;
import javax.swing.JProgressBar;
import javax.swing.JEditorPane;
import java.awt.Panel;
import java.awt.RenderingHints;

public class MainGraphics {
	
	private JFrame frame;
	private JPanel panel;
	private JSlider slider;
	private JButton btnResume;
	private JPanel worldPanel;
	private JPanel tilePanel;
	private boolean stop = false;
	private boolean resume = false;
	private JLabel yearsLabel;
	private JLabel daysLabel;
	private JLabel humidityLabel;
	private JLabel tempLabel;
	private JLabel precipLabel;
	private JLabel seasonLabel;
	private JLabel weatherLabel;
	private JLabel heatNum;
	private JLabel typeLabel;
	private JLabel tilePrecipLabel;
	private JLabel waterConLabel;
	private JLabel goldLabel;
	private JLabel deerLabel;
	private JLabel foxLabel;
	private JProgressBar progressBar;
	private JLabel thermoLabel;
	private JLabel numThermo;
	private JLabel weatherVisual;
	private JLabel weatherVisualLabel;
	private JLabel totalDeer;
	private JLabel totalFox;
	private JButton addDeer;
	private JButton addFox;
	private JTextPane txtpnWelcomeToTerrarium;
	private JButton btnInstructions;
	private JPanel instructionsPanel;
	private JButton addHeatBtn;
	//private String music = "terrarium.wav";
	private PrecipVisual precipVisual;
	private PrecipBar precipBar;
	private JLabel precipP;
	private int recentR;
	private int recentC;
	private JLabel numPrecip;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// opens program
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// tests if window can open, if not then nothing will occur
				try {
					MainGraphics window = new MainGraphics();
					window.frame.setVisible(true);					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws LineUnavailableException 
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 */
	public MainGraphics() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		// creates specific graphics
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 * @throws LineUnavailableException 
	 */
	private void initialize() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		
		//playMusic();
		
		// frame to hold everything
		frame = new JFrame();
		frame.setBounds(100, 100, 837, 670);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnStart = new JButton("START");
		btnStart.setFont(new Font("Source Code Pro Semibold", Font.PLAIN, 15));
		btnStart.setBounds(46, 568, 93, 32);
		frame.getContentPane().add(btnStart);
		btnStart.addActionListener((ActionListener) new StartPlotting());
		
		JButton btnStop = new JButton("STOP");
		btnStop.setFont(new Font("Source Code Pro Semibold", Font.PLAIN, 15));
		btnStop.setBounds(254, 568, 89, 32);
		frame.getContentPane().add(btnStop);
		btnStop.addActionListener((ActionListener) new StopPlotting());
		
		btnResume = new JButton("Pause");
		btnResume.setFont(new Font("Source Code Pro Semibold", Font.PLAIN, 15));
		btnResume.setBounds(149, 568, 95, 32);
		frame.getContentPane().add(btnResume);
		btnResume.addActionListener((ActionListener) new ResumePlotting());
		
		JButton restartButton = new JButton("");
		restartButton.setIcon(new ImageIcon(MainGraphics.class.getResource("/com/sun/javafx/scene/web/skin/Redo_16x16_JFX.png")));
		restartButton.setFont(new Font("Dialog", Font.PLAIN, 15));
		restartButton.setBounds(353, 568, 46, 32);
		frame.getContentPane().add(restartButton);
		restartButton.addActionListener((ActionListener) new ResetPlotting());
		
		JLabel lblTest = new JLabel("Terrarium World");
		lblTest.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 30));
		lblTest.setBounds(155, 11, 278, 26);
		frame.getContentPane().add(lblTest);
		
		// world map
		panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, null, null, null));
		panel.setBackground(Color.WHITE);
		panel.setBounds(46, 48, 500, 500);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		// speed for day cycle
		slider = new JSlider(10, 2000, 1000);
		slider.setBounds(421, 591, 125, 15);
		frame.getContentPane().add(slider);
		
		JLabel speedLabel = new JLabel("World Cycle Speed");
		speedLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		speedLabel.setBounds(421, 566, 125, 14);
		frame.getContentPane().add(speedLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBackground(SystemColor.desktop);
		separator.setBounds(583, 48, -16, 532);
		frame.getContentPane().add(separator);
		
		JLabel controlPanelLabel = new JLabel("Control Panel");
		controlPanelLabel.setHorizontalAlignment(SwingConstants.CENTER);
		controlPanelLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 25));
		controlPanelLabel.setBounds(595, 61, 171, 32);
		frame.getContentPane().add(controlPanelLabel);
		
		tilePanel = new JPanel();
		tilePanel.setBounds(573, 133, 225, 459);
		frame.getContentPane().add(tilePanel);
		tilePanel.setLayout(null);
		tilePanel.setBackground(Color.WHITE);
		tilePanel.setVisible(false);
		
		heatNum = new JLabel();
		heatNum.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		heatNum.setBounds(10, 11, 196, 19);
		tilePanel.add(heatNum);
		
		typeLabel = new JLabel();
		typeLabel.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		typeLabel.setBounds(10, 39, 196, 19);
		tilePanel.add(typeLabel);
		
		tilePrecipLabel = new JLabel();
		tilePrecipLabel.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		tilePrecipLabel.setBounds(10, 69, 196, 19);
		tilePanel.add(tilePrecipLabel);
		
		waterConLabel = new JLabel();
		waterConLabel.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		waterConLabel.setBounds(10, 99, 196, 19);
		tilePanel.add(waterConLabel);
		
		goldLabel = new JLabel();
		goldLabel.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		goldLabel.setBounds(10, 129, 196, 19);
		tilePanel.add(goldLabel);
		
		deerLabel = new JLabel();
		deerLabel.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		deerLabel.setBounds(10, 159, 196, 19);
		tilePanel.add(deerLabel);
		
		foxLabel = new JLabel();
		foxLabel.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		foxLabel.setBounds(10, 189, 196, 19);
		tilePanel.add(foxLabel);
		
		addDeer = new JButton("Add Deer");
		addDeer.setBounds(10, 226, 102, 23);
		tilePanel.add(addDeer);
		addDeer.setVisible(false);
		addDeer.setFont(new Font("Roboto", Font.PLAIN, 15));
		
		addFox = new JButton("Add Fox");
		addFox.setBounds(122, 226, 93, 23);
		tilePanel.add(addFox);
		addFox.setVisible(false);
		addFox.setFont(new Font("Roboto", Font.PLAIN, 15));
	
		worldPanel = new JPanel();
		worldPanel.setBackground(SystemColor.window);
		worldPanel.setBounds(573, 133, 225, 459);
		frame.getContentPane().add(worldPanel);
		worldPanel.setLayout(null);
		worldPanel.setVisible(false);
		
		yearsLabel = new JLabel();
		yearsLabel.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		yearsLabel.setBounds(10, 11, 196, 19);
		worldPanel.add(yearsLabel);
		
		daysLabel = new JLabel();
		daysLabel.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		daysLabel.setBounds(10, 39, 196, 19);
		worldPanel.add(daysLabel);
		
		humidityLabel = new JLabel();
		humidityLabel.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		humidityLabel.setBounds(10, 69, 196, 19);
		worldPanel.add(humidityLabel);
		
		tempLabel = new JLabel();
		tempLabel.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		tempLabel.setBounds(10, 99, 196, 19);
		worldPanel.add(tempLabel);
		
		precipLabel = new JLabel();
		precipLabel.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		precipLabel.setBounds(10, 129, 196, 19);
		worldPanel.add(precipLabel);
		
		seasonLabel = new JLabel();
		seasonLabel.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		seasonLabel.setBounds(10, 159, 196, 19);
		worldPanel.add(seasonLabel);
		
		weatherLabel = new JLabel();
		weatherLabel.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		weatherLabel.setBounds(10, 189, 196, 19);
		worldPanel.add(weatherLabel);
		
		totalDeer = new JLabel();
		totalDeer.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		totalDeer.setBounds(10, 219, 196, 19);
		worldPanel.add(totalDeer);
		
		totalFox = new JLabel();
		totalFox.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		totalFox.setBounds(10, 249, 196, 19);
		worldPanel.add(totalFox);
		
		numThermo = new JLabel();
		numThermo.setHorizontalAlignment(SwingConstants.CENTER);
		numThermo.setFont(new Font("Telugu Sangam MN", Font.BOLD, 11));
		numThermo.setBounds(18, 353, 26, 19);
		worldPanel.add(numThermo);
		
		numPrecip = new JLabel();
		numPrecip.setHorizontalAlignment(SwingConstants.CENTER);
		numPrecip.setFont(new Font("Telugu Sangam MN", Font.BOLD, 11));
		numPrecip.setForeground(Color.WHITE);
		numPrecip.setBounds(185, 353, 26, 19);
		worldPanel.add(numPrecip);
		
		// thermometer 
		progressBar = new JProgressBar();
		progressBar.setBackground(new Color(255, 228, 225));
		progressBar.setFont(new Font("Oswald", Font.BOLD, 16));
		progressBar.setForeground(new Color(220, 20, 60));
		progressBar.setOrientation(SwingConstants.VERTICAL);
		progressBar.setBounds(18, 291, 26, 146);
		progressBar.setVisible(false);
		worldPanel.add(progressBar);
		
		precipBar = new PrecipBar(new Color(25, 38, 155));
		precipBar.setVisible(false);
		worldPanel.add(precipBar);
		
		// precipitation
		precipVisual = new PrecipVisual(Color.WHITE, new Color(25, 38, 155));
		precipVisual.setBounds(185, 291, 26, 146);
		precipVisual.setVisible(false);
		worldPanel.add(precipVisual);		
		
		precipP = new JLabel();
		precipP.setHorizontalAlignment(SwingConstants.CENTER);
		precipP.setText("P");
		precipP.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		precipP.setBounds(188, 273, 19, 19);
		precipP.setVisible(false);
		worldPanel.add(precipP);
		
		thermoLabel = new JLabel();
		thermoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		thermoLabel.setText("F");
		thermoLabel.setFont(new Font("Telugu Sangam MN", Font.BOLD, 15));
		thermoLabel.setBounds(22, 273, 19, 19);
		thermoLabel.setVisible(false);
		worldPanel.add(thermoLabel);
		
		weatherVisual = new JLabel("");
		weatherVisual.setHorizontalAlignment(SwingConstants.CENTER);
		weatherVisual.setBounds(58, 339, 114, 97);
		worldPanel.add(weatherVisual);
		
		weatherVisualLabel = new JLabel("");
		weatherVisualLabel.setHorizontalAlignment(SwingConstants.CENTER);
		weatherVisualLabel.setFont(new Font("Verdana", Font.BOLD, 13));
		weatherVisualLabel.setBounds(63, 315, 103, 19);
		worldPanel.add(weatherVisualLabel);
		
		addHeatBtn = new JButton("");
		addHeatBtn.setText("Add Fire");
		addHeatBtn.setBounds(122, 266, 93, 23);
		addHeatBtn.setVisible(false);
		addHeatBtn.setFont(new Font("Roboto", Font.PLAIN, 15));
		tilePanel.add(addHeatBtn);
		
		JButton worldSelect = new JButton("World");
		worldSelect.setFont(new Font("Segoe UI", Font.BOLD, 15));
		worldSelect.setBounds(572, 104, 119, 32);
		frame.getContentPane().add(worldSelect);
		worldSelect.addActionListener((ActionListener) new WorldPanel());
		
		JButton tileSelect = new JButton("Tile");
		tileSelect.setFont(new Font("Segoe UI", Font.BOLD, 15));
		tileSelect.setBounds(688, 104, 111, 32);
		frame.getContentPane().add(tileSelect);
		tileSelect.addActionListener((ActionListener) new TilePanel());
		
		btnInstructions = new JButton("Instructions");
		btnInstructions.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		btnInstructions.setBounds(640, 597, 95, 23);
		frame.getContentPane().add(btnInstructions);
		btnInstructions.addActionListener((ActionListener) new InstructionPanel());
		
		instructionsPanel = new JPanel();
		instructionsPanel.setBounds(573, 133, 225, 459);
		frame.getContentPane().add(instructionsPanel);
		instructionsPanel.setLayout(null);
		
		txtpnWelcomeToTerrarium = new JTextPane();
		txtpnWelcomeToTerrarium.setFont(new Font("Lucida Console", Font.PLAIN, 12));
		txtpnWelcomeToTerrarium.setText("Welcome to Terrarium World!\r\n\r\nPress START to begin.\r\nPress PAUSE to suspend.\r\nPress STOP to end.\r\n\r\n\r\nSelect the \"Tile\" tab to view specfic tile information.\r\n\r\nPause the Simulation.\r\n\r\nThen select a Tile on the Map to view information.\r\n\r\nAdding Deer and Fox may only happen when simulation is pasued.\r\n\r\n\r\nUser may change how fast the world cycles through days with dial (Left = Fast, Right = Slow).\r\n\r\n\r\n\r\n\r\n");
		txtpnWelcomeToTerrarium.setBounds(10, 11, 205, 437);
		instructionsPanel.add(txtpnWelcomeToTerrarium);
		
		// checks for the most recent row and column and adds a fire, deer, or fox to that location
		addHeatBtn.addActionListener((ActionListener) new addHeat());
		addDeer.addActionListener((ActionListener) new addDeer());
		addFox.addActionListener((ActionListener) new addFox());
	}
	
	// adds a listener to the World Button
	class WorldPanel implements ActionListener{
		
		// method makes the world panel only visible
		public void actionPerformed(ActionEvent arg0) {
			tilePanel.setVisible(false);
			worldPanel.setVisible(true);
			instructionsPanel.setVisible(false);
		}
	}
	
	// adds a listener to the Tile Button
	class TilePanel implements ActionListener{

		// method makes the tile panel only visible
		public void actionPerformed(ActionEvent arg0) {
			tilePanel.setVisible(true);
			worldPanel.setVisible(false);
			instructionsPanel.setVisible(false);
		}
	}
	
	// adds a listener to the Instruction Button
	class InstructionPanel implements ActionListener{

		// method makes the instruction panel only visible
		public void actionPerformed(ActionEvent arg0) {
			tilePanel.setVisible(false);
			worldPanel.setVisible(false);
			instructionsPanel.setVisible(true);
		}
	}
	
	// adds a listener to the Stop Button
	class StopPlotting implements ActionListener{

		// ends the simulation
		public void actionPerformed(ActionEvent arg0) {
			stop = !stop;
		}
	}
	
	// restarting the map and island
	class ResetPlotting implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			World.startSimulation();
		}
	}
	
	// changes the text of the button and goes to the Thread.suspend
	class ResumePlotting implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			resume = !resume;
			if(btnResume.getText().equals("Pause")) {
				btnResume.setText("Resume");
			} else {
				btnResume.setText("Pause");
			}
		}
	}
	
	// makes everything visible on the world panel
	class StartPlotting implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			numThermo.setVisible(true);
			progressBar.setVisible(true);
			thermoLabel.setVisible(true);
			precipP.setVisible(true);
			precipVisual.setVisible(true);
			precipBar.setVisible(true);
			// starts plotting the tiles
			Plot p = new Plot();
			p.start();
		}
	}
	
	/*
	 * all methods will allow the tile to add any animals or fire to it
	 */
	
	class addDeer implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			World.getTile(recentR, recentC).addDeer(1);
		}
	}
	
	class addFox implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			World.getTile(recentR, recentC).addFox(1);
		}
	}
		
	class addHeat implements ActionListener{
		 
		public void actionPerformed(ActionEvent arg0) {
			World.getTile(recentR, recentC).setFireStatus((int)(Math.random() * 30));
		}
	}
	
	class Plot extends Thread{
		@SuppressWarnings("deprecation")
		public void run(){
			// starts the world map on a data scale
			World.startSimulation();
			// boolean will decide to stop the entire simulation
			while(!stop) {
				// decides if the simulation is paused or not
				if(!resume) {
					// refreshes the screen to reduce overload
					panel.removeAll();
					// cycles the data content
					World.cycle();
					for (int r = 0; r < World.getMapLength(); r++) {
						for (int c = 0; c < World.getMapLength(); c++) {
							// gets the color of the tile
							String color = World.getTile(r, c).getColor();
							// checks what color the tile is to make the correct tile
							if(World.getTile(r, c).getStatus().equals("fire")) {
								Color cl = new Color(229, 48, 11);
								Tile infoTile = World.getTile(r, c);
								TileGraphic tile = new TileGraphic(cl);
								// sets the bounds of the tile to fit the map
								tile.setBounds(r * 20, c * 20, 20, 20);
								tile.setLayout(null);
								// cycles and adds the animals visually to the tile
								addAnimals(infoTile.foxCount(), infoTile.deerCount(), infoTile, tile);
								// allows the user to select the tile and show information about the tile on the tile Panel
								tile.addMouseListener(new MouseListener(){
									public void mouseClicked(MouseEvent arg0) {
										BigDecimal heat = new BigDecimal(infoTile.getHeat() + "");
										BigDecimal roundHeat = heat.setScale(0, RoundingMode.DOWN);
										BigDecimal water = new BigDecimal(infoTile.getWaterConcentration() + "");
										BigDecimal roundWater = water.setScale(1, RoundingMode.DOWN);
										heatNum.setText("Heat:" + " " + roundHeat);
										typeLabel.setText("Type:" + " " + infoTile.getType());
										tilePrecipLabel.setText("Precipitation:" + " " + infoTile.getPrecipitation());
										waterConLabel.setText("Water Concentration: " + " " + roundWater);
										goldLabel.setText("Gold Number:" + " " + infoTile.getGoldilocksTime());
										foxLabel.setText("Fox:" + " " + infoTile.foxCount());
										deerLabel.setText("Deer:" + " " + infoTile.deerCount());
										addDeer.setVisible(true);
										addFox.setVisible(true);
										addHeatBtn.setVisible(true);
										recentR = infoTile.getR();
										recentC = infoTile.getC();
										tilePanel.repaint();
									}
		
									public void mouseEntered(MouseEvent arg0) {}
		
									public void mouseExited(MouseEvent arg0) {}
		
									public void mousePressed(MouseEvent arg0) {}
		
									public void mouseReleased(MouseEvent arg0) {}
								});
								panel.add(tile);
							} else if(World.getTile(r, c).getStatus().equals("snowy")) {
								Color cl = new Color(243, 247, 252);
								Tile infoTile = World.getTile(r, c);
								TileGraphic tile = new TileGraphic(cl);
								tile.setBounds(r * 20, c * 20, 20, 20);
								tile.setLayout(null);
								addAnimals(infoTile.foxCount(), infoTile.deerCount(), infoTile, tile);
								tile.addMouseListener(new MouseListener(){
									public void mouseClicked(MouseEvent arg0) {
										BigDecimal water = new BigDecimal(infoTile.getWaterConcentration() + "");
										BigDecimal roundWater = water.setScale(1, RoundingMode.DOWN);
										BigDecimal heat = new BigDecimal(infoTile.getHeat() + "");
										BigDecimal roundHeat = heat.setScale(0, RoundingMode.DOWN);
										heatNum.setText("Heat:" + " " + roundHeat);
										typeLabel.setText("Type:" + " " + infoTile.getType());
										tilePrecipLabel.setText("Precipitation:" + " " + infoTile.getPrecipitation());
										waterConLabel.setText("Water Concentration: " + " " + roundWater);
										goldLabel.setText("Gold Number:" + " " + infoTile.getGoldilocksTime());
										foxLabel.setText("Fox:" + " " + infoTile.foxCount());
										deerLabel.setText("Deer:" + " " + infoTile.deerCount());
										addDeer.setVisible(true);
										addFox.setVisible(true);
										addHeatBtn.setVisible(true);
										recentR = infoTile.getR();
										recentC = infoTile.getC();
										tilePanel.repaint();
									}
		
									public void mouseEntered(MouseEvent arg0) {}
		
									public void mouseExited(MouseEvent arg0) {}
		
									public void mousePressed(MouseEvent arg0) {}
		
									public void mouseReleased(MouseEvent arg0) {}
								});
								panel.add(tile);
							} else if(color.equals("tan")) {
								Color cl = new Color(117, 62, 0);
								Tile infoTile = World.getTile(r, c);
								TileGraphic tile = new TileGraphic(cl);
								tile.setBounds(r * 20, c * 20, 20, 20);
								tile.setLayout(null);
								addAnimals(infoTile.foxCount(), infoTile.deerCount(), infoTile, tile);
								tile.addMouseListener(new MouseListener(){
									public void mouseClicked(MouseEvent arg0) {
										BigDecimal heat = new BigDecimal(infoTile.getHeat() + "");
										BigDecimal roundHeat = heat.setScale(0, RoundingMode.DOWN);
										BigDecimal water = new BigDecimal(infoTile.getWaterConcentration() + "");
										BigDecimal roundWater = water.setScale(1, RoundingMode.DOWN);
										heatNum.setText("Heat:" + " " + roundHeat);
										typeLabel.setText("Type:" + " " + infoTile.getType());
										tilePrecipLabel.setText("Precipitation:" + " " + infoTile.getPrecipitation());
										waterConLabel.setText("Water Concentration: " + " " + roundWater);
										goldLabel.setText("Gold Number:" + " " + infoTile.getGoldilocksTime());
										foxLabel.setText("Fox:" + " " + infoTile.foxCount());
										deerLabel.setText("Deer:" + " " + infoTile.deerCount());
										addDeer.setVisible(true);
										addFox.setVisible(true);
										addHeatBtn.setVisible(true);
										recentR = infoTile.getR();
										recentC = infoTile.getC();
										tilePanel.repaint();
									}
		
									public void mouseEntered(MouseEvent arg0) {}
		
									public void mouseExited(MouseEvent arg0) {}
		
									public void mousePressed(MouseEvent arg0) {}
		
									public void mouseReleased(MouseEvent arg0) {}
								});
								panel.add(tile);
							} else if(color.equals("light green")){
								Color cl = new Color(132, 255, 107); 
								Tile infoTile = World.getTile(r, c);
								TileGraphic tile = new TileGraphic(cl);
								tile.setBounds(r * 20, c * 20, 20, 20);
								tile.setLayout(null);
								addAnimals(infoTile.foxCount(), infoTile.deerCount(), infoTile, tile);
								tile.addMouseListener(new MouseListener(){
									public void mouseClicked(MouseEvent e) {
										BigDecimal heat = new BigDecimal(infoTile.getHeat() + "");
										BigDecimal roundHeat = heat.setScale(0, RoundingMode.DOWN);
										BigDecimal water = new BigDecimal(infoTile.getWaterConcentration() + "");
										BigDecimal roundWater = water.setScale(1, RoundingMode.DOWN);
										heatNum.setText("Heat:" + " " + roundHeat);
										typeLabel.setText("Type:" + " " + infoTile.getType());
										tilePrecipLabel.setText("Precipitation:" + " " + infoTile.getPrecipitation());
										waterConLabel.setText("Water Concentration: " + " " + roundWater);
										goldLabel.setText("Gold Number:" + " " + infoTile.getGoldilocksTime());
										foxLabel.setText("Fox:" + " " + infoTile.foxCount());
										deerLabel.setText("Deer:" + " " + infoTile.deerCount());
										addDeer.setVisible(true);
										addFox.setVisible(true);
										addHeatBtn.setVisible(true);
										recentR = infoTile.getR();
										recentC = infoTile.getC();
										tilePanel.repaint();
									}
		
									public void mouseEntered(MouseEvent arg0) {}
		
									public void mouseExited(MouseEvent arg0) {}
		
									public void mousePressed(MouseEvent arg0) {}
		
									public void mouseReleased(MouseEvent arg0) {}
								});
								panel.add(tile);
							} else if(color.equals("green")) {
								Color cl = new Color(102, 216, 79);
								Tile infoTile = World.getTile(r, c);
								TileGraphic tile = new TileGraphic(cl);
								tile.setBounds(r * 20, c * 20, 20, 20);
								tile.setLayout(null);
								addAnimals(infoTile.foxCount(), infoTile.deerCount(), infoTile, tile);
								tile.addMouseListener(new MouseListener(){
									public void mouseClicked(MouseEvent arg0) {
										BigDecimal heat = new BigDecimal(infoTile.getHeat() + "");
										BigDecimal roundHeat = heat.setScale(0, RoundingMode.DOWN);
										BigDecimal water = new BigDecimal(infoTile.getWaterConcentration() + "");
										BigDecimal roundWater = water.setScale(1, RoundingMode.DOWN);
										heatNum.setText("Heat:" + " " + roundHeat);
										typeLabel.setText("Type:" + " " + infoTile.getType());
										tilePrecipLabel.setText("Precipitation:" + " " + infoTile.getPrecipitation());
										waterConLabel.setText("Water Concentration: " + " " + roundWater);
										goldLabel.setText("Gold Number:" + " " + infoTile.getGoldilocksTime());
										foxLabel.setText("Fox:" + " " + infoTile.foxCount());
										deerLabel.setText("Deer:" + " " + infoTile.deerCount());
										addDeer.setVisible(true);
										addFox.setVisible(true);
										addHeatBtn.setVisible(true);
										recentR = infoTile.getR();
										recentC = infoTile.getC();
										tilePanel.repaint();
									}
		
									public void mouseEntered(MouseEvent arg0) {}
		
									public void mouseExited(MouseEvent arg0) {}
		
									public void mousePressed(MouseEvent arg0) {}
		
									public void mouseReleased(MouseEvent arg0) {}
								});
								panel.add(tile);
							} else if(color.equals("dark green")) {
								Color cl = new Color(45, 158, 22);
								Tile infoTile = World.getTile(r, c);
								TileGraphic tile = new TileGraphic(cl);
								tile.setBounds(r * 20, c * 20, 20, 20);
								tile.setLayout(null);
								addAnimals(infoTile.foxCount(), infoTile.deerCount(), infoTile, tile);
								tile.addMouseListener(new MouseListener(){
									public void mouseClicked(MouseEvent arg0) {
										BigDecimal heat = new BigDecimal(infoTile.getHeat() + "");
										BigDecimal roundHeat = heat.setScale(0, RoundingMode.DOWN);
										BigDecimal water = new BigDecimal(infoTile.getWaterConcentration() + "");
										BigDecimal roundWater = water.setScale(1, RoundingMode.DOWN);
										heatNum.setText("Heat:" + " " + roundHeat);
										typeLabel.setText("Type:" + " " + infoTile.getType());
										tilePrecipLabel.setText("Precipitation:" + " " + infoTile.getPrecipitation());
										waterConLabel.setText("Water Concentration: " + " " + roundWater);
										goldLabel.setText("Gold Number:" + " " + infoTile.getGoldilocksTime());
										foxLabel.setText("Fox:" + " " + infoTile.foxCount());
										deerLabel.setText("Deer:" + " " + infoTile.deerCount());
										addDeer.setVisible(true);
										addFox.setVisible(true);
										addHeatBtn.setVisible(true);
										recentR = infoTile.getR();
										recentC = infoTile.getC();
										tilePanel.repaint();
									}
		
									public void mouseEntered(MouseEvent arg0) {}
		
									public void mouseExited(MouseEvent arg0) {}
		
									public void mousePressed(MouseEvent arg0) {}
		
									public void mouseReleased(MouseEvent arg0) {}
								});
								panel.add(tile);
							} else if(color.equals("blue")){
								Color cl = new Color(135, 206, 250);
								Tile infoTile = World.getTile(r, c);
								TileGraphic tile = new TileGraphic(cl);
								tile.setBounds(r * 20, c * 20, 20, 20);
								tile.addMouseListener(new MouseListener(){
									public void mouseClicked(MouseEvent arg0) {
										BigDecimal heat = new BigDecimal(infoTile.getHeat() + "");
										BigDecimal roundHeat = heat.setScale(0, RoundingMode.DOWN);
										heatNum.setText("Heat:" + " " + roundHeat);
										typeLabel.setText("Type:" + " " + infoTile.getType());
										tilePrecipLabel.setText("Precipitation:" + " " + infoTile.getPrecipitation());
										waterConLabel.setText("Water Concentration: " + " " + infoTile.getWaterConcentration());
										goldLabel.setText("Gold Number:" + " " + infoTile.getGoldilocksTime());
										foxLabel.setText("Fox:" + " " + infoTile.foxCount());
										deerLabel.setText("Deer:" + " " + infoTile.deerCount());
										tilePanel.repaint();
									}
									
									public void mouseEntered(MouseEvent arg0) {}
		
									public void mouseExited(MouseEvent arg0) {}
		
									public void mousePressed(MouseEvent arg0) {}
		
									public void mouseReleased(MouseEvent arg0) {}
								});
								panel.add(tile);
							}
						}
					}
					// rounds the decimals to a certain point
					BigDecimal hum = new BigDecimal(World.getHumidity() + "");
					BigDecimal roundHum = hum.setScale(2, RoundingMode.DOWN);
					// set of conditions selects what image to show for the weather
					if(World.getPrecipitation().equals("heavy snow")) {
						weatherVisualLabel.setText("Heavy Snow");
						weatherLabel.setText("Weather:" + " " + "Heavy Snow");
						weatherVisual.setIcon(new ImageIcon("Snow-1.png"));
					} else if(World.getPrecipitation().equals("heavy rain")) {
						weatherVisualLabel.setText("Heavy Rain");
						weatherLabel.setText("Weather:" + " " + "Heavy Rain");
						weatherVisual.setIcon(new ImageIcon("Rain.png"));
					} else if(World.getPrecipitation().equals("snow storm")) {
						weatherVisualLabel.setText("Snow Storm");
						weatherLabel.setText("Weather:" + " " + "Snow Storm");
						weatherVisual.setIcon(new ImageIcon("Snow.png"));
					} else if(World.getPrecipitation().equals("rain storm")) {
						weatherVisualLabel.setText("Rain Storm");
						weatherLabel.setText("Weather:" + " " + "Rain Storm");
						weatherVisual.setIcon(new ImageIcon("Heavy_Rain.png"));
					} else if(World.getPrecipitation().equals("flash flood")) {
						weatherVisualLabel.setText("Flash Flood");
						weatherLabel.setText("Weather:" + " " + "Flash Flood");
						weatherVisual.setIcon(new ImageIcon("Heavy_Rain.png"));
					} else if(World.getPrecipitation().equals("light rain")) {
						weatherVisualLabel.setText("Light Rain");
						weatherLabel.setText("Weather:" + " " + "Light Rain");
						weatherVisual.setIcon(new ImageIcon("LittleRain.png"));
					} else if(World.getPrecipitation().equals("light snow")) {
						weatherVisualLabel.setText("Light Snow");
						weatherLabel.setText("Weather:" + " " + "Light Snow");
						weatherVisual.setIcon(new ImageIcon("LightSnow.png"));
					} else if(World.getPrecipitation().equals("cloudy")) {
						weatherVisualLabel.setText("Cloudy");
						weatherLabel.setText("Weather:" + " " + "Cloudy");
						weatherVisual.setIcon(new ImageIcon("ClearSkys.png"));
					} else if(World.getPrecipitation().equals("clear skies")) {
						weatherVisualLabel.setText("Clear Skies");
						weatherLabel.setText("Weather:" + " " + "Clear Skies");
						weatherVisual.setIcon(new ImageIcon("Sunny.png"));
					}
					// changes the numbers for different days of the cycle
					progressBar.setValue((int) World.getTemperature());
					numThermo.setText((int)World.getTemperature() + " F");
					numPrecip.setText(World.getPrecipitationValue() +"");
					yearsLabel.setText("Years:" + " " + World.getRelativeYear());
					daysLabel.setText("Days:" + " " + World.getRelativeTime());
					humidityLabel.setText("Humidity:" + " " + roundHum);
					tempLabel.setText("Temperature:" + " " + (int)World.getTemperature() + " F");
					precipLabel.setText("Precipitation:" + " " + World.getPrecipitationValue());
					seasonLabel.setText("Season:" + " " + World.getSeason());
					totalDeer.setText("Total Deer: " + World.totalDeer());
					totalFox.setText("Total Fox: " + World.totalFox());
					// decides where the visual for the precipitation should be
					if(World.getPrecipitationValue() == 1) {
						precipBar.setBounds(185, 291, 26, (int)(146 * World.getPrecipitationValue()));
					} else if(World.getPrecipitationValue() == 0.75) {
						precipBar.setBounds(185, 291 + (int)(146 * World.getPrecipitationValue())/2 - 20, 26, (int)(146 * World.getPrecipitationValue()));
					} else if(World.getPrecipitationValue() == 0.5) {
						precipBar.setBounds(185, 291 + (int)(146 * World.getPrecipitationValue()), 26, (int)(146 * World.getPrecipitationValue()));
					} else if(World.getPrecipitationValue() == 0.25) {
						precipBar.setBounds(185, 291 + 2 * (int)(146 * World.getPrecipitationValue()) + 39, 26, (int)(146 * World.getPrecipitationValue()));
					} else if(World.getPrecipitationValue() == 0.0) {
						precipBar.setBounds(185, 291, 26, (int)(146 * World.getPrecipitationValue()));
					}
					// refreshes the graphics on the map
					panel.repaint();
					// refreshes the graphics on the panel
					worldPanel.repaint();
					try {
						// gives the thread a pausing millisecond value based off of the slider
						Thread.sleep(slider.getValue());
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					// suspends the thread hence the program until the boolean becomes true
					Thread resume = new Thread();
					resume.suspend();
				}
			}
		}
	}
	
	/*public void playMusic() throws IOException {
		try {
			// makes new music file
			FileInputStream music = new FileInputStream(new File(this.music));
			@SuppressWarnings("resource")
			AudioStream audios = new AudioStream(music);
			// gets the data and then plays the music
			AudioPlayer.player.start(audios);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}*/
	
	public void addAnimals(int fox, int deer, Tile infoTile, TileGraphic tile) {
		// places the animals in the tile and running a for loop giving them a random position
		for(int amt = 0; amt < infoTile.deerCount(); amt++) {
			Color brown = new Color(73, 36, 0);
			AnimalGraphic deerVisual = new AnimalGraphic(brown);
			deerVisual.setBounds((int)(Math.random() * 20), (int)(Math.random() * 20), 2, 2);
			tile.add(deerVisual);
		}
		for(int amt = 0; amt < infoTile.foxCount(); amt++) {
			Color orange = new Color(255, 135, 14);
			AnimalGraphic foxVisual = new AnimalGraphic(orange);
			foxVisual.setBounds((int)(Math.random() * 20), (int)(Math.random() * 20), 2, 2);
			tile.add(foxVisual);
		}
	}
	
	class PrecipVisual extends JPanel{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Color firstColor;
		private Color secondColor;
		
		public PrecipVisual(Color one, Color two) {
			firstColor = one;
			secondColor = two;
		}
		
		public void paintComponent(Graphics g) {
			 super.paintComponent(g);
			 // creates a gradient through 2D graphics
			 Graphics2D g2d = (Graphics2D) g;
			 // uses two colors to create a precipitation gradient
		     GradientPaint gp = new GradientPaint(0, 0, this.firstColor, 0, 146, this.secondColor);
			 g2d.setPaint(gp);
			 g2d.fillRect(0, 0, 26, 146);
		 }
	}
	
	class PrecipBar extends JPanel{
		
		private static final long serialVersionUID = 1L;
		private Color firstColor;
		
		public PrecipBar(Color one) {
			firstColor = one;
		}
		
		// creates a rectangle for the precipitation value
		public void paintComponent(Graphics g) {
			 super.paintComponent(g);
			 g.setColor(this.firstColor);
			 g.fillRect(0, 0, 26, 146);
		 }
	}
}
