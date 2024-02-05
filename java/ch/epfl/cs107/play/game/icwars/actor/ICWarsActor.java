package ch.epfl.cs107.play.game.icwars.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class ICWarsActor extends MovableAreaEntity {

    private final Faction faction;

    /** sets the 2 factions : Enemy and ally*/
    public enum Faction {
        FRIENDLY,
        ENEMY
    }

    /**
     * returns the faction
     * @return
     */
    public Faction getFaction(){
        return faction;
    }

    /**
     *
     * @param area
     * @param position
     * @param faction
     */

    /**
     * Constructeur of ICWarsActor
     * @param area
     * @param position
     */
    public ICWarsActor(Area area, DiscreteCoordinates position, Faction faction) {
        super(area, Orientation.UP, position);
        this.faction = faction;
    }


    /**
     * makes the actor enter the area, registers it into the area and sets it at a position
     * @param area
     * @param position
     */
    public void enterArea(Area area, DiscreteCoordinates position){
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
    }

    /** removes the player from the area*/
    public void leaveArea(){ getOwnerArea().unregisterActor(this); }


    /**
     * returns the current cells
     * @return
     */
    public List<DiscreteCoordinates> getCurrentCells() { return Collections.singletonList(getCurrentMainCellCoordinates()); }

    /**
     * doesn't allow to take a cell's position and to exclusively occupy it
     * @return
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }


    /**
     * allows to interact with the cells
     * @return
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }


    /**
     *  doesn't allow to interact with the view
     * @return
     */
    @Override
    public boolean isViewInteractable() {
        return false;
    }


    /**
     * accepts interactions
     * @param v (AreaInteractionVisitor) : the visitor
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {}

    /**
     * draws the actor
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {}

}
