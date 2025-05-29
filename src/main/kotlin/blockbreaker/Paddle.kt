package blockbreaker

import blockbreaker.Game.Companion.currentlyActiveKeys
import javafx.geometry.Point2D
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape

class Paddle(position: Point2D = Point2D(Game.WIDTH.toDouble() / 2.0, Game.HEIGHT.toDouble() - 50.0)) : Entity(
    direction = Point2D(0.0, 0.0), speed = 0.5
) {

    override var position: Point2D
        get() = Point2D(rectangle.x, rectangle.y)
        set(value) {
            rectangle.x = value.x
            rectangle.y = value.y
        }

    companion object {
        private const val WIDTH = 170.0
        private const val HEIGHT = 20.0
    }

    var rectangle = Rectangle(
        position.x - WIDTH / 2.0, position.y - HEIGHT / 2.0, WIDTH, HEIGHT
    )

    override var shape: Shape = rectangle

    fun update(deltaTime: Double) {
        rectangle.fill = Color.WHITE
        direction = Point2D(0.0, 0.0)

        if (currentlyActiveKeys.contains(KeyCode.LEFT) && position.x > 0.0) {
            direction = Point2D(-1.0, 0.0)
        }

        if (currentlyActiveKeys.contains(KeyCode.RIGHT) && position.x < (Game.WIDTH.toDouble() - rectangle.width)) {
            direction = Point2D(1.0, 0.0)
        }

        ::position += velocity * deltaTime
    }
}
