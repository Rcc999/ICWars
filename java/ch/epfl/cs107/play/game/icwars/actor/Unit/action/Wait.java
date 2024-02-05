package ch.epfl.cs107.play.game.icwars.actor.Unit.action;

import ch.epfl.cs107.play.game.icwars.actor.Unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class Wait extends Action{

    private ICWarsArea area;
    private Unit unit;



    /**
     *  calls the super class and sets the area and unit
     * @param unit
     * @param area
     */
    public Wait (Unit unit, ICWarsArea area)  {
        super(unit, area, Keyboard.W, "(W)ait");
        this.area = area;
        this.unit = unit;
    }

    /**
     * allows us to recognize the keyboard input for W
     * @return
     */
    @Override
    public int getKey() { return Keyboard.W; }


    /**
     * gets the name of the action for the GUI
     * @return
     */
    @Override
    public String getName() {
        return "(W)ait";
    }


    @Override
    public void draw(Canvas canvas) {}

    /**
     * execute the action of a RealPlayer
     * @param dt
     * @param player
     * @param keyboard
     */
    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {
        unit.setIsUsed(true);
        player.setState(ICWarsPlayer.State.NORMAL);
    }

    /**
     * execute the action of a AIPlayer
     * @param dt
     * @param player
     */
    @Override
    public void doAutoAction(float dt, ICWarsPlayer player){
        unit.setIsUsed(true);
        player.setState(ICWarsPlayer.State.NORMAL);
    }

}
