package team.bytephoria.bytesessionrestore.platform.paper.component.adapter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class MiniMessageComponentSerializerAdapter implements ComponentSerializerAdapter {

    @Contract(pure = true)
    @Override
    public @NotNull ComponentSerializer<Component, ? extends Component, String> getSerializer() {
        return MiniMessage.miniMessage();
    }

}