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
        lateinit var blocks: EntityList<Block>
        lateinit var balls: EntityList<Ball>
        var stage = 1
    }

    lateinit var mainScene: Scene
    lateinit var graphicsContext: GraphicsContext

    lateinit var animation: AnimationTimer
    private var lastFrameTime: Long = System.nanoTime()

    var paddle = Paddle()
    val root = Group()

    var score = 0
    var lives = 3

    var gameOver: Boolean = lives >= 0

    override fun start(mainStage: Stage) {
        mainStage.title = "Block breaker"
        mainScene = Scene(root, WIDTH.toDouble(), HEIGHT.toDouble(), Color.BLACK)
        mainStage.scene = mainScene
        mainStage.isResizable = false
        mainStage.centerOnScreen()
        mainStage.requestFocus()

        val canvas = Canvas(WIDTH.toDouble(), HEIGHT.toDouble())
        root.children.add(canvas)
        root.children.add(paddle.rectangle)

        blocks = EntityList(root)
        balls = EntityList(root)
        balls.add(Ball(Point2D(paddle.position.x, paddle.position.y - Ball.RADIUS * 2.0)))
        blocks.generate()

        prepareActionHandlers()

        graphicsContext = canvas.graphicsContext2D

        animation = object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                if (gameOver) {
                }
                tickAndRender(currentNanoTime)
            }
        }
        animation.start()

        mainStage.show()
    }

    private fun tickAndRender(currentNanoTime: Long) {
        val deltaTime = ((currentNanoTime - lastFrameTime) / 1_000_000)
        lastFrameTime = currentNanoTime

        graphicsContext.fill = Color.WHITE
        graphicsContext.clearRect(0.0, 0.0, WIDTH.toDouble(), HEIGHT.toDouble())

        if (deltaTime != 0L) {
            graphicsContext.font = javafx.scene.text.Font.font(24.0)
            graphicsContext.fillText("Lives: $lives Score: $score", WIDTH / 2.0, 30.0)

            val ballsToRemove: MutableList<Ball> = mutableListOf()
            var blockToRemove: Block? = null
            for (ball in balls) {
                if (ball.position.y - 2.0 * Ball.RADIUS >= HEIGHT) {
                    ballsToRemove.add(ball)
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

                for (block in blocks) {
                    for (line in block.rectangle.toLines()) {
                        if (ball.circle.intersects(line)) {
                            ball.direction = ball.position.getNormal(ball.direction, line)
                            blockToRemove = block
                            score++
                        }
                    }
                }

                ball.update(deltaTime.toDouble())
            }

            blockToRemove?.hit()
            blocks.remove(blockToRemove)
            balls.removeAll(ballsToRemove)
            paddle.update(deltaTime.toDouble())

            gameOver = lives < 1
            if (!gameOver && balls.isEmpty()) {
                lives--
                balls.add(Ball(Point2D(paddle.position.x, paddle.position.y - Ball.RADIUS * 2.0)))
            }

            if (blocks.isEmpty()) {
                stage++
                balls.forEach { it.speed += 0.2 * stage }
                blocks.generate()
            }
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