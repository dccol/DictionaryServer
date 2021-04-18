/**
 * Author: Daniel Coleman, 994887
 * Date: 18/04/2021
 * */

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
