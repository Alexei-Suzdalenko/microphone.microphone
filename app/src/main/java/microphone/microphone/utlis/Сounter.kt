package microphone.microphone.utlis
import android.widget.TextView
import microphone.microphone.MainActivity
import microphone.microphone.R
import microphone.microphone.utlis.App.Companion.i
import microphone.microphone.utlis.App.Companion.thread
import java.io.File

object Сounter {
    val stringList: ArrayList<String> = ArrayList()


    fun countiong(activity: MainActivity){
        stringList.add("****-")
        stringList.add("***-*")
        stringList.add("**-**")
        stringList.add("*-***")
        stringList.add("-****")
        stringList.add("*-***")
        stringList.add("**-**")
        stringList.add("***-*")
        var x = "00"
        var result = ""
         thread = Thread{
            while (true){
                Thread.sleep(111)
                i++
                activity.runOnUiThread{
                    try{
                        val textViewCounter = activity.findViewById<TextView>(R.id.timeTextView)

                        if(i > 7) i = 0
                        if(App.sharedPreferences.getBoolean(Common.ISRECORD, false)){
                            textViewCounter.text = stringList[i]
                        } else {
                            textViewCounter.text = ""
                        }
                    } catch (e: Exception){}
                }
            }
        }
        thread!!.start()
    }

}