package net.alfredandersson.anyanglepathfinding.util;

public class MathUtil {
  
  public static double frac(double n) {
    return n - floor(n);
  }
  
  public static int floor(double n) {
    int in = (int)n;
    return in == n || n >= 0 ? in : in - 1;
  }
}
