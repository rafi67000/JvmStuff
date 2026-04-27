package xyz.rafi67000.titanspigot.debug

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        server.getPluginCommand("debug").executor = this
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (!(sender.hasPermission("titan.debug") || sender.isOp)) {
            sender.sendMessage(ChatColor.RED.toString() + "You don't have permission to use this command!")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage(ChatColor.RED.toString() + "Usage: /debug <class>")
            return true
        }

        val klass = args[0]

        runCatching {
            sender.sendMessage(Class.forName(klass).describe())
        }.onFailure {
            sender.sendMessage(ChatColor.RED.toString() + "Class not found")
        }

        return true
    }

    fun Class<*>.describe(): String {

        val fields = declaredFields.joinToString("\n") {
        "&a${it.name} &8- &b${it.type.simpleName} &8- &7synthetic=${if (it.isSynthetic) "&atrue" else "&cfalse"}"
        }.let { if (it.isEmpty()) "&7None" else "\n" + it }

        val methods = declaredMethods.joinToString("\n") { m ->
            val params = m.parameterTypes.joinToString(", ") { it.simpleName }

            "&a${m.name} &8- &b${m.returnType.simpleName}(&7$params&b) &8- &7flags=${
                m.accessFlags().joinToString("&7, ") { "&e" + it.name }
            }"
        }.let { if (it.isEmpty()) "&7None" else "\n" + it }

        val format = """
        &7Class: &a$name
        &7Superclass: &b${superclass?.name}
        &7Interfaces: &b${interfaces.joinToString("&8,") { "&3" + it.name }.ifEmpty { "&7None" }}
        &7Fields: %s
        &7Methods: %s
    """.trimIndent().format(fields, methods)

        return ChatColor.translateAlternateColorCodes('&',"&eDescribing class:\n\n$format\n")
    }
}