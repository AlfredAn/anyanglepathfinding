package net.alfredandersson.anyanglepathfinding.engine;

public final class ManhattanDistance implements Heuristic {
  
  public static final ManhattanDistance INSTANCE = new ManhattanDistance();
  
  private ManhattanDistance() {}
  
  @Override
  public float get(int fromX, int fromY, int toX, int toY) {
    return Math.abs(toX - fromX) + Math.abs(toY - fromY);
  }
}
