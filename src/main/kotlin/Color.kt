object Color {

    fun red(argb: Int) = argb shr 16 and 255

    fun grn(argb: Int) = argb shr 8 and 255

    fun blu(argb: Int) = argb and 255

    fun pack(red: Float, grn: Float, blu: Float): Int {
        val r = red.toInt().coerceIn(0, 255) shl 16
        val g = grn.toInt().coerceIn(0, 255) shl 8
        val b = blu.toInt().coerceIn(0, 255)
        return 255 shl 24 or r or g or b
    }

    fun grayScale(argb: Int) = 0.2126F * red(argb) + 0.7152F * grn(argb) + 0.0722F * blu(argb)
}
