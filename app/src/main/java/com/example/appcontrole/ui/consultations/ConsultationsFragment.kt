package com.example.appcontrole.ui.consultations

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.appcontrole.databinding.FragmentConsultationsBinding
import com.example.appcontrole.ui.records.RecordsViewModel

class ConsultationsFragment : Fragment() {

    private var _binding: FragmentConsultationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val consultationViewModel =
            ViewModelProvider(this).get(ConsultationsViewModel::class.java)

        _binding = FragmentConsultationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textConsultations
        consultationViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}