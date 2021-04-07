package Exceptions;

public class WordNotFound extends Throwable{
    private String message = null;

    public WordNotFound(String eMessage){
        this.message = eMessage;
    }

    public String getMessage(){
        return message;
    }
}
