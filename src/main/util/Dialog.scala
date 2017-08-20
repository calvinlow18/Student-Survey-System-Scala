package main.util

import scalafx.scene.control.Alert
import scalafx.stage.Stage

object Dialog {
  def createDialog(alertType : Alert.AlertType, t : String, h : String, c : String) {
    //Create dialog according to the information provided in the parameter
    
    val dialogStage : Stage = null
    
    val alert = new Alert(alertType){
        initOwner(dialogStage)
        title = t
        headerText = h
        contentText = c
    }.showAndWait()
  }
}