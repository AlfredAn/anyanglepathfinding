package net.alfredandersson.anyanglepathfinding.desktop;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.alfredandersson.anyanglepathfinding.AnyAnglePathfinding;
import net.alfredandersson.anyanglepathfinding.Game;

public class DesktopLauncher {
  
  public static void main(String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    
    config.width = Game.WIDTH;
    config.height = Game.HEIGHT;
    
    config.resizable = false;
    
    DisplayMode dm = LwjglApplicationConfiguration.getDesktopDisplayMode();
    config.foregroundFPS = dm.refreshRate;
    config.backgroundFPS = dm.refreshRate;
    
    config.vSyncEnabled = true;
    
    new LwjglApplication(new AnyAnglePathfinding(), config);
  }
}
