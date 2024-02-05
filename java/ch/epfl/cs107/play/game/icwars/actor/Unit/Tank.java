package ch.epfl.cs107.play.game.icwars.actor.Unit;

import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.actor.Unit.action.Action;
import ch.epfl.cs107.play.game.icwars.actor.Unit.action.Attack;
import ch.epfl.cs107.play.game.icwars.actor.Unit.action.Wait;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Tank extends Unit {

    private Sprite tank;
    private ArrayList<Action> listOfAction = new ArrayList<>();


    /**
     * Constructor of Tank : allows us to create a unit "Tank" able to attack or wait, with a certain position and faction
     *
     * @param area
     * @param position
     * @param faction
     */
    public Tank(ICWarsArea area, DiscreteCoordinates position, Faction faction) {
        super(area, position, faction, 10, 7, 4);
        listOfAction.add(new Wait(this, area));
        listOfAction.add(new Attack(this, area));
        if (faction == Faction.FRIENDLY) {
            tank = new Sprite("icwars/friendlyTank", 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
        } else if (faction == Faction.ENEMY) {
            tank = new Sprite("icwars/enemyTank", 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
        }
    }


    /**
     * draws the "Tank" on the area
     *
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        if (getHp() > 0) {
            if (this.getIsUsed()) {
                tank.setAlpha(0.5f);
            } else {
                tank.setAlpha(1.f);
            }
            tank.draw(canvas);
        }
    }


    /**
     * gives all possible actions of a unit
     *
     * @return
     */
    @Override
    public ArrayList<Action> getListActions() {
        return listOfAction;
    }

    /**
     * returns the name of the unit
     *
     * @return
     */
    @Override
    public String getName() {
        return "Tank";
    }


    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

}



