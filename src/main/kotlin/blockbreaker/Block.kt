package blockbreaker

import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class Block(var position: Point2D, var color: Color) {
    companion object {
        val WIDTH = 40.0
        val HEIGHT = 20.0
    }

    var rectangle = Rectangle(position.x, position.y, WIDTH, HEIGHT).apply { fill = color }

    fun hit() {

    }
}