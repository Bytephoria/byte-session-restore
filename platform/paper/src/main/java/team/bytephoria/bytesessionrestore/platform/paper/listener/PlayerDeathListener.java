package team.bytephoria.bytesessionrestore.platform.paper.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.api.model.event.SessionEventType;
import team.bytephoria.bytesessionrestore.platform.paper.session.PlatformSessionCreator;

public final class PlayerDeathListener implements Listener {

    private final PlatformSessionCreator platformSessionCreator;
    public PlayerDeathListener(final @NotNull PlatformSessionCreator platformSessionCreator) {
        this.platformSessionCreator = platformSessionCreator;
    }

    @EventHandler
    public void onPlayerDeathEvent(final @NotNull PlayerDeathEvent deathEvent) {
        this.platformSessionCreator.createSessionIfValid(deathEvent.getPlayer(), SessionEventType.DEATH);
    }
}
