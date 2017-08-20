package main.controller

import scalafxml.core.macros.sfxml
import scalafx.scene.control.{TextField, ChoiceBox, Spinner, Alert, PasswordField}
import scalafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory
import scalafx.collections.ObservableBuffer
import scalafx.Includes._
import scalafx.stage.Stage
import main.util._
import main.model.StudentAccount
import scalafx.beans.property.{StringProperty, IntegerProperty}


@sfxml
class AddStudentController(
    private val stuID : TextField,
    private val stuPass : PasswordField,
    private val stuName : TextField,
    private val stuEmail : TextField,
    private val stuIntake : ChoiceBox[String],
    private val stuSem : Spinner[Int]
  ) {
  
  //Initialize stuIntake ChiceBox with the ObservableBuffer that contains 3 String element
  stuIntake.items = ObservableBuffer("January", "February", "March") 
  stuIntake.getSelectionModel().selectFirst()
  
  def okClicked = {
    //When OK is clicked, if the information provided is valid, a new STudentAccount will be created and added to studentList
    
    if(isValid) {
      val stuAcc = new StudentAccount(new StringProperty(stuID.text.value), stuPass.text.value, new StringProperty(stuName.text.value), new StringProperty(stuEmail.text.value), new StringProperty(stuIntake.getValue), IntegerProperty(stuSem.getValue))
      Database.addStudent(stuAcc)
    }
  }
  
  def resetClicked = {
    //When reset is clicked, initialize all the text field, choice box and spinner to default value
    
    stuID.text.value = ""
    stuPass.text.value = ""
    stuName.text.value = ""
    stuEmail.text.value = ""
    stuIntake.getSelectionModel().selectFirst()
    stuSem.getValueFactory.setValue(1)
  }
  
  def isValid : Boolean = {
    //Check whether all the components are filled in with appropriate values. If there are any error, an error dialog ill be created
    
    var error : String = ""
    if(Validation.checkInput(stuID.text.value)) {
      error += "Please enter the student ID\n"
    } else if(Database.checkStudentIDUsed(stuID.text.value)) {
      error += "This student ID is used by another student\n"
    }
    
    if(Validation.checkInput(stuPass.text.value)) {
      error += "Please enter the student password\n"
    }
    
    if(Validation.checkInput(stuName.text.value)) {
      error += "Please enter the student name\n"
    }
    
    if(Validation.checkInput(stuEmail.text.value)) {
      error += "Please enter student email\n"
    } else if(!Validation.isEmailValid(stuEmail.text.value)) {
      error += "Please enter valid student email\n"
    }
    
    if(!Validation.checkInput(error)){
      Dialog.createDialog(Alert.AlertType.Error, "Add Student", "Please correct error(s) below", error)
        return false
    }
    
    return true
  }
  
}