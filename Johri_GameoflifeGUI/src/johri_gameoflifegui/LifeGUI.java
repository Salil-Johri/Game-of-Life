/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package johri_gameoflifegui;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
/**
 *
 * @author salil
 */
public class LifeGUI extends MouseAdapter{
    Font f = new Font("Times New Roman", Font.BOLD, 30);
    private final static int TIMER_MIN = 0;
    private final static int TIMER_MAX = 1000;
    JLabel titleLife, labelSlider, labelPresets;
    JButton[][] grid = new JButton[50][50];
    private int[][] lifeNumbersCurrent;
    private int[][] lifeNumbersNext;
    JButton startGame, resetGrid, stopGame;
    JFrame frame;
    JPanel contentPaneGrid, contentPaneTitle, contentPaneButtons, contentPane, contentPaneSlider, contentPaneOptions;
    JComboBox presets;
    private String[] options;
    JSlider timerDelay;
    Life cells = new Life(50);
    Timer time;

    /**Constructor for GUI that creates an array of 50 buttons that represent the grid, a title, 
    * buttons to manipulate the grid, preset patters for the user to choose from,
    * and a slider that increases and decreases the delay on the timer
    * Pre: none
    * Post: Life GUI is instantiated but not created for the user.
    * 
    */
    public LifeGUI(){
        //Content pane where title is located. Title's font is manipulated.
        titleLife = new JLabel("CONWAY'S GAME OF LIFE");
        titleLife.setFont(f);
        contentPaneTitle = new JPanel();
        contentPaneTitle.setLayout(new BoxLayout(contentPaneTitle, BoxLayout.PAGE_AXIS));
        contentPaneTitle.add(titleLife);
        titleLife.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        //Content Pane where grid is located
        contentPaneGrid = new JPanel();
        contentPaneGrid.setLayout(new GridLayout(0, 50, 0, 0));
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = new JButton("");
                grid[i][j].setPreferredSize(new Dimension(8, 8));
                grid[i][j].setBorderPainted(false);
                grid[i][j].setBackground(Color.blue);
                grid[i][j].addMouseListener(this);
                contentPaneGrid.add(grid[i][j]);
            }
        }
        //Content Pane where buttons are located
        contentPaneGrid.setBackground(Color.black);
        startGame = new JButton("Start Game");
        startGame.addActionListener(new StartListener());
        startGame.setBackground(Color.yellow);
        stopGame = new JButton("Stop Game");
        stopGame.setBackground(Color.yellow);
        stopGame.addActionListener(new StopListener());
        resetGrid = new JButton("Reset Grid");
        resetGrid.addActionListener(new ResetListener());
        resetGrid.setBackground(Color.yellow);
        contentPaneButtons = new JPanel();
        contentPaneButtons.add(startGame);
        contentPaneButtons.add(stopGame);
        contentPaneButtons.add(resetGrid);
        
        //Content pane where combo box containing options for presets are located
        String [] options = {"Rectangle Border", "Glider", "Blinker", "Pulsar"};
        presets = new JComboBox(options);
        presets.setSelectedIndex(0);
        presets.addActionListener(new ComboBoxListener());
        contentPaneOptions = new JPanel();
        labelPresets = new JLabel("Preset Options: ");
        contentPaneOptions.add(labelPresets);
        contentPaneOptions.add(presets);
        
        //Content pane where slider is located
        labelSlider = new JLabel("Set speed of Timer: ");
        contentPaneSlider = new JPanel();
        timerDelay = new JSlider(JSlider.HORIZONTAL, TIMER_MIN, TIMER_MAX, TIMER_MAX - 100);
        timerDelay.setMajorTickSpacing(100);
        timerDelay.setPaintTicks(true);
        timerDelay.addChangeListener(new TimeChange());
        contentPaneSlider.add(labelSlider);
        contentPaneSlider.add(timerDelay);
        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        //Where all content panes are added to main content pane for GUI
        contentPane.add(contentPaneTitle);
        contentPane.add(contentPaneGrid);
        contentPane.add(contentPaneButtons);
        contentPane.add(contentPaneOptions);
        contentPane.add(contentPaneSlider);
        frame = new JFrame("Conway's Game of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(contentPane);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.pack();
        time = new Timer(TIMER_MAX - timerDelay.getValue(), new StartListener());
    }
    
    /**Action listener that starts the game when the user is ready. This action listener is added to the "Start game" button
    * Pre: none
    * Post: Game and timer is started and displayed for the user
    * 
    */
    class StartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            time.start();
            takeStep();
           
        }
    }
    
    /**Action listener that resets the game board and stops the timer. This action listener is added to the "Reset game" button
    * Pre: none
    * Post: Game board is reset and timer is stopped.
    * 
    */
    class ResetListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            time.stop();
            resetBoard();
        }
        
    }
 
    /**Action listener that stops the timer without resetting the game board. This action listener is added to the "Stop game" button
    * Pre: none
    * Post: Timer is stopped, but game board is not reset.
    * 
    */
    class StopListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            time.stop();
        }
    }
    
    /**Change listener that manipulates the delay on the timer. This listener is added to the slider that the user manipulates
    * Pre: User changing the timer delay
    * Post: Timer delay is set to the desired speed by the user
    * 
    */
    class TimeChange implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            int timeDelay;
            JSlider source = (JSlider)e.getSource();
            if(source.getValueIsAdjusting()) {
                time.setDelay(TIMER_MAX - source.getValue());
            }
    }
    }
    
    /**Action listener that tracks the preset that the user chooses. This action listener is added to the combo box that the user sees
     * to choose the preset
    * Pre: User changing the preset
    * Post: The board is cleared and desired preset is made and displayed for the user.
    * 
    */
    class ComboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox box = (JComboBox)e.getSource();
            String choice = (String)box.getSelectedItem();
            
            if(choice.equals("Rectangle Border")) {
                resetBoard();
                createRectangle();
            }else if(choice.equals("Glider")) {
                resetBoard();
                createGlider();
            }else if(choice.equals("Blinker")) {
                resetBoard();
                createBlinker();
            } else if(choice.equals("Pulsar")) {
                resetBoard();
                createPulsar();
            }
        }
    }
    /**Helper method that instantiates the GUI for the user
    * Pre: None
    * Post: The helper method makes the GUI and is called upon in the main method
    * 
    */
    private static void runGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        LifeGUI foo = new LifeGUI();
    }
    
    /**Take step method that takes a step of the game of life. This is done with the help of the console verion of life.
     * An int grid is made based on what the user selected on the gui (red being 1 or alive, blue being 0 or dead). This grid is then passed
     * to the life method. A new grid is then created and set to the result of the grid that was passed to the life console. The initial grid is then
     * set to the new grid, and the new grid then used to change the gui board to red of blue
     * This method is continuous once the game is started because of a timer
    * Pre: User input on pattern
    * Post: Step of game is taken and board is set according to rules.
    * 
    */
    public void takeStep() {
        lifeNumbersCurrent = new int[50][50];
        //Int grid is made based on gui board
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if(grid[i][j].getBackground().equals(Color.red)) {
                        lifeNumbersCurrent[i][j] = 1;
                    }else {
                        lifeNumbersCurrent[i][j] = 0;
                    }
                }
            }
            
            //Int grid is passed to life console
            cells.setPattern(lifeNumbersCurrent);
            lifeNumbersNext = new int[50][50];
            //Next iteration of life is made based on previous int grid
            lifeNumbersNext = cells.takeStep(lifeNumbersCurrent);
            
            //The next life array becomes the current life array
            for (int i = 0; i < lifeNumbersCurrent.length; i++) {
            System.arraycopy(lifeNumbersNext[i], 0, lifeNumbersCurrent[i], 0, lifeNumbersCurrent[0].length);
        }
            
            //Background is changed based on the change 
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if(lifeNumbersNext[i][j] == 1) {
                        grid[i][j].setBackground(Color.red);
                    } else {
                        grid[i][j].setBackground(Color.blue);
                    }
                }
            }
    }
    
    /**Mouse listener that changes the colour of the buttons based on user clicks. This listener is added to all the buttons on the life grid
    * Pre: None
    * Post: Button is either changed to red or blue
    */
    @Override
    public void mouseClicked(MouseEvent e) {
        JButton clicked = (JButton)e.getSource();
        
        if(clicked.getBackground().equals(Color.blue)) {
            clicked.setBackground(Color.red);
        } else {
            clicked.setBackground(Color.blue);
        }
    }
    
    /**Method that creates a pattern for the user
    * Pre: None
    * Post: Rectangle border is created for the user
    */
    public void createRectangle() {
        for (int i = 0; i < grid.length; i++) {
            grid[i][0].setBackground(Color.red);
        }
        
        for (int i = 0; i < grid.length; i++) {
            grid[49][i].setBackground(Color.red);
        }
        
        for (int i = 0; i < grid.length; i++) {
            grid[i][49].setBackground(Color.red);
        }
        
        for (int i = 0; i < grid.length; i++) {
            grid[0][i].setBackground(Color.red);
        }
    }
    
    /**Method that resets the board
    * Pre: None
    * Post: board is set to all blue, or dead.
    */
    public void resetBoard() {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    grid[i][j].setBackground(Color.blue);
                }
            }
        }
    
    /**Method that creates a pattern for the user
    * Pre: None
    * Post: Glider pattern is created for the user
    */
    public void createGlider() {
        grid[2][0].setBackground(Color.red);
        grid[2][1].setBackground(Color.red);
        grid[2][2].setBackground(Color.red);
        grid[1][2].setBackground(Color.red);
        grid[0][1].setBackground(Color.red);
    }
    
    /**Method that creates a pattern for the user
    * Pre: None
    * Post: Blinker pattern is created for the user
    */
    public void createBlinker() {
        grid[24][23].setBackground(Color.red);
        grid[24][24].setBackground(Color.red);
        grid[24][25].setBackground(Color.red);
    }
    
    /**Method that creates a pattern for the user
    * Pre: None
    * Post: Pulsar pattern is created for the user
    */
    public void createPulsar() {
        grid[24][22].setBackground(Color.red);
        grid[23][23].setBackground(Color.red);
        grid[23][24].setBackground(Color.red);
        grid[23][25].setBackground(Color.red);
        grid[23][26].setBackground(Color.red);
        grid[24][27].setBackground(Color.red);
        grid[25][23].setBackground(Color.red);
        grid[25][24].setBackground(Color.red);
        grid[25][25].setBackground(Color.red);
        grid[25][26].setBackground(Color.red);
    }
    
    
    /**Main method that displays the GUI 
    * Pre: None
    * Post: GUI is displayed for the user
    */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            runGUI();
        }
    });
                
    }

}