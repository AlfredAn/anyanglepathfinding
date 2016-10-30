package net.alfredandersson.anyanglepathfinding.engine;

public final class CellTraversal {
  
  private CellTraversal() {}
  
  public static boolean collisionLine(Map map, int x1, int y1, int x2, int y2) {
    int x, dx, xStep, endX;
    if (x2 >= x1) {
      x = x1;
      endX = x2 - 1;
      dx = x2 - x1;
      xStep = 1;
    } else {
      x = x1 - 1;
      endX = x2;
      dx = x1 - x2;
      xStep = -1;
    }
    
    int y, dy, yStep, endY;
    if (y2 >= y1) {
      y = y1;
      endY = y2 - 1;
      dy = y2 - y1;
      yStep = 1;
    } else {
      y = y1 - 1;
      endY = y2;
      dy = y1 - y2;
      yStep = -1;
    }
    
    if (dx == 0 && dy == 0) {
      // no movement, only need to check the starting position
      if (map.isBlocked(x - 1, y - 1)
       && map.isBlocked(x - 1, y)
       && map.isBlocked(x, y - 1)
       && map.isBlocked(x, y)) {
        return true;
      }
    } else if (x == endX && y == endY) {
      // only need to move within the starting square
      if (map.isBlocked(x, y)) return true;
    } else if (x1 == x2) {
      // all moves are fully vertical
      if (map.isBlocked(x, y) && map.isBlocked(x - 1, y)) return true;
      while (y != endY) {
        y += yStep;
        if (map.isBlocked(x, y) && map.isBlocked(x - 1, y)) return true;
      }
    } else if (y1 == y2) {
      // all moves are fully horizontal
      if (map.isBlocked(x, y) && map.isBlocked(x, y - 1)) return true;
      while (x != endX) {
        x += xStep;
        if (map.isBlocked(x, y) && map.isBlocked(x, y - 1)) return true;
      }
    } else if (x == endX) {
      // all moves are along the y axis
      if (map.isBlocked(x, y)) return true;
      while (y != endY) {
        y += yStep;
        if (map.isBlocked(x, y)) return true;
      }
    } else if (y == endY) {
      // all moves are along the x axis
      if (map.isBlocked(x, y)) return true;
      while (x != endX) {
        x += xStep;
        if (map.isBlocked(x, y)) return true;
      }
    } else if (dx == dy) {
      // all moves are diagonal
      //System.out.println(x + ", " + y);
      if (map.isBlocked(x, y)) return true;
      while (x != endX || y != endY) {
        x += xStep;
        y += yStep;
        //System.out.println(x + ", " + y);
        if (map.isBlocked(x, y)) return true;
      }
    } else if (dx > dy) {
      if (map.isBlocked(x, y)) return true;
      
      int fracScale = dx * dy;
      int yFrac = 0; // yFrac is (mod fracScale)
      int yPerXStep = dy * dy; // dy / dx
      
      while (x != endX || y != endY) {
        if (yFrac + yPerXStep > fracScale) {
          // y step, then x step
          y += yStep;
          if (map.isBlocked(x, y)) return true;
          
          x += xStep;
          if (map.isBlocked(x, y)) return true;
          
          yFrac = yFrac + yPerXStep - fracScale; // wrap around yFrac
        } else if (yFrac + yPerXStep == fracScale) {
          // simultaneous x and y step
          x += xStep;
          y += yStep;
          if (map.isBlocked(x, y)) return true;
          
          yFrac = 0; // passing through corner of cell
        } else {
          // just x step
          x += xStep;
          if (map.isBlocked(x, y)) return true;
          
          yFrac += yPerXStep;
        }
      }
    } else if (dy > dx) {
      if (map.isBlocked(x, y)) return true;
      
      int fracScale = dx * dy;
      int xFrac = 0; // xFrac is (mod fracScale)
      int xPerYStep = dx * dx; // dx / dy
      
      while (x != endX || y != endY) {
        if (xFrac + xPerYStep > fracScale) {
          // x step, then y step
          x += xStep;
          if (map.isBlocked(x, y)) return true;
          
          y += yStep;
          if (map.isBlocked(x, y)) return true;
          
          xFrac = xFrac + xPerYStep - fracScale; // wrap around xFrac
        } else if (xFrac + xPerYStep == fracScale) {
          // simultaneous x and y step
          x += xStep;
          y += yStep;
          if (map.isBlocked(x, y)) return true;
          
          xFrac = 0; // passing through corner of cell
        } else {
          // just y step
          y += yStep;
          if (map.isBlocked(x, y)) return true;
          
          xFrac += xPerYStep;
        }
      }
    } else {
      throw new AssertionError("Unhandled case: dx = " + dx + ", dy = " + dy);
    }
    
    return false;
  }
}
