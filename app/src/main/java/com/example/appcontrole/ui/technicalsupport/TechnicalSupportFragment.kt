package com.example.appcontrole.ui.technicalsupport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.appcontrole.databinding.FragmentTechnicalSupportBinding

class TechnicalSupportFragment : Fragment() {

    private var _binding: FragmentTechnicalSupportBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val technicalSupportViewModel =
            ViewModelProvider(this).get(TechnicalSupportViewModel::class.java)

        _binding = FragmentTechnicalSupportBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textTechnicalSupport
        technicalSupportViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}