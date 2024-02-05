package ch.epfl.cs107.play.game.icwars.actor.Unit;

import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Path;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.Unit.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.area.ICWarsRange;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public abstract class Unit extends ICWarsActor implements Interactor, Interactable {

    private int hp;
    private int attaque;
    private int repare;
    private int rayonDeplacement;
    private ICWarsRange range = new ICWarsRange();
    private boolean used = false;
    private int defenseStar;
    private ICWarsUnitInteractionHandler handler = new ICWarsUnitInteractionHandler();
    private int indiceEnemy;
    private int hpRemaining;


    /**
     * Constructor of Unit
     * @param area
     * @param position
     * @param faction
     * @param hp
     * @param attaque
     * @param rayonDeplacement
     */
    public Unit(ICWarsArea area, DiscreteCoordinates position, Faction faction, int hp, int attaque, int rayonDeplacement) {
        super(area, position, faction);
        this.hp = hp;
        this.attaque = attaque;
        this.rayonDeplacement = rayonDeplacement;
        int FromX = position.x;
        int FromY = position.y;
        setNode(area, FromX, FromY);
    }

    /**
     * Draw the unit's range and a path from the unit position to
     * destination
     *
     * @param destination path destination
     * @param canvas      canvas
     */
    public void drawRangeAndPathTo(DiscreteCoordinates destination,
                                   Canvas canvas) {
        range.draw(canvas);
        Queue<Orientation> path = range.shortestPath(getCurrentMainCellCoordinates(), destination);
        //Draw path only if it exists (destination inside the range)
        if (path != null) {
            new Path(getCurrentMainCellCoordinates().toVector(),
                    path).draw(canvas);
        }
    }

    /**
     * Let us know the possible range of damage and displacement of our units
     * @param area
     * @param FromX
     * @param FromY
     */
    private void setNode(ICWarsArea area, int FromX, int FromY) {

        int rayonDeplacementInitialY = -rayonDeplacement;
        int rayonDeplacementInitialX = -rayonDeplacement;
        int rayonDeplacementFinalX = rayonDeplacement;
        int rayonDeplacementFinalY = rayonDeplacement;


        if (FromY - rayonDeplacement < 0) {
            rayonDeplacementInitialY = -FromY;
        }

        if (FromX + rayonDeplacement > area.getWidth() - 1) {
            rayonDeplacementFinalX = (area.getWidth() - 1) - FromX;
        }


        if (FromX - rayonDeplacement < 0) {
            rayonDeplacementInitialX = -FromX;
        }


        if (FromY + rayonDeplacement > area.getHeight() - 1) {
            rayonDeplacementFinalY = (area.getHeight() - 1) - FromY;
        }


        for (int x = rayonDeplacementInitialX; x <= rayonDeplacementFinalX; x++) {
            for (int y = rayonDeplacementInitialY; y <= rayonDeplacementFinalY; y++) {

                boolean hasRightEdge = false;
                boolean hasLeftEdge = false;
                boolean hasUpEdge = false;
                boolean hasDownEdge = false;

                if (x > -rayonDeplacement && x + FromX > 0) {
                    hasLeftEdge = true;
                }
                if (x < rayonDeplacement && x + FromX < area.getWidth() - 1) {
                    hasRightEdge = true;
                }
                if (y > -rayonDeplacement && y + FromY > 0) {
                    hasDownEdge = true;
                }
                if (y < rayonDeplacement && y + FromY < area.getHeight() - 1) {
                    hasUpEdge = true;
                }

                range.addNode(new DiscreteCoordinates(x + FromX, y + FromY), hasLeftEdge, hasUpEdge, hasRightEdge, hasDownEdge);
            }
        }
    }

    @Override
    public boolean changePosition(DiscreteCoordinates newPosition) {

        if (newPosition.equals(getCurrentMainCellCoordinates()))
            return false;

        if (!getOwnerArea().canEnterAreaCells(this, List.of(newPosition)))
            return false;

        if (!range.nodeExists(newPosition)) {
            return false;
        }


        getOwnerArea().leaveAreaCells(this, getCurrentCells());
        setCurrentPosition(newPosition.toVector());
        getOwnerArea().enterAreaCells(this, getCurrentCells());


        range = new ICWarsRange();
        setNode((ICWarsArea) getOwnerArea(), newPosition.x, newPosition.y);
        return true;
    }

    /**
     * abstract method redefined in Tank and Soldier that gives the units the action they can do such as wait or attack
     * @return
     */
    public abstract ArrayList<Action> getListActions();

    /**
     * let us a know if a unit is located in the range of an other unit
     * @param position
     * @return
     */
    public boolean dansLeRayon(DiscreteCoordinates position){
        return range.nodeExists(position);
    }

    public int getHp() {
        return hp;
    }

    public int getDamage() {
        return attaque;
    }

    public void setIsUsed(boolean u) {
        this.used = u;
    }

    public void setIndiceEnemy(int j) {
        indiceEnemy = j;
    }

    public boolean getIsUsed() {
        return used;
    }

    public int getIndiceEnemy() {
        return indiceEnemy;
    }

    public int getRayonDeplacement() {
        return rayonDeplacement;
    }

    public ICWarsRange getRange() {
        return range;
    }

    public abstract String getName();


    /**
     * Calculate the Damage done by a unit taking in consideration the defense starts that offers the cell
     * @param degat
     */
    public void degatInflige( int degat){
        if(degat > defenseStar) {
            hp = hp - degat + defenseStar;
            if (hp < 0) {
                hp = 0;
            }
        }
    }


    @Override
    public boolean takeCellSpace() { return true; }

    @Override
    public boolean isCellInteractable() { return true; }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void draw(Canvas canvas) { }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) { ((ICWarsInteractionVisitor) v).interactWith(this); }

    @Override
    public void interactWith(Interactable other) {
        other.acceptInteraction(handler);
    }

    /**
     * Nested class that allows to get the defense star on each cells
     */
    private class ICWarsUnitInteractionHandler implements ICWarsInteractionVisitor {

        @Override
        public void interactWith(ICWarsBehavior.ICWarsCell cell) {
            defenseStar = (cell.getType().getDefenseStar());
        }

    }


}
