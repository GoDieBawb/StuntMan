package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;

/**
 *
 * @author Bob
 */
public class PlayerManager extends AbstractAppState {
    
   public  SimpleApplication      app;
   public  AppStateManager        stateManager;
   public  AssetManager           assetManager;
   public  Node                   rootNode;
   public  Player                 currentPlayer;
    
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    
    this.app          = (SimpleApplication) app;
    this.rootNode     = this.app.getRootNode();
    this.assetManager = this.app.getAssetManager();
    this.stateManager = this.app.getStateManager();
    makeNewPlayer();
    System.out.println("PlayerManager Attached");
    }
  
  //Make the player
  public void makeNewPlayer(){
    Player player = new Player();
    player.model = new Node();
    player.isDead = false;
    player.score  = 0;
    Node ninja = (Node) assetManager.loadModel("Models/Ferrari/Car.scene");
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key = new TextureKey("Models/Ferrari/Car.jpg", false);
    Texture tex = assetManager.loadTexture(key);
    mat.setTexture("ColorMap", tex);
    ninja.setMaterial(mat);
    ninja.setLocalScale(.5f);
    player.model.attachChild(ninja);
    currentPlayer = player;
    rootNode.attachChild(player.model);
    player.model.setLocalTranslation(0, -.5f, 0);
    }
  
  
  @Override
  public void update(float tpf){
    }
}