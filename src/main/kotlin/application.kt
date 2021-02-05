import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint32Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.Image
import org.w3c.dom.events.MouseEvent
import kotlin.math.PI
import kotlin.math.pow

suspend fun main() {
    simpleGreeting()
    coroutineDemo()
    canvas2DExample()
    animationLoop()
    simpleWebGLDemo()
    teapotWebGLDemo()
    lightingDemo()
}

fun simpleGreeting() {
    val greet = "world"
    console.log("Hello, $greet")
}

suspend fun coroutineDemo() {
    val root = document.querySelector("#root") as HTMLDivElement
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    canvas.width = 200
    canvas.height = 400
    root.appendChild(canvas)
    val ctx = canvas.getContext("2d") as CanvasRenderingContext2D
    ctx.font = "16px Helvetica Neue"
    val colors = arrayOf("#F00", "#FFF", "#0F0", "#00F", "#F0F", "#FF0", "#0FF")
    window.fetch("poem.txt").await().text().await().split('\n').forEachIndexed { index, line ->
        ctx.fillStyle = colors[index % colors.size]
        ctx.fillText(line, 0.0, 16.0 + index * 18.0, 200.0)
    }
}

fun canvas2DExample() {
    val root = document.querySelector("#root") as HTMLDivElement
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    canvas.width = 100
    canvas.height = 100

    root.appendChild(canvas)

    val context2D = canvas.getContext("2d") as CanvasRenderingContext2D
    context2D.arc(50.0, 50.0, 20.0, 0.0, 2.0 * PI, true)

    val grd = context2D.createLinearGradient(40.0, 40.0, 50.0, 75.0)
    grd.addColorStop(0.0, "#F9FF00")
    grd.addColorStop(1.0, "#E0C000")
    context2D.fillStyle = grd
    context2D.fill()
    context2D.beginPath()
    context2D.arc(44.0, 45.0, 4.0, 0.0, 2.0 * PI, true)

    context2D.fillStyle = "#ffffff"
    context2D.fill()
    context2D.beginPath()
    context2D.arc(44.0, 45.0, 2.0, 0.0, 2.0 * PI, true)

    context2D.fillStyle = "#000000"
    context2D.fill()
    context2D.beginPath()
    context2D.arc(58.0, 45.0, 4.0, 0.0, 2.0 * PI, true)

    context2D.fillStyle = "#ffffff"
    context2D.fill()
    context2D.beginPath()
    context2D.arc(58.0, 45.0, 2.0, 0.0, 2.0 * PI, true)

    context2D.fillStyle = "#000000"
    context2D.fill()
    context2D.beginPath()
    context2D.arc(50.0, 55.0, 10.0, 0.0, PI, false)
    context2D.stroke()
}

fun animationLoop() {
    val root = document.querySelector("#root") as HTMLDivElement
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    canvas.width = 256
    canvas.height = 256

    root.appendChild(canvas)
    val ctx2D = canvas.getContext("2d") as CanvasRenderingContext2D

    fun loop(timestamp: Double) {
        ctx2D.fillStyle = "#000"
        ctx2D.fillRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())

        ctx2D.fillStyle = "#0F0"
        ctx2D.translate(128.0, 128.0)
        ctx2D.rotate(0.25 * timestamp * PI / 180.0)
        ctx2D.fillRect(-50.0, -20.0, 100.0, 40.0)
        ctx2D.resetTransform()

        window.requestAnimationFrame { loop(it) }
    }

    window.requestAnimationFrame { loop(it) }
}

suspend fun simpleWebGLDemo() {
    val rootDivElement = document.querySelector("#root") as HTMLDivElement
    val canvasElement = document.createElement("canvas") as HTMLCanvasElement
    canvasElement.width = 400
    canvasElement.height = 400

    rootDivElement.appendChild(canvasElement)

    val gl = canvasElement.getContext("webgl2") as WebGL2RenderingContext

    val vertexShaderSrc = window.fetch("shaders/vertex.glsl").await().text().await()
    val fragmentShaderSrc = window.fetch("shaders/fragment.glsl").await().text().await()

    val vertexShader = Glu.compileShader(gl, vertexShaderSrc, gl.VERTEX_SHADER)
    val fragmentShader = Glu.compileShader(gl, fragmentShaderSrc, gl.FRAGMENT_SHADER)
    val program = Glu.createProgram(gl, vertexShader, fragmentShader)

    // look up where the vertex data needs to go.
    val positionAttributeLocation = gl.getAttribLocation(program, "aPosition")
    val colorAttributeLocation = gl.getAttribLocation(program, "aColor")

    // Allocate GPU memory
    val buffer = gl.createBuffer()

    // Create a vertex array object to store attribute state
    val vao = gl.createVertexArray()

    // Make it the active
    gl.bindVertexArray(vao)

    // Turn on position attribute
    gl.enableVertexAttribArray(positionAttributeLocation)

    // Turn on color attribute
    gl.enableVertexAttribArray(colorAttributeLocation)

    // Bind the buffer
    gl.bindBuffer(gl.ARRAY_BUFFER, buffer)

    // 2D F letter compromise of X,Y and R,G,B vertices
    val vertices = arrayOf(
        -0.2F, 0.35F, 1.0F, 0.0F, 0.0F,
        -0.05F, 0.35F, 1.0F, 0.0F, 0.0F,
        -0.2F, -0.4F, 1.0F, 0.0F, 0.0F,
        -0.2F, -0.4F, 0.0F, 1.0F, 0.0F,
        -0.05F, 0.35F, 0.0F, 1.0F, 0.0F,
        -0.05F, -0.4F, 0.0F, 1.0F, 0.0F,
        -0.05F, 0.35F, 0.0F, 0.0F, 1.0F,
        0.3F, 0.35F, 0.0F, 0.0F, 1.0F,
        -0.05F, 0.2F, 0.0F, 0.0F, 1.0F,
        -0.05F, 0.2F, 1.0F, 1.0F, 1.0F,
        0.3F, 0.35F, 0.0F, 1.0F, 1.0F,
        0.3F, 0.2F, 0.0F, 1.0F, 0.0F,
        -0.05F, 0.05F, 1.0F, 0.0F, 1.0F,
        0.135F, 0.05F, 0.0F, 1.0F, 1.0F,
        -0.05F, -0.1F, 1.0F, 1.0F, 0.0F,
        -0.05F, -0.1F, 1.0F, 0.0F, 0.0F,
        0.135F, 0.05F, 0.0F, 1.0F, 0.0F,
        0.135F, -0.1F, 0.0F, 0.0F, 1.0F
    )

    // Fill buffer with position and color data elements
    gl.bufferData(gl.ARRAY_BUFFER, Float32Array(vertices), gl.STATIC_DRAW)

    // Tell the attribute how to get data out of buffer
    gl.vertexAttribPointer(positionAttributeLocation, 2, gl.FLOAT, false, 5 * 4, 0)
    gl.vertexAttribPointer(colorAttributeLocation, 3, gl.FLOAT, false, 5 * 4, 2 * 4)

    // Tell WebGL how to convert from clip space to pixels
    gl.viewport(0, 0, gl.drawingBufferWidth, gl.drawingBufferHeight)

    // Clear the canvas
    gl.clearColor(0.4F, 0.8F, 0.8F, 1F)
    gl.clear(gl.COLOR_BUFFER_BIT or gl.DEPTH_BUFFER_BIT)

    // Tell it to use our program
    gl.useProgram(program)

    // Bind the attribute/buffer
    gl.bindVertexArray(vao)

    // Draw the geometry.
    gl.drawArrays(gl.TRIANGLES, 0, vertices.size / 5)
}

suspend fun teapotWebGLDemo() {
    val rootDivElement = document.querySelector("#root") as HTMLDivElement
    val canvasElement = document.createElement("canvas") as HTMLCanvasElement
    canvasElement.width = 400
    canvasElement.height = 400

    rootDivElement.appendChild(canvasElement)
    val gl = canvasElement.getContext("webgl2") as WebGL2RenderingContext

    val vertexShaderSrc = window.fetch("shaders/vertex.glsl").await().text().await()
    val fragmentShaderSrc = window.fetch("shaders/fragment.glsl").await().text().await()

    val vertexShader = Glu.compileShader(gl, vertexShaderSrc, gl.VERTEX_SHADER)
    val fragmentShader = Glu.compileShader(gl, fragmentShaderSrc, gl.FRAGMENT_SHADER)
    val program = Glu.createProgram(gl, vertexShader, fragmentShader)

    val positionAttributeLocation = gl.getAttribLocation(program, "aPosition")

    val vao = gl.createVertexArray()

    gl.bindVertexArray(vao)

    gl.enableVertexAttribArray(positionAttributeLocation)

    val positionBuffer = gl.createBuffer()

    gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer)

    // Load teapot from wavefront file
    val teapot = window.fetch("models/teapot.obj").await().text().await()

    // Unpack the model into vertex pairs
    val vertices = mutableListOf<Vector3>()
    val packed = mutableListOf<Float>()
    teapot.split('\n').forEach { line ->
        val fragments = line.split(' ')
        if (fragments[0] == "v") {
            val x = 2.0F * fragments[1].toFloat()
            val y = 2.0F * fragments[2].toFloat()
            val z = 2.0F * fragments[3].toFloat()
            vertices.add(Vector3(x, y, z))
        } else if (fragments[0] == "f") {
            val parts = fragments.takeLast(3).map { it.split('/').first().toInt() }
            val v0 = vertices[parts[0] - 1]
            val v1 = vertices[parts[1] - 1]
            val v2 = vertices[parts[2] - 1]
            packed += v0.x; packed += v0.y; packed += v0.z
            packed += v1.x; packed += v1.y; packed += v1.z
            packed += v1.x; packed += v1.y; packed += v1.z
            packed += v2.x; packed += v2.y; packed += v2.z
            packed += v2.x; packed += v2.y; packed += v2.z
            packed += v0.x; packed += v0.y; packed += v0.z
        }
    }
    val buffer = packed.toTypedArray()

    // Fill buffer with position and color data elements
    gl.bufferData(gl.ARRAY_BUFFER, Float32Array(buffer), gl.STATIC_DRAW)

    // Tell the attribute how to get data out of buffer
    gl.vertexAttribPointer(positionAttributeLocation, 3, gl.FLOAT, false, 0, 0)

    // Tell WebGL how to convert from clip space to pixels
    gl.viewport(0, 0, gl.drawingBufferWidth, gl.drawingBufferHeight)

    // Clear the canvas
    gl.clearColor(0.9F, 0.9F, 0.9F, 1F)
    gl.clear(gl.COLOR_BUFFER_BIT or gl.DEPTH_BUFFER_BIT)

    // Tell it to use our program (pair of shaders)
    gl.useProgram(program)

    // Bind the attribute/buffer set we want.
    gl.bindVertexArray(vao)

    // Draw the lines....
    gl.drawArrays(gl.LINES, 0, buffer.size / 3)
}

fun lightingDemo() {
    var lightPosition = Vector3(0.0F, 128.0F, 0.0F)
    var lastY = 0.0F
    var dragging = false

    val root = document.querySelector("#root") as HTMLDivElement
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    root.appendChild(canvas)
    canvas.width = 700
    canvas.height = 465
    val originX = canvas.width / 2.0F
    val originY = canvas.height / 2.0F

    canvas.addEventListener("mousedown", { dragging = true })
    canvas.addEventListener("mouseup", { dragging = false })
    canvas.addEventListener("mousemove", { e ->
        e as MouseEvent
        val canvasRect = canvas.getBoundingClientRect()
        val mx = e.clientX - canvasRect.left
        val my = e.clientY - canvasRect.top
        if (dragging) {
            val ny = if (my < lastY) lightPosition.y - 10.0F else lightPosition.y + 10.0F
            lastY = my.toFloat()
            lightPosition = Vector3(lightPosition.x, ny.coerceIn(30.0F, 800.0F), lightPosition.z)
        } else {
            val x = mx - canvas.clientWidth / 2.0
            val z = my - canvas.clientHeight / 2.0
            lightPosition = Vector3(x.toFloat(), lightPosition.y, z.toFloat())
        }
    })

    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    val imageData = context.createImageData(canvas.width.toDouble(), canvas.height.toDouble())
    val imageDataBuffer = Uint32Array(imageData.data.buffer)
    val pixels = Uint32Array(imageDataBuffer.length)
    val normals = Array(pixels.length) { Vector3(0.0F, 1.0F, 0.0F) }

    val image = Image()
    image.addEventListener("load", {
        pixels.set(grabPixels(image), 0)
        for (y in 0 until image.height) {
            for (x in 0 until image.width) {
                normals[y * image.width + x] = computeNormal(pixels, x, y, image.width, image.height)
            }
        }
    })
    image.src = "cherries.jpg"

    fun loop() {
        for (y in 0 until canvas.height) {
            for (x in 0 until canvas.width) {
                val mem = y * canvas.width + x
                val intensity = computeIntensity(normals[mem], lightPosition, x - originX, y - originY)
                val specular = 255.0F * intensity.pow(15.0F)
                val rgb = pixels[mem]
                val red = specular + Color.red(rgb) * intensity
                val grn = specular + Color.grn(rgb) * intensity
                val blu = specular + Color.blu(rgb) * intensity
                imageDataBuffer[mem] = Color.pack(red, grn, blu)
            }
        }

        context.putImageData(imageData, 0.0, 0.0)
        window.requestAnimationFrame { loop() }
    }

    window.requestAnimationFrame { loop() }
}


private fun grabPixels(image: Image): Uint32Array {
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    canvas.width = image.width
    canvas.height = image.height
    context.drawImage(image, 0.0, 0.0)
    return Uint32Array(context.getImageData(0.0, 0.0, image.width.toDouble(), image.height.toDouble()).data.buffer)
}

private fun computeIntensity(normal: Vector3, light: Vector3, x: Float, y: Float): Float {
    val toLight = Vector3.normalize(Vector3(light.x - x, light.y, light.z - y))
    return Vector3.dotProduct(normal, toLight)
}

private fun computeNormal(pixels: Uint32Array, x: Int, y: Int, width: Int, height: Int): Vector3 {
    val gl = Color.grayScale(pixels[y * width + (x - 1).coerceIn(0, width - 1)])
    val gr = Color.grayScale(pixels[y * width + (x + 1).coerceIn(0, width - 1)])
    val gu = Color.grayScale(pixels[(y - 1).coerceIn(0, height - 1) * width + x])
    val gd = Color.grayScale(pixels[(y + 1).coerceIn(0, height - 1) * width + x])
    return Vector3.normalize(Vector3(gl - gr, 30.0F, gu - gd))
}
