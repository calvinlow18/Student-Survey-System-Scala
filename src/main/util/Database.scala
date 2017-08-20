package main.util

import java.io.{File, PrintWriter}
import java.util.{Iterator, ArrayList}
import scala.io.Source
import scalafx.collections.ObservableBuffer
import scalafx.beans.property.{StringProperty, IntegerProperty}
import scala.collection.JavaConversions._
import java.util.Arrays
import main.model.{AdministratorAccount, StudentAccount, Question, Survey, Account}
import main.MainApp

/**
  * Created by Calvin Low on 5/15/2016.
  */
object Database {
  
  // ObservableBuffer of StudentAccount class that is used to store the input of all the StudentAccount objects when they are inputed
  private var studentList : ObservableBuffer[StudentAccount] = new ObservableBuffer[StudentAccount]()
  
  // ObservableBuffer of AdministratorAccount class that is used to store the input of all the AdministratorAccount objects when they are inputed
  private var adminList : ObservableBuffer[AdministratorAccount] = new ObservableBuffer[AdministratorAccount]()
  
  // Array of Question class that contains 10 questions for the survey
  private var quesList : Array[Question] = new Array[Question](10)
  
  //ArrayList of Survey class which contains all the surveys done
  private var surveyList : ArrayList[Survey] = new ArrayList[Survey]()  // Linked List that is used to store the input of all the Survey Object in the beginning of the application. (Contains all the survey done by students)

  def initQuesList {
    
    //Initialize all the questions according to their index number+1, i.e. question in index 1 is "Question 1", in index 2 is "Question 2" etc...
    for(c <- 0 until quesList.length) {
      quesList(c) = new Question("Question " + (c+1))
    }
    
  }
  
  def outputSurveyList {
    
    
    //Initialize the file location of survey.txt to a variable
    val file : File = new File("survey.txt")

    //Initialize an iterator of surveyList ArrayList
    val surveyIterator : Iterator[Survey] = surveyList iterator()

    //Initialize print writer for the survey.txt
    val writer = new PrintWriter(file)
    
    //Loop through all the Survey object in surveyList Array List and print them into survey.txt file
    while (surveyIterator hasNext)
      writer println surveyIterator.next().toString

    //Close the print writer
    writer close()

  }

  def outputQuesList {

    //Initialize the file location of question.txt to a variable
    val file : File = new File("question.txt")

    //Initialize print writer for the question.txt
    val writer = new PrintWriter(file)
    
    //Loop through quesList array and print the description of all question into question.txt file
    for(i <- 0 until quesList.length) {
      writer println quesList(i).description
    }
    
    //Close the print writer
    writer close()

  }
  
  def outputStudentList {

    //Initialize the file location of student.txt to a variable
    val file : File = new File("student.txt")

    //Initialize an iterator of studentList ObservableBuffer
    val studentIterator = studentList iterator
    
    //Initialize print writer for the student.txt
    val writer = new PrintWriter(file)
    
    //Loop through all the StudentAccount object in studentList ObservableBuffer and print them into survey.txt file
    while (studentIterator hasNext)
      writer println studentIterator.next().toString

    //Close the print writer
    writer close()
    
  }

  def outputAdminList {

    //Initialize the file location of admin.txt to a variable
    val file : File = new File("admin.txt")
    
    //Initialize an iterator of adminList ObservableBuffer
    val adminIterator = adminList.iterator
    
    //Initialize print writer for the admin.txt
    val writer = new PrintWriter(file)
    
    //Loop through all the StudentAccount object in adminList ObservableBuffer and print them into admin.txt file
    while (adminIterator hasNext)
      writer println adminIterator.next().toString
    
    //Close the print writer
    writer close()
    
  }

  def inputStudentList {

    //Initialize the file location of student.txt to a variable
    val studentFile : File = new File("student.txt")

    /*If student.txt does not exist, create it. If it exists already, read line by line from the file. For each line, the information is separated using 
     * ";". Remove it and create StudentAccount object and append it to studentList
     */
    if(!studentFile.exists())
      studentFile createNewFile()
    else
      for ( line <- Source.fromFile(studentFile).getLines()) {
        val tempStudent : Array[String] = line split ";" 
        studentList += new StudentAccount(new StringProperty(tempStudent(0)), tempStudent(1), new StringProperty(tempStudent(2)), new StringProperty(tempStudent(3)), new StringProperty(tempStudent(4)), IntegerProperty(tempStudent(5).toInt))
      }

  }

  def inputAdminList {
    
    //Initialize the file location of admin.txt to a variable
    val adminFile : File = new File("admin.txt")

    /*If admin.txt does not exist, create it. If it exists already, read line by line from the file. For each line, the information is separated using 
     * ";". Remove it and create AdministratorAccount object and append it to adminList
     */
    if(!adminFile.exists())
      adminFile createNewFile()
    else
      for ( line <- Source.fromFile(adminFile).getLines()) {
        val tempAdmin : Array[String] = line split ";"
        adminList += new AdministratorAccount(new StringProperty(tempAdmin(0)), tempAdmin(1), new StringProperty(tempAdmin(2)))
      }

  }

  def inputQuestionList {
    
    //Initialize the file location of question.txt to a variable
    val quesFile : File = new File("question.txt")

    initQuesList
    
    /*If admin.txt does not exist, create it. If it exists already, read line by line from the file. For each line, replace the description of 
     * Question object in quesList
     */
    if(!quesFile.exists()) {
      quesFile createNewFile()
      outputQuesList
    }
    else {
      var count = 0
      for ( line <- Source.fromFile(quesFile).getLines()) { 
        quesList(count).description = line
        count +=1
      }
    }
    
  }

  def inputSurveyList {
    
    //Initialize the file location of survey.txt to a variable
    val surveyFile : File = new File("survey.txt")

    //Clear surveyList to prevent duplication when input
    surveyList clear()

    /*If survey.txt does not exist, create it. If it exists already, read line by line from the file. For each line, separate the survey information by
     * removing ";". Then, parse the string into integer (except the first one, student id) as the survey answer must be in integer. After that create a new Survey object and add it into 
     * surveyList
     */
    if(!surveyFile.exists())
      surveyFile createNewFile()
    else {
      for (line <- Source.fromFile(surveyFile).getLines()) { 
        val tempSurvey : Array[String] = line split(";")
        val parseString : Array[Int] = new Array[Int](tempSurvey.length-1)
        for(counter <- 1 until tempSurvey.length) {
          parseString(counter-1) = Integer.parseInt(tempSurvey(counter))
        }
        surveyList add new Survey(parseString, tempSurvey(0))
      }
    }
    
    //Assign the Survey object in surveyList to its respective StudentAcccount according to id. If no id, ignore the survey
    surveyList.foreach { survey =>
      if(survey.student != "-") {
        getStudent(survey.student).survey = survey
      }
    }
    
  }
  
  def getStudent(id : String) : StudentAccount = {
    
    //Search for student that has id that is passed in (student unique according to id). Return StudentAccount after searching
    studentList.foreach { student =>
      if(student.equals(id)) {
        return student
      }      
    }
    
    return null
    
  }
  
  def analysis(selected : Int) = {
    
    //Initialize the count of Question objects to 0
    Arrays.fill(quesList(selected).count, 0)
    
    //Calculate the responds of students and accumulate it in count array of selected Question object
    surveyList.foreach {survey =>
        quesList(selected).count(survey.quesAns(selected) - 1) += 1
    }
    
  }
  
  def isAdminListEmpty : Boolean = {
    
    inputAdminList
    
    //Request to create an AdministratorAccount if no object of AdministratorAccount in adminList
    if(adminList.size() == 0) {
      MainApp.showRegisterDialog
      return true
    }
    return false
    
  }
  
  def editQuestion(quesNo : Integer, newDesc : String) {
    
    //Assign new description of question to replace the selected Question object's description
    quesList(quesNo).description = newDesc 
  
  }

  def addStudent(newStudent:StudentAccount) = {
    
    //Add new StudentAccount object passed in into studentList
    studentList += newStudent
    
    Database.outputStudentList
  }

  def addAdmin(newAdmin:AdministratorAccount) = {
    
    //Add new AdministratorAccount object passed in into adminList
    adminList += newAdmin
    
    Database.outputAdminList
  }
  
  def getStudentList : ObservableBuffer[StudentAccount] = studentList //Getter of studentList
  
  def checkStudentIDUsed(stuID : String) : Boolean = {
    
    //Check whether the stuID passed in used by any of the StudentAccount objects in studentList
    studentList.foreach { student =>
      if(student.equals(stuID)) {
        return true
      }
    }
    return false
    
  }
  
  def checkAdminIDUsed(regID : String) : Boolean = {
    
    //Check whether the regID passed in used by any of the AdministratorAccount objects in adminList
    adminList.foreach { admin =>
      if(admin.equals(regID)) {
        return true
      }
    }
    return false
    
  }
  
  def clearAllSurvey = {
    //CLear all the surveys
    
    initQuesList
    outputQuesList
    surveyList.clear
    studentList.foreach{student =>
      if(student.survey != null)
        student.survey = null
    }
    outputSurveyList
  }
  
  def getQuesList = quesList //Getter for quesList
  
  def getQuestion(selected : Int) = quesList(selected) //Getter for selected Question object in quesList
  
  def getSurvey = surveyList //Getter for surveyList
  
  def checkAvailability(role : String, acc : Account) : Boolean = {
    
    //Check the role selected and check whether the account exists according to the role
    if (role equals("student")) {
      studentList.foreach { student =>
        if(student.equals(acc)) {
          MainApp.currentAccount = student
          return true
        }
        
      }
    } else {
      adminList.foreach { admin =>
        if(admin.equals(acc)) {
          MainApp.currentAccount = admin
          return true
        }
      }
    }
    return false
  }
  
  def addSurvey(newSurvey : Survey) {
    
    //Append newSurvey Survey object into surveyList
    surveyList += newSurvey
  }

}
