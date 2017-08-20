
  /*
  * Created by Calvin Low on 5/29/2016.
  */

package main

import scalafx.application.JFXApp
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafx.stage.WindowEvent
import scalafx.scene.image.Image
import scalafx.scene.control.{Alert, ButtonType, Button, Label}
import main.model.Account
import main.util.Database
import javafx.{scene => jfxs}
import java.io.File

object MainApp extends JFXApp{  
  
  //Account that is current logged in
  var currentAccount : Account = null;
  
  //Log out button
  var logOut : Button = null
  
  //Welcome text on top of the interface
  var welcomeText : Label = null
  
  //Transform path of RootLayout.fxml to URI for resource location
  val rootResource = getClass.getResource("view/RootLayout.fxml")
  
  //Initialize the loader object
  var loader = new FXMLLoader(rootResource, NoDependencyResolver)
  
  //Load root layout from FXML
  loader.load();
  
  //Retrieve the root component BorderPane from the FXML 
  val roots = loader.getRoot[jfxs.layout.BorderPane]
  
  //Initialize the stage
  stage = new PrimaryStage {
    title = "Student Survey System"
    
    icons += new Image(getClass.getResource("images/1467925767_office-05.png").toString())
    
    height = 650
    width = 1014
    centerOnScreen
    resizable = false
    
    //Check whether to exit when exit button clicked
    onCloseRequest = (v : WindowEvent) => {
      if(!closeDialog) {
        //Consume the close command
        v.consume
      }
    }
    scene = new Scene {
      root = roots
    }
  }
  
  //Set log out button to invisible
  logOut.visible = false
  
  /*Check whether there is any AdministratorAccount object in the adminList. If no AdministratorAccount object in adminList, show interface to create a 
    new Administrator, else, show login interface*/
  if(Database.isAdminListEmpty) {
    welcomeText.text = "Register New Administrator"
    showRegisterDialog
  } else {
    welcomeText.text = "User Login"
    showLogin
  }
  
  //Input all the StudentAccount objects, QUestion objects and Survey objects
  Database.inputStudentList
  Database.inputQuestionList
  Database.inputSurveyList
  
  def closeDialog : Boolean = {
    //Create dialog and wait for the output of dialog. If OK button is clicked, return true (quit the system) else return false
    
    val alert = new Alert(Alert.AlertType.Confirmation) {
      initOwner(stage)
      title = "Confirmation Dialog"
      headerText = "Exit Student Survey System"
      contentText = "Are you sure you want to exit?"
    }
    val result = alert.showAndWait()

    result match {
      case Some(ButtonType.OK) => return true
      case _                   => return false
    }
  }
  
  def showLogin {
    //Load LoginScreen.fxml to the rootlayout center
    
    val resource = getClass.getResource("view/LoginScreen.fxml")
    loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }
  
  def showRegisterDialog = {
    //Load AddAdministrator.fxml to the rootlayout center
    
    val resource = getClass.getResource("view/AddAdministrator.fxml")
    loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.Parent]
    this.roots.setCenter(roots)
  }
  
}
