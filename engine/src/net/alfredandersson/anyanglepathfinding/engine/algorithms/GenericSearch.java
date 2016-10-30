package net.alfredandersson.anyanglepathfinding.engine.algorithms;

import gnu.trove.list.array.TIntArrayList;
import net.alfredandersson.anyanglepathfinding.engine.GridConnections;
import net.alfredandersson.anyanglepathfinding.engine.GridPathfinder;
import net.alfredandersson.anyanglepathfinding.engine.Map;

public abstract class GenericSearch extends GridPathfinder {
  
  protected final int[][]
          cameFromX, cameFromY,
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
    
    lastModified = new int[map.getWidth() + 1][map.getHeight() + 1];
    
    buf = new int[neighborBufferSize() * 2];
    costBuf = new float[neighborBufferSize()];
  }
  
  protected void reset() {}
  
  @Override
  public int[] findPath(int startX, int startY, int endX, int endY) {
    reset();
    
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
    
    int[] earlyExit = earlyExit();
    if (earlyExit != null) {
      return earlyExit;
    }
    
    incrementModIndex();
    
    initStartNode();
    addStartNodeToOpenSet(startX, startY);
    
    while (!isOpenSetEmpty()) {
      popFromOpenSet();
      
      if (!shouldExpandNode()) {
        continue;
      }
      
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
    lastModified[startX][startY] = modIndex;
    
    cameFromX[startX][startY] = startX;
    cameFromY[startX][startY] = startY;
  }
  
  protected void updateNeighbor() {
    lastModified[neighborX][neighborY] = modIndex;
    
    cameFromX[neighborX][neighborY] = currentX;
    cameFromY[neighborX][neighborY] = currentY;
  }
  
  protected abstract boolean isOpenSetEmpty();
  
  protected abstract void popFromOpenSet();
  
  protected int[] assemblePath() {
    TIntArrayList path = new TIntArrayList((currentSteps + 1) * 2);
    
    while (true) {
      path.add(currentX);
      path.add(currentY);
      
      if (currentX == startX && currentY == startY) {
        int[] result = new int[path.size()];
        for (int i = 0; i < result.length / 2; i++) {
          result[i * 2] = path.getQuick(result.length - 2 * i - 2);
          result[i * 2 + 1] = path.getQuick(result.length - 2 * i - 1);
        }
        return result;
      }
      
      int newX = cameFromX[currentX][currentY];
      int newY = cameFromY[currentX][currentY];
      
      currentX = newX;
      currentY = newY;
    }
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
    return con.getNeighbors(currentX, currentY, buf, costBuf);
  }
  
  protected int neighborBufferSize() {
    return con.maxNeighbors();
  }
  
  protected int[] earlyExit() {
    if (startX == endX && startY == endY) {
      return new int[] {endX, endY};
    } else {
      return null;
    }
  }
}
