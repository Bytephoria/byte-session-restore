package team.bytephoria.bytesessionrestore.infra.config;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;
import java.util.Map;

@ConfigSerializable
public final class MenuConfiguration {

    @Setting("menus")
    private Menus menus = new Menus();

    public @NotNull Menus menus() {
        return this.menus;
    }

    @ConfigSerializable
    public static final class Menus {

        @Setting("main")
        private MainMenu main = new MainMenu();

        @Setting("session-list")
        private SessionListMenu sessionList = new SessionListMenu();

        @Setting("session-content")
        private SessionContentMenu sessionContent = new SessionContentMenu();

        public @NotNull MainMenu main() {
            return this.main;
        }

        public @NotNull SessionListMenu sessionList() {
            return this.sessionList;
        }

        public @NotNull SessionContentMenu sessionContent() {
            return this.sessionContent;
        }
    }

    @ConfigSerializable
    public static final class MainMenu {

        @Setting("title")
        private String title = "<gradient:#00fff2:#66ffcc:#b3fff3>⛓ Sessions of {player}</gradient>";

        @Setting("size")
        private int size = 5;

        @Setting("content")
        private ContentRange content = new ContentRange(10, 25);

        @Setting("buttons")
        private Map<String, MenuButton> buttons = Map.of(
                "close", new MenuButton(
                        40,
                        "BARRIER",
                        "<gradient:#ff6b81:#ff4d6d>✖ Close Menu</gradient>",
                        List.of("<#a0a0a0>Click to close this menu.")
                )
        );

        @Setting("session-types")
        private Map<String, MenuItem> sessionTypes = Map.of(
                "DEATH", new MenuItem(
                        "SKELETON_SKULL",
                        "<gradient:#ff4d6d:#ff8095>☠ Death Sessions</gradient>",
                        List.of("<#a0a0a0>View sessions created when the player <#ff4d6d>died</#ff4d6d>.")
                ),
                "DISCONNECT", new MenuItem(
                        "OAK_DOOR",
                        "<gradient:#00ffbf:#80ffe0>🚪 Quit Sessions</gradient>",
                        List.of("<#a0a0a0>View sessions saved when the player <#80ffe0>left</#80ffe0>.")
                ),
                "CHANGE_WORLD", new MenuItem(
                        "ENDER_PEARL",
                        "<gradient:#80ffe0:#b3fff3>🌍 World Change Sessions</gradient>",
                        List.of("<#a0a0a0>View sessions saved when changing <#b3fff3>worlds</#b3fff3>.")
                ),
                "MANUAL", new MenuItem(
                        "BOOK",
                        "<gradient:#66ffcc:#b3fff3>📘 Manual Sessions</gradient>",
                        List.of("<#a0a0a0>Saved manually by <#66ffcc>admins</#66ffcc> or the system.")
                )
        );

        public String title() {
            return this.title;
        }

        public int size() {
            return this.size;
        }

        public @NotNull ContentRange content() {
            return this.content;
        }

        public @NotNull Map<String, MenuButton> buttons() {
            return this.buttons;
        }

        public @NotNull Map<String, MenuItem> sessionTypes() {
            return this.sessionTypes;
        }
    }

    @ConfigSerializable
    public static final class SessionListMenu {

        @Setting("title")
        private String title = "<gradient:#00ffbf:#b3fff3>📦 Session Content</gradient>";

        @Setting("size")
        private int size = 6;

        @Setting("content")
        private ContentRange content = new ContentRange(10, 34);

        @Setting("buttons")
        private Map<String, MenuButton> buttons = Map.of(
                "back", new MenuButton(
                        48,
                        "ARROW",
                        "<gradient:#80ffe0:#b3fff3>⬅ Back</gradient>",
                        List.of("<#a0a0a0>Return to <#80ffe0>session types</#80ffe0>.")
                ),
                "close", new MenuButton(
                        49,
                        "BARRIER",
                        "<gradient:#ff6b81:#ff4d6d>✖ Close</gradient>",
                        List.of("<#a0a0a0>Close this menu.")
                )
        );

        @Setting("navigation")
        private Navigation navigation = new Navigation();

        @Setting("item")
        private MenuItem item = new MenuItem(
                "BARREL",
                "<gradient:#ff4d6d:#ff8080>{type} Session</gradient>",
                List.of(
                        "<#a0a0a0>Date: <gradient:#00ffbf:#b3fff3>{date}</gradient>",
                        "<#a0a0a0>Click to view <#80ffe0>details</#80ffe0>."
                )
        );

        public String title() {
            return this.title;
        }

        public int size() {
            return this.size;
        }

        public @NotNull ContentRange content() {
            return this.content;
        }

        public @NotNull Map<String, MenuButton> buttons() {
            return this.buttons;
        }

        public @NotNull Navigation navigation() {
            return this.navigation;
        }

        public @NotNull MenuItem item() {
            return this.item;
        }
    }

    @ConfigSerializable
    public static final class SessionContentMenu {

        @Setting("title")
        private String title = "<gradient:#00ffbf:#b3fff3>🧰 Snapshot: {snapshot-type}</gradient>";

        @Setting("size")
        private int size = 6;

        @Setting("buttons")
        private Map<String, MenuButton> buttons = Map.of(
                "back", new MenuButton(
                        45,
                        "ARROW",
                        "<gradient:#80ffe0:#b3fff3>⬅ Back</gradient>",
                        List.of("<#a0a0a0>Return to <#80ffe0>session list</#80ffe0>.")
                ),
                "close", new MenuButton(
                        49,
                        "BARRIER",
                        "<gradient:#ff6b81:#ff4d6d>✖ Close</gradient>",
                        List.of("<#a0a0a0>Exit this menu.")
                ),
                "teleport", new MenuButton(
                        50,
                        "ENDER_EYE",
                        "<gradient:#00ffbf:#80ffe0>🧭 Go to Location</gradient>",
                        List.of("<#a0a0a0>Teleport to this snapshot's <#66ffcc>location</#66ffcc>.")
                ),
                "restore-inventory", new MenuButton(
                        51,
                        "CHEST",
                        "<gradient:#80ffe0:#b3fff3>📦 Restore Inventory</gradient>",
                        List.of("<#a0a0a0>Restores only <#80ffe0>inventory items</#80ffe0>.")
                ),
                "restore-session", new MenuButton(
                        52,
                        "CHEST_MINECART",
                        "<gradient:#00ffbf:#66ffcc>💾 Restore Session</gradient>",
                        List.of(
                                "<#a0a0a0>Restores full <#66ffcc>player state</#66ffcc>:",
                                "<#a0a0a0>(inventory, health, exp, etc.)"
                        )
                )
        );

        @Setting("item")
        private EmptyItem item = new EmptyItem();

        public String title() {
            return this.title;
        }

        public int size() {
            return this.size;
        }

        public @NotNull Map<String, MenuButton> buttons() {
            return this.buttons;
        }

        public @NotNull EmptyItem item() {
            return this.item;
        }
    }

    @ConfigSerializable
    public static final class ContentRange {

        @Setting("start-slot")
        private int startSlot;

        @Setting("end-slot")
        private int endSlot;

        public ContentRange() {
        }

        public ContentRange(final int startSlot, final int endSlot) {
            this.startSlot = startSlot;
            this.endSlot = endSlot;
        }

        public int startSlot() {
            return this.startSlot;
        }

        public int endSlot() {
            return this.endSlot;
        }
    }

    @ConfigSerializable
    public static final class MenuButton {

        @Setting("slot")
        private int slot;

        @Setting("material")
        private String material;

        @Setting("name")
        private String name;

        @Setting("lore")
        private List<String> lore;

        public MenuButton() {
        }

        public MenuButton(final int slot, final String material, final String name, final List<String> lore) {
            this.slot = slot;
            this.material = material;
            this.name = name;
            this.lore = lore;
        }

        public int slot() {
            return this.slot;
        }

        public String material() {
            return this.material;
        }

        public String name() {
            return this.name;
        }

        public @NotNull List<String> lore() {
            return this.lore;
        }
    }

    @ConfigSerializable
    public static final class MenuItem {

        @Setting("material")
        private String material;

        @Setting("name")
        private String name;

        @Setting("lore")
        private List<String> lore;

        public MenuItem() {
        }

        public MenuItem(final String material, final String name, final List<String> lore) {
            this.material = material;
            this.name = name;
            this.lore = lore;
        }

        public String material() {
            return this.material;
        }

        public String name() {
            return this.name;
        }

        public @NotNull List<String> lore() {
            return this.lore;
        }
    }

    @ConfigSerializable
    public static final class Navigation {

        @Setting("previous")
        private MenuButton previous = new MenuButton(
                45,
                "ARROW",
                "<gradient:#80ffe0:#b3fff3>◀ Previous Page</gradient>",
                List.of("<#a0a0a0>Go back one page.")
        );

        @Setting("next")
        private MenuButton next = new MenuButton(
                53,
                "ARROW",
                "<gradient:#00ffbf:#66ffcc>▶ Next Page</gradient>",
                List.of("<#a0a0a0>Go to the next page.")
        );

        public @NotNull MenuButton previous() {
            return this.previous;
        }

        public @NotNull MenuButton next() {
            return this.next;
        }
    }

    @ConfigSerializable
    public static final class EmptyItem {

        @Setting("empty-material")
        private String emptyMaterial = "AIR";

        @Setting("empty-name")
        private String emptyName = "";

        @Setting("empty-lore")
        private List<String> emptyLore = List.of("");

        public String emptyMaterial() {
            return this.emptyMaterial;
        }

        public String emptyName() {
            return this.emptyName;
        }

        public @NotNull List<String> emptyLore() {
            return this.emptyLore;
        }
    }
}
