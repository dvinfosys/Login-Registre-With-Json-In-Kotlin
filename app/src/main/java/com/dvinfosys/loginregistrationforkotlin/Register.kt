package com.dvinfosys.loginregistrationforkotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.R.attr.bitmap
import android.content.Context
import android.os.AsyncTask
import android.util.Base64
import android.util.Log
import android.widget.RadioButton
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.json.JSONObject
import java.io.*


class Register : AppCompatActivity() {

    companion object {
        private val txt11: TextView? = null
        private var edt111: EditText? = null
        var edt222: EditText? = null
        var edt333: EditText? = null
        var edt444: EditText? = null
        private var radio2: RadioGroup? = null
        private var rbtn3: RadioButton? = null
        var rbtn4: RadioButton? = null
        private var button1: Button? = null
        var btn1: Button? = null
        private var image1: ImageView? = null
        private var txt24: TextView? = null
        private var FirstName: String? = null
        var LastName: String? = null
        var EmailID: String? = null
        var Password: String? = null
        var image: String? = null
        private var Gender: String? = null
        private val registergetset: RegisterGetSet? = null
        var context:Context?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        context=this@Register
        edt111=findViewById(R.id.ed11)as EditText
        edt222=findViewById(R.id.ed22)as EditText
        edt333=findViewById(R.id.ed33)as EditText
        edt444=findViewById(R.id.ed44)as EditText
        radio2=findViewById(R.id.radio2)as RadioGroup
        rbtn3=findViewById(R.id.rbtn3)as RadioButton
        rbtn4=findViewById(R.id.rbtn4)as RadioButton
        button1=findViewById(R.id.button1)as Button
        image1=findViewById(R.id.image1)as ImageView
        txt24=findViewById(R.id.txt24)as TextView
        btn1=findViewById(R.id.btn1)as Button

        btn1!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                FirstName = edt111!!.getText().toString()
                LastName = edt222!!.getText().toString()
                EmailID = edt333!!.getText().toString()
                Password = edt444!!.getText().toString()
                rbtn3!!.getText().toString()
                rbtn4!!.getText().toString()
                image = txt24!!.getText().toString()

                HttpAsyncRegister().execute("http://192.168.1.12/TandRAPI/api/Basic/Registration")
            }
        })
        button1!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 101)
        })
    }

    class HttpAsyncRegister : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            Common.showProgressDialog(context)
        }
        override fun doInBackground(vararg p0: String?): String {
            return POST(p0[0]);
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Common.dismissProgressDialog()
            Common.showToast(context,"Registration Successfully")
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
                jsonObject.accumulate("FirstName", FirstName);
                jsonObject.accumulate("LastName", LastName);
                jsonObject.accumulate("EmailID", EmailID);
                jsonObject.accumulate("Password", Password);
                jsonObject.accumulate("Gender", Gender);
                jsonObject.accumulate("image", image);

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
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                // 8. Execute POST request to the given URL
                var httpResponse = httpclient.execute(httpPost);

                // 9. receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // 10. convert inputstream to string
                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";
            } catch (e:Exception) {
                Log.d("InputStream", e.getLocalizedMessage());
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK && null != data) {
            if (requestCode == 101 && resultCode == RESULT_OK && null != data) {

               var targetUri = data.getData()
                var bitmap:Bitmap
                var resizedBitmap :Bitmap
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false)
                    image = ConvertBitmapToString(resizedBitmap);
                    image1!!.setImageBitmap(resizedBitmap);
                    txt24!!.setText(image);
                } catch (e: FileNotFoundException) {
                    e.printStackTrace();
                }
            }
        }
    }

    private fun ConvertBitmapToString(bitmap: Bitmap?): String? {
        var encodedimage = ""
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val ba = byteArrayOutputStream.toByteArray()
        val ba1 = Base64.encodeToString(ba, Base64.DEFAULT)
        encodedimage = "data:image/png;base64," + ba1
        return encodedimage
    }
    fun RadioButtonClicked(view :View){
        val selectgender = ""
        val checked = (view as RadioButton).isChecked
        when (view.getId()) {
            R.id.rbtn3 -> if (checked)
                Gender = "Male"
            R.id.rbtn4 -> if (checked)
                Gender = "Female"
        }
    }
}
