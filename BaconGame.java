import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BaconGame {

    private HashMap<String, String> actorIDtoName;
    private HashMap<String, String> movieIDtoName;
    private HashMap<String, List<String>> actorsToMovies;

    private AdjacencyMapGraph<String, Set<String>> actors;
    private Graph<String, Set<String>> tree;


    GraphLibPS4 lib;

    public BaconGame(String actorIDfile, String movieIDfile, String movieActorsFile) throws IOException {
        actorIDtoName = new HashMap<>();
        movieIDtoName = new HashMap<>();
        actorsToMovies = new HashMap<>();
        actors = new AdjacencyMapGraph<>();
        lib = new GraphLibPS4("Kevin Bacon");

        BufferedReader actorIDs = new BufferedReader(new FileReader(actorIDfile));
        String line = actorIDs.readLine();
        while (line != null ){
            actorIDtoName.put(line.split("\\|")[0], line.split("\\|")[1]);
            actors.insertVertex(line.split("\\|")[1]);
            line = actorIDs.readLine();
        }

        BufferedReader movieIDs = new BufferedReader(new FileReader(movieIDfile));
        String line1 = movieIDs.readLine();
        while (line1 != null ){
            movieIDtoName.put(line1.split("\\|")[0], line1.split("\\|")[1]);
            line1 = movieIDs.readLine();
        }

        BufferedReader moviesToActors = new BufferedReader(new FileReader(movieActorsFile));
        String line2 = moviesToActors.readLine();
        while (line2 != null){
            String movie = movieIDtoName.get(line2.split("\\|")[0]);
            String actor = actorIDtoName.get(line2.split("\\|")[1]);
            List<String> listOfActors;
            if (!actorsToMovies.containsKey(movie)) {
                listOfActors = new ArrayList<>();
            }
            else {
                listOfActors = actorsToMovies.get(movie);
            }
            listOfActors.add(actor);
            actorsToMovies.put(movie, listOfActors);
            line2 = moviesToActors.readLine();
        }
        for (String movie: actorsToMovies.keySet()) {
            List <String> stars = actorsToMovies.get(movie);
            for (int i = 0; i < stars.size()-1; i++) {
                for (int j = 0; j < stars.size(); j++) {
                    if (!actors.hasEdge(stars.get(i), stars.get(j))) {
                        Set<String> label = new HashSet<>();
                        label.add(movie);
                        actors.insertUndirected(stars.get(i), stars.get(j), label);
                    }
                    else {
                        Set<String> label = actors.getLabel(stars.get(i), stars.get(j));
                        label.add(movie);
                        actors.insertUndirected(stars.get(i), stars.get(j), label);
                    }
                }
            }
        }

        tree = lib.bfs(actors, lib.getCenter());



    }


    public void path(Scanner s) {
        System.out.println("Please type the name of a valid actor and press enter: ");
        String actor = s.next();
        List<String> path = lib.getPath(tree, actor);
        System.out.println(path.get(0) + "'s number is " + (path.size()-1));
        for (int i = 0; i < path.size()-1; i++) {
            System.out.println(path.get(i) + " appeared in " + tree.getLabel(path.get(i), path.get(i+1)) + " with " + path.get(i+1));
        }
    }

    public void sortDegree() {
        List<String> list = new ArrayList<String>();
        for (String actor : actors.vertices()) {
            list.add(actor);
        }
        list.sort((s1,s2) -> actors.outDegree(s1)-actors.outDegree(s2));
        System.out.println("The following actors are sorted from lowest degrees from the center to highest degree of separation:");
        System.out.println("----------------------------------------------------------------------------------------------");
        for (String actor : list) {

            System.out.println(actor + " has " + actors.outDegree(actor) + " in degree(s).");
        }
    }

    public List<String> findBestBacons(Scanner s) {
        ArrayList<String> bacons = new ArrayList<>();
        Graph<String, Set<String>> centerTree = lib.bfs(actors,lib.getCenter());

        for (String actor : actors.vertices()) {
            if (centerTree.hasVertex(actor)) {
                Graph<String, Set<String>> tree1 = lib.bfs(actors, actor);
                bacons.add(actor);
                bacons.sort(Comparator.comparingInt(s2 -> (int) lib.averageSeparation(tree1, s2) * 1000000));
            }
        }
        return bacons;

    }

    public void unreachable(Scanner s) {
        Set<String> unreached = lib.missingVertices(actors, tree);
        for (String actor : unreached) {
            System.out.println(actor + " is unreachable from " + lib.getCenter());
        }
    }

    public void play() {
        System.out.println("Welcome to the Bacon game! The current center of the universe is " + lib.getCenter() + ".");
        System.out.println();
        System.out.println("Menu of commands:");
        System.out.println("Type and enter \"f\" to find other suitable Bacons in the graph.");
        System.out.println("Type and enter \"sd\" to sort the actors by in degree.");
        System.out.println("Type and enter \"u\" to find the actors unreachable to the current center of the graph.");
        System.out.println("Type and enter \"p\" to find the path from an actor to the current center of the graph.");
        System.out.println("Type and enter \"as\" to find the average separation between the center and other actors in the graph.");
        System.out.println("Type and enter \"cc\" to change the center of the graph.");
        System.out.println("----------------------------------------------------------------------------------------------");
        System.out.println();

        Scanner s = new Scanner(System.in);
        while(s.hasNextLine()) {
            String line = s.next();
            if (line.equals("f")) {
                System.out.println("Finding other Bacons...");
                System.out.println("In order from shortest average separation to longest, here are the other actors. Maybe a different actor would be a better center?");
                System.out.println(findBestBacons(s));
            }

            if(line.equals("sd")) {
                System.out.println("Sorting by degree...");
                sortDegree();
            }

            if (line.equals("u")) { // find which actors are unreachable from the center
                System.out.println("Finding the unreachable actors...");
                unreachable(s);
            }

            if(line.equals("p")) { // finds path from given actor to current center of the universe
                System.out.println("Finding a path...");
                path(s);
            }

            if(line.equals("cc")) { // change center of the universe
                System.out.println("Changing center of the universe...");
                System.out.println("Please type the name of another actor in the universe to become the new center: ");
                String name = s.next();
                name += s.nextLine();
                lib.setCenter(name);
                tree = lib.bfs(actors, lib.getCenter());
                System.out.println(lib.getCenter() + " is the new center of the universe.");

            }

            if(line.equals("as")) { //find average separation
                System.out.println("Finding average separation...");
                System.out.println("Average separation for the center " + lib.getCenter() + " is " + lib.averageSeparation(tree, lib.getCenter()) + ".");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BaconGame game = new BaconGame("ps4/bacon/actorsTest.txt", "ps4/bacon/moviesTest.txt", "ps4/bacon/movie-actorsTest.txt");
        game.play();

    }

}
