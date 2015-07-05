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

import java.util.HashMap;

/**
 * The landmark class provides objects that contain locations of places in a graph, 
 * such as a name, keywords relevant to the location, an places on the graph a landmark is connected to
 * etc.
 * 
 * @author Jacob Romero
 *
 */
public class Landmark implements Comparable<Landmark> {
	//The name of the landmark
	//it is a constant since once it is made, it should not change.
	public final String name;
	//Keywords used to search for the landmark
	String[] keywords;
	//Locations connected to the landmark
    public HashMap<String, Edge> adjacencies = new HashMap<String, Edge>();
    //the distance used for dijkstra's shortest path
    public double minDistance = Double.POSITIVE_INFINITY;
    //used for tracing back from a target
    public Landmark previous;
    
    /**
     * Constructor for creating a landmark object.
     * 
     * @param argName
     * The name that will be given to an instance of landmark.
     * @param kw
     * A list of keywords that describe the landmark.
     */
    public Landmark(String argName, String[] kw){
    	name = argName; 
    	keywords = kw;
    }
    
    /**
     * Returns the name of the landmark.
     * As that is how a landmark is primarily identified.
     */
    public String toString(){
    	return name;
    }
    
    /**
     * Two landmarks are compared based on their distance in a path.
     */
    public int compareTo(Landmark other)
    {
        return Double.compare(minDistance, other.minDistance);
    }
}
