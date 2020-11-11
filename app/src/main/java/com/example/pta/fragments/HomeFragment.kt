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
import android.widget.Toast
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

    var freeFireTV: TextView? = null
    var pubgTV: TextView? = null
    var pubgLiteTV: TextView? = null
    var ludoTV: TextView? = null

    var freeFire: LinearLayout? = null
    var pubg:LinearLayout? = null
    var pubgLite:LinearLayout? = null
    var ludo:LinearLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        freeFire?.setOnClickListener(View.OnClickListener {

            go("FreeFire")

        })
        pubg?.setOnClickListener(View.OnClickListener {

            go("PUBG")

        })

        pubgLite?.setOnClickListener(View.OnClickListener {

            go("PUBG Lite")

        })

        ludo?.setOnClickListener(View.OnClickListener {

            go("Ludo")

        })

    }

    private fun go(value: String){

        var i = Intent(context, MatchShowActivity::class.java)
        i.putExtra("category", value)
        startActivity(i)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var root =  inflater.inflate(R.layout.fragment_home, container, false)


        saveUserInfo = SaveUserInfo(context!!)
        //Toast.makeText(context, "" + saveUserInfo!!.number, Toast.LENGTH_SHORT).show()

        freeFireTV = root.findViewById<TextView>(R.id.freeFireStatus)
        pubgTV = root.findViewById<TextView>(R.id.PUBGMatchStatus)

        pubgLiteTV = root.findViewById<TextView>(R.id.pubgLiteGameStatus)
        ludoTV = root.findViewById<TextView>(R.id.ludoGameStatus)

        freeFire = root.findViewById(R.id.freeFireGame)
        pubg = root.findViewById(R.id.pubgGame)
        pubgLite = root.findViewById(R.id.pubgLiteGame)
        ludo = root.findViewById(R.id.ludoGame)

        getFreeFireMatch("FreeFire")
        getPubgMatch("PUBG")
        getPubgLiteMatch("PUBG Lite")
        getLudoMatch("Ludo")


        val container = root.findViewById(R.id.shimmer_view_container) as ShimmerFrameLayout
        container.startShimmer() // If auto-start is set to false

        val container2 = root.findViewById(R.id.shimmer_view_container2) as ShimmerFrameLayout
        container2.startShimmer() // If auto-start is set to false


        val imageList = ArrayList<SlideModel>() // Create image list
        imageList.add(SlideModel("https://drive.google.com/uc?export=view&id=1z8_YQkKQhBizVOhr8UZ186mLSxEAlRZq", "Official Youtube Channel", ScaleTypes.FIT))
        imageList.add(SlideModel("https://drive.google.com/uc?export=view&id=112fheeg2dmI0uH9YD2h3qeGgYZezPRVW", "JOIN NOW ON TELEGRAM", ScaleTypes.FIT))
        imageList.add(SlideModel("https://i.ytimg.com/vi/pKUu6PKNyzk/maxresdefault.jpg", "", ScaleTypes.FIT))
        imageList.add(SlideModel("https://mobilemodegaming.s3.ap-south-1.amazonaws.com/wp-content/uploads/2019/12/Free-Fire-TDM-Mode-1.png", "", ScaleTypes.FIT))
        imageList.add(SlideModel("https://staticg.sportskeeda.com/editor/2020/06/1d27a-15925520527800-500.jpg", "", ScaleTypes.FIT))


        val imageSlider = root.findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)
        imageSlider.startSliding(5000) // with new period
        imageSlider.startSliding()
        imageSlider.stopSliding()
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {

                if (position == 0) {

                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://m.youtube.com/channel/UCgTbF5i9nrQuMRgvS1YwGtw")))
                    } catch (e: ActivityNotFoundException) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://m.youtube.com/channel/UCgTbF5i9nrQuMRgvS1YwGtw")))
                    }


                } else if (position == 1) {

                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/joinchat/PdPVixfYYxmDAxXjQwrz2w")))
                    } catch (e: ActivityNotFoundException) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/joinchat/PdPVixfYYxmDAxXjQwrz2w")))
                    }

                } else if (position == 2) {

                } else if (position == 3) {

                } else if (position == 4) {

                }
            }
        })

        return root;
    }

    private fun getLudoMatch(category: String){

        val url = getString(R.string.BASS_URL)+"getMatch"
        val stringRequest = @SuppressLint("SetTextI18n")
        object : StringRequest(Request.Method.POST, url,
                Response.Listener<String> { response ->
                    try {
                        val obj = JSONObject(response)
                        val res = obj.getString("list")
                        val jArray = JSONArray(res)
                        ludoTV!!.text = "" + jArray.length() + " match found"

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
                params.put("category", category)
                return params
            }
    }
        val queue = Volley.newRequestQueue(context)
        queue.add(stringRequest)
    }

   private fun getPubgLiteMatch(category: String){

        val url = getString(R.string.BASS_URL)+"getMatch"
        val stringRequest = @SuppressLint("SetTextI18n")
        object : StringRequest(Request.Method.POST, url,
                Response.Listener<String> { response ->
                    try {
                        val obj = JSONObject(response)
                        val res = obj.getString("list")
                        val jArray = JSONArray(res)
                        pubgLiteTV!!.text = "" + jArray.length() + " match found"

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
                params.put("category", category)
                return params
            }
    }
        val queue = Volley.newRequestQueue(context)
        queue.add(stringRequest)
    }

   private fun getPubgMatch(category: String){

        val url = getString(R.string.BASS_URL)+"getMatch"
        val stringRequest = @SuppressLint("SetTextI18n")
        object : StringRequest(Request.Method.POST, url,
                Response.Listener<String> { response ->
                    try {
                        val obj = JSONObject(response)
                        val res = obj.getString("list")
                        val jArray = JSONArray(res)
                        pubgTV!!.text = "" + jArray.length() + " match found"

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
                params.put("category", category)
                return params
            }
    }
        val queue = Volley.newRequestQueue(context)
        queue.add(stringRequest)
    }


  private fun getFreeFireMatch(category: String){

        val url = getString(R.string.BASS_URL)+"getMatch"
        val stringRequest = @SuppressLint("SetTextI18n")
        object : StringRequest(Request.Method.POST, url,
                Response.Listener<String> { response ->
                    try {
                        val obj = JSONObject(response)
                        val res = obj.getString("list")
                        val jArray = JSONArray(res)
                        freeFireTV!!.text = "" + jArray.length() + " match found"

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
                params.put("category", category)
                return params
            }
    }
        val queue = Volley.newRequestQueue(context)
        queue.add(stringRequest)
    }


}