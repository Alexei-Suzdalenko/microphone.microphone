package microphone.microphone
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_files.*
import kotlinx.android.synthetic.main.player.*
import microphone.microphone.utlis.App.Companion.file
import microphone.microphone.utlis.FileListAdapter
import java.io.File

class FilesActivity : AppCompatActivity() {
    var fileList: ArrayList<File> = ArrayList()
    var nameList: ArrayList<String> = ArrayList()
    var mp: MediaPlayer? = null
    lateinit var fab_play: FloatingActionButton
    lateinit var fab_delete: FloatingActionButton
    lateinit var fab_stop: FloatingActionButton
    lateinit var seekBar: SeekBar
    lateinit var fullpath: File
    lateinit var share: ImageView
    private lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_files)

        fullpath = File(getExternalFilesDir(null), "rec_mic")
        getMeListFilesSongs()

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
            showMePlayer(fileList[position].path, fileList[position])
        }
    }


    fun showMePlayer(pathFile: String, fileMP3: File) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.player)

        share = dialog.findViewById(R.id.share)
        share.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            val name = fileMP3.name
            sharingIntent.type = "$name/*"
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileMP3.absolutePath))
            startActivity(Intent.createChooser(sharingIntent, name))
        }

        fab_play = dialog.findViewById(R.id.fab_play)
        fab_play.setOnClickListener{ plaing(pathFile) }

        fab_delete = dialog.findViewById(R.id.fab_delete)
        fab_delete.setOnClickListener{
            if(mp != null){
                mp?.stop()
                mp?.reset()
                mp = null
            }
            dialog.dismiss()
            showMeDialogDeletedFile(pathFile)
        }

        fab_stop = dialog.findViewById(R.id.fab_stop)
        fab_stop.setOnClickListener {
            if(mp != null){
                mp?.stop()
                mp?.reset()
                mp = null
            }
            dialog.dismiss()
        }

        seekBar = dialog.findViewById(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(@SuppressLint("AppCompatCustomView") object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mp?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        plaing(pathFile)
        dialog.show()
    }

    private fun plaing(pathFile: String) {
        if(mp == null){
            try {
            mp = MediaPlayer.create(this, Uri.parse(pathFile))
                initialeSeekBar()
            } catch (e: Exception){
                Toast.makeText(this, "This file is corrumped, delete it in file manager", Toast.LENGTH_LONG).show()
            }
        }
        mp?.start()
    }


    fun initialeSeekBar() {
        seekBar.max = mp!!.duration
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    seekBar.progress = mp!!.currentPosition
                    handler.postDelayed(this, 1111)
                } catch (e: Exception) {
                    seekBar.progress = 0
                }
            }
        }, 0)
    }

    fun showMeDialogDeletedFile(x: String){
        val dialogDelete = AlertDialog.Builder(this)
        dialogDelete.setMessage("¿ DELETE THIS SONG ?")
        dialogDelete.setPositiveButton("Yes") { dialog, which ->
            Toast.makeText(this, "deleting " + x, Toast.LENGTH_LONG).show()
            val myFile: File = File(x)
            myFile.delete()
            getMeListFilesSongs()
        }
        dialogDelete.setNegativeButton("No") { _, _ -> }
        val dialog2: AlertDialog? = dialogDelete.create()
        dialog2!!.show()
    }
}