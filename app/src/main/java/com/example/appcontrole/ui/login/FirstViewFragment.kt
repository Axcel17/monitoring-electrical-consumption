package com.example.appcontrole.ui.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.appcontrole.R
import com.example.appcontrole.databinding.FragmentFirstViewBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FirstViewFragment : Fragment() {

    private var _binding: FragmentFirstViewBinding? = null
    private var listener: OnLoginButtonClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_first_view, container, false)

        val loginButton = view.findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener{
            listener?.onLoginButtonClicked()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnLoginButtonClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnLoginButtonClickListener {
        fun onLoginButtonClicked()
    }
}