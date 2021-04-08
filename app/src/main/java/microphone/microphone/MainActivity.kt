package microphone.microphone
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import microphone.microphone.utlis.*
import microphone.microphone.utlis.App.Companion.isRecording
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity() {
    private lateinit var mAdView : AdView
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = ""
        timeTextView.text = ""
        Common().requestPermission(this)
        Сounter.countiong(this)

        if(App.sharedPreferences.getBoolean(Common.ISRECORD, false)){ // если мы в процессе записи
            isRecording = true
            micImageButton.setBackgroundResource(R.drawable.mic_red)
        } else {
            isRecording = false
            stopService(Intent(this, AudioOurService::class.java))
            micImageButton.setBackgroundResource(R.drawable.mic_white)
        }

        swicherCompat.isChecked = App.sharedPreferences.getBoolean(Common.ISRECORD, false)

        swicherCompat.setOnCheckedChangeListener { _, isChecked ->
            stopMicSharing()
            stopRec()
            if(isChecked){
                App.editor.putBoolean(Common.ISCHESCED, true);  App.editor.apply()
            } else {
                App.editor.putBoolean(Common.ISCHESCED, false);  App.editor.apply()
            }
        }

        micImageButton.setOnClickListener {
            if(Common().testIHavePersmission(this)){
                if(App.sharedPreferences.getBoolean(Common.ISCHESCED, false)){
                    // если мы в процессе записи с микрофона
                    if(App.sharedPreferences.getBoolean(Common.ISRECORD, false)){ // если мы в процессе раздачи с микрофрна работаем
                        stopRec()
                    } else {
                        startRec()
                    }
                } else {
                    // если мы в процессе раздачи с микрофрна
                    if(App.sharedPreferences.getBoolean(Common.ISRECORD, false)){ // если мы в процессе раздачи с микрофрна работаем
                        stopMicSharing()
                    } else {
                        startMicSharing()
                    }
                }
            }
            App.count++
        }

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val adRequest2 = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-7286158310312043/4553461050", adRequest2, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
        Thread {
            while ( true ){
                Thread.sleep(2000)
                runOnUiThread { if ( mInterstitialAd != null &&  App.count % 11 == 0 ) { mInterstitialAd?.show(this)} }
            }
        }.start()


    }

    fun startRec(){
        isRecording = true
        App.editor.putBoolean(Common.ISRECORD, true)
        App.editor.apply()
        micImageButton.setBackgroundResource(R.drawable.mic_red)
        startService(Intent(this, RecordAudioService::class.java))
    }

    fun stopRec(){
        try{
            App.recorder.stop()
        } catch (e: Exception){ }
        isRecording = false
        App.editor.putBoolean(Common.ISRECORD, false)
        App.editor.apply()
        micImageButton.setBackgroundResource(R.drawable.mic_white)
        timeTextView.text = ""
        stopService(Intent(this, RecordAudioService::class.java))
    }


    fun stopMicSharing(){
        isRecording = false
        App.editor.putBoolean(Common.ISRECORD, false)
        App.editor.apply()
        micImageButton.setBackgroundResource(R.drawable.mic_white)
        timeTextView.text = ""
        stopService(Intent(this, AudioOurService::class.java))
    }

    fun startMicSharing(){
        isRecording = true
        App.editor.putBoolean(Common.ISRECORD, true)
        App.editor.apply()
        micImageButton.setBackgroundResource(R.drawable.mic_red)
        startService(Intent(this, AudioOurService::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share -> {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain";
                val index = resources.getString(R.string.app_name)
                shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "$index https://play.google.com/store/apps/details?id=microphone.microphone"
                )
                startActivity(Intent.createChooser(shareIntent, ""))
                return true
            }
            R.id.comment -> {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=microphone.microphone")
                    )
                )
                return true
            }
            R.id.web -> {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://diseno-cantabria.web.app/")
                    )
                )
                return true
            }
            R.id.files -> {
                startActivity(Intent(this, FilesActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

// https://youtu.be/k-bKnFX3Hrg невзоров
// https://youtu.be/GtdoWFgbxqE?list=RDz8-Je1JtWWs music from background