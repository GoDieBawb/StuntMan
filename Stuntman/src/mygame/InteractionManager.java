package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import tonegod.gui.core.Screen;


public class InteractionManager extends AbstractAppState implements ActionListener {

  public    SimpleApplication      app;
  public    AppStateManager        stateManager;
  public    InputManager           inputManager;
  public    boolean                up = false;
  public    boolean                down = false;
    
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    this.app           = (SimpleApplication) app;
    this.stateManager  = this.app.getStateManager();
    this.inputManager  = this.app.getInputManager();
    inputManager.setSimulateMouse(true);
    System.out.println("InteractionManager Attached");
    setUpKeys();
    }
  
  //Sets up the keys
  
  public void setUpKeys(){
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    inputManager.addListener(this, "Up");
    inputManager.addListener(this, "Down");
    inputManager.addListener(this, "Left");
    inputManager.addListener(this, "Right");
    inputManager.addListener(this, "Click");
    }
  
  //Listens for keys

  public void onAction(String binding, boolean isPressed, float tpf) {
    if (binding.equals("Up") ^ (binding.equals("Left"))){
      up = isPressed;
      } else if (binding.equals("Down") ^ (binding.equals("Right"))){
      down = isPressed;
      }

    if (binding.equals("Click")) {
      Vector2f clickSpot = inputManager.getCursorPosition();
      float xSpot = clickSpot.getX();
      float ySpot = clickSpot.getY();
      Screen screen = stateManager.getState(GUIManager.class).screen;
      
      
      if (ySpot > screen.getHeight()/2 && xSpot > screen.getWidth()/2)
        up = isPressed;
      
      else if (ySpot < screen.getHeight()/2 && xSpot < screen.getWidth()/2)
        up = isPressed;
      
      else
       down = isPressed;
      
      }
    }
  
  //Acts on the keys being listened for
  
  @Override
  public void update(float tpf){
  Player player = stateManager.getState(PlayerManager.class).currentPlayer;
  if (!player.isDead){
    Node   wallNode = stateManager.getState(SceneManager.class).wallNode;
    CollisionResults results = new CollisionResults();
    wallNode.collideWith(player.model.getWorldBound(), results);
    
    if(results.size() > 0){
    player.isDead = true;
    app.getRootNode().detachAllChildren();
    stateManager.detach(stateManager.getState(CameraManager.class));
    stateManager.detach(stateManager.getState(PlayerManager.class));
    stateManager.detach(stateManager.getState(SceneManager.class));
    stateManager.detach(stateManager.getState(InteractionManager.class));
    stateManager.detach(stateManager.getState(GUIManager.class));
    stateManager.attach(new GUIManager());
    System.out.println("you died");
    }
    
    if (up){
      Vector3f moveDir = new Vector3f(-10f, 0f, 0f);
      player.model.setLocalTranslation(player.model.getLocalTranslation().addLocal(moveDir.mult(tpf)));
      }
    if(down){
      Vector3f moveDir = new Vector3f(10f, 0f, 0f);
      player.model.setLocalTranslation(player.model.getLocalTranslation().addLocal(moveDir.mult(tpf)));
      }
    }
  }
}
