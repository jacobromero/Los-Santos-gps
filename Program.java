/**
 * CS 241: Data Structures and Algorithms II
 * Professor: Edwin Rodr&iacute;guez
 *
 * Programming Assignment #3
 *
 * A program implmenting a couple concepts we learned in class
 * including Graphs, Weighted edges, and Dijkstra's Algorithm.
 * The program take a set of locations in 'Los Santos', and
 * will find, or navigate between locaitons in the city of
 * 'Los Santos'.
 *  
 * @author Jacob Romero
 * 
 */

/**
 * The main entry point for the Los Santos GPS.
 * 
 * @author Jacob Romero
 *
 */
public class Program {

	/**
	 * Main method for Los Santos GPS
	 * @param args
	 */
	public static void main(String[] args) {
		//Create Los Santos
		Gps Los_Santos = new Gps();
		
		//Add all the Los Santos locations
		Los_Santos.initGps();
		
		//Print the GPS menu.
		Los_Santos.printMenu();
	}
}