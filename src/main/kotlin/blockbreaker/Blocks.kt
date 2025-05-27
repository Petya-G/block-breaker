package blockbreaker

import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.paint.Color
import java.util.function.IntFunction

class Blocks(var root: Group) : MutableList<Block> by mutableListOf() {

    private val blocks = mutableListOf<Block>()

    init {
        val rows = 4
        val cols = 7
        val blockSpacingX = 10.0
        val blockSpacingY = 8.0
        val startX = (Game.WIDTH - (cols * Block.WIDTH + (cols - 1) * blockSpacingX)) / 2
        val startY = 40.0

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val x = startX + col * (Block.WIDTH + blockSpacingX)
                val y = startY + row * (Block.HEIGHT + blockSpacingY)
                val block = Block(Point2D(x, y), Color.hsb((col * 360 / cols).toDouble(), 0.7, 0.9))
                add(block)
            }
        }
    }

    override fun remove(element: Block): Boolean {
        blocks.remove(element)
        root.children.remove(element.rectangle)
        return true
    }

    fun removeAll(blocksToRemove: MutableList<Block>) {
        blocks.removeAll(blocksToRemove)
        root.children.removeAll(blocksToRemove.map { it.rectangle })
    }

    override val size: Int
        get() = blocks.size

    override fun contains(element: Block): Boolean = blocks.contains(element)

    override fun containsAll(elements: Collection<Block>): Boolean = blocks.containsAll(elements)

    override fun get(index: Int): Block = blocks[index]

    override fun indexOf(element: Block): Int = blocks.indexOf(element)

    override fun isEmpty(): Boolean = blocks.isEmpty()

    override fun iterator(): MutableIterator<Block> = object : MutableIterator<Block> {
        private val it = blocks.iterator()
        private var last: Block? = null

        override fun hasNext(): Boolean = it.hasNext()

        override fun next(): Block {
            last = it.next()
            return last!!
        }

        override fun remove() {
            val l = last ?: throw IllegalStateException()
            this@Blocks.remove(l)
            last = null
        }
    }

    override fun lastIndexOf(element: Block): Int = blocks.lastIndexOf(element)

    override fun add(element: Block): Boolean {
        blocks.add(element)
        root.children.add(element.rectangle)
        return true
    }

    override fun add(index: Int, element: Block) {
        blocks.add(index, element)
        root.children.add(element.rectangle)
    }

    override fun addAll(index: Int, elements: Collection<Block>): Boolean {
        val result = blocks.addAll(index, elements)
        elements.forEach { root.children.add(it.rectangle) }
        return result
    }

    override fun addAll(elements: Collection<Block>): Boolean {
        val result = blocks.addAll(elements)
        elements.forEach { root.children.add(it.rectangle) }
        return result
    }

    override fun clear() {
        blocks.forEach { root.children.remove(it.rectangle) }
        blocks.clear()
    }

    override fun listIterator(): MutableListIterator<Block> = blocks.listIterator()

    override fun listIterator(index: Int): MutableListIterator<Block> = blocks.listIterator(index)

    override fun removeAt(index: Int): Block {
        val block = blocks.removeAt(index)
        root.children.remove(block.rectangle)
        return block
    }

    override fun set(index: Int, element: Block): Block {
        val old = blocks.set(index, element)
        root.children.remove(old.rectangle)
        root.children.add(element.rectangle)
        return old
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<Block> = blocks.subList(fromIndex, toIndex)
    override fun <T : Any?> toArray(generator: IntFunction<Array<out T?>?>): Array<out T?>? {
        return super.toArray(generator)
    }
}