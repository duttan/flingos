package com.parse.starter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.starter.Adapters.ContactAdapter;
import com.parse.starter.Adapters.Contact_adapter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;


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
    private Contact_adapter contact_adapter;
    private View mRootview;
    private ArrayList<Contact_model> contactarrayList;
    private static ProgressDialog pd;



    private static final String[] FROM_COLOUMNS = {
            ContactsContract.Data.CONTACT_ID,
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? ContactsContract.Data.DISPLAY_NAME_PRIMARY : ContactsContract.Data.DISPLAY_NAME,
            ContactsContract.Data.PHOTO_ID,



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
        mflingosrecycler = (RecyclerView) mRootview.findViewById(R.id.contactsrecycler);
        mflingosrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mflingosrecycler.setHasFixedSize(true);
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS},REQUEST_PERMISSION);
            setinitAdapter();
           // getLoaderManager().initLoader(LOADER_ID,savedInstanceState,this);
            new LoadContacts().execute();
        }
        else
        {
            setinitAdapter();
            //getLoaderManager().initLoader(LOADER_ID,savedInstanceState,this);
            new LoadContacts().execute();

            }

        return mRootview;
    }

    //TODO change this Async to Rxjava
    private class LoadContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            contactarrayList = readContacts();
            contactarrayList = intelligent_Sort(contactarrayList);/// Get contacts array list from this
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            // If array list is not null and is contains value
            if (contactarrayList != null && contactarrayList.size() > 0) {

                // then set total contacts to subtitle
                //getSupportActionBar().setSubtitle(contactarrayList.size() + " Contacts");
                contact_adapter = null;
                if (contact_adapter == null) {
                    contact_adapter = new Contact_adapter(getContext(), contactarrayList);
                    mflingosrecycler.setAdapter(contact_adapter);// set adapter
                }
                contact_adapter.notifyDataSetChanged();
            } else {

                // If adapter is null then show toast
                Toast.makeText(getContext(), "There are no contacts.", Toast.LENGTH_LONG).show();
            }

            // Hide dialog if showing
            if (pd.isShowing())
                pd.dismiss();

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Show Dialog
            pd = ProgressDialog.show(getContext(), "Loading Contacts", "Please Wait...");
        }

    }

    private ArrayList<Contact_model> readContacts() {
        ArrayList<Contact_model> contactList = new ArrayList<Contact_model>();

        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        Cursor contactsCursor = getActivity().getContentResolver().query(uri, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC ");

        if (contactsCursor.moveToFirst()) {
            do {
                // Strings to get all details
                String displayName = "";
                String nickName = "";
                String homePhone = "";
                String mobilePhone = "";
                String workPhone = "";
                String photoPath = "" + R.mipmap.user; // Photo path
                byte[] photoByte = null;// Byte to get photo since it will come in BLOB
                String homeEmail = "";
                String workEmail = "";
                String companyName = "";
                String title = "";

                // This strings stores all contact numbers, email and other
                // details like nick name, company etc.
                List<String> contactNumbers = new ArrayList<String>();
                String contactEmailAddresses = "";
                String contactOtherDetails = "";








                long contctId = contactsCursor.getLong(contactsCursor.getColumnIndex("_ID"));  // Get contact ID

                Uri dataUri = ContactsContract.Data.CONTENT_URI; // URI to get data of contacts

                Cursor dataCursor = getActivity().getContentResolver().query(dataUri, null, ContactsContract.Data.CONTACT_ID + " = " + contctId, null, null);// Retrun data cusror represntative to contact ID

                // starting the cusrsor
                if (dataCursor.moveToFirst()) {
                    displayName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); // get contact name

                    do {
                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)) {
                            nickName = dataCursor.getString(dataCursor.getColumnIndex("data1")); // Get Nick Name
                            contactOtherDetails += "NickName : " + nickName + "n";    // Add the nick name to string
                        }

                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {

                            // In this get All contact numbers like home,mobile, work, etc and add them to numbers string

                            switch (dataCursor.getInt(dataCursor.getColumnIndex("data2"))) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    homePhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                   // contactNumbers += "Home Phone : " + homePhone + "n";
                                   // contactNumbers.add(homePhone);
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    workPhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                   // contactNumbers += "Work Phone : " + workPhone + "n";
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    mobilePhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                  //  contactNumbers += "Mobile Phone : " + mobilePhone + "n";
                                    contactNumbers.add(mobilePhone);
                                    break;

                            }
                        }
                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {

                            // In this get all Emails like home, work etc and
                            // add them to email string

                            switch (dataCursor.getInt(dataCursor.getColumnIndex("data2"))) {
                                case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                                    homeEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                    contactEmailAddresses += "Home Email : " + homeEmail + "n";
                                    break;

                                case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                                    workEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                    contactEmailAddresses += "Work Email : " + workEmail + "n";
                                    break;
                            }
                        }

                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {

                            companyName = dataCursor.getString(dataCursor.getColumnIndex("data1"));// get company name
                            contactOtherDetails += "Coompany Name : " + companyName + "n";
                            title = dataCursor.getString(dataCursor.getColumnIndex("data4"));// get Company title
                            contactOtherDetails += "Title : " + title + "n";

                        }

                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype")).equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE))
                        {
                            photoByte = dataCursor.getBlob(dataCursor.getColumnIndex("data15")); // get photo in byte

                            if (photoByte != null) {

                                // Now make a cache folder in file manager to
                                // make cache of contacts images and save them
                                // in .png

                                Bitmap bitmap = BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length);
                                File cacheDirectory = getActivity().getBaseContext().getCacheDir();
                                File tmp = new File(cacheDirectory.getPath() + "/_androhub" + contctId + ".png");

                                try {
                                    FileOutputStream fileOutputStream = new FileOutputStream(tmp);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                                    fileOutputStream.flush();
                                    fileOutputStream.close();
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }
                                photoPath = tmp.getPath();// finally get the saved path of image

                            }

                        }

                    } while (dataCursor.moveToNext()); // Now move to next cursor


                    contactNumbers =  removeDuplicate(contactNumbers);
                    contactList.add(new Contact_model(Long.toString(contctId), displayName, contactNumbers, contactEmailAddresses, photoPath, contactOtherDetails));


                }
            } while (contactsCursor.moveToNext());
        }
        return contactList;
    }


    public ArrayList<Contact_model> intelligent_Sort(ArrayList<Contact_model> contactlist)
    {
        ArrayList<Contact_model> contact_list = new ArrayList<>();

        for(Contact_model contact : contactlist)
        {
            if((contact.getContactName()!= "") && (contact.getContactNumber().size() > 0) && (isValidName(contact.getContactName())))
            {
                contact_list.add(contact);

            }

        }

        HashSet<Contact_model> set = new HashSet();
        List<Contact_model> newList = new ArrayList();
        for (Iterator iter = contact_list.iterator(); iter.hasNext();)
        {
            Contact_model contact = (Contact_model) iter.next();


            if (set.add(contact))
                newList.add(contact);
        }
        contact_list.clear();
        contact_list.addAll(newList);

//        HashSet<Contact_model> contacthash = new HashSet<Contact_model>(contact_list);
//        contact_list.clear();
//        contact_list.addAll(contacthash);



        return contact_list;
    }


    void setinitAdapter()
    {
//        mflingosrecycler = (RecyclerView) mRootview.findViewById(R.id.contactsrecycler);
//        mflingosrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
//        mflingosrecycler.setHasFixedSize(true);
//
//        mContactsAdapter = new ContactAdapter(getContext(), null, ContactsContract.Data.CONTACT_ID);
//        mflingosrecycler.setAdapter(mContactsAdapter);

    }

    public static List<String> removeDuplicate(List<String> arlList)
    {
        HashSet h = new HashSet(arlList);
        arlList.clear();
        arlList.addAll(h);

        return arlList;
    }

    public boolean isValidName(String name)
    {
       // boolean bool = Pattern.compile("^(?!AL).+").matcher(name).matches();
        boolean bool = Pattern.compile("^((?!AL).+)([^0-9]+).$").matcher(name).matches(); //^((?!AL).+)([^0-9]+).$
        return bool;
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
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS},REQUEST_PERMISSION);

            //getLoaderManager().initLoader(LOADER_ID,null,this);
        }
        else
        {
            setinitAdapter();
           // getLoaderManager().initLoader(LOADER_ID,null,this);

        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//ContactsContract.CommonDataKinds.Phone.CONTACT_ID

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id)
        {
            case LOADER_ID:
                return new CursorLoader(getContext(),
                        ContactsContract.Data.CONTENT_URI,
                        FROM_COLOUMNS,
                        null,
                        null,
                        ContactsContract.Data.DISPLAY_NAME_PRIMARY);
                default:
                    if(BuildConfig.DEBUG)
                    {
                        throw new IllegalArgumentException("No id handled");
                    }
                    return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mContactsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
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

