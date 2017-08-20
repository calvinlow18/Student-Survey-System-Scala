package main.controller

import scalafx.scene.control.{TableView, TableColumn, Label, Tab, TextField, ComboBox, Alert}
import scalafx.scene.layout.AnchorPane
import javafx.scene.control.Tooltip
import scalafx.scene.chart.PieChart
import javafx.scene.chart.PieChart.Data
import main.util._
import main.MainApp
import main.model.{AdministratorAccount, StudentAccount, Question}
import scalafxml.core.macros.sfxml
import scalafx.collections.ObservableBuffer
import scala.collection.JavaConversions._
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, NoDependencyResolver, FXMLView}
import javafx.{scene => jfxs}
import scalafx.scene.Scene
import scalafx.stage.{ Stage, Modality }
import scalafx.event.ActionEvent
import scalafx.scene.input.MouseEvent
import java.text.DecimalFormat
import scalafx.beans.property.StringProperty

@sfxml
class AdminScreenController(
    private val stuTable : TableView[StudentAccount],
    private val stuIdClm : TableColumn[StudentAccount, String],
    private val stuNameClm : TableColumn[StudentAccount, String],
    private val stuDetailID : Label,
    private val stuName : Label,
    private val stuEmail : Label,
    private val stuIntake : Label,
    private val stuSem : Label,
    private var addAdminTab : AnchorPane,
    private var chgPassTab : AnchorPane,
    private val comboBoxOldQues : ComboBox[String],
    private val newQues : TextField,
    private val analysisQues : ComboBox[String],
    private val pieChartAnalysis : PieChart
  ) {

  setChgPassTab
  
  setAddAdminTab
  
  //Set up conboBoxOldQues combo box with questions' description and index
  comboBoxOldQues.setItems(quesString)
  
  //Set up analysisQues combo box with questions' description and index
  analysisQues.setItems(quesString)
  
  //Set up stuTable Table with studentList in Database
  stuTable.items = Database.getStudentList
  
  //Initialize the value of column
  stuIdClm.cellValueFactory = {_.value.id} 
  stuNameClm.cellValueFactory = {_.value.name}
  
  
  showStudentDetails(None);
  
  //Select the first element in analysisQues combo box
  analysisQues.getSelectionModel.select(0)
  
  //Show the pie chart for the first question
  showPieChart(0)
  
  //When there are any changes in selected student in table, show the new student details
  stuTable.selectionModel().selectedItem.onChange(
      (_, _, newStudent) => showStudentDetails(Some(newStudent))
  )
  
  /*When there are any changes in the element of comboBoxOldQyes, if the selected element is valid, insert the old question description into newQues 
  	text field*/ 
  comboBoxOldQues.selectionModel().selectedItem.onChange {
    if(comboBoxOldQues.getSelectionModel().getSelectedIndex() != -1) {
      newQues.text.value = Database.getQuestion(comboBoxOldQues.getSelectionModel().getSelectedIndex()).description
    }
  }
  
  /*When there are any changes in the element of analysisQues, if the selected element is valid, show the pie chart for the selected question*/
  analysisQues.selectionModel().selectedItem.onChange(
      if(analysisQues.getSelectionModel().getSelectedIndex() != -1) {
        showPieChart(analysisQues.getSelectionModel().getSelectedIndex())
      }
  )
  
  def setChgPassTab {
    //Load the ChangePassword.fxml and add anchor pane of ChangePassword.fxml to chgPassTab's children
    
    val resource = getClass.getResource("../view/ChangePassword.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val root = loader.getRoot[jfxs.layout.AnchorPane]
    chgPassTab.children += root
  }
  
  def setAddAdminTab {
    //Load the AddAdministrator.fxml and add anchor pane of AddAdministrator.fxml to chgPassTab's children
    
    val resource = getClass.getResource("../view/AddAdministrator.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val root = loader.getRoot[jfxs.layout.AnchorPane]
    addAdminTab.children += root
  }
  
  private def showPieChart (selected : Int) = {
    //Show pie chart of the selected question element
    
    Database.analysis(selected)
    val option : Array[String] = Array("Mostly Disagree", "Disagree", "Neutral", "Agree", "Mostly Agree")
    var dataOB : ObservableBuffer[Data] = new ObservableBuffer()
    
    //Add PieChart.Datainto dataOBif there are student that choses that rank for the question
    for(i<-0 until 5) {
      if(Database.getQuestion(selected).count(i) != 0) {
        dataOB += PieChart.Data(option(i), Database.getQuestion(selected).count(i))
      }
    }

    //DescimalFormat object to convert integer to 2d.p. decimal
    val decimalFormat : DecimalFormat = new DecimalFormat("#.00") 
    
    //Add dataOB ObservableBuffer to the data of pie chart
    pieChartAnalysis.data = dataOB
    
    //Create and install tooltip for each data node in piechart to show the statistics
    dataOB.foreach{ data =>
      data.node().onMouseEntered = ( e : MouseEvent) => {
        val tooltip : Tooltip = new Tooltip()
        tooltip.text = data.getPieValue.toInt + "/" + Database.getSurvey.size() + "(" + decimalFormat.format(data.getPieValue/Database.getSurvey.length*100) + "%)"
        Tooltip.install(data.node(), tooltip)
      }
    }
    
    //CHange the title of pieChartAnalysis to the desciption of the question that is being analysed
    pieChartAnalysis.title = Database.getQuestion(selected).description
  }
  
  private def showStudentDetails (studentAcc : Option[StudentAccount]) = {
    studentAcc match {
      case Some(studentAcc) =>
        // Fill the labels with info from the studentAcc object.
        stuDetailID.text <== studentAcc.id
        stuName.text <== studentAcc.name
        stuEmail.text <== studentAcc.email
        stuIntake.text <== studentAcc.intake
        stuSem.text = studentAcc.semester.value.toString
      case None =>
        // studentAcc is null, remove all the text.
        stuDetailID.text = ""
        stuName.text = ""
        stuEmail.text = ""
        stuIntake.text = ""
        stuSem.text = ""
    }    
  }
  
  def quesString : ObservableBuffer[String] = {
    //Convert question description to ObservableBuffer
    
    var tempOB = new ObservableBuffer[String]()
    var no : Int = 1
    
    //Assign index for each question description when forming a new OnservableBuffer
    Database.getQuesList.foreach { question =>
        tempOB += no + ". " + question.description
        no += 1
    }
    tempOB
  }

  
  def addStudent = {
    //Load AddStudent.fxml to pop out a dialog that allows administrator add student account
    
    val resource = getClass.getResource("../view/AddStudent.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots2  = loader.getRoot[jfxs.Parent]
    
    val dialog = new Stage() {
      initModality(Modality.APPLICATION_MODAL)
      initOwner(MainApp.stage)
      title = "Adding New Student"
      scene = new Scene {
        root = roots2
      }
    }
    dialog.showAndWait()
  }
  
  
  def deleteStudent = {
    /*Delete StudentAccount object selected and remove the student id of the student's survey and change to "-". Then, output survey list and student 
    	list to ensure they are stored
    */
    
    val selectedIndex = stuTable.selectionModel().selectedIndex.value
    
    if (selectedIndex != -1) {
      println(stuTable.items()(selectedIndex).survey)
      if(stuTable.items()(selectedIndex).survey != null) {
        stuTable.items()(selectedIndex).survey.student = "-"
      }
      
      stuTable.items().remove(selectedIndex);
    }
    Database.outputSurveyList
    Database.outputStudentList
   } 
  
  def onChgQues {
    val selected = comboBoxOldQues.getSelectionModel().getSelectedIndex()
    if(selected == -1 ) {
      //Create dialog as there is not Question object with index -1 in quesList
      Dialog.createDialog(Alert.AlertType.Error, "Change Question", "Error", "Please choose a question.")
    } else {
      //Change the question and set up all components related to changing in question description
      Database.editQuestion(selected, newQues.text.value)
      comboBoxOldQues.setItems(quesString)
      comboBoxOldQues.getSelectionModel.select(selected)
      analysisQues.setItems(quesString)
      analysisQues.getSelectionModel.select(0)
      Database.outputQuesList
      Dialog.createDialog(Alert.AlertType.Information, "Change Question", "Congratulation", "Question changed successfully.")
    }
  }
  
  def onClearSurveyClicked {
    //Clear all the Survey object in the surveyList and set up all components related to changing in question description
    Database.clearAllSurvey
    comboBoxOldQues.setItems(quesString)
    analysisQues.setItems(quesString)
    newQues.text.value = ""
  }
  
}