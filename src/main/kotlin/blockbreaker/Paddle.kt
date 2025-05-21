package blockbreaker

import blockbreaker.Game.Companion.currentlyActiveKeys
import javafx.geometry.Point2D
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class Paddle(
    center: Point2D = Point2D(2.0 / 3.0 * Game.WIDTH.toDouble(), 4.0 / 5.0 * Game.HEIGHT.toDouble()),
    var velocity: Point2D = Point2D(0.0, 0.0)
) {

    var center: Point2D
        get() = Point2D(rectangle.x, rectangle.y)
        set(value) {
            rectangle.x = value.x
            rectangle.y = value.y
        }

    var rectangle = Rectangle(center.x, center.y, 100.0, 20.0)

    fun update(deltaTime: Double) {
        rectangle.fill = Color.WHITE
        velocity = Point2D(0.0, 0.0)

        if (currentlyActiveKeys.contains(KeyCode.LEFT) && center.x > 0.0)
            velocity = Point2D(-0.3, 0.0)

        if (currentlyActiveKeys.contains(KeyCode.RIGHT) && center.x < (Game.WIDTH.toDouble() - rectangle.width))
            velocity = Point2D(0.3, 0.0)


        ::center += velocity * deltaTime


        if (currentlyActiveKeys.contains(KeyCode.UP)) {
        }
    }
}
