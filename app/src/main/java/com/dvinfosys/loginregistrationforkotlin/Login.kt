package com.dvinfosys.loginregistrationforkotlin

import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import android.view.View
import android.content.*
import android.os.*
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*

class Login : AppCompatActivity() {

    companion object {
        var edt1: EditText? = null
        var edt2: EditText? = null
        private var btn1: Button? = null
        private var txt1: TextView? = null
        private var em: String? = null
        private var pp: String? = null
        private var session: SessionManager? = null
        lateinit var context: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        context=this@Login
        edt1=findViewById<EditText>(R.id.edt1)
        edt2=findViewById(R.id.edt2)as EditText
        btn1=findViewById(R.id.btnlogin)as Button
        txt1=findViewById(R.id.txt1)as TextView

        var register=findViewById<TextView>(R.id.txt1)

        register!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                var i=Intent(this@Login,Register::class.java)
                startActivity(i)
            }
        })
        session= SessionManager(context)
        btn1!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                em = edt1!!.getText().toString()
                pp = edt2!!.getText().toString()

                HttpAsyncTask().execute("http://192.168.1.12/TandRAPI/api/Basic/GetLoginData")
            }
        })
    }
    class HttpAsyncTask : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            Common.showProgressDialog(context)
        }
        override fun doInBackground(vararg p0: String?): String {
            return POST(p0[0])
        }
        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            Common.dismissProgressDialog()

            if (s!=null){
                var status : String?=null
                var RegisterID : String?=null
                try {
                    var array = JSONArray(s)
                    var i=0
                    var i1=array.length()
                    while (i<i1){
                        val jsonObject : JSONObject=array.getJSONObject(i)
                        status= jsonObject.getString("Status")
                        RegisterID=jsonObject.getString("RegistrationID")
                        i++
                    }
                    session!!.setRegistrationID(RegisterID!!)

                    if (status!!.equals("1")){
                        session!!.setLogin(true)
                        var i=Intent(context,Welcome::class.java)
                        context.startActivity(i)
                    }
                }
                catch (e: JSONException) {
                    e.printStackTrace();
                }
            }else{
                Common.showToast(context,"Server Error")
            }

        }
        private fun POST(s: String?): String {
            var inputStream: InputStream? = null
            var result = ""
            try {
                // 1. create HttpClient
                val httpclient = DefaultHttpClient()

                // 2. make POST request to the given URL
                val httpPost = HttpPost(s)

                var json = ""

                // 3. build jsonObject
                val jsonObject = JSONObject()
                jsonObject.accumulate("EmailID", em)
                jsonObject.accumulate("password", pp)

                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString()

                // ** Alternative way to convert Person object to JSON string usin Jackson Lib
                // ObjectMapper mapper = new ObjectMapper();
                // json = mapper.writeValueAsString(Person);

                // 5. set json to StringEntity
                var se = StringEntity(json)

                // 6. set httpPost Entity
                httpPost.setEntity(se)

                // 7. Set some headers to inform server about the type of the content
                httpPost.setHeader("Accept", "application/json")
                httpPost.setHeader("Content-type", "application/json")

                // 8. Execute POST request to the given URL
                var httpResponse = httpclient.execute(httpPost)

                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent()

                // 10. convert inputstream to string
                if (inputStream != null)
                    result = convertInputStreamToString(inputStream)
                else
                    result = "Did not work!";
            } catch (e:Exception) {
                Log.d("InputStream", e.getLocalizedMessage())
            }
            // 11. return result
            return result;
        }

        private fun convertInputStreamToString(inputStream: InputStream): String {
            var bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var line = ""
            var result = ""
            while ((line == bufferedReader.readLine()))
                result += line

            inputStream.close()
            return result
        }
    }
}
