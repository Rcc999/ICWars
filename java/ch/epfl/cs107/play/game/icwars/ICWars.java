package ch.epfl.cs107.play.game.icwars;


import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.icwars.actor.ICWarsActor;
import ch.epfl.cs107.play.game.icwars.actor.player.AIPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.ICWarsPlayer;
import ch.epfl.cs107.play.game.icwars.actor.player.RealPlayer;
import ch.epfl.cs107.play.game.icwars.area.ICWarsArea;
import ch.epfl.cs107.play.game.icwars.area.Level0;
import ch.epfl.cs107.play.game.icwars.area.Level1;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;

public class ICWars extends AreaGame {

    public final static float CAMERA_SCALE_FACTOR = 10.f;


    private final String[] levels = {"icwars/Level0", "icwars/Level1"};
    private int levelsIndex;

    protected Dynamic dynamic;

    private ICWarsPlayer player1;
    private ICWarsPlayer player2;

    private ArrayList<ICWarsPlayer> listOfPlayer = new ArrayList<>();
    private ArrayList<ICWarsPlayer> currentWaitingPlayer = new ArrayList<>();
    private ArrayList<ICWarsPlayer> nextGamePlayer = new ArrayList<>();
    private ICWarsPlayer activePlayer;


    /**
     * creates the different areas we will be able to play on
     */
    private void createAreas() {
        addArea(new Level0());
        addArea(new Level1());
    }

    /**
     * begins the game by starting the few important and default settings
     * @param window
     * @param fileSystem
     * @return
     */
    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            levelsIndex = 0;
            initArea(levels[levelsIndex]);
            listOfPlayer.get(0).startTurn();
            dynamic = Dynamic.INIT;
            return true;
        }
        return false;
    }


    /**
     * initialize the area in which we play the game
     * @param areaKey
     */
    private void initArea(String areaKey) {
        listOfPlayer.clear();

        ICWarsArea area = (ICWarsArea) setCurrentArea(areaKey, true);
        area.clearRegisterUnitArea();
        DiscreteCoordinates coordsAlly = area.getPlayerSpawnPosition();
        DiscreteCoordinates coordsEnemy = area.getEnemySpawnPosition();

        player1 = new RealPlayer(area, coordsAlly, ICWarsActor.Faction.FRIENDLY);
        player2 = new AIPlayer(area, coordsEnemy, ICWarsActor.Faction.ENEMY);

        listOfPlayer.add(player1);
        listOfPlayer.add(player2);

        listOfPlayer.get(0).enterArea(area, coordsAlly);
        listOfPlayer.get(0).addUnitToArea(area.retourneTableauAllyUnit());

        listOfPlayer.get(1).enterArea(area, coordsEnemy);
        listOfPlayer.get(1).addUnitToArea(area.retourneTableauEnemyUnit());


    }


    /**
     * allows us to pass to the next level when we press N and change automatically when all the units are dead;
     */

    public void nextLevel() {
        Keyboard keyboard = getCurrentArea().getKeyboard();
        if (keyboard.get(Keyboard.N).isReleased() || listOfPlayer.get(0).playerIsDefeated() || listOfPlayer.get(1).playerIsDefeated() ) {
            if (levelsIndex == 0) {
                for (ICWarsPlayer icWarsPlayer : listOfPlayer) {
                    icWarsPlayer.leaveArea();
                }
                levelsIndex++;
                initArea(levels[levelsIndex]);
                nextGamePlayer.clear();
                currentWaitingPlayer.clear();
                dynamic = Dynamic.INIT;
                listOfPlayer.get(0).startTurn();
            } else {
                end();
            }
        }
    }


    /**
     * allows us to reset the game when we press R
     */
    public void reset() {
        Keyboard keyboard = getCurrentArea().getKeyboard();
        if (keyboard.get(Keyboard.R).isReleased()) {
            for (ICWarsPlayer icWarsPlayer : listOfPlayer) {
                icWarsPlayer.leaveArea();
            }
            levelsIndex = 0;
            initArea(levels[levelsIndex]);
            nextGamePlayer.clear();
            currentWaitingPlayer.clear();
            dynamic = Dynamic.INIT;
            listOfPlayer.get(0).startTurn();
        }
    }

    /** gives all the possible states of the game*/
    public enum Dynamic {
        INIT, CHOOSE_PLAYER, START_PLAYER_TURN, PLAYER_TURN, END_PLAYER_TURN, END_TURN, END;
    }

    /**
     * updates the game at all deltaTime
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        nextLevel();
        reset();
        dynamicUpdate();
    }


    /** updates the game considering it's state*/
    public void dynamicUpdate() {
        switch (dynamic) {

            case INIT:
                currentWaitingPlayer.addAll(listOfPlayer);
                dynamic = Dynamic.CHOOSE_PLAYER;
                break;

            case CHOOSE_PLAYER:
                if (currentWaitingPlayer.size() == 0) {
                    dynamic = Dynamic.END_TURN;
                } else if (!currentWaitingPlayer.get(0).playerIsDefeated()) {
                    activePlayer = currentWaitingPlayer.get(0);
                    currentWaitingPlayer.remove(0);
                    dynamic = Dynamic.START_PLAYER_TURN;
                } else {
                    currentWaitingPlayer.get(0).leaveArea();
                    currentWaitingPlayer.remove(0);
                    dynamic = Dynamic.END_TURN;
                }
                break;

            case START_PLAYER_TURN:
                activePlayer.startTurn();
                dynamic = Dynamic.PLAYER_TURN;
                break;

            case PLAYER_TURN:
                if (activePlayer.getState() == ICWarsPlayer.State.IDLE) {
                    dynamic = Dynamic.END_PLAYER_TURN;
                }
                break;

            case END_PLAYER_TURN:

                if (activePlayer.playerIsDefeated()) {
                    activePlayer.leaveArea();
                } else {
                    activePlayer.unused();
                    nextGamePlayer.add(activePlayer);
                }
                dynamic = Dynamic.CHOOSE_PLAYER;
                break;

            case END_TURN:
                if (nextGamePlayer.size() == 1) {
                    dynamic = Dynamic.END;
                } else {
                    currentWaitingPlayer.addAll(nextGamePlayer);
                    nextGamePlayer.clear();
                    dynamic = Dynamic.CHOOSE_PLAYER;
                }
                break;

            case END:
                if (!activePlayer.playerIsDefeated()) {
                    nextLevel();
                } else {
                    end();
                }
                break;
        }
    }


    /** ends the game*/
    @Override
    public void end() {
        System.out.println("Game over");
        System.exit(0);
    }

    /**
     * gets the title of the game
     * @return
     */
    @Override
    public String getTitle() {
        return "ICWars";
    }

}