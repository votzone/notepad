package wq.votzone.notebook.dialog

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import wq.votzone.notebook.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [WxDnaFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [WxDnaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WxDnaFragment : DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_wx_dna, container, false)
    }



}
