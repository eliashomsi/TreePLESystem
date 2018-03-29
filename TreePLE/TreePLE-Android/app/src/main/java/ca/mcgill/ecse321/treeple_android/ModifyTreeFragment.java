package ca.mcgill.ecse321.treeple_android;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateTreeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateTreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyTreeFragment extends DialogFragment implements View.OnClickListener {
    //arguments
    private String status;
    private String userEmail;

    //private list of municipalities/ statuses/ and species
    private ArrayList<String> statusList;
    private ArrayAdapter<String> statusAdapter;
    private Spinner statusSpinner;
    private int treeid;

    //buttons
    private Button submitButton;
    private Button cancelButton;

    //EditText
    private EditText diameterEditText;

    private OnFragmentInteractionListener mListener;

    public ModifyTreeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateTreeFragment.
     */
    public static ModifyTreeFragment newInstance() {
        ModifyTreeFragment fragment = new ModifyTreeFragment();
        Bundle args = new Bundle();
        return fragment;
    }


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        this.userEmail = args.getString("email");
        this.treeid = args.getInt("treeid");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * refresh the spinners list
     */
    private void refresh() {
        HttpUtils.get("treestatuslist/", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                statusList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        statusList.add(response.getString(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                statusAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                String error = "";
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", error);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modify_tree, container, false);

        //init the lists
        statusList = new ArrayList<>();

        //setting up the spinners
        statusSpinner = (Spinner) view.findViewById(R.id.statusSpinner);

        statusAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, statusList);

        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        statusSpinner.setAdapter(statusAdapter);

        refresh();

        //get the rest of the elements
        submitButton = (Button) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        diameterEditText = (EditText) view.findViewById(R.id.diameterEditText);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v == submitButton) {
            MapsActivity myActivity = (MapsActivity) getActivity();
            this.status = statusSpinner.getSelectedItem().toString();

            myActivity.createTransaction(status, userEmail, treeid+"");
            getDialog().dismiss();
        } else if (v == cancelButton) {
            //cancel action
            getDialog().dismiss();
        }
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
