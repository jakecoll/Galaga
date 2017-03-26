package mvc.model;

/**
 * Created by jakecoll on 11/21/16.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.*;

public class Bullet extends JPanel{
    private Ship mShip;
    private int XCoord;
    private int YCoord;

    private Dimension mDim = new Dimension(1,7);

    public Bullet()
    {

    }

    /**
     * Constructs a bullet that shoots from a ship object
     * @param ship
     */

    public Bullet(Ship ship)
    {
        mShip = ship;
        XCoord = (int) Math.round(ship.getPosition().getX());
        YCoord = (int) Math.round(ship.getPosition().getY());
    }

    /**
     * move method() moves the bullet at a given speed vertically and repaints
     * @param deltaTime
     */
    public void move(long deltaTime) {
        XCoord = XCoord;
        YCoord = YCoord - 5;

        repaint();
    }

    /**
     * Method draws the bullet
     * @param g
     */
    public void paintComponent(Graphics g)
    {
        g.setColor(Color.WHITE);
        g.drawRect(XCoord + mShip.getShipDim().width/2,YCoord,mDim.width,mDim.height);
    }

    /**
     * Method returns the X coordinate of the bullet
     */
    public int getX()
    {
        return XCoord;
    }

    /**
     * Method returns the Y coordinate of the bullet
     * @return
     */
    public int getY()
    {
        return YCoord;
    }

}
