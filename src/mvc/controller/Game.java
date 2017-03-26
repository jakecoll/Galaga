package mvc.controller;

import mvc.model.*;
import mvc.view.GameFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;


public class Game implements Runnable, KeyListener {

    /** Represents the JFrame for the game */
    private GameFrame mGameFrame;

    /** Represents the Animation delay between frames */
    public final static int DRAW_DELAY = 45;

    /** The thread that handles the render loop for the game */
    private Thread mRenderThread;

    /** Represents the ship in the game */
    private Ship mShip;

    /** boolean value for if player is holding down left and right buttons */
    private boolean mShipIsMovingRight;
    private boolean mShipIsMovingLeft;

    /** player lives ship in bottom left corner*/
    private Ship mLifeShip1;
    private Ship mLifeShip2;
    private Ship mLifeShip3;

    private Player mPlayer;

    private EnemyController mEnemyController;
    private RedFighter mRedFighter;
    private BlueFighter mBlueFighter;

    //stores number of EnemyFigers not in formation
    private int EnemiesNotInFormation;

    //variable for level information
    private Level level;
    private int nLevel = 0;

    /** List of Sprites that need to be rendered  */
    private static ArrayList<Sprite> _sprites = new ArrayList<Sprite>();
    private static ArrayList<EnemyFighter> _enemies = new ArrayList<EnemyFighter>();
    private static ArrayList<Bullet> _bullets = new ArrayList<Bullet>();
    private static ArrayList<EnemyBullet> _enemyBullets = new ArrayList<EnemyBullet>();
    private static ArrayList<Star> _stars = new ArrayList<Star>();

    //increments on start of level so blue fighters enter one after the other in
    private int blueFighterLeftPosCounter = 0;
    private int blueFighterRightPosCounter = 0;

    //boolean indicates if player is on main menu
    private boolean OnMainMenu;

    //Object deals with getting and updating high scores
    private HighScore mHighScore;

    public Game() throws FileNotFoundException {

        //start on main menu
        this.OnMainMenu = true;

        //get high scores file and update
        this.mHighScore = new HighScore();

        //declare and initialize the player
        this.mPlayer = new Player();
        this.mGameFrame = new GameFrame(this);
        this.mGameFrame.pack();
        this.mShip = new Ship(new Point(mGameFrame.frameWidth()/2,mGameFrame.frameHeight()-100));

        this.mEnemyController = new EnemyController(this.mShip);

        //sets the ship textures that show player's number of lives
        this.mLifeShip1 = new Ship(new Point(10,mGameFrame.frameHeight()-mShip.getShipDim().height - 5));
        this.mLifeShip2 = new Ship(new Point(10 + mShip.getShipDim().width + 5,mGameFrame.frameHeight()-mShip.getShipDim().height - 5));
        this.mLifeShip3 = new Ship(new Point(10 + 2*(mShip.getShipDim().width + 5),mGameFrame.frameHeight()-mShip.getShipDim().height - 5));

        //initializes booleans for if user is holding down left/right buttons
        this.mShipIsMovingRight = false;
        this.mShipIsMovingLeft = false;

        //adds some sprites
        Game._sprites.add(mShip);

        Game._sprites.add(mLifeShip1);
        Game._sprites.add(mLifeShip2);
        Game._sprites.add(mLifeShip3);

        //Sets high scores on main menue
        mGameFrame.setHighScores(mHighScore.toString());

        //Produces the stars that move in the background
        startStarsOnBackground();

    }

    /**
     * Starts the thread that will handle the render loop for the game
     */
    private void startRenderLoopThread() {
        //Check to make sure the render loop thread has not begun
        if (this.mRenderThread == null) {
            //All threads that are created in java need to be passed a Runnable object.
            //In this case we are making the "Runnable Object" the actual game instance.
            this.mRenderThread = new Thread(this);
            //Start the thread
            this.mRenderThread.start();
        }
    }

    /**
     * This represents the method that will be called for a Runnable object when a thread starts.
     * In this case, this run method represents the render loop.
     */
    public void run() {

        //Make this thread a low priority such that the main thread of the Event Dispatch is always is
        //running first.
        this.mRenderThread.setPriority(Thread.MIN_PRIORITY);

        //Get the current time of rendering this frame
        long elapsedTime = System.currentTimeMillis();

        long currentTime = 0;
        long lastTime = 0;
        long deltaTime = 0;

        // this thread animates the scene
        while (Thread.currentThread() == this.mRenderThread) {

            currentTime = System.currentTimeMillis();

            if(lastTime == 0){
                lastTime = currentTime;
                deltaTime = 0;
            }else {
                deltaTime = currentTime - lastTime;
                lastTime = currentTime;
            }

            /************* Update game HERE
             * - Move the game models
             * - Check for collisions between the bullet, or fighters and the ship
             * - Check whether we should move to a new level potentially.
             */

            //if game finishes level three then end game and return to menu after updating high scores
            if (nLevel > 3 & !OnMainMenu) {
                try {
                    mHighScore.updateHighScores(mPlayer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mGameFrame.setHighScores(mHighScore.toString());
                OnMainMenu = true;
                mGameFrame.updateMenuState(true);
            }

            //If player not on main menu then update the game state
            if (!OnMainMenu)
            {
                //if all enemies destroyed then move on to next level
                if (_enemies.size() == 0) {
                    nLevel++;
                    startLevel();
                    mGameFrame.updateLevel(nLevel);
                }

                //update score
                mGameFrame.updateScore(mPlayer.getScore());

                //update EnemyFighters
                _enemies = mEnemyController.update(deltaTime,_enemies);
                mEnemyController.setLevel(nLevel);
                for (EnemyBullet enemyBullet : mEnemyController.getEnemyBullets())
                {
                    _enemyBullets.add(enemyBullet);
                }
            }

            //update Stars to it appears that they are moving
            for (Star star : _stars) {
                //move start back to top of screen if it reaches bottom
                if (star.getY() > GameFrame.FRAME_DIM.height) {
                    star.cycle(deltaTime);
                } else {
                    star.move(deltaTime);
                }

            }

            /** Moves ship left or right but prevents user from moving Ship off screen*/
            if (mShipIsMovingRight) {
                if (mShip.getPosition().getX()< mGameFrame.frameWidth() - mShip.getDim().width) {
                    mShip.moveRight();
                }
            }
            if (mShipIsMovingLeft) {
                if (mShip.getPosition().getX()>0) {
                    mShip.moveLeft();
                }
            }

            /**
             * Checks for collisions for all Sprites and updates game state
             */
            try {
                checkCollisions(_bullets, _enemyBullets, _enemies, mShip, deltaTime);
            } catch (IOException e) {
                e.printStackTrace();
            }


            //Redraw the game frame with to visually show the updated game state.
            this.mGameFrame.draw();

            try {
                /** We want to ensure that the drawing time is at least the DRAW_DELAY we specified. */
                 elapsedTime += DRAW_DELAY;
                 Thread.sleep(Math.max(0, elapsedTime - currentTime));
            } catch (InterruptedException e) {
                //If an interrupt occurs then you can just skip this current frame.
                continue;
            }
        }
    }

    /***
     * Generates all the drawable sprites for the game currently
     * @return an arraylist of all the drawable sprites in the game
     */
    public static ArrayList<Sprite> getDrawableSprites() {
        return new ArrayList<Sprite>(_sprites);
    }

    public static ArrayList<Bullet> getDrawableBullets() {
        return new ArrayList<Bullet>(_bullets);
    }

    public static ArrayList<EnemyFighter> getDrawableEnemySprites() {
        return new ArrayList<EnemyFighter>(_enemies);
    }

    public static ArrayList<Star> getDrawableStars() {
        return new ArrayList<Star>(_stars);
    }

    public static ArrayList<EnemyBullet> getDrawableEnemyBullets() { return new ArrayList<EnemyBullet>(_enemyBullets); }



    @Override
    public void keyPressed(KeyEvent e) {

        int nKey = e.getKeyCode();

        switch (nKey) {
            case KeyEvent.VK_LEFT:
                this.mShipIsMovingLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                this.mShipIsMovingRight = true;
                break;

            case KeyEvent.VK_SPACE:
                if (!mShip.checkIsExploding()) {
                    Bullet bullet = new Bullet(mShip);
                    Game._bullets.add(bullet);
                }
                break;

            case KeyEvent.VK_ENTER:
                //if you press enter button on main menu then the game will start
                if (OnMainMenu) {
                    mPlayer = new Player();
                    nLevel = 0;
                    _enemies = new ArrayList<EnemyFighter>();
                    OnMainMenu = false;
                    mGameFrame.updateMenuState(false);
                }
                break;

            default:
                System.out.println("Pressing the key: " + KeyEvent.getKeyText(nKey));
                break;
        }
    }
    public void keyReleased(KeyEvent e) {
        int nKey = e.getKeyCode();

        switch (nKey) {
            case KeyEvent.VK_LEFT:
                this.mShipIsMovingLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                this.mShipIsMovingRight = false;
                break;

        }

    }
    public void keyTyped(KeyEvent e) {}

    /**
     * Method returns boolean value for if game is on main menu
     * @return
     */
    public boolean checkOnMainMenu()
    {
        if (OnMainMenu) {return true; }
        else {return false; }
    }

    /**
     * Method initializes the background starts
     */
    public void startStarsOnBackground() {
        for (int i=0; i<25; i++) {
            Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.WHITE};

            for (Color color : colors) {
                Random xRand = new Random();
                Random yRand = new Random();

                int x = (int) xRand.nextInt(GameFrame.FRAME_DIM.width);
                int y = (int) yRand.nextInt(GameFrame.FRAME_DIM.height);

                Star star = new Star(x,y,color);
                _stars.add(star);
            }
        }

    }

    /**
     * Method checks for various types of collisions and updates game state (e.g. EnemyFighter and bullet, Fighter and SHip, Ship and enemy bullet)
     * @param bullets
     * @param enemyBullets
     * @param enemies
     * @param ship
     * @param deltaTime
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void checkCollisions(ArrayList<Bullet> bullets, ArrayList<EnemyBullet> enemyBullets,ArrayList<EnemyFighter> enemies, Ship ship,long deltaTime) throws FileNotFoundException, IOException
    {

        //If user ship has gone through explosion aninmation then deduct one life and restore ship texture
        if(ship.checkExploded())
        {
            lostLife();
            ship.stopDestruction(deltaTime);
            ship.resetAfterExploded();
        }

        //move the bullets fired by the ship
        for (int w=0; w<bullets.size(); w++)
        {
            bullets.get(w).move(deltaTime);
        }

        Point shipXY = ship.getPosition();
        Dimension shipDim = ship.getDim();

        //variables provide coordinates for boundary of ship
        int shipXLeft = (int) Math.round(shipXY.getX() - (shipDim.width/2));
        int shipXRight = (int) Math.round(shipXY.getX() + (shipDim.width/2));
        int shipYTop = (int) Math.round(shipXY.getY() - (shipDim.height/2));
        int shipYBottom = (int) Math.round(shipXY.getY());

        for (int j=0; j< enemies.size(); j++) {

            Point spriteXY = enemies.get(j).getPosition();
            Dimension spriteDim = enemies.get(j).getDim();

            //variables for boundary of enemy fighter
            int enemyXLeft = (int) Math.round(spriteXY.getX() - (spriteDim.width/2));
            int enemyXRight = (int) Math.round(spriteXY.getX() + (spriteDim.width/2));
            int enemyYTop = (int) Math.round(spriteXY.getY() - spriteDim.height);
            int enemyYBottom = (int) Math.round(spriteXY.getY());

            //If ship and enemy fighter collide then destroy both
            if (ship.getPosition().getX()>=enemyXLeft && ship.getPosition().getX()<=enemyXRight && shipYTop<=enemyYBottom && shipYBottom>=enemyYTop)
            {
                ship.destroy(deltaTime);
                enemies.get(j).destroy(deltaTime);
            } else if (enemies.get(j).checkExploded()) {

                mPlayer.addPoints(10*nLevel);
                enemies.remove(j);
            }

            for(int i=0; i<bullets.size(); i++)
            {

                int x = bullets.get(i).getX();
                int y = bullets.get(i).getY();

                //if bullet from ship hits boundary of enemy fighter then destroy it
                if (x>=enemyXLeft && x<=enemyXRight && y<=enemyYBottom && y>=enemyYTop)
                {

                    enemies.get(j).destroy(deltaTime);
                    bullets.remove(i);
                }

            }
        }

        //If bullet from enemy fighter hits then ship the destroy ship
        for (int k = 0; k<enemyBullets.size(); k++)
        {
            enemyBullets.get(k).move(deltaTime);

            int xE = enemyBullets.get(k).getX();
            int yE = enemyBullets.get(k).getY();

            if (xE>=shipXLeft && xE<=shipXRight && yE<=shipYBottom && yE>=shipYTop)
            {
                ship.destroy(deltaTime);
                enemyBullets.remove(k);
            }

        }

    }

    /**
     * Method for updating game state if player ship is destroyed
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void lostLife() throws FileNotFoundException, IOException
    {
        //deducts one life from player
        mPlayer.loseLife();

        //updates Sprites the represent lives of player
        if (mPlayer.remainingLives()==2) {
            _sprites.remove(_sprites.indexOf(mLifeShip3));
        } else if (mPlayer.remainingLives()==1) {
            _sprites.remove(_sprites.indexOf(mLifeShip2));
        } else if (mPlayer.remainingLives()==0)
        {
            //if player out of lives then end game (update high scores, return to main menu)
            mHighScore.updateHighScores(mPlayer);
            mGameFrame.setHighScores(mHighScore.toString());
            OnMainMenu = true;
            mGameFrame.updateMenuState(true);
        }
    }

    /**
     * Method for starting a new level and create new enemy sprites to move on screen
     */
    public void startLevel() {
        level = new Level(mGameFrame, nLevel);
        int[][] blueFighterPositions = level.getBlueFighterPositions();
        int[][] redFighterPositions = level.getRedFighterPositions();

        int blueSplitCount = level.getTotalBlueFighters()/2;

        //creates the blue fighters to enter from left and right of screen
        for (int i=0; i<blueSplitCount; i++) {
            blueFighterLeftPosCounter = blueFighterLeftPosCounter - 10;
            Point leftFighterStartPosition = new Point(blueFighterLeftPosCounter,blueFighterLeftPosCounter);
            BlueFighter leftBlueFighter = new BlueFighter(leftFighterStartPosition,new Point(blueFighterPositions[i][0],blueFighterPositions[i][1]));
            leftBlueFighter.setEntersFromLeft(true);
            leftBlueFighter.setStartPos((int) Math.round(leftFighterStartPosition.getX()), (int) Math.round(leftFighterStartPosition.getY()));
            leftBlueFighter.setFormationPosition(blueFighterPositions[i][0],blueFighterPositions[i][1]);
            Game._enemies.add(leftBlueFighter);

            blueFighterRightPosCounter = blueFighterRightPosCounter + 10;
            Point rightFighterStartPosition = new Point(GameFrame.FRAME_DIM.width+blueFighterRightPosCounter,blueFighterLeftPosCounter);
            BlueFighter rightBlueFighter = new BlueFighter(rightFighterStartPosition, new Point(blueFighterPositions[i+blueSplitCount][0],blueFighterPositions[i+blueSplitCount][1]));
            rightBlueFighter.setEntersFromLeft(false);
            rightBlueFighter.setStartPos( (int) Math.round(rightFighterStartPosition.getX()), (int) Math.round(rightFighterStartPosition.getY()));
            rightBlueFighter.setFormationPosition(blueFighterPositions[i+blueSplitCount][0],blueFighterPositions[i+blueSplitCount][1]);
            Game._enemies.add(rightBlueFighter);
        }

        //creates the red fighters that enter from top of screen
        for (int j=0; j<level.getTotalRedFighters(); j++)
        {
            Point startPosition = new Point(redFighterPositions[j][0],redFighterPositions[j][1]-750);

            RedFighter redFighter = new RedFighter(startPosition,new Point(redFighterPositions[j][0],redFighterPositions[j][1]));

            redFighter.setStartPos( (int) Math.round(startPosition.getX()), (int) Math.round(startPosition.getY()));
            redFighter.setFormationPosition(redFighterPositions[j][0],redFighterPositions[j][1]);

            Game._enemies.add(redFighter);
        }
    }


    public static void main(String args[]) {

            EventQueue.invokeLater(new Runnable() { // uses the Event dispatch thread from Java 5 (refactored)
                public void run() {
                    try {
                        //Construct the game controller
                        Game game = new Game();
                        //Start the render loop for the game
                        game.startRenderLoopThread();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

    }

}
