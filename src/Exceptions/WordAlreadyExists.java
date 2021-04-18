package Exceptions;

public class WordAlreadyExists extends Throwable {

    private String message = null;

    public WordAlreadyExists(String eMessage){
        this.message = eMessage;
    }

    public String getMessage(){
        return message;
    }
}
