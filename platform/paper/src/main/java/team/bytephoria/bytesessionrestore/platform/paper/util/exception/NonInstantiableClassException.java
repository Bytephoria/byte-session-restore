package team.bytephoria.bytesessionrestore.platform.paper.util.exception;

public final class NonInstantiableClassException extends UnsupportedOperationException {

    public NonInstantiableClassException() {
        super("This class cannot be instantiated.");
    }
}