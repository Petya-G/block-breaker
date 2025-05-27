package blockbreaker

import javafx.geometry.Point2D
import javafx.scene.shape.Shape

abstract class Entity(
    var direction: Point2D = Point2D(0.0, 0.0), var speed: Double = 0.0
) {
    abstract var position: Point2D

    val velocity: Point2D
        get() = direction * speed

    abstract var shape: Shape
}