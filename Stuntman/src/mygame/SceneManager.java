package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Bob
 */
public class SceneManager extends AbstractAppState {
    
  public    SimpleApplication      app;
  public    AppStateManager        stateManager;
  public    Node                   sceneModel;
  public    Node                   rootNode;
  public    AssetManager           assetManager;
  public    Material               terMat;
  private   boolean                lastWall;
  public    Node                   wallNode;
  public    int                    difficulty;
  private   ArrayList<TextureKey>  textArray;
  private   int                    neg;
  private   int                    negDelay;
    
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    System.out.println("Manager Attached");
    super.initialize(stateManager, app);
    this.app           = (SimpleApplication) app;
    this.rootNode      = this.app.getRootNode();
    this.stateManager  = this.app.getStateManager();
    this.assetManager  = this.app.getAssetManager();
    lastWall = true;
    difficulty = 30;
    wallNode = new Node();
    rootNode.attachChild(wallNode);
    initMaterials();
    createFloor();
    negDelay = 0;
    neg = 1;
    }
  
  //Creating a list of textures for the buildings
  public void initMaterials(){
    textArray = new ArrayList();
    TextureKey key1 = new TextureKey("Textures/Buildings/CasaRosa.jpg");
    textArray.add(key1);
    TextureKey key2 = new TextureKey("Textures/Buildings/Yellowhouse.jpg");
    textArray.add(key2);
    TextureKey key3 = new TextureKey("Textures/Buildings/casaamarela.jpg");
    textArray.add(key3);
    TextureKey key4 = new TextureKey("Textures/Buildings/whitehouselittle.jpg");
    textArray.add(key4);
    }
  
  //Creates the Floor
  public void createFloor(){
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.DarkGray);
    Box box = new Box(150, .2f, 150);
    Geometry floor = new Geometry("the Floor", box);
    floor.setMaterial(mat);
    floor.setLocalTranslation(0, -1, -1);
    sceneModel = new Node();
    sceneModel.attachChild(floor);
    rootNode.attachChild(sceneModel);
    }
  
  //Create the wall on the right side
  public void createRightWall(){
      
    //Generate the materials and random numbers
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    Random texRand = new Random();
    int texSel = texRand.nextInt(4);
    
    //choose and add a random texture
    TextureKey key = textArray.get(texSel);
    key.setGenerateMips(true);
    Texture tex = assetManager.loadTexture(key);
    mat.setTexture("ColorMap", tex);
    
    //Create the Geometry/
    Box a = new Box(5f, 5f, 50f);
    Geometry wall = new Geometry("Right Wall", a);
    wall.setMaterial(mat);
    wallNode.attachChild(wall);
    
    //Place the Geometry in a hallway fashion
    Random rand = new Random();
    float distance = rand.nextInt(7) + 9; 
    wall.setLocalTranslation(-distance, 0f, -101f);
    }
  
  
  //Create the wall on the left side
  public void createLeftWall(){
    
    //generate the materials and random numbers
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    Random texRand = new Random();
    int texSel = texRand.nextInt(4);
    
    //choose and add a random texture
    TextureKey key = textArray.get(texSel);
    key.setGenerateMips(true);
    Texture tex = assetManager.loadTexture(key);
    mat.setTexture("ColorMap", tex);
    
    //Create the Geometry
    Box a = new Box(5f, 5f, 50f);
    Geometry wall = new Geometry("Left Wall", a);
    wall.setMaterial(mat);
    wallNode.attachChild(wall);
    
    //Place the Geometry in a hallway fashion
    Random rand = new Random();
    float distance = rand.nextInt(7) + 9; 
    wall.setLocalTranslation(distance, 0f, -101f);
    }
  
  @Override
  public void update(float tpf){
      
  //Check if the player is dead.
  Player player = stateManager.getState(PlayerManager.class).currentPlayer;
  if (!player.isDead){
    
    //Fixes the hallways when there is nothing there. Fixes the Bug, also used to start  
    if(wallNode.getChildren().isEmpty()){
      System.out.println("Bug Fixed!");
      createLeftWall();
      createRightWall();
      }
      
    //Getting the walls and moving them past the car, simulating movement
      
    for(int i = 0; i < wallNode.getChildren().size(); i++){
      Geometry geom = (Geometry) wallNode.getChild(i);
      Random rand = new Random();
      
      //Sets the amplitude of the shakes
      int shaker = rand.nextInt(5);
      
      //Keeps the shake reasonable
      int shakeShift = difficulty/30;
      if(shakeShift < 1)
      shakeShift = 1;
      
      //Determines the direction of the shake
      if (negDelay == 2000){
        neg = rand.nextInt(2) - 1;
        if (neg == 0)
        neg = 1;
        negDelay = 0;
        } else{
        negDelay++;
        }
      
      //Actually Does the moving of the geometry
      float shake = shakeShift * shaker * neg;
      Vector3f moveDir = new Vector3f(shake, 0f, difficulty);
      geom.setLocalTranslation(geom.getLocalTranslation().addLocal(moveDir.mult(tpf)));
      int distance = (int) geom.getLocalTranslation().distance(player.model.getLocalTranslation().add(0f, 0f, -50f));
      
      
      //If the distance is exactly 50 and there is less than for walls, create two more walls.
      if (distance == 53 && wallNode.getChildren().size() < 4) {
        
        //creates the right wall, which is the second wall, sets boolean to false, to assure walls generate in pairs
        if (lastWall){
          createRightWall();
          player.score++;
          if (difficulty < 150)
          difficulty = difficulty + 5;
          System.out.println(player.score);
          lastWall = false;
          }
        
        //Creates the left wall, sets boolean to true, allowing the right wall to be created
        if (!lastWall){
          createLeftWall();
          lastWall = true;
          }
        }

      //If greater than 120 remove the wall, allowing for new walls to be created
      if(distance > 120){
        geom.removeFromParent();
        }
      
      }
    }
  }
}
