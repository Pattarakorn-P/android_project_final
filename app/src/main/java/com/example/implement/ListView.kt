import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.implement.CustomAdapter
import com.example.implement.R
import com.example.implement.fragment_detail
import org.json.JSONObject

class ListView: Fragment(){
private lateinit var listView: ListView

override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    val view = inflater.inflate(R.layout.fragment_list_view, container, false)

    listView = view.findViewById(R.id.listview)

    val    jsonString : String = loadJsonFromAsset("recipes.json", activity!!.baseContext).toString()
    val json = JSONObject(jsonString)
    val jsonArray = json.getJSONArray("recipes")

    val adapter = CustomAdapter(activity!!.baseContext, jsonArray)
    listView.adapter = adapter


    listView.setOnItemClickListener { parent:AdapterView<*>, view:View, position:Int, id:Long ->
        val req = parent.getItemAtPosition(position).toString()
        val res = JSONObject(req)
        val head = res.get("title").toString()
        val body = res.get("description").toString()
        val img = res.get("image").toString()
        val fragment_detail = fragment_detail().newInstance(head,body,img)
        val fm = fragmentManager
        val transaction : FragmentTransaction = fm!!.beginTransaction()
        transaction.replace(R.id.Layout, fragment_detail,"fragment_detail")
        transaction.addToBackStack("fragment_detail")
        transaction.commit()
    }
    
    
    return view
}

private fun loadJsonFromAsset(filename: String, context: Context): String? {
    val json: String?

    try {
        val inputStream = context.assets.open(filename)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer, Charsets.UTF_8)
    } catch (ex: java.io.IOException) {
        ex.printStackTrace()
        return null
    }

    return json
}
}
