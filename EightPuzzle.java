/*
Programmer: Cuyler Warnock
Date: 4/27/2016
Course: CSCI 1302 11AM
Purpose: Homework
ACADEMIC HONESTY STATEMENT:
I am sole author of the assignment. I have not received a digital copy or printout of the solution from anyone; however I receive
outside help from the following websites and people: No one
I have not given a digital copy or printout of my code to anyone; however, I discussed this problem with the following people: No one
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.EmptyBorder;
import java.awt.image.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.*;
import java.io.*;
import java.applet.*;
//import com.phidgets.*;
//import com.phidgets.event.*;

public class EightPuzzle extends JFrame
{ 
   // Panels Needed
   private JPanel pnlMain = new JPanel();
   private JPanel pnlLeft = new JPanel();
   private JPanel pnlScore = new JPanel();
   private JPanel pnlPuzzle = new JPanel();
   private JPanel pnlInstruc = new JPanel();
   private JPanel pnlRight = new JPanel();  
   private JPanel pnlBackground1 = new JPanel();
   private JPanel pnlBackground2 = new JPanel();
   private JPanel pnlBackground3 = new JPanel();
   private JPanel pnlBackgroundBlank = new JPanel();
   private JPanel pnlDifficulty = new JPanel();
   private JPanel pnlStart = new JPanel();
   
   private JFrame frmHighScore = new JFrame();
   private JPanel pnlHighScores = new JPanel();
   private JPanel pnlHighScores2 = new JPanel(new BorderLayout());
   
   // Buttons for Puzzle
   ArrayList<PuzzleButton> btnArray = new ArrayList<PuzzleButton>();
   {
      for (int i =1; i<=9; i++)
      {
         btnArray.add(new PuzzleButton());
      }
      btnArray.get(8).setEmpty();
   }
   
   // Labels for Instructions Panel
   private JLabel lblInstruc = new JLabel("This game is played by making a scrambled picture look like the orginal picture with an empty space located in the bottom right corner.");
   private JLabel lblInstruc1 = new JLabel("First, enter in a user name and then choose a difficulty. Once a difficulty is selected, you may choose a background for the picture which you would like to solve.");
   private JLabel lblInstruc2 = new JLabel("In order to rearrange the picture, press the touch sensor corresponding to a picture beside the empty space in order to move the picture to that space.");
   private JLabel lblInstruc3 = new JLabel("The game is complete when the picture is back to its orignal form.");
   private JLabel lblInstruc4 = new JLabel("You may view the top 5 high scores from each difficulty by pressing the High Scores button. Also, you can reset the puzzle by clikcing the Reset button."); 
   
   // Creating Images for Background
   public ImageIcon image;
   ImageIcon image1 = new ImageIcon("LOTR.jpg");
   ImageIcon imgLOTR = new ImageIcon(getScaledImage(image1.getImage(),300,200));
   ImageIcon image2 = new ImageIcon("starwars.jpg");
   ImageIcon imgStarWars = new ImageIcon(getScaledImage(image2.getImage(),300,200));
   ImageIcon image3 = new ImageIcon("harrypotter.jpg");
   ImageIcon imgHarryPotter = new ImageIcon(getScaledImage(image3.getImage(),300,200));
   ImageIcon image4 = new ImageIcon("Hubble.jpg");
   ImageIcon imgHubble = new ImageIcon(getScaledImage(image4.getImage(),300,200));
   ImageIcon image5 = new ImageIcon("universe.jpg");
   ImageIcon imgUniverse = new ImageIcon(getScaledImage(image5.getImage(),300,200));
   ImageIcon image6 = new ImageIcon("galaxy.jpg");
   ImageIcon imgGalaxy = new ImageIcon(getScaledImage(image6.getImage(),300,200));
   ImageIcon image7 = new ImageIcon("troll.jpg");
   ImageIcon imgTroll = new ImageIcon(getScaledImage(image7.getImage(),200,200));
   ImageIcon image8 = new ImageIcon("wood.jpg");
   ImageIcon wood = new ImageIcon(getScaledImage(image8.getImage(),300,300));

   // Everything Needed for Right Panel
   private JRadioButton btnBack1 = new JRadioButton(imgLOTR);
   private JRadioButton btnBack2 = new JRadioButton(imgStarWars);
   private JRadioButton btnBack3 = new JRadioButton(imgHarryPotter);
   private JRadioButton btnBack4 = new JRadioButton(imgHubble);
   private JRadioButton btnBack5 = new JRadioButton(imgUniverse);
   private JRadioButton btnBack6 = new JRadioButton(imgGalaxy);
   private JRadioButton btnTroll1 = new JRadioButton(imgTroll);
   private JRadioButton btnTroll2 = new JRadioButton(imgTroll);
   private JRadioButton btnTroll3 = new JRadioButton(imgTroll);
   
   private JRadioButton btnDiff1 = new JRadioButton("Intermediate");
   private JRadioButton btnDiff2 = new JRadioButton("Expert");
   private JRadioButton btnDiff3 = new JRadioButton("Impossible");
   private JButton btnStart = new JButton("START");
   private JButton btnHighScores = new JButton("High Scores");
   private JButton btnReset = new JButton("Reset");
   private JLabel lblUserName = new JLabel();
   private JLabel lblTimer = new JLabel();
   private JLabel lblNumMoves = new JLabel();
   private JLabel lblScore = new JLabel();

   // Initializing AudioClips
   private AudioClip soundLOTR;
   private AudioClip soundSW;
   private AudioClip soundHP;
   private AudioClip soundMassEffect;
   private AudioClip soundInterstellar;
   private AudioClip soundHalo;
   private AudioClip soundTroll;
   private AudioClip soundYAH;
   
   // Initializing Phidget Components
   /*
   private InterfaceKitPhidget ik1;
   private InterfaceKitPhidget ik2;
   private TextLCDPhidget LCD;
   */
  
   // Needed with StartListener and PuzzleButtonListener
   private boolean start=false;
   private int h,w;
   private String userName="";
   private Random r = new Random();
   private int count1 = r.nextInt(11) + 1;
   private int count2 = r.nextInt(6) + 1;
   
   // Setting up Score Panel
   private int minutes=0;
   private int seconds=0;
   private String time="00:00";
   private Timer timer;
   private int numMoves = 0;
   private int score = 1000;
   private ArrayList<Score> hs1Array = new ArrayList<>();
   private ArrayList<Score> hs2Array = new ArrayList<>();
   
   // Reading from and writing to highScores1 and highScores2
   private PrintWriter outfileDiff1;
   private PrintWriter outfileDiff2;
   private File fileDiff1;
   private Scanner infileDiff1;
   private File fileDiff2;
   private Scanner infileDiff2;
   
   public EightPuzzle()
   {
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int screenWidth = (int) screenSize.getWidth();
      int screenHeight = (int) screenSize.getHeight();
      
      setSize(screenWidth,screenHeight-50);
      setTitle("Eight Puzzle");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      //openPhidgets();
      
      // Setting Layouts
      pnlMain.setLayout(new BorderLayout(5,5));
      pnlLeft.setLayout(new BorderLayout(5,5));
      pnlScore.setLayout(new GridLayout(0,4));
      pnlPuzzle.setLayout(new GridLayout(3,3));
      pnlInstruc.setLayout(new GridLayout(0,1));
      pnlRight.setLayout(new BorderLayout());
      pnlBackground1.setLayout(new GridLayout(3,1,2,2));
      pnlBackground2.setLayout(new GridLayout(3,1,2,2));
      pnlBackground3.setLayout(new GridLayout(3,1,2,2));
      pnlBackgroundBlank.setLayout(new GridLayout(3,1,2,2));
      pnlStart.setLayout(new GridLayout(5,1,2,2));
      pnlDifficulty.setLayout(new GridLayout(1,2));
      
      
      // Adding Buttons to Puzzle Layout
      for (PuzzleButton pb:btnArray)
         pnlPuzzle.add(pb);
      
      // Setting Up Instruction Panel
      Font fontInstruc = new Font("Century Schoolbook",Font.PLAIN, 14);
      lblInstruc.setFont(fontInstruc);
      lblInstruc1.setFont(fontInstruc);
      lblInstruc2.setFont(fontInstruc);
      lblInstruc3.setFont(fontInstruc);
      lblInstruc4.setFont(fontInstruc);
      pnlInstruc.add(lblInstruc);
      pnlInstruc.add(lblInstruc1);
      pnlInstruc.add(lblInstruc2);
      pnlInstruc.add(lblInstruc3);
      pnlInstruc.add(lblInstruc4);
      
      // Creating Button Groups for Background and Difficulty
      ButtonGroup groupDifficulty = new ButtonGroup();
      groupDifficulty.add(btnDiff1);
      groupDifficulty.add(btnDiff2);
      groupDifficulty.add(btnDiff3);
      
      ButtonGroup groupBackground1 = new ButtonGroup();
      groupBackground1.add(btnBack1);
      groupBackground1.add(btnBack2);
      groupBackground1.add(btnBack3);
      
      ButtonGroup groupBackground2 = new ButtonGroup();
      groupBackground2.add(btnBack4);
      groupBackground2.add(btnBack5);
      groupBackground2.add(btnBack6);
      
      ButtonGroup groupBackground3 = new ButtonGroup();
      groupBackground3.add(btnTroll1);
      groupBackground3.add(btnTroll2);
      groupBackground3.add(btnTroll3);
      
      // Creating Background Panels
      pnlBackground1.add(btnBack1);
      pnlBackground1.add(btnBack2);
      pnlBackground1.add(btnBack3);
   
      pnlBackground2.add(btnBack4);
      pnlBackground2.add(btnBack5);
      pnlBackground2.add(btnBack6);
      
      pnlBackground3.add(btnTroll1);
      pnlBackground3.add(btnTroll2);
      pnlBackground3.add(btnTroll3);   
   
      
      // Creating Difficulty Panel
      pnlDifficulty.add(btnDiff1);
      pnlDifficulty.add(btnDiff2);
      pnlDifficulty.add(btnDiff3);
      
      //Creating Score Panel
      Font font1 = new Font("Engravers MT",Font.BOLD, 12);
      
      lblUserName.setText("User :  ");
      lblUserName.setFont(font1);
      pnlScore.add(lblUserName);
      
      lblTimer.setText("Timer :  " + time);
      lblTimer.setFont(font1);
      pnlScore.add(lblTimer);
      
      lblNumMoves.setText("Moves :  " + numMoves);
      lblNumMoves.setFont(font1);
      pnlScore.add(lblNumMoves);
      
      lblScore.setText("Score :  " + score);
      lblScore.setFont(font1);
      pnlScore.add(lblScore);
      
      // Creating Start Panel
      pnlStart.add(btnStart);
      pnlStart.add(new JLabel());
      pnlStart.add(btnHighScores);
      pnlStart.add(new JLabel());
      pnlStart.add(btnReset);
      
      // Setting Borders
      pnlMain.setBorder(new EmptyBorder(5,5,5,5));
      pnlScore.setBorder(BorderFactory.createEtchedBorder());
      pnlInstruc.setBorder(BorderFactory.createTitledBorder("Instructions"));
      pnlBackground1.setBorder(BorderFactory.createTitledBorder("Background"));
      pnlBackground2.setBorder(BorderFactory.createTitledBorder("Background"));
      pnlBackground3.setBorder(BorderFactory.createTitledBorder("Background"));
      pnlBackgroundBlank.setBorder(BorderFactory.createTitledBorder("Background"));
      pnlDifficulty.setBorder(BorderFactory.createTitledBorder("Difficulty"));
      
      // Setting Fixed Dimension to pnlRight
      pnlRight.setPreferredSize(new Dimension(330,802));
      
      // Adding Panels to JFrame
      pnlLeft.add(pnlScore, BorderLayout.NORTH);
      pnlLeft.add(pnlPuzzle, BorderLayout.CENTER);
      pnlLeft.add(pnlInstruc, BorderLayout.SOUTH);
      pnlMain.add(pnlLeft, BorderLayout.CENTER);
      pnlRight.add(pnlDifficulty, BorderLayout.NORTH);
      pnlRight.add(pnlBackgroundBlank, BorderLayout.CENTER);
      pnlRight.add(pnlStart, BorderLayout.SOUTH);
      pnlMain.add(pnlRight, BorderLayout.EAST);
      add(pnlMain);
      
      // Adding Listeners
      btnStart.addActionListener(new StartListener());
      btnDiff1.addItemListener(new DifficultyListener());
      btnDiff2.addItemListener(new DifficultyListener());
      btnDiff3.addItemListener(new DifficultyListener());
      
      btnBack1.addActionListener(new BackgroundListener());
      btnBack2.addActionListener(new BackgroundListener());
      btnBack3.addActionListener(new BackgroundListener());
      btnBack4.addActionListener(new BackgroundListener());
      btnBack5.addActionListener(new BackgroundListener());
      btnBack6.addActionListener(new BackgroundListener());
      btnTroll1.addActionListener(new BackgroundListener());
      btnTroll2.addActionListener(new BackgroundListener());
      btnTroll3.addActionListener(new BackgroundListener());
      
      for (PuzzleButton pb:btnArray)
         pb.addActionListener(new PuzzleButtonListener());
      btnHighScores.addActionListener(new HighScoreListener());
      btnReset.addActionListener(new ResetButtonListener());
      addWindowListener(new WindowCloseListener());
      
         
      // Creating Audio Clips
      try
      {
         File file = new File("ConcerningHobbits.wav");	
         URI uri = file.toURI();			
         URL url = uri.toURL();			 
         soundLOTR = Applet.newAudioClip(url); 	
         
         file = new File("PhantomMenace.wav");
         uri = file.toURI();
         url=uri.toURL();
         soundSW = Applet.newAudioClip(url);
         
         file = new File("HarryPotter.wav");
         uri = file.toURI();
         url=uri.toURL();
         soundHP = Applet.newAudioClip(url);
         
         file = new File("MassEffect.wav");
         uri = file.toURI();
         url=uri.toURL();
         soundMassEffect = Applet.newAudioClip(url);
         
         file = new File("Interstellar.wav");
         uri = file.toURI();
         url=uri.toURL();
         soundInterstellar = Applet.newAudioClip(url);
         
         file = new File("Halo.wav");
         uri = file.toURI();
         url=uri.toURL();
         soundHalo = Applet.newAudioClip(url);
         
         file = new File("Troll.wav");
         uri = file.toURI();
         url=uri.toURL();
         soundTroll = Applet.newAudioClip(url);
         
         file = new File("YAH.wav");
         uri = file.toURI();
         url=uri.toURL();
         soundYAH = Applet.newAudioClip(url);
      }
      catch (MalformedURLException e)
      {
         System.out.println("Error playing sound");
         e.printStackTrace();
      }
      
      
      setVisible(true);
      
      h=btnArray.get(0).getHeight()+1;
      w=btnArray.get(0).getWidth()+1;
      
      for (PuzzleButton pb:btnArray)
         pb.resetPicSize(h,w,pb.getImage());
         
      try
      {
         do
         {
            userName = JOptionPane.showInputDialog(pnlMain, "Enter username: ");
         }while (userName.equals(""));
         
         lblUserName.setText("User :  " + userName);
         lblUserName.revalidate();
         
         /*
         try{
            LCD.setDisplayString(0,"Welcome " + userName + "!");
            LCD.setDisplayString(1,"Select A Difficulty");
         }
         catch(PhidgetException pe)
         {
            pe.printStackTrace();
         }
         */
      }
      catch(NullPointerException e2){}
      
      timer=new Timer(1000, new TimerListener());
      
      // Reading from and Writing to High Score Files
      try
      {       
         fileDiff1 = new File("highScores1.txt");
         infileDiff1 = new Scanner(fileDiff1);
         fileDiff2 = new File("highScores2.txt");
         infileDiff2 = new Scanner(fileDiff2);
         
         createHighScoreArrays();
         
         outfileDiff1 = new PrintWriter("highScores1.txt");
         outfileDiff2 = new PrintWriter("highScores2.txt");
      }
      catch (Exception e2)
      {
         System.out.println("File cannot be created");
      }
      
   
   }
   
   // Reading from highScores1.txt and highScore2.txt and creating appropriate high scores arrays
   public void createHighScoreArrays()
   {
      while (infileDiff1.hasNext())
      {
         String s = infileDiff1.nextLine();
         String[] tokens = s.split(" ");
      
         try
         {
            int score = Integer.parseInt(tokens[tokens.length-1]);
            int numMoves = Integer.parseInt(tokens[tokens.length -2]);
            String time = tokens[tokens.length - 3];
            String userName = tokens[0];
            for (int i=1; i<tokens.length - 3; i++)
               userName = userName + tokens[i];
            
            hs1Array.add(new Score(userName, time, numMoves, score));
         }
         catch (Exception e3)
         {
            e3.printStackTrace();
         }
         
      
      }
      
      while (infileDiff2.hasNext())
      {
         String s = infileDiff2.nextLine();
         String[] tokens = s.split(" ");
      
         try
         {
            int score = Integer.parseInt(tokens[tokens.length-1]);
            int numMoves = Integer.parseInt(tokens[tokens.length -2]);
            String time = tokens[tokens.length - 3];
            String userName = tokens[0];
            for (int i=1; i<tokens.length - 3; i++)
               userName = userName + tokens[i];
            
            hs2Array.add(new Score(userName, time, numMoves, score));
         }
         catch (Exception e3)
         {
            e3.printStackTrace();
         }
         
      
      }
   }
   
   /*
   public void openPhidgets()
   {
      try 
      {    
         
         ik1 = new InterfaceKitPhidget();
         ik1.openAny();
         System.out.println("waiting for attachment...");
         ik1.waitForAttachment();
         System.out.println("InterfaceKitPhidget1 Attached");
         
         
         ik2 = new InterfaceKitPhidget();
         ik2.openAny();
         System.out.println("waiting for attachment...");
         ik2.waitForAttachment();
         System.out.println("InterfaceKitPhidget1 Attached");
         
         LCD = new TextLCDPhidget();
         LCD.openAny();
         System.out.println("waiting for attachment...");
         LCD.waitForAttachment();
         System.out.println("TextLCDPhidget Attached");
      
         LCD.setBacklight(true);
         LCD.setDisplayString(0,"Enter A User Name");
         LCD.setDisplayString(1,"");
         
         ik1.addSensorChangeListener(new InterfaceSensorChangeListener());
         ik2.addSensorChangeListener(new InterfaceSensorChangeListener());
      }
      
      catch(PhidgetException e)
      {
         e.printStackTrace();
      }
   }
   
   // Determining which sensor is pressed and moving appropriate tile
   private class InterfaceSensorChangeListener implements SensorChangeListener
   {
      public void sensorChanged(SensorChangeEvent se)
      {
         int port = se.getIndex();
         int value = se.getValue();
         
         for (int i=0; i<8; i++)
         {
            if (port == i && value>800 && btnArray.get(i).isClickable && se.getSource() == ik2)
            {
               btnArray.get(i).doClick();
               btnArray.get(i).setClickable();
            }
            
            if (port == i && value<800 && !btnArray.get(i).isClickable && se.getSource() == ik2)
               btnArray.get(i).setClickable();
         }
         
         if (port == 0 && value>800 && btnArray.get(8).isClickable && se.getSource() == ik1)
         {
            btnArray.get(8).doClick();
            btnArray.get(8).setClickable();
         }
            
         if (port == 0 && value<800 && !btnArray.get(8).isClickable && se.getSource() == ik1)
            btnArray.get(8).setClickable();
            
      }
   }
   
   */

   // Listener for Starting Puzzle
   private class StartListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         h=btnArray.get(0).getHeight()+1;
         w=btnArray.get(0).getWidth()+1;
         
         if (btnDiff1.isSelected())
         {
         
         // Setting Lord of the Rings background and playing music         
            if (btnBack1.isSelected())
            {
               pictureSetUp("lotr",h,w);
               soundLOTR.loop();
            }
            
            
            //Setting Star Wars background and playing music
            else if (btnBack2.isSelected())
            {
               pictureSetUp("sw",h,w);
               soundSW.loop();
            }
            
            // Setting Harry Potter background and playing music
            else if (btnBack3.isSelected())
            {
               pictureSetUp("hp",h,w);
               soundHP.loop();
            }
            
            else
               JOptionPane.showMessageDialog(pnlMain, "Please select a Background.");
         }
         
         else if (btnDiff2.isSelected())
         {
            
            // Setting up Hubble background and playing music
            if (btnBack4.isSelected())
            {
               pictureSetUp("hubble",h,w);
               soundMassEffect.loop();
            }
            
            // Setting up universe background and playing music
            else if (btnBack5.isSelected())
            {
               pictureSetUp("universe",h,w);
               soundInterstellar.loop();
            }
            
            // Setting up galaxy background and playing music
            else if (btnBack6.isSelected())
            {
               pictureSetUp("galaxy",h,w);
               soundHalo.loop();
            }
            
            else
               JOptionPane.showMessageDialog(pnlMain, "Please select a Background.");
            
         }
         
         // Setting up TrollFace background and playing music
         else if (btnDiff3.isSelected())
         {
            if (btnTroll1.isSelected() || btnTroll2.isSelected() || btnTroll3.isSelected())
            {
               pictureSetUp("troll",h,w);
               soundTroll.loop();
            }
            else
               JOptionPane.showMessageDialog(pnlMain ,"Please select a Background.");   
         }    
         else
            JOptionPane.showMessageDialog(pnlMain, "Please select a Difficulty.");
      
         // Setting blank tile to wood background
         btnArray.get(8).setIcon(new ImageIcon(getScaledImage(image8.getImage(),w,h)));
         for (PuzzleButton pb:btnArray)
         {
            pb.resetPicID();
            if (pb.isEmpty())
               pb.setEmpty();
         }
         btnArray.get(8).setEmpty();   
         
         // Determining difficulty and placing tiles
         if (btnDiff1.isSelected() || btnDiff2.isSelected())
         {
            generateSolvablePuzzle();            
         }
      
         if (btnDiff3.isSelected())
         {
            generateUnsolvablePuzzle();
         }
         
         if (start)
         {          
            minutes=0;
            seconds=-1;
            timer.start();
            
            score=1001;
            numMoves=0;
            lblNumMoves.setText("Moves :  " + numMoves);
            lblNumMoves.revalidate();
                        
         
         }
         
         /*
         try
         {
            LCD.setDisplayString(0,"Timer:" + time + " Moves:" + numMoves);
            LCD.setDisplayString(1,"Score:1000");
         }
         catch(PhidgetException pe)
         {
            pe.printStackTrace();
         }
         */
      }
      
      // Sets the background of each button to the correct picture
      public void pictureSetUp(String s, int h, int w)
      {
         for (int i=1; i<=8; i++)
         {
            start = true;
            String picFile = String.format("%s%d.jpg",s,i);
            btnArray.get(i-1).setIcon(new ImageIcon(getScaledImage(new ImageIcon(picFile).getImage(),w,h)));
         }
         soundTroll.stop();
         soundHP.stop();
         soundLOTR.stop();
         soundSW.stop();
         soundMassEffect.stop();
         soundInterstellar.stop();
         soundHalo.stop();
      } 
      
      public void generateSolvablePuzzle()
      {
         //PuzzleButton.swap(btnArray.get(8), btnArray.get(7));
      
         if (count1 == 1)
         {
            PuzzleButton.swap(btnArray.get(7), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(3), btnArray.get(1));
            PuzzleButton.swap(btnArray.get(4), btnArray.get(6));
         }
         else if (count1 == 2)
         {
            PuzzleButton.swap(btnArray.get(4), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(3), btnArray.get(1));
            PuzzleButton.swap(btnArray.get(6), btnArray.get(7));
            PuzzleButton.swap(btnArray.get(2), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(3), btnArray.get(1));
            PuzzleButton.swap(btnArray.get(4), btnArray.get(6));
         }  
         else if (count1 == 3)
         {        
            PuzzleButton.swap(btnArray.get(0), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(4), btnArray.get(3));
            PuzzleButton.swap(btnArray.get(6), btnArray.get(7));
            PuzzleButton.swap(btnArray.get(2), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(3), btnArray.get(7));
            PuzzleButton.swap(btnArray.get(8), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(1), btnArray.get(5));
            PuzzleButton.swap(btnArray.get(5), btnArray.get(4));
         }
         else if (count1 == 4)
         {
            PuzzleButton.swap(btnArray.get(3), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(4), btnArray.get(3));
            PuzzleButton.swap(btnArray.get(7), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(1), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(2), btnArray.get(7));
            PuzzleButton.swap(btnArray.get(5), btnArray.get(1));
         }
         else if (count1 == 5)
         { 
            PuzzleButton.swap(btnArray.get(0), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(7), btnArray.get(3));
            PuzzleButton.swap(btnArray.get(4), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(1), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(2), btnArray.get(4));
            PuzzleButton.swap(btnArray.get(5), btnArray.get(0));
         }
         else if (count1 == 6)
         {            
            PuzzleButton.swap(btnArray.get(0), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(7), btnArray.get(3));
            PuzzleButton.swap(btnArray.get(4), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(1), btnArray.get(2));
         }
         else if (count1 == 7)
         { 
            PuzzleButton.swap(btnArray.get(1), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(2), btnArray.get(4));
            PuzzleButton.swap(btnArray.get(5), btnArray.get(0));
            PuzzleButton.swap(btnArray.get(3), btnArray.get(2));
         }
         else if (count1 == 8)
         { 
            PuzzleButton.swap(btnArray.get(4), btnArray.get(3));
            PuzzleButton.swap(btnArray.get(7), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(1), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(2), btnArray.get(7));
            PuzzleButton.swap(btnArray.get(5), btnArray.get(1)); 
            PuzzleButton.swap(btnArray.get(4), btnArray.get(5));           
         } 
         else if (count1 == 9)
         { 
            PuzzleButton.swap(btnArray.get(0), btnArray.get(7));
            PuzzleButton.swap(btnArray.get(2), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(3), btnArray.get(7));
            PuzzleButton.swap(btnArray.get(1), btnArray.get(4));
         } 
         else if (count1 == 10)
         {
            PuzzleButton.swap(btnArray.get(3), btnArray.get(1));
            PuzzleButton.swap(btnArray.get(6), btnArray.get(7));
            PuzzleButton.swap(btnArray.get(2), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(3), btnArray.get(1));
            PuzzleButton.swap(btnArray.get(1), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(2), btnArray.get(7));
            PuzzleButton.swap(btnArray.get(4), btnArray.get(3));
         } 
         else if (count1 == 11)
         { 
            PuzzleButton.swap(btnArray.get(0), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(7), btnArray.get(3));
            PuzzleButton.swap(btnArray.get(4), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(1), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(2), btnArray.get(4));
            PuzzleButton.swap(btnArray.get(5), btnArray.get(0));
            PuzzleButton.swap(btnArray.get(3), btnArray.get(2));
         }
         else if (count1 == 12)
         {
            PuzzleButton.swap(btnArray.get(7), btnArray.get(6));
            PuzzleButton.swap(btnArray.get(5), btnArray.get(0));
            PuzzleButton.swap(btnArray.get(2), btnArray.get(3));
            PuzzleButton.swap(btnArray.get(5), btnArray.get(3));
            PuzzleButton.swap(btnArray.get(8), btnArray.get(6));
            PuzzleButton.swap(btnArray.get(4), btnArray.get(3));
         }
          
         count1 ++;
         if (count1 >12)
            count1=1; 
      }
      
      public void generateUnsolvablePuzzle()
      {
         if (count2 == 1)
         {
            PuzzleButton.swap(btnArray.get(7), btnArray.get(6));
            PuzzleButton.swap(btnArray.get(5), btnArray.get(0));
            PuzzleButton.swap(btnArray.get(2), btnArray.get(3));
            PuzzleButton.swap(btnArray.get(5), btnArray.get(3));
            PuzzleButton.swap(btnArray.get(1), btnArray.get(7)); 
         }
         else if (count2 == 2)   
         {   
            PuzzleButton.swap(btnArray.get(0), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(4), btnArray.get(3));
            PuzzleButton.swap(btnArray.get(6), btnArray.get(7));
            PuzzleButton.swap(btnArray.get(2), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(3), btnArray.get(7));
            PuzzleButton.swap(btnArray.get(8), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(1), btnArray.get(5));
         }   
         else if(count2 == 3)
         {   
            PuzzleButton.swap(btnArray.get(4), btnArray.get(3));
            PuzzleButton.swap(btnArray.get(7), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(1), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(2), btnArray.get(7));
            PuzzleButton.swap(btnArray.get(5), btnArray.get(1));
         }   
         else if (count2 == 4)
         {   
            PuzzleButton.swap(btnArray.get(0), btnArray.get(1));
            PuzzleButton.swap(btnArray.get(4), btnArray.get(3));
            PuzzleButton.swap(btnArray.get(6), btnArray.get(7));
            PuzzleButton.swap(btnArray.get(3), btnArray.get(7));
            PuzzleButton.swap(btnArray.get(8), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(1), btnArray.get(5));
            PuzzleButton.swap(btnArray.get(8), btnArray.get(7));
         }   
         else if (count2 == 5)
         {   
            PuzzleButton.swap(btnArray.get(0), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(7), btnArray.get(3));
            PuzzleButton.swap(btnArray.get(4), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(1), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(2), btnArray.get(4));
            PuzzleButton.swap(btnArray.get(5), btnArray.get(0));
            PuzzleButton.swap(btnArray.get(0), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(7), btnArray.get(3));
            PuzzleButton.swap(btnArray.get(4), btnArray.get(8));
         }   
         else if (count2 == 6)
         {   
            PuzzleButton.swap(btnArray.get(8), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(1), btnArray.get(5));
            PuzzleButton.swap(btnArray.get(8), btnArray.get(7));
            PuzzleButton.swap(btnArray.get(0), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(7), btnArray.get(3));
            PuzzleButton.swap(btnArray.get(4), btnArray.get(8));
            PuzzleButton.swap(btnArray.get(1), btnArray.get(2));
            PuzzleButton.swap(btnArray.get(2), btnArray.get(4));
            PuzzleButton.swap(btnArray.get(6), btnArray.get(7));
         }
         
         count2++;
         if (count2>6)
            count2=1;
      }
   }
   // Painting background choices depending on difficulty
   private class DifficultyListener implements ItemListener
   {
      public void itemStateChanged(ItemEvent e)
      {
         if (btnDiff1.isSelected())
         {
            pnlRight.remove(pnlBackgroundBlank);
            pnlRight.remove(pnlBackground2);
            pnlRight.remove(pnlBackground3);
            pnlRight.add(pnlBackground1, BorderLayout.CENTER);
            pnlRight.validate();
            pnlRight.repaint();
            
            /*
            try{
               LCD.setDisplayString(0,"Select A Background");
               LCD.setDisplayString(1,"And Press Start");
            }
            catch(PhidgetException pe)
            {
               pe.printStackTrace();
            }*/
         }
         else if (btnDiff2.isSelected())
         {  
            pnlRight.remove(pnlBackgroundBlank);
            pnlRight.remove(pnlBackground1);
            pnlRight.remove(pnlBackground3);
            pnlRight.add(pnlBackground2, BorderLayout.CENTER);
            pnlRight.validate();
            pnlRight.repaint();
            
            /*
            try{
               LCD.setDisplayString(0,"Select A Background");
               LCD.setDisplayString(1,"And Press Start");
            }
            catch(PhidgetException pe)
            {
               pe.printStackTrace();
            }
            */
         }
         else if (btnDiff3.isSelected())
         {
            pnlRight.remove(pnlBackgroundBlank);
            pnlRight.remove(pnlBackground1);
            pnlRight.remove(pnlBackground2);
            pnlRight.add(pnlBackground3, BorderLayout.CENTER);
            pnlRight.validate();
            pnlRight.repaint();
            
            /*
            try{
               LCD.setDisplayString(0,"Select A Background");
               LCD.setDisplayString(1,"And Press Start");
            }
            catch(PhidgetException pe)
            {
               pe.printStackTrace();
            }
            */
         }
      }      
   }
   
   private class BackgroundListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if (btnDiff1.isSelected())
         {
            if (btnBack1.isSelected())
               backgroundSet(btnBack1);
            else if (btnBack2.isSelected())
               backgroundSet(btnBack2);
            else if (btnBack3.isSelected())
               backgroundSet(btnBack3);
         }
         
         if (btnDiff2.isSelected())
         {
            if (btnBack4.isSelected())
               backgroundSet(btnBack4);
            else if (btnBack5.isSelected())
               backgroundSet(btnBack5);
            else if (btnBack6.isSelected())
               backgroundSet(btnBack6);
         }
         
         if (btnDiff3.isSelected())
         {
            if (btnTroll1.isSelected())
               backgroundSet(btnTroll1);
            else if (btnTroll2.isSelected())
               backgroundSet(btnTroll2);
            else if (btnTroll3.isSelected())
               backgroundSet(btnTroll3);
         }
      }
      
      public void backgroundSet(JRadioButton back)
      {
         btnBack1.setBackground(null);
         btnBack2.setBackground(null);
         btnBack3.setBackground(null);
         btnBack4.setBackground(null);
         btnBack5.setBackground(null);
         btnBack6.setBackground(null);
         btnTroll1.setBackground(null);
         btnTroll2.setBackground(null);
         btnTroll3.setBackground(null);
         back.setBackground(Color.GREEN);
      }
   
   }
   
   // Listener for Moving Tiles
   private class PuzzleButtonListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         PuzzleButton btnClicked = new PuzzleButton();
         PuzzleButton btnEmpty = new PuzzleButton();
      
         if (start)
         {
            for (PuzzleButton pb:btnArray)
            {
               if (e.getActionCommand().equals(""+pb.getID()))
               {
                  btnClicked = pb;
               }
            
               if (pb.isEmpty())
               {  
                  btnEmpty = pb;
               }
            }
            
            // Setting Score for Intermediate and Impossible Difficulty
            if (PuzzleButton.isNear(btnClicked, btnEmpty) && (btnDiff1.isSelected() || btnDiff3.isSelected()))
            {
               PuzzleButton.swap(btnClicked, btnEmpty);
               
               numMoves++;
               lblNumMoves.setText("Moves :  " + numMoves);
               lblNumMoves.revalidate();
               score -= 5;
               lblScore.setText("Score :  " + score);
               lblScore.revalidate();
               
               /*
               try
               {
                  LCD.setDisplayString(0,"Timer:" + time + " Moves:" + numMoves);
                  LCD.setDisplayString(1,"Score:" + score);
               }
               catch(PhidgetException pe)
               {
                  pe.printStackTrace();
               }     
               */
            }
            
            // Setting Score for Expert Difficulty
            else if (PuzzleButton.isNear(btnClicked, btnEmpty) && btnDiff2.isSelected())
            {
               PuzzleButton.swap(btnClicked, btnEmpty);
               
               numMoves++;
               lblNumMoves.setText("Moves :  " + numMoves);
               lblNumMoves.revalidate();
               score -= 2;
               lblScore.setText("Score :  " + score);
               lblScore.revalidate();   
               
               /*
               try
               {
                  LCD.setDisplayString(0,"Timer:" + time + " Moves:" + numMoves);
                  LCD.setDisplayString(1,"Score:" + score);
               }
               catch(PhidgetException pe)
               {
                  pe.printStackTrace();
               }  
               */
            }
            
         
            
         // Determining end of game
            if (btnArray.get(0).getPicID()==btnArray.get(0).getID()
               && btnArray.get(1).getPicID()==btnArray.get(1).getID()
               && btnArray.get(2).getPicID()==btnArray.get(2).getID()
               && btnArray.get(3).getPicID()==btnArray.get(3).getID()
               && btnArray.get(4).getPicID()==btnArray.get(4).getID()
               && btnArray.get(5).getPicID()==btnArray.get(5).getID()
               && btnArray.get(6).getPicID()==btnArray.get(6).getID()
               && btnArray.get(7).getPicID()==btnArray.get(7).getID())
            { 
               soundHP.stop();
               soundLOTR.stop();
               soundSW.stop();
               soundMassEffect.stop();
               soundInterstellar.stop();
               soundHalo.stop();
               soundYAH.play();
               start=false;
               timer.stop();
               
               /*
               try
               {
                  LCD.setDisplayString(0,"Winner!  Choose");
                  LCD.setDisplayString(1,"Another Puzzle"); 
               }
               catch(PhidgetException e3)
               {
                  e3.printStackTrace();
               }
               */
               
               JOptionPane.showMessageDialog(pnlMain,"WINNER!!!");
               
               
               Score newHighScore = new Score(userName,time,numMoves,score);
               
               // Determining if the new score is in the top 5. If so, then insert into the array corresponding
               // the its difficulty in the appropriate position. Then remove the sixth score which will be the lowest.
               
               if(btnDiff1.isSelected())
               {
                  try
                  {
                     if (newHighScore.getScore() > hs1Array.get(0).getScore())
                     {
                        hs1Array.add(0,newHighScore);
                        JOptionPane.showMessageDialog(pnlMain, "NEW HIGH SCORE!! \n Your score of " + hs1Array.get(0).getScore()+ " is the highest score!");
                     }
                         
                     else if (newHighScore.getScore() > hs1Array.get(1).getScore())
                     {
                        hs1Array.add(1,newHighScore);
                        JOptionPane.showMessageDialog(pnlMain, "CONGRATULATIONS!! \n Your score of " + hs1Array.get(1).getScore()+ " is the second highest score!");
                     }
                     
                     else if (newHighScore.getScore() > hs1Array.get(2).getScore())
                     {
                        hs1Array.add(2,newHighScore);
                        JOptionPane.showMessageDialog(pnlMain, "CONGRATULATIONS!! \n Your score of " + hs1Array.get(2).getScore()+ " is the third highest score!");
                     }
                     
                     else if (newHighScore.getScore() > hs1Array.get(3).getScore())
                     {
                        hs1Array.add(3,newHighScore);
                        JOptionPane.showMessageDialog(pnlMain, "CONGRATULATIONS!! \n Your score of " + hs1Array.get(3).getScore()+ " is the fourth highest score!");
                     }
                        
                     else if (newHighScore.getScore() > hs1Array.get(4).getScore())
                     {
                        hs1Array.add(4,newHighScore);
                        JOptionPane.showMessageDialog(pnlMain, "CONGRATUALTIONS!! \n Your score of " + hs1Array.get(4).getScore()+ " is the fourth highest score!");
                     }
                     
                     if (hs1Array.size() == 6)
                        hs1Array.remove(5);  
                  }
                  
                  catch(Exception e1)
                  {
                     hs1Array.add(newHighScore);
                     
                     if (hs1Array.size() == 1)
                        JOptionPane.showMessageDialog(pnlMain, "NEW HIGH SCORE!! \n Your score of " + hs1Array.get(0).getScore()+ " is the highest score!");
                     else if (hs1Array.size() == 2)
                        JOptionPane.showMessageDialog(pnlMain, "CONGRATULATIONS!! \n Your score of " + hs1Array.get(1).getScore()+ " is the second highest score!");
                     else if (hs1Array.size() == 3)
                        JOptionPane.showMessageDialog(pnlMain, "CONGRATULATIONS!! \n Your score of " + hs1Array.get(2).getScore()+ " is the third highest score!");
                     else if (hs1Array.size() == 4)
                        JOptionPane.showMessageDialog(pnlMain, "CONGRATULATIONS!! \n Your score of " + hs1Array.get(3).getScore()+ " is the fourth highest score!");
                     else if (hs1Array.size() == 5)
                        JOptionPane.showMessageDialog(pnlMain, "CONGRATULATIONS!! \n Your score of " + hs1Array.get(4).getScore()+ " is the highest score!");
                  }
                  
               }
               
               
               
               
               else if(btnDiff2.isSelected())
               {
                  try
                  {
                     if (newHighScore.getScore() > hs2Array.get(0).getScore())
                     {
                        hs2Array.add(0,newHighScore);
                        JOptionPane.showMessageDialog(pnlMain, "NEW HIGH SCORE!! \n Your score of " + hs2Array.get(0).getScore()+ " is the highest score!");
                     }
                         
                     else if (newHighScore.getScore() > hs2Array.get(1).getScore())
                     {
                        hs2Array.add(1,newHighScore);
                        JOptionPane.showMessageDialog(pnlMain, "CONGRATULATIONS!! \n Your score of " + hs2Array.get(1).getScore()+ " is the second highest score!");
                     }
                     
                     else if (newHighScore.getScore() > hs2Array.get(2).getScore())
                     {
                        hs2Array.add(2,newHighScore);
                        JOptionPane.showMessageDialog(pnlMain, "CONGRATULATIONS!! \n Your score of " + hs2Array.get(2).getScore()+ " is the third highest score!");
                     }
                     
                     else if (newHighScore.getScore() > hs2Array.get(3).getScore())
                     {
                        hs2Array.add(3,newHighScore);
                        JOptionPane.showMessageDialog(pnlMain, "CONGRATULATIONS!! \n Your score of " + hs2Array.get(3).getScore()+ " is the fourth highest score!");
                     }
                        
                     else if (newHighScore.getScore() > hs2Array.get(4).getScore())
                     {
                        hs2Array.add(4,newHighScore);
                        JOptionPane.showMessageDialog(pnlMain, "CONGRATUALTIONS!! \n Your score of " + hs2Array.get(4).getScore()+ " is the fourth highest score!");
                     }
                     
                     if (hs2Array.size() == 6)
                        hs2Array.remove(5); 
                  }
                  
                  catch(Exception e1)
                  {
                     hs2Array.add(newHighScore);
                     
                     if (hs2Array.size() == 1)
                        JOptionPane.showMessageDialog(pnlMain, "NEW HIGH SCORE!! \n Your score of " + hs2Array.get(0).getScore()+ " is the highest score!");
                     else if (hs2Array.size() == 2)
                        JOptionPane.showMessageDialog(pnlMain, "CONGRATULATIONS!! \n Your score of " + hs2Array.get(1).getScore()+ " is the second highest score!");
                     else if (hs2Array.size() == 3)
                        JOptionPane.showMessageDialog(pnlMain, "CONGRATULATIONS!! \n Your score of " + hs2Array.get(2).getScore()+ " is the third highest score!");
                     else if (hs2Array.size() == 4)
                        JOptionPane.showMessageDialog(pnlMain, "CONGRATULATIONS!! \n Your score of " + hs2Array.get(3).getScore()+ " is the fourth highest score!");
                     else if (hs2Array.size() == 5)
                        JOptionPane.showMessageDialog(pnlMain, "CONGRATULATIONS!! \n Your score of " + hs2Array.get(4).getScore()+ " is the highest score!");
                  }
               }
               
            }
         }
      }
   }
   
   private class WindowCloseListener implements WindowListener
   {
      public void windowActivated(WindowEvent e)
      {
      }
      public void windowClosing(WindowEvent e)
      {
         // Writing the top 5 scores to a file which can then be called when the program opens again
         // to obtain the 5 highest scores from anytime not just the current runtime.
         
        
         try
         {   
            /*
            LCD.setBacklight(false);
            LCD.setDisplayString(0,"");
            LCD.setDisplayString(1,"");
            */
            
            for (int i=0; i<hs1Array.size(); i++)
            {
               outfileDiff1.println(hs1Array.get(i));
            }
            outfileDiff1.close();
            for (int i=0; i<hs2Array.size(); i++)
               outfileDiff2.println(hs2Array.get(i));
            outfileDiff2.close();
         }
         catch(Exception e2)
         {
            e2.getStackTrace();
         }
         
      }
      public void windowDeactivated(WindowEvent e)
      {
      }
      public void windowDeiconified(WindowEvent e)
      {
      }
      public void windowIconified(WindowEvent e)
      {
      }
      public void windowOpened(WindowEvent e)
      {
      }
      public void windowClosed(WindowEvent e)
      {
      }
   }
      
   // Keeps track of how much time has passed during game
   private class TimerListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if (seconds != 59)
            seconds++;
         else
         {
            seconds=0;
            minutes++;
         }
         time = String.format("%02d:%02d",minutes,seconds);
         lblTimer.setText("Timer :  " + time);
         lblTimer.revalidate();
         
         score--;
         lblScore.setText("Score :  " + score);
         lblScore.revalidate();
         
         /*
         try
         {
            LCD.setDisplayString(0,"Timer:" + time + " Moves:" + numMoves);
            LCD.setDisplayString(1,"Score:" + score);
         }
         catch(PhidgetException pe)
         {
            pe.printStackTrace();
         }
         */
      }
   }
   
   // Creates high score panel
   public class HighScoreListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         createFrame();
      }
      
      
   }
   
   // Creating High Scores Frame
   public void createFrame()
   {
      JFrame frmHighScore = new JFrame("High Scores");
      frmHighScore.setSize(600,400);
      frmHighScore.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frmHighScore.setTitle("High Scores");
      
      pnlHighScores2.remove(pnlHighScores);   
      
      createPnlHighScores();
      
      // I had to add another panel so that I could repaint and have 
      // window instantly refresh after removing high scores
      
      pnlHighScores2.add(pnlHighScores, BorderLayout.CENTER);
      pnlHighScores2.validate();
      pnlHighScores2.repaint();
      
      frmHighScore.add(pnlHighScores2);
      frmHighScore.setVisible(true);
   }
      
   public void createPnlHighScores()
   {
      pnlHighScores = new JPanel(new GridLayout(21,0,0,0));
      JPanel pnlTopHead = new JPanel(new BorderLayout());
      JPanel pnlMidHead = new JPanel(new BorderLayout());
      JPanel pnlBotHead = new JPanel(new BorderLayout());
      JPanel pnlHeader1 = new JPanel(new GridLayout(1,5));
      JPanel pnlHeader2 = new JPanel(new GridLayout(1,5));
      JPanel pnlHeader3 = new JPanel(new GridLayout(1,5));
      JPanel pnlReset = new JPanel(new GridLayout(1,3));
      JButton btnResetHS = new JButton("Reset High Scores");      
         
      btnResetHS.addActionListener(new ResetHighScoreListener());
         
      pnlHeader1.add(new JLabel("Intermediate"));
      pnlHeader1.add(new JLabel("User"));
      pnlHeader1.add(new JLabel("Time"));
      pnlHeader1.add(new JLabel("Moves"));
      pnlHeader1.add(new JLabel("Score"));
      
      
      pnlTopHead.add(pnlHeader1);
      pnlTopHead.add(new JSeparator(), BorderLayout.SOUTH);
      pnlHighScores.add(pnlTopHead);
   
      int count=0;
      for (int i=0; i<hs1Array.size(); i++)
      {  
         JPanel pnlRow1 = new JPanel(new GridLayout(1,5));
         pnlRow1.add(new JLabel());
         pnlRow1.add(new JLabel(hs1Array.get(i).getUserName()));
         pnlRow1.add(new JLabel(hs1Array.get(i).getTime()));
         pnlRow1.add(new JLabel(""+hs1Array.get(i).getNumMoves()));
         pnlRow1.add(new JLabel(""+hs1Array.get(i).getScore()));
            
         pnlHighScores.add(pnlRow1);
         count++;
      }
            
      for (int i=count; i<5; i++)
         pnlHighScores.add(new JPanel());
         
         
      pnlHeader2.add(new JLabel("Expert"));
      pnlHeader2.add(new JLabel("User"));
      pnlHeader2.add(new JLabel("Time"));
      pnlHeader2.add(new JLabel("Moves"));
      pnlHeader2.add(new JLabel("Score"));
         
      pnlMidHead.add(pnlHeader2);
      pnlMidHead.add(new JSeparator(), BorderLayout.SOUTH);
         
      pnlHighScores.add(new JLabel());
      pnlHighScores.add(new JLabel());
      pnlHighScores.add(pnlMidHead);
         
      count=0;
      for (int i=0; i<hs2Array.size(); i++)
      {
         JPanel pnlRow2 = new JPanel(new GridLayout(1,5));
         pnlRow2.add(new JLabel());
         pnlRow2.add(new JLabel(hs2Array.get(i).getUserName()));
         pnlRow2.add(new JLabel(hs2Array.get(i).getTime()));
         pnlRow2.add(new JLabel(""+hs2Array.get(i).getNumMoves()));
         pnlRow2.add(new JLabel(""+hs2Array.get(i).getScore()));
            
         pnlHighScores.add(pnlRow2);
         count++;
      }
            
      for (int i=count; i<5; i++)
         pnlHighScores.add(new JPanel());
         
         
      pnlHeader3.add(new JLabel("Impossible"));
      pnlHeader3.add(new JLabel("User"));
      pnlHeader3.add(new JLabel("Time"));
      pnlHeader3.add(new JLabel("Moves"));
      pnlHeader3.add(new JLabel("Score"));
         
      pnlBotHead.add(pnlHeader3);
      pnlBotHead.add(new JSeparator(), BorderLayout.SOUTH);
         
      pnlHighScores.add(new JLabel());
      pnlHighScores.add(new JLabel());
      pnlHighScores.add(pnlBotHead);
         
      JPanel pnlRow3 = new JPanel(new GridLayout(1,5));
      pnlRow3.add(new JLabel());
      pnlRow3.add(new JLabel("Chuck Norris"));
      pnlRow3.add(new JLabel("- 00:27"));
      pnlRow3.add(new JLabel("1"));
      pnlRow3.add(new JLabel("31415926"));
         
      pnlHighScores.add(pnlRow3);
      pnlHighScores.add(new JLabel());
      pnlHighScores.add(new JLabel());
         
      pnlReset.add(new JLabel());
      pnlReset.add(btnResetHS);
      pnlReset.add(new JLabel());
         
      pnlHighScores.add(pnlReset);
   }
   
   public class ResetHighScoreListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         hs1Array = new ArrayList<>();
         hs2Array = new ArrayList<>();
         pnlHighScores2.remove(pnlHighScores);
         createPnlHighScores();
         pnlHighScores2.add(pnlHighScores, BorderLayout.CENTER); 
         pnlHighScores2.validate();
         pnlHighScores2.repaint(); 
         
      }
   }
   
   private class ResetButtonListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         for (PuzzleButton pb:btnArray)
         {
            pb.setIcon(new ImageIcon(getScaledImage(image8.getImage(),w,h)));
            pb.resetPicID();
            if (pb.isEmpty())
               pb.setEmpty();
         }
         btnArray.get(8).setEmpty();
      
         timer.stop();
         soundTroll.stop();
         soundHP.stop();
         soundLOTR.stop();
         soundSW.stop();
         soundMassEffect.stop();
         soundInterstellar.stop();
         soundHalo.stop();
         
         time = "00:00";
         numMoves = 0;
         score = 1000;
         lblUserName.setText("User :  ");
         lblUserName.revalidate();
         lblTimer.setText("Timer :  " + time);
         lblTimer.revalidate();
         lblNumMoves.setText("Moves :  " + numMoves);
         lblNumMoves.revalidate();
         lblScore.setText("Score :  " + score);
         lblScore.revalidate();
         
         /*
         try
         {
            LCD.setDisplayString(0,"Enter A User Name");
            LCD.setDisplayString(1,"");
         }
         catch(PhidgetException pe)
         {
            pe.printStackTrace();
         }
         */
         
         try
         {   
            do
            {
               userName = JOptionPane.showInputDialog(pnlMain, "Enter username: ");
            }while (userName.equals(""));
            lblUserName.setText("User :  " + userName);
            lblUserName.revalidate();
            
            /*
            try{
               LCD.setDisplayString(0,"Welcome " + userName + "!");
               LCD.setDisplayString(1,"Select A Difficulty");
            }
            catch(PhidgetException pe)
            {
               pe.printStackTrace();
            }
            */
         }
         catch(NullPointerException e1)
         {
         
         }
      }
   }
   
   private static class PuzzleButton extends JButton
   {
      private boolean isEmpty = false;
      private boolean isClickable = true;
      private static int n = 0;
      private int id;
      private int picID;
      private ImageIcon pic;
      private ImageIcon wood;
      
      public PuzzleButton()
      {
         n+=1;
         id=n;
         setActionCommand(""+id);
         picID=n;
         wood = new ImageIcon("wood.jpg");
         pic = new ImageIcon(getScaledImage(wood.getImage(),300,300));
         
         setIcon(pic);
         
         setBorderPainted(false);
      } 
      
      public boolean isEmpty()
      {
         if (this.isEmpty)
            return true;
         else 
            return false;
      }
      
      public void setEmpty()
      {
         this.isEmpty=!this.isEmpty;
      }
      
      public void setClickable()
      {
         this.isClickable=!this.isClickable;
      }
      public ImageIcon getImage()
      {
         return (ImageIcon) getIcon();
      }
      
      public int getID()
      {
         return id;
      }
      
      public int getPicID()
      {
         return picID;
      }
      
      public void setPicID(int a)
      {
         picID = a;
      }
      
      public void resetPicID()
      {
         picID = id;
      }
      
      // Determines if Button is near the Empty Space
      public static boolean isNear(PuzzleButton cl, PuzzleButton em)
      {
         if (cl.id == 1 && (em.id == 2 || em.id == 4))
            return true;
         else if (cl.id == 2 && (em.id == 1 || em.id == 3 || em.id == 5))
            return true;
         else if (cl.id == 3 && (em.id == 2 || em.id == 6))
            return true;
         else if (cl.id == 4 && (em.id == 1 || em.id == 5 || em.id == 7))
            return true;
         else if (cl.id == 5 && (em.id == 2 || em.id == 4 || em.id == 6 || em.id == 8))
            return true;
         else if (cl.id == 6 && (em.id == 3 || em.id == 5 || em.id == 9))
            return true;
         else if (cl.id == 7 && (em.id == 4 || em.id == 8))
            return true;
         else if (cl.id == 8 && (em.id == 5 || em.id == 7 || em.id == 9))
            return true;
         else if (cl.id == 9 && (em.id == 8 || em.id == 6))
            return true;
         else
            return false;
      }
      
      public void resetPicSize(int h, int w, ImageIcon p)
      {
         p = new ImageIcon(getScaledImage(p.getImage(),w,h));
         setIcon(p);
      }
   
      
      public static void swap(PuzzleButton btn1, PuzzleButton btn2)
      {
         int temp = btn1.getPicID();
         btn1.setPicID(btn2.getPicID());
         btn2.setPicID(temp);
                  
         PuzzleButton btnTemp = new PuzzleButton();
         btnTemp.setIcon(btn1.getIcon());
         btn1.setIcon(btn2.getIcon());
         btn2.setIcon(btnTemp.getIcon());
         
         if (btn1.isEmpty())
         {
            btn1.setEmpty();
            btn2.setEmpty();
         }
         else if (btn2.isEmpty())
         {
            btn1.setEmpty();
            btn2.setEmpty();
         }
         
      }
      
      
      public Image getScaledImage(Image srcImg, int w, int h)
      {   
         BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
         Graphics2D g2 = resizedImg.createGraphics();
         g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
         g2.drawImage(srcImg, 0, 0, w, h, null);
         g2.dispose();
         return resizedImg;
      }
      
   }
   
   public class Score
   {
      String userName="";
      String time="";
      int numMoves=0;
      int score=0;
      
      public Score(String uN, String t, int nM, int s)
      {
         userName = uN;
         time = t;
         numMoves = nM;
         score = s;
      }
      
      public String getUserName()
      {
         return userName;
      }
      
      public String getTime()
      {
         return time;
      }
      
      public int getNumMoves()
      {
         return numMoves;
      }
            
      public int getScore()
      {
         return score;
      }
      
      public String toString()
      {
         return String.format("%s %s %d %d",userName,time,numMoves,score);
      }
         
         
   }

   public static void main (String[] args)
   {
      new EightPuzzle();
   }
   
   
   public Image getScaledImage(Image srcImg, int w, int h)
   {   
      BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
      Graphics2D g2 = resizedImg.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2.drawImage(srcImg, 0, 0, w, h, null);
      g2.dispose();
      return resizedImg;
   }
}