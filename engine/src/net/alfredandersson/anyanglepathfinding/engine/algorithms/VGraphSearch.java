package net.alfredandersson.anyanglepathfinding.engine.algorithms;

import gnu.trove.set.hash.THashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.alfredandersson.anyanglepathfinding.engine.CellTraversal;
import net.alfredandersson.anyanglepathfinding.engine.EuclideanHeuristic;
import net.alfredandersson.anyanglepathfinding.engine.Heuristic;
import net.alfredandersson.anyanglepathfinding.engine.Map;
import net.alfredandersson.anyanglepathfinding.engine.Point;
import net.alfredandersson.anyanglepathfinding.engine.VGraphConnections;

public class VGraphSearch extends AStarSearch {
  
  protected final List<Point> validNodeList = new ArrayList<>();
  protected final Set<Point> validNodeSet = new THashSet<>();
  
  private final Point tempPoint = new Point();
  
  public VGraphSearch(Map map) {
    this(map, new VGraphConnections(map, false));
  }
  
  public VGraphSearch(Map map, VGraphConnections con) {
    this(map, con, EuclideanHeuristic.INSTANCE);
  }
  
  public VGraphSearch(Map map, Heuristic h) {
    this(map, new VGraphConnections(map, false), h);
  }
  
  public VGraphSearch(Map map, VGraphConnections con, Heuristic h) {
    super(map, con, h);
    
    for (int x = 0; x < map.getWidth() + 1; x++) {
      for (int y = 0; y < map.getHeight() + 1; y++) {
        if (con.isValidNode(x, y)) {
          Point p = new Point(x, y);
          validNodeList.add(p);
          validNodeSet.add(p);
        }
      }
    }
  }
  
  @Override
  protected int[] earlyExit() {
    int[] result = super.earlyExit();
    if (result != null) {
      return result;
    } else if (!CellTraversal.collisionLine(map, startX, startY, endX, endY)) {
      return new int[] {startX, startY, endX, endY};
    } else {
      return null;
    }
  }
  
  @Override
  protected void addStartNodeToOpenSet(int startX, int startY) {
    if (validNodeSet.contains(tempPoint.set(startX, startY))) {
      super.addStartNodeToOpenSet(startX, startY);
    }
  }
  
  @Override
  protected void initStartNode() {
    super.initStartNode();
    
    if (validNodeSet.contains(tempPoint.set(startX, startY))) {
      return;
    }
    
    // add all nodes in line of sight to the open set (as neighbors to the starting node)
    currentX = startX;
    currentY = startY;
    currentSteps = 0;
    
    for (int i = 0; i < validNodeList.size(); i++) {
      Point p = validNodeList.get(i);
      neighborX = p.x;
      neighborY = p.y;
      
      if (CellTraversal.collisionLine(map, currentX, currentY, neighborX, neighborY)) {
        continue;
      }
      
      int dx = neighborX - startX;
      int dy = neighborY - startY;
      
      newCost = neighborCost = (float)Math.sqrt(dx * dx + dy * dy);
      
      updateNeighbor();
      addToOpenSet(neighborX, neighborY);
    }
  }
  
  @Override
  protected int neighborBufferSize() {
    return super.neighborBufferSize() + 1;
  }
  
  @Override
  protected int getNeighborsOfCurrentCell() {
    int n = super.getNeighborsOfCurrentCell();
    
    if (!CellTraversal.collisionLine(map, currentX, currentY, endX, endY)) {
      buf[n * 2] = endX;
      buf[n * 2 + 1] = endY;
      
      int dx = endX - currentX;
      int dy = endY - currentY;
      
      costBuf[n] = (float)Math.sqrt(dx * dx + dy * dy);
      
      n++;
    }
    
    return n;
  }
}
