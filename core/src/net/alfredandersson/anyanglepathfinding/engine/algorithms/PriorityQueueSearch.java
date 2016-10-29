package net.alfredandersson.anyanglepathfinding.engine.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import net.alfredandersson.anyanglepathfinding.engine.ComparablePoint;
import net.alfredandersson.anyanglepathfinding.engine.GridConnections;
import net.alfredandersson.anyanglepathfinding.engine.Map;

public abstract class PriorityQueueSearch extends GenericSearch {
  
  protected final PriorityQueue<ComparablePoint> open = new PriorityQueue<>();
  private final List<ComparablePoint> pool = new ArrayList<>();
  
  public PriorityQueueSearch(Map map, GridConnections con) {
    super(map, con);
  }
  
  @Override
  protected void reset() {
    for (ComparablePoint p : open) {
      freePoint(p);
    }
    open.clear();
  }
  
  protected abstract float getPriority(int x, int y);
  
  @Override
  protected boolean isOpenSetEmpty() {
    return open.isEmpty();
  }
  
  @Override
  protected void popFromOpenSet() {
    ComparablePoint current = open.poll();
    currentX = current.x;
    currentY = current.y;
    freePoint(current);
  }
  
  @Override
  protected void addStartNodeToOpenSet(int startX, int startY) {
    addToOpenSet(startX, startY);
  }
  
  @Override
  protected void addToOpenSet(int x, int y) {
    open.add(newPoint(x, y, getPriority(x, y)));
  }
  
  protected final ComparablePoint newPoint(int x, int y, float priority) {
    if (!pool.isEmpty()) {
      return pool.remove(pool.size() - 1).set(x, y, priority);
    } else {
      return new ComparablePoint(x, y, priority);
    }
  }
  
  protected final void freePoint(ComparablePoint point) {
    pool.add(point);
  }
}
