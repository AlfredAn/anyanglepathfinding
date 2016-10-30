package net.alfredandersson.anyanglepathfinding.engine;

import gnu.trove.list.array.TFloatArrayList;
import gnu.trove.list.array.TIntArrayList;

public final class VGraphConnections extends GridConnections {
  
  private static final Node EMPTY_NODE = new Node(new int[0], new float[0]);
  
  private final Node[][] nodes;
  
  private final int maxNeighbors;
  
  public VGraphConnections(Map map) {
    this(map, false);
  }
  
  /**
   * 
   * @param map The map to search.
   * @param precomputeAll Whether to precompute visibility between all relevant points.
   * Takes a long time and uses a lot of memory, so can only be used for small maps. Not recommended.
   * If this is false, VGraphSearch must be used. Otherwise, any search algorithm can be used.
   */
  public VGraphConnections(Map map, boolean precomputeAll) {
    super(map);
    
    nodes = new Node[map.getWidth() + 1][map.getHeight() + 1];
    
    TIntArrayList coord = new TIntArrayList();
    TFloatArrayList cost = new TFloatArrayList();
    boolean[][] isValid = new boolean[map.getWidth() + 1][map.getHeight() + 1];
    
    for (int x = 0; x < map.getWidth() + 1; x++) {
      for (int y = 0; y < map.getHeight() + 1; y++) {
        isValid[x][y] = isValidNode(x, y);
      }
    }
    
    int maxNeighbors0 = 0;
    for (int x1 = 0; x1 < map.getWidth() + 1; x1++) {
      for (int y1 = 0; y1 < map.getHeight() + 1; y1++) {
        if (!precomputeAll && !isValid[x1][y1]) {
          nodes[x1][y1] = EMPTY_NODE;
          continue;
        }
        for (int x2 = 0; x2 < map.getWidth() + 1; x2++) {
          for (int y2 = 0; y2 < map.getHeight() + 1; y2++) {
            if (isValid[x2][y2] && !CellTraversal.collisionLine(map, x1, y1, x2, y2)) {
              coord.add(x2);
              coord.add(y2);
              
              int dx = x2 - x1;
              int dy = y2 - y1;
              cost.add((float)Math.sqrt(dx * dx + dy * dy));
            }
          }
        }
        if (coord.size() == 0) {
          nodes[x1][y1] = EMPTY_NODE;
        } else {
          nodes[x1][y1] = new Node(coord.toArray(), cost.toArray());
          maxNeighbors0 = Math.max(maxNeighbors0, cost.size());
        }
        coord.resetQuick();
        cost.resetQuick();
      }
    }
    
    maxNeighbors = maxNeighbors0;
  }
  
  @Override
  public int maxNeighbors() {
    return maxNeighbors;
  }
  
  @Override
  public int getNeighbors(int x, int y, int[] coord, float[] cost) {
    Node n = nodes[x][y];
    
    System.arraycopy(n.coord, 0, coord, 0, n.coord.length);
    System.arraycopy(n.cost, 0, cost, 0, n.cost.length);
    
    return n.cost.length;
  }
  
  @Override
  public Heuristic defaultHeuristic() {
    return EuclideanHeuristic.INSTANCE;
  }
  
  private static class Node {
    
    private final int[] coord;
    private final float[] cost;
    
    private Node(int[] coord, float[] cost) {
      this.coord = coord;
      this.cost = cost;
    }
  }
  
  public boolean isValidNode(int x, int y) {
    boolean b1 = map.isBlocked(x - 1, y - 1);
    boolean b2 = map.isBlocked(x, y - 1);
    boolean b3 = map.isBlocked(x, y);
    boolean b4 = map.isBlocked(x - 1, y);
    return (b1 && !b2 && !b4)
        || (b2 && !b3 && !b1)
        || (b3 && !b4 && !b2)
        || (b4 && !b1 && !b3);
  }
}
