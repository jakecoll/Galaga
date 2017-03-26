package mvc.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class KeyframeAnimator {


    private class KeyFramePair {

        private String mFilename;
        private BufferedImage mImage;

        private KeyFramePair(String filename) {
            this.mFilename = filename;
        }

        public String getFilename() {
            return mFilename;
        }

        public BufferedImage getImage() {

            if (mImage == null) {
                try {
                    //Read in the texture file (this is one thing I had to change. How to get file location

                    //this.mImage = ImageIO.read(getClass().getResource(mFilename));
                    this.mImage = ImageIO.read(new File(mFilename));
                    return mImage;

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
                return null;
            } else {
                return mImage;
            }
        }

    }

    /**
     * All the frames for the animator
     */
    private ArrayList<KeyFramePair> mFrames;

    /**
     * The current frame of the animation
     */
    private int mCurrentFrame;

    /**
     * Represents whether the animation should loop from start to end and vice versa
     */
    private boolean mShouldLoop;

    /**
     * Represents whether the animation should loop from start to end and vice versa
     */
    private boolean mIsCountingUp;

    /**
     * Represent whether animation has cycled through the end;
     */
    private boolean mAnimationFinished;

    public KeyframeAnimator(boolean shouldLoop, String[] frames) {

        this.mFrames = new ArrayList<KeyFramePair>(frames.length);
        this.mCurrentFrame = 0;
        this.mShouldLoop = shouldLoop;
        this.mIsCountingUp = true;
        this.mAnimationFinished = false;

        for (String framePath : frames) {

            this.mFrames.add(new KeyFramePair(framePath));
        }
    }

    public BufferedImage next() {

        if (mShouldLoop) {
            if (mCurrentFrame == mFrames.size() && mIsCountingUp == true) {
                mAnimationFinished = true;
                mCurrentFrame = mFrames.size() - 1;
                mIsCountingUp = !mIsCountingUp;
            } else if (mCurrentFrame < 0 && mIsCountingUp == false) {
                mIsCountingUp = !mIsCountingUp;
                mCurrentFrame = 0;
            }

        } else {
            if (mCurrentFrame == mFrames.size()) {
                mAnimationFinished = true;
                mCurrentFrame = 0;
            }
        }

        BufferedImage image = mFrames.get(mCurrentFrame).getImage();


        if (mIsCountingUp) {
            mCurrentFrame++;
        } else {
            mCurrentFrame--;

        }
        return image;
    }

    /**
     * Method returns boolean value for if explosion animation has cycled through 1 time
     * @return
     */
    public boolean checkAnimationFinished()
    {
        if (mAnimationFinished) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Resets boolean value for if explosion animation has occurred. Used when player has another life so that ship texture is restored.
     */
    public void resetAnimation() {
        mAnimationFinished = false;
    }

    public void reset() {
        mCurrentFrame = 0;
    }
}
