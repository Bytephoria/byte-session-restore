package team.bytephoria.bytesessionrestore.core.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;
import team.bytephoria.bytesessionrestore.api.model.event.SessionEventType;
import team.bytephoria.bytesessionrestore.api.model.session.SessionRecord;
import team.bytephoria.bytesessionrestore.api.model.user.User;

import java.util.*;

public final class UserAggregate implements User {

    private final UUID userId;
    private final Map<SessionEventType, List<LazySessionRecord>> lazySessionsMap;

    public UserAggregate(
            final @NotNull UUID userId,
            final @NotNull Map<SessionEventType, List<LazySessionRecord>> lazySessionsMap
    ) {
        this.userId = userId;
        this.lazySessionsMap = lazySessionsMap;
    }

    public UserAggregate(final @NotNull UUID userId) {
        this(userId, new EnumMap<>(SessionEventType.class));
    }

    public void addSession(final @NotNull SessionEventType sessionEventType, final @NotNull LazySessionRecord lazySessionRecord) {
        this.lazySessionsMap
                .computeIfAbsent(sessionEventType, ignored -> new ArrayList<>())
                .add(lazySessionRecord);
    }

    @Override
    public @NotNull UUID userId() {
        return this.userId;
    }

    public Map<SessionEventType, List<LazySessionRecord>> lazySessionsMap() {
        return this.lazySessionsMap;
    }

    @Override
    public @NotNull @UnmodifiableView Map<SessionEventType, List<SessionRecord>> sessions() {
        final Map<SessionEventType, List<SessionRecord>> view = new EnumMap<>(SessionEventType.class);

        for (final var entry : this.lazySessionsMap.entrySet()) {
            final List<SessionRecord> records = entry.getValue()
                    .stream()
                    .map(LazySessionRecord::delegate)
                    .toList();

            view.put(entry.getKey(), records);
        }

        return view;
    }

    @Override
    public @NotNull @Unmodifiable List<SessionRecord> sessionsOf(final @NotNull SessionEventType sessionEventType) {
        return this.lazySessionsMap().getOrDefault(sessionEventType, Collections.emptyList())
                .stream()
                .map(LazySessionRecord::delegate)
                .toList();
    }

    @Override
    public boolean hasSessionsOf(final @NotNull SessionEventType sessionEventType) {
        return this.lazySessionsMap.containsKey(sessionEventType);
    }

}
