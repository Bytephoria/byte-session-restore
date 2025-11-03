package team.bytephoria.bytesessionrestore.infra.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigurationLoader {

    private ConfigurationLoader() {}

    public static @NotNull AbstractConfigurationLoader<@NotNull CommentedConfigurationNode> createConfiguration(
            final @NotNull String fileName
    ) {
        final File file = new File(fileName);
        return YamlConfigurationLoader.builder()
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .file(file)
                .build();
    }

    public static <T> @Nullable T loadConfiguration(
            final @NotNull String fileName,
            final @NotNull Class<T> typeClass,
            final boolean copyFromResources
    ) {
        if (copyFromResources) {
            copyFromResourcesIfMissing(fileName, true);
        }

        final AbstractConfigurationLoader<@NotNull CommentedConfigurationNode> configurationLoader =
                createConfiguration(fileName);

        try {
            final CommentedConfigurationNode commentedConfigurationNode = configurationLoader.load();
            final T config = commentedConfigurationNode.get(typeClass);

            commentedConfigurationNode.set(typeClass, config);

            if (!copyFromResources) {
                configurationLoader.save(commentedConfigurationNode);
            }

            return config;
        } catch (ConfigurateException e) {
            throw new RuntimeException("Failed to load configuration: " + fileName, e);
        }
    }

    public static void copyFromResourcesIfMissing(
            final @NotNull String resourcePath,
            final boolean createEmptyIfMissing
    ) {
        final File targetFile = new File(resourcePath);
        final Path targetPath = targetFile.toPath();

        if (targetFile.exists()) {
            return;
        }

        try {
            final File parent = targetFile.getParentFile();
            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    throw new IOException("Failed to create parent directories for " + targetFile);
                }
            }

            try (InputStream inputStream = ConfigurationLoader.class.getClassLoader().getResourceAsStream(targetFile.getName())) {
                if (inputStream != null) {
                    try (OutputStream outputStream = Files.newOutputStream(targetPath)) {
                        inputStream.transferTo(outputStream);
                    }
                } else if (createEmptyIfMissing) {
                    Files.createFile(targetPath);
                } else {
                    throw new FileNotFoundException("Resource not found in JAR and empty creation disabled: " + resourcePath);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to create or copy configuration file: " + resourcePath, e);
        }
    }
}
