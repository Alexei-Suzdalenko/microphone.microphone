package microphone.microphone
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_files.*
import microphone.microphone.utlis.App.Companion.mp3File
import microphone.microphone.utlis.FileListAdapter
import java.io.File

class FilesActivity : AppCompatActivity() {
    var fileList: ArrayList<File> = ArrayList()
    var nameList: ArrayList<String> = ArrayList()
    var mp: MediaPlayer? = null
    lateinit var seekBar: SeekBar
    lateinit var fullpath: File
    lateinit var share: ImageView
    private lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_files)

        fullpath = File(getExternalFilesDir(null), "rec_mic")

        MobileAds.initialize(this) { }
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                var size = 211
                val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                if( mAdView.height > 15 ) size = mAdView.height
                params.setMargins(22, 0, 22, size)
                mainListView.layoutParams = params
            }
        }
        mAdView.loadAd(adRequest)
    }

    override fun onStart() {
        super.onStart()
        getMeListFilesSongs()
    }

    private fun getMeListFilesSongs() {
        fun imageReader(root: File) {
            nameList.clear()
            fileList.clear()
            val listAllFiles = root.listFiles()
            if (listAllFiles != null && listAllFiles.isNotEmpty()) {
                for (currentFile in listAllFiles) {
                    nameList.add(currentFile.name)
                    fileList.add(currentFile.absoluteFile)
                }
            }
        }

        imageReader(fullpath)

        mainListView.adapter = FileListAdapter(this, nameList)
        mainListView.setOnItemClickListener { _, _, position, _ ->
            // Toast.makeText(this, fileList[position].path + "  ---  " +  position.toString(), Toast.LENGTH_LONG).show()
            mp3File = fileList[position]
            startActivity(Intent(this, PlayFile::class.java))
        }
    }

    // old show dialog
    fun showMePlayer(pathFile: String, fileMP3: File) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.player)

        seekBar = dialog.findViewById(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(@SuppressLint("AppCompatCustomView") object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mp?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        dialog.show()
    }


}