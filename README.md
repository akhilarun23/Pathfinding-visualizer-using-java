## Pathfinding Visualizer
https://github.com/abelgeostan/Pathfinding-Visualizer/assets/170155087/23da99a8-cc9e-4a5e-bd98-dd74f0f7a0b9

## Description
The Pathfinding Visualizer is a Java-based application that allows users to visualize pathfinding algorithms on a grid. This application currently supports the Dijkstra algorithm for finding the shortest path between two points. Users can interactively set start and end points, draw walls, and watch the algorithm find the path in real-time.

## Features
- **Interactive Grid**: Users can set start and end points, draw walls, and clear the grid.
- **Dijkstra Algorithm**: Visualizes the Dijkstra algorithm for pathfinding.
- **Real-Time Visualization**: The pathfinding process is shown step-by-step, allowing users to see how the algorithm explores the grid.
- **Responsive UI**: Adjust grid size and speed for different visualization preferences.

## Components
- **JFrame**: Main application window.
- **JPanel**: Control panel and canvas for drawing the grid.
- **JButtons**: Start Search, Reset, and Clear Map buttons for controlling the visualization.
- **JComboBox**: Selection for choosing algorithms (currently only Dijkstra is available).
- **Checkboxes**: Tools for setting start, end, walls, and erasing.

## How to Run
1. Ensure you have Java installed on your machine.
2. Clone the repository and navigate to the project directory.
3. Compile the Java files:
    ```sh
    javac PathfindingVisualizer.java
    ```
4. Run the application:
    ```sh
    java PathfindingVisualizer
    ```

## Future Enhancements
- Add more pathfinding algorithms (A*, Breadth-First Search, etc.).
- Implement adjustable grid size and speed sliders.
- Improve the UI with additional features and styling.
