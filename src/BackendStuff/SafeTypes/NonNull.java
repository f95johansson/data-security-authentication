package BackendStuff.SafeTypes;

import java.rmi.RemoteException;

public class NonNull<T> {
    public final T value;

    public NonNull(T value) throws RemoteException {
        if (value == null) throw new RemoteException("Value was null");
        this.value = value;
    }
}
