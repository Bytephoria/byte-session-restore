package team.bytephoria.bytesessionrestore.platform.paper.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.api.model.event.SessionEventType;
import team.bytephoria.bytesessionrestore.platform.paper.session.PlatformSessionCreator;

public final class PlayerQuitListener implements Listener {

    private final PlatformSessionCreator platformSessionCreator;
    public PlayerQuitListener(final @NotNull PlatformSessionCreator platformSessionCreator) {
        this.platformSessionCreator = platformSessionCreator;
    }

    @EventHandler
    public void onPlayerQuitEvent(final @NotNull PlayerQuitEvent quitEvent) {
        this.platformSessionCreator.createSessionIfValid(quitEvent.getPlayer(), SessionEventType.DISCONNECT);
    }

}
