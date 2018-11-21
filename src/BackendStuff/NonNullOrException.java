package BackendStuff;

import java.rmi.RemoteException;

public class NonNullOrException<T> {
    public final T value;

    public NonNullOrException(T value) throws RemoteException {
        if (value == null) throw new RemoteException("String was null");
        this.value = value;
    }
}
