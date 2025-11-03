package team.bytephoria.bytesessionrestore.storage.sql.h2;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.api.Builder;
import team.bytephoria.bytesessionrestore.spi.storage.config.JdbcPoolConfig;

import java.io.File;

public final class H2StorageConnectionData implements Builder<HikariDataSource> {

    private File file;
    private JdbcPoolConfig jdbcPoolConfig;

    public H2StorageConnectionData file(final @NotNull File file) {
        this.file = file;
        return this;
    }

    public H2StorageConnectionData pool(final @NotNull JdbcPoolConfig jdbcPoolConfig) {
        this.jdbcPoolConfig = jdbcPoolConfig;
        return this;
    }

    @Override
    public @NotNull HikariDataSource build() {
        final HikariConfig hikari = new HikariConfig();


        hikari.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");

        final String basePath = this.file.getAbsolutePath().replace('\\', '/');
        final String jdbcUrl =
                "jdbc:h2:file:" + basePath +
                        ";MODE=MySQL" +
                        ";DATABASE_TO_UPPER=FALSE" +
                        ";DB_CLOSE_DELAY=-1";

        hikari.addDataSourceProperty("URL", jdbcUrl);
        hikari.setUsername("sa");
        hikari.setPassword("");

        hikari.setConnectionTimeout(this.jdbcPoolConfig.connectionTimeoutMs());
        hikari.setMaxLifetime(this.jdbcPoolConfig.maxLifetimeMs());
        hikari.setMaximumPoolSize(this.jdbcPoolConfig.maxPoolSize());
        hikari.setMinimumIdle(this.jdbcPoolConfig.minIdle());
        hikari.setPoolName("ByteSessionRestore-H2Pool");

        return new HikariDataSource(hikari);
    }

}
