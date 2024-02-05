package ch.epfl.cs107.play.game.icwars.actor.Unit.action;

import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.icwars.actor.Unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.ArrayList;

public class Attack extends Action {

    private final ImageGraphics cursor = new ImageGraphics(ResourcePath.getSprite("icwars/UIpackSheet"), 1.f, 1.f, new RegionOfInterest(4 * 18, 26 * 18, 16, 16));
    private ICWarsArea area;
    private Unit unit;
    private boolean peutDessiner = false;

    private int indiceUnitAdverse;
    private ArrayList<Integer> tableauIndiceUnitAdverse = new ArrayList();


    /**
     * Attack Constructor
     *
     * @param unit
     * @param area
     */
    public Attack(Unit unit, ICWarsArea area) {
        super(unit, area, Keyboard.A, "(A)ttack");
        this.unit = unit;
        this.area = area;
    }


    /**
     * allows us to choose between the different targets and to attack the target with our unit
     *
     * @param dt
     * @param player
     * @param keyboard
     */
    @Override
    public void doAction(float dt, ICWarsPlayer player, Keyboard keyboard) {

        tableauIndiceUnitAdverse = area.getTableauIndiceUnitAdverse(unit);
        if (tableauIndiceUnitAdverse.size() >= 1) {

            if (keyboard.get(Keyboard.RIGHT).isPressed()) {
                if (indiceUnitAdverse == tableauIndiceUnitAdverse.size() - 1) {
                    indiceUnitAdverse = 0;
                } else {
                    ++indiceUnitAdverse;
                }
            }

            if (keyboard.get(Keyboard.LEFT).isPressed()) {
                if (indiceUnitAdverse == 0) {
                    indiceUnitAdverse = tableauIndiceUnitAdverse.size() - 1;
                } else {
                    --indiceUnitAdverse;
                }
            }
        }

        if (tableauIndiceUnitAdverse.size() > 1) {
            peutDessiner = true;
            area.setCamera(tableauIndiceUnitAdverse.get(indiceUnitAdverse));
            unit.setIndiceEnemy(tableauIndiceUnitAdverse.get(indiceUnitAdverse));
        }

        if (keyboard.get(Keyboard.ENTER).isPressed()) {
            peutDessiner = true;
            area.setDegat(tableauIndiceUnitAdverse.get(indiceUnitAdverse), unit.getDamage());
            unit.setIsUsed(true);
            unit.setIndiceEnemy(-1);
            player.setState(ICWarsPlayer.State.NORMAL);
            player.selectUnit(null);
            area.setViewCandidate(player);
            indiceUnitAdverse = 0;
            area.resetListeIndiceUniteAdverse();
            tableauIndiceUnitAdverse.clear();

        }

        if (tableauIndiceUnitAdverse.isEmpty()) {
            peutDessiner = false;
            player.setState(ICWarsPlayer.State.NORMAL);
            player.centerCamera();
            indiceUnitAdverse = 0;
            tableauIndiceUnitAdverse.clear();
        }

        if (keyboard.get(Keyboard.TAB).isPressed()) {
            peutDessiner = false;
            unit.setIsUsed(false);
            player.setState(ICWarsPlayer.State.ACTION_SELECTION);
            player.centerCamera();
        }
    }


    public void doAutoAction(float dt, ICWarsPlayer player) {
        tableauIndiceUnitAdverse = area.getTableauIndiceUnitAdverse(unit);
        if (!tableauIndiceUnitAdverse.isEmpty()) {
            int index = area.lowestHpUnit(tableauIndiceUnitAdverse);
            area.setDegat(index, unit.getDamage());
            player.setState(ICWarsPlayer.State.NORMAL);
        } else {
            //player.centerCamera();
            player.setState(ICWarsPlayer.State.ACTION_SELECTION);
        }
        tableauIndiceUnitAdverse.clear();
        area.resetListeIndiceUniteAdverse();
    }

    /**
     * allows us to recognize the keyboard input for A
     *
     * @return
     */
    @Override
    public int getKey() {
        return Keyboard.A;
    }

    /**
     * sets the name of the method for the GUI
     *
     * @return
     */
    @Override
    public String getName() {
        return "(A)ttack";
    }


    /**
     * Let us execute the action and draw the little hand next to unit selected that we want to attack
     *
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        if (peutDessiner) {
            if (!tableauIndiceUnitAdverse.isEmpty() && unit.getIndiceEnemy() >= 0) {
                area.setCamera(unit.getIndiceEnemy());
                cursor.setAnchor(canvas.getPosition().add(1, 0));
                cursor.setDepth(2);
                cursor.draw(canvas);
            }
        }
    }
}
