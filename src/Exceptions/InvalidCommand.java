package Exceptions;

public class InvalidCommand extends Throwable {

    private String message = null;

    public InvalidCommand(String eMessage){
        this.message = eMessage;
    }

    public String getMessage(){
        return message;
    }
}
