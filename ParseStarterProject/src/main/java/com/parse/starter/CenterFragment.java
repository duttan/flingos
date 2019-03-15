package com.parse.starter;

import android.Manifest;
import android.content.CursorLoader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.starter.Adapters.ContactAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CenterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CenterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CenterFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final int REQUEST_PERMISSION = 2001 ;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int LOADER_ID = 1;
    private RecyclerView mflingosrecycler;
    private ContactAdapter mContactsAdapter;
    private View mRootview;

    private static final String[] FROM_COLOUMNS = {
            ContactsContract.Data.CONTACT_ID,
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? ContactsContract.Data.DISPLAY_NAME_PRIMARY : ContactsContract.Data.DISPLAY_NAME,
            ContactsContract.Data.PHOTO_ID
    };

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Bundle mbundle;
    private OnFragmentInteractionListener mListener;


    public CenterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CenterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CenterFragment newInstance(String param1, String param2) {
        CenterFragment fragment = new CenterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            setRetainInstance(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_PERMISSION)
        {
            if(grantResults.length != 0)
            {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    setinitAdapter();

                }
                else
                    {
                    getFragmentManager().popBackStack();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setRetainInstance(true);
        mRootview = inflater.inflate(R.layout.fragment_center,container,false);
        mbundle = savedInstanceState;
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.READ_CONTACTS
            },REQUEST_PERMISSION);

             getLoaderManager().initLoader(LOADER_ID,savedInstanceState,this);
        }
        else
        {
            setinitAdapter();
            getLoaderManager().initLoader(LOADER_ID,savedInstanceState,this);

        }


        return mRootview;
    }

    void setinitAdapter()
    {
        mflingosrecycler = (RecyclerView) mRootview.findViewById(R.id.contactsrecycler);
        mflingosrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mflingosrecycler.setHasFixedSize(true);

        mContactsAdapter = new ContactAdapter(getContext(), null, ContactsContract.Data.CONTACT_ID);
        mflingosrecycler.setAdapter(mContactsAdapter);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.READ_CONTACTS
            },REQUEST_PERMISSION);

            //getLoaderManager().initLoader(LOADER_ID,null,this);
        }
        else
        {
            setinitAdapter();
            getLoaderManager().initLoader(LOADER_ID,null,this);

        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id)
        {
            case LOADER_ID:
                return new android.support.v4.content.CursorLoader(getContext(),
                        ContactsContract.Data.CONTENT_URI,FROM_COLOUMNS,
                        null,
                        null,
                        ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? ContactsContract.Data.DISPLAY_NAME_PRIMARY : ContactsContract.Data.DISPLAY_NAME)
                        );
                default:
                    if(BuildConfig.DEBUG)
                    {
                        throw new IllegalArgumentException("No id handled");
                    }
                    return null;
        }
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mContactsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mContactsAdapter.swapCursor(null);

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
