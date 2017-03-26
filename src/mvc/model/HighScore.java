package mvc.model;

/**
 * Created by jakecoll on 12/7/16.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HighScore {

    private ArrayList<String[]> highScores = new ArrayList<String[]>();
    private File highScoreFile;

    /**
     * Creates HighScore object for sorting and saving high scores
     * @throws FileNotFoundException
     */
    public HighScore() throws FileNotFoundException
    {
        String strDirectory = System.getProperty("user.dir");
        this.highScoreFile = new File(strDirectory + "/src/resources/highscores.txt");

        Scanner in = new Scanner(highScoreFile);

        while (in.hasNextLine())
        {
            String[] parts = in.nextLine().split(",");
            highScores.add(parts);

        }

    }

    public ArrayList<String[]> getHighScores() {
        return highScores;
    }

    /**
     * Method overrides toString() method to return high scores in string format
     * @return
     */
    @Override
    public String toString() {

        String strHighScore = "";

        for (String[] part : highScores)
        {
            strHighScore += part[0] + ". " + part[1] + " - " + part[2] + "\n";
        }

        return strHighScore;
    }

    /**
     * Method determines if player score qualifies for a new high score updates highScore array and high score file to save
     * @param player
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void updateHighScores(Player player) throws FileNotFoundException, IOException
    {
        int playerScore = player.getScore();

        for (int i=0; i<highScores.size(); i++)
        {
            if (playerScore > Integer.parseInt(highScores.get(i)[2]))
            {
                String[] playerStats = new String[3];
                playerStats[0] = Integer.toString(i+1);
                playerStats[1] = player.getName();
                playerStats[2] = Integer.toString(playerScore);
                highScores.add(i,playerStats);
                break;
            }
        }

        System.out.println("Check1");
        PrintWriter out = new PrintWriter(highScoreFile);

        for (int j=0; j<10; j++)
        {
            System.out.println("Check2");
            String line = Integer.toString(j+1) + "," + highScores.get(j)[1] + "," + highScores.get(j)[2] + ",\n";
            out.printf(line);
        }

        out.close();


    }
}
