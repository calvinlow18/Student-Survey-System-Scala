package main.model/**
  * Created by Calvin Low on 5/15/2016.
  */

import scalafx.beans.property.StringProperty

class Account(val id : StringProperty, var password : String, var name : StringProperty) {
  
  def equals(acc: Account): Boolean = {
    
    /*Check whether the account id and password is the same as the Account object passed in.
    Return true if the account exist and false if account does not exist*/
    
    if(acc.id.value.equals(id.value) && acc.password.equals(password)) {
      return true
    }
    return false
  }
  
  def equals(identity : String) : Boolean = identity.equals(id.value) //Check whether the passed in identity is the same as current account id
}
