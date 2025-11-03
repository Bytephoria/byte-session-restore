package team.bytephoria.bytesessionrestore.platform.paper.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.api.model.event.SessionEventType;
import team.bytephoria.bytesessionrestore.platform.paper.session.PlatformSessionCreator;

public final class PlayerChangedWorldListener implements Listener {

    private final PlatformSessionCreator platformSessionCreator;
    public PlayerChangedWorldListener(final @NotNull PlatformSessionCreator platformSessionCreator) {
        this.platformSessionCreator = platformSessionCreator;
    }

    @EventHandler
    public void onPlayerChangedWorldEvent(final @NotNull PlayerChangedWorldEvent worldEvent) {
        this.platformSessionCreator.createSessionIfValid(worldEvent.getPlayer(), SessionEventType.WORLD_CHANGE);
    }

}
