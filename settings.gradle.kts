rootProject.name = "byte-session-restore"

include(
    "api",
    "spi",
    "providers:serializer",
    "providers:compression",
    "providers:compression:none",
    "providers:compression:zstd",
    "providers:storage-sql",
    "providers:storage-sql:h2",
    "infra:config",
    "infra:bootstrap",
    "core",
    "platform:common-bukkit",
    "platform:paper"
)
