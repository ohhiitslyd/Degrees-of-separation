# README: Kevin Bacon Game and Social Network Analysis

## Overview

This project implements a social network analysis tool and a variation of the Kevin Bacon game. The goal is to find the shortest path between two actors in a network where the vertices represent actors and the edges represent co-appearance in movies. The tool also evaluates the suitability of actors as centers of the acting universe by analyzing their degrees (number of costars) and average separations.

The program supports interactive gameplay and provides analyses based on real-world data sourced from the provided datasets.

---

## Features

### Core Gameplay
1. **Change the Center of the Universe**  
   Allows users to select any actor as the "center of the universe," recalculating the shortest path tree for all connected actors.
   
2. **Find the Shortest Path to an Actor**  
   Computes the shortest path from the current center to a specified actor using a breadth-first search (BFS). Outputs the path, including the actors and movies connecting them.

3. **Analyze Connectivity**  
   - Lists actors who have a path to the current center of the universe.
   - Computes the number of actors connected to the center and their average separation.

### Additional Analyses
1. **Top and Bottom "Centers of the Universe"**  
   Ranks actors by:
   - Degree: Number of costars.
   - Average Separation: The average path length when the actor is the center of the universe.

2. **Disconnected Actors**  
   Identifies actors who are not connected to the current center (i.e., infinite separation).

### Interactive Commands
- `c <#>`: Lists the top (positive) or bottom (negative) centers based on average separation.
- `d <low> <high>`: Lists actors sorted by degree, with values between the specified range.
- `i`: Displays actors with infinite separation from the current center.
- `p <name>`: Finds the shortest path from the specified actor to the current center.
- `s <low> <high>`: Lists actors sorted by separation, within the specified range.
- `u <name>`: Changes the center of the universe to the specified actor.
- `q`: Quits the game.

---

## Input Data

The program uses three input files:
1. **`actors.txt`**  
   Contains actor IDs and names, separated by a pipe (`|`).
   - Format: `<actorID>|<actorName>`

2. **`movies.txt`**  
   Contains movie IDs and titles, separated by a pipe (`|`).
   - Format: `<movieID>|<movieTitle>`

3. **`movie-actors.txt`**  
   Lists movie-actor relationships, with movie IDs and actor IDs separated by a pipe (`|`).
   - Format: `<movieID>|<actorID>`

Sample test files (`actorsTest.txt`, `moviesTest.txt`, `movie-actorsTest.txt`) are provided for development and testing. Full-scale datasets (`actors.txt`, `movies.txt`, `movie-actors.txt`) contain data for 9,235 actors, 7,067 movies, and 21,370 movie-actor pairs.

---

## Implementation Details

### Graph Construction
- **Vertices:** Actors are represented as graph vertices.
- **Edges:** Edges connect actors who appeared together in movies. Each edge is labeled with the set of movies they co-starred in.

### BFS for Shortest Path Tree
A breadth-first search (BFS) algorithm constructs the shortest-path tree:
- Starting from the root (current center), BFS calculates the shortest path to each reachable actor.
- Paths are stored in a directed graph where each vertex points to its parent in the shortest path.

### Analysis Algorithms
- **Degree Calculation:** Counts the number of edges (costars) for each actor.
- **Average Separation:** Computes the mean path length for all actors connected to the center.
- **Disconnected Actors:** Identifies actors without paths to the center.

### Performance
The program handles large datasets efficiently, processing 32,337 edges in under a minute for most operations.

---

## Usage Instructions

1. **Setup**  
   Ensure Java is installed on your system. Clone the project repository and unzip the datasets (`bacon.zip`).

2. **Compile and Run**  
   ```
   javac KevinBaconGame.java
   java KevinBaconGame
   ```

3. **Input Datasets**  
   Place the datasets in the same directory as the program or specify their paths during runtime.

4. **Interactive Commands**  
   Follow the prompts to interact with the game using the commands described above.

---

## Example Output

### Changing the Center of the Universe
```
Kevin Bacon is now the center of the acting universe, connected to 8800/9235 actors with average separation 3.67.
```

### Finding a Path
```
p Diane Keaton
Diane Keaton's number is 2
Diane Keaton appeared in [Something's Gotta Give (2003)] with Jack Nicholson
Jack Nicholson appeared in [Few Good Men, A (1992)] with Kevin Bacon
```

### Degree Analysis
```
d 10 20
Actors with degree between 10 and 20:
1. Meryl Streep (19)
2. Tom Hanks (18)
...
```

---

## Development Notes

### Dependencies
The program relies only on the Java Standard Library. 

### Testing
Use the test datasets (`actorsTest.txt`, `moviesTest.txt`, `movie-actorsTest.txt`) during development to verify correctness.

### Known Limitations
- The BFS tree computation may become memory-intensive with larger datasets.
- Long paths or highly connected nodes can slightly delay results.

---

## Future Improvements
1. **Parallel Processing:** Enhance BFS and analysis performance using multithreading.
2. **Graph Visualization:** Integrate tools to visualize the shortest-path tree.
3. **Web Interface:** Provide a user-friendly interface for non-technical users.

---

## Authors
- Developed by Lydia Jin as part of CS 10: Problem Solving via Object Oriented Programming with Tim Pierson
- GraphLibPS4 skeleton provided by the course
