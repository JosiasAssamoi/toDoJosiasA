package com.example.todojosiasassamoi.tasklist

import java.io.Serializable

data class Task(val id:String,val title:String,val description:String = "description par defaut") : Serializable {

}