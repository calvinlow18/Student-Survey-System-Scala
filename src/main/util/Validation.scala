package main.util

import java.util.regex.{Pattern, Matcher}

object Validation {
  def checkInput(data : String) : Boolean = {data == null || data.length == 0} //Check whether the data passed in through the argument is empty
  
  def isEmailValid(stuEmail : String) : Boolean = {
    //Check whether stuEmail passed in has a valid email format
    
    val pattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
    val p : Pattern = Pattern.compile(pattern)
    val m : Matcher = p.matcher(stuEmail)
    return m.matches()
  }
 
  def noSelectedCheck(toggle : Object) : Boolean = { toggle == null } //Check whether toggle is selected
}