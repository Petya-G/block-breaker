package blockbreaker

import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.reflect.KMutableProperty0

fun KMutableProperty0<Point2D>.setFrom(other: Point2D) {
    set(Point2D(other.x, other.y))
}

operator fun KMutableProperty0<Point2D>.minusAssign(other: Point2D) {
    set(Point2D(get().x - other.x, get().y - other.y))
}

operator fun KMutableProperty0<Point2D>.plusAssign(other: Point2D) {
    set(Point2D(get().x + other.x, get().y + other.y))
}

operator fun Point2D.times(other: Double): Point2D = Point2D(x * other, y * other)

fun Rectangle.toLines(): List<Line> {
    val x1 = x
    val y1 = y
    val x2 = x + width
    val y2 = y + height

    val top = Line(x1, y1, x2, y1)
    val right = Line(x2, y1, x2, y2)
    val bottom = Line(x2, y2, x1, y2)
    val left = Line(x1, y2, x1, y1)

    return listOf(bottom, left, top, right)
}

fun Line.closestPoint(point: Point2D): Point2D {
    val ab = Point2D(endX - startX, endY - startY)
    val ap = Point2D(point.x - startX, point.y - startY)
    val t = (ab.dotProduct(ap) / ab.magnitude().pow(2)).coerceIn(0.0, 1.0)
    return Point2D(startX + ab.x * t, startY + ab.y * t)
}

fun Circle.intersects(line: Line): Boolean {
    val center = Point2D(centerX, centerY)
    return line.closestPoint(center).distance(center) <= radius
}

fun Point2D.getNormal(velocity: Point2D, line: Line): Point2D {
    val dx = line.endX - line.startX
    val dy = line.endY - line.startY

    val normal = Point2D(-dy, dx).normalize()
    val dot = velocity.dotProduct(normal)
    val reflected = velocity.subtract(normal.multiply(2 * dot))
    return reflected
}

fun Point2D.getBounceVelocity(
    velocity: Point2D, line: Line, maxBounceAngle: Double = Math.toRadians(75.0)
): Point2D {
    val leftX = minOf(line.startX, line.endX)
    val rightX = maxOf(line.startX, line.endX)
    val paddleCenterX = (leftX + rightX) / 2.0
    val relativeIntersect = (this.x - paddleCenterX) / ((rightX - leftX) / 2.0)
    val clamped = relativeIntersect.coerceIn(-1.0, 1.0)

    val angle = clamped * maxBounceAngle

    return (Point2D(sin(angle), -cos(angle)) * velocity.magnitude()).normalize()
}

fun Circle.clamp() {
    centerX = centerX.coerceIn(radius, Game.WIDTH - radius)
    centerY = centerY.coerceAtLeast(radius)
}

operator fun Point2D.plus(other: Point2D): Point2D = Point2D(this.x + other.x, this.y + other.y)