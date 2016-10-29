package net.alfredandersson.anyanglepathfinding.desktop;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.alfredandersson.anyanglepathfinding.core.AnyAnglePathfinding;

public class DesktopLauncher {
  
  public static void main(String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    
    DisplayMode dm = LwjglApplicationConfiguration.getDesktopDisplayMode();
    
    config.width = dm.width;
    config.height = dm.height;
    
    config.foregroundFPS = dm.refreshRate;
    config.backgroundFPS = dm.refreshRate;
    config.vSyncEnabled = true;
    
    config.fullscreen = true;
    
    config.resizable = true;
    
    new LwjglApplication(new AnyAnglePathfinding(), config);
  }
}
