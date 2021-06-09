package com.b21.finalproject.smartlibraryapp.services

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.b21.finalproject.smartlibraryapp.ml.Model
import com.b21.finalproject.smartlibraryapp.ml.Model3
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.util.ArrayList
import java.util.concurrent.Executors

class ModelService : JobIntentService() {

    companion object {
        private const val JOB_ID = 1000
        private val TAG = ModelService::class.java.simpleName

        fun enqueueWork(context: Context, intent: Intent){
            enqueueWork(context, ModelService::class.java, JOB_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.d(TAG, "service model has started...")
        val userId = intent.getStringExtra("userId")
        val dataLength = 271360

        val array = ArrayList<Float>()

        val model = Model.newInstance(baseContext)
        for (j in 0 until dataLength-1) {
            val byteBuffer1: ByteBuffer = ByteBuffer.allocateDirect(1 * 4)
            byteBuffer1.putFloat(userId!!.toFloat())

            val byteBuffer2: ByteBuffer = ByteBuffer.allocateDirect(1 * 4)
            byteBuffer2.putFloat(j.toFloat())

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
            inputFeature0.loadBuffer(byteBuffer1)
            val inputFeature1 = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
            inputFeature1.loadBuffer(byteBuffer2)

            val outputs = model.process(inputFeature0, inputFeature1)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

            array.add(outputFeature0[0])
        }
        model.close()

        if (! Python.isStarted()) {
            Python.start(AndroidPlatform(baseContext))
        }

        val py: Python = Python.getInstance()

        val pyObj = py.getModule("myscript")
        val obj = pyObj.callAttr("getToIndex", array.toTypedArray())

        val resultIndexBook = obj.asList()
        Log.d("getToIndex", obj.toString())
        Log.d("getToIndex2", resultIndexBook.toString())
        Log.d("getToIndex3", resultIndexBook[0].toString())

        GlobalScope.launch {
            val resultToArray = ArrayList<Int>()
            for (i in resultIndexBook.indices) {
                resultToArray.add(resultIndexBook[i].toInt())
            }
            sendData(resultToArray)
        }

        for (i in 0..50) {
            Log.d("output$i", array.size.toString() + " " + array[i].toString())
        }

        array.sort()

        for (i in 51..97) {
            Log.d("output$i", array.size.toString() + " " + array[i].toString())
        }

        GlobalScope.launch {
            val newArray = array.distinct().filter { !it.isNaN() }
            for (i in 0..11) {
                Log.d("output$i", newArray.size.toString() + " " + newArray[i].toString())
            }
        }
    }

    private fun sendData(resultIndexBook: ArrayList<Int>) {
        val intent = Intent("data")
        intent.putExtra("data_model", resultIndexBook)
        LocalBroadcastManager.getInstance(baseContext).sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
    }

}