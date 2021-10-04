package microphone.microphone
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_play_file.*
import kotlinx.android.synthetic.main.player.*
import microphone.microphone.utlis.App.Companion.mp
import microphone.microphone.utlis.App.Companion.mp3File
import java.io.File

class PlayFile : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_file)

        fab_playFile.setOnClickListener{ plaing(mp3File.path) }
        fab_stopFile.setOnClickListener {
            if(mp != null && mp!!.isPlaying){ mp?.pause() }
        }

        shareFile.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            val name = mp3File.name
            sharingIntent.type = "$name/*"
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mp3File.absolutePath))
            startActivity(Intent.createChooser(sharingIntent, name))
        }

        seekBarFile.setOnSeekBarChangeListener(@SuppressLint("AppCompatCustomView") object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mp?.seekTo(progress)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        fab_deleteFile.setOnClickListener{ if(mp != null){ mp?.stop(); } ; showMeDialogDeletedFile(mp3File.path); }
    }

    private fun plaing(pathFile: String) {
        if( mp != null && mp!!.isPlaying){ mp!!.pause(); return; }
        if(mp == null){
            try { mp = MediaPlayer.create(this, Uri.parse(pathFile)) ; initialeSeekBar();  mp?.start()
            } catch (e: Exception){ Toast.makeText(this, "Error, comment from Alexei Suzdalenko email: alexei.saron@gmail.com", Toast.LENGTH_LONG).show() }
        }
    }

    fun initialeSeekBar() {
        seekBarFile.max = mp!!.duration
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                try { seekBarFile.progress = mp!!.currentPosition; handler.postDelayed(this, 1111)
                } catch (e: Exception) { seekBarFile.progress = 0; }
            }
        }, 0)
    }

    private fun showMeDialogDeletedFile(x: String){
        val dialogDelete = AlertDialog.Builder(this)
        dialogDelete.setMessage("¿ DELETE THIS SONG ?")
        dialogDelete.setPositiveButton("Yes") { dialog, which ->
            Toast.makeText(this, "deleting " + x, Toast.LENGTH_LONG).show()
            val myFile: File = File(x)
            myFile.delete()
            finish()
        }
        dialogDelete.setNegativeButton("No") { _, _ -> }
        val dialog2: AlertDialog? = dialogDelete.create()
        dialog2!!.show()
    }
}