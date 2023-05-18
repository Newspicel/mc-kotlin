package net.spaceblock.utils.di

import net.spaceblock.utils.di.annotations.Command
import net.spaceblock.utils.di.annotations.Event
import net.spaceblock.utils.di.annotations.MinecraftController
import net.spaceblock.utils.di.annotations.TabComplete
import org.springframework.context.support.GenericApplicationContext
import java.lang.reflect.Method

class RegistrationService(
    private val ctx: GenericApplicationContext
) {

    fun searchForEventsAndCommands(){
        ctx.getBeansWithAnnotation(MinecraftController::class.java).forEach { (name, bean) ->
            println("Found Controller $name")
            bean.javaClass.methods.forEach { method ->
                val commands = method.getAnnotationsByType(Command::class.java)
                val tabCompletes = method.getAnnotationsByType(TabComplete::class.java)
                val events = method.getAnnotationsByType(Event::class.java)

                if (commands.isEmpty() && events.isEmpty() && tabCompletes.isEmpty()) return@forEach

                // If a method has more than one annotation, it throws an error



                if (tabCompletes.isNotEmpty()) {
                    registerTabComplete(method, tabCompletes)
                }

                if (commands.isNotEmpty()) {
                    registerCommand(method, commands)
                }

                if (events.isNotEmpty()) {
                    registerEvent(method, events)
                }

            }
        }
    }

    fun registerCommand(method: Method, commands: Array<out Command>) {
        commands.forEach { command ->
            println("Registering Command ${command.name}")
        }
    }

    fun registerTabComplete(method: Method, tabCompletes: Array<out TabComplete>) {
        tabCompletes.forEach { tabComplete ->
            println("Registering TabComplete ${tabComplete.label}")
        }
    }

    fun registerEvent(method: Method, events: Array<out Event>) {
        events.forEach { event ->
            println("Registering Event ${event.event.simpleName}")
        }
    }

}
