package net.alfredandersson.anyanglepathfinding.engine;

public final class FourConnectedGrid implements GridConnections {
  
  @Override
  public int maxNeighbors() {
    return 4;
  }
  
  @Override
  public int getNeighbors(Map map, int x, int y, int[] dest) {
    int i = 0;
    
    // moving east
    if (!map.isBlocked(x, y - 1) || !map.isBlocked(x, y)) {
      dest[i++] = x + 1;
      dest[i++] = y;
    }
    
    // moving west
    if (!map.isBlocked(x - 1, y - 1) || !map.isBlocked(x - 1, y)) {
      dest[i++] = x - 1;
      dest[i++] = y;
    }
    
    // moving south
    if (!map.isBlocked(x - 1, y) || !map.isBlocked(x, y)) {
      dest[i++] = x;
      dest[i++] = y + 1;
    }
    
    // moving north
    if (!map.isBlocked(x - 1, y - 1) || !map.isBlocked(x, y - 1)) {
      dest[i++] = x;
      dest[i++] = y - 1;
    }
    
    return i / 2;
  }
  
  @Override
  public boolean isNeighbor(Map map, int fromX, int fromY, int toX, int toY) {
    if (Math.abs(toX - fromX) + Math.abs(toY - fromY) != 1) {
      return false;
    }
    
    switch (toX - fromX + 2 * (toY - fromY)) {
      case 1: // moving east
        return !map.isBlocked(fromX, fromY - 1) || !map.isBlocked(fromX, fromY);
      case -1: // moving west
        return !map.isBlocked(fromX - 1, fromY - 1) || !map.isBlocked(fromX - 1, fromY);
      case 2: // moving south
        return !map.isBlocked(fromX - 1, fromY) || !map.isBlocked(fromX, fromY);
      case -2: // moving north
        return !map.isBlocked(fromX - 1, fromY - 1) || !map.isBlocked(fromX, fromY - 1);
      default:
        throw new AssertionError();
    }
  }
  
  @Override
  public float cost(Map map, int fromX, int fromY, int toX, int toY) {
    if (isNeighbor(map, fromX, fromY, toX, toY)) {
      return 1;
    } else {
      return Float.POSITIVE_INFINITY;
    }
  }
}
