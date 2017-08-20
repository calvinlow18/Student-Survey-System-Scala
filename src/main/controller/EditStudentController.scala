package main.controller

import scalafxml.core.macros.sfxml
import scalafx.scene.control.{TextField, ChoiceBox, Spinner, Alert}
import scalafx.stage.Stage
import scalafx.collections.ObservableBuffer
import main.MainApp
import main.model.StudentAccount
import main.util._
import scalafx.beans.property.{StringProperty, IntegerProperty}

@sfxml
class EditStudentController (
    private val stuID : TextField,
    private val stuName : TextField,
    private val stuEmail : TextField,
    private val stuIntake : ChoiceBox[String],
    private val stuSemester : Spinner[Int]
) {
  
  var dialogStage : Stage = null
  
  private var _studentAcc : StudentAccount = null
  
  //Boolean that shows whether OK is clicked
  var okClickedCheck : Boolean = false
  
  //Initialize the items of stuIntake choice box
  stuIntake.items = ObservableBuffer("January", "February", "March")

  //Setter of _studentAcc fill up the information of students
  def studentAcc_=(sA : StudentAccount) = {
    _studentAcc = sA
    fillTextField
  }
  
  //Getter for _studentAcc
  def studentAcc = _studentAcc

  //Fill up all the text fields, Choice Box and Spinner with student information
  def fillTextField = {
    stuID.text = studentAcc.id.value
    stuName.text = studentAcc.name.value
    stuEmail.text = studentAcc.email.value
    stuIntake.getSelectionModel().select(studentAcc.intake.value)
    stuSemester.getValueFactory.setValue(studentAcc.semester.value)
  }
  
  def okClicked = {
    
    /*If OK is clicked and all information student input is valid, update the StudentAccount object and the welcome text on top of screen, output the
    	latest studentList and assign true to okClickedCheck*/
    if(isValid) {
      studentAcc.id <== stuID.text
      studentAcc.name <== stuName.text
      studentAcc.email <== stuEmail.text
      studentAcc.intake <== new StringProperty(stuIntake.getValue)
      studentAcc.semester.value = stuSemester.getValue
      MainApp.welcomeText.text = studentAcc.name.value + " - " + studentAcc.id.value
      Database.outputStudentList
      dialogStage.close
      okClickedCheck = true
    }
    
  }
  
  def isValid : Boolean = {
    /*Check whether all the fields are filled up and whether they are filled up with correct information. If no error detected, true will be returned.
    	If error exists, a dialog will be created and signify the error exists*/
    
    var error : String = ""
    
    if(Validation.checkInput(stuID.text.value)) {
      error += "Please enter the student ID\n"
    }
    
    if(Validation.checkInput(stuName.text.value)) {
      error += "Please enter the student name\n"
    }
    
    if(Validation.checkInput(stuEmail.text.value)) {
      error += "Please enter student email\n"
    } else if (!Validation.isEmailValid(stuEmail.text.value)) {
      error += "Please enter a valid email\n"
    }
    
    if(!Validation.checkInput(error)){
      Dialog.createDialog(Alert.AlertType.Error, "Edit Student Information", "Please correct the error(s) below.", error)
        return false
    }
    
    return true
  }
  
  def resetClicked = {
    //Reset all the information in text fields, choice box and spinner to student's previous information
    
    fillTextField
    
  }
  
}