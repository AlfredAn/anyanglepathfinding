package net.alfredandersson.anyanglepathfinding.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;

public final class AnyAnglePathfinding extends ApplicationAdapter {
  
  private Game game;
  private Draw d;
  
  private int
          windowedWidth,
          windowedHeight;
  
  @Override
  public void create() {
    if (game == null) {
      game = new Game(this);
      
      DisplayMode desktop = Gdx.graphics.getDisplayMode();
      windowedWidth = (int)(desktop.width * 0.8);
      windowedHeight = (int)(desktop.height * 0.8);
    }
    d = new Draw();
    game.create();
  }
  
  private void update() {
    game.update();
    
    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
      Gdx.app.exit();
    }
    
    if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
      if (Gdx.graphics.isFullscreen()) {
        Gdx.graphics.setResizable(true);
        Gdx.graphics.setWindowedMode(windowedWidth, windowedHeight);
      } else {
        windowedWidth = Gdx.graphics.getBackBufferWidth();
        windowedHeight = Gdx.graphics.getBackBufferHeight();
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
      }
    }
  }
  
  @Override
  public void render() {
    update();
    
    Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
    game.draw(d);
  }
  
  @Override
  public void dispose() {
    game.dispose();
    d = null;
  }
}
