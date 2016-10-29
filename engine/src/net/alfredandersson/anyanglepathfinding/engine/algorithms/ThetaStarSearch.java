package net.alfredandersson.anyanglepathfinding.engine.algorithms;

import net.alfredandersson.anyanglepathfinding.engine.CellTraversal;
import net.alfredandersson.anyanglepathfinding.engine.EuclideanHeuristic;
import net.alfredandersson.anyanglepathfinding.engine.GridConnections;
import net.alfredandersson.anyanglepathfinding.engine.Heuristic;
import net.alfredandersson.anyanglepathfinding.engine.Map;

public class ThetaStarSearch extends AStarSearch {
  
  protected int parentX, parentY;
  
  public ThetaStarSearch(Map map, GridConnections con) {
    this(map, con, EuclideanHeuristic.INSTANCE);
  }
  
  public ThetaStarSearch(Map map, GridConnections con, Heuristic h) {
    super(map, con, h);
  }
  
  @Override
  protected void initNeighbor(int neighborX, int neighborY, float neighborCost) {
    super.initNeighbor(neighborX, neighborY, neighborCost);
    
    int px = cameFromX[currentX][currentY];
    int py = cameFromY[currentX][currentY];
    
    if (CellTraversal.collisionLine(map, px, py, neighborX, neighborY)) {
      // use standard A* parent
      parentX = currentX;
      parentY = currentY;
    } else {
      // use shortcut
      parentX = px;
      parentY = py;
      
      int dx = neighborX - px;
      int dy = neighborY - py;
      
      this.neighborCost = (float)Math.sqrt(dx * dx + dy * dy);
      newCost = cost[px][py] + this.neighborCost;
    }
  }
  
  @Override
  protected void updateNeighbor() {
    super.updateNeighbor();
    
    cameFromX[neighborX][neighborY] = parentX;
    cameFromY[neighborX][neighborY] = parentY;
  }
}
