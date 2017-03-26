package mvc.model;

/**
 * Created by jakecoll on 11/21/16.
 */
public class Player {

    private String strName;
    private int nLives;
    private int nScore;

    /**
     * Creates a player object with a predefined name and sets lives to 3 and score to 0
     */
    public Player () {
        strName = "Player1";
        nLives = 3;
        nScore = 0;
    }

    /**
     * Method creates a player object with a name based as a String value
     * Meant to implement way for player to enter name but didn't get to it
     * @param name
     */
    public Player (String name) {
        strName = name;
        nLives = 3;
        nScore = 0;
    }

    /**
     * Method for changing name of player
     * @param name
     */
    public void setName(String name)
    {
        strName = name;
    }

    /**
     * Method returns the name of the player object
     * @return
     */
    public String getName()
    {
        return strName;
    }

    /**
     * Method reduces player's lives by 1
     */
    public void loseLife()
    {
        nLives = nLives -1;
    }

    /**
     * Method returns the number of lives player has remaining
     * @return
     */
    public int remainingLives()
    {
        return nLives;
    }

    /**
     * Method adds points to the player score
     * @param points
     */
    public void addPoints(int points)
    {
        nScore = nScore + points;
    }

    /**
     * Method returns the player's score
     * @return
     */
    public int getScore()
    {
        return nScore;
    }

}
