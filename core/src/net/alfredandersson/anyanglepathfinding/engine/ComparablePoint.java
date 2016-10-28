package net.alfredandersson.anyanglepathfinding.engine;

public final class ComparablePoint implements Comparable<ComparablePoint> {
  
  public int x, y;
  public float priority;
  
  public ComparablePoint(int x, int y, float priority) {
    set(x, y, priority);
  }
  
  public ComparablePoint set(int x, int y, float priority) {
    this.x = x;
    this.y = y;
    this.priority = priority;
    return this;
  }
  
  @Override
  public int compareTo(ComparablePoint other) {
    return Float.compare(priority, other.priority);
  }
}
