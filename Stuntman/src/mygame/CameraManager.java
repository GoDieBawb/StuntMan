/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.ChaseCamera;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 *
 * @author Bob
 */
public class CameraManager extends AbstractAppState {
    
  public    Player                 player;
  public    SimpleApplication      app;
  public    AppStateManager        stateManager;
  public    Camera                 cam;
  public    InputManager           inputManager;

  public    FlyByCamera            flyCam;
  public    ChaseCamera            chaseCam;
  public    boolean               chase;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
    this.app           = (SimpleApplication) app;
    this.stateManager  = this.app.getStateManager();
    this.cam           = this.app.getCamera();
    this.inputManager  = this.app.getInputManager();
    this.flyCam        = this.stateManager.getState(FlyCamAppState.class).getCamera();
    this.chase         = this.stateManager.getState(GUIManager.class).chase;
    this.player        = this.stateManager.getState(PlayerManager.class).currentPlayer;
    flyCam.setEnabled(false);
    flyCam.setDragToRotate(false);
    if (chase)
    setUpFollowCamera();
    else
    setUpTopCamera();
    }
    
    //Arcade mode camera
  
  public void setUpTopCamera(){
    chaseCam = new ChaseCamera(cam, player.model, inputManager);
    chaseCam.setMinDistance(35);
    chaseCam.setMaxDistance(35);
    chaseCam.setDefaultHorizontalRotation(0);
    chaseCam.setDefaultVerticalRotation(-5);
    chaseCam.setUpVector(new Vector3f(0f, 1f, 0f));
    chaseCam.setDragToRotate(false);
    }
  
  //Chase Mode Camera
  
  public void setUpFollowCamera(){
    System.out.println(cam + " " + player + " " + inputManager);
    chaseCam = new ChaseCamera(cam, player.model, inputManager);
    chaseCam.setMinDistance(5);
    chaseCam.setMaxDistance(5);
    chaseCam.setLookAtOffset(new Vector3f(0f, 1f, 0));
    chaseCam.setDefaultHorizontalRotation(1.6f);
    chaseCam.setDefaultVerticalRotation(0);
    chaseCam.setUpVector(new Vector3f(0f, 1f, 0f));
    chaseCam.setDragToRotate(false);
    }
}
