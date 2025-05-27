package blockbreaker

import javafx.scene.Group

class EntityList<T : Entity>(var root: Group) : MutableList<T> {
    private val entities = mutableListOf<T>()

    override fun add(element: T): Boolean {
        val result = entities.add(element)
        root.children.add(element.shape)
        return result
    }

    override fun remove(element: T): Boolean {
        val result = entities.remove(element)
        root.children.remove(element.shape)
        return result
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val result = entities.addAll(elements)
        elements.forEach { root.children.add(it.shape) }
        return result
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val result = entities.addAll(index, elements)
        elements.forEach { root.children.add(it.shape) }
        return result
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        val result = entities.removeAll(elements.toSet())
        elements.forEach { root.children.remove(it.shape) }
        return result
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val toRemove = entities.filterNot { it in elements }
        val result = entities.retainAll(elements.toSet())
        toRemove.forEach { root.children.remove(it.shape) }
        return result
    }

    override fun clear() {
        entities.forEach { root.children.remove(it.shape) }
        entities.clear()
    }

    override fun set(index: Int, element: T): T {
        val old = entities[index]
        root.children.remove(old.shape)
        entities[index] = element
        root.children.add(element.shape)
        return old
    }

    override fun add(index: Int, element: T) {
        entities.add(index, element)
        root.children.add(element.shape)
    }

    override fun removeAt(index: Int): T {
        val element = entities.removeAt(index)
        root.children.remove(element.shape)
        return element
    }

    override fun listIterator(): MutableListIterator<T> {
        return entities.listIterator()
    }

    override fun listIterator(index: Int): MutableListIterator<T> {
        return entities.listIterator(index)
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        return entities.subList(fromIndex, toIndex)
    }

    override val size: Int
        get() = entities.size

    override fun isEmpty(): Boolean {
        return entities.isEmpty()
    }

    override fun contains(element: T): Boolean {
        return entities.contains(element)
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return entities.containsAll(elements)
    }

    override fun get(index: Int): T {
        return entities[index]
    }

    override fun indexOf(element: T): Int {
        return entities.indexOf(element)
    }

    override fun lastIndexOf(element: T): Int {
        return entities.lastIndexOf(element)
    }

    override fun iterator(): MutableIterator<T> {
        return entities.iterator()
    }
}