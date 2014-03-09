package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    
    public static void main(String[] args) {
      Main app = new Main();
      app.start();
      }

    @Override
    public void simpleInitApp() {
    this.getStateManager().attach(new GUIManager());
    this.setShowSettings(false);
    this.setDisplayStatView(false);
    }
}
