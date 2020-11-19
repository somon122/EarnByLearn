package com.example.pta.fragments

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.pta.R
import com.example.pta.SaveUserInfo
import com.example.pta.match.MatchShowActivity
import com.facebook.shimmer.ShimmerFrameLayout
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class HomeFragment : Fragment() {


    var saveUserInfo: SaveUserInfo? = null

    var dailyMatchesTV: TextView? = null
    var weeklyGameTV: TextView? = null

    var dailyGame: LinearLayout? = null
    var weeklyGame:LinearLayout? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dailyGame?.setOnClickListener(View.OnClickListener {

            go("Daily_Match")

        })
        weeklyGame?.setOnClickListener(View.OnClickListener {

            go("Weekly_Match")

        })


    }

    private fun go(value: String){

        var i = Intent(context, MatchShowActivity::class.java)
        i.putExtra("type", value)
        startActivity(i)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var root =  inflater.inflate(R.layout.fragment_home, container, false)


        saveUserInfo = SaveUserInfo(context!!)
        //Toast.makeText(context, "" + saveUserInfo!!.number, Toast.LENGTH_SHORT).show()

        dailyMatchesTV = root.findViewById<TextView>(R.id.dailyMatchesStatus)
        dailyGame = root.findViewById(R.id.dailyMatchGame)

        weeklyGameTV = root.findViewById<TextView>(R.id.weeklyMatchesStatus)
        weeklyGame = root.findViewById(R.id.weeklyMatchGame)

        getDailyMatch("Daily_Match")
        getWeeklyMatch("Weekly_Match")

        val container = root.findViewById(R.id.shimmer_view_container) as ShimmerFrameLayout
        container.startShimmer() // If auto-start is set to false

        val container2 = root.findViewById(R.id.shimmer_view_container2) as ShimmerFrameLayout
        container2.startShimmer() // If auto-start is set to false


        val imageList = ArrayList<SlideModel>() // Create image list
        imageList.add(SlideModel("https://data-flair.training/blogs/wp-content/uploads/sites/2/2018/10/QlikView-Quiz-Questions-3.jpg", "", ScaleTypes.FIT))
        imageList.add(SlideModel("https://i.ytimg.com/vi/6b4hSESA33A/maxresdefault.jpg", "", ScaleTypes.FIT))
        imageList.add(SlideModel("https://fiverr-res.cloudinary.com/images/q_auto,f_auto/gigs/125491016/original/ef0669c68fbfc005d267b10718809b790729bf06/create-mcq-questions-for-the-quiz-test.jpg", "", ScaleTypes.FIT))


        val imageSlider = root.findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)
        imageSlider.startSliding(5000) // with new period
        imageSlider.startSliding()
        imageSlider.stopSliding()
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
            }
        })

        return root;
    }


   private fun getWeeklyMatch(category: String){

        val url = getString(R.string.BASS_URL)+"getMatch"
        val stringRequest = @SuppressLint("SetTextI18n")
        object : StringRequest(Request.Method.POST, url,
                Response.Listener<String> { response ->
                    try {
                        val obj = JSONObject(response)
                        val res = obj.getString("list")
                        val jArray = JSONArray(res)
                        weeklyGameTV!!.text = "" + jArray.length() + " match found"

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(volleyError: VolleyError) {

                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("type", category)
                return params
            }
    }
        val queue = Volley.newRequestQueue(context)
        queue.add(stringRequest)
    }


  private fun getDailyMatch(type: String){

        val url = getString(R.string.BASS_URL)+"getMatch"
        val stringRequest = @SuppressLint("SetTextI18n")
        object : StringRequest(Request.Method.POST, url,
                Response.Listener<String> { response ->
                    try {
                        val obj = JSONObject(response)
                        val res = obj.getString("list")
                        val jArray = JSONArray(res)
                        dailyMatchesTV!!.text = "" + jArray.length() + " match found"

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                object : Response.ErrorListener {
                    override fun onErrorResponse(volleyError: VolleyError) {
                        // startActivity(Intent(context, MainActivity::class.java))
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("type", type)
                return params
            }
    }
        val queue = Volley.newRequestQueue(context)
        queue.add(stringRequest)
    }


}