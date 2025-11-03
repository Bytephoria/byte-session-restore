package team.bytephoria.bytesessionrestore.infra.bootstrap;

/**
 * Defines the lifecycle contract for a plugin or platform integration.
 * Each implementation is expected to handle initialization,
 * activation, and graceful shutdown of its internal systems.
 */
public interface PluginLifecycle {

    /**
     * Called during the initial load phase before the plugin is enabled.
     * Use this stage to prepare configurations, registries, or dependencies.
     */
    void load();

    /**
     * Called when the plugin is enabled by the server.
     * All core systems should be started here.
     */
    void enable();

    /**
     * Called when the plugin is disabled or the server is shutting down.
     * Use this stage to release resources and stop scheduled tasks.
     */
    void disable();
}
