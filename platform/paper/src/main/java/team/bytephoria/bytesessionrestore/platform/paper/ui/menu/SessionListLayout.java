package team.bytephoria.bytesessionrestore.platform.paper.ui.menu;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.api.model.event.SessionEventType;
import team.bytephoria.bytesessionrestore.core.model.LazySessionRecord;
import team.bytephoria.bytesessionrestore.core.model.UserAggregate;
import team.bytephoria.bytesessionrestore.infra.config.MenuConfiguration;
import team.bytephoria.bytesessionrestore.infra.config.MessagesConfiguration;
import team.bytephoria.bytesessionrestore.platform.commonbukkit.snapshot.InventoryLocationSnapshot;
import team.bytephoria.bytesessionrestore.platform.paper.component.adapter.ComponentSerializerAdapter;
import team.bytephoria.bytesessionrestore.platform.paper.messages.Messenger;
import team.bytephoria.bytesessionrestore.platform.paper.util.MaterialFor;
import team.bytephoria.bytesessionrestore.platform.paper.util.TextFor;
import team.bytephoria.bytesessionrestore.platform.paper.util.exception.NonInstantiableClassException;
import team.bytephoria.layout.items.base.MaterialItem;
import team.bytephoria.layout.items.types.ClickableItemLayout;
import team.bytephoria.layout.items.types.ItemLayout;
import team.bytephoria.layout.layouts.Layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SessionListLayout {

    private SessionListLayout() {
        throw new NonInstantiableClassException();
    }

    public static void open(
            final @NotNull Player viewer,
            final @NotNull Player target,
            final @NotNull SessionEventType type,
            final @NotNull UserAggregate user,
            final @NotNull MenuConfiguration menuConfig,
            final @NotNull ComponentSerializerAdapter serializer,
            final @NotNull MessagesConfiguration messages,
            final @NotNull Messenger messenger
    ) {
        final MenuConfiguration.SessionListMenu sessionList = menuConfig.menus().sessionList();
        final MenuConfiguration.MenuItem itemTemplate = sessionList.item();

        final List<LazySessionRecord> sessions = user.lazySessionsMap()
                .getOrDefault(type, Collections.emptyList());

        if (sessions.isEmpty()) {
            messenger.send(viewer, messages.menu().noSessions());
            return;
        }

        final List<ItemLayout> items = new ArrayList<>(sessions.size());

        for (int i = sessions.size() - 1; i >= 0; i--) {
            final LazySessionRecord lazySessionRecord = sessions.get(i);
            final MaterialItem materialItem = MaterialItem.builder()
                    .material(MaterialFor.safeType(itemTemplate.material()))
                    .displayName(TextFor.itemText(itemTemplate.name()
                            .replace("{type}", type.name()), serializer)
                    )
                    .lore(itemTemplate.lore().stream()
                            .map(line -> TextFor.itemText(
                                    line.replace("{date}", lazySessionRecord.delegate().meta().createdAt().toString()),
                                    serializer
                            ))
                            .toList())
                    .build();

            final var layout = ClickableItemLayout.builder()
                    .item(materialItem)
                    .onLeftClick(ctx -> {
                        final var snapshot = (InventoryLocationSnapshot) lazySessionRecord.snapshot();
                        if (snapshot != null) {
                            SessionContentLayout.open(viewer, target, sessions,
                                    type, snapshot, user, menuConfig, serializer, messages, messenger);
                        }
                    })
                    .build();

            items.add(layout);
        }

        Layout.paged()
                .title(TextFor.itemText(sessionList.title(), serializer))
                .size(sessionList.size())
                .range(r -> r.start(sessionList.content().startSlot())
                        .end(sessionList.content().endSlot())
                        .compact())
                .extend(items)
                .item(sessionList.buttons().get("back").slot(),
                        CommonButtons.custom(sessionList.buttons().get("back"), serializer,
                                () -> MainMenuLayout.open(viewer, target, user,
                                        menuConfig, serializer, messages, messenger)))
                .item(sessionList.buttons().get("close").slot(),
                        CommonButtons.custom(sessionList.buttons().get("close"), serializer))
                .navigation(nav -> {
                    final MenuConfiguration.MenuButton previous = sessionList.navigation().previous();
                    final MenuConfiguration.MenuButton next = sessionList.navigation().next();
                    nav.previous(previous.slot(),
                            MaterialItem.builder()
                                    .material(MaterialFor.safeType(previous.material()))
                                    .displayName(TextFor.itemText(previous.name(), serializer))
                                    .build());
                    nav.next(next.slot(),
                            MaterialItem.builder()
                                    .material(MaterialFor.safeType(next.material()))
                                    .displayName(TextFor.itemText(next.name(), serializer))
                                    .build());
                    nav.hideOnSinglePage().hideOnFirstPage().hideOnLastPage();
                })
                .build()
                .open(viewer);
    }
}
