package main.controller

import scalafxml.core.macros.sfxml
import scalafx.scene.control.{PasswordField, Alert}
import main.util._
import main.MainApp
import scalafx.stage.Stage

@sfxml
class ChangePasswordController(
    private val chgOldPass : PasswordField,
    private val chgNewPass : PasswordField,
    private val chgRepeatPass : PasswordField

) {
  
  def onChgPass = {
    //Change password of account, output the latest adminList and create dialog to notify user success in changing password if all the fields are valid
    
    if(isValid) {
      MainApp.currentAccount.password = chgNewPass.text.value
      Database.outputAdminList
      Dialog.createDialog(Alert.AlertType.Information, "Change Password", "Congratulation", "Password changed successfully")
    }
  }  

  def isValid : Boolean = {
    /*Check whether all the fields are filled up and whether they are filled up with correct information. If no error detected, true will be returned.
    	If error exists, a dialog will be created and signify the error exists*/
    
    var error : String = ""
    
    if(Validation.checkInput(chgOldPass.text.value)) {
      error += "Please enter your old password\n"
    }
    if(Validation.checkInput(chgNewPass.text.value)) {
      error += "Please enter your new password\n"
    }
    if(Validation.checkInput(chgRepeatPass.text.value)) {
      error += "Please repeat your password\n"
    }
    if(!Validation.checkInput(chgRepeatPass.text.value) && !Validation.checkInput(chgNewPass.text.value)) {
      if(!chgNewPass.text.value.equals(chgRepeatPass.text.value)) {
        error += "Your repeated password is wrong\n"
      } else {
        if(!chgOldPass.text.value.equals(MainApp.currentAccount.password)) {
          error += "Your old password is wrong\n"
        }
      }
    }
    if(Validation.checkInput(error)) {
      true
    } else{
      Dialog.createDialog(Alert.AlertType.Error, "Change PPassword", "Please correct the error(s) below.", error)
      false
    }
  }
}