package team.bytephoria.bytesessionrestore.platform.paper;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.compression.CompressionProvider;
import team.bytephoria.bytesessionrestore.core.ApplicationFacade;
import team.bytephoria.bytesessionrestore.core.registry.CompressionRegistry;
import team.bytephoria.bytesessionrestore.infra.bootstrap.BootstrapContext;
import team.bytephoria.bytesessionrestore.infra.bootstrap.PluginLifecycle;
import team.bytephoria.bytesessionrestore.infra.config.Configuration;
import team.bytephoria.bytesessionrestore.platform.commonbukkit.BukkitSnapshotProvider;
import team.bytephoria.bytesessionrestore.platform.paper.command.SessionCommand;
import team.bytephoria.bytesessionrestore.platform.paper.listener.PlayerChangedWorldListener;
import team.bytephoria.bytesessionrestore.platform.paper.listener.PlayerDeathListener;
import team.bytephoria.bytesessionrestore.platform.paper.listener.PlayerQuitListener;
import team.bytephoria.bytesessionrestore.platform.paper.session.PlatformSessionCreator;
import team.bytephoria.bytesessionrestore.platform.paper.task.SessionCleanupTask;
import team.bytephoria.bytesessionrestore.spi.compression.CompressionStrategy;
import team.bytephoria.bytesessionrestore.spi.storage.StorageConnection;
import team.bytephoria.bytesessionrestore.spi.storage.UserStorage;
import team.bytephoria.bytesessionrestore.spi.storage.config.JdbcPoolConfig;
import team.bytephoria.bytesessionrestore.storage.sql.h2.H2StorageConnection;
import team.bytephoria.bytesessionrestore.storage.sql.h2.H2StorageConnectionData;
import team.bytephoria.bytesessionrestore.storage.sql.h2.H2UserStorage;

import java.io.File;
import java.util.logging.Logger;

public final class PaperBootstrap implements PluginLifecycle {

    private final PaperPlugin paperPlugin;
    private final BootstrapContext context;

    private StorageConnection storageConnection;
    private UserStorage userStorage;
    private ApplicationFacade applicationFacade;
    private PlatformSessionCreator platformSessionCreator;

    public PaperBootstrap(
            final @NotNull PaperPlugin paperPlugin,
            final @NotNull BootstrapContext context
    ) {
        this.paperPlugin = paperPlugin;
        this.context = context;
    }

    @Override
    public void load() {
        CompressionProvider.registerAll();
        BukkitSnapshotProvider.registerAll();
    }

    @Override
    public void enable() {
        this.logger().info("[ByteSessionRestore] Enabling runtime components...");

        this.initializeStorage();
        if (this.userStorage == null) {
            throw new IllegalStateException("UserStorage could not be initialized.");
        }

        final String compressionName = this.paperPlugin.configuration().session().compression();
        final CompressionStrategy compression = CompressionRegistry.get(compressionName);
        if (compression == null) {
            throw new IllegalStateException("Compression strategy not found: " + compressionName);
        }

        this.applicationFacade = new ApplicationFacade(
                this.userStorage,
                compression,
                this.paperPlugin.configuration().session().maxPerType()
        );

        if (this.storageConnection == null) {
            throw new IllegalStateException("Storage connection is null.");
        }

        this.storageConnection.connect();
        this.logger().info("[ByteSessionRestore] Storage connection established.");

        this.platformSessionCreator = new PlatformSessionCreator(
                this.paperPlugin.configuration(),
                this.applicationFacade.userSessionService()
        );

        this.registerListeners();
        this.registerCommands();
        this.scheduleCleanupTask();

        this.logger().info("[ByteSessionRestore] Runtime initialization complete.");
    }

    @Override
    public void disable() {
        this.logger().info("[ByteSessionRestore] Disabling plugin and cleaning up resources...");

        this.paperPlugin.getServer().getAsyncScheduler().cancelTasks(this.paperPlugin);
        HandlerList.unregisterAll(this.paperPlugin);

        if (this.storageConnection != null) {
            this.storageConnection.close();
            this.logger().info("[ByteSessionRestore] Storage connection closed.");
        }

        this.platformSessionCreator = null;
        this.storageConnection = null;
        this.userStorage = null;
        this.applicationFacade = null;
    }

    private void initializeStorage() {
        final Configuration.Storage storageConfig = this.paperPlugin.configuration().storage();
        final JdbcPoolConfig poolConfig = new JdbcPoolConfig(
                storageConfig.pool().connectionTimeout(),
                storageConfig.pool().maxLifetime(),
                storageConfig.pool().maxPoolSize(),
                storageConfig.pool().minIdle()
        );

        final String type = storageConfig.type().toUpperCase();
        if (type.equals("H2")) {
            final File file = this.context.dataDirectory().resolve(storageConfig.h2().file()).toFile();
            final HikariDataSource hikari = new H2StorageConnectionData()
                    .file(file)
                    .pool(poolConfig)
                    .build();

            this.storageConnection = new H2StorageConnection(hikari);
            this.userStorage = new H2UserStorage(this.storageConnection);

            this.logger().info("[ByteSessionRestore] H2 storage initialized successfully at: " + file.getAbsolutePath());
        } else {
            throw new IllegalStateException("Invalid storage type: " + type);
        }
    }

    private void scheduleCleanupTask() {
        final Configuration.Session sessionConfig = this.paperPlugin.configuration().session();
        if (sessionConfig.maxPerType() <= -1) {
            this.logger().info("[ByteSessionRestore] Cleanup task disabled (maxPerType = -1).");
            return;
        }

        final int interval = sessionConfig.cleanupIntervalSeconds();
        final SessionCleanupTask cleanupTask = new SessionCleanupTask(
                this.paperPlugin,
                this.applicationFacade.cleanupService()
        );

        cleanupTask.schedule(interval);
        this.logger().info("[ByteSessionRestore] Cleanup task scheduled every " + interval + " seconds.");
    }

    private void registerListeners() {
        this.paperPlugin.registerListeners(
                new PlayerQuitListener(this.platformSessionCreator),
                new PlayerDeathListener(this.platformSessionCreator),
                new PlayerChangedWorldListener(this.platformSessionCreator)
        );

        this.logger().info("[ByteSessionRestore] Listeners registered successfully.");
    }

    private void registerCommands() {
        this.paperPlugin.registerCommands(
                new SessionCommand(
                        this.paperPlugin,
                        this.applicationFacade.userService(),
                        this.applicationFacade.sessionService(),
                        this.paperPlugin.menuConfiguration(),
                        this.paperPlugin.messagesConfiguration(),
                        this.paperPlugin.componentSerializerAdapter(),
                        this.paperPlugin.messenger()
                )
        );

        this.logger().info("[ByteSessionRestore] Commands registered successfully.");
    }

    public PlatformSessionCreator platformSessionCreator() {
        return this.platformSessionCreator;
    }

    public StorageConnection storageConnection() {
        return this.storageConnection;
    }

    public UserStorage userStorage() {
        return this.userStorage;
    }

    private @NotNull Logger logger() {
        return this.context.logger();
    }

    public @NotNull ApplicationFacade applicationFacade() {
        return this.applicationFacade;
    }

}
