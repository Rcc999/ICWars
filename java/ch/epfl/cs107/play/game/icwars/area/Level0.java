package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit.Soldat;
import ch.epfl.cs107.play.game.icwars.actor.Unit.Tank;
import ch.epfl.cs107.play.game.icwars.actor.Unit.Unit;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;

public class Level0 extends ICWarsArea {
    private Unit tankAlly;
    private Unit soldatAlly;
    private Unit soldatEnemy;
    private Unit tankEnemy;

    /**
     * Allow us to access the name of the level;
     *
     * @return
     */
    @Override
    public String getTitle() {
        return "icwars/Level0";
    }

    /**
     * Create the areas of the initial level with the friendly and enemy Units
     */
    @Override
    protected void createArea() {
        this.tankAlly = new Tank(this, new DiscreteCoordinates(2, 5), ICWarsActor.Faction.FRIENDLY);
        this.soldatAlly = new Soldat(this, new DiscreteCoordinates(3, 5), ICWarsActor.Faction.FRIENDLY);
        this.tankEnemy = new Tank(this, new DiscreteCoordinates(8, 5), ICWarsActor.Faction.ENEMY);
        this.soldatEnemy = new Soldat(this, new DiscreteCoordinates(9, 5), ICWarsActor.Faction.ENEMY);
        registerActor(new Background(this));
    }

    /**
     * Give us the acces to the enemy Units throughout an ArrayList
     *
     * @return
     */
    public ArrayList<Unit> retourneTableauAllyUnit() {
        ArrayList<Unit> tableauAllyUnit = new ArrayList<>();
        tableauAllyUnit.add(tankAlly);
        tableauAllyUnit.add(soldatAlly);
        return tableauAllyUnit;
    }

    /**
     * Give us the acces to the friendly Units throughout an ArrayList
     *
     * @return
     */
    public ArrayList<Unit> retourneTableauEnemyUnit() {
        ArrayList<Unit> tableauEnemyUnit = new ArrayList<>();
        tableauEnemyUnit.add(tankEnemy);
        tableauEnemyUnit.add(soldatEnemy);
        return tableauEnemyUnit;
    }


    /**
     * returns the coordinates when the friendly PLayer that will Start his turn
     * @return
     */
    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(0, 0);
    }


    /**
     * returns the coordinates when the Enemy PLayer that will Start his turn
     * @return
     */
    public DiscreteCoordinates getEnemySpawnPosition() {
        return new DiscreteCoordinates(7, 4);
    }

}
