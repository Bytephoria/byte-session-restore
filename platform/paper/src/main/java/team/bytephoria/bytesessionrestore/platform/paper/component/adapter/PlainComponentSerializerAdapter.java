package team.bytephoria.bytesessionrestore.platform.paper.component.adapter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class PlainComponentSerializerAdapter implements ComponentSerializerAdapter {

    @Contract(pure = true)
    @Override
    public @NotNull ComponentSerializer<Component, ? extends Component, String> getSerializer() {
        return PlainTextComponentSerializer.plainText();
    }
}
