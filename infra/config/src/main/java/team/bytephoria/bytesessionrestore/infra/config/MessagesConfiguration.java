package team.bytephoria.bytesessionrestore.infra.config;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;

@ConfigSerializable
public final class MessagesConfiguration {

    @Setting("help")
    private List<String> help = List.of(
            "       <gradient:#00fff2:#66ffcc:#b3fff3><bold>ByteSessionRestore</bold></gradient>",
            "",
            "<gradient:#80ffe0:#b3fff3>/bsr view <yellow><player></yellow></gradient> - <#a0a0a0>View saved sessions of a player.</#a0a0a0>",
            "<gradient:#80ffe0:#b3fff3>/bsr save <yellow><player></yellow></gradient> - <#a0a0a0>Manually save a session for a player.</#a0a0a0>",
            "<gradient:#80ffe0:#b3fff3>/bsr save <yellow>all</yellow></gradient> - <#a0a0a0>Save sessions for all online players.</#a0a0a0>",
            "<gradient:#80ffe0:#b3fff3>/bsr reload</gradient> - <#a0a0a0>Reload all configuration files.</#a0a0a0>",
            "",
            "<#a0a0a0>All commands require the appropriate permission node.</#a0a0a0>"
    );

    public @NotNull List<String> help() {
        return this.help;
    }

    @Setting("reload")
    private ReloadSection reload = new ReloadSection();

    public @NotNull ReloadSection reload() {
        return this.reload;
    }

    @ConfigSerializable
    public static final class ReloadSection {

        @Setting("start")
        private String start = "<gray>Reloading configuration files...</gray>";

        @Setting("success")
        private String success = "<green>All configurations reloaded successfully.</green>";

        public String start() {
            return this.start;
        }

        public String success() {
            return this.success;
        }
    }

    @Setting("no-permission")
    private String noPermission = "<red>You do not have permission to perform this action.</red>";

    @Setting("player-only")
    private String playerOnly = "<red>This command can only be executed by a player.</red>";

    @Setting("unknown-command")
    private String unknownCommand = "<red>Unknown command. Use <yellow>/bsr help</yellow> for usage information.</red>";

    @Setting("invalid-target")
    private String invalidTarget = "<red>The specified player is offline or does not exist.</red>";

    public String noPermission() {
        return this.noPermission;
    }

    public String playerOnly() {
        return this.playerOnly;
    }

    public String unknownCommand() {
        return this.unknownCommand;
    }

    public String invalidTarget() {
        return this.invalidTarget;
    }

    @Setting("restore")
    private RestoreSection restore = new RestoreSection();

    public @NotNull RestoreSection restore() {
        return this.restore;
    }

    @ConfigSerializable
    public static final class RestoreSection {

        @Setting("success")
        private String success = "<green>Session restored successfully.</green>";

        @Setting("inventory-only")
        private String inventoryOnly = "<green>Inventory restored successfully.</green>";

        @Setting("teleport")
        private String teleport = "<green>Teleport executed successfully.</green>";

        public String success() {
            return this.success;
        }

        public String inventoryOnly() {
            return this.inventoryOnly;
        }

        public String teleport() {
            return this.teleport;
        }
    }

    @Setting("menu")
    private MenuSection menu = new MenuSection();

    public @NotNull MenuSection menu() {
        return this.menu;
    }

    @ConfigSerializable
    public static final class MenuSection {

        @Setting("no-sessions")
        private String noSessions = "<red>No sessions found for this player.</red>";

        public String noSessions() {
            return this.noSessions;
        }
    }

    @Setting("session-create")
    private SessionCreateSection sessionCreate = new SessionCreateSection();

    public @NotNull SessionCreateSection sessionCreate() {
        return this.sessionCreate;
    }

    @ConfigSerializable
    public static final class SessionCreateSection {

        @Setting("manual")
        private String manual = "<yellow>Manual session saved successfully for <white>{player}</white>.</yellow>";

        @Setting("manual-all")
        private String manualAll = "<green>Manual sessions have been saved successfully for all online players.</green>";

        public String manual() {
            return this.manual;
        }

        public String manualAll() {
            return this.manualAll;
        }
    }
}