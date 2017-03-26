package mvc.model;


import java.awt.*;

/**
 * Created by jakecoll on 12/4/16.
 */
public class EnemyBullet extends Bullet {

    private EnemyFighter mEnemy;
    private int XCoord;
    private int YCoord;

    private Dimension mDim = new Dimension(3,12);

    /**
     * Constructs a Bullet that shoots from an enemy fighter
     * @param enemyFighter
     */
    public EnemyBullet(EnemyFighter enemyFighter) {
        mEnemy = enemyFighter;
        XCoord = (int) Math.round(mEnemy.getPosition().getX());
        YCoord = (int) Math.round(mEnemy.getPosition().getY());

    }

    /**
     * Moves bullets position vertically and repaints object
     * @param deltaTime
     */
    @Override
    public void move(long deltaTime) {
        XCoord = XCoord;
        YCoord = YCoord + 10;

        repaint();
    }

    /**
     * Paints EnemyBullet object
     * @param g
     */
    @Override
    public void paintComponent(Graphics g)
    {
        g.setColor(Color.RED);
        g.drawRect(XCoord + mEnemy.getDim().width/2,YCoord,mDim.width,mDim.height);
    }

    /**
     * Return X coordinate of EnemyBullet
     * @return
     */
    public int getX()
    {
        return XCoord;
    }

    /**
     * Returns Y coordinate of EnemyBullet
     * @return
     */
    public int getY()
    {
        return YCoord;
    }


}
