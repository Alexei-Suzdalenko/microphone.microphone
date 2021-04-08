package microphone.microphone.utlis

import android.app.Service
import android.content.Intent
import android.media.*
import android.media.AudioTrack
import android.os.IBinder
import android.util.Log
import microphone.microphone.utlis.App.Companion.news
import java.nio.ByteBuffer


class AudioOurService: Service() {
    private val APP_TAG = "tag"
    private val mSampleRate = 44100
    private val mFormat = AudioFormat.ENCODING_PCM_16BIT
    private val deperecated = AudioFormat.CHANNEL_IN_STEREO // .CHANNEL_IN_DEFAULT //CHANNEL_CONFIGURATION_MONO

    private var mAudioOutput: AudioTrack? = null
    private var mAudioInput: AudioRecord? = null
    private var mInBufferSize = 0
    private var mOutBufferSize = 0

    override fun onBind(p0: Intent?): IBinder? { return null }

    override fun onCreate() {
        mInBufferSize = AudioRecord.getMinBufferSize(mSampleRate, deperecated, mFormat)
        mOutBufferSize = AudioTrack.getMinBufferSize(mSampleRate, deperecated, mFormat)
        mAudioInput = AudioRecord(MediaRecorder.AudioSource.MIC, mSampleRate, deperecated, mFormat, mInBufferSize)
      //  mAudioOutput = AudioTrack(AudioManager.STREAM_MUSIC, mSampleRate, deperecated, mFormat, mOutBufferSize, AudioTrack.MODE_STREAM)
        mAudioOutput = AudioTrack(
                      AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build(),
                      AudioFormat.Builder().setSampleRate(mSampleRate).setEncoding(mFormat).setChannelMask(AudioFormat.CHANNEL_OUT_STEREO).build(),
                      mOutBufferSize,
                      AudioTrack.MODE_STREAM,
                      AudioManager.AUDIO_SESSION_ID_GENERATE)
        record()
    }

    fun record() {
        val t =  Thread() {
                if (mAudioOutput!!.state != AudioTrack.STATE_INITIALIZED || mAudioInput!!.state != AudioTrack.STATE_INITIALIZED) {
                    Log.d(APP_TAG, "Can't start. Race condition?")
                } else {
                    try {
                        try {
                            mAudioOutput!!.play()
                        } catch (e: Exception) {
                            Log.e(APP_TAG, "Failed to start playback")
                            return@Thread
                        }
                        try {
                            mAudioInput!!.startRecording()
                        } catch (e: Exception) {
                            Log.e(APP_TAG, "Failed to start recording")
                            mAudioOutput!!.stop()
                            return@Thread
                        }
                        try {
                            val bytes = ByteBuffer.allocateDirect(mInBufferSize)
                            var o = 0
                            val b = ByteArray(mInBufferSize)
                            while (App.isRecording) { // if recording or not recording
                                o = mAudioInput!!.read(bytes, mInBufferSize)
                                bytes[b]
                                bytes.rewind()
                                mAudioOutput!!.write(b, 0, o)
                            }
                            Log.d(APP_TAG, "Finished recording")
                        } catch (e: Exception) {
                            Log.d(APP_TAG, "Error while recording, aborting.")
                        }
                        try {
                            mAudioOutput!!.stop()
                        } catch (e: Exception) {
                            Log.e(APP_TAG, "Can't stop playback")
                            mAudioInput!!.stop()
                            return@Thread
                        }
                        try {
                            mAudioInput!!.stop()
                        } catch (e: Exception) {
                            Log.e(APP_TAG, "Can't stop recording")
                            return@Thread
                        }
                    } catch (e: Exception) {
                        Log.d(APP_TAG, "Error somewhere in record loop.")
                    }
                }
        }
        t.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(4, news)
        return START_STICKY
    }
}