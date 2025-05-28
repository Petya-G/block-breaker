package blockbreaker

import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Shape
import kotlin.random.Random

class Ball(
    position: Point2D,
    direction: Point2D = Point2D(if (Random.nextBoolean()) 0.5 else -0.5, -0.35).normalize(),
    color: Color = Color.WHITE,
    speed: Double = 0.8 + 0.2 * Game.stage
) : Entity(direction, speed) {

    companion object {
        val RADIUS = 20.0
    }

    override var position: Point2D
        get() = Point2D(circle.centerX, circle.centerY)
        set(value) {
            circle.centerX = value.x
            circle.centerY = value.y
        }

    var circle = Circle(position.x, position.y, RADIUS, color)

    override var shape: Shape = circle

    fun update(deltaTime: Double) {
        ::position += velocity * deltaTime
        circle.clamp()
    }
}