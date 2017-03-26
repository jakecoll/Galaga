package mvc.model;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public abstract class Sprite {

    /** The dimensions of the sprite */
    private Dimension mDim;

    /** The position of the sprite */
    protected Point mPos;

    /** The texture for the sprite */
    private BufferedImage mTex;

    /** For Destruction*/
    private KeyframeAnimator mExplosionAnimator;
    private boolean IsExploding;

    /**
     * Constructs a Sprite object. Super class for all sprite subclass (includes Ship, and EnemyFighters)
     * @param initPos
     * @param dim
     * @param texture
     */
    public Sprite(Point initPos, Dimension dim, BufferedImage texture) {
        this.mPos = initPos;
        this.mDim = dim;
        this.mTex = texture;
        this.mExplosionAnimator = new KeyframeAnimator(IsExploding,getExplosionFrames());
        this.IsExploding = false;

    }

    /**
     * Method gets the explosion jpg paths for explosion animation
     * PNGs for explosions from wrath games
     * http://wrathgames.com/blog/2011/12/25/explosion-animation-1/
     * @return
     */
    private String[] getExplosionFrames() {
        String[] image_paths = new String[90];
        String strDirectory = System.getProperty("user.dir");


        for(int i=0; i<90; i++) {
            if (i<9) {
               image_paths[i] = strDirectory + "/src/images/explosion_images/explosion1_000" + Integer.toString(i+1) + ".png";

            }
            if (i>=9){
                image_paths[i] = strDirectory + "/src/images/explosion_images/explosion1_00" + Integer.toString(i+1) + ".png";
            }
        }

        return image_paths;
    }

    /**
     * Method returns the position of the Sprite
     * @return
     */
    public Point getPosition()
    {
        return mPos;
    }

    /**
     * Method returns the dimensions of the Sprite
     * @return
     */
    public Dimension getDim()
    {
        return mDim;
    }

    /**
     * Method draws the Sprite as either its texture (e.g. ship) or as an explosion if it collided with a bullet or other Sprite object
     * @param g
     */
    public void draw (Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        //uses KeyFrameAnimator to change texture to sequence of explosion jpgs
        if (IsExploding) {
            g2d.drawImage(mExplosionAnimator.next(), (int) this.mPos.getX() - 20, (int) this.mPos.getY() - 20, 64,
                    48, null);
        }
        //if not exploding uses the Sprite's texture
        if (!IsExploding) {
            g2d.drawImage(this.mTex, (int) this.mPos.getX(), (int) this.mPos.getY(), (int) this.mDim.getWidth(),
                    (int) this.mDim.getHeight(), null);
        }

    }

    /**
     * Method changes the boolean value to IsExploding so that when repainted it uses explosion KeyFrameAnimator
     * @param deltaTime
     */
    public void destroy(long deltaTime) {
        //mDim = new Dimension(0,0);
        this.IsExploding = true;

    }

    /**
     * Method changes the IsExploding boolean value back to false to change Sprite's back from an explosion.
     * Used on Ship object only
     * @param deltaTime
     */
    public void stopDestruction(long deltaTime) {
        this.IsExploding = false;
    }

    public void move(long deltaTime) {
        this.mPos.translate(1,1);
    }

    /**
     * Method returns boolean value for if the Sprite is exploding
     * @return
     */
    public boolean checkIsExploding() {
        if (this.IsExploding) {
            return true;
        } else { return false; }
    }

    /**
     * Method returns boolean value for if the Sprite exploded (e.g. it has cycled through the full explosion animation)
     * @return
     */
    public boolean checkExploded() {
        if (mExplosionAnimator.checkAnimationFinished()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Resets explosion animation back from its first frame so that game doesn't freeze
     * It is a bit glitchy sometimes. If two bullets hit close together the Sprite may explode twice.
     */
    public void resetAfterExploded() {
        mExplosionAnimator.resetAnimation();
    }


}
