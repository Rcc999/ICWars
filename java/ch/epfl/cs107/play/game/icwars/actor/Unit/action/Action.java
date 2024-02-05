package ch.epfl.cs107.play.game.icwars.actor.Unit.action;

import ch.epfl.cs107.play.game.icwars.actor.Unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public abstract class Action {

    private Unit unit;
    private ICWarsArea area;
    private String name;
    private int key;


    /**
     * Action Constructor
     * @param unit
     * @param area
     * @param key
     * @param name
     */
    public Action(Unit unit, ICWarsArea area, int key, String name){
        this.unit = unit;
        this.key = key;
        this.area = area;
        this.name = name;
    }

    /** we declared all the abstract classes that we are going to redefine in Attack and Wait respectufully*/

    /**
     *
     * @param dt
     * @param player
     * @param keyboard
     */
    public abstract void doAction(float dt, ICWarsPlayer player, Keyboard keyboard);

    public abstract int getKey();

    public  abstract void draw(Canvas canvas);

    public abstract String getName();

    public abstract void doAutoAction(float dt, ICWarsPlayer player);


}
