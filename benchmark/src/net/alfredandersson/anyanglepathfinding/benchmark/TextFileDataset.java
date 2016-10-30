package net.alfredandersson.anyanglepathfinding.benchmark;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import net.alfredandersson.anyanglepathfinding.engine.Map;
import org.apache.commons.io.FileUtils;

public class TextFileDataset extends Dataset {
  
  private static final String[] extension = {"map"};
  
  protected final String name;
  protected final File[] files;
  
  public TextFileDataset(File root) {
    files = FileUtils.listFiles(root, extension, true).toArray(FileUtils.EMPTY_FILE_ARRAY);
    name = root.getName();
  }
  
  @Override
  public int numMaps() {
    return files.length;
  }

  @Override
  public Map getMap(int i) {
    try {
      return Map.fromText(new FileInputStream(files[i]));
    } catch (IOException e) {
      throw new RuntimeException("Failed to load map from " + files[i], e);
    }
  }
  
  @Override
  public String getName(int i) {
    return files[i].getName();
  }
  
  @Override
  public String toString() {
    return getClass().getSimpleName() + ": " + name + " (" + numMaps() + " maps)";
  }
}
