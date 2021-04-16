package Exceptions;

public class MissingMeaning extends Throwable {

    private String message = null;

    public MissingMeaning(String eMessage){
        this.message = eMessage;
    }

    public String getMessage(){
        return message;
    }
}