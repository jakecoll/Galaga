package mvc.view;

import mvc.controller.Game;
import mvc.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import java.util.Random;


public class GamePanel extends JPanel {

    //Strings for game stats that can be drawn on screen
    private String strScore = "Score: 0";
    private String strLevel = "Level: 1";
    private String strHighScores;

    //If player is on main menu boolean
    private boolean OnMainMenu = true;

    public GamePanel() throws FileNotFoundException {

        this.setPreferredSize(new Dimension(GameFrame.FRAME_DIM.width, GameFrame.FRAME_DIM.height));

    }

    /**
     * Sets the score string so that it can be update when GamePanel redrawn
     * @param updatedScore
     */
    public void setScore(int updatedScore)
    {
        strScore = "Score: " + Integer.toString(updatedScore);
    }

    /**
     * Updates the level string so that it can be updated when GamePanel redrawn
     * @param updateLevel
     */
    public void setLevel(int updateLevel) { strLevel = "Level: " + Integer.toString(updateLevel); }

    /**
     * Updates the Menu state boolean
     * @param bool
     */
    public void setMenuState(boolean bool) {
        OnMainMenu = bool;
    }

    /**
     * Updates the high scores string so that it can be draw on game panel
     * @param highScores
     */
    public void setStrHighScores(String highScores) { strHighScores = highScores; }

    @Override
    public void paintComponent(Graphics g){
        // Call the super paintComponent of the panel
        super.paintComponent(g);

        //Draw a black background
        g.setColor(Color.black);
        g.fillRect(0, 0, GameFrame.FRAME_DIM.width,  GameFrame.FRAME_DIM.height);

        Graphics2D g2d = (Graphics2D) g;

        //Always draw the Starts background because it looks cool
        ArrayList<Star> stars = Game.getDrawableStars();

        for(Star star : stars) {
            star.paintComponent(g);
        }

        //If player is on main menu only draw play instructions and high scores
        if (OnMainMenu)
        {
            Font gameFont = new Font("Monospaced", Font.BOLD, 24);
            g2d.setColor(Color.RED);
            g2d.setFont(gameFont);

            g2d.drawString("PRESS <ENTER> TO PLAY",300,450);
            g2d.drawString("High Scores",300,70);

            int y = 70;

            for (String line : strHighScores.split("\n"))
            {
                y += 30;
                g2d.drawString(line,300, y);
            }

        }
        //Else GamePanel draws the Game state
        else {

            Font gameFont = new Font("Monospaced", Font.BOLD, 24);
            g2d.setColor(Color.RED);
            g2d.setFont(gameFont);

            g2d.drawString(strScore, 10, 30);
            g2d.drawString(strLevel, GameFrame.FRAME_DIM.width - 120, 30);

            //Start redrawing all the objects of the game.
            ArrayList<Sprite> sprites = Game.getDrawableSprites();
            ArrayList<Bullet> bullets = Game.getDrawableBullets();
            ArrayList<EnemyFighter> enemySprites = Game.getDrawableEnemySprites();
            ArrayList<EnemyBullet> enemyBullets = Game.getDrawableEnemyBullets();

            for (Sprite sprite : sprites) {
                sprite.draw(g);
            }

            for (Sprite sprite : enemySprites) {
                sprite.draw(g);
            }

            for (Bullet bullet : bullets) {
                bullet.paintComponent(g);
            }

            for (EnemyBullet enemyBullet : enemyBullets) {
                enemyBullet.paintComponent(g);
            }

        }
    }


}
