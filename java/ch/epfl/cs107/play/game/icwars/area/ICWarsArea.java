package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.icwars.ICWars;
import ch.epfl.cs107.play.game.icwars.actor.Unit.Unit;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;

public abstract class ICWarsArea extends Area {

    private ICWarsBehavior behavior;
    private ArrayList<Integer> listeIndiceUniteAdverse = new ArrayList<>();
    private ArrayList<Unit> registerUnitInArea = new ArrayList<>();
    private double distance;

    /**
     * gets the camera scale factor
     *
     * @return
     */
    @Override
    public final float getCameraScaleFactor() {
        return ICWars.CAMERA_SCALE_FACTOR;
    }

    /**
     * abstract method  that is redefine in level 1 and level 0
     *
     * @return
     */
    public abstract ArrayList<Unit> retourneTableauAllyUnit();

    /**
     * abstract method  that is redefine in level 1 and level 0
     *
     * @return
     */
    public abstract ArrayList<Unit> retourneTableauEnemyUnit();

    /**
     * abstract method  that is redefine in level 1 and level 0
     *
     * @return
     */
    public abstract DiscreteCoordinates getPlayerSpawnPosition();

    public abstract DiscreteCoordinates getEnemySpawnPosition();

    /**
     * abstract method  that is redefine in level 1 and level 0
     *
     * @return
     */
    protected abstract void createArea();


    /**
     * creates and area from scratch
     *
     * @param window
     * @param fileSystem
     * @return
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            behavior = new ICWarsBehavior(window, getTitle());
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }


    /**
     * let us know all the units present in our area
     *
     * @param unitsInArea
     */
    public void registeredUnit(ArrayList<Unit> unitsInArea) {
        for (Unit unitRegistered : unitsInArea) {
            registerActor(unitRegistered);
            registerUnitInArea.add(unitRegistered);
        }
    }

    /**
     * let us remove the units from the ArrayList above
     *
     * @param unit
     */
    public void removeRegisterUnit(Unit unit) {
        registerUnitInArea.remove(unit);
    }


    /**
     * Allow us to know the indexes of the unit of different Faction and in the range
     *
     * @param unit
     * @return
     */
    public ArrayList<Integer> getTableauIndiceUnitAdverse(Unit unit) {
        for (Unit enemy : registerUnitInArea) {
            DiscreteCoordinates position = new DiscreteCoordinates((int) enemy.getPosition().x, (int) enemy.getPosition().y);
            if ((unit.getFaction() != enemy.getFaction()) && unit.dansLeRayon(position)) {
                listeIndiceUniteAdverse.add(registerUnitInArea.indexOf(enemy));
            }
        }
        return listeIndiceUniteAdverse;
    }


    /**
     * resets the list of enemy units
     */
    public void resetListeIndiceUniteAdverse() {
        listeIndiceUniteAdverse.clear();
    }


    /**
     * clears the units on the area
     */
    public void clearRegisterUnitArea() {
        registerUnitInArea.clear();
    }


    /**
     * sets the camera on the unit that we want to target during the selection of our action
     *
     * @param i
     */
    public void setCamera(int i) {
        setViewCandidate(registerUnitInArea.get(i));
    }


    /**
     * Set the damage to the opposite unit that we want to attack and remove them from the index list if they are dead
     *
     * @param index
     * @param degat
     */
    public void setDegat(int index, int degat) {
        registerUnitInArea.get(index).degatInflige(degat);

        if (registerUnitInArea.get(index).getHp() <= 0) {

            registerUnitInArea.remove(index);
        }
    }


    /**
     * Lest us know the closest Unit possible to our AI
     *
     * @param unit
     * @return
     */
    public DiscreteCoordinates findClosestUnit(Unit unit) {
        distance = DiscreteCoordinates.distanceBetween(new DiscreteCoordinates(0, 0), new DiscreteCoordinates(getWidth(), getHeight()));
        double closest;
        int index = 0;
        for (int i = 0; i < registerUnitInArea.size(); i++) {
            if (registerUnitInArea.get(i).getFaction() != unit.getFaction()) {
                closest = DiscreteCoordinates.distanceBetween(new DiscreteCoordinates((int) registerUnitInArea.get(i).getPosition().x, (int) registerUnitInArea.get(i).getPosition().y), new DiscreteCoordinates((int) unit.getPosition().x, (int) unit.getPosition().y));
                if (closest < distance) {
                    distance = closest;
                    index = i;
                }
            }
        }
        return registerUnitInArea.get(index).getCurrentCells().get(0);
    }


    /**
     * Allow our AI to know the unit with the lowest Hp in order to attack him
     *
     * @param arrayList
     * @return
     */
    public int lowestHpUnit(ArrayList<Integer> arrayList) {
        int hp = registerUnitInArea.get(arrayList.get(0)).getHp();
        int index = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            int hpEnnemi = registerUnitInArea.get(arrayList.get(i)).getHp();
            if (hpEnnemi < hp) {
                hp = hpEnnemi;
                index = arrayList.get(i);
            }

        }
        return index;
    }

}
