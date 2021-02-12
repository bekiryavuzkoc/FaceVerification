package com.huawei.faceverificationhuawei

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.mlsdk.common.MLApplication
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.faceverify.MLFaceVerificationAnalyzer
import com.huawei.hms.mlsdk.faceverify.MLFaceVerificationAnalyzerFactory
import com.huawei.hms.mlsdk.faceverify.MLFaceVerificationAnalyzerSetting
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "FaceVerification"

class MainActivity : AppCompatActivity() {

    private lateinit var analyzer:MLFaceVerificationAnalyzer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MLApplication.getInstance().apiKey = "your_api_key"

        createAndSetAnalyzer()

        val actualInputImage = MLFrame.fromBitmap(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.first_kivanc))
        val resultOfInputImage = analyzer.setTemplateFace(actualInputImage)

        if(resultOfInputImage.size ==1){
            Log.d(TAG, "Face is obtained")
            kivanc_tatlitug_button.setOnClickListener {
                compareImages(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.second_kivanc))
            }
            joseph_morgan_button.setOnClickListener {
                compareImages(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.joseph_morgan))
            }
            kenan_imirzalioglu_button.setOnClickListener {
                compareImages(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.kenan_imirzalioglu))
            }
        }else{
            Log.d(TAG, "Image's face could not be found")
        }
    }

    private fun createAndSetAnalyzer(){
        val setting: MLFaceVerificationAnalyzerSetting.Factory =MLFaceVerificationAnalyzerSetting.Factory()
        setting.setMaxFaceDetected(1)
        val  mlAnalyzerSetting: MLFaceVerificationAnalyzerSetting = setting.create()
        analyzer = MLFaceVerificationAnalyzerFactory.getInstance().getFaceVerificationAnalyzer(mlAnalyzerSetting)
    }

    @SuppressLint("SetTextI18n")
    private fun compareImages(comparisonImage: Bitmap){
        val compareFrame = MLFrame.fromBitmap(comparisonImage)
            val task = analyzer.asyncAnalyseFrame(compareFrame)

            task.addOnSuccessListener {
                resultTextView.text = "Faces Similarity Ratio: " + it[0].similarity.toString()
                resultTextView.visibility = View.VISIBLE
            }.
            addOnFailureListener {
                Log.d(TAG, "Error in comparing the images")
            }
        }

    override fun onDestroy() {
        analyzer.stop()
        super.onDestroy()
    }
}


