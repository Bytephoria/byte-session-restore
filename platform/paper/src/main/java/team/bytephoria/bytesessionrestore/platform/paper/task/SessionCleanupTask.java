package team.bytephoria.bytesessionrestore.platform.paper.task;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.core.service.SessionCleanupService;
import team.bytephoria.bytesessionrestore.platform.paper.PaperPlugin;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Logger;

public final class SessionCleanupTask implements Consumer<ScheduledTask> {

    private final PaperPlugin paperPlugin;
    private final SessionCleanupService sessionCleanupService;
    private final Logger logger;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public SessionCleanupTask(
            final @NotNull PaperPlugin paperPlugin,
            final @NotNull SessionCleanupService sessionCleanupService
    ) {
        this.paperPlugin = paperPlugin;
        this.sessionCleanupService = sessionCleanupService;
        this.logger = paperPlugin.getLogger();
    }

    public void schedule(final long intervalSeconds) {
        this.paperPlugin.getServer().getAsyncScheduler().runAtFixedRate(
                this.paperPlugin,
                this,
                intervalSeconds,
                intervalSeconds,
                TimeUnit.SECONDS
        );
    }

    @Override
    public void accept(final ScheduledTask scheduledTask) {
        if (!this.running.compareAndSet(false, true)) {
            return;
        }

        try {
            final List<UUID> uuids = Bukkit.getOnlinePlayers()
                    .stream()
                    .map(Entity::getUniqueId)
                    .toList();

            if (!uuids.isEmpty()) {
                this.sessionCleanupService.cleanupOnline(uuids);
            }

        } catch (Throwable t) {
            this.logger.warning("[Cleanup] Exception during cleanup: " + t.getMessage());
        } finally {
            this.running.set(false);
        }
    }
}
