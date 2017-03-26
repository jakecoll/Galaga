package mvc.model;

import mvc.view.GameFrame;

/**
 * Created by jakecoll on 11/28/16.
 */
public class Level {

    private int nLevel;

    private int[][] arrBlueFighterPositions;
    private int[][] arrRedFighterPositions;

    private int nTotalBlueFighters;
    private int nTotalRedFighters;

    private int leftBuffer = 30;
    private int rightBuffer = 30;

    private int nPositionIncrement;

    /**
     * Constructs a level object for deciding how many EnemyFighters are created and where they being and their formation positions
     * @param gameFrame
     * @param level
     */
    public Level(GameFrame gameFrame, int level) {

        nLevel = level;
        nPositionIncrement = 30;

        if (nLevel == 1)
        {
            nTotalBlueFighters =20;
            nTotalRedFighters = 16;
        } else if (level == 2) {
            nTotalBlueFighters = 24;
            nTotalRedFighters = 18;
        } else if (level == 3) {
            nTotalBlueFighters = 32;
            nTotalRedFighters = 20;
        }

        arrBlueFighterPositions = new int[nTotalBlueFighters][2];
        arrBlueFighterPositions = getPositions(arrBlueFighterPositions,nTotalBlueFighters,gameFrame,0.3,0.25);

        resetBuffers();
        arrRedFighterPositions = new int[nTotalRedFighters][2];
        arrRedFighterPositions = getPositions(arrRedFighterPositions,nTotalRedFighters,gameFrame,0.2,0.15);


    }

    /**
     * Method calculates and returns the start positions of the EnemyFighters for a given level
     * @param fighterPositions
     * @param numberOfFighters
     * @param gameFrame
     * @param firstRow
     * @param secondRow
     * @return
     */
    private int[][] getPositions(int[][] fighterPositions, int numberOfFighters, GameFrame gameFrame, double firstRow, double secondRow) {


        for(int i=1; i<=numberOfFighters; i++)
        {
            if (i<=numberOfFighters/2 && i%2 != 0)
            {
                fighterPositions[i-1][0] = gameFrame.frameWidth()/2 - leftBuffer;
                fighterPositions[i-1][1] = (int) Math.round(gameFrame.frameHeight()*firstRow);

            }
            else if (i<=numberOfFighters/2 && i%2 == 0)
            {
                fighterPositions[i-1][0] = gameFrame.frameWidth()/2 - (leftBuffer);
                fighterPositions[i-1][1] = (int) Math.round(gameFrame.frameHeight()*secondRow);
                leftBuffer = leftBuffer+nPositionIncrement;
            }
            else if (i > numberOfFighters/2 && i%2 != 0)
            {
                fighterPositions[i-1][0] = gameFrame.frameWidth()/2 + rightBuffer;
                fighterPositions[i-1][1] = (int) Math.round(gameFrame.frameHeight()*firstRow);
            }
            else if (i > numberOfFighters/2 && i%2 == 0)
            {
                fighterPositions[i-1][0] = gameFrame.frameWidth()/2 + (rightBuffer);
                fighterPositions[i-1][1] = (int) Math.round(gameFrame.frameHeight()*secondRow);
                rightBuffer = rightBuffer+nPositionIncrement;
            }
        }

        return fighterPositions;

    }

    /**
     * method returns the positions for blue fighters
     * @return
     */
    public int[][] getBlueFighterPositions()
    {
        return arrBlueFighterPositions;
    }

    /**
     * Method returns the starting positions of red fighters
     * @return
     */
    public int[][] getRedFighterPositions()
    {
        return arrRedFighterPositions;
    }

    /**
     * Method returns the total number of blue fighters for level object
     * @return
     */
    public int getTotalBlueFighters() {
        return nTotalBlueFighters;
    }

    /**
     * Method return the total number of blue fighter for level object
     * @return
     */
    public int getTotalRedFighters() {
        return nTotalRedFighters;
    }

    /**
     * Method resets buffer increment so that when another set of fighters is created their positions are not pushed off screen
     */
    private void resetBuffers() {
        leftBuffer = 30;
        rightBuffer = 30;
    }
}
