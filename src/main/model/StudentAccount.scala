package main.model

import scalafx.beans.property.{StringProperty, IntegerProperty}

/**
  * Created by Calvin Low on 5/15/2016.
  */
class StudentAccount(override val id : StringProperty, stuPass: String, stuName : StringProperty, var email : StringProperty, var intake : StringProperty, var semester : IntegerProperty) extends Account(id, stuPass, stuName) {

  //Make sure all Student object have survey initialized. If survey attempted, the survey will be assigned when inputting the survey into system.
  var survey : Survey = null

  //Create string which can be stored in database later
  override def toString(): String = id.value + ";" + password + ";" + name.value + ";" + email.value + ";" + intake.value + ";" + semester.value 
  
}


