package microphone.microphone.utlis
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import microphone.microphone.R
class FileListAdapter(private val c: Context, private val listNameFiles: ArrayList<String>) : BaseAdapter() {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val layotInflater   = LayoutInflater.from(c)
        val rowMain         = layotInflater.inflate(R.layout.row_main , viewGroup, false)
        val textStation     = rowMain.findViewById<TextView>(R.id.textStation)
        textStation.text = listNameFiles[position]
        //   val idName         = rowMain.findViewById<TextView>(R.id.idName)
        //   val text = listPerson[position].name
        //   var y = text.length - 1
        //   if ( y < 0 ){ }
        //   else {
        //       if( y > 21 ) y = 21
        //       val out = text.slice(0..y)
        //       idName.text = out
        //   }

        //   val idTlf          = rowMain.findViewById<TextView>(R.id.idTlf)
        //       idTlf.text     = listPerson[position].tlf
        return rowMain
    }

    override fun getItem(position: Int): Any {
        return listNameFiles[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listNameFiles.size
    }
}