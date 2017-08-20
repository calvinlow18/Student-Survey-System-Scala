	/*
  * Created by Calvin Low on 5/15/2016.
  */
package main.model

case class Question(var description : String) {
  
  var count : Array[Int] = Array.fill(5)(0) //Used to count the number of students that rank from 1 to 5
  
}