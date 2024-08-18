package cli;

public enum ExitStatus {
    SUCCESS(0),
    FAILURE(1);

    int code;
    ExitStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
