# Itemstack

Builder to create an itemstack.

```kotlin
val itemStack = itemStack(Material.AIR) {
  meta {
    name = text("Test")
    customModel = 1

    setLore {
      +text("Test")
      +text("Test")
    }

    flag(ItemFlag.HIDE_ENCHANTS)
  }
}



toLoreList("Hallo, ich bin ein Test") // List<String>
// Limits the length of the string to 40 characters per line.
```
