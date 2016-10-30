package net.alfredandersson.anyanglepathfinding.benchmark;

import net.alfredandersson.anyanglepathfinding.engine.Map;

public abstract class Dataset {
  
  public abstract int numMaps();
  
  public abstract Map getMap(int i);
  
  public String getName(int i) {
    return "#" + i;
  }
}
