package mvc.model;

import mvc.view.GameFrame;

import java.awt.*;
import javax.swing.*;

/**
 * Created by jakecoll on 11/26/16.
 */
public class Star extends JPanel {

    private Ship mShip;
    private int XCoord;
    private int YCoord;
    private Color mColor;

    private Dimension mDim = new Dimension(1,1);

    /**
     * Constructs a Star object with a given position and color
     * @param x
     * @param y
     * @param color
     */
    public Star (int x, int y, Color color)
    {
        XCoord = x;
        YCoord = y;
        mColor = color;
    }

    /**
     * Moves the Start object vertically by 1
     * @param deltaTime
     */
    public void move(long deltaTime) {

        YCoord = YCoord + 1;

        repaint();
    }

    /**
     * Sets start objects Y coordinate to 0
     * @param deltaTime
     */
    public void cycle(long deltaTime) {
        YCoord = 0;

        repaint();
    }

    /**
     * Draws the Star object
     * @param g
     */
    public void paintComponent(Graphics g)
    {
        g.setColor(mColor);
        g.drawRect(XCoord,YCoord,mDim.width,mDim.height);
    }

    /**
     * Returns X coordinate of Star object
     * @return
     */
    public int getX()
    {
        return XCoord;
    }

    /**
     * Returns the Y coordinate of Star objects
     * @return
     */
    public int getY()
    {
        return YCoord;
    }

}
