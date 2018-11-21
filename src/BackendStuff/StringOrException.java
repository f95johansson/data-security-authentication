package BackendStuff;

import java.rmi.RemoteException;

public class StringOrException extends NonNullOrException<String> {

    public StringOrException(String value) throws RemoteException {
        super(value);
    }
}
