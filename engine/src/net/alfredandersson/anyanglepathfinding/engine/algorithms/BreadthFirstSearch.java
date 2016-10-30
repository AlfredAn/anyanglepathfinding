package net.alfredandersson.anyanglepathfinding.engine.algorithms;

import java.util.ArrayDeque;
import java.util.Deque;
import net.alfredandersson.anyanglepathfinding.engine.GridConnections;
import net.alfredandersson.anyanglepathfinding.engine.Map;

public class BreadthFirstSearch extends GenericSearch {
  
  protected final Deque<Integer>
          openListX = new ArrayDeque<>(),
          openListY = new ArrayDeque<>();
  
  protected final int[][] steps;
  
  public BreadthFirstSearch(Map map, GridConnections con) {
    super(map, con);
    
    steps = new int[map.getWidth() + 1][map.getHeight() + 1];
  }
  
  @Override
  protected void reset() {
    openListX.clear();
    openListY.clear();
  }
  
  @Override
  protected void initStartNode() {
    super.initStartNode();
    steps[startX][startY] = 0;
  }
  
  @Override
  protected void addStartNodeToOpenSet(int startX, int startY) {
    openListX.addLast(startX);
    openListY.addLast(startY);
  }
  
  @Override
  protected void updateNeighbor() {
    super.updateNeighbor();
    steps[neighborX][neighborY] = currentSteps + 1;
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
    
    currentSteps = steps[currentX][currentY];
  }
}
