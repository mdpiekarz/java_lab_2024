public class AmbiguousPersonException extends RuntimeException {
    public AmbiguousPersonException(String name) {
        super("Znaleziono więcej niż jedną osobę o imieniu i nazwisku: " + name);
    }
}