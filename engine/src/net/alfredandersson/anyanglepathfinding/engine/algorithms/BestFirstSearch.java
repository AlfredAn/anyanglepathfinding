package net.alfredandersson.anyanglepathfinding.engine.algorithms;

import net.alfredandersson.anyanglepathfinding.engine.GridConnections;
import net.alfredandersson.anyanglepathfinding.engine.Heuristic;
import net.alfredandersson.anyanglepathfinding.engine.Map;

public class BestFirstSearch extends PriorityQueueSearch {
  
  public final Heuristic h;
  
  public BestFirstSearch(Map map, GridConnections con) {
    this(map, con, con.defaultHeuristic());
  }
  
  public BestFirstSearch(Map map, GridConnections con, Heuristic h) {
    super(map, con);
    this.h = h;
  }
  
  @Override
  protected float getPriority(int x, int y) {
    return h.get(x, y, endX, endY);
  }
}
