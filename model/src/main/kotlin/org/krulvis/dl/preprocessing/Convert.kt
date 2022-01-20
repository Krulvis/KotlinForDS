package org.krulvis.dl.preprocessing

import org.jetbrains.kotlinx.dl.dataset.image.ColorOrder
import org.jetbrains.kotlinx.dl.dataset.preprocessor.ImageShape
import org.jetbrains.kotlinx.dl.dataset.preprocessor.image.ImagePreprocessorBase
import java.awt.image.BufferedImage

class Convert(var colorOrder: ColorOrder = ColorOrder.BGR) : ImagePreprocessorBase() {
    
    override fun getOutputShape(inputShape: ImageShape?): ImageShape {
        return ImageShape(inputShape?.width, inputShape?.height, 3)
    }

    override fun apply(image: BufferedImage): BufferedImage {
        if (image.colorOrder() == colorOrder) return image
        val outputType = colorOrder.imageType()
        val result = BufferedImage(image.width, image.height, outputType)
        val graphics = result.createGraphics()
        graphics.drawImage(image, 0, 0, null)
        graphics.dispose()
        return result
    }

    fun ColorOrder.imageType(): Int {
        return when (this) {
            ColorOrder.RGB -> BufferedImage.TYPE_INT_RGB
            ColorOrder.BGR -> BufferedImage.TYPE_3BYTE_BGR
//            ColorOrder.GRAYSCALE -> BufferedImage.TYPE_BYTE_GRAY
        }
    }

    fun BufferedImage.colorOrder(): ColorOrder {
        return when (type) {
            BufferedImage.TYPE_INT_RGB -> ColorOrder.RGB
            BufferedImage.TYPE_3BYTE_BGR, BufferedImage.TYPE_INT_BGR -> ColorOrder.BGR
//            BufferedImage.TYPE_BYTE_GRAY -> ColorOrder.GRAYSCALE
            else -> throw UnsupportedOperationException("Images with type $type are not supported.")
        }
    }
}