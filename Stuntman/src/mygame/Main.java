package mygame;

import com.jme3.app.SimpleApplication;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    
    public static void main(String[] args) {
      Main app = new Main();
      app.start();
      app.stateManager.attach(new GUIManager());
      }

    @Override
    public void simpleInitApp() {
      
    }
}
