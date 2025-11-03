package team.bytephoria.bytesessionrestore.platform.paper.component.adapter;

import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.platform.paper.util.exception.NonInstantiableClassException;

public final class ComponentSerializerFactory {

    private ComponentSerializerFactory() {
        throw new NonInstantiableClassException();
    }

    public static @NotNull ComponentSerializerAdapter createAdapter(final @NotNull String typeName) {
        return switch (typeName.toUpperCase()) {
            case "MINI_MESSAGE" -> new MiniMessageComponentSerializerAdapter();
            case "LEGACY_AMPERSAND" -> new LegacyAmpersandComponentSerializerAdapter();
            default -> new PlainComponentSerializerAdapter();
        };
    }

}
