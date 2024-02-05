package ch.epfl.cs107.play.game.icwars.area;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icwars.handler.ICWarsInteractionVisitor;
import ch.epfl.cs107.play.window.Window;

public class ICWarsBehavior extends AreaBehavior {

    public enum ICWarsCellType {

        NONE(0, 0),
        ROAD(-16777216, 0),
        PLAIN(-14112955, 1),
        WOOD(-65536, 3),
        RIVER(-16776961, 0),
        MOUNTAIN(-256, 4),
        CITY(-1, 2);


        final int type;
        final int defenseStar;

        /**
         * initialise the type and defenceStar
         *
         * @param type
         * @param defenseStar
         */
        ICWarsCellType(int type, int defenseStar) {
            this.type = type;
            this.defenseStar = defenseStar;
        }


        public static ICWarsCellType toType(int type) {
            for (ICWarsCellType ict : ICWarsCellType.values()) {
                if (ict.type == type)
                    return ict;
            }
            System.out.println(type);
            return NONE;
        }


        public String typeToString() {
            return "" + toType(this.type);
        }


        public int getDefenseStar() {
            return defenseStar;
        }
    }

    /**
     * Default ICWarsBehavior Constructor
     * @param window (Window), not null
     * @param name (String): Name of the Behavior, not null
     */
    public ICWarsBehavior(Window window, String name) {
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ICWarsCellType color = ICWarsCellType.toType(getRGB(height - 1 - y, x));
                setCell(x, y, new ICWarsCell(x, y, color));
            }
        }
    }

    /**
     * Cell adapted to the ICWars game
     */
    public class ICWarsCell extends AreaBehavior.Cell {


        private final ICWarsCellType type;

        /**
         * Default ICWarsCell Constructor
         *
         * @param x    (int): x coordinate of the cell
         * @param y    (int): y coordinate of the cell
         * @param type (EnigmeCellType), not null
         */


        public ICWarsCell(int x, int y, ICWarsCellType type) {
            super(x, y);
            this.type = type;
        }


        /**
         * allow us to get the acces to the type of a cell
         * @return
         */
        public ICWarsCellType getType() {
            return type;
        }

        /**
         * allows us to leave a cell
         *
         * @param entity (Interactable), not null
         * @return
         */
        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }


        /**
         * Allow us to enter a cell if no units are already in there
         * @param entity (Interactable), not null
         * @return
         */
        @Override
        protected boolean canEnter(Interactable entity) {
            for (Interactable value : entities) {
                if (value.takeCellSpace() && entity.takeCellSpace()) {
                    return false;
                }
            }
            return true;
        }

        /**
         * allows us to interact with the cells
         *
         * @return
         */
        @Override
        public boolean isCellInteractable() {
            return true;
        }

        /**
         * doesn't allow to have an effect on the view
         *
         * @return
         */
        @Override
        public boolean isViewInteractable() {
            return false;
        }

        /**
         * allows us to accept interactions
         *
         * @param v (AreaInteractionVisitor) : the visitor
         */
        @Override
        public void acceptInteraction(AreaInteractionVisitor v) {
            ((ICWarsInteractionVisitor) v).interactWith(this);
        }

    }
}
