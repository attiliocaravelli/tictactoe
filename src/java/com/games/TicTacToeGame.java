/**
 * 
 */
package com.games;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author attilio
 *
 */
public class TicTacToeGame {

	private final Scanner in = new Scanner(System.in);
	private final PrintStream out = System.out;
	private final Game game = new Game();
	
	/**
	 * 
	 */
	public TicTacToeGame() {
	
	}

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args)  {
		if (args.length == 1) {
			File file = new File(args[0]);
		 try {

		        Scanner sc = new Scanner(file);

		        while (sc.hasNextLine()) {
		            int i = sc.nextInt();
		            System.out.println(i);
		        }
		        sc.close();
		    } 
		    catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }
			}
			else System.out.println("Usage: java TicTacToeGame.class fullpath_filename");
		 
			TicTacToeGame ticTacToeGame = new TicTacToeGame();
			ticTacToeGame.out.println("Welcome to TicTacToe.");
			ticTacToeGame.out.println(ticTacToeGame.game);
		    while (ticTacToeGame.game.isRunning()) {
		    	ticTacToeGame.informPlayersOfNextTurn();
		        ticTacToeGame.makeNextMove();
		        ticTacToeGame.out.println(ticTacToeGame.game);
		    }
		    ticTacToeGame.out.println(ticTacToeGame.game.getResult());
	}
	
	private void informPlayersOfNextTurn() {
	    String message = "Player %s, it's your turn. Make your move!\n";
	    out.printf(message, game.getCurrentPlayer());
	}
	
	private void makeNextMove() {
	    Move move = askForMove();
	    while (!game.isMoveAllowed(move)) {
	        out.println("Illegal move, try again.");
	        move = askForMove();
	    }
	    game.makeMove(move);
	}
	
	private Move askForMove() {
	    int x = askForCoordinate("horizontal");
	    int y = askForCoordinate("vertical");
	    return new Move(x - 1, y - 1);
	}
	
	private int askForCoordinate(String coordinate) {
	    out.printf("Enter the %s coordinate of the cell [1-3]: ", coordinate);
	    while (!in.hasNextInt()) {
	        out.print("Invalid number, re-enter: ");
	        in.next();
	    }
	    return in.nextInt();
	}
	
	
	public class Game {
	    private final Board board = new Board();
	    private Player currentPlayer = Player.X;
	    public boolean isRunning() {
	        return !currentPlayer.isWinner && !board.isFull();
	    }
	    public Player getCurrentPlayer() {
	        return currentPlayer;
	    }

	    public boolean isMoveAllowed(Move move) {
	        return isRunning() && board.isCellEmpty(move.X, move.Y);
	    }
	    
	    public void makeMove(Move move) {
	        if (!isRunning()) {
	            throw new IllegalStateException("Game has ended!");
	        }
	        board.setCell(move.X, move.Y, currentPlayer);
	        if (!currentPlayer.isWinner) {
	            currentPlayer = currentPlayer.getNextPlayer();
	        }
	    }
	    
	    public GameResult getResult() {
	        if (isRunning()) {
	            throw new IllegalStateException("Game is still running!");
	        }
	        return new GameResult(currentPlayer);
	    }
	    
	    @Override
	    public String toString() {
	        return board.toString();
	    }

	}

	public class Board {
	    private final int LENGTH = 3;
	    private final Player[][] cells = new Player[LENGTH][LENGTH];
	    private int numberOfMoves = 0;
	    
	 public Board() {
	        for (Player[] row : cells)
	            Arrays.fill(row, Player.Blank);
	    }
	
	public void setCell(int x, int y, Player player) {
        cells[x][y] = player;
        numberOfMoves++;
        int row = 0, column = 0, diagonal = 0, antiDiagonal = 0;
        for (int i = 0; i < LENGTH; i++) {
            if (cells[x][i] == player) column++;
            if (cells[i][y] == player) row++;
            if (cells[i][i] == player) diagonal++;
            if (cells[i][LENGTH - i - 1] == player) antiDiagonal++;
        }
        player.isWinner = isAnyLongEnough(row, column, diagonal, antiDiagonal);
    }

    private boolean isAnyLongEnough(int... combinationLengths) {
        Arrays.sort(combinationLengths);
        return Arrays.binarySearch(combinationLengths, LENGTH) >= 0;
    }
	
	 public boolean isCellEmpty(int x, int y) {
	        boolean isInsideBoard = x < LENGTH && y < LENGTH && x >= 0 && y >= 0;
	        return isInsideBoard && cells[x][y] == Player.Blank;
	    }
	
	 public boolean isFull() {
	        return numberOfMoves == LENGTH * LENGTH;
	    }
	 
	 @Override
	    public String toString() {
	        final String horizontalLine = "-+-+-\n";
	        StringBuilder builder = new StringBuilder();
	        for (int row = 0; row < cells.length; row++) {
	            for (int column = 0; column < cells[row].length; column++) {
	                builder.append(cells[column][row]);
	                if (column < cells[row].length - 1)
	                    builder.append('|');
	            }
	            if (row < cells.length - 1)
	                builder.append('\n').append(horizontalLine);
	        }
	        return builder.toString();
	    }
	}
	 
	 
	public class GameResult {
	    private final Player player;

	    public GameResult(Player lastPlayer) {
	        player = lastPlayer;
	    }

	    @Override
	    public String toString() {
	        String winner = player.isWinner ? player.toString() : "Nobody";
	        return String.format("%s won. Thank you for playing.", winner);
	    }
	}
	
	public class Move {

	    public final int X;
	    public final int Y;

	    public Move(int x, int y){
	        X = x;
	        Y = y;
	    }
	}
	
	public enum Player {
	    X, O, Blank {
	        @Override // to give us a blank space on the board
	        public String toString() {
	            return " ";
	        }
	        
	        
	    };

		
	    public boolean isWinner = false;

	    public Player getNextPlayer() {
	        return this == Player.X ? Player.O : Player.X;
	    }

	}
}
