package team.bytephoria.bytesessionrestore.core.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.core.model.UserAggregate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class UserCache {

    private final Cache<UUID, UserAggregate> cache = Caffeine.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();

    public UserAggregate get(final @NotNull UUID id) {
        return this.cache.getIfPresent(id);
    }

    public void put(final @NotNull UUID uuid, final @NotNull UserAggregate userAggregate) {
        this.cache.put(uuid, userAggregate);
    }

}
