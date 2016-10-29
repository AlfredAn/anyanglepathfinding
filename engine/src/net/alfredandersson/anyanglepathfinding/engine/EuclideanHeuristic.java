package net.alfredandersson.anyanglepathfinding.engine;

public final class EuclideanHeuristic implements Heuristic {
  
  public static final EuclideanHeuristic INSTANCE = new EuclideanHeuristic();
  
  @Override
  public float get(int fromX, int fromY, int toX, int toY) {
    int dx = toX - fromX;
    int dy = toY - fromY;
    return (float)Math.sqrt(dx * dx + dy * dy);
  }
}
