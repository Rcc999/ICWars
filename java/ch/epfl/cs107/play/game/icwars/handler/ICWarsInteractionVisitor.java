package ch.epfl.cs107.play.game.icwars.handler;


import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.Unit.Unit;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;

public interface ICWarsInteractionVisitor extends AreaInteractionVisitor {

    /**
     * interaction with Unit;
     * @param unit
     */
    default void interactWith (Unit unit){}

    /**
     * Interaction with cell
     * @param cell
     */
    default void interactWith(ICWarsBehavior.ICWarsCell cell) {}

    /**
     * Interactable héritant de AreaInteractionVisitor
     * @param other (Interactable): interactable to interact with, not null
     */
    @Override
    default void interactWith(Interactable other){}




}
