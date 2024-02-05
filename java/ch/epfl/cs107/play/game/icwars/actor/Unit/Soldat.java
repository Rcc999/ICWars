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

public class Soldat extends Unit {

    private Sprite soldat;
    private ArrayList<Action> listOfAction = new ArrayList<>();

    /**
     * Constructor of Soldier : allows us to create a unit "Soldier" able to attack or wait, with a certain position and faction
     *
     * @param area
     * @param position
     * @param faction
     */
    public Soldat(ICWarsArea area, DiscreteCoordinates position, Faction faction) {
        super(area, position, faction, 5, 2, 2);
        listOfAction.add(new Wait(this, area));
        listOfAction.add(new Attack(this, area));
        if (faction == Faction.FRIENDLY) {
            soldat = new Sprite("icwars/friendlySoldier", 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
        } else if (faction == Faction.ENEMY) {
            soldat = new Sprite("icwars/enemySoldier", 1.5f, 1.5f, this, null, new Vector(-0.25f, -0.25f));
        }


    }

    /**
     * draws the "Soldat" on the area
     *
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        if (getHp() > 0) {
            if (this.getIsUsed()) {
                soldat.setAlpha(0.5f);
            } else {
                this.setIsUsed(false);
                soldat.setAlpha(1.f);
            }
            soldat.draw(canvas);
        }
    }

    /**
     * gives all possible actions of a Soldier Unit
     * @return
     */
    @Override
    public ArrayList<Action> getListActions() {
        return listOfAction;
    }

    /**
     * returns the name of the unit
     * @return
     */
    @Override
    public String getName() {
        return "Soldat";
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
