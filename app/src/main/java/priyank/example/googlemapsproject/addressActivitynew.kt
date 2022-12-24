package priyank.example.googlemapsproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

lateinit var  inputLat: EditText
lateinit var inputLong: EditText
lateinit var  address: TextView
lateinit var  locationButton: Button


class addressActivitynew : AppCompatActivity(),View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_activitynew)

        inputLat = findViewById(R.id.editTextLat)
        inputLong = findViewById(R.id.editTextLng)
        address = findViewById(R.id.textViewAddressFound)
        inputLat.setText("43.01244");
        inputLong.setText("-81.20018")
        address.setText("Fanshawe")
        locationButton = findViewById(R.id.buttonUseNew)

    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.buttonUseNew->{
                val intent = Intent(this, MapsActivity::class.java).apply {


                    var intent: Intent = Intent()
                    var inLat: Editable? = inputLat.text
                    var inLong: Editable = inputLong.text
                    intent.putExtra("passingLat", inLat)

                    intent.putExtra("passingLog", inLat)

                    setResult(RESULT_OK, intent)
                    startActivity(intent)
                }
            }
        }

    }


}