package team.bytephoria.bytesessionrestore.api;

/**
 * Generic builder interface used for constructing objects
 * in a type-safe and fluent manner.
 *
 * @param <O> the output type to be built
 */
public interface Builder<O> {

    /**
     * Builds and returns the resulting object.
     *
     * @return the constructed object
     */
    O build();
}
