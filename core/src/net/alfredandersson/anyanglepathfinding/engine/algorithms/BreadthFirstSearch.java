package net.alfredandersson.anyanglepathfinding.engine.algorithms;

import java.util.ArrayDeque;
import java.util.Deque;
import net.alfredandersson.anyanglepathfinding.engine.GridConnections;
import net.alfredandersson.anyanglepathfinding.engine.GridPathfinder;
import net.alfredandersson.anyanglepathfinding.engine.Map;

public final class BreadthFirstSearch extends GridPathfinder {
  
  private final Deque<Integer>
          openListX = new ArrayDeque<>(),
          openListY = new ArrayDeque<>();
  
  private final int[][]
          cameFromX, cameFromY,
          steps,
          lastModified;
  
  private final int[] buf;
  private final float[] costBuf;
  
  private int modIndex = 0;
  
  public BreadthFirstSearch(Map map, GridConnections con) {
    super(map, con);
    
    cameFromX = new int[map.getWidth() + 1][map.getHeight() + 1];
    cameFromY = new int[map.getWidth() + 1][map.getHeight() + 1];
    
    steps = new int[map.getWidth() + 1][map.getHeight() + 1];
    
    lastModified = new int[map.getWidth() + 1][map.getHeight() + 1];
    
    buf = new int[con.maxNeighbors() * 2];
    costBuf = new float[con.maxNeighbors()];
  }
  
  private void reset() {
    openListX.clear();
    openListY.clear();
  }
  
  @Override
  public int[] findPath(int startX, int startY, int endX, int endY) {
    reset();
    
    modIndex++;
    
    openListX.addLast(startX);
    openListY.addLast(startY);
    
    cameFromX[startX][startY] = startX;
    cameFromY[startX][startY] = startY;
    
    steps[startX][startY] = 0;
    
    while (!openListX.isEmpty()) {
      int currentX = openListX.removeFirst();
      int currentY = openListY.removeFirst();
      
      int currentSteps = steps[currentX][currentY];
      
      if (currentX == endX && currentY == endY) {
        // assemble the path
        int[] path = new int[(currentSteps + 1) * 2];
        
        for (int i = currentSteps; i >= 0; i--) {
          path[i * 2] = currentX;
          path[i * 2 + 1] = currentY;
          
          int newX = cameFromX[currentX][currentY];
          int newY = cameFromY[currentX][currentY];
          
          currentX = newX;
          currentY = newY;
        }
        
        return path;
      }
      
      int numNeighbors = con.getNeighbors(map, currentX, currentY, buf, costBuf);
      
      for (int i = 0; i < numNeighbors; i++) {
        int neighborX = buf[i * 2];
        int neighborY = buf[i * 2 + 1];
        
        if (lastModified[neighborX][neighborY] != modIndex) {
          lastModified[neighborX][neighborY] = modIndex;
          
          openListX.add(neighborX);
          openListY.add(neighborY);
          
          cameFromX[neighborX][neighborY] = currentX;
          cameFromY[neighborX][neighborY] = currentY;
          
          steps[neighborX][neighborY] = currentSteps + 1;
        }
      }
    }
    
    return null;
  }
}
