import java.util.*;

/**
 * Library for graph analysis
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2016
 *
 */
public class GraphLibPS4<V, E> {

    private String center;

    public GraphLibPS4(String c) {
        center = c;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    /**
     * Takes a random walk from a vertex, up to a given number of steps
     * So a 0-step path only includes start, while a 1-step path includes start and one of its out-neighbors,
     * and a 2-step path includes start, an out-neighbor, and one of the out-neighbor's out-neighbors
     * Stops earlier if no step can be taken (i.e., reach a vertex with no out-edge)
     * @param g		graph to walk on
     * @param start	initial vertex (assumed to be in graph)
     * @param steps	max number of steps
     * @return		a list of vertices starting with start, each with an edge to the sequentially next in the list;
     * 			    null if start isn't in graph
     */
    public static <V,E> List<V> randomWalk(Graph<V,E> g, V start, int steps) {
        // TODO: your code here
        if(!g.hasVertex(start)) {
            return null;
        }
        List<V> path = new ArrayList<>();
        V current = start;
        for (int i = 0; i <= steps; i++) {
            path.add(current);
            if(g.outDegree(current) > 0) {

                int index = (int) ((Math.random() * g.outDegree(current)));
                ArrayList<V> vertices = new ArrayList<>();
                for (V elem : g.outNeighbors(current)) {
                    vertices.add(elem);
                }
                current = vertices.get(index);
            }
            else {
                break;
            }
        }
        return path;

    }

    /**
     * Orders vertices in decreasing order by their in-degree
     * @param g		graph
     * @return		list of vertices sorted by in-degree, decreasing (i.e., largest at index 0)
     */
    public static <V,E> List<V> verticesByInDegree(Graph<V,E> g) {
        // TODO: your code here
        List<V> list = new ArrayList<>();
        for (V elem : g.vertices()) {
            list.add(elem);
        }
        list.sort((V v1, V v2) -> g.inDegree(v2)-g.inDegree(v1));
        return list;
    }


    /**
     * BFS to find shortest path tree for a current center of the universe. Return a path tree as a Graph.
     * @param g graph
     * @param source the actor in the universe
     * @return a path tree as a Graph.
     * @param <V> the center actor of this universe
     * @param <E> the movie they were in
     */
    public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source) {
        Graph<V,E> path = new AdjacencyMapGraph<>(); //initialize backTrack
        path.insertVertex(source); //load start vertex with null parent
        Set<V> visited = new HashSet<V>(); //Set to track which vertices have already been visited
        Queue<V> queue = new LinkedList<V>(); //queue to implement BFS
        queue.add(source); //enqueue start vertex
        visited.add(source); //add start to visited Set
        while (!queue.isEmpty()) { //loop until no more vertices
            V u = queue.remove(); //dequeue
            for (V v : g.outNeighbors(u)) { //loop over out neighbors
                if (!visited.contains(v)) { //if neighbor not visited, then neighbor is discovered from this vertex
                    visited.add(v); //add neighbor to visited Set
                    queue.add(v); //enqueue neighbor
                    path.insertVertex(v);
                    path.insertVertex(u);
                    path.insertDirected(v, u, g.getLabel(u, v)); //save that this vertex was discovered from prior vertex
                }
            }
        }
        return path;
    }

    public static <V,E> List<V> getPath(Graph<V,E> tree, V v) {
        List<V> path = new ArrayList<>();
        if(!tree.hasVertex(v)) {
            return null;
        }
        V current = v;
        path.add(current);
        while(tree.outDegree(current) > 0) {
            for (V elem : tree.outNeighbors(current)) {
                current = elem;
                path.add(current);
            }
        }
        return path;
    }

    public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph) {
        Set<V> notInSubgraph = new HashSet<>();
        for (V elem : graph.vertices()) {
            if(!subgraph.hasVertex(elem)) {
                notInSubgraph.add(elem);
            }
        }
        return notInSubgraph;
    }

    public static <V,E> double averageSeparation(Graph<V,E> tree, V root) {
        double sum = averageSeparationHelper(tree, root, 0);
        return sum/tree.numEdges();
    }

    public static <V,E> double averageSeparationHelper(Graph<V,E> tree, V vertex, int level) {
        double current = level;
        for (V v : tree.inNeighbors(vertex)) {
            current += averageSeparationHelper(tree, v, level+1);
        }
        return current;
    }

    public static void main(String [] args) {
        Graph<String, String> actors = new AdjacencyMapGraph<String, String>();
        GraphLibPS4 test = new GraphLibPS4("Kevin Bacon");

        actors.insertVertex("Kevin Bacon");
        actors.insertVertex("Alice");
        actors.insertVertex("Bob");
        actors.insertVertex("Charlie");
        actors.insertVertex("Dartmouth");
        actors.insertVertex("Nobody");
        actors.insertVertex("Nobody's Friend");
        actors.insertUndirected("Alice", "Kevin Bacon", "A Movie, E movie");
        actors.insertUndirected("Bob", "Kevin Bacon", "A Movie");
        actors.insertUndirected("Charlie", "Bob", "C Movie");
        actors.insertUndirected("Charlie", "Alice", "D Movie");
        actors.insertUndirected("Dartmouth", "Charlie", "B Movie");
        actors.insertUndirected("Nobody's Friend", "Nobody", "F Movie");

        System.out.println("The graph:");
        System.out.println(actors);

        System.out.println();

        System.out.println(test.bfs(actors, "Kevin Bacon"));

        System.out.println();
        System.out.println("Testing getPath(): ");
        System.out.println(test.getPath(test.bfs(actors, "Kevin Bacon"), "Charlie"));
        System.out.println(test.getPath(test.bfs(actors, "Kevin Bacon"), "Alice"));

        System.out.println(test.getPath(test.bfs(actors, "Kevin Bacon"), "Dartmouth"));
        System.out.println();


        System.out.println("Testing missingVertices(): " + missingVertices(actors, test.bfs(actors, "Kevin Bacon")));

        System.out.println("Testing averageSeparation(): " + test.averageSeparation(test.bfs(actors, "Kevin Bacon"), "Kevin Bacon"));


    }

}

