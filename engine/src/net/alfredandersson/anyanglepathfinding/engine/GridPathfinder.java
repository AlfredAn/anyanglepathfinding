package net.alfredandersson.anyanglepathfinding.engine;

public abstract class GridPathfinder extends Pathfinder {
  
  public final GridConnections con;
  
  public GridPathfinder(Map map, GridConnections con) {
    super(map);
    this.con = con;
  }
}
