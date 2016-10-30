package net.alfredandersson.anyanglepathfinding.engine;

import de.matthiasmann.twl.utils.PNGDecoder;
import it.unimi.dsi.util.XoRoShiRo128PlusRandom;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

public final class Map {
  
  private final boolean[][] cellBlocked;
  
  private Map(boolean[][] cellBlocked) {
    this.cellBlocked = cellBlocked;
  }
  
  public int getWidth() {
    return cellBlocked.length;
  }
  
  public int getHeight() {
    return cellBlocked[0].length;
  }
  
  public boolean isBlocked(int x, int y) {
    if (!isValidCell(x, y)) {
      return true;
    } else {
      return cellBlocked[x][y];
    }
  }
  
  public boolean isValidCell(int x, int y) {
    return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
  }
  
  public static Map fromPNG(InputStream in) throws IOException {
    PNGDecoder dec = new PNGDecoder(in);

    ByteBuffer buf = ByteBuffer.allocate(4 * dec.getWidth() * dec.getHeight());
    dec.decode(buf, 4 * dec.getWidth(), PNGDecoder.Format.RGBA);
    buf.flip();
    
    boolean[][] cells = new boolean[dec.getWidth()][dec.getHeight()];
    
    for (int y = 0; y < dec.getHeight(); y++) {
      for (int x = 0; x < dec.getWidth(); x++) {
        int brightness = (buf.get() & 0xff) + (buf.get() & 0xff) + (buf.get() & 0xff);
        buf.get(); // skip the alpha byte
        cells[x][y] = brightness <= (255 * 3) / 2;
      }
    }
    
    return new Map(cells);
  }
  
  public static Map fromArray(boolean[][] array) {
    if (array.length == 0) {
      return new Map(new boolean[0][]);
    }
    
    int height = array[0].length;
    
    boolean[][] copy = new boolean[array.length][];
    for (int i = 0; i < array.length; i++) {
      if (array[i].length != height) {
        throw new IllegalArgumentException();
      }
      copy[i] = Arrays.copyOf(array[i], height);
    }
    
    return new Map(copy);
  }
  
  public static Map randomize(int width, int height, double fractionBlocked, long seed) {
    Random rand = new XoRoShiRo128PlusRandom(seed);
    
    boolean[][] map = new boolean[width][height];
    
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        map[x][y] = rand.nextFloat() < fractionBlocked;
      }
    }
    
    return new Map(map);
  }
  
  public static Map fromText(InputStream in) throws IOException {
    return fromText(new InputStreamReader(in));
  }
  
  public static Map fromText(Reader reader) throws IOException {
    return fromText((reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader));
  }
  
  public static Map fromText(BufferedReader reader) throws IOException {
    String[] typeLine = readAndSplitLine(reader, 2);
    if (!typeLine[0].equals("type")) {
      throw new IOException("Expected \"type\", found \"" + typeLine[0] + "\"");
    }
    
    switch (typeLine[1]) {
      case "octile":
        break;
      default:
        throw new IOException("Unsupported map type: \"" + typeLine[1] + "\"");
    }
    
    String[] heightLine = readAndSplitLine(reader, 2);
    if (!heightLine[0].equals("height")) {
      throw new IOException("Expected \"height\", found \"" + heightLine[0] + "\"");
    }
    
    int height;
    try {
      height = Integer.parseInt(heightLine[1]);
      if (height <= 0) {
        throw new IOException("Invalid height: " + heightLine[1]);
      }
    } catch (NumberFormatException e) {
      throw new IOException("Invalid height: " + heightLine[1], e);
    }
    
    String[] widthLine = readAndSplitLine(reader, 2);
    if (!widthLine[0].equals("width")) {
      throw new IOException("Expected \"width\", found \"" + widthLine[0] + "\"");
    }
    
    int width;
    try {
      width = Integer.parseInt(widthLine[1]);
      if (width <= 0) {
        throw new IOException("Invalid width: " + widthLine[1]);
      }
    } catch (NumberFormatException e) {
      throw new IOException("Invalid width: " + widthLine[1], e);
    }
    
    String[] mapLine = readAndSplitLine(reader, 1);
    if (!mapLine[0].equals("map")) {
      throw new IOException("Expected \"map\", found \"" + mapLine[0] + "\"");
    }
    
    boolean[][] map = new boolean[width][height];
    
    for (int y = 0; y < height; y++) {
      String line = reader.readLine();
      if (line.length() != width) {
        throw new IOException("Invalid length of line: " + line.length());
      }
      
      for (int x = 0; x < width; x++) {
        char c = line.charAt(x);
        
        switch (c) {
          case '@':
            map[x][y] = true;
            break;
          case 'T':
          case '.':
            map[x][y] = false;
            break;
          default:
            throw new IOException("Unexpected symbol: " + c);
        }
      }
    }
    
    return new Map(map);
  }
  
  private static String[] readAndSplitLine(BufferedReader reader, int expectedWords) throws IOException {
    String str = reader.readLine();
    if (str == null) {
      throw new EOFException();
    }
    String[] split = str.split(" ");
    
    if (expectedWords >= 0 && split.length != expectedWords) {
      throw new IOException("Unexpected line: " + str);
    }
    
    return split;
  }
  
  public static Map fromTextAlt(InputStream in) throws IOException {
    return fromTextAlt(new InputStreamReader(in));
  }
  
  public static Map fromTextAlt(Reader reader) throws IOException {
    return fromTextAlt((reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader));
  }
  
  public static Map fromTextAlt(BufferedReader reader) throws IOException {
    String[] sizeLine = readAndSplitLine(reader, 2);
    
    int width, height;
    try {
      width = Integer.parseInt(sizeLine[0]);
      height = Integer.parseInt(sizeLine[1]);
      
      if (width <= 0 || height <= 0) {
        throw new IOException("Invalid dimensions: " + sizeLine[0] + " " + sizeLine[1]);
      }
    } catch (NumberFormatException e) {
      throw new IOException("Invalid dimensions: " + sizeLine[0] + " " + sizeLine[1], e);
    }
    
    boolean[][] map = new boolean[width][height];
    
    for (int y = 0; y < height; y++) {
      String[] row = readAndSplitLine(reader, width);
      for (int x = 0; x < width; x++) {
        map[x][y] = row[x].equals("1");
      }
    }
    
    return new Map(map);
  }
}
