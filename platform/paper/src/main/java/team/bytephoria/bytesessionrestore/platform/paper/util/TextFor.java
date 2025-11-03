package team.bytephoria.bytesessionrestore.platform.paper.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.platform.paper.component.adapter.ComponentSerializerAdapter;
import team.bytephoria.bytesessionrestore.platform.paper.util.exception.NonInstantiableClassException;

public final class TextFor {

    private TextFor() {
        throw new NonInstantiableClassException();
    }

    public static @NotNull Component itemText(final @NotNull String text, final @NotNull ComponentSerializerAdapter componentSerializerAdapter) {
        return componentSerializerAdapter.getSerializer().deserialize(text)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

}
