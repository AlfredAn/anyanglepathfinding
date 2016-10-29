package net.alfredandersson.anyanglepathfinding.engine.algorithms;

import net.alfredandersson.anyanglepathfinding.engine.GridConnections;
import net.alfredandersson.anyanglepathfinding.engine.Heuristic;
import net.alfredandersson.anyanglepathfinding.engine.Map;

public class AStarSearch extends DijkstraSearch {
  
  protected final Heuristic h;
  
  public AStarSearch(Map map, GridConnections con) {
    this(map, con, con.defaultHeuristic());
  }
  
  public AStarSearch(Map map, GridConnections con, Heuristic h) {
    super(map, con);
    this.h = h;
  }
  
  @Override
  protected float getPriority(int x, int y) {
    return cost[x][y] + h.get(x, y, endX, endY);
  }
}
