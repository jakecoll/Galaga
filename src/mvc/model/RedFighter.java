package mvc.model;

import images.SpriteTexLoader;

import java.awt.*;
import java.lang.Math;

/**
 * Created by jakecoll on 11/24/16.
 */
public class RedFighter extends EnemyFighter {

    private final static Dimension FIGHTER_DIM = new Dimension(25,20);
    private Point mFormationPosition;

    /**
     * Method constructs a RedFighter object with initial and formation position
     * @param initPos
     * @param formationPosition
     */
    public RedFighter(Point initPos, Point formationPosition) {
        super(initPos, FIGHTER_DIM, SpriteTexLoader.load(SpriteTexLoader.SpriteTex.RED_FIGHTER), formationPosition);
        mFormationPosition = formationPosition;

    }

    /**
     * Methods were in original source code but not used.
     * @param deltaTime
     */
    @Override
    public void move(long deltaTime) {
        this.mPos.translate(5,5);
    }

    public void moveLeft() {
        this.mPos.translate(5,0);
        //    this.mPos = new Point(this.mPos.x + 5, this.mPos.y);
    }
    public void moveRight() {
        this.mPos.translate(-5,0);
        //    this.mPos = new Point(this.mPos.x - 5, this.mPos.y);

    }

    public Dimension getRedFighterDim()
    {
        return FIGHTER_DIM;
    }

}
