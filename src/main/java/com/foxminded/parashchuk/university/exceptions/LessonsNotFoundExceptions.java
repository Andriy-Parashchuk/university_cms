package com.foxminded.parashchuk.university.exceptions;

public class LessonsNotFoundExceptions extends Exception{

  public LessonsNotFoundExceptions(){}

  public LessonsNotFoundExceptions(String message){
    super(message);
  }

  public LessonsNotFoundExceptions(Throwable cause){
    super(cause);
  }

  public LessonsNotFoundExceptions(String message, Throwable cause){
    super(message, cause);
  }
}
