package ca.mcgill.ecse321.treeple_android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ModifyTreeFragment.OnFragmentInteractionListener, CreateTreeFragment.OnFragmentInteractionListener {
    private List<TreeDTO> trees = new ArrayList<>();
    private GoogleMap mMap;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.email = this.getIntent().getExtras().getString("email");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        //location of zooms
        LatLng montreal = new LatLng(45.516136, -73.656830);


        //adding trees in montreal
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(montreal, 10));
        refreshTrees(mMap);


        //long click event to add trees
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                CreateTreeFragment newFragment = new CreateTreeFragment();
                Bundle args = new Bundle();
                args.putDouble("latitude", point.latitude);
                args.putDouble("longitude", point.longitude);
                newFragment.setArguments(args);
                newFragment.show(getSupportFragmentManager(), "new Tree");

            }
        });

        //on bubble click
        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {

            @Override
            public void onInfoWindowLongClick(Marker marker) {
                marker.hideInfoWindow();
                ModifyTreeFragment newFragment = new ModifyTreeFragment();
                Bundle args = new Bundle();

                if (!email.contains("@")) {
                    createToast("go back and login");
                    return;
                }

                args.putString("email", email);
                args.putInt("treeid", findTreeGivenLocation(marker.getPosition()));
                newFragment.setArguments(args);
                newFragment.show(getSupportFragmentManager(), "modify Tree");
            }
        });
    }

    private int findTreeGivenLocation(LatLng position) {
        for (TreeDTO tree : trees) {
            if (tree.getTreeLocation().equals(position))
                return tree.getId();
        }

        return -1; //error
    }

    /**
     * messeges
     *
     * @param text
     */
    private void createToast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    public void createTree(String status, String species, String municipality, String diameter, double lat, double lng) {
        RequestParams params = new RequestParams();
        params.add("treestatus", status);
        params.add("treespecies", species);
        params.add("municipality", municipality);
        params.add("diameter", diameter);
        params.add("longitude", lng + "");
        params.add("latitude", lat + "");

        HttpUtils.post("trees/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                createToast("Success");
                refreshTrees(mMap);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                String error = "";
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                createToast(error);
            }
        });
    }

    private void refreshTrees(final GoogleMap mMap) {
        HttpUtils.get("trees/", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jo = response.getJSONObject(i);
                        trees.add(new TreeDTO(jo));
                    } catch (JSONException e) {
                        createToast("error parsing trees from source");
                    }

                    mMap.clear();
                    for (TreeDTO t : trees) {
                        mMap.addMarker(new MarkerOptions()
                                .position(t.getTreeLocation())
                                .title(t.getLower())
                                .snippet(t.toString())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parks)));
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable
                    throwable, JSONObject errorResponse) {
                String error = "";
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                createToast(error);
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void createTransaction(String status, String userEmail, String treeid) {
        String error = "";
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);

        RequestParams params = new RequestParams();
        params.add("resident", userEmail);
        params.add("tree", treeid);
        params.add("status", status);
        NumberFormat formatter = new DecimalFormat("00");
        params.add("time", formatter.format(hours) + ":" + formatter.format(minutes));
        params.add("date", year + "-" + formatter.format(month) + "-" + formatter.format(day));

        HttpUtils.post("transactions/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                createToast("success");
                refreshTrees(mMap);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                String error = "";
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                createToast(error);
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();

        if(mMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mMap.clear();

            // add markers from database to the map
            refreshTrees(mMap);
        }
    }
}

