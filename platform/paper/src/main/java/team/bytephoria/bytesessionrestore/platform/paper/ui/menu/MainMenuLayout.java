package team.bytephoria.bytesessionrestore.platform.paper.ui.menu;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import team.bytephoria.bytesessionrestore.api.model.event.SessionEventType;
import team.bytephoria.bytesessionrestore.core.model.UserAggregate;
import team.bytephoria.bytesessionrestore.infra.config.MenuConfiguration;
import team.bytephoria.bytesessionrestore.infra.config.MessagesConfiguration;
import team.bytephoria.bytesessionrestore.platform.paper.component.adapter.ComponentSerializerAdapter;
import team.bytephoria.bytesessionrestore.platform.paper.messages.Messenger;
import team.bytephoria.bytesessionrestore.platform.paper.util.MaterialFor;
import team.bytephoria.bytesessionrestore.platform.paper.util.TextFor;
import team.bytephoria.layout.items.base.MaterialItem;
import team.bytephoria.layout.items.types.ClickableItemLayout;
import team.bytephoria.layout.items.types.ItemLayout;
import team.bytephoria.layout.layouts.builder.page.PageNavigationBuilder;
import team.bytephoria.layout.layouts.types.layout.LayoutPagedInventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class MainMenuLayout {

    private MainMenuLayout() {}

    public static void open(
            final @NotNull Player viewer,
            final @NotNull Player target,
            final @NotNull UserAggregate user,
            final @NotNull MenuConfiguration menuConfig,
            final @NotNull ComponentSerializerAdapter serializer,
            final @NotNull MessagesConfiguration messages,
            final @NotNull Messenger messenger
    ) {
        final MenuConfiguration.MainMenu mainMenu = menuConfig.menus().main();
        final List<ItemLayout> sessionTypeItems = buildSessionTypeItems(
                target, user, menuConfig, mainMenu.sessionTypes(),
                serializer, messages, messenger
        );

        LayoutPagedInventory.builder()
                .title(TextFor.itemText(mainMenu.title().replace("{player}", target.getName()), serializer))
                .size(mainMenu.size())
                .range(range -> range.start(mainMenu.content().startSlot())
                        .end(mainMenu.content().endSlot())
                        .compact())
                .extend(sessionTypeItems)
                .item(mainMenu.buttons()
                                .getOrDefault("close", defaultButton())
                                .slot(),
                        CommonButtons.custom(mainMenu.buttons().get("close"), serializer))
                .navigation(PageNavigationBuilder::hideOnSinglePage)
                .build()
                .open(viewer);
    }

    private static @NotNull @Unmodifiable List<ItemLayout> buildSessionTypeItems(
            final @NotNull Player target,
            final @NotNull UserAggregate user,
            final @NotNull MenuConfiguration menuConfig,
            final @NotNull Map<String, MenuConfiguration.MenuItem> menuItems,
            final @NotNull ComponentSerializerAdapter serializer,
            final @NotNull MessagesConfiguration messages,
            final @NotNull Messenger messenger
    ) {
        final List<ItemLayout> items = new ArrayList<>();
        for (final SessionEventType sessionEventType : SessionEventType.values()) {
            if (!user.hasSessionsOf(sessionEventType)) {
                continue;
            }

            final MenuConfiguration.MenuItem menuItem = menuItems.getOrDefault(sessionEventType.name(), defaultItem());
            final Material material = MaterialFor.safeType(menuItem.material());
            final Component name = TextFor.itemText(menuItem.name(), serializer);
            final List<Component> lore = menuItem.lore().stream()
                    .map(line -> TextFor.itemText(line, serializer))
                    .toList();

            final ItemLayout layout = ClickableItemLayout.builder()
                    .item(MaterialItem.builder()
                            .material(material)
                            .displayName(name)
                            .lore(lore)
                            .build())
                    .onLeftClick(clickContext ->
                            SessionListLayout.open(clickContext.player(), target, sessionEventType, user, menuConfig, serializer, messages, messenger)
                    )
                    .build();

            items.add(layout);
        }

        return Collections.unmodifiableList(items);
    }

    @Contract(" -> new")
    private static MenuConfiguration.@NotNull MenuButton defaultButton() {
        return new MenuConfiguration.MenuButton();
    }

    @Contract(" -> new")
    private static MenuConfiguration.@NotNull MenuItem defaultItem() {
        return new MenuConfiguration.MenuItem();
    }
}
