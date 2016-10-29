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

public class LazyVGraphSearch extends AStarSearch {
  
  protected final List<Point> validNodeList = new ArrayList<>();
  protected final Set<Point> validNodeSet = new THashSet<>();
  
  private final Point tempPoint = new Point();
  
  public LazyVGraphSearch(Map map) {
    this(map, EuclideanHeuristic.INSTANCE);
  }
  
  public LazyVGraphSearch(Map map, Heuristic h) {
    super(map, new VGraphConnections(map, false), h);
    
    for (int x = 0; x < map.getWidth() + 1; x++) {
      for (int y = 0; y < map.getHeight() + 1; y++) {
        int nearCellsBlocked =
                (map.isBlocked(x - 1, y - 1) ? 1 : 0) +
                (map.isBlocked(x    , y - 1) ? 1 : 0) +
                (map.isBlocked(x - 1, y    ) ? 1 : 0) +
                (map.isBlocked(x    , y    ) ? 1 : 0);
        if (nearCellsBlocked == 1) {
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
  protected void incrementModIndex() {
    modIndex += 3;
  }
  
  @Override
  protected boolean shouldExpandNode() {
    if (lastModified[currentX][currentY] == modIndex + 2) {
      return false;
    } else if (lastModified[currentX][currentY] == modIndex) {
      // haven't checked for line of sight yet
      if (CellTraversal.collisionLine(map, startX, startY, currentX, currentY)) {
        lastModified[currentX][currentY] = modIndex - 1; // pretend we never found this node
        return false;
      } else {
        lastModified[currentX][currentY] = modIndex + 1;
        return true;
      }
    } else {
      return true;
    }
  }
  
  @Override
  protected boolean shouldAddNeighbor(int neighborX, int neighborY) {
    if (lastModified[neighborX][neighborY] == modIndex) {
      // haven't checked for line of sight yet
      if (CellTraversal.collisionLine(map, startX, startY, neighborX, neighborY)) {
        lastModified[neighborX][neighborY] = modIndex - 1;
        return true;
      } else {
        lastModified[neighborX][neighborY] = modIndex + 1;
        return false;
      }
    }
    
    return lastModified[neighborX][neighborY] != modIndex + 1 || newCost < cost[neighborX][neighborY];
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
    
    // add all nodes to the open set (as neighbors to the starting node)
    // they will be checked for line of sight before first being visited
    currentX = startX;
    currentY = startY;
    currentSteps = 0;
    
    for (int i = 0; i < validNodeList.size(); i++) {
      Point p = validNodeList.get(i);
      neighborX = p.x;
      neighborY = p.y;
      
      int dx = neighborX - startX;
      int dy = neighborY - startY;
      
      newCost = neighborCost = (float)Math.sqrt(dx * dx + dy * dy);
      
      updateNeighbor();
      lastModified[neighborX][neighborY] = modIndex;
      addToOpenSet(neighborX, neighborY);
    }
  }
  
  @Override
  protected void updateNeighbor() {
    super.updateNeighbor();
    lastModified[neighborX][neighborY] = modIndex + 1;
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
