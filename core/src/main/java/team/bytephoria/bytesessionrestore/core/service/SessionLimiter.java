package team.bytephoria.bytesessionrestore.core.service;

import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.spi.storage.UserStorage;

import java.util.UUID;

public final class SessionLimiter {

    private final UserStorage userStorage;
    public SessionLimiter(final @NotNull UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void enforce(final @NotNull UUID playerId, final int maxPerType) {
        this.userStorage.cleanUp(playerId, maxPerType);
    }
}
