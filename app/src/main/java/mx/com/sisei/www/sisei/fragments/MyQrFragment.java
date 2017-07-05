package mx.com.sisei.www.sisei.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import mx.com.sisei.www.sisei.R;
import mx.com.sisei.www.sisei.utils.SavingUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyQrFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyQrFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyQrFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    public MyQrFragment() {
        // Required empty public constructor
    }


    public static MyQrFragment newInstance() {
        MyQrFragment fragment = new MyQrFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View outView=inflater.inflate(R.layout.fragment_my_qr, container, false);
        final ImageView image= (ImageView) outView.findViewById(R.id.my_qr_image);
        Picasso.with(getContext()).load("https://bloodmprojects.com/SecretSXI/index.php/Usuario_controller/me_qr/"+ SavingUtils.preferences.getString(getString(R.string.shared_fbId),"")+"/Android/"+SavingUtils.preferences.getString(getString(R.string.shared_master1),"")+"/"+SavingUtils.preferences.getString(getString(R.string.shared_master2),"")).into(image, new Callback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError() {
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });
        return outView;
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
