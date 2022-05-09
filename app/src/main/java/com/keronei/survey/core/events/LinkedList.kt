package com.keronei.survey.core.events

class LinkedList<T> {
    private var head: EventNode<T>? = null
    private var tail: EventNode<T>? = null
    private var size = 0

    fun isEmpty(): Boolean {
        return size == 0
    }

    override fun toString(): String {
        return if (isEmpty()) {
            "Empty"
        } else {
            head.toString()
        }
    }

    // Add at the front of the list
    fun push(value: T) {
        head = EventNode(value = value, next = head)
        if (tail == null) {
            tail = head
        }
        size++
    }

    // remove at the front
    fun pop(): T? {
        if (!isEmpty()) size--
        val result = head?.value
        head = head?.next

        if (isEmpty()) {
            tail = null
        }

        return result
    }

    // Add at the end of the list
    fun append(value: T) {
        if (isEmpty()) {
            push(value)
            return
        }

        tail?.next = EventNode(value = value)
        tail = tail?.next
        size++
    }

    fun removeLast(): T? {
        val head = head ?: return null
        if (head.next == null) pop()
        size--
        var previous = head
        var current = head

        var next = current.next
        while (next != null) {
            previous = current
            current = next
            next = current.next
        }

        previous.next = null
        tail = previous

        return current.value
    }

    fun eventNodeAt(index: Int): EventNode<T>? {
        var currentEventNode = head
        var currentIndex = 0
        while (currentEventNode != null && currentIndex < index) {
            currentEventNode = currentEventNode.next
            currentIndex++
        }

        return currentEventNode
    }

    // Add after a particular node in the list - specified index
    fun insert(value: T, afterEventNode: EventNode<T>): EventNode<T> {
        if (tail == afterEventNode) {
            append(value)
            return tail!!
        }

        val newEventNode = EventNode(value = value, next = afterEventNode.next)

        afterEventNode.next = newEventNode
        size++
        return newEventNode
    }

    fun removeAfter(eventNode: EventNode<T>): T? {
        val result = eventNode.next?.value
        if (eventNode.next == tail) {
            tail = eventNode
        }
        if (eventNode.next != null) {
            size--
        }
        eventNode.next = eventNode.next?.next
        return result
    }
}