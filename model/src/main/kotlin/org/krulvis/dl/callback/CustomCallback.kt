package org.krulvis.dl.callback

import org.jetbrains.kotlinx.dl.api.core.callback.Callback
import org.jetbrains.kotlinx.dl.api.core.history.*

class CustomCallback : Callback() {
    override fun onEpochBegin(epoch: Int, logs: TrainingHistory) {
        println("Epoch $epoch begins.")
    }

    override fun onEpochEnd(epoch: Int, event: EpochTrainingEvent, logs: TrainingHistory) {
        println("Epoch $epoch ends.")
    }

    override fun onTrainBatchBegin(batch: Int, batchSize: Int, logs: TrainingHistory) {
        println("Training batch $batch begins.")
    }

    override fun onTrainBatchEnd(
        batch: Int,
        batchSize: Int,
        event: BatchTrainingEvent,
        logs: TrainingHistory
    ) {
        println("Training batch $batch ends with loss ${event.lossValue}.")
    }

    override fun onTrainBegin() {
        println("Train begins")
    }

    override fun onTrainEnd(logs: TrainingHistory) {
        println("Train ends with last loss ${logs.lastBatchEvent().lossValue}")
    }

    override fun onTestBatchBegin(batch: Int, batchSize: Int, logs: History) {
        println("Test batch $batch begins.")
    }

    override fun onTestBatchEnd(batch: Int, batchSize: Int, event: BatchEvent?, logs: History) {
        println("Test batch $batch ends with loss ${event!!.lossValue}..")
    }

    override fun onTestBegin() {
        println("Test begins")
    }

    override fun onTestEnd(logs: History) {
        println("Train ends with last loss ${logs.lastBatchEvent().lossValue}")
    }

    override fun onPredictBatchBegin(batch: Int, batchSize: Int) {
        println("Prediction batch $batch begins.")
    }

    override fun onPredictBatchEnd(batch: Int, batchSize: Int) {
        println("Prediction batch $batch ends.")
    }

    override fun onPredictBegin() {
        println("Train begins")
    }

    override fun onPredictEnd() {
        println("Test begins")
    }
}
