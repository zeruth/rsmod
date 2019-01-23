
import gg.rsmod.plugins.osrs.api.helper.*
import gg.rsmod.plugins.osrs.content.inter.keybind.Hotkey
import gg.rsmod.plugins.osrs.content.inter.keybind.KeyBinding


/**
 * Set 'focused' hotkey.
 */
Hotkey.values().forEach { hotkey ->
    r.bindButton(parent = 121, child = hotkey.child) {
        it.player().setVarbit(KeyBinding.FOCUSED_HOTKEY_VARBIT, hotkey.id)
    }
}

/**
 * Set hotkey value.
 */
r.bindButton(parent = 121, child = 111) {
    val p = it.player()
    val focused = p.getVarbit(KeyBinding.FOCUSED_HOTKEY_VARBIT)
    val hotkey = Hotkey.values().firstOrNull { h -> h.id == focused } ?: return@bindButton

    val slot = it.getInteractingSlot()
    KeyBinding.set(p, hotkey, slot)
}

/**
 * Restore defaults.
 */
r.bindButton(parent = 121, child = 104) {
    val p = it.player()

    Hotkey.values().forEach { hotkey ->
        p.setVarbit(hotkey.varbit, hotkey.defaultValue)
    }
}

r.bindButton(parent = 121, child = 103) {
    it.player().toggleVarbit(KeyBinding.ESC_CLOSES_INTERFACES)
}