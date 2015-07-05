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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;


/**
 * A graph is a collections of nodes, or in this case
 * Landmarks, which also has a set of vertices connecting points on
 * out graph.
 * 
 * @author Jacob Romero
 *
 */

public class Graph {
	Scanner kb = new Scanner(System.in);
	
	//A Hashmap containing the locations of a graph.
	HashMap<String, Landmark> locations = new HashMap<String, Landmark>();
	
	/**
	 * Adds a landmark to the map.
	 * 
	 * @param name
	 * The name of the landmark
	 * @param kw
	 * Keywords describing the landmark, ex: Recreations, Fast-Food, etc.
	 */
	public void addLandmark(String name, String[] kw){
		//put the new location in the hashmap of locations for access later on.
		locations.put(name, new Landmark(name, kw));
	}
	
	/**
	 * Add a one way connection between landmarks.
	 * 
	 * @param to
	 * The location we are connecting to.
	 * @param from
	 * The locations that we are coming from.
	 * @param dist
	 * Distance that must be traveled to traverse the edge.
	 * @param dir
	 * Direction we go on when traversing the edge.
	 * @param street
	 * Name of the street the edge represents.
	 */
	public void addEdge(String to, String from, double dist, String dir, String street){
		//Get the landmark we want to build the edge from, and connect it the the landmark we
		//want to go to, with all details about that edge.
		locations.get(from).adjacencies.put(to, new Edge(locations.get(to), dist, dir, street));
	}
	
	/**
	 * Run Dijkstra's algorithm to compute the shortest path
	 * from a Landmark to all nodes, connecting them back to that edge.
	 * 
	 * @param source
	 * Location we are starting from.
	 */
	public void computePaths(Landmark source){
		//set the distance we have traveled to 0 since we are at the start node.
        source.minDistance = 0.;
        
        //Create a minimum priority queue, so we can quickly access the next smallest landmark. (log(v) time).
        //then add the initial location the the queue.
        PriorityQueue<Landmark> pq = new PriorityQueue<Landmark>();
        pq.add(source);        
        
        //now iterate through until every node has been visited.
	    while (!pq.isEmpty()) {
	    	//get element at the head of the queue
	        Landmark current = pq.poll();
	        
	        //Create an iterator to go through the all adjacent nodes of the current Landmark.
	        Iterator<Entry<String, Edge>> it = current.adjacencies.entrySet().iterator();
	        
            // Visit each edge exiting from the current landmark.
            while(it.hasNext())
            {
            	//Get an edge
            	Edge e = it.next().getValue();
            	
            	//Get the target of that edge.
                Landmark adjacentNode = e.target;
                double weight = e.distance;
                double distanceThroughCurrent = current.minDistance + weight;
                
                //If the current distance is less that the distance the Landmark has tagged add it to the queue, for shortest path.
                if (distanceThroughCurrent < adjacentNode.minDistance) {
                	//dequeue the head element in the priority queue, no longer needed.
		            pq.remove(current);
		            
		            //Tag the adjacent node with the updated cost of travel.
		            adjacentNode.minDistance = distanceThroughCurrent;
		            //Tag the node with the previous node linking to it that is the shortest.
		            adjacentNode.previous = current;
		            
		            //Make sure we never add the initial location, and get stuck in an infinite loop. 
//		            if(adjacentNode != source)
		            	pq.add(adjacentNode);
		        }		        
            }
	    }
    }
	
	/**
	 * Finds the next shortest path when using the navigate function.
	 * 
	 * @param target
	 * The location we want to navigate to.
	 * @param source
	 * The location we are starting the navigation from.
	 * @return
	 */
	public List<Landmark> reroute(Landmark target, Landmark source){
		Landmark min;
		Edge minEdge = null;
		for(Edge e : source.adjacencies.values()){
			if(minEdge == null || e.distance < minEdge.distance)
				minEdge = e;
		}
		
		min = minEdge.target;
		
		computePaths(min);
		List<Landmark> tmp = getPath(target);
       
		return tmp;
    }
	
	/**
	 * Returns a list of all the landmarks in the path we cross.
	 * Also prints out the directions the target landmark.
	 * 
	 * @param target
	 * The location we want to get directions to.
	 * 
	 * @return
	 * Returns a list of landmarks that we cross over on the way to the target.
	 */
	public List<Landmark> getDirections(Landmark target){
		//Create a list that will hold landmarks, then from the target, follow
		//the path of previous until we get to the source.
		List<Landmark> path = new ArrayList<Landmark>();
		for (Landmark lm = target; lm != null; lm = lm.previous)
			path.add(lm);
		
		//Reverse the list so it is in order according the the source.
		Collections.reverse(path);	
		
		//Print the directions of the list.
		for(int i = 0; i < path.size(); i++){
			try{
				Edge e = path.get(i).adjacencies.get(path.get(i+1).name);
				System.out.println("Head " + e.direction + " for " + e.distance + " miles on " +e.streetName + " toward " + path.get(i+1));
			}
			//If we go out of bounds on the index, we have reached the destination.
			catch(IndexOutOfBoundsException e){
				System.out.println("You have will arrive at " + path.get(i) + "!");
			}
		}
		
		return path;
	}
	
	public List<Landmark> getPath(Landmark target){
		//Create a list that will hold landmarks, then from the target, follow
		//the path of previous until we get to the source.
		List<Landmark> path = new ArrayList<Landmark>();
		for (Landmark lm = target; lm != null; lm = lm.previous)
			path.add(lm);
		
		//Reverse the list so it is in order according the the source.
		Collections.reverse(path);	
		
		return path;
	}
	
	/**
	 * Step by step navigation to the target that is passed in.
	 * 
	 * @param target
	 * The landmark, we wish to navigate to.
	 */
	public void navigateTo(Landmark target){
		//As with get directions, navigate the the target backwards, to the source.
		List<Landmark> path = new ArrayList<Landmark>();
		for (Landmark lm = target; lm != null; lm = lm.previous)
			path.add(lm);
		 
		//reverse the list.
		Collections.reverse(path);
		
		//user input for decisions later.
		int input;
		Landmark current = path.get(0);
		
		System.out.println("You are currently at " + path.get(0));
		System.out.println("Press 0 to continue on the shortest path to " + target);
		System.out.println("or press 1 to reroute");
		
		input = kb.nextInt();
		
		//print directions to the target.
		for(int i = 0; i < path.size(); i++){
			//if user presses 0 the continue on the path already calculated.
			if(input == 0 ){
				try{
					Edge e = path.get(i).adjacencies.get(path.get(i+1).name);
					System.out.println("Head " + e.direction + " for " + e.distance + " miles on " +e.streetName + " toward " + path.get(i+1));
				}
				catch(IndexOutOfBoundsException e){
					System.out.println("You have arrived at " + path.get(i) + "!");
				}
			}
			//if the user presses 1 then reroute.
			else{
				System.out.println("Rerouting...");
				
				path = reroute(target, current);
				i = 0;
				
				System.out.print("Press -> 0 to continue on new path: ");
			}
			input = kb.nextInt();
		}
	}
	
	/**
	 * Search the graph for any locations that contain the keywords given by the user
	 * 
	 * @param name
	 * Name of the keyword we are searching for.
	 * @return
	 * return a list of all locations containing the keyword.
	 */
	public List<String> searchKeywords(String name){
		List<String> list = new ArrayList<String>();		
		List<Landmark> visited = new ArrayList<Landmark>();
		
		//Depth first search of all locations in the graph
		Stack<Landmark> s = new Stack<Landmark>();
		
		//push the initial location of city hall on the stack
		s.push(locations.get("Los Santos City Hall"));
		Landmark current = locations.get("Los Santos City Hall");
		
		//do the searching
		while(!s.isEmpty()){
			current = s.pop();
			visited.add(current);
			
			//if the the list of keywords has the keyword parameter then add it to the results list.
			for(String str : current.keywords){
				if(str.toLowerCase().contains(name.toLowerCase())){
					list.add(current.name);
				}
			}
			
			Iterator<Entry<String, Edge>> it = current.adjacencies.entrySet().iterator();
			
			//for all adjacent landmarks add them to the stack
			while(it.hasNext()){
				Landmark x = it.next().getValue().target;
				
				if(!visited.contains(x) && !s.contains(x))
					s.push(x);
			}

		}

		//return the results of the search.
		return list;
	}
	
	/**
	 * Searches all landmarks on the graph for 
	 * a matching locations to the string argument.
	 * 
	 * @param name
	 * The name of the locations we want to search for.
	 * @return
	 * Returns a list of landmarks that have the name argument.
	 */
	public List<String> searchName(String name){
		List<String> results = new ArrayList<String>();
		
		//Gets all the keys in the hashmap of locations.
		//Then compares them to the argument for a match.
		for(String s : locations.keySet()){
			//If the key and the argument match then add it to the list.
			if(s.toLowerCase().contains(name.toLowerCase()))
				results.add(s);
		}
		
		//Return the results of the search.
		return results;
	}
	
}
