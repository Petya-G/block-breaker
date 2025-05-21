package blockbreaker

import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class Ball(center: Point2D, speed: Double = 1.0, color: Color = Color.WHITE) {

    companion object {
        val RADIUS = 10.0
    }

    var center: Point2D
        get() = Point2D(circle.centerX, circle.centerY)
        set(value) {
            circle.centerX = value.x
            circle.centerY = value.y
        }

    var velocity : Point2D = Point2D(0.0, speed)

    var circle = Circle(center.x, center.y, RADIUS, color)

    fun update(deltaTime: Double) {
        ::center += velocity * deltaTime
        circle.clamp()
    }
}