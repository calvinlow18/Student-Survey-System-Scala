package main.controller

import scalafxml.core.macros.sfxml
import main.MainApp
import scalafx.scene.control.{Button, Label}
import scalafx.Includes._

@sfxml
class RootLayoutController(val logOutBtn : Button, var welcome : Label) {
  
  //When log out button is clicked, remove current logged in account from MainApp and send user back to login interface
  def onLogOut = {
    MainApp.showLogin
    MainApp.currentAccount = null
    logOutBtn.visible = false
    welcome.text = "User Login"
  }
  
  //Assign log out button and welcome text label reference to MainApp for easy reference
  MainApp.logOut = logOutBtn
  MainApp.welcomeText = welcome
}