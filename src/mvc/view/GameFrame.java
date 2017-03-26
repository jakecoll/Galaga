package mvc.view;

import mvc.controller.Game;
import mvc.model.Sprite;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;


public class GameFrame extends JFrame {

    /** The window dimensions for the Frame*/
    /** origin dimension is 1100 x 900. Changing to fit on screen*/
    public static final Dimension FRAME_DIM = new Dimension(900, 700);

    /* The Game panel for the game **/
    private GamePanel mPanel;

    /* The controller for the game **/
    private Game mController;

    public GameFrame(Game controller) {

        //Enable the frame to be notified if the user clicks on the close button
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);

        //Set the game controller for the Frame
        this.mController = controller;

        try {
            // Try to initialize the game panel
            initPanel();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Initialize the frame
        this.setTitle("Galaga");
        this.setSize(FRAME_DIM);
        this.setFocusable(true);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
    }




    /**
     * returns the frame heights and widths
     * @return
     */

    public int frameHeight()
    {
        return FRAME_DIM.height;
    }

    public int frameWidth()
    {
        return FRAME_DIM.width;
    }
    /**
     * Draws the current game information to the window
     */

    public void updateScore(int score) {
        mPanel.setScore(score);
    }

    public void updateLevel(int newLevel)
    {
        mPanel.setLevel(newLevel);
    }

    public void setHighScores(String highScores) {
        mPanel.setStrHighScores(highScores);
    }

    /**
     * Updates the GamePanel state to reflect if Game is on main menu or in actual game
     * @param bool
     */
    public void updateMenuState(boolean bool)
    {
        mPanel.setMenuState(bool);
    }


    public void draw() {
        this.mPanel.repaint();
    }
    private void initPanel() throws Exception {

        //Set the layout for the panel
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(new BorderLayout());

        //Create a new GamePanel for the controller
        this.mPanel = new GamePanel();

        //Let the controller be the listener for the all actions that happen on the game panel
        this.addKeyListener(this.mController);


        //Add the game panel to the window's content panel.
        contentPane.add(mPanel);


    }
    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        //Exit the game if the window closed button is closed.
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }

}