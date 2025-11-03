package team.bytephoria.bytesessionrestore.platform.paper.util;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.bytephoria.bytesessionrestore.platform.paper.util.exception.NonInstantiableClassException;

public final class MaterialFor {

    private MaterialFor() {
        throw new NonInstantiableClassException();
    }

    public static @NotNull Material safeType(final @Nullable String name) {
        if (name == null || name.isEmpty()) {
            return Material.STONE;
        }

        final String materialName = name.toUpperCase();
        final Material material = Material.getMaterial(materialName);
        return material != null ? material : Material.STONE;
    }

}
