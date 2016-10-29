package net.alfredandersson.anyanglepathfinding.engine;

public abstract class GridConnections {
  
  public final Map map;
  
  public GridConnections(Map map) {
    this.map = map;
  }
  
  /**
   * Returns the maximal number of neighbors that a cell can have.
   */
  public abstract int maxNeighbors();
  
  /**
   * Retrieves the positions of all the neighbors of the specified cell.
   * The input array must be of at least size 2 * maxNeighbors().
   * @param coord An array to put the neighbors in, as interleaved x and y coordinates.
   * @param cost An array to put the neighbor costs in.
   * @return The number of neighbors added to the array.
   */
  public abstract int getNeighbors(int x, int y, int[] coord, float[] cost);
  
  public abstract Heuristic defaultHeuristic();
}
