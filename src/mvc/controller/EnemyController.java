package mvc.controller;

import mvc.model.*;
import mvc.view.GameFrame;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jakecoll on 11/29/16.
 */
public class EnemyController {

    private ArrayList<EnemyFighter> _enemies;
    private ArrayList<EnemyBullet> _enemyBullets;
    private Ship mTarget;
    private int nLevel;

    /**
     * Constructs an Enemy Controller object with Ship object referenced so Enemy Fighters know what to target
     * @param ship
     */
    public EnemyController(Ship ship) {

        mTarget = ship;

    }

    /**
     * Returns an array list of EnemyFighter objects with positions and actions updated
     * @param deltaTime
     * @param enemies
     * @return
     */
    public ArrayList<EnemyFighter> update(long deltaTime, ArrayList<EnemyFighter> enemies) {

        _enemies = enemies;
        _enemyBullets = new ArrayList<EnemyBullet>();
        int nTotalNotInformation = 0;
        int nTotalIsAttacking =0;

        for (EnemyFighter enemy : _enemies) {

            //Checks if a fighter is in attack mode and if it should fire a buller
            if (enemy.checkIsAttacking())
            {
                nTotalIsAttacking++;

                if (enemy.getPosition().getY() > 400 && !enemy.checkHasFired() && !enemy.checkIsExploding()) {
                    EnemyBullet fire = new EnemyBullet(enemy);
                    _enemyBullets.add(fire);
                    enemy.setHasFired(true);
                }

                enemy.attack();
            }

            //if an EnemyFighter has fired at ship on attack run then don't fire again on this instance of an attack run
            if (!enemy.checkIsAttacking() && enemy.checkHasFired()){
                enemy.setHasFired(false);
            }

            //If Enemy Fighter is not in formation then increment variable for storing the total number of fighters not in formation
            if (!enemy.checkInFormation()) {
                nTotalNotInformation++;

                if (enemy.checkIsAttacking())
                {
                    nTotalIsAttacking++;
                }
            }

            //get the X,Y position of the EnemyFighter
            double dX = enemy.getPosition().getX();
            double dY = enemy.getPosition().getY();
            int x = (int) Math.floor(dX);
            int y = (int) Math.floor(dY);

            //This tells blue fighter what they should do on their initial movement to formation
            if (enemy.getClass().equals(BlueFighter.class)) {

                //If Blue fighter reached the middle of the screen the tell it to start its fancy loop
                if (y == GameFrame.FRAME_DIM.height / 2 && !enemy.checkIfLooping() && !enemy.checkIsAttacking()) {

                    enemy.setIsStarting(false);
                    enemy.setIsLooping(true);
                    enemy.loop(deltaTime, 15);

                } else if (y == GameFrame.FRAME_DIM.height / 2 && enemy.checkIfLooping() && !enemy.checkIsAttacking()) {
                    enemy.setIsLooping(false);
                    enemy.move(deltaTime, 5, 2);
                } else if (enemy.checkIfStarting() && !enemy.checkIsAttacking()) {
                    enemy.move(deltaTime, 5, 5);
                } else if (enemy.checkIfLooping() && !enemy.checkIsAttacking()) {
                    enemy.loop(deltaTime, 15);
                } else if (!enemy.checkIfLooping() && !enemy.checkIfStarting() && !enemy.checkIsAttacking()) {
                    enemy.moveToFormationPos();
                }

            }

            //Red Fighters aren't fancy. They just move to position
            if (enemy.getClass().equals(RedFighter.class) && !enemy.checkIsAttacking()) {
                enemy.moveToFormationPos();
            }
        }

        //If all fighters are in position then choose some to attack
        if (nTotalNotInformation == 0 && nTotalIsAttacking==0) {
            chooseAttack();
        }

        return _enemies;
    }

    /**
     * Method for returning EnemyBullet objects if an EnemyFighter has fired so that it can be added to game
     * @return
     */
    public ArrayList<EnemyBullet> getEnemyBullets() {
        return _enemyBullets;
    }

    /**
     * Method for choosing which EnemyFighters should attack
     */
    public void chooseAttack() {

        ArrayList<Integer> indexOfEligibleAttackers = new ArrayList<Integer>();

        //double loop for evaluating if an EnemyFighter is blocked by any other enemy fighters
        for (EnemyFighter enemy : _enemies) {

            boolean IsBlocked = false;

            int enemyXLeft = (int) Math.round(enemy.getPosition().getX() - (enemy.getDim().width/2));
            int enemyXRight = (int) Math.round(enemy.getPosition().getX() + (enemy.getDim().width/2));
            int enemyYBottom = (int) Math.round(enemy.getPosition().getY());

            for (EnemyFighter neighbor : _enemies) {

                int neighborX = (int) Math.round(neighbor.getPosition().getX());
                int neighborYBottom = (int) Math.round(neighbor.getPosition().getY());

                if (enemy.getPosition() != neighbor.getPosition()) {


                    if (neighborX >= enemyXLeft+1 && neighborX <= enemyXRight+1 && !neighbor.checkIsAttacking()) {

                        if (neighborYBottom >= enemyYBottom) {
                            IsBlocked = true;
                        }
                    }

                }


            }

            //If EnemyFighter is not blocked by any of its counterparts then add its index to an arraylist of indices for each Fighter eligible to attack
            if (!IsBlocked) {
                int index = _enemies.indexOf(enemy);
                indexOfEligibleAttackers.add(index);
            }

        }

        //Choose which random fighter should attack based on the level. Up to three for level 3.
        if (nLevel<=indexOfEligibleAttackers.size()) {
            for (int k = 1; k <= nLevel; k++) {
                Random rand = new Random();
                int fighterLottery = rand.nextInt(indexOfEligibleAttackers.size());
                int fighterToAttack = indexOfEligibleAttackers.get(fighterLottery);
                _enemies.get(fighterToAttack).setAttack(mTarget);
                indexOfEligibleAttackers.remove(fighterLottery);
            }
        } else if (indexOfEligibleAttackers.size()>0){
            Random rand = new Random();
            int fighterLottery = rand.nextInt(indexOfEligibleAttackers.size());
            int fighterToAttack = indexOfEligibleAttackers.get(fighterLottery);
            _enemies.get(fighterToAttack).setAttack(mTarget);
            indexOfEligibleAttackers.remove(fighterLottery);
        }


    }

    /**
     * Method for passing what level the game is on the the Enemy Controller
     * @param level
     */
    public void setLevel(int level) {
        this.nLevel = level;
    }


}
