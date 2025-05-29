package blockbreaker

import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape

open class Block(position: Point2D, var color: Color) : Entity() {
    override var position: Point2D
        get() = Point2D(rectangle.x, rectangle.y)
        set(value) {
            rectangle.x = value.x
            rectangle.y = value.y
        }

    companion object {
        const val WIDTH = 80.0
        const val HEIGHT = 40.0
        const val ARC = 18.0
    }

    open var rectangle = Rectangle(position.x, position.y, WIDTH, HEIGHT).apply {
        fill = color
        arcWidth = ARC
        arcHeight = ARC
    }

    override var shape: Shape = rectangle

    open fun hit(){
    }
}

class BallBlock(position: Point2D) : Block(position, Color.GREEN){
    override fun hit() {
       Game.balls.add(Ball(position, color))
    }
}