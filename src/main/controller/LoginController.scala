/**
  * Created by Calvin Low on 5/15/2016.
  */
package main.controller

import scalafx.scene.control.{PasswordField, TextField, Alert, RadioButton, ToggleGroup, Toggle}
import scalafx.scene.image.{ImageView, Image}
import scalafxml.core.macros.sfxml
import scalafx.stage.Stage
import javafx.scene.control.TabPane
import scalafxml.core.{NoDependencyResolver, FXMLLoader}
import main.model.{Account, AdministratorAccount}
import main.MainApp
import scala.collection.JavaConversions._
import java.io.File
import main.util._

@sfxml
class LoginController (
  private val id : TextField,
  private val password : PasswordField,
  private val accType : ToggleGroup,
  private val stuRadio : RadioButton,
  private val adminRadio : RadioButton,
  private val roleImage : ImageView
) {
  
  var dialogStage : Stage  = null
  
  MainApp.welcomeText.text = "User Login"
  
  //Role that user chose to log in
  var role : String  = "student"
  
  //Set up the image in the interface
  roleImage.image = new Image(getClass.getResource("../images/1467487828_graduated.png").toString())
    
  def adminSelected = {
    //Set up the administrator image and assign role as "administrator" if administrator radio button is selected
    role = "administrator"
    roleImage.image = new Image(getClass.getResource("../images/1467487832_administrator.png").toString())
    
  }
  
  def stuSelected = {
    //Set up the student image and assign role as "student" if student radio button is selected
    role = "student"
    roleImage.image = new Image(getClass.getResource("../images/1467487828_graduated.png").toString())
    
  }
  
  def onLoginBtnClicked() {
    //Try to login if all the information entered are valid
    
    if(isValid) {
      
      //Check whether the account available. If available, show the menu of account selected
      if(Database.checkAvailability(role, new Account(id.text, password.text.value, null))){
        if(MainApp.currentAccount.isInstanceOf[AdministratorAccount]) {
          showAdministratorMenu
        } else {
          showStudentMenu
        }
        
        //CHange the welcome text of the interface to account name and id
        MainApp.welcomeText.text = MainApp.currentAccount.name.value + " - " + MainApp.currentAccount.id.value
        
        //Make the log out button visible
        MainApp.logOut.visible = true
      } else {
        //If the account does not exist, change the header according to role and throw an error
        
        var header : String = "Administrator Login"
        
        if(role.equals("student")) {
          header = "Student Login"
        }
        
        Dialog.createDialog(Alert.AlertType.Error, "Student Survey System", header, "Login Failed")
      }
    }
   
  }
  
  def showStudentMenu = {
    //Load StudentScreen.fxml to the rootlayout center
    
    val resource = getClass.getResource("../view/StudentScreen.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[TabPane]
    MainApp.roots.setCenter(roots)
  }
  
  def showAdministratorMenu = {
    //Load AdministratorScreen.fxml to the rootlayout center
    
    val resource = getClass.getResource("../view/AdministratorScreen.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[TabPane]
    MainApp.roots.setCenter(roots)
  }
  
  def isValid : Boolean = {
    //Check whether the fields input is valid and not empty, if there is errors, an error dialog will be thrown
    
    var error : String = ""
    
    if(Validation.checkInput(id.text.value))
      error += "Please enter your username\n"
    
    if(Validation.checkInput(password.text.value))
      error += "Please enter your password\n"
     
    if(Validation.checkInput(error)) {
      true
    } else {
      Dialog.createDialog(Alert.AlertType.Error, "Login Fail", "Please corect the error below", error)
      false
    }
    
  }


}
