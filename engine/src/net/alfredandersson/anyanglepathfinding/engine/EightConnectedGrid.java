package net.alfredandersson.anyanglepathfinding.engine;

public final class EightConnectedGrid extends GridConnections {
  
  private static final float DIAG_COST = (float)Math.sqrt(2);
  
  public EightConnectedGrid(Map map) {
    super(map);
  }
  
  @Override
  public int maxNeighbors() {
    return 8;
  }
  
  @Override
  public int getNeighbors(int x, int y, int[] coord, float[] cost) {
    int i = 0;
    int j = 0;
    
    // moving east
    if (!map.isBlocked(x, y - 1) || !map.isBlocked(x, y)) {
      coord[i++] = x + 1;
      coord[i++] = y;
      cost[j++] = 1;
    }
    
    // moving west
    if (!map.isBlocked(x - 1, y - 1) || !map.isBlocked(x - 1, y)) {
      coord[i++] = x - 1;
      coord[i++] = y;
      cost[j++] = 1;
    }
    
    // moving south
    if (!map.isBlocked(x - 1, y) || !map.isBlocked(x, y)) {
      coord[i++] = x;
      coord[i++] = y + 1;
      cost[j++] = 1;
    }
    
    // moving north
    if (!map.isBlocked(x - 1, y - 1) || !map.isBlocked(x, y - 1)) {
      coord[i++] = x;
      coord[i++] = y - 1;
      cost[j++] = 1;
    }
    
    // north-east
    if (!map.isBlocked(x, y - 1)) {
      coord[i++] = x + 1;
      coord[i++] = y - 1;
      cost[j++] = DIAG_COST;
    }
    
    // north-west
    if (!map.isBlocked(x - 1, y - 1)) {
      coord[i++] = x - 1;
      coord[i++] = y - 1;
      cost[j++] = DIAG_COST;
    }
    
    // south-west
    if (!map.isBlocked(x - 1, y)) {
      coord[i++] = x - 1;
      coord[i++] = y + 1;
      cost[j++] = DIAG_COST;
    }
    
    // south-east
    if (!map.isBlocked(x, y)) {
      coord[i++] = x + 1;
      coord[i++] = y + 1;
      cost[j++] = DIAG_COST;
    }
    
    return i / 2;
  }
  
  @Override
  public Heuristic defaultHeuristic() {
    return DiagonalDistance.INSTANCE;
  }
}
