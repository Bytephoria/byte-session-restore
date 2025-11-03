package team.bytephoria.bytesessionrestore.platform.paper;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.platform.paper.util.exception.NonInstantiableClassException;

public final class FeaturePermission {

    private FeaturePermission() {
        throw new NonInstantiableClassException();
    }

    private static final String ROOT = "bytesessionrestore";

    public static final class Command {

        private static final String IDENTIFIER = "command";

        public static final String BASE = node("base");

        public static final String RELOAD = node("reload");

        public static final String VIEW = node("view");

        public static final String SAVE = node("save");

        public static final String SAVE_ALL = node("save_all");

        private Command() {
            throw new NonInstantiableClassException();
        }

        @Contract(pure = true)
        private static @NotNull String node(final String name) {
            return "%s.%s.%s".formatted(ROOT, IDENTIFIER, name);
        }
    }

}
