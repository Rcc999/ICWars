package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.actor.Unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.Unit.action.Action;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.game.icwars.gui.ICWarsPlayerGUI;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.List;

public class RealPlayer extends ICWarsPlayer {

    private ICWarsPlayerGUI playerGui = new ICWarsPlayerGUI(10.f, this);
    private Sprite spriteUnit;
    private ICWarsPlayerInteractionHandler handler = new ICWarsPlayerInteractionHandler();
    private Action act;


    /**
     * RealPlayer Constructor
     * @param area
     * @param position
     * @param faction
     * @param units
     */
    public RealPlayer(ICWarsArea area, DiscreteCoordinates position, Faction faction, Unit... units) {
        super(area, position, faction, units);
        if (faction == Faction.FRIENDLY) {
            spriteUnit = new Sprite("icwars/allyCursor", 1.f, 1.f, this);
        } else if (faction == Faction.ENEMY) {
            spriteUnit = new Sprite("icwars/enemyCursor", 1.f, 1.f, this);
        }
    }


    /**
     *
     * @param orientation
     * @param b
     */
    public void moveIfPress(Orientation orientation, ch.epfl.cs107.play.window.Button b) {
        if (b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(6);
            }
        }
    }


    /** Let the player enter the area
     * @param area
     * @param position
     */
    public void enterArea(Area area, DiscreteCoordinates position) {
        area.registerActor(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        area.setViewCandidate(this);
    }


    /**
     * updates the state of the player at all times between the 6 possible states
     * @param deltaTime
     */
    public void update(float deltaTime) {

        super.update(deltaTime);

        Keyboard keyboard = getOwnerArea().getKeyboard();

        switch (state) {

            case IDLE:
                break;

            case NORMAL:
                selectedUnit = null;
                if (keyboard.get(Keyboard.ENTER).isPressed()) {
                    setState(State.SELECT_CELL);

                }

                if (keyboard.get(Keyboard.TAB).isPressed()) {
                    setState(State.IDLE);
                }

                break;

            case SELECT_CELL:

                if (keyboard.get(Keyboard.TAB).isPressed()) {
                    selectedUnit = null;
                    setState(State.NORMAL);
                }

                if (selectedUnit != null && !selectedUnit.getIsUsed()) {
                    setState(State.MOVE_UNIT);

                }
                break;

            case MOVE_UNIT:

                if (keyboard.get(Keyboard.ENTER).isPressed() && !selectedUnit.getIsUsed() && selectedUnit.getRange().nodeExists(getCurrentMainCellCoordinates())) {
                    if (!(this.getPosition().x == selectedUnit.getPosition().x && this.getPosition().y == selectedUnit.getPosition().y)) {
                        if (selectedUnit.changePosition(getCurrentMainCellCoordinates())) {
                            setState(State.ACTION_SELECTION);
                        } else {
                            selectedUnit.setIsUsed(false);
                            setState(State.NORMAL);
                        }
                    } else {
                        setState(State.ACTION_SELECTION);
                    }
                }

                if (keyboard.get(Keyboard.TAB).isPressed()) {
                    setState(State.NORMAL);
                }


                break;

            case ACTION_SELECTION:

                for (int j = 0; j < selectedUnit.getListActions().size(); j++) {
                    if (keyboard.get(selectedUnit.getListActions().get(j).getKey()).isReleased()) {
                        act = selectedUnit.getListActions().get(j);
                        setState(State.ACTION);
                    }
                }
                break;

            case ACTION:

                act.doAction(10, this, keyboard);
                if (keyboard.get(Keyboard.TAB).isPressed()) {
                    setState(State.ACTION_SELECTION);
                    selectedUnit.setIsUsed(false);
                }
                break;

        }

        if (state == State.NORMAL || state == State.SELECT_CELL || state == State.MOVE_UNIT) {
            moveIfPress(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
            moveIfPress(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
            moveIfPress(Orientation.UP, keyboard.get(Keyboard.UP));
            moveIfPress(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        }
    }


    /**
     *
     * @param discreteCoordinates
     */
    @Override
    public void onLeaving(List<DiscreteCoordinates> discreteCoordinates) {
        playerGui.setInfoPanel(null);
        super.onLeaving(discreteCoordinates);
    }


    /**
     *
     * @param unit
     */
    public void selectUnit(Unit unit) {
        selectedUnit = unit;
        playerGui.setSelectedUnit(selectedUnit);
    }


    /**
     * make the unit leave the area
     */
    public void leaveArea() {
        super.leaveArea();
        selectedUnit = null;
    }


    /**
     * Draw the RealPLayer, the cursor
     * @param canvas target, not null
     */
    public void draw(Canvas canvas) {
        if (state != State.IDLE && state != State.ACTION) {
            spriteUnit.draw(canvas);
        }
        playerGui.draw(canvas);

        if (act != null) {
            act.draw(canvas);
        }

    }


    /**
     * allows interactions if not moving
     * @param other
     */
    public void interactWith(Interactable other) {
        if (!isDisplacementOccurs()) {
            other.acceptInteraction(handler);
        }
    }

    private class ICWarsPlayerInteractionHandler implements ICWarsInteractionVisitor {

        /**
         * manages the interactions between the units
         * @param unit
         */
        @Override
        public void interactWith(Unit unit) {
            Keyboard keyboard = getOwnerArea().getKeyboard();
            playerGui.setInfoPanel(unit);
            if (!isDisplacementOccurs() && unit.getFaction() == getFaction() && keyboard.get(Keyboard.ENTER).isReleased()) {
                selectUnit(unit);
            }
        }


        /**
         * manages the interactions with the information panel
         * @param cell
         */
        @Override
        public void interactWith(ICWarsBehavior.ICWarsCell cell) {
            playerGui.setCelluleInfoPanel(cell.getType());
            playerGui.setInfoPanel(null);
        }

    }


}
