package team.bytephoria.bytesessionrestore.platform.paper;

import com.google.gson.Gson;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class PaperPluginLoader implements PluginLoader {

    @Override
    public void classloader(final @NotNull PluginClasspathBuilder pluginClasspathBuilder) {
        final MavenLibraryResolver mavenLibraryResolver = new MavenLibraryResolver();
        final PluginLibraries pluginLibraries = load();

        pluginLibraries.asRepositories().forEach(mavenLibraryResolver::addRepository);
        pluginLibraries.asDependencies().forEach(mavenLibraryResolver::addDependency);

        pluginClasspathBuilder.addLibrary(mavenLibraryResolver);
    }

    public PluginLibraries load() {
        try (InputStream inputStream = getClass().getResourceAsStream("/paper-libraries.json")) {
            assert inputStream != null;
            return new Gson().fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), PluginLibraries.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public record PluginLibraries(Map<String, String> repositories, List<String> dependencies) {

        public Stream<Dependency> asDependencies() {
            return dependencies.stream()
                    .map(dependencies -> new Dependency(new DefaultArtifact(dependencies), null));
        }

        public Stream<RemoteRepository> asRepositories() {
            return repositories.entrySet().stream()
                    .map(repositories -> new RemoteRepository.Builder(repositories.getKey(), "default", repositories.getValue()).build());
        }
    }

}

