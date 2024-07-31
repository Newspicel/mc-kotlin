@file:Suppress("Unused")

package dev.newspicel.itemstack

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.Component.translatable
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

inline fun itemStack(material: Material, builder: ItemStack.() -> Unit) = ItemStack(material).apply(builder)

inline fun <reified T : ItemMeta> ItemStack.meta(builder: T.() -> Unit) {
    val curMeta = itemMeta as? T
    itemMeta = if (curMeta != null) {
        curMeta.apply(builder)
        curMeta
    } else {
        itemMeta(type, builder)
    }
}

@JvmName("simpleMeta")
inline fun ItemStack.meta(builder: ItemMeta.() -> Unit) = meta<ItemMeta>(builder)

inline fun <reified T : ItemMeta> ItemStack.setMeta(builder: T.() -> Unit) {
    itemMeta = itemMeta(type, builder)
}

@JvmName("simpleSetMeta")
inline fun ItemStack.setMeta(builder: ItemMeta.() -> Unit) = setMeta<ItemMeta>(builder)

inline fun <reified T : ItemMeta> itemMeta(material: Material, builder: T.() -> Unit): T? {
    val meta = Bukkit.getItemFactory().getItemMeta(material)
    return if (meta is T) meta.apply(builder) else null
}

@JvmName("simpleItemMeta")
inline fun itemMeta(material: Material, builder: ItemMeta.() -> Unit) = itemMeta<ItemMeta>(material, builder)

inline fun ItemMeta.setLore(builder: ItemMetaLoreBuilder.() -> Unit) {
    lore(ItemMetaLoreBuilder().apply(builder).lorelist)
}

inline fun ItemMeta.addLore(builder: ItemMetaLoreBuilder.() -> Unit) {
    val newLore = lore() ?: mutableListOf<Component>()
    newLore.addAll(ItemMetaLoreBuilder().apply(builder).lorelist)
    lore(newLore)
}

class ItemMetaLoreBuilder {
    val lorelist = ArrayList<Component>()

    operator fun Component.unaryPlus() {
        lorelist += this
    }

    operator fun String.unaryPlus() {
        lorelist += text(this)
    }
}

fun ItemMeta.flag(itemFlag: ItemFlag) = addItemFlags(itemFlag)

fun ItemMeta.flags(vararg itemFlag: ItemFlag) = addItemFlags(*itemFlag)

fun ItemMeta.removeFlag(itemFlag: ItemFlag) = removeItemFlags(itemFlag)

fun ItemMeta.removeFlags(vararg itemFlag: ItemFlag) = removeItemFlags(*itemFlag)

var ItemMeta.name: Component?
    get() = if (hasDisplayName()) displayName() else null
    set(value) = displayName(value ?: Component.space())

var ItemMeta.customModel: Int?
    get() = if (hasCustomModelData()) customModelData else null
    set(value) = setCustomModelData(value)

var ItemMeta.localName: TranslatableComponent
    get() = if (hasDisplayName()) displayName() as TranslatableComponent else translatable("")
    set(value) = displayName(value)

fun toLoreList(string: String, lineColor: TextColor = TextColor.color(0xFFFFFF), vararg lineDecorations: TextDecoration = arrayOf(), lineLength: Int = 40): List<Component> {
    val loreList = ArrayList<Component>()
    val lineBuilder = StringBuilder()
    fun submitLine() {
        loreList += text(lineBuilder.toString()).color(lineColor).decorations(lineDecorations.toMutableSet(), true)
        lineBuilder.clear()
    }

    fun addWord(word: String) {
        if (lineBuilder.lengthWithoutMinecraftColour + word.lengthWithoutMinecraftColour > lineLength) {
            submitLine()
        }

        if (lineBuilder.isNotEmpty()) {
            lineBuilder.append(" ")
        }

        lineBuilder.append(word)
    }

    string.split(" ").forEach { addWord(it) }

    if (lineBuilder.isNotEmpty()) {
        submitLine()
    }

    return loreList
}

val CharSequence.lengthWithoutMinecraftColour: Int
    get() {
        var count = 0
        var isPreviousColourCode = false

        this.forEachIndexed { index, char ->
            if (isPreviousColourCode) {
                isPreviousColourCode = false
                return@forEachIndexed
            }

            if (char == '§') {
                if (lastIndex >= index + 1) {
                    val nextChar = this[index + 1]
                    if (nextChar.isLetter() || nextChar.isDigit()) {
                        isPreviousColourCode = true
                    } else {
                        count++
                    }
                }
            } else {
                count++
            }
        }

        return count
    }
