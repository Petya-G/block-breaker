package blockbreaker

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Stage

class Game : Application() {

    companion object {
        const val WIDTH = 512
        const val HEIGHT = 512
        val currentlyActiveKeys = mutableSetOf<KeyCode>()
        private val gameBounds = Rectangle(0.0, 0.0, WIDTH.toDouble(), HEIGHT.toDouble())
    }

    private lateinit var mainScene: Scene
    private lateinit var graphicsContext: GraphicsContext

    private lateinit var space: Image

    private var lastFrameTime: Long = System.nanoTime()

    var paddle = Paddle()
    var ball = Ball(Point2D(WIDTH / 2.0, HEIGHT / 2.0))
    override fun start(mainStage: Stage) {
        mainStage.title = "Block breaker"
        val root = Group()
        mainScene = Scene(root)
        mainStage.scene = mainScene

        val canvas = Canvas(WIDTH.toDouble(), HEIGHT.toDouble())
        root.children.add(canvas)
        root.children.add(ball.circle)
        root.children.add(paddle.rectangle)

        prepareActionHandlers()

        graphicsContext = canvas.graphicsContext2D

        space = Image(getResource("/space.png"))

        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                tickAndRender(currentNanoTime)
            }
        }.start()

        mainStage.show()
    }

    private fun tickAndRender(currentNanoTime: Long) {
        val deltaTime = ((currentNanoTime - lastFrameTime) / 1_000_000).toLong()
        lastFrameTime = currentNanoTime

        graphicsContext.clearRect(0.0, 0.0, WIDTH.toDouble(), HEIGHT.toDouble())
        graphicsContext.drawImage(space, 0.0, 0.0)

        if (deltaTime != 0L) {
            graphicsContext.fill = Color.WHITE
            graphicsContext.fillText("${1000 / deltaTime} fps", 10.0, 10.0)

            for (line in gameBounds.toLines()) {
                if (ball.circle.intersects(line)) {
                    println("HIT")
                    ball.velocity = ball.center.getNormal(ball.velocity, line)
                }
            }

            for (line in paddle.rectangle.toLines()) {
                if (ball.circle.intersects(line)) {
                    println("HIT")
                    ball.velocity = ball.center.getBounceVelocity(ball.velocity, line)
                }
            }

            ball.update(deltaTime.toDouble())
            paddle.update(deltaTime.toDouble())
        }
    }

    private fun prepareActionHandlers() {
        mainScene.onKeyPressed = EventHandler { event ->
            currentlyActiveKeys.add(event.code)
        }
        mainScene.onKeyReleased = EventHandler { event ->
            currentlyActiveKeys.remove(event.code)
        }
    }
}

