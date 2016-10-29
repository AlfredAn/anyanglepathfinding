package net.alfredandersson.anyanglepathfinding.engine.algorithms;

import net.alfredandersson.anyanglepathfinding.engine.GridConnections;
import net.alfredandersson.anyanglepathfinding.engine.GridPathfinder;
import net.alfredandersson.anyanglepathfinding.engine.Map;

public abstract class GenericSearch extends GridPathfinder {
  
  protected final int[][]
          cameFromX, cameFromY,
          steps,
          lastModified;
  
  protected final int[] buf;
  protected final float[] costBuf;
  
  protected int modIndex = 0;
  
  protected int startX, startY, endX, endY;
  protected int currentX, currentY, currentSteps;
  protected int neighborX, neighborY;
  
  public GenericSearch(Map map, GridConnections con) {
    super(map, con);
    
    cameFromX = new int[map.getWidth() + 1][map.getHeight() + 1];
    cameFromY = new int[map.getWidth() + 1][map.getHeight() + 1];
    
    steps = new int[map.getWidth() + 1][map.getHeight() + 1];
    
    lastModified = new int[map.getWidth() + 1][map.getHeight() + 1];
    
    buf = new int[con.maxNeighbors() * 2];
    costBuf = new float[con.maxNeighbors()];
  }
  
  protected void reset() {}
  
  @Override
  public int[] findPath(int startX, int startY, int endX, int endY) {
    reset();
    
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
    
    incrementModIndex();
    
    initStartNode();
    addStartNodeToOpenSet(startX, startY);
    
    while (!isOpenSetEmpty()) {
      popFromOpenSet();
      
      if (!shouldExpandNode()) {
        continue;
      }
      
      currentSteps = steps[currentX][currentY];
      
      if (currentX == endX && currentY == endY) {
        return assemblePath();
      }
      
      int numNeighbors = getNeighborsOfCurrentCell();
      
      for (int i = 0; i < numNeighbors; i++) {
        initNeighbor(buf[i * 2], buf[i * 2 + 1], costBuf[i]);
        
        if (shouldAddNeighbor(neighborX, neighborY)) {
          updateNeighbor();
          addToOpenSet(neighborX, neighborY);
        }
      }
    }
    
    return null;
  }
  
  protected void incrementModIndex() {
    modIndex++;
  }
  
  protected abstract void addStartNodeToOpenSet(int startX, int startY);
  protected abstract void addToOpenSet(int x, int y);
  
  protected void initStartNode() {
    cameFromX[startX][startY] = startX;
    cameFromY[startX][startY] = startY;
    
    steps[startX][startY] = 0;
  }
  
  protected void updateNeighbor() {
    lastModified[neighborX][neighborY] = modIndex;
    
    cameFromX[neighborX][neighborY] = currentX;
    cameFromY[neighborX][neighborY] = currentY;
    
    steps[neighborX][neighborY] = currentSteps + 1;
  }
  
  protected abstract boolean isOpenSetEmpty();
  
  protected abstract void popFromOpenSet();
  
  protected int[] assemblePath() {
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
  
  protected boolean shouldExpandNode() {
    return true;
  }
  
  protected void initNeighbor(int neighborX, int neighborY, float neighborCost) {
    this.neighborX = neighborX;
    this.neighborY = neighborY;
  }
  
  protected boolean shouldAddNeighbor(int neighborX, int neighborY) {
    return lastModified[neighborX][neighborY] != modIndex;
  }
  
  protected int getNeighborsOfCurrentCell() {
    return con.getNeighbors(map, currentX, currentY, buf, costBuf);
  }
}
