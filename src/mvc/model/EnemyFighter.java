package mvc.model;

import mvc.view.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by jakecoll on 11/26/16.
 */
public class EnemyFighter extends Sprite {

    //values center of looping circle and increment radians for point on circle
    private Point mLoopCenter = new Point(375,325);
    private double theta = 0.0;

    //booleans for what state the EnemyFighter is in
    private boolean IsLooping;
    private boolean IsStarting;
    private boolean InFormation;
    private boolean IsAttacking;
    private boolean HasFired;

    //integer values for starting and formation x,y positions
    private int nXstart;
    private int nYstart;
    private int nXFormation;
    private int nYFormation;

    //boolean for if fighter enters from the left or right of screen when a level starts or returning from attack run
    private boolean EntersFromLeft;

    //Point for where Fighter's formation point is
    private Point mFormationPosition;

    //Integer used for x coordinate translation when fighter is on attack
    private int nAttackXMove;

    /**
     * Constructs EnemyFighter object. This is a super class for Red and Blue Fighters to make handling their behavior easier
     * @param initPos
     * @param mDim
     * @param texture
     * @param formationPosition
     */
    public EnemyFighter(Point initPos, Dimension mDim, BufferedImage texture, Point formationPosition) {

        super(initPos,mDim,texture);

        //sets formation position in grid
        mFormationPosition = formationPosition;

        //sets appropriate boolean values
        this.EntersFromLeft = true;
        this.IsLooping = false;
        this.IsStarting = true;
        this.InFormation = false;
        this.IsAttacking = false;
        this.HasFired = false;

    }

    /**
     * Changes the EnemyFighters position by reference variables for x and y
     * @param deltaTime
     * @param x
     * @param y
     */
    public void move(long deltaTime, int x, int y) {
        if (EntersFromLeft) {
            this.mPos.translate(x,y);
        } else if (!EntersFromLeft) {
            this.mPos.translate(-x,y);
        }

    }

    /**
     * Method moves ship in a circle. The variable radians effects how fast the fighter loops through circle
     * @param deltaTime
     * @param radians
     */
    public void loop(long deltaTime, int radians) {
        theta = theta + Math.toRadians(radians);

        if (EntersFromLeft) {
            int x = (int) (15*Math.cos(theta));
            int y = (int) (-15*Math.sin(theta));

            this.mPos.translate(x, y);
        }

        if (!EntersFromLeft) {
            int x = (int) (-15*Math.cos(theta));
            int y = (int) (15*Math.sin(theta));

            this.mPos.translate(x, -y);
        }

    }

    /**
     * Returns boolean value if the fighter is currently in its fancy movement loop
     * @return
     */
    public boolean checkIfLooping()
    {
        if (IsLooping) {
            return true;
        } else { return false; }
    }

    /**
     * Returns boolean value for if the fighter is on its initial linear path into screen before loop
     * @return
     */
    public boolean checkIfStarting() {
        if (IsStarting) {
            return true;
        } else {return false; }
    }

    /**
     * Returns boolean value for if the fighter is in its formation position
     * @return
     */
    public boolean checkInFormation() {
        if(InFormation) {
            return true;
        } else { return false; }
    }

    /**
     * Returns boolean value for if the fighter has left formation on attack run at player's ship
     * @return
     */
    public boolean checkIsAttacking() {
        if(IsAttacking) {
            return true;
        } else { return false; }
    }

    /**
     * Returns boolean value if the fighter has already fired a shot at the player's ship on one instance of an attack
     * @return
     */
    public boolean checkHasFired() {
        if (HasFired) {
            return true;
        } else { return false; }
    }

    /**
     * Sets boolean value for if the ship is in its fancy loop
     * @param bool
     */
    public void setIsLooping(boolean bool) {
        IsLooping = bool;
    }

    /**
     * Sets boolean value for if the ship is on its initial linear path onto screen
     * @param bool
     */
    public void setIsStarting(boolean bool) {
        IsStarting = bool;
    }

    /**
     * Sets boolean value for if the ship has fired on one instance of an attack run
     * @param bool
     */
    public void setHasFired(boolean bool) { HasFired = bool; }

    /**
     * Method sets EnemyFighter to leave formation and attack the player's ship
     * Fighter will head toward the position of the player's ship at the moment it is ordered to attack
     * @param ship
     */
    public void setAttack(Ship ship) {
        IsAttacking = true;
        InFormation = false;

        if(ship.getPosition().getX()-mPos.getX()<0 || ship.getPosition().getX()-mPos.getX()>0 )
        {
            double dX = ship.getPosition().getX()-mPos.getX();
            double dY = (ship.getPosition().getY()-mPos.getY())/5;
            nAttackXMove = (int) Math.round(dX/dY);

        }
        else if (ship.getPosition().getX()-mPos.getX()==0) {
            nAttackXMove = 0;
        }

    }

    /**
     * Method changes the position of the fighter to move dynamically towards its formation position
     */
    public void moveToFormationPos() {

        if(nXFormation-mPos.getX()<0)
        {
            double dX = (nXFormation-mPos.getX())/5;
            double dY = nYFormation-mPos.getY();
            int y = (int) Math.round(dY/dX) * -1;

            this.mPos.translate(-5,y);
        } else if (nXFormation-mPos.getX()>0) {
            double dX = (nXFormation-mPos.getX())/5;
            double dY = nYFormation-mPos.getY();
            int y = (int) Math.round(dY/dX);

            this.mPos.translate(5,y);

        } else if (nXFormation-mPos.getX()==0 && nYFormation-mPos.getY()==0) {
            this.mPos = new Point(nXFormation, nYFormation);
            InFormation = true;
        } else if (nXFormation-mPos.getX()==0)
        {
            this.mPos.translate(0,5);
        }

    }

    /**
     * Method continues to move fighter on attack run until off screen then resets it to its start position
     */
    public void attack() {

        if (mPos.getY() > GameFrame.FRAME_DIM.height)
        {
            this.mPos = new Point(nXstart, nYstart);
            IsStarting = true;
            IsAttacking = false;
            theta = 0.0;

        } else {
            this.mPos.translate(nAttackXMove,5);
        }
    }

    /**
     * Sets boolean value for EnemyFighters (just used on blue fighters) who enter from the left.
     * Used to allow reuse of enter and looping code but change certain behavior based on the boolean
     * @param bool
     */
    public void setEntersFromLeft(boolean bool)
    {
        EntersFromLeft = bool;
    }

    /**
     * Sets the X,Y position of where the EnemyFighter starts from
     * @param x
     * @param y
     */
    public void setStartPos(int x, int y) {
        nXstart = x;
        nYstart = y;

    }

    /**
     * Sets the formation position for the EnemyFighter
     * @param x
     * @param y
     */
    public void setFormationPosition(int x, int y) {
        nXFormation = x;
        nYFormation = y;
    }

    /**
     * Returns the formation position assigned to the EnemyFighter
     * @return
     */
    public Point getFormationPosition() {
        return mFormationPosition;
    }
}
