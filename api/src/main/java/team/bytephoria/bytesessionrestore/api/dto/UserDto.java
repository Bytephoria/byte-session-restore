package team.bytephoria.bytesessionrestore.api.dto;

import java.util.List;
import java.util.UUID;

/**
 * Represents a data transfer object for user-related session data.
 * It contains the user's unique identifier and all associated sessions.
 */
public record UserDto(
        UUID userId,
        List<SessionDto> sessionDto
) {
}
