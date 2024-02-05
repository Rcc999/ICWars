package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ICWarsPlayer extends ICWarsActor implements Interactor, Interactable {

    /**
     * @param area
     * @param position
     * @param faction
     */

    private ArrayList<Unit> unitEnregistree = new ArrayList<>();
    private ICWarsArea area;
    protected Unit selectedUnit;
    protected State state;


    /**
     * ICWarsPlayer Constructor
     *
     * @param area
     * @param position
     * @param faction
     * @param units
     */
    public ICWarsPlayer(ICWarsArea area, DiscreteCoordinates position, Faction faction, Unit... units) {
        super(area, position, faction);
        this.area = area;
        for (Unit value : units) {
            area.registerActor(value);
            unitEnregistree.add(value);
        }
        state = State.IDLE;
    }

    /**
     * Adds a unit to the area we are currently playing on
     *
     * @param unitsInArea
     */
    public void addUnitToArea(ArrayList<Unit> unitsInArea) {
        if (unitsInArea.size() > 0) {
            area.registeredUnit(unitsInArea);
            unitEnregistree.addAll(unitsInArea);
        }
    }


    public Unit getIndexSelectedUnit(int index) {
        return unitEnregistree.get(index);
    }

    public int sizeUniteEnregistree() {
        return unitEnregistree.size();
    }

    /**
     * Centers the camera on the player
     */
    public void centerCamera() {
        getOwnerArea().setViewCandidate(this);
    }

    /**
     * Allows us to select a certain Unit
     *
     * @param unit
     */
    public void selectUnit(Unit unit) {
        selectedUnit = unit;
    }

    /**
     * always updates the area to remove units that have no hp remaining
     *
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (int i = unitEnregistree.size() - 1; i >= 0; i--) {
            if (unitEnregistree.get(i).getHp() == 0) {
                unitEnregistree.get(i).leaveArea();
                area.removeRegisterUnit(unitEnregistree.get(i));
                unitEnregistree.remove(i);
            }
        }
    }

    /**
     * sets all the specific states in which the player can be
     */
    public enum State {
        IDLE,
        NORMAL,
        SELECT_CELL,
        MOVE_UNIT,
        ACTION_SELECTION,
        ACTION
    }

    /**
     * gives us the current state of the player
     *
     * @return
     */
    public State getState() {
        return state;
    }


    /**
     * sets the state of the player to a certain state
     *
     * @param state
     */
    public void setState(State state) {
        this.state = state;
    }


    /**
     * when leaving, sets the state of the player to normal after he went through the SELECT_CELL state
     *
     * @param coordinates left cell coordinates
     */
    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {
        if (selectedUnit == null && state == State.SELECT_CELL) {
            state = State.NORMAL;
        }
    }

    /**
     * starts the turn, puts all the units in an unused mode, allowing us to use them,
     * sets the state to NORMAL, and centers the camera on the player
     */
    public void startTurn() {
        for (Unit unit : unitEnregistree) {
            unit.setIsUsed(false);
        }
        state = State.NORMAL;
        centerCamera();
    }

    /**
     * sets a unit as unused
     */
    public void unused() {
        for (Unit unit : unitEnregistree) {
            unit.setIsUsed(false);
        }
    }


    /**
     * set All the units as used if they are not already used
     *
     * @return
     */
    public boolean allUsed() {
        for (Unit unit : unitEnregistree) {
            if (!unit.getIsUsed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * gets the unit to leave the area
     */
    @Override
    public void leaveArea() {
        for (Unit unit : unitEnregistree) {
            unit.leaveArea();
        }
        super.leaveArea();
    }

    /**
     * returns if the player has no units remaining
     *
     * @return
     */
    public boolean playerIsDefeated() {
        return unitEnregistree.size() == 0;
    }


    /**
     * returns a list of the current cells
     *
     * @return
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return null;
    }


    /**
     * allows Cell interactions
     *
     * @return
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * doesn't allow View interactions
     *
     * @return
     */
    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other) {

    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     * allows Cell interactions
     *
     * @return
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * doesn't allow View interactions
     *
     * @return
     */
    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * does the interactions that the player has
     *
     * @param v (AreaInteractionVisitor) : the visitor
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ICWarsInteractionVisitor) v).interactWith(this);
    }

}
