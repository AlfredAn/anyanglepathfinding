package net.alfredandersson.anyanglepathfinding;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public final class AnyAnglePathfinding extends ApplicationAdapter {
  
  private final Game game;
  private Draw d;
  
  public AnyAnglePathfinding() {
    game = new Game(this);
  }
  
  @Override
  public void create() {
    d = new Draw();
    game.create();
  }
  
  @Override
  public void render() {
    game.update();
    
    Gdx.gl.glClearColor(.75f, .75f, .75f, 1.0f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
    game.draw(d);
  }
  
  @Override
  public void dispose() {
    game.dispose();
    d = null;
  }
}
