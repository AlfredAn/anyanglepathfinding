package net.alfredandersson.anyanglepathfinding.engine.algorithms;

import java.util.Arrays;
import net.alfredandersson.anyanglepathfinding.engine.CellTraversal;
import net.alfredandersson.anyanglepathfinding.engine.GridConnections;
import net.alfredandersson.anyanglepathfinding.engine.Heuristic;
import net.alfredandersson.anyanglepathfinding.engine.Map;

public class AStarPostSmoothedSearch extends AStarSearch {
  
  public AStarPostSmoothedSearch(Map map, GridConnections con) {
    super(map, con);
  }
  
  public AStarPostSmoothedSearch(Map map, GridConnections con, Heuristic h) {
    super(map, con, h);
  }
  
  @Override
  protected int[] assemblePath() {
    int[] base = super.assemblePath();
    
    int bx = base[0];
    int by = base[1];
    int px = bx;
    int py = by;
    int j = 0;
    for (int i = 1; i < base.length / 2; i++) {
      int x = base[i * 2];
      int y = base[i * 2 + 1];
      
      if (CellTraversal.collisionLine(map, bx, by, x, y)) {
        j++;
        base[j * 2] = px;
        base[j * 2 + 1] = py;
        bx = px;
        by = py;
      }
      
      px = x;
      py = y;
    }
    
    j++;
    base[j * 2] = base[base.length - 2];
    base[j * 2 + 1] = base[base.length - 1];
    
    return Arrays.copyOf(base, (j+1) * 2);
  }
}
