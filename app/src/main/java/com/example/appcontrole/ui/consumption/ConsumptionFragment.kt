package com.example.appcontrole.ui.consumption

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.example.appcontrole.R
import com.example.appcontrole.databinding.FragmentConsumptionBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ConsumptionFragment : Fragment() {
    private var _binding: FragmentConsumptionBinding? = null

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
        val view = inflater.inflate(R.layout.fragment_consumption, container, false)
        val borderDrawable = GradientDrawable()
        borderDrawable.shape = GradientDrawable.RECTANGLE
        borderDrawable.setStroke(7, ContextCompat.getColor(requireContext(), R.color.orange)) // Set the border color and width

        // Create a layer drawable to add the border to your LinearLayout
        val layerDrawable = LayerDrawable(arrayOf(borderDrawable))
        // Set the index 0 (the first and only layer) to your LinearLayout
        layerDrawable.setLayerInset(0, 0, 0, 0, 0)

        // Set the layer drawable as the background of your LinearLayout
        val linearLayoutTitle: LinearLayout = view.findViewById(R.id.LinearLayoutTitleConsumption)
        val linearLayoutDevices: LinearLayout = view.findViewById(R.id.LinearLayoutDevicesConsumption)
        linearLayoutTitle.background = layerDrawable
        linearLayoutDevices.background = layerDrawable
        val optionsArray = resources.getStringArray(R.array.hoursArray)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, optionsArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        referenceSensor.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                linearLayoutDevices.removeAllViews()
                val value = dataSnapshot.value
                if (value is Map<*, *>) {
                    val sensors = value as Map<*, *>
                    var sensorsMap = sensors.values.filterIsInstance<Map<*, *>>()
                        .filter { it["user"] == "admin" }
                    sensorsMap.forEach { sensor ->
                        val horizontalLayout = inflater.inflate(R.layout.sensor_consumption, container, false)
                        horizontalLayout.findViewById<ConstraintLayout>(R.id.LinearLayoutSensorConsumption).background = layerDrawable
                        horizontalLayout.findViewById<ImageView>(R.id.imagenViewActuatorCons).setImageResource(actuatorSelected(sensor))


                        val spinner = horizontalLayout.findViewById<Spinner>(R.id.spinnerTimes)
                        spinner.adapter = adapter
                        // Agregar el OnItemSelectedListener al Spinner
                        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                linearLayoutDevices.background = layerDrawable
                                linearLayoutTitle.background = layerDrawable
                                linearLayoutDevices.background = layerDrawable
                                // Acción que se realizará cuando cambia el valor seleccionado
                                val timeSelected = optionsArray[position].toString().split(" ")[0].toIntOrNull()
                                if (timeSelected != null) {
                                    val energeticConsumption = sensor["power"].toString().toDouble() * timeSelected
                                    horizontalLayout.findViewById<TextView>(R.id.textViewConsumption)
                                        .text = String.format("%.${2}f kWh", energeticConsumption)
                                    horizontalLayout.findViewById<TextView>(R.id.textViewPrice)
                                        .text = String.format("$%.${2}f", energeticConsumption*0.5)
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // Acción que se realizará si no hay ningún elemento seleccionado
                            }
                        }
                        linearLayoutDevices.addView(horizontalLayout)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Error al leer los datos: ${error.message}")
            }
        })
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
}