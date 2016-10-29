package net.alfredandersson.anyanglepathfinding.engine;

public final class Point {
  
  public int x, y;
  
  public Point() {}
  
  public Point(int x, int y) {
    set(x, y);
  }
  
  public Point set(int x, int y) {
    this.x = x;
    this.y = y;
    return this;
  }
  
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Point)) return false;
    
    Point other = (Point)o;
    
    return other.x == x && other.y == y;
  }
  
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 23 * hash + x;
    hash = 23 * hash + y;
    return hash;
  }
}
