package net.alfredandersson.anyanglepathfinding.benchmark;

import it.unimi.dsi.Util;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import net.alfredandersson.anyanglepathfinding.engine.CellTraversal;
import net.alfredandersson.anyanglepathfinding.engine.EightConnectedGrid;
import net.alfredandersson.anyanglepathfinding.engine.GridConnections;
import net.alfredandersson.anyanglepathfinding.engine.Map;
import net.alfredandersson.anyanglepathfinding.engine.Pathfinder;
import net.alfredandersson.anyanglepathfinding.engine.VGraphConnections;
import net.alfredandersson.anyanglepathfinding.engine.algorithms.AStarPostSmoothedSearch;
import net.alfredandersson.anyanglepathfinding.engine.algorithms.AStarSearch;
import net.alfredandersson.anyanglepathfinding.engine.algorithms.LazyVGraphSearch;
import net.alfredandersson.anyanglepathfinding.engine.algorithms.ThetaStarSearch;
import net.alfredandersson.anyanglepathfinding.engine.algorithms.VGraphSearch;

public final class Benchmark {
  
  private static final Dataset[] datasets;
  
  static {
    try {
      datasets = new Dataset[] {
        //new RandomMapDataset(32, 100, 100, 0.1f),
        //new RandomMapDataset(32, 100, 100, 0.2f),
        //new RandomMapDataset(32, 100, 100, 0.3f),
        //new RandomMapDataset(32, 500, 500, 0.1f),
        //new RandomMapDataset(32, 500, 500, 0.2f),
        //new RandomMapDataset(32, 500, 500, 0.3f),
        //new TextFileDataset(new File("testmaps/bgmaps")),
        //new TextFileDataset(new File("testmaps/da2")),
        //new TextFileDataset(new File("testmaps/dao")),
        //new TextFileDataset(new File("testmaps/sc1")),
        new AltTextFileDataset(new File("testmaps/sc2")),
      };
    } catch (NullPointerException e) {
      throw new RuntimeException("Failed to load dataset files", e);
    }
  }
  
  public static void main(String[] args) throws Throwable {
    Benchmark b = new Benchmark();
    
    System.out.println("----------\nRunning warmup");
    b.run(new RandomMapDataset(4, 64, 64, 0.2f), 64, Util.randomSeed());
    
    b.result.setLength(0);
    
    for (int i = 0; i < datasets.length; i++) {
      System.out.println("----------\nRunning dataset #" + (i+1) + ": " + datasets[i].toString());
      b.result.append("----------Dataset #").append(i+1).append(": ").append(datasets[i].toString()).append("----------\n");
      b.run(datasets[i], 1024, Util.randomSeed());
    }
    
    System.out.println("----------RESULTS----------");
    System.out.println(b.result);
    
    try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("RESULT.txt")), "UTF-8"))) {
      out.write(b.result.toString());
      out.flush();
    }
  }
  
  private StringBuilder result = new StringBuilder();
  
  private long time;
  private String timerTag;
  
  private Benchmark() {}
  
  private void run(Dataset dataset, int pathsPerMap, long seed) {
    Pathfinder[] algos = new Pathfinder[5];
    int[] startX = new int[pathsPerMap];
    int[] startY = new int[pathsPerMap];
    int[] endX = new int[pathsPerMap];
    int[] endY = new int[pathsPerMap];
    Random rand = new XoRoShiRo128PlusRandom(seed);
    
    int[][][] paths = new int[algos.length][pathsPerMap][];
    
    long numPaths = 0;
    
    BigInteger totalVGraphGenNanos = BigInteger.ZERO;
    BigInteger[] totalAlgoNanos = new BigInteger[algos.length];
    Arrays.fill(totalAlgoNanos, BigInteger.ZERO);
    
    double[] addLenFrac = new double[algos.length];
    double[] totalLengthFraction = new double[algos.length];
    
    for (int i = 0; i < dataset.numMaps(); i++) {
      System.out.println("---Map " + dataset.getName(i) + "---");
      
      Map map = dataset.getMap(i);
      GridConnections eightCon = new EightConnectedGrid(map);
      
      System.gc();
      
      startTimer("Generate VGraphConnections");
      VGraphConnections vGraphCon = new VGraphConnections(map);
      totalVGraphGenNanos = totalVGraphGenNanos.add(BigInteger.valueOf(stopTimer()));
      
      algos[0] = new AStarSearch(map, eightCon);
      algos[1] = new AStarPostSmoothedSearch(map, eightCon);
      algos[2] = new ThetaStarSearch(map, eightCon);
      algos[3] = new VGraphSearch(map, vGraphCon);
      algos[4] = new LazyVGraphSearch(map, vGraphCon);
      
      //System.out.println("Generating path points...");
      for (int j = 0; j < pathsPerMap; j++) {
        // find points with a valid path between them
        while (true) {
          startX[j] = rand.nextInt(map.getWidth() + 1);
          startY[j] = rand.nextInt(map.getHeight() + 1);
          endX[j] = rand.nextInt(map.getWidth() + 1);
          endY[j] = rand.nextInt(map.getHeight() + 1);
          
          if (startX[j] != endX[j] && startY[j] != endY[j] && algos[0].findPath(startX[j], startY[j], endX[j], endY[j]) != null) {
            break;
          }
        }
      }
      
      for (int a = 0; a < algos.length; a++) {
        Pathfinder algo = algos[a];
        startTimer(algo.getClass().getSimpleName());
        for (int j = 0; j < pathsPerMap; j++) {
          paths[a][j] = algo.findPath(startX[j], startY[j], endX[j], endY[j]);
        }
        totalAlgoNanos[a] = totalAlgoNanos[a].add(BigInteger.valueOf(stopTimer()));
      }
      
      // calc path lengths
      for (int j = 0; j < pathsPerMap; j++) {
        for (int a = 0; a < algos.length; a++) {
          if (paths[a][j] == null) {
            System.out.println("NULL: " + algos[a].getClass().getSimpleName() + " - (" + startX[j] + ", " + startY[j] + ")->(" + endX[j] + ", " + endY[j] + ")");
          } else if (a == 4 && Math.abs(calcLength(paths[4][j]) - calcLength(paths[3][j])) > 0.00001) {
            System.out.println("WRONG: " + algos[a].getClass().getSimpleName() + " - (" + startX[j] + ", " + startY[j] + ")->(" + endX[j] + ", " + endY[j] + ")");
          } else if (a == 3 && calcLength(paths[3][j]) > calcLength(paths[2][j]) + 0.00001) {
            System.out.println("THETA: " + algos[a].getClass().getSimpleName() + " - (" + startX[j] + ", " + startY[j] + ")->(" + endX[j] + ", " + endY[j] + ")");
          }
        }
        
        double optimalLength = calcLength(paths[3][j]);
        
        for (int a = 0; a < algos.length; a++) {
          addLenFrac[a] += calcLength(paths[a][j]) / optimalLength - 1;
        }
      }
      
      for (int a = 0; a < algos.length; a++) {
        totalLengthFraction[a] += addLenFrac[a];
        addLenFrac[a] = 0;
      }
      
      numPaths += pathsPerMap;
    }
    
    System.out.println("---DONE!---");
    for (int a = 0; a < algos.length; a++) {
      double totalTimeNanos = totalAlgoNanos[a].doubleValue();
      double msPerPath = totalTimeNanos * 0.000001 / numPaths;
      double lenFactor = totalLengthFraction[a] / numPaths + 1;
      result.append(algos[a].getClass().getSimpleName()).append(": ").append(msPerPath).append("ms, lengthFactor: ").append(lenFactor).append('\n');
    }
    result.append("VGraphConnections generation: ").append(totalVGraphGenNanos.doubleValue() * 0.000001 / dataset.numMaps()).append("ms").append('\n');
  }
  
  private double calcLength(int[] path) {
    return calcLength(path, null);
  }
  
  private double calcLength(int[] path, Map map) {
    if (path.length == 0) {
      return 0;
    }
    
    double length = 0;
    
    boolean illegal = false;
    
    int px = path[0];
    int py = path[1];
    for (int i = 1; i < path.length / 2; i++) {
      int x = path[i * 2];
      int y = path[i * 2 + 1];
      
      int dx = x - px;
      int dy = y - py;
      
      length += Math.sqrt(dx * dx + dy * dy);
      
      if (map != null && !illegal && CellTraversal.collisionLine(map, px, py, x, y)) {
        System.out.println("ILLEGAL PATH");
        illegal = true;
      }
      
      px = x;
      py = y;
    }
    
    return length;
  }
  
  private void startTimer() {
    startTimer("");
  }
  
  private void startTimer(String tag) {
    timerTag = tag;
    time = System.nanoTime();
  }
  
  private long stopTimer() {
    return System.nanoTime() - time;
  }
  
  private long stopTimerPrint() {
    long delta = stopTimer();
    double ms = (double)delta * 0.000001;
    
    System.out.println(timerTag + ": " + ms + "ms");
    
    return delta;
  }
}