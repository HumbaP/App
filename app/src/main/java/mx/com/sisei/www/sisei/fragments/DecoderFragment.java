package mx.com.sisei.www.sisei.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import mx.com.sisei.www.sisei.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DecoderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DecoderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DecoderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private OnFragmentInteractionListener mListener;

    public DecoderFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DecoderFragment newInstance() {
        DecoderFragment fragment = new DecoderFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    EditText decoder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View outView=inflater.inflate(R.layout.fragment_decoder, container, false);
        decoder=(EditText) outView.findViewById(R.id.decoder_input);
        decoder.setImeOptions(EditorInfo.IME_ACTION_DONE);
        return  outView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
