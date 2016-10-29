package net.alfredandersson.anyanglepathfinding.engine;

public final class DiagonalDistance implements Heuristic {
  
  public static final DiagonalDistance INSTANCE = new DiagonalDistance();
  
  private static final float DIAG_COST_MINUS_2 = (float)(Math.sqrt(2) - 2);
  
  @Override
  public float get(int fromX, int fromY, int toX, int toY) {
    int dx = Math.abs(toX - fromX);
    int dy = Math.abs(toY - fromY);
    return dx + dy + DIAG_COST_MINUS_2 * Math.min(dx, dy);
  }
}
