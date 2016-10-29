package net.alfredandersson.anyanglepathfinding.engine.algorithms;

import java.util.ArrayDeque;
import java.util.Deque;
import net.alfredandersson.anyanglepathfinding.engine.GridConnections;
import net.alfredandersson.anyanglepathfinding.engine.Map;

public class BreadthFirstSearch extends GenericSearch {
  
  private final Deque<Integer>
          openListX = new ArrayDeque<>(),
          openListY = new ArrayDeque<>();
  
  public BreadthFirstSearch(Map map, GridConnections con) {
    super(map, con);
  }
  
  @Override
  protected void reset() {
    openListX.clear();
    openListY.clear();
  }
  
  @Override
  protected void addStartNodeToOpenSet(int startX, int startY) {
    openListX.addLast(startX);
    openListY.addLast(startY);
  }
  
  @Override
  protected void addToOpenSet(int x, int y) {
    openListX.addLast(x);
    openListY.addLast(y);
  }
  
  @Override
  protected boolean isOpenSetEmpty() {
    return openListX.isEmpty();
  }
  
  @Override
  protected void popFromOpenSet() {
    currentX = openListX.removeFirst();
    currentY = openListY.removeFirst();
  }
}
