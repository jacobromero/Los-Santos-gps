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

import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * A class the acts as the main program of the Los Santos
 * Gps.
 * 
 * @author Jacob Romero
 *
 */
public class Gps {
	private Scanner kb = new Scanner(System.in);
	//The location we will be starting at.
	private Landmark initialLocation;
	private int input;
	//Create a Los Santos graph.
	private Graph ls = new Graph();
	
	/**
	 * Print the menu of actions the user may perform.
	 */
	public void printMenu(){
		System.out.println("Welcome to the wonderful city of Los Santos!");
		System.out.println("You are currently at " + initialLocation);
		
		System.out.println();
		
		System.out.println("To Find a destination press -> 0.");
		System.out.println("For navigation between two locations press -> 1.");
		System.out.println("To quit press -> Any other number.");
		
		System.out.println();
		
		System.out.println("Enter your selection: ");
		
		try{
			input = kb.nextInt();	
		}
		//Catch invalid input.
		catch(InputMismatchException e){
			System.out.println("You have entered invalid input pls try again.");
			
			clearScreen();
			
			//Clear keyboard buffer.
			kb.nextLine();
			
			//reset input.
			input = 0;
			
			printMenu();
		}
		
		//figure out the input from the user.
		calcInput(input);
	}
	
	/**
	 * Perform certain actions based on the user input.
	 * 
	 * @param input
	 * The input of the user, used for determining the next course of action.
	 */
	private void calcInput(int input){
		//if the input is 0 the we perform a find on either keyword, or by name.
		if(input == 0){
			search();
		}
		//If the user input from the main menu is 1
		else if(input == 1){
			pointToPoint();
		}
		else{
			exit();
		}
	}

	/**
	 * If the user input was 0 the we will be performing
	 * some sort of search, either keyword, or by name.
	 * This method will handle that.
	 */
	private void search(){
		boolean flag = true;
		
		String kwSearch;
		System.out.println("To search by keyword press -> 0.");
		System.out.println("To search by name press -> 1.");
		System.out.println("To go back press -> Any other number.");
		
		do{
			try{
				input = kb.nextInt();
				flag = false;
			}
			catch(InputMismatchException e){
				System.out.println("Sorry unrecognized input, please try again.");
				kb.nextLine();
			}
		}while(flag);
		
		
		//Search by keyword if the user enters 0
		if(input == 0){
			System.out.print("Enter the keyword you want to search for: ");
			kwSearch = kb.next();
			
			//Get results of the keyword search
			List<String> results = ls.searchKeywords(kwSearch);
			
			System.out.println();
			
			//Print the results to the user.
			System.out.println("0) Select closest " + kwSearch.toUpperCase());
			for(int i = 0; i < results.size(); i++){
				System.out.println(i+1 + ") " + results.get(i));
			}
			
			//Clear keyboard buffer.
			kb.nextLine();
			
			System.out.print("Select location: ");
			
			do{
				try{
					//Get the desired location.
					int location = kb.nextInt();
					flag = false;
			
					//If the user doesn't want the closest location then enter 1 less that the location.
					if(location > 0)
						location -= 1;
					
					System.out.println();
					
					System.out.println("Press -> 0 to get directions to " + results.get(location));
					System.out.println("Press -> 1 to navigate to " + results.get(location));
					
					//Run Dijkstra's
					ls.computePaths(initialLocation);
					
					do{
						input = kb.nextInt();
						//Print directions if input is 0
						if(input == 0){
							ls.getDirections(ls.locations.get(results.get(location)));
							break;
						}
						//Print step by step if input is 1
						else if(input == 1){
							ls.navigateTo(ls.locations.get(results.get(location)));
							break;
						}
						//Input is not understood otherwise.
						else{
							System.out.println("Sorry I didnt understand your command.");
						}
					}while(input > 1);
				}
				catch(InputMismatchException e){
					flag = true;
					
					System.out.println("Unrecognized input please try again.");
					kb.nextLine();
				}
			}while(flag);
		}
		//Perform a search by name.
		else if(input == 1){
			System.out.print("Enter the Location you want to search for (Exactly): ");
			kb.nextLine();
			kwSearch = kb.nextLine();

			List<String> results = ls.searchName(kwSearch);

			//If the list is empty of the first index is null then the location was not found.
			if(results == null || results.get(0) == null){
				System.out.println("Im sorry that location does not exist.");
				return;
			}
			
			//Otherwise we should only find one location if the exact name is used for search so we can list the first element.
			System.out.println("Press -> 0 to get directions to " + results.get(0));
			System.out.println("Press -> 1 to navigate to " + results.get(0));
			
			//Run Dijkstra's
			ls.computePaths(initialLocation);
			
			do{
				try{
					do{
						input = kb.nextInt();
						flag = true;
						//Print all directions at once if input is 0
						if(input == 0){
							ls.getDirections(ls.locations.get(results.get(0)));
							break;
						}
						//Otherwise step-by-step navigation
						else if(input == 1){
							ls.navigateTo(ls.locations.get(results.get(0)));
							break;
						}
						//Or not recognized input.
						else{
							System.out.println("Sorry I didnt understand your command.");
						}
					}while(input > 1);
				}
				catch(InputMismatchException e){
					flag = true;
					
					System.out.println("Sorry unrecognized input, please try again.");
					kb.nextLine();
				}
			}while(flag);
		}
		//if the user want to return to the main menu print white space then print the menu again.
		else{
			clearScreen();
			printMenu();
		}
	}
	
	/**
	 * If the user input was 1 then we will perform
	 * a point to point navigation based on the user's choices
	 * This method will handle that.
	 */
	private void pointToPoint(){
		//String that we will be navigating from and to.
		String begin = "";
		String end = "";
		
		kb.nextLine();
		
		//Get the locations of beginning and end points.
		System.out.print("Enter the location you will be starting from: ");
		
		//if the location entered is not a location keep prompting.
		do{
			begin = kb.nextLine();
			
			if(!ls.locations.keySet().contains(begin))
				System.out.println("That is not a locations, try again.\n");
		}while(!ls.locations.keySet().contains(begin));
		
		System.out.print("Enter the location you want to navigate to: ");
		//if the location entered is not a location keep prompting.
		do{
			end = kb.nextLine();
			if(!ls.locations.keySet().contains(begin))
				System.out.println("That is not a locations, try again.\n");
		}while(!ls.locations.keySet().contains(begin));
		
		//Run Dijkstra's and print directions.
		ls.computePaths(ls.locations.get(begin));
		System.out.println(ls.getDirections(ls.locations.get(end)));
	}
	
	/**
	 * Exit the program printing a special message based on the time of the day.
	 */
	private void exit(){
		try{
			Calendar calendar = Calendar.getInstance();
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			String farwell = "Thank you for using Los Santos gps, have a good ";
			
			//If the hour of the day is after 6 am and before 5 pm
			//print have a good day.
			if(hour >= 6 && hour <= 16){
				farwell = farwell.concat("day!");
			}
			//otherwise print have a good night.
			else{
				farwell = farwell.concat("night!");
			}
			
			System.out.println();
			
			System.out.println(farwell);
		}
		//Catch any errors that may occur on other systems.
		catch(Exception e){
			System.out.println("Thank you for using Los Santos gps, have a good day!");
		}
	}

	/**
	 * Initialize the map of Los Santos with all the landmarks, streets, and keywords based on project specifications.
	 */
	public void initGps(){
		//Create los santos graph.
		ls = new Graph();
		
		//Add all the landmarks and their corresponding keywords.
		ls.addLandmark("Los Santos Public Library", new String[] {"Library", "Recreation", "Landmark"});
		ls.addLandmark("Los Santos Saints' Stadium", new String[] {"Recreation", "Landmark", "Sports"});
		ls.addLandmark("Vinewood Boulevard", new String[] {"Recreation", "Landmark"});
		ls.addLandmark("Los Santos Forum", new String[] {"Recreation", "Landmark", "Sports"});
		ls.addLandmark("Los Santos City Hall", new String[] {"Landmark"});
		ls.addLandmark("Centennial Theater", new String[] {"Recreation", "Arts", "Landmark"});
		ls.addLandmark("All Saints General Hospital", new String[] {"Hospital", "Health&Care"});
		ls.addLandmark("Richman Country Club", new String[] {"Recreation"});
		ls.addLandmark("BurgerShot", new String[] {"Dining", "Fast-food", "Restaurant"});
		ls.addLandmark("Los Santos Gym", new String[] {"Fitness", "Health&Care"});
		ls.addLandmark("Cluckin'Bell", new String[] {"Dining", "Fast-food", "Restaurant"});
		ls.addLandmark("Pimiento's", new String[] {"Dining", "Restaurant"});
		
		//Add the Library adjacencies.
		ls.addEdge("Los Santos City Hall", "Los Santos Public Library", 5, "south", "1st st.");
		ls.addEdge("Los Santos Saints' Stadium", "Los Santos Public Library", 3, "east", "Main ave.");
		
		//Add the stadium adjacencies.
		ls.addEdge("Los Santos Public Library", "Los Santos Saints' Stadium", 3, "west", "Main ave.");
		ls.addEdge("Vinewood Boulevard", "Los Santos Saints' Stadium", 2, "east", "Main ave.");
		
		//Add the vinewood adjacencies.
		ls.addEdge("All Saints General Hospital", "Vinewood Boulevard", 6, "south", "3rd st.");
		ls.addEdge("Los Santos Saints' Stadium", "Vinewood Boulevard", 2, "west", "Main ave.");
		ls.addEdge("Los Santos Forum", "Vinewood Boulevard", 5, "east", "Main ave.");
		
		//Add the forum adjacencies.
		ls.addEdge("Vinewood Boulevard", "Los Santos Forum", 5, "west", "Main ave.");
		
		//Add the city hall adjacencies.
		ls.addEdge("Centennial Theater", "Los Santos City Hall", 4, "east", "Centennial ave.");

		//Add the theater adjacencies.
		ls.addEdge("Los Santos City Hall", "Centennial Theater", 4, "west", "Centennial ave.");
		ls.addEdge("All Saints General Hospital", "Centennial Theater", 7, "east", "Centennial ave.");
		ls.addEdge("Los Santos Saints' Stadium", "Centennial Theater", 1, "north", "2nd st.");
		
		//Add the hospital adjacencies.
		ls.addEdge("Centennial Theater", "All Saints General Hospital", 7, "west", "Centennial ave.");
		ls.addEdge("Los Santos Gym", "All Saints General Hospital", 1, "south", "3rd st.");
		ls.addEdge("Richman Country Club", "All Saints General Hospital", 3, "east", "Centennial ave.");
		
		//Add the country club adjacencies.
		ls.addEdge("Los Santos Forum", "Richman Country Club", 1, "noth", "1st st.");
		ls.addEdge("All Saints General Hospital", "Richman Country Club", 3, "west", "Centennial ave.");

		//Add the burgetshot adjacencies.
		ls.addEdge("Centennial Theater", "BurgerShot", 2, "north", "2nd st.");
		ls.addEdge("Los Santos Gym", "BurgerShot", 1, "east", "Gerald ave.");

		//Add the gym adjacencies.
		ls.addEdge("BurgerShot", "Los Santos Gym", 1, "west", "Gerald ave.");
		ls.addEdge("Cluckin'Bell", "Los Santos Gym", 1, "east", "Gerald ave.");
		ls.addEdge("Pimiento's", "Los Santos Gym", 2, "south east", "Food alley");
		
		//Add the cluckin bell adjacencies.
		ls.addEdge("Richman Country Club", "Cluckin'Bell", 3, "north", "4th st.");
		ls.addEdge("Los Santos Gym", "Cluckin'Bell", 1, "west", "Gerald ave.");
		
		//Add the pimiento's adjacencies.
		ls.addEdge("Cluckin'Bell", "Pimiento's", 3, "north", "4th st.");
		
		//Set initial location.
		initialLocation = ls.locations.get("Los Santos City Hall");
	}

	/**
	 * Print white space to allow for a clean reading.
	 */
	private void clearScreen() {
		for(int i = 0; i < 4; i++)
			System.out.println();		
	}
}
