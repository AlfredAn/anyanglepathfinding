package net.alfredandersson.anyanglepathfinding.engine;

public interface Heuristic {
  
  float get(int fromX, int fromY, int toX, int toY);
}
