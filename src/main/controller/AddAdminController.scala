package main.controller

import scalafx.scene.control.{TextField, PasswordField, Alert}
import scalafx.Includes._
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml
import main.util._
import main.MainApp
import main.model.AdministratorAccount
import scalafx.beans.property.StringProperty

@sfxml
class AddAdminController (
  private val regID : TextField,    
  private val regName : TextField,
  private val regPassword : PasswordField,
  private val regConfirmPass : PasswordField

) {
  
  def onRegisterClicked() {
    //Create a new AdministratorAccount object if the information filled in is valid and direct user to Login page
    
     if(isValid()) {
       Database.addAdmin(new AdministratorAccount(new StringProperty(regID.text.value), regPassword.text.value, regName.text))
       Database.outputAdminList
       Dialog.createDialog(Alert.AlertType.Information, "Administrator Account Registration", "Congratulation", "Administrator Account Created Successfully")
       if(MainApp.currentAccount == null) {
         MainApp.showLogin
       }
     }
  }
  
  def onResetClicked = {
    //Reset all the text fields in the form to empty
    
    regID.text.value = ""
    regName.text.value = ""
    regPassword.text.value = ""
    regConfirmPass.text.value = ""
  }
  
  
  def isValid() : Boolean = {
    //Check whether the text field are filled in correctly. If there is any error, a dialog will created.
    
    var errorString : String = ""
    
    if (Validation.checkInput(regID.text.value)) {
      errorString += "Please insert a user ID\n"
    } else if (Database.checkAdminIDUsed(regID.text.value)) {
      errorString += "This Admin ID is used by other admin\n"
    }
      
      
    if (Validation.checkInput(regName.text.value)) {
      errorString += "Please insert a name\n"
    }
      
      
    if (Validation.checkInput(regPassword.text.value)) {
      errorString += "Please insert a password\n"
    }
      
    if (Validation.checkInput(regConfirmPass.text.value)) {
      errorString += "Please confirm your password\n"
    }
      
    if(!Validation.checkInput(regPassword.text.value) && !Validation.checkInput(regConfirmPass.text.value)) {
      if(regPassword.text.value != regConfirmPass.text.value) {
        errorString += "Your passwords do not match\n"
      }
    }
      
    if(Validation.checkInput(errorString)) {
      true
    } else {
      Dialog.createDialog(Alert.AlertType.Error, "Administrator Account Registration", "Please correct the error(s) below", errorString)
      false
    }
  }
}