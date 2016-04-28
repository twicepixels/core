package utils.exception.dao;

/**
 * Created by Gabriel
 * on 13/04/2016.
 */
public class ConnectionNoFoundException extends RuntimeException {
    public ConnectionNoFoundException(){
        super("conection.error");
    }
}
