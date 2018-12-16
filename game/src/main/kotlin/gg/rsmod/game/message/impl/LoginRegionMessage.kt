package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message
import gg.rsmod.game.model.Tile
import gg.rsmod.game.service.xtea.XteaKeyService

/**
 * @author Tom <rspsmods@gmail.com>
 */
class LoginRegionMessage(val playerIndex: Int, val tile: Tile, val xteaKeyService: XteaKeyService?) : Message