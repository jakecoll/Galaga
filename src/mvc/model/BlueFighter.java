package mvc.model;

import images.SpriteTexLoader;
import java.awt.*;
import java.lang.Math;

/**
 * Created by jakecoll on 11/24/16.
 */

public class BlueFighter extends EnemyFighter {

    private final static Dimension FIGHTER_DIM = new Dimension(25,20);
    private Point mFormationPosition;


    /**
     * Constructs a blue fighter given its initial starting position and position it ends up in when it moves into formation
     * @param initPos
     * @param formationPosition
     */
    public BlueFighter(Point initPos, Point formationPosition) {
        super(initPos, FIGHTER_DIM, SpriteTexLoader.load(SpriteTexLoader.SpriteTex.BLUE_FIGHTER), formationPosition);

    }

    /**
     * Original code given, but not used. Move method() implemented in super class EnemyFighter
     * @param deltaTime
     */
    @Override
    public void move(long deltaTime) {
        this.mPos.translate(1,1);
    }

    /**
     * following methods were given in original source code but are not used
     */
    public void moveLeft() {
        this.mPos.translate(5,0);
        //    this.mPos = new Point(this.mPos.x + 5, this.mPos.y);
    }
    public void moveRight() {
        this.mPos.translate(-5,0);
        //    this.mPos = new Point(this.mPos.x - 5, this.mPos.y);

    }



}

