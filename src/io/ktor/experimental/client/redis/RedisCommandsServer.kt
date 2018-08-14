package io.ktor.experimental.client.redis

import kotlinx.coroutines.experimental.channels.*
import java.util.*

/**
 * Asynchronously rewrite the append-only file
 *
 * https://redis.io/commands/bgrewriteaof
 *
 * @since 1.0.0
 */
suspend fun Redis.bgrewriteaof(): String? = commandString("bgrewriteaof")

/**
 * Asynchronously save the dataset to disk
 *
 * https://redis.io/commands/bgsave
 *
 * @since 1.0.0
 */
suspend fun Redis.bgsave(): String? = commandString("bgsave")

/**
 * Get the current connection name.
 *
 * https://redis.io/commands/client-getname
 *
 * @since 2.6.9
 */
suspend fun Redis.clientGetname(): String? = commandString("client", "getname")

/**
 * Kill the connection of a client
 *
 * https://redis.io/commands/client-kill
 *
 * @since 2.4.0
 */
internal suspend fun Redis.clientKill(todo: Any): Any = TODO()

/**
 * Get the list of client connections
 *
 * https://redis.io/commands/client-list
 *
 * @since 2.4.0
 */
suspend fun Redis.clientList(): List<Map<String, String>> {
    val res = commandString("client", "list") ?: ""
    return res.lines().map {
        it.split(' ').map {
            val parts = it.split('=', limit = 2)
            parts[0] to parts.getOrElse(1) { "" }
        }.toMap()
    }
}

/**
 * Stop processing commands from clients for some time.
 *
 * https://redis.io/commands/client-pause
 *
 * @since 2.9.50
 */
suspend fun Redis.clientPause(timeoutMs: Int): Unit = commandUnit("client", "pause", timeoutMs)

enum class RedisClientReplyMode { ON, OFF, SKIP }

/**
 * Instruct the server whether to reply to commands.
 *
 * https://redis.io/commands/client-reply
 *
 * @since 3.2
 */
// @TODO: This would require some processing from the client.
suspend fun Redis.clientReply(mode: RedisClientReplyMode): Unit = commandUnit("client", "reply", mode.name)

/**
 * Set the current connection name.
 *
 * https://redis.io/commands/client-setname
 *
 * @since 2.6.9
 */
suspend fun Redis.clientSetname(name: String): Unit = commandUnit("client", "setname", name)

/**
 * Get array of Redis command details
 *
 * https://redis.io/commands/command
 *
 * @since 2.8.13
 */
suspend fun Redis.command(): Any = executeText("command")!!

/**
 * Get total number of Redis commands.
 *
 * https://redis.io/commands/command-count
 *
 * @since 2.8.13
 */
suspend fun Redis.commandCount(): Long = commandLong("command", "count")

/**
 * Extract keys given a full Redis command
 *
 * https://redis.io/commands/command-getkeys
 *
 * @since 2.8.13
 */
internal suspend fun Redis.commandGetKeys(todo: Any): Any = TODO()

/**
 * Get array of specific Redis command details
 *
 * https://redis.io/commands/command-info
 *
 * @since 2.8.13
 */
internal suspend fun Redis.commandInfo(vararg names: String): Any = executeText("command", "info", *names)!!

/**
 * Get the value of a configuration parameter
 *
 * https://redis.io/commands/config-get
 *
 * @since 2.0.0
 */
internal suspend fun Redis.configGet(pattern: String): Map<String, String> {
    return commandArrayString("config", "get", pattern).toListOfPairsString().toMap()
}

/**
 * Reset the stats returned by INFO.
 *
 * https://redis.io/commands/config-resetstat
 *
 * @since 2.0.0
 */
suspend fun Redis.configResetStat(): Unit = commandUnit("config", "resetstat")

/**
 * Rewrite the configuration file with the in memory configuration.
 *
 * https://redis.io/commands/config-rewrite
 *
 * @since 2.8.0
 */
suspend fun Redis.configRewrite(): Unit = commandUnit("config", "rewrite")

/**
 * Set a configuration parameter to the given value.
 *
 * https://redis.io/commands/config-set
 *
 * @since 2.0.0
 */
suspend fun Redis.configSet(key: String, value: Any?): Unit = commandUnit("config", "set", key, value)

/**
 * Return the number of keys in the selected database.
 *
 * https://redis.io/commands/dbsize
 *
 * @since 1.0.0
 */
suspend fun Redis.dbsize(): Long = commandLong("dbsize")

/**
 * Get debugging information about a key.
 *
 * https://redis.io/commands/debug-object
 *
 * @since 1.0.0
 */
suspend fun Redis.debugObject(key: String): String? = commandString("debug", "object", key)

/**
 * Make the server crash.
 *
 * https://redis.io/commands/debug-segfault
 *
 * @since 1.0.0
 */
suspend fun Redis.debugSegfault(): String? = commandString("debug", "segfault")

/**
 * Remove all keys from all databases.
 *
 * https://redis.io/commands/flushall
 *
 * @since 1.0.0
 * @since 4.0.0 (async)
 */
suspend fun Redis.flushall(async: Boolean = false) =
    commandUnit("flushall", *arrayOfNotNull(if (async) "async" else null))

/**
 * Remove all keys from the current database.
 *
 * https://redis.io/commands/flushdb
 *
 * @since 1.0.0
 * @since 4.0.0 (async)
 */
suspend fun Redis.flushdb(async: Boolean = false) =
    commandUnit("flushdb", *arrayOfNotNull(if (async) "async" else null))

/**
 * Get information and statistics about the server.
 *
 * https://redis.io/commands/info
 *
 * @since 1.0.0
 */
suspend fun Redis.info(section: String? = null) = commandString("info", *arrayOfNotNull(section))

/**
 * Get the UNIX time stamp of the last successful save to disk.
 *
 * https://redis.io/commands/lastsave
 *
 * @since 1.0.0
 */
suspend fun Redis.lastsave() = Date(commandLong("lastsave") * 1000L)

/**
 * Reports about different memory-related issues that the Redis server experiences,
 * and advises about possible remedies.
 *
 * https://redis.io/commands/memory-doctor
 *
 * @since 4.0.0
 */
suspend fun Redis.memoryDoctor() = commandString("memory", "doctor")

/**
 * Returns a helpful text describing the different subcommands.
 *
 * https://redis.io/commands/memory-help
 *
 * @since 4.0.0
 */
suspend fun Redis.memoryHelp() = commandArrayString("memory", "help")

/**
 * Provides an internal statistics report from the memory allocator.
 *
 * This command is currently implemented only when using jemalloc as an allocator,
 * and evaluates to a benign NOOP for all others.
 *
 * https://redis.io/commands/memory-malloc-stats
 *
 * @since 4.0.0
 */
suspend fun Redis.memoryMallocStats() = commandArrayString("memory", "malloc-stats")

/**
 * Attempts to purge dirty pages so these can be reclaimed by the allocator.
 *
 * This command is currently implemented only when using jemalloc as an allocator,
 * and evaluates to a benign NOOP for all others.
 *
 * https://redis.io/commands/memory-purge
 *
 * @since 4.0.0
 */
suspend fun Redis.memoryPurge() = commandString("memory", "purge")

/**
 * Returns an Array reply about the memory usage of the server.
 *
 * https://redis.io/commands/memory-stats
 *
 * @since 4.0.0
 */
suspend fun Redis.memoryStats(): Any? = executeText("memory", "stats")

/**
 * Reports the number of bytes that a key and its value require to be stored in RAM.
 *
 * https://redis.io/commands/memory-usage
 *
 * @since 4.0.0
 */
suspend fun Redis.memoryUsage(key: String, samplesCount: Long? = null) =
    commandLong("memory", "usage", key, *(if (samplesCount != null) arrayOf("samples", samplesCount) else arrayOf()))

// @TODO: This changes the client state too
/**
 * Streams back every command processed by the Redis server.
 * It can help in understanding what is happening to the database.
 * This command can both be used via redis-cli and via telnet.
 *
 * https://redis.io/commands/monitor
 *
 * @since 1.0.0
 */
internal suspend fun Redis.monitor(): Channel<String> = TODO()

/**
 * Provide information on the role of a Redis instance in the context of replication,
 * by returning if the instance is currently a master, slave, or sentinel.
 * The command also returns additional information about the state of the replication (if the role is master or slave)
 * or the list of monitored master names (if the role is sentinel).
 *
 *
 * https://redis.io/commands/role
 *
 * @since 2.8.12
 */
suspend fun Redis.role(): Any? = executeText("role")

/**
 * Performs a synchronous save of the dataset producing a point in time snapshot of all the data inside
 * the Redis instance, in the form of an RDB file.
 *
 * https://redis.io/commands/save
 *
 * @since 1.0.0
 */
suspend fun Redis.save(): Long = commandLong("save")

/**
 * Internal command used for replication
 *
 * https://redis.io/commands/sync
 *
 * @since 1.0.0
 */
internal suspend fun Redis.sync(): Unit = commandUnit("sync")

/**
 * Returns the current server time as a two items lists: a Unix timestamp and the
 * amount of microseconds already elapsed in the current second.
 * Basically the interface is very similar to the one of the gettimeofday system call.
 *
 * https://redis.io/commands/time
 *
 * @since 2.6.0
 */
suspend fun Redis.time(): Pair<Date, Long> {
    val res = commandArrayLong("time")
    val unixSeconds = res.getOrElse(0) { 0 }
    val microSeconds = res.getOrElse(1) { 0 }
    return Date(unixSeconds * 1000L + (microSeconds / 1000L)) to (microSeconds % 1000L)
}

/**
 * Synchronously save the dataset to disk and then shut down the server.
 *
 * https://redis.io/commands/shutdown
 *
 * @since 1.0.0
 */
suspend fun Redis.shutdown(save: Boolean = true): Unit = commandUnit("shutdown", if (save) "save" else "nosave")

/**
 * Make the server a slave of another instance, or promote it as master.
 *
 * https://redis.io/commands/slaveof
 *
 * @since 1.0.0
 */
suspend fun Redis.slaveof(host: String, port: Int): Unit = commandUnit("slaveof", host, port)

/**
 * Manages the Redis slow queries log.
 *
 * https://redis.io/commands/slowlog
 *
 * @since 2.2.12
 */
suspend fun Redis.slowlog(subcommand: String, vararg args: Any?): Any? = executeText("slowlog", subcommand, *args)