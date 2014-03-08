/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector2f;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Screen;

/**
 *
 * @author Bob
 */
public class GUIManager extends AbstractAppState {
    
  public    SimpleApplication      app;
  public    AppStateManager        stateManager;
  public    AssetManager           assetManager;
  public    Screen                 screen;
  public    Window                 startMenu;
  public    boolean                chase;
  public    boolean                started;

  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    this.app           = (SimpleApplication) app;
    this.stateManager  = this.app.getStateManager();
    this.assetManager  = this.app.getAssetManager();
    chase = false;
    started = false;
    createStartMenu();
    }

  public void createStartMenu(){  
        screen = new Screen(app, "tonegod/gui/style/atlasdef/style_map.gui.xml");
        screen.setUseTextureAtlas(true,"tonegod/gui/style/atlasdef/atlas.png");
        screen.setUseMultiTouch(true);
        app.getGuiNode().addControl(screen);
        startMenu = new Window(screen, "MainWindow", new Vector2f(15f, 15f));
        startMenu.setWindowTitle("Main Windows");
        startMenu.setMinDimensions(new Vector2f(130, 100));
        startMenu.setWidth(new Float(50));
        startMenu.setHeight(new Float (15));
        startMenu.setIgnoreMouse(true);
        startMenu.setWindowIsMovable(false);
        app.getInputManager().setCursorVisible(true);
        
        // create buttons
        ButtonAdapter chaseButton = new ButtonAdapter( screen, "ChaseButton", new Vector2f(15, 15) ) {
         @Override
           public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
             chase = true;
             startMenu.removeFromParent();
             stateManager.attach(new SceneManager());
             stateManager.attach(new PlayerManager());
             stateManager.attach(new CameraManager());
             stateManager.attach(new InteractionManager());
             app.getInputManager().setCursorVisible(true);
             startMenu.hideWindow();
             started = true;
            }  
          };

        ButtonAdapter arcadeButton = new ButtonAdapter( screen, "TopButton", new Vector2f(15, 15) ) {
         @Override
           public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
             chase = false;
             stateManager.attach(new PlayerManager());
             stateManager.attach(new SceneManager());
             stateManager.attach(new CameraManager());
             startMenu.removeFromParent();
             stateManager.attach(new InteractionManager());
             app.getInputManager().setCursorVisible(true);
             startMenu.hideWindow();
             started = true;
            }  
          };
        
        arcadeButton.setText("Arcade Mode");
        startMenu.addChild(arcadeButton);
        arcadeButton.setPosition(15, -75);
        
        chaseButton.setText("Chase Mode");
        startMenu.addChild(chaseButton);
        screen.addElement(startMenu);
        startMenu.setLocalTranslation(screen.getWidth() / 2 - startMenu.getWidth()/2, screen.getHeight() / 2 + startMenu.getHeight()/2, 0);
     }
    
  @Override
    public void update(float tpf){
      if (started) {
        BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        String bla = String.valueOf(stateManager.getState(PlayerManager.class).currentPlayer.score);
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText(bla);
        helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
        app.getGuiNode().detachAllChildren();
        app.getGuiNode().attachChild(helloText);
        }
      }
}
