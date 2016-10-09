package net.alfredandersson.anyanglepathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

public final class Draw implements Disposable {
  
  public OrthographicCamera cam;
  
  public final SpriteBatch sprites;
  public final ShapeRenderer shapes;
  
  public Draw() {
    sprites = new SpriteBatch();
    sprites.enableBlending();
    sprites.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    shapes = new ShapeRenderer();
    shapes.setAutoShapeType(true);
    
    cam = new OrthographicCamera();
  }
  
  public void enableBlending() {
    Gdx.gl.glBlendFunc(
            GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glEnable(GL20.GL_BLEND);
    sprites.enableBlending();
  }
  
  public void disableBlending() {
    Gdx.gl.glDisable(GL20.GL_BLEND);
  }
  
  @Override
  public void dispose() {
    sprites.dispose();
    shapes.dispose();
  }
}
