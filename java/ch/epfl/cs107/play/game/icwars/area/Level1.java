package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit.Soldat;
import ch.epfl.cs107.play.game.icwars.actor.Unit.Tank;
import ch.epfl.cs107.play.game.icwars.actor.Unit.Unit;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;

public class Level1 extends ICWarsArea{

    private Unit soldat1Ally;
    private Unit tank1Ally;
    private Unit tank1Enemy;
    private Unit soldat1Enemy;

    /**
     * Allow us to access the name of the level;
     * @return
     */
    @Override
    public String getTitle() {return "icwars/Level1";}

    /**
     * Create the areas of the first level with the friendly and enemy Units
     */
    @Override
    protected void createArea() {
        this.tank1Ally = new Tank(this, new DiscreteCoordinates(2,5), ICWarsActor.Faction.FRIENDLY);
        this.soldat1Ally =  new Soldat(this, new DiscreteCoordinates(3,5), ICWarsActor.Faction.FRIENDLY);
        this.tank1Enemy= new Tank(this, new DiscreteCoordinates(8,5), ICWarsActor.Faction.ENEMY);
        this.soldat1Enemy = new Soldat(this, new DiscreteCoordinates(9,5), ICWarsActor.Faction.ENEMY);
        registerActor(new Background(this));
    }

    /**
     * Give us the acces to the enemy Units throughout an ArrayList
     * @return
     */
    public ArrayList<Unit> retourneTableauEnemyUnit(){ ArrayList<Unit> tableauAllyUnit = new ArrayList<>();
        tableauAllyUnit.add(tank1Enemy);
        tableauAllyUnit.add(soldat1Enemy);
        return tableauAllyUnit;}

    /**
     * Give us the acces to the friendly Units throughout an ArrayList
     * @return
     */
    public ArrayList<Unit> retourneTableauAllyUnit(){ArrayList<Unit> tableauEnemyUnit = new ArrayList<>();
        tableauEnemyUnit.add(tank1Ally);
        tableauEnemyUnit.add(soldat1Ally);
        return tableauEnemyUnit;}

    /**
     * returns the coordinates when the friendly PLayer that will Start his turn
     * @return
     */
    public DiscreteCoordinates getPlayerSpawnPosition() {return new DiscreteCoordinates(2,5);}

    /**
     * returns the coordinates when the friendly PLayer that will Start his turn
     * @return
     */
    public  DiscreteCoordinates getEnemySpawnPosition() {return new DiscreteCoordinates(17,5);}
}
