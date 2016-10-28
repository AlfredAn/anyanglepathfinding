package net.alfredandersson.anyanglepathfinding.engine.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import net.alfredandersson.anyanglepathfinding.engine.GridConnections;
import net.alfredandersson.anyanglepathfinding.engine.GridPathfinder;
import net.alfredandersson.anyanglepathfinding.engine.Map;
import net.alfredandersson.anyanglepathfinding.engine.ComparablePoint;
import net.alfredandersson.anyanglepathfinding.engine.Heuristic;

public final class BestFirstSearch extends GridPathfinder {
  
  private final PriorityQueue<ComparablePoint> open = new PriorityQueue<>();
  private final List<ComparablePoint> pool = new ArrayList<>();
  
  private final Heuristic h;
  
  private final int[][]
          cameFromX, cameFromY,
          steps,
          lastModified;
  
  private final int[] buf;
  private final float[] costBuf;
  
  private int modIndex = 0;
  
  public BestFirstSearch(Map map, GridConnections con) {
    this(map, con, con.defaultHeuristic());
  }
  
  public BestFirstSearch(Map map, GridConnections con, Heuristic h) {
    super(map, con);
    
    cameFromX = new int[map.getWidth() + 1][map.getHeight() + 1];
    cameFromY = new int[map.getWidth() + 1][map.getHeight() + 1];
    
    steps = new int[map.getWidth() + 1][map.getHeight() + 1];
    
    lastModified = new int[map.getWidth() + 1][map.getHeight() + 1];
    
    buf = new int[con.maxNeighbors() * 2];
    costBuf = new float[con.maxNeighbors()];
    
    this.h = h;
  }
  
  private void reset() {
    open.clear();
  }
  
  @Override
  public int[] findPath(int startX, int startY, int endX, int endY) {
    reset();
    
    modIndex++;
    
    addToOpen(startX, startY, endX, endY);
    
    cameFromX[startX][startY] = startX;
    cameFromY[startX][startY] = startY;
    
    steps[startX][startY] = 0;
    
    while (!open.isEmpty()) {
      ComparablePoint current = open.poll();
      
      int currentSteps = steps[current.x][current.y];
      
      if (current.x == endX && current.y == endY) {
        // assemble the path
        int[] path = new int[(currentSteps + 1) * 2];
        
        for (int i = currentSteps; i >= 0; i--) {
          path[i * 2] = current.x;
          path[i * 2 + 1] = current.y;
          
          int newX = cameFromX[current.x][current.y];
          int newY = cameFromY[current.x][current.y];
          
          current.x = newX;
          current.y = newY;
        }
        
        return path;
      }
      
      int numNeighbors = con.getNeighbors(map, current.x, current.y, buf, costBuf);
      
      for (int i = 0; i < numNeighbors; i++) {
        int neighborX = buf[i * 2];
        int neighborY = buf[i * 2 + 1];
        
        if (lastModified[neighborX][neighborY] != modIndex) {
          lastModified[neighborX][neighborY] = modIndex;
          
          addToOpen(neighborX, neighborY, endX, endY);
          
          cameFromX[neighborX][neighborY] = current.x;
          cameFromY[neighborX][neighborY] = current.y;
          
          steps[neighborX][neighborY] = currentSteps + 1;
        }
      }
    }
    
    return null;
  }
  
  private void addToOpen(int x, int y, int endX, int endY) {
    open.add(newPoint(x, y, h.get(x, y, endX, endY)));
  }
  
  private ComparablePoint newPoint(int x, int y, float priority) {
    if (!pool.isEmpty()) {
      return pool.remove(pool.size() - 1).set(x, y, priority);
    } else {
      return new ComparablePoint(x, y, priority);
    }
  }
  
  private void freePoint(ComparablePoint point) {
    pool.add(point);
  }
}
