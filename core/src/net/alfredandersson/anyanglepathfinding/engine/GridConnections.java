package net.alfredandersson.anyanglepathfinding.engine;

public interface GridConnections {
  
  /**
   * Returns the maximal number of neighbors that a cell can have.
   */
  int maxNeighbors();
  
  /**
   * Puts the positions of all the neighbors of the specified cell into the dest array,
   * as interleaved x and y coordinates.
   * The input array must be of at least size 2 * maxNeighbors().
   * @return The number of neighbors added to the array.
   */
  int getNeighbors(Map map, int x, int y, int[] dest);
  
  /**
   * Returns whether the two given cells are neighbors.
   */
  boolean isNeighbor(Map map, int fromX, int fromY, int toX, int toY);
  
  /**
   * Returns the cost of moving between two squares, or {@code Float.POSITIVE_INFINITY} if there is no connection.
   */
  float cost(Map map, int fromX, int fromY, int toX, int toY);
}
