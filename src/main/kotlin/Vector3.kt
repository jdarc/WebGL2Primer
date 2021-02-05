import kotlin.math.sqrt

data class Vector3(val x: Float, val y: Float, val z: Float) {

    companion object {
        fun normalize(v: Vector3): Vector3 {
            val length = sqrt(v.x * v.x + v.y * v.y + v.z * v.z)
            return Vector3(v.x / length, v.y / length, v.z / length)
        }

        fun dotProduct(lhs: Vector3, rhs: Vector3) = lhs.x * rhs.x + lhs.y * rhs.y + lhs.z * rhs.z
    }
}
