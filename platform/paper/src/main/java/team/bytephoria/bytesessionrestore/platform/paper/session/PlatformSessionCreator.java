package team.bytephoria.bytesessionrestore.platform.paper.session;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.api.model.event.SessionEventType;
import team.bytephoria.bytesessionrestore.core.service.UserSessionService;
import team.bytephoria.bytesessionrestore.infra.config.Configuration;
import team.bytephoria.bytesessionrestore.platform.commonbukkit.snapshot.common.CommonPlayerRestorableSnapshot;

public final class PlatformSessionCreator {

    private final Configuration configuration;
    private final UserSessionService userSessionService;

    public PlatformSessionCreator(
            final @NotNull Configuration configuration,
            final @NotNull UserSessionService userSessionService
    ) {
        this.configuration = configuration;
        this.userSessionService = userSessionService;
    }

    public void createSessionIfValid(
            final @NotNull Player player,
            final @NotNull SessionEventType eventType
    ) {

        final Configuration.Session session = this.configuration.session();
        if (session.ignoreEmptyInventory() && player.getInventory().isEmpty()) {
            return;
        }

        this.userSessionService.createForUserAsync(
                player.getUniqueId(),
                player.getName(),
                eventType,
                CommonPlayerRestorableSnapshot.createFromPlayer(player)
        );
    }
}
