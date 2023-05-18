package net.spaceblock.utils.di

import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import java.util.logging.Logger

abstract class SpringBootJavaPlugin: JavaPlugin() {

    private lateinit var context: GenericApplicationContext

    final override fun onEnable() {
        this.startSpringBoot()
    }

    final override fun onDisable() {
        context.close()
    }

    private fun startSpringBoot() {
        this.logger.info("Starting Spring Boot")
        context = GenericApplicationContext()

        val beans = beans {
            bean<JavaPlugin> (isPrimary = true) { this@SpringBootJavaPlugin }
            bean<JavaPlugin>(name = "plugin") { this@SpringBootJavaPlugin }
            bean<Logger>(isPrimary = true) { this@SpringBootJavaPlugin.logger }
            bean<ComponentLogger>(isPrimary = true) { this@SpringBootJavaPlugin.componentLogger }
        }

        beans.initialize(context)

        //Search for Beans and @Configuration classes
        context.start()





        this.logger.info("Started Spring Boot")
    }

    // Final Stuff

    final override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        return super.onCommand(sender, command, label, args)
    }

    final override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>?): MutableList<String>? {
        return super.onTabComplete(sender, command, alias, args)
    }

}
