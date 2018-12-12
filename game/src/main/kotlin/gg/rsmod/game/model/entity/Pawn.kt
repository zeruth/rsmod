package gg.rsmod.game.model.entity

import gg.rsmod.game.model.*
import gg.rsmod.game.model.path.strategy.AStarPathfindingStrategy
import gg.rsmod.game.model.region.Chunk
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.game.sync.UpdateBlockBuffer

/**
 * A controllable character in the world that is used by something, or someone,
 * for their own purpose.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class Pawn(open val world: World) : Entity() {

    /**
     * The index assigned when this [Pawn] is successfully added to a [PawnList].
     */
    var index = -1

    var blockBuffer = UpdateBlockBuffer()

    /**
     * The 3D [Tile] that this [Pawn] was standing on, in the last game cycle.
     */
    var lastTile: Tile? = null

    var chunk: Chunk? = null

    /**
     * Whether or not this pawn can teleported this game cycle.
     */
    var teleport = false

    val movementQueue by lazy { MovementQueue(this) }

    /**
     * The current directions that this [Pawn] is moving.
     */
    var steps: MovementQueue.StepDirection? = null

    /**
     * The last [Direction] this pawn was facing.
     */
    var lastFacingDirection: Direction = Direction.NONE

    /**
     * The current [LockState] which filters what actions this [Pawn] can perform.
     */
    var lock = LockState.NONE

    /**
     * The attributes attached to the [Pawn].
     *
     * @see AttributeSystem
     */
    val attr = AttributeSystem()

    /**
     * Handles logic before any synchronization tasks are executed.
     */
    abstract fun cycle()

    abstract fun isDead(): Boolean

    abstract fun isRunning(): Boolean

    fun canMove(): Boolean = !isDead() && lock.canMove()

    fun walkTo(x: Int, z: Int, type: MovementQueue.StepType) {
        val pathfinding = AStarPathfindingStrategy(world.collision)
        val path = pathfinding.getPath(tile, Tile(x, z, tile.height), getType())

        movementQueue.clear()
        path.forEach { tile ->
            movementQueue.addStep(tile, type)
        }
    }

    fun teleport(x: Int, z: Int, height: Int = 0) {
        teleport = true
        tile = Tile(x, z, height)
        movementQueue.clear()
    }

    fun teleport(tile: Tile) {
        teleport(tile.x, tile.z, tile.height)
    }

    /**
     * Executes a plugin with this [Pawn] as its context.
     */
    fun executePlugin(plugin: Function1<Plugin, Unit>) {
        world.pluginExecutor.execute(this, plugin)
    }

    /**
     * Terminates any on-going plugins that are being executed by this [Pawn].
     */
    fun interruptPlugins() {
        world.pluginExecutor.interruptPluginsWithContext(this)
    }
}
