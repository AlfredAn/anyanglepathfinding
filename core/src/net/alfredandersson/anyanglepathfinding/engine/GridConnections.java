package net.alfredandersson.anyanglepathfinding.engine;

public interface GridConnections {
  
  /**
   * Returns the maximal number of neighbors that a cell can have.
   */
  int maxNeighbors();
  
  /**
   * Retrieves the positions of all the neighbors of the specified cell.
   * The input array must be of at least size 2 * maxNeighbors().
   * @param coord An array to put the neighbors in, as interleaved x and y coordinates.
   * @param cost An array to put the neighbor costs in.
   * @return The number of neighbors added to the array.
   */
  int getNeighbors(Map map, int x, int y, int[] coord, float[] cost);
  
  /**
   * Returns whether the two given cells are neighbors.
   */
  boolean isNeighbor(Map map, int fromX, int fromY, int toX, int toY);
  
  /**
   * Returns the cost of moving between two squares, or {@code Float.POSITIVE_INFINITY} if there is no connection.
   */
  float cost(Map map, int fromX, int fromY, int toX, int toY);
  
  Heuristic defaultHeuristic();
}
