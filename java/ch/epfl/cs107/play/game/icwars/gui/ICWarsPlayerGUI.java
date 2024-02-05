package ch.epfl.cs107.play.game.icwars.gui;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.icwars.actor.Unit.Unit;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsBehavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class ICWarsPlayerGUI implements Graphics {

    public static final float FONT_SIZE = 20.f;
    private final ICWarsPlayer player;
    private Unit unitSelected;
    private final ICWarsActionsPanel panelAction = new ICWarsActionsPanel(10.f);
    private final ICWarsInfoPanel infoPanel = new ICWarsInfoPanel(10.f);


    /**
     * calls the GUI on a player with a certain camera scale factor
     *
     * @param cameraScaleFactor
     * @param player
     */
    public ICWarsPlayerGUI(float cameraScaleFactor, ICWarsPlayer player) {
        this.player = player;
    }

    /**
     * Set a selects a unit
     *
     * @param unit
     */
    public void setSelectedUnit(Unit unit) {
        unitSelected = unit;
    }


    /**
     * sets the specific unit's informations on the infopanel
     *
     * @param unit
     */
    public void setInfoPanel(Unit unit) {
        infoPanel.setUnit((unit));
    }


    /**
     * sets the specific cell's informations on the infopanel
     *
     * @param celluleInfoPanel
     */
    public void setCelluleInfoPanel(ICWarsBehavior.ICWarsCellType celluleInfoPanel) {
        infoPanel.setCurrentCell(celluleInfoPanel);
    }

    /**
     * draws the player GUI throughout  different panels such as the information pannel, action pannel and the path of the player;
     *
     * @param canvas target, not null
     */
    @Override
    public void draw(Canvas canvas) {
        DiscreteCoordinates coords = new DiscreteCoordinates((int) player.getPosition().x, (int) player.getPosition().y);

        if (unitSelected != null && player.getState() == ICWarsPlayer.State.MOVE_UNIT)
            unitSelected.drawRangeAndPathTo(coords, canvas);


        if (player.getState() == ICWarsPlayer.State.ACTION_SELECTION && unitSelected != null) {
            panelAction.setActions(unitSelected.getListActions());
            panelAction.draw(canvas);
        }


        if (player.getState() == ICWarsPlayer.State.NORMAL || player.getState() == ICWarsPlayer.State.SELECT_CELL) {
            infoPanel.draw(canvas);
        }
    }
}
