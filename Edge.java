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
 * Edge class that contains the weight, target location, direction of the street,
 * and the street that the edge corresponds to.
 * 
 * @author Jacob Romero
 *
 */
public class Edge {
	//The target location of the edge, ie city hall's target is centennial theater.
	public final Landmark target;
	//The distance form the source landmark to the target.
    public final double distance;
    //The compass directions to the landmark (north, south, east, west).
    public final String direction;
    //the street the edge represents.
    public final String streetName;
    
    /**
     * Constructor for creating an edge.
     * 
     * @param tar
     * The target location of the edge.
     * @param dist
     * The distance of the edge(street).
     * @param dir
     * The compass direction of the street (North, South, East, West).
     * @param street
     * The street name.
     */
    public Edge(Landmark tar, double dist, String dir, String street){
    	target = tar; 
    	distance = dist; 
    	direction = dir;
    	streetName = street;
    }
}
