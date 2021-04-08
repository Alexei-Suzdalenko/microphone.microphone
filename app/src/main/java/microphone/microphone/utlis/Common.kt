package microphone.microphone.utlis
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import microphone.microphone.MainActivity

class Common {
    companion object{
        val TAG = "tag"
        val ISRECORD = "isrecord"
        val ISCHESCED = "ischecked"
    }
    fun requestPermission(context: MainActivity){
        if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(context, arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 11)
        }
    }

    fun testIHavePersmission(context: MainActivity): Boolean {
        if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                val info = "You did not allow recording in the application, reinstall it and allow recording"
                Toast.makeText(context, info, Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}