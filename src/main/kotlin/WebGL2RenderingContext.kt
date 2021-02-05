import org.khronos.webgl.WebGLObject
import org.khronos.webgl.WebGLRenderingContext

abstract external class WebGL2RenderingContext : WebGLRenderingContext {
    val DEPTH_BUFFER_BIT: Int
    val COLOR_BUFFER_BIT: Int
    val LINES: Int
    val TRIANGLES: Int
    val ARRAY_BUFFER: Int
    val STATIC_DRAW: Int
    val FLOAT: Int
    val FRAGMENT_SHADER: Int
    val VERTEX_SHADER: Int
    val LINK_STATUS: Int
    val COMPILE_STATUS: Int

    fun createVertexArray(): WebGLObject
    fun bindVertexArray(vao: WebGLObject)
}
