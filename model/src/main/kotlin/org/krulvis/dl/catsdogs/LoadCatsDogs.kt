package org.krulvis.dl.catsdogs

import org.jetbrains.kotlinx.dl.api.core.Sequential
import org.jetbrains.kotlinx.dl.api.core.loss.Losses
import org.jetbrains.kotlinx.dl.api.core.metric.Metrics
import org.jetbrains.kotlinx.dl.api.core.optimizer.Adam
import org.jetbrains.kotlinx.dl.dataset.OnFlyImageDataset
import org.jetbrains.kotlinx.dl.dataset.image.ColorOrder
import org.jetbrains.kotlinx.dl.dataset.preprocessor.*
import org.jetbrains.kotlinx.dl.dataset.preprocessor.generator.FromFolders
import org.jetbrains.kotlinx.dl.dataset.preprocessor.image.InterpolationType
import org.jetbrains.kotlinx.dl.dataset.preprocessor.image.resize
import org.krulvis.dl.callback.CustomCallback
import org.krulvis.dl.catsdogs.CatsDogs.IMAGE_SIZE
import org.krulvis.dl.catsdogs.CatsDogs.PATH_TO_MODEL
import java.io.File

private const val DATA_PATH = "cache/datasets/dogs-vs-cats"

fun loadModel(): Sequential {
    val model = Sequential.loadDefaultModelConfiguration(File(PATH_TO_MODEL))
    model.compile(
        optimizer = Adam(),
        loss = Losses.SOFT_MAX_CROSS_ENTROPY_WITH_LOGITS,
        metric = Metrics.ACCURACY,
        callback = CustomCallback()
    )
    model.loadWeights(File(PATH_TO_MODEL))
    return model
}


fun main() {
    val model = loadModel()
    val preprocessing = Preprocessing()
    preprocessing.load {
        pathToData = File(DATA_PATH)
        imageShape = ImageShape(channels = 3)
        labelGenerator = FromFolders(mapping = mapOf("cat" to 0, "dog" to 1))
    }
    preprocessing.transformImage {
        resize {
            outputHeight = IMAGE_SIZE.toInt()
            outputWidth = IMAGE_SIZE.toInt()
            interpolation = InterpolationType.NEAREST
        }
        convert { colorOrder = ColorOrder.BGR }
    }
    preprocessing.transformTensor {
        rescale {
            scalingCoefficient = 255f
        }
    }

    val test = OnFlyImageDataset.create(preprocessing).shuffle()
    val testSize = 100
    var acc = 0.0
    (0 until testSize).forEach {
        val pred = model.predict(test.getX(it))
        if (pred == test.getY(it).toInt())
            acc += 1.0 / testSize
    }
    println("Accuracy is: $acc")
//    val accuracy = model.evaluate(test).metrics[Metrics.ACCURACY] ?: 0.0
////        println("Accuracy is : $accuracy")
}