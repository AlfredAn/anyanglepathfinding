package net.alfredandersson.anyanglepathfinding.benchmark;

import it.unimi.dsi.Util;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;
import java.util.Random;
import net.alfredandersson.anyanglepathfinding.engine.Map;

public class RandomMapDataset extends Dataset {
  
  private final int numMaps, width, height;
  private final float fractionBlocked;
  private final long[] mapSeeds;
  
  public RandomMapDataset(int numMaps, int width, int height, float fractionBlocked) {
    this(numMaps, width, height, fractionBlocked, Util.randomSeed());
  }
  
  public RandomMapDataset(int numMaps, int width, int height, float fractionBlocked, long seed) {
    this.numMaps = numMaps;
    this.width = width;
    this.height = height;
    this.fractionBlocked = fractionBlocked;
    
    Random rand = new XoRoShiRo128PlusRandom(seed);
    mapSeeds = new long[numMaps];
    
    for (int i = 0; i < numMaps; i++) {
      mapSeeds[i] = rand.nextLong();
    }
  }
  
  public long getSeed(int i) {
    return mapSeeds[i];
  }
  
  @Override
  public int numMaps() {
    return numMaps;
  }
  
  @Override
  public Map getMap(int i) {
    return Map.randomize(width, height, fractionBlocked, mapSeeds[i]);
  }
  
  @Override
  public String toString() {
    return getClass().getSimpleName() + ": "
            + width + "x" + height + ", " 
            + Math.round(fractionBlocked * 100) + "% blocked";
  }
}
