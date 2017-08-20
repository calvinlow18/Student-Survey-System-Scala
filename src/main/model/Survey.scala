package main.model/**
  * Created by Calvin Low on 5/15/2016.
  */
class Survey(val quesAns : Array[Int], var student : String) {

  override def toString : String = {
    //Create a string that stores information of survey including student account that take the survey and his/her answer
    
    val tempString : StringBuilder = new StringBuilder()
    for (i <- 0 to 9) {
      tempString append(quesAns(i)) 
      if (i != 9) 
        tempString append(";")
    }
    student + ";" + tempString.toString() 
    
  }

}
