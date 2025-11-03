package team.bytephoria.bytesessionrestore.platform.paper.ui.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.api.model.event.SessionEventType;
import team.bytephoria.bytesessionrestore.core.model.*;
import team.bytephoria.bytesessionrestore.infra.config.*;
import team.bytephoria.bytesessionrestore.platform.commonbukkit.snapshot.InventoryLocationSnapshot;
import team.bytephoria.bytesessionrestore.platform.paper.component.adapter.ComponentSerializerAdapter;
import team.bytephoria.bytesessionrestore.platform.paper.messages.Messenger;
import team.bytephoria.bytesessionrestore.platform.paper.util.*;
import team.bytephoria.bytesessionrestore.platform.paper.util.exception.NonInstantiableClassException;
import team.bytephoria.layout.items.base.MaterialItem;
import team.bytephoria.layout.items.types.ClickableItemLayout;
import team.bytephoria.layout.layouts.*;
import team.bytephoria.layout.layouts.types.layout.LayoutSizedInventory;

import java.util.List;

public final class SessionContentLayout {

    private SessionContentLayout() {
        throw new NonInstantiableClassException();
    }

    public static void open(
            final @NotNull Player viewer,
            final @NotNull Player target,
            final @NotNull List<LazySessionRecord> sessions,
            final @NotNull SessionEventType type,
            final @NotNull InventoryLocationSnapshot snapshot,
            final @NotNull UserAggregate user,
            final @NotNull MenuConfiguration menuConfig,
            final @NotNull ComponentSerializerAdapter serializer,
            final @NotNull MessagesConfiguration messages,
            final @NotNull Messenger messenger
    ) {
        final var contentMenu = menuConfig.menus().sessionContent();

        final LayoutSizedInventory layout = Layout.sized()
                .title(TextFor.itemText(contentMenu.title()
                        .replace("{snapshot-type}", snapshot.typeId()), serializer))
                .size(contentMenu.size())

                // Navigation
                .item(contentMenu.buttons().get("back").slot(),
                        CommonButtons.custom(contentMenu.buttons().get("back"), serializer,
                                () -> SessionListLayout.open(viewer, target, type, user, menuConfig, serializer, messages, messenger)))

                .item(contentMenu.buttons().get("close").slot(),
                        CommonButtons.custom(contentMenu.buttons().get("close"), serializer))

                // Teleport button
                .item(contentMenu.buttons().get("teleport").slot(),
                        ClickableItemLayout.builder()
                                .item(MaterialItem.builder()
                                        .material(MaterialFor.safeType(contentMenu.buttons().get("teleport").material()))
                                        .displayName(TextFor.itemText(contentMenu.buttons().get("teleport").name(), serializer))
                                        .lore(contentMenu.buttons().get("teleport").lore().stream()
                                                .map(line -> TextFor.itemText(line, serializer))
                                                .toList())
                                        .build())
                                .onAnyClick(clickContext -> {
                                    clickContext.clickEvent().setCancelled(true);
                                    viewer.teleportAsync(snapshot.location())
                                            .thenAccept(v -> messenger.send(viewer, messages.restore().teleport()));
                                })
                                .build())

                // Restore buttons
                .item(contentMenu.buttons().get("restore-inventory").slot(),
                        CommonButtons.custom(contentMenu.buttons().get("restore-inventory"), serializer,
                                () -> {
                                    target.getInventory().setContents(snapshot.contents());
                                    messenger.send(viewer, messages.restore().inventoryOnly());
                                }))

                .item(contentMenu.buttons().get("restore-session").slot(),
                        CommonButtons.custom(contentMenu.buttons().get("restore-session"), serializer,
                                () -> {
                                    snapshot.restore(target);
                                    messenger.send(viewer, messages.restore().success());
                                }))

                // Behavior
                .behavior(b -> b.ignoreEmptySlots(true)
                        .cancelLayoutClicks(false)
                        .allowPlayerInventoryClicks(true)
                        .itemLoadingStrategy(ItemLoadingStrategy.LAZY))
                .build();

        // Fill inventory contents
        final Inventory inventory = layout.getInventory();
        final ItemStack[] contents = snapshot.contents();
        for (int i = 0; i < inventory.getSize() && i < contents.length; i++) {
            if (contents[i] != null) inventory.setItem(i, contents[i]);
        }

        layout.open(viewer);
    }
}
