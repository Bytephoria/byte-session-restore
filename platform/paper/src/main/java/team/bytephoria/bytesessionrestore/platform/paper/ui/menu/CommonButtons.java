package team.bytephoria.bytesessionrestore.platform.paper.ui.menu;

import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.infra.config.MenuConfiguration;
import team.bytephoria.bytesessionrestore.platform.paper.component.adapter.ComponentSerializerAdapter;
import team.bytephoria.bytesessionrestore.platform.paper.util.MaterialFor;
import team.bytephoria.bytesessionrestore.platform.paper.util.TextFor;
import team.bytephoria.bytesessionrestore.platform.paper.util.exception.NonInstantiableClassException;
import team.bytephoria.layout.items.base.MaterialItem;
import team.bytephoria.layout.items.types.ClickableItemLayout;

public final class CommonButtons {

    private CommonButtons() {
        throw new NonInstantiableClassException();
    }

    public static @NotNull ClickableItemLayout custom(
            final MenuConfiguration.@NotNull MenuButton button,
            final @NotNull ComponentSerializerAdapter serializer
    ) {
        return custom(button, serializer, () -> {});
    }

    public static @NotNull ClickableItemLayout custom(
            final MenuConfiguration.@NotNull MenuButton button,
            final @NotNull ComponentSerializerAdapter serializer,
            final @NotNull Runnable action
    ) {
        final MaterialItem item = MaterialItem.builder()
                .material(MaterialFor.safeType(button.material()))
                .displayName(TextFor.itemText(button.name(), serializer))
                .lore(button.lore().stream()
                        .map(line -> TextFor.itemText(line, serializer))
                        .toList())
                .build();

        return ClickableItemLayout.builder()
                .item(item)
                .onAnyClick(clickContext -> {
                    clickContext.clickEvent().setCancelled(true);
                    clickContext.player().closeInventory();
                    action.run();
                })
                .build();
    }
}
