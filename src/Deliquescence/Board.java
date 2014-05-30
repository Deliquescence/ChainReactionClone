/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Deliquescence;

import Deliquescence.Panel.GamePanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

/**
 * A Board of the game. It has the {@link Tile}s that make up gameplay.
 *
 * @author Josh
 */
public class Board extends JPanel {

    private int numRows;
    private int numCols;
    private int numPlayers;
    private int turn;
    private int currentPlayerID;
    private Player currentPlayer;

    /**
     * Array of the {@link Player}s in the game.
     */
    public Player[] players;

    /**
     * The field containing the {@link Tile}s of the game.
     */
    protected Tile[][] field;
    private Tile[][] fieldPrevious;

    private boolean inGame;
    private boolean inReaction;

    private final GamePanel gamePanel;

    /**
     * Create a new game board.
     *
     * @param parent The {@link GamePanel} that contains this board.
     * @param NumberOfPlayers The number of players in this board.
     * @param Rows The number of rows in this game.
     * @param Columns The number of columns in this game.
     * @param playerNames A String array containing friendly names of the players.
     * @param RandomizePlayerStart True to choose a random player to start.
     */
    public Board(GamePanel parent, int NumberOfPlayers, int Rows, int Columns, String[] playerNames, boolean RandomizePlayerStart) {
        this.numPlayers = NumberOfPlayers;
        this.numRows = Rows;
        this.numCols = Columns;
        this.gamePanel = parent;
        this.turn = 1;

        this.players = new Player[Config.getInt("MAX_PLAYERS") + 1];
        players[0] = new Player(0);
        for (int i = 1; i < players.length; i++) {
            players[i] = new Player(i, playerNames[i]);
        }

        setDoubleBuffered(true);
        addMouseListener(new MyMouseAdapter());
        newGame(RandomizePlayerStart);
        //MyMouseAdapter will get clicks to the board
    }

    private void newGame(boolean randomizePlayer) {
        //Create blank tiles
        field = new Tile[numCols][numRows];
        for (int x = 0; x < numCols; x++) {
            for (int y = 0; y < numRows; y++) {
                field[x][y] = new Tile(this, x, y);
            }
        }
        inGame = true;
        if (randomizePlayer) {
            Random random = new Random();

            setCurrentPlayerByID(random.nextInt(numPlayers) + 1);
        } else {
            setCurrentPlayerByID(1);
        }
        refreshDisplayPlayers();
        repaint();
    }

    private Tile[] getAllTiles() {
        return getAllTiles(this.field);
    }

    private Tile[] getAllTiles(Tile[][] theField) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int x = 0; x < numCols; x++) {
            for (int y = 0; y < numRows; y++) {
                tiles.add(theField[x][y]);
            }
        }
        return tiles.toArray(new Tile[0]);
    }

    private boolean playerIsAlive(Player p) {
        if (turn <= numPlayers) {
            return true;//Players may be incorrectly labled as dead
        } else {
            return p.isAlive();
        }
    }

    private boolean setCurrentPlayerByID(int pID) {
        if (!playerIsAlive(players[pID])) {
            return false;
        }
        this.currentPlayerID = pID;
        this.currentPlayer = players[pID];
        this.gamePanel.setPlayerStatus(currentPlayer.getColor(), currentPlayer.getDisplayName() + "'s Turn");
        return true;
    }

    private void incrementPlayer(int curPlayerID) {
        int targetID = 0;
        if (curPlayerID == numPlayers) {
            targetID = 1;
        } else {
            targetID = curPlayerID + 1;
        }

        if (setCurrentPlayerByID(targetID)) {
            gamePanel.resetTimer();
            turn++;
        } else {
            incrementPlayer(targetID);
        }
    }

    private Tile[] getSurroundingTiles(Tile t) {
        ArrayList<Tile> tiles = new ArrayList<>();
        //All these trys are somewhat sketchy
        //But putting all the .add's in one would sometimes stop the lower ones from being called
        try {
            tiles.add(field[t.getX() - 1][t.getY()]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            //Ignore
        }

        try {
            tiles.add(field[t.getX() + 1][t.getY()]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            //Ignore
        }

        try {
            tiles.add(field[t.getX()][t.getY() - 1]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            //Ignore
        }

        try {
            tiles.add(field[t.getX()][t.getY() + 1]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            //Ignore
        }

        return tiles.toArray(new Tile[1]);
    }

    private void doReaction(Tile InitiatingTile) throws StackOverflowError {//todo ajdust to look better
        //-Xss JVM arg will change stack size
        if (gameWon()) {
            return;
        }
        boolean dirty = false;
        if (inGame) {
            if (InitiatingTile.canExplode()) {
                InitiatingTile.setNumberOfParticles(0);
                for (Tile t : getSurroundingTiles(InitiatingTile)) {
                    t.setOwner(InitiatingTile.getOwner());
                    t.setNumberOfParticles(t.getNumberOfParticles() + 1);
                    dirty = true;
                    if (dirty) {
                        paintImmediately(0, 0, numCols * Config.getInt("CELL_SIZE"), numRows * Config.getInt("CELL_SIZE"));
                        sleep();
                    }

                    doReaction(t);
                }
                if (InitiatingTile.getNumberOfParticles() == 0) { //Clear owner of empty tile
                    InitiatingTile.setOwner(players[0]);//Todo Fix rare orphaned particles
                }
            }
        }
    }

    private Tile[] explodeTile(Tile tile) {
        ArrayList<Tile> dirtyTiles = new ArrayList<>();//Does not include initiating tile
        for (Tile t : getSurroundingTiles(tile)) {
            t.setOwner(tile.getOwner());
            t.setNumberOfParticles(t.getNumberOfParticles() + 1);
            dirtyTiles.add(t);
        }
        tile.setNumberOfParticles(0);
        tile.setOwner(players[0]);
        return dirtyTiles.toArray(new Tile[0]);
    }

    private void sleep() {
        try {
            Thread.sleep(100);
        } catch (Exception er) {

        }
    }

    /**
     * Gets the number of columns in this game.
     *
     * @return The number of columns.
     */
    public int getCols() {
        return this.numCols;
    }

    /**
     * Gets the number of rows in this game.
     *
     * @return The rows of columns.
     */
    public int getRows() {
        return this.numRows;
    }

    /**
     * Used for RNG turns.
     */
    private Tile getRandomTile() {
        Random random = new Random();
        int x = random.nextInt(numCols);
        int y = random.nextInt(numRows);

        return field[x][y];
    }

    /**
     * Preform a move as the current player on a random tile.
     */
    public void RNGTurn() {
        if (!inGame || inReaction) {
            return;
        }
        while (!doTurn(getRandomTile())) {//Try random tiles until works

        }
    }

    public void SkipTurn() {//This might be glitchy and may need to be removed
        if (!inGame || inReaction) {
            return;
        }
        incrementPlayer(currentPlayerID);
    }

    /**
     * Attempts to add a particle to the specified {@link Tile}, but may not be successful. For example, will fail if the tile is not owned by the current player or the game is not running.
     *
     * @param onTile The tile that will be added to.
     *
     * @return True if the particle was successfully added.
     */
    public boolean doTurn(Tile onTile) {
        boolean dirty = false;
        Tile[][] fieldPreviousTemp = new Tile[numCols][numRows];//Temp because the turn may not be valid
        for (int fieldx = 0; fieldx < numCols; fieldx++) {
            for (int fieldy = 0; fieldy < numRows; fieldy++) {
                fieldPreviousTemp[fieldx][fieldy] = field[fieldx][fieldy].cloneTile();
            }
        }
        if (inGame) {
            if (onTile.getOwnerID() == 0) { //Unowned, can claim
                dirty = true;
                onTile.setOwner(currentPlayer);
                onTile.setNumberOfParticles(onTile.getNumberOfParticles() + 1);//Increase particle count
            } else { //Is owned
                if (onTile.getOwner() == currentPlayer) { //Make sure it is their tile
                    dirty = true;
                    onTile.setNumberOfParticles(onTile.getNumberOfParticles() + 1);//Increase particle count
                } else { //Cannot play on others tiles
                    return false;
                }
            }
        }
        if (dirty) {//Need to repaint and do reactions
            fieldPrevious = fieldPreviousTemp;

            inReaction = true;
            try {
                doReaction(onTile);
            } catch (StackOverflowError er) {

            }
            inReaction = false;

            if (!gameWon()) {
                incrementPlayer(currentPlayerID);//Only increment if not won
            }

            repaint();
            return true;
        }
        return false;
    }

    private void refreshDisplayPlayers() {
        ArrayList<Player> displayPlayers = new ArrayList<>();
        if (turn <= numPlayers) {
            for (int i = 1; i <= numPlayers; i++) {
                players[i].setLiving(true);
            }
        }
        for (int i = 1; i <= numPlayers; i++) {
            displayPlayers.add(players[i]);
        }
        gamePanel.refreshPlayerList(displayPlayers.toArray(new Player[0]));
    }

    /**
     * Undos the last move made on the board. Can only undo once.
     */
    public void undo() {
        if (inReaction || !inGame) {
            return;
        }
        //Check if tiles are same
        boolean allSame = true;
        for (int x = 0; x < numCols; x++) {
            for (int y = 0; y < numRows; y++) {
                if (field[x][y].equals(fieldPrevious[x][y])) {
                } else {
                    allSame = false;
                    break;
                }
            }
        }

        if (allSame) {
            return;
        }

        this.field = this.fieldPrevious.clone();
        //Decrement player
        if (currentPlayerID == 1) {
            setCurrentPlayerByID(numPlayers);
        } else {
            setCurrentPlayerByID(currentPlayerID - 1);
        }
        turn--;

        repaint();
    }

    private boolean gameWon() {
        refreshDisplayPlayers();

        //If the first turn is checked things dont work right
        if (turn <= numPlayers) {
            return false;
        }
        //Reset everyone to dead and fill in the live ones later
        for (Player player : players) {
            player.setLiving(false);
        }
        //Determine living players
        repaint();
        for (Tile tile : getAllTiles()) {
            if (tile.getNumberOfParticles() > 0) {
                tile.getOwner().setLiving(true);
            }
        }
        refreshDisplayPlayers();

        //check if more than one player living; if not winner is found
        int numberAlive = 0;
        for (Player p : players) {
            if (p.isAlive()) {
                numberAlive++;
            }
        }
        if (numberAlive > 1) {//no winner
            return false;
        } else {
            for (Player p : players) {
                if (p.isAlive()) {
                    inGame = false;
                    gamePanel.setPlayerStatus(currentPlayer.getColor(), p.getDisplayName() + " Wins!");
                    gamePanel.stopTimer();
                }
            }
            return true;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        //Clear everything, resolves a graphical glitch at bottom of grid
        g.setColor(gamePanel.getBackground());
        g.fillRect(0, 0, numCols * Config.getInt("CELL_SIZE") + 1000, numRows * Config.getInt("CELL_SIZE") + 1000);

        for (Tile tile : getAllTiles()) {
            //Draw particles (spheres)
            Image theImage;
            theImage = Config.getImageByPlayerID(tile.getOwnerID(), tile.getNumberOfParticles());

            g.drawImage(theImage, (tile.getX() * Config.getInt("CELL_SIZE")), (tile.getY() * Config.getInt("CELL_SIZE")), this);
        }
    }

    class MyMouseAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            int clickedCol = x / Config.getInt("CELL_SIZE");
            int clickedRow = y / Config.getInt("CELL_SIZE");

            if ((clickedCol >= numCols) || (clickedRow >= numRows)) { //Out of bounds
                return;
            }

            Tile tile = field[clickedCol][clickedRow];
            if ((x < numCols * Config.getInt("CELL_SIZE")) && (y < numRows * Config.getInt("CELL_SIZE"))) { //in bounds
                doTurn(tile);
            }
        }
    }
}
