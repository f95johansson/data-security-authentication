package BackendStuff.SafeTypes;

import java.rmi.RemoteException;

public class NonNullString extends NonNull<String> {
    public NonNullString(String value) throws RemoteException {
        super(value);
    }
}