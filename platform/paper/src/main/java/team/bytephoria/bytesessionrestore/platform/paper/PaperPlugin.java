package team.bytephoria.bytesessionrestore.platform.paper;

import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.core.ApplicationFacade;
import team.bytephoria.bytesessionrestore.core.concurrent.AsyncExecutor;
import team.bytephoria.bytesessionrestore.infra.bootstrap.BootstrapContext;
import team.bytephoria.bytesessionrestore.infra.config.Configuration;
import team.bytephoria.bytesessionrestore.infra.config.ConfigurationLoader;
import team.bytephoria.bytesessionrestore.infra.config.MenuConfiguration;
import team.bytephoria.bytesessionrestore.infra.config.MessagesConfiguration;
import team.bytephoria.bytesessionrestore.platform.paper.component.adapter.ComponentSerializerAdapter;
import team.bytephoria.bytesessionrestore.platform.paper.component.adapter.ComponentSerializerFactory;
import team.bytephoria.bytesessionrestore.platform.paper.messages.Messenger;
import team.bytephoria.bytesessionrestore.platform.paper.session.PlatformSessionCreator;
import team.bytephoria.layout.layouts.Layout;

import java.io.File;

public final class PaperPlugin extends JavaPlugin {

    private PaperBootstrap bootstrap;

    private Configuration configuration;
    private MessagesConfiguration messagesConfiguration;
    private MenuConfiguration menuConfiguration;

    private ComponentSerializerAdapter componentSerializerAdapter;
    private Messenger messenger;
    private PlatformSessionCreator platformSessionCreator;

    @Override
    public void onLoad() {
        this.getLogger().info("[ByteSessionRestore] Loading configurations...");

        final BootstrapContext bootstrapContext = new BootstrapContext(this.getLogger(), this.getDataFolder().toPath());
        this.loadAllConfigurations();

        this.componentSerializerAdapter = ComponentSerializerFactory.createAdapter(
                this.configuration.settings().serializer()
        );
        this.messenger = new Messenger(this.componentSerializerAdapter);

        this.bootstrap = new PaperBootstrap(this, bootstrapContext);
        this.bootstrap.load();

        this.getLogger().info("[ByteSessionRestore] Load phase completed successfully.");
    }

    @Override
    public void onEnable() {
        Layout.init(this);

        if (this.bootstrap == null) {
            this.getLogger().severe("[ByteSessionRestore] Bootstrap initialization failed. Aborting startup.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.getLogger().info("[ByteSessionRestore] Enabling platform systems...");

        try {
            this.bootstrap.enable();
        } catch (Exception ex) {
            this.getLogger().severe("[ByteSessionRestore] Failed to enable plugin: " + ex.getMessage());
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.platformSessionCreator = new PlatformSessionCreator(
                this.configuration(),
                this.bootstrap.applicationFacade().userSessionService()
        );

        this.getLogger().info("[ByteSessionRestore] Plugin enabled successfully.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("[ByteSessionRestore] Beginning shutdown...");

        if (this.bootstrap != null) {
            this.bootstrap.disable();
            this.bootstrap = null;
        }

        AsyncExecutor.shutdown();

        this.platformSessionCreator = null;
        this.messenger = null;
        this.componentSerializerAdapter = null;
        this.menuConfiguration = null;
        this.messagesConfiguration = null;
        this.configuration = null;

        this.getLogger().info("[ByteSessionRestore] Shutdown completed successfully.");
    }

    public void reloadConfiguration() {
        this.getLogger().info("[ByteSessionRestore] Reloading configuration files...");

        this.loadAllConfigurations();
        this.componentSerializerAdapter = ComponentSerializerFactory.createAdapter(
                this.configuration.settings().serializer()
        );

        this.messenger = new Messenger(this.componentSerializerAdapter);

        this.getLogger().info("[ByteSessionRestore] Configuration reload completed.");
    }

    private void loadAllConfigurations() {
        this.configuration = this.loadConfiguration("config.yml", Configuration.class, true);
        this.messagesConfiguration = this.loadConfiguration("messages.yml", MessagesConfiguration.class, true);
        this.menuConfiguration = this.loadConfiguration("menu.yml", MenuConfiguration.class, true);
    }

    public void registerListeners(final @NotNull Listener @NotNull ... listeners) {
        for (final Listener listener : listeners) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    public void registerCommands(final @NotNull BukkitCommand @NotNull ... bukkitCommands) {
        for (final BukkitCommand bukkitCommand : bukkitCommands) {
            this.getServer().getCommandMap().register("bytesessionrestore", bukkitCommand);
        }
    }

    private <T> T loadConfiguration(
            final @NotNull String fileName,
            final @NotNull Class<T> type,
            final boolean copyFromResources
    ) {
        final File file = new File(this.getDataFolder(), fileName);
        return ConfigurationLoader.loadConfiguration(file.toString(), type, copyFromResources);
    }

    public @NotNull PlatformSessionCreator platformSessionCreator() {
        return this.platformSessionCreator;
    }

    public @NotNull Configuration configuration() {
        return this.configuration;
    }

    public @NotNull MessagesConfiguration messagesConfiguration() {
        return this.messagesConfiguration;
    }

    public @NotNull MenuConfiguration menuConfiguration() {
        return this.menuConfiguration;
    }

    public @NotNull ApplicationFacade applicationFacade() {
        return this.bootstrap.applicationFacade();
    }

    public @NotNull ComponentSerializerAdapter componentSerializerAdapter() {
        return this.componentSerializerAdapter;
    }

    public @NotNull Messenger messenger() {
        return this.messenger;
    }

}