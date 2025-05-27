package blockbreaker

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Stage

class Game : Application() {

    companion object {
        const val WIDTH = 862
        const val HEIGHT = 862
        val currentlyActiveKeys = mutableSetOf<KeyCode>()
        private val gameBounds = Rectangle(0.0, 0.0, WIDTH.toDouble(), HEIGHT.toDouble())
        val bottom = gameBounds.toLines()[1]
    }

    private lateinit var mainScene: Scene
    private lateinit var graphicsContext: GraphicsContext

    private var lastFrameTime: Long = System.nanoTime()

    var paddle = Paddle()
    val root = Group()
    lateinit var blocks: EntityList<Block>
    lateinit var balls: EntityList<Ball>

    var score = 0

    override fun start(mainStage: Stage) {
        mainStage.title = "Block breaker"
        mainScene = Scene(root, WIDTH.toDouble(), HEIGHT.toDouble(), Color.BLACK)
        mainStage.scene = mainScene
        mainStage.isResizable = false
        mainStage.centerOnScreen()

        val canvas = Canvas(WIDTH.toDouble(), HEIGHT.toDouble())
        root.children.add(canvas)
        root.children.add(paddle.rectangle)

        blocks = EntityList(root)
        balls = EntityList(root)
        var ball = Ball(Point2D(paddle.position.x, paddle.position.y - Ball.RADIUS * 2.0))
        balls.add(ball)
        blocks.generate()

        prepareActionHandlers()

        graphicsContext = canvas.graphicsContext2D

        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                tickAndRender(currentNanoTime)
            }
        }.start()

        mainStage.show()
    }

    private fun tickAndRender(currentNanoTime: Long) {
        val deltaTime = ((currentNanoTime - lastFrameTime) / 1_000_000)
        lastFrameTime = currentNanoTime

        graphicsContext.fill = Color.WHITE
        graphicsContext.clearRect(0.0, 0.0, WIDTH.toDouble(), HEIGHT.toDouble())

        if (deltaTime != 0L) {
            graphicsContext.font = javafx.scene.text.Font.font(24.0)
            graphicsContext.fillText("Score: $score", WIDTH / 2.0, 30.0)

            val ballsToRemove: MutableList<Ball> = mutableListOf()
            for (ball in balls) {
                if (ball.position.y - 2.0 * Ball.RADIUS >= HEIGHT) {
                    ballsToRemove.remove(ball)
                }

                for (line in gameBounds.toLines().drop(1)) {
                    if (ball.circle.intersects(line)) {
                        ball.direction = ball.position.getNormal(ball.direction, line)
                    }
                }

                for (line in paddle.rectangle.toLines()) {
                    if (ball.circle.intersects(line)) {
                        ball.direction =
                            (ball.position.getBounceVelocity(ball.direction, line) + paddle.direction).normalize()
                    }
                }

                val blocksToRemove: MutableList<Block> = mutableListOf()
                for (block in blocks) {
                    for (line in block.rectangle.toLines()) {
                        if (ball.circle.intersects(line)) {
                            ball.direction = ball.position.getNormal(ball.direction, line)
                            root.children.remove(block.rectangle)
                            blocksToRemove.add(block)
                            score++
                        }
                    }
                }

                blocks.removeAll(blocksToRemove)
                ball.update(deltaTime.toDouble())
            }
            balls.removeAll(ballsToRemove)
        }
        paddle.update(deltaTime.toDouble())
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