package net.alfredandersson.anyanglepathfinding.engine.algorithms;

import net.alfredandersson.anyanglepathfinding.engine.GridConnections;
import net.alfredandersson.anyanglepathfinding.engine.Map;

public class DijkstraSearch extends PriorityQueueSearch {
  
  protected final float[][] cost;
  protected float neighborCost;
  protected float newCost;
  
  public DijkstraSearch(Map map, GridConnections con) {
    super(map, con);
    cost = new float[map.getWidth() + 1][map.getHeight() + 1];
  }
  
  @Override
  protected float getPriority(int x, int y) {
    return cost[x][y];
  }
  
  @Override
  protected void incrementModIndex() {
    modIndex += 2;
  }
  
  @Override
  protected void initStartNode() {
    super.initStartNode();
    cost[startX][startY] = 0;
  }
  
  @Override
  protected void updateNeighbor() {
    super.updateNeighbor();
    cost[neighborX][neighborY] = newCost;
  }
  
  @Override
  protected void addToOpenSet(int x, int y) {
    open.add(newPoint(x, y, getPriority(x, y)));
  }
  
  @Override
  protected boolean shouldExpandNode() {
    return lastModified[currentX][currentY] != modIndex + 1;
  }
  
  @Override
  protected void initNeighbor(int neighborX, int neighborY, float neighborCost) {
    super.initNeighbor(neighborX, neighborY, neighborCost);
    this.neighborCost = neighborCost;
    newCost = cost[currentX][currentY] + neighborCost;
  }
  
  @Override
  protected boolean shouldAddNeighbor(int neighborX, int neighborY) {
    return lastModified[neighborX][neighborY] != modIndex || newCost < cost[neighborX][neighborY];
  }
}
