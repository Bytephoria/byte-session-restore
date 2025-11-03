package team.bytephoria.bytesessionrestore.infra.bootstrap;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Provides contextual information used during plugin bootstrap.
 * Encapsulates the plugin's logger and data directory,
 * allowing consistent access throughout initialization.
 */
public final class BootstrapContext {

    private final @NotNull Logger logger;
    private final @NotNull Path dataDirectory;

    public BootstrapContext(final @NotNull Logger logger, final @NotNull Path dataDirectory) {
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    /**
     * Returns the logger assigned to the plugin instance.
     *
     * @return the plugin logger
     */
    public @NotNull Logger logger() {
        return this.logger;
    }

    /**
     * Returns the path to the plugin's data directory.
     *
     * @return the plugin data directory
     */
    public @NotNull Path dataDirectory() {
        return this.dataDirectory;
    }
}
