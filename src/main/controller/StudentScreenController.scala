package main.controller

import scalafxml.core.macros.sfxml
import scalafx.scene.control.{Label, ToggleGroup, Toggle, Alert, Tab, TextField}
import scalafx.scene.layout.AnchorPane
import scalafx.Includes._
import scalafx.stage.Stage
import main.MainApp
import main.model.{Survey, StudentAccount}
import main.util._
import scala.collection.JavaConversions._
import scalafxml.core.{NoDependencyResolver, FXMLLoader}
import scalafx.stage.{ Stage, Modality }
import javafx.{scene => jfxs}
import scalafx.scene.Scene

@sfxml
class StudentScreenController(
    private val q1group : ToggleGroup,
    private val q2group : ToggleGroup,
    private val q3group : ToggleGroup,
    private val q4group : ToggleGroup,
    private val q5group : ToggleGroup,
    private val q6group : ToggleGroup,
    private val q7group : ToggleGroup,
    private val q8group : ToggleGroup,
    private val q9group : ToggleGroup,
    private val q10group : ToggleGroup,
    private val q1 : Label,
    private val q2 : Label,
    private val q3 : Label,
    private val q4 : Label,
    private val q5 : Label,
    private val q6 : Label,
    private val q7 : Label,
    private val q8 : Label,
    private val q9 : Label,
    private val q10 : Label,
    private val surveyTab : Tab,
    private val studentID : Label,
    private val studentName : Label,
    private val studentEmail : Label,
    private val studentIntake : Label,
    private val studentSemester : Label,
    private var chgPassTab : AnchorPane
) {
  
  //Assign the downcast of currentAccount to studentAcc
  private val studentAcc : StudentAccount = MainApp.currentAccount.asInstanceOf[StudentAccount]
  
  setUpQuestion
  
  fillUpStudentInfo
  
  setChgPassTab
  
  //Check whether survey in studentAcc exist, if not null, means survey done and surveyTab will be disabled
  if(studentAcc.survey != null)
    surveyTab.disable = true
  
  def setChgPassTab {
    //Load the ChangePassword.fxml and add anchor pane of ChangePassword.fxml to chgPassTab's children
    
    val resource = getClass.getResource("../view/ChangePassword.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val root = loader.getRoot[jfxs.layout.AnchorPane]
    chgPassTab.children += root
  }
  
  def fillUpStudentInfo = {
    //Fill up all the student information in their respective labels
    
    studentID.text.value = studentAcc.id.value
    studentName.text.value = studentAcc.name.value
    studentEmail.text.value = studentAcc.email.value
    studentIntake.text.value = studentAcc.intake.value
    studentSemester.text.value = String.valueOf(studentAcc.semester.value)
  }

  def onSubmit {
    //Id all the questions are answered, add the survey to Database's surveyList
    
    if(checkRadio) {
      
      val newSurvey = new Survey(interpretAnswer, MainApp.currentAccount.id.value)
      studentAcc.survey = newSurvey
      Database.addSurvey(newSurvey)
      
      //Output the latest surveyList
      Database.outputSurveyList
      
      //CReate a dialog notify user that survey is submitted
      Dialog.createDialog(Alert.AlertType.Information, "Completed Survey", "Congratulation", "Your Survey is Submitted")
      
      //Disabled the survey tab
      surveyTab.disable = true
    }
  }
  
  def onEdit {
    //Check whether OK is clicked. If OK is clicked, fill up all the student's information label with latest information
    
    val okClicked : Boolean = showEditDialog
    if(okClicked) {
      fillUpStudentInfo
    }
  }
  
  def showEditDialog : Boolean = {
    //Load the EditStudent.fxml to a dialog that pop out and downcast and passed the currentAccount in MainApp to studentAcc in the EditStudentController
    
    val resource = getClass.getResource("../view/EditStudent.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[EditStudentController#Controller]

    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(MainApp.stage)
      title = "Edit Information"
      scene = new Scene {
        root = roots2
      }
    }
    control.dialogStage = dialog
    control.studentAcc = MainApp.currentAccount.asInstanceOf[StudentAccount]
    dialog.showAndWait()
    control.okClickedCheck
  }
  
  def interpretAnswer : Array[Int] = {
    /*Get the answer of the survey by parsing userData of the toggle into Integer ad store it into an array. The array will be passed out 
    	as this survey's answer*/
    
    val tempAns : Array[Int] = new Array[Int](10)

    tempAns(0) = Integer.parseInt(q1group.getSelectedToggle.userData.toString())
    tempAns(1) = Integer.parseInt(q2group.getSelectedToggle.userData.toString())
    tempAns(2) = Integer.parseInt(q3group.getSelectedToggle.userData.toString())
    tempAns(3) = Integer.parseInt(q4group.getSelectedToggle.userData.toString())
    tempAns(4) = Integer.parseInt(q5group.getSelectedToggle.userData.toString())
    tempAns(5) = Integer.parseInt(q6group.getSelectedToggle.userData.toString())
    tempAns(6) = Integer.parseInt(q7group.getSelectedToggle.userData.toString())
    tempAns(7) = Integer.parseInt(q8group.getSelectedToggle.userData.toString())
    tempAns(8) = Integer.parseInt(q9group.getSelectedToggle.userData.toString())
    tempAns(9) = Integer.parseInt(q10group.getSelectedToggle.userData.toString())
    
    tempAns
  }
  
  def onRefresh = {
    //Refresh the survey to remove all the previously checked Radio Buttons
    
    if(!Validation.noSelectedCheck(q1group.getSelectedToggle)) {
      q1group.getSelectedToggle.selected = false
    }
    if(!Validation.noSelectedCheck(q2group.getSelectedToggle)) {
      q2group.getSelectedToggle.selected = false
    }
    if(!Validation.noSelectedCheck(q3group.getSelectedToggle)) {
      q3group.getSelectedToggle.selected = false
    }
    if(!Validation.noSelectedCheck(q4group.getSelectedToggle)) {
      q4group.getSelectedToggle.selected = false
    }
    if(!Validation.noSelectedCheck(q5group.getSelectedToggle)) {
      q5group.getSelectedToggle.selected = false
    }
    if(!Validation.noSelectedCheck(q6group.getSelectedToggle)) {
      q6group.getSelectedToggle.selected = false
    }
    if(!Validation.noSelectedCheck(q7group.getSelectedToggle)) {
      q7group.getSelectedToggle.selected = false
    }
    if(!Validation.noSelectedCheck(q8group.getSelectedToggle)) {
      q8group.getSelectedToggle.selected = false
    }
    if(!Validation.noSelectedCheck(q9group.getSelectedToggle)) {
      q9group.getSelectedToggle.selected = false
    }
    if(!Validation.noSelectedCheck(q10group.getSelectedToggle)) {
      q10group.getSelectedToggle.selected = false
    }
    
  }
  
  def checkRadio : Boolean = {
    //Check for each question whether one radio button is selected.
    
    var error : String = ""
    
    if(Validation.noSelectedCheck(q1group.getSelectedToggle)) {
      error += "Please select one option in Question 1\n"
    }
    if(Validation.noSelectedCheck(q2group.getSelectedToggle)) {
      error += "Please select one option in Question 2\n"
    }
    if(Validation.noSelectedCheck(q3group.getSelectedToggle)) {
      error += "Please select one option in Question 3\n"
    }
    if(Validation.noSelectedCheck(q4group.getSelectedToggle)) {
      error += "Please select one option in Question 4\n"
    }
    if(Validation.noSelectedCheck(q5group.getSelectedToggle)) {
      error += "Please select one option in Question 5\n"
    }
    if(Validation.noSelectedCheck(q6group.getSelectedToggle)) {
      error += "Please select one option in Question 6\n"
    }
    if(Validation.noSelectedCheck(q7group.getSelectedToggle)) {
      error += "Please select one option in Question 7\n"
    }
    if(Validation.noSelectedCheck(q8group.getSelectedToggle)) {
      error += "Please select one option in Question 8\n"
    }
    if(Validation.noSelectedCheck(q9group.getSelectedToggle)) {
      error += "Please select one option in Question 9\n"
    }
    if(Validation.noSelectedCheck(q10group.getSelectedToggle)) {
      error += "Please select one option in Question 10\n"
    }
    if(!Validation.checkInput(error)) {
      Dialog.createDialog(Alert.AlertType.Error, "Survey", "Please answer all the questions.", error)
      return false
    }
    return true
  }
  
  def setUpQuestion = {
    //Set up all the questions in the survey according to the questions in quesList in Database
    
    q1.text = " 1. " + Database.getQuestion(0).description
    q2.text = " 2. " + Database.getQuestion(1).description
    q3.text = " 3. " + Database.getQuestion(2).description
    q4.text = " 4. " + Database.getQuestion(3).description
    q5.text = " 5. " + Database.getQuestion(4).description
    q6.text = " 6. " + Database.getQuestion(5).description
    q7.text = " 7. " + Database.getQuestion(6).description
    q8.text = " 8. " + Database.getQuestion(7).description
    q9.text = " 9. " + Database.getQuestion(8).description
    q10.text = "10. " + Database.getQuestion(9).description
  }
  
}