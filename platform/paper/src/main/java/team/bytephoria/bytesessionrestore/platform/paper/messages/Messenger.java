package team.bytephoria.bytesessionrestore.platform.paper.messages;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.platform.paper.component.adapter.ComponentSerializerAdapter;

import java.util.Collection;
import java.util.List;

public final class Messenger {

    private final ComponentSerializerAdapter componentSerializerAdapter;
    public Messenger(final @NotNull ComponentSerializerAdapter componentSerializerAdapter) {
        this.componentSerializerAdapter = componentSerializerAdapter;
    }

    public void send(final @NotNull Audience audience, final @NotNull Collection<String> messages) {
        final List<Component> components = messages
                .stream()
                .<Component>map(string -> this.componentSerializerAdapter.getSerializer().deserialize(string))
                .toList();

        for (final Component component : components) {
            audience.sendMessage(component);
        }
    }

    public void send(final @NotNull Audience audience, final @NotNull String message) {
        final Component component = this.componentSerializerAdapter
                .getSerializer()
                .deserialize(message);

        audience.sendMessage(component);
    }

}
