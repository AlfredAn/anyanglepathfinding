package net.alfredandersson.anyanglepathfinding.engine;

public class EightConnectedGrid implements GridConnections {
  
  public static final EightConnectedGrid INSTANCE = new EightConnectedGrid();
  
  private static final float DIAG_COST = (float)Math.sqrt(2);
  
  @Override
  public int maxNeighbors() {
    return 8;
  }
  
  @Override
  public int getNeighbors(Map map, int x, int y, int[] coord, float[] cost) {
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
  public boolean isNeighbor(Map map, int fromX, int fromY, int toX, int toY) {
    if (Math.max(Math.abs(toX - fromX), Math.abs(toY - fromY)) != 1) {
      return false;
    }
    
    switch (toX - fromX + 4 * (toY - fromY)) {
      case 1: // moving east
        return !map.isBlocked(fromX, fromY - 1) || !map.isBlocked(fromX, fromY);
      case -1: // moving west
        return !map.isBlocked(fromX - 1, fromY - 1) || !map.isBlocked(fromX - 1, fromY);
      case 4: // moving south
        return !map.isBlocked(fromX - 1, fromY) || !map.isBlocked(fromX, fromY);
      case -4: // moving north
        return !map.isBlocked(fromX - 1, fromY - 1) || !map.isBlocked(fromX, fromY - 1);
      case -4 + 1: // north-east
        return !map.isBlocked(fromX, fromY - 1);
      case -4 - 1: // north-west
        return !map.isBlocked(fromX - 1, fromY - 1);
      case 4 + 1: // south-west
        return !map.isBlocked(fromX - 1, fromY);
      case 4 - 1: // south-east
        return !map.isBlocked(fromX, fromY);
      default:
        throw new AssertionError();
    }
  }
  
  @Override
  public float cost(Map map, int fromX, int fromY, int toX, int toY) {
    if (!isNeighbor(map, fromX, fromY, toX, toY)) {
      return Float.POSITIVE_INFINITY;
    }
    
    if (Math.abs(toX - fromX) + Math.abs(toY - fromY) == 1) {
      return 1;
    } else {
      return DIAG_COST;
    }
  }
  
  @Override
  public Heuristic defaultHeuristic() {
    return DiagonalDistance.INSTANCE;
  }
}
