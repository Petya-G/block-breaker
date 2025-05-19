package blockbreaker

import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.transform.Translate

class Ball(var center: Point2D, var velocity: Point2D, color: Color) {

    companion object {
        val RADIUS = 10.0
    }

    var circle = Circle(center.x, center.y, RADIUS, color)

    fun update(deltaTime: Long) {
        center = Point2D(center.x + deltaTime * velocity.x, center.x + deltaTime * velocity.y)

        println("${center.x} ${center.y}")
        if (center.x <= 0 || center.x >= Game.WIDTH - RADIUS) {
            velocity = Point2D(-velocity.x, velocity.y)
        }

        if (center.y <= 0 || center.y >= Game.HEIGHT - RADIUS) {
            velocity = Point2D(velocity.x, -velocity.y)
        }

        circle.centerX = center.x
        circle.centerY = center.y
    }
}