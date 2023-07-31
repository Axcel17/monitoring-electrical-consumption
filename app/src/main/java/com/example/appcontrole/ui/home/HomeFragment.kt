package com.example.appcontrole.ui.home

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.appcontrole.R
import com.example.appcontrole.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val database = FirebaseDatabase.getInstance()
    private val referenceSensor = database.getReference("sensors")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // doesn't work
        val user = arguments?.getString("user").toString()
        Log.d("user", user)
        //
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val btnOpenDialog = view.findViewById<FloatingActionButton>(R.id.floatingActionButton_home)
        btnOpenDialog.setOnClickListener {
            showDialog()
        }
        val textViewUser = view.findViewById<TextView>(R.id.textView_user)
        textViewUser.text = "admin"

        val linearLayout: LinearLayout = view.findViewById(R.id.sensorsInHome)

        referenceSensor.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                linearLayout.removeAllViews()
                val value = dataSnapshot.value
                if (value is Map<*, *>) {
                    val sensors = value as Map<*, *>
                    var sensorsMap = sensors.values.filterIsInstance<Map<*, *>>()
                        .filter { it["user"] == "admin" }
                    sensorsMap.forEach { sensor ->
                        run {
                            val view = inflater.inflate(R.layout.sensor_home, container, false)
                            view.findViewById<TextView>(R.id.nameSensor).text = sensor["name"].toString()
                            view.findViewById<ImageView>(R.id.levelBatteryImage).setImageResource(levelBatteryNeed(sensor))
                            view.findViewById<ImageView>(R.id.imageViewActuator).setImageResource(actuatorSelected(sensor))
                            linearLayout.addView(view)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Error al leer los datos: ${error.message}")
            }
        })
        return view
    }

    private fun levelBatteryNeed(sensor : Map<*, *>) : Int {
        val measure =  sensor["measure"].toString().toDouble()
        val power = sensor["power"].toString().toDouble()
        if (measure < power*0.8){
            return R.drawable.low
        }
        else if (measure > power*1.2){
            return R.drawable.over
        }
        else {
            return R.drawable.mid
        }
    }

    private fun actuatorSelected(sensor : Map<*, *>) : Int {
        val name =  sensor["name"].toString()

        return if (name.equals("Lavadora", ignoreCase = true)){
            R.drawable.laundry_machine
        } else if (name.equals("Aire acondicionado", ignoreCase = true)){
            R.drawable.air_conditioner
        } else if (name.equals("Licuadora", ignoreCase = true)){
            R.drawable.blender
        } else {
            R.drawable.sensor
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.fragment_add_object)
        dialog.setCancelable(true)

        val btnClose = dialog.findViewById<Button>(R.id.btnClose)
        btnClose.setOnClickListener {
            saveSensor(dialog)
        }

        val spinner = dialog.findViewById<Spinner>(R.id.spinnerVoltage)
        val optionsArray = resources.getStringArray(R.array.voltageArray)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, optionsArray)

        // Opcional: Define el diseño de las opciones desplegables
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asigna el ArrayAdapter al Spinner
        spinner.adapter = adapter

        dialog.show()
    }

    private fun saveSensor(dialog: Dialog) {
        val textDeviceName = dialog.findViewById<EditText>(R.id.editTextName).text.toString()
        val spinnerVoltage = dialog.findViewById<Spinner>(R.id.spinnerVoltage).selectedItem.toString().split(" ")[0].toIntOrNull()
        val textPower = dialog.findViewById<EditText>(R.id.editTextPower).text.toString().toDoubleOrNull()
        val textCode = dialog.findViewById<EditText>(R.id.editTextCode).text.toString()

        if (textCode != "" && textPower != null && textDeviceName != ""&& spinnerVoltage != null){
            referenceSensor.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val dataSnapshot: DataSnapshot? = task.result
                    if (dataSnapshot != null && dataSnapshot.exists()) {
                        val value = dataSnapshot.value
                        if (value is Map<*, *>) {
                            // sensor exists?
                           if(value.keys.contains(textCode)){
                               val dataSensor = value[textCode] as Map<*,*>
                               Log.d("user", dataSensor["user"].toString() )
                               if (dataSensor["user"] == null){
                                   val dataToSave = mapOf(
                                       "user" to "admin",
                                       "name" to textDeviceName,
                                       "voltage" to spinnerVoltage,
                                       "power" to textPower
                                   )
                                   referenceSensor.child(textCode).updateChildren(dataToSave).addOnCompleteListener { task ->
                                       if (task.isSuccessful) {
                                           Toast.makeText(requireContext(), "Código registrado", Toast.LENGTH_SHORT).show()
                                           dialog.dismiss()
                                       } else {
                                           Toast.makeText(requireContext(), "Inténtelo nuevamente", Toast.LENGTH_SHORT).show()
                                       }
                                   }
                               } else {
                                   Toast.makeText(requireContext(), "Código de referencia ya asingado", Toast.LENGTH_SHORT).show()
                               }
                           }
                            else {
                               Toast.makeText(requireContext(), "Código de referencia no válido", Toast.LENGTH_SHORT).show()
                           }
                        }

                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), "Antes de guardar, llene todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}