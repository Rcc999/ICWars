package ch.epfl.cs107.play.game.icwars.actor.player;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icwars.actor.Unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.Unit.action.Action;
import ch.epfl.cs107.play.game.icwars.actor.Unit.action.Attack;
import ch.epfl.cs107.play.game.icwars.actor.Unit.action.Wait;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class AIPlayer extends ICWarsPlayer {

    private Float counter;
    private boolean counting;
    private int index = 0;
    private Action act;
    private Sprite spriteUnit;
    private ICWarsArea area;
    private final static float WAIT_FOR_PLAY = 1.f;


    /**
     * AIPlayer Constructor
     * @param area
     * @param position
     * @param faction
     * @param units
     */
    public AIPlayer(ICWarsArea area, DiscreteCoordinates position, Faction faction, Unit... units) {
        super(area, position, faction, units);
        this.area = area;
        if (faction == Faction.FRIENDLY) {
            spriteUnit = new Sprite("icwars/allyCursor", 1.f, 1.f, this);
        } else if (faction == Faction.ENEMY) {
            spriteUnit = new Sprite("icwars/enemyCursor", 1.f, 1.f, this);
        }
    }


    /**
     * Let us the know wether we can attack the opposite unit or move close to her to get to reach her because she is out of reach
     * @param unit
     */
    public void moveAIUnit(Unit unit) {

        DiscreteCoordinates closestEnemy = area.findClosestUnit(unit);

        if (unit.dansLeRayon(closestEnemy)) {
            DiscreteCoordinates positionUnit = new DiscreteCoordinates((int) unit.getPosition().x , (int) unit.getPosition().y );
            changePosition(positionUnit);
        }
        int newX = closestEnemy.x;
        int newY = closestEnemy.y;

        int leftBound = (int) unit.getPosition().x - unit.getRayonDeplacement();
        int rightBound = (int) unit.getPosition().x + unit.getRayonDeplacement();
        int upBound = (int) unit.getPosition().y + unit.getRayonDeplacement();
        int lowBound = (int) unit.getPosition().y - unit.getRayonDeplacement();

        if (closestEnemy.x < leftBound) newX = leftBound;
        if (closestEnemy.x > rightBound) newX = rightBound;
        if (closestEnemy.y > upBound) newY = upBound;
        if (closestEnemy.y < lowBound) newY = lowBound;

        unit.changePosition(new DiscreteCoordinates(newX, newY));
    }


    /**
     * Let the AI enter the area
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
     * Ensures that value time elapsed before returning true
     *
     * @param dt    elapsed time
     * @param value waiting time (in seconds)
     * @return true if value seconds has elapsed , false otherwise
     */
    private boolean waitFor(float value, float dt) {

        if (counting) {
            counter += dt;
            if (counter > value) {
                counting = false;
                return true;
            }
        } else {
            counter = 0f;
            counting = true;
        }
        return false;
    }


    /**
     * update at all time the AI player throughout his different state
     * @param deltaTime
     */
    public void update(float deltaTime) {

        super.update(deltaTime);

        switch (state) {

            case IDLE:
                break;

            case NORMAL:

                if (index == sizeUniteEnregistree() || allUsed()) {
                    index = 0;
                    setState(State.IDLE);

                } else {
                    selectedUnit = getIndexSelectedUnit(index);
                    setCurrentPosition(selectedUnit.getPosition());
                    setState(State.SELECT_CELL);
                }

                break;

            case SELECT_CELL:

                if (selectedUnit != null && !selectedUnit.getIsUsed()) {
                    if (waitFor(WAIT_FOR_PLAY, deltaTime)) {
                        setState(State.MOVE_UNIT);
                    }

                }
                break;

            case MOVE_UNIT:


                if (waitFor(WAIT_FOR_PLAY, deltaTime)) {
                    moveAIUnit(selectedUnit);
                    setState(State.ACTION_SELECTION);
                    setCurrentPosition(selectedUnit.getPosition());
                }


                break;

            case ACTION_SELECTION:

                if (!area.getTableauIndiceUnitAdverse(selectedUnit).isEmpty()) {
                    for (Action a : selectedUnit.getListActions()) {
                        if (a instanceof Attack) {
                            act = a;
                        }
                    }
                } else {
                    for (Action a : selectedUnit.getListActions()) {
                        if (a instanceof Wait) {
                            act = a;
                        }
                    }
                }
                selectedUnit.setIsUsed(true);
                state = State.ACTION;

                break;

            case ACTION:

                act.doAutoAction(deltaTime, this);
                index++;
                state = State.NORMAL;

                break;
        }

    }


    /**
     * Draw the AIPlayer, the cursor
     * @param canvas target, not null
     */
    public void draw(Canvas canvas) {
        if (state != State.IDLE && state != State.ACTION) {
            spriteUnit.draw(canvas);
        }

        if (act != null) {
            act.draw(canvas);
        }

    }


}
