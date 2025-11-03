package team.bytephoria.bytesessionrestore.infra.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public final class Configuration {

    @Setting("storage")
    private Storage storage = new Storage();

    @Setting("settings")
    private Settings settings = new Settings();

    @Setting("session")
    private Session session = new Session();

    public Storage storage() {
        return this.storage;
    }

    public Settings settings() {
        return this.settings;
    }

    public Session session() {
        return this.session;
    }

    @ConfigSerializable
    public static final class Settings {

        @Setting("serializer")
        private String serializer = "MINI_MESSAGE";

        public String serializer() {
            return this.serializer;
        }
    }

    @ConfigSerializable
    public static final class Storage {

        @Setting("type")
        private String type = "H2";

        @Setting("pool")
        private Pool pool = new Pool();

        @Setting("credentials")
        private Credentials credentials = new Credentials();

        @Setting("h2")
        private H2 h2 = new H2();

        public String type() {
            return this.type;
        }

        public Pool pool() {
            return this.pool;
        }

        public Credentials credentials() {
            return this.credentials;
        }

        public H2 h2() {
            return this.h2;
        }

        @ConfigSerializable
        public static final class Pool {

            @Setting("connection-timeout")
            private int connectionTimeout = 5000;

            @Setting("max-lifetime")
            private int maxLifetime = 1800000;

            @Setting("max-pool-size")
            private int maxPoolSize = 10;

            @Setting("min-idle")
            private int minIdle = 1;

            public int connectionTimeout() {
                return this.connectionTimeout;
            }

            public int maxLifetime() {
                return this.maxLifetime;
            }

            public int maxPoolSize() {
                return this.maxPoolSize;
            }

            public int minIdle() {
                return this.minIdle;
            }
        }

        @ConfigSerializable
        public static final class Credentials {

            @Setting("host")
            private String host = "localhost";

            @Setting("port")
            private int port = 3306;

            @Setting("database")
            private String database = "bytesessionstore";

            @Setting("username")
            private String username = "root";

            @Setting("password")
            private String password = "ilovefory";

            @Setting("use-ssl")
            private boolean useSSL = false;

            public String host() {
                return this.host;
            }

            public int port() {
                return this.port;
            }

            public String database() {
                return this.database;
            }

            public String username() {
                return this.username;
            }

            public String password() {
                return this.password;
            }

            public boolean useSSL() {
                return this.useSSL;
            }
        }

        @ConfigSerializable
        public static final class H2 {

            @Setting("file")
            private String file = "data/sessions";

            public String file() {
                return this.file;
            }
        }
    }

    @ConfigSerializable
    public static final class Session {

        @Setting("ignore-empty-inventory")
        private boolean ignoreEmptyInventory = true;

        @Setting("max-per-type")
        private int maxPerType = 50;

        @Setting("compression")
        private String compression = "ZSTD";

        @Setting("cleanup-interval-seconds")
        private int cleanupIntervalSeconds = 12000;

        public boolean ignoreEmptyInventory() {
            return this.ignoreEmptyInventory;
        }

        public int maxPerType() {
            return this.maxPerType;
        }

        public String compression() {
            return this.compression;
        }

        public int cleanupIntervalSeconds() {
            return this.cleanupIntervalSeconds;
        }
    }
}