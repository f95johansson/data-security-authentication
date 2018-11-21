public enum Permissions {
    PRINT("print"),
    QUEUE("queue"),
    TOP_QUEUE("topQueue"),
    START("start"),
    STOP("stop"),
    RESTART("restart"),
    STATUS("status"),
    READ_CONFIG("readConfig"),
    SET_CONFIG("setConfig");

    public final String string;

    Permissions(String string) {
        this.string = string;
    }

    static Permissions fromString(String string) {
        for (Permissions permission : values()) {
            if (permission.string.equals(string)) {
                return permission;
            }
        }
        return null;
    }
}

// print;queue;topQueue;start;stop;restart;status;readConfig;setConfig