package team.bytephoria.bytesessionrestore.core;

import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.core.cache.UserCache;
import team.bytephoria.bytesessionrestore.core.service.*;
import team.bytephoria.bytesessionrestore.spi.compression.CompressionStrategy;
import team.bytephoria.bytesessionrestore.spi.storage.UserStorage;

public final class ApplicationFacade {

    private final SessionService sessionService;
    private final SessionCleanupService cleanupService;

    private final UserCache userCache;
    private final UserService userService;
    private final UserSessionService userSessionService;

    public ApplicationFacade(
            final @NotNull UserStorage userStorage,
            final @NotNull CompressionStrategy defaultCompression,
            final int maxPerType
    ) {
        final SessionLimiter sessionLimiter = new SessionLimiter(userStorage);

        this.sessionService = new SessionService(userStorage, defaultCompression);
        this.cleanupService = new SessionCleanupService(sessionLimiter, maxPerType);

        this.userCache = new UserCache();
        this.userService = new UserService(this.userCache, userStorage);

        this.userSessionService = new UserSessionService(
                this.sessionService,
                this.userCache
        );
    }

    public SessionService sessionService() {
        return this.sessionService;
    }

    public SessionCleanupService cleanupService() {
        return this.cleanupService;
    }

    public UserCache userCache() {
        return this.userCache;
    }

    public UserService userService() {
        return this.userService;
    }

    public UserSessionService userSessionService() {
        return this.userSessionService;
    }
}
