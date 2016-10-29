package net.alfredandersson.anyanglepathfinding.engine;

public abstract class Pathfinder {
  
  public final Map map;
  
  public Pathfinder(Map map) {
    this.map = map;
  }
  
  /**
   * <p>Returns a path between (startX, startY) and (endX, endY), as an array
   * with interleaved x and y coordinates, or null if no path could be found.
   * <p>All points, including the start and end points, are included in the array.
   * <p>All coordinates are assumed to be on the corners of cells.
   */
  public abstract int[] findPath(int startX, int startY, int endX, int endY);
}
