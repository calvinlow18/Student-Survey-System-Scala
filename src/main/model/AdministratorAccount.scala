package main.model
/*
  * Created by Calvin Low on 5/15/2016.
  */

import scalafx.beans.property.StringProperty

class AdministratorAccount(adminId : StringProperty, adminPass : String, adminName : StringProperty) extends Account(adminId, adminPass, adminName) {

  override def toString : String = { id.value + ";" + password + ";" + name.value } //Create a string that contains data of AdministratorAccount object
  
}