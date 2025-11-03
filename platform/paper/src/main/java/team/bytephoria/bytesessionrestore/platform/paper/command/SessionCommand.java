package team.bytephoria.bytesessionrestore.platform.paper.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.api.model.event.SessionEventType;
import team.bytephoria.bytesessionrestore.core.service.SessionService;
import team.bytephoria.bytesessionrestore.core.service.UserService;
import team.bytephoria.bytesessionrestore.infra.config.MenuConfiguration;
import team.bytephoria.bytesessionrestore.infra.config.MessagesConfiguration;
import team.bytephoria.bytesessionrestore.platform.commonbukkit.snapshot.common.CommonPlayerRestorableSnapshot;
import team.bytephoria.bytesessionrestore.platform.paper.FeaturePermission;
import team.bytephoria.bytesessionrestore.platform.paper.PaperPlugin;
import team.bytephoria.bytesessionrestore.platform.paper.component.adapter.ComponentSerializerAdapter;
import team.bytephoria.bytesessionrestore.platform.paper.messages.Messenger;
import team.bytephoria.bytesessionrestore.platform.paper.ui.menu.MainMenuLayout;

import java.util.Arrays;
import java.util.Collections;

public final class SessionCommand extends BukkitCommand {

    private final PaperPlugin paperPlugin;
    private final UserService userService;

    private final SessionService sessionService;

    private final MenuConfiguration menuConfiguration;
    private final MessagesConfiguration messagesConfiguration;
    private final ComponentSerializerAdapter componentSerializerAdapter;
    private final Messenger messenger;

    public SessionCommand(
            final @NotNull PaperPlugin paperPlugin,
            final @NotNull UserService userService,
            final @NotNull SessionService sessionService,
            final @NotNull MenuConfiguration menuConfiguration,
            final @NotNull MessagesConfiguration messagesConfiguration,
            final @NotNull ComponentSerializerAdapter componentSerializerAdapter,
            final @NotNull Messenger messenger
    ) {
        super(
                "bytesessionrestore",
                "",
                "/<command>",
                Collections.singletonList("bsr")
        );

        this.paperPlugin = paperPlugin;
        this.userService = userService;
        this.sessionService = sessionService;

        this.menuConfiguration = menuConfiguration;
        this.messagesConfiguration = messagesConfiguration;
        this.componentSerializerAdapter = componentSerializerAdapter;
        this.messenger = messenger;
    }

    @Override
    public boolean execute(
            final @NotNull CommandSender commandSender,
            final @NotNull String commandLabel,
            final @NotNull String @NotNull [] arguments
    ) {
        if (!(commandSender instanceof Player)) {
            this.messenger.send(commandSender, this.messagesConfiguration.playerOnly());
            return true;
        }

        if (!commandSender.hasPermission(FeaturePermission.Command.BASE)) {
            this.messenger.send(commandSender, this.messagesConfiguration.noPermission());
            return true;
        }

        if (arguments.length == 0) {
            this.messenger.send(commandSender, this.messagesConfiguration.unknownCommand());
            return true;
        }

        switch (arguments[0].toLowerCase()) {
            case "help" -> this.messenger.send(commandSender, this.messagesConfiguration.help());
            // no
            //case "reload" -> this.reload(commandSender);
            case "view" -> this.view(commandSender, Arrays.copyOfRange(arguments, 1, arguments.length));
            case "save" -> this.save(commandSender, Arrays.copyOfRange(arguments, 1, arguments.length));
            default -> this.messenger.send(commandSender, this.messagesConfiguration.unknownCommand());
        }

        return true;
    }

    private void reload(final @NotNull CommandSender commandSender) {
        if (!commandSender.hasPermission(FeaturePermission.Command.RELOAD)) {
            this.messenger.send(commandSender, this.messagesConfiguration.noPermission());
            return;
        }

        this.messenger.send(commandSender, this.messagesConfiguration.reload().start());
        this.paperPlugin.reloadConfiguration();
        this.messenger.send(commandSender, this.messagesConfiguration.reload().success());
    }

    private void view(final @NotNull CommandSender commandSender, final @NotNull String @NotNull [] arguments) {
        if (!commandSender.hasPermission(FeaturePermission.Command.VIEW)) {
            this.messenger.send(commandSender, this.messagesConfiguration.noPermission());
            return;
        }

        final Player player = (Player) commandSender;
        final String targetName = arguments[0];
        final Player targetPlayer = Bukkit.getPlayerExact(targetName);
        if (targetPlayer == null) {
            this.messenger.send(commandSender, this.messagesConfiguration.invalidTarget());
            return;
        }

        this.userService.loadAsync(targetPlayer.getUniqueId())
                .thenAccept(targetUser -> {
                    if (targetUser == null) {
                        this.messenger.send(commandSender, this.messagesConfiguration.menu().noSessions());
                        return;
                    }

                    Bukkit.getScheduler().runTask(this.paperPlugin, () ->
                            MainMenuLayout.open(
                                    player,
                                    targetPlayer,
                                    targetUser,
                                    this.menuConfiguration,
                                    this.componentSerializerAdapter,
                                    this.messagesConfiguration,
                                    this.messenger
                            )
                    );
                });
    }

    private void save(final @NotNull CommandSender commandSender, final @NotNull String @NotNull [] arguments) {

        final String targetName = arguments[0];
        if (targetName.equalsIgnoreCase("all")) {
            if (!commandSender.hasPermission(FeaturePermission.Command.SAVE_ALL)) {
                this.messenger.send(commandSender, this.messagesConfiguration.noPermission());
                return;
            }

            for (final Player player : Bukkit.getOnlinePlayers()) {
                this.sessionService.createDirectAsync(
                        player.getUniqueId(),
                        player.getName(),
                        SessionEventType.MANUAL,
                        CommonPlayerRestorableSnapshot.createFromPlayer(player)
                );
            }

            this.messenger.send(commandSender, this.messagesConfiguration.sessionCreate().manualAll());
            return;
        }

        if (!commandSender.hasPermission(FeaturePermission.Command.SAVE)) {
            this.messenger.send(commandSender, this.messagesConfiguration.noPermission());
            return;
        }

        final Player targetPlayer = Bukkit.getPlayerExact(targetName);
        if (targetPlayer == null) {
            this.messenger.send(commandSender, this.messagesConfiguration.invalidTarget());
            return;
        }

        this.sessionService.createAsync(
                targetPlayer.getUniqueId(),
                targetPlayer.getName(),
                SessionEventType.MANUAL,
                CommonPlayerRestorableSnapshot.createFromPlayer(targetPlayer)
        );

        this.messenger.send(commandSender,
                this.messagesConfiguration.sessionCreate().manual()
                        .replace("{player}", targetName)
        );

    }

}
