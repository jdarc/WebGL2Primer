import org.khronos.webgl.WebGLProgram
import org.khronos.webgl.WebGLShader

object Glu {

    fun compileShader(gl: WebGL2RenderingContext, shaderSource: String, shaderType: Int): WebGLShader {
        val shader = gl.createShader(shaderType)
        gl.shaderSource(shader, shaderSource)
        gl.compileShader(shader)
        val success = gl.getShaderParameter(shader, gl.COMPILE_STATUS) as Boolean
        if (!success) {
            throw RuntimeException("shader failed to compile: ${gl.getShaderInfoLog(shader)}")
        }
        return shader ?: throw RuntimeException("failed to create shader")
    }

    fun createProgram(gl: WebGL2RenderingContext, vertexShader: WebGLShader, fragmentShader: WebGLShader): WebGLProgram {
        val program = gl.createProgram()
        gl.attachShader(program, vertexShader)
        gl.attachShader(program, fragmentShader)
        gl.linkProgram(program)
        val success = gl.getProgramParameter(program, gl.LINK_STATUS) as Boolean
        if (!success) {
            throw RuntimeException("program failed to link: ${gl.getProgramInfoLog(program)}")
        }
        return program ?: throw RuntimeException("failed to create program")
    }
}
