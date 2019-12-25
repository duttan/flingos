package com.parse.starter.Adapters;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.starter.R;

public class ContactAdapter extends CursorRecyclerViewAdapter<ContactAdapter.ContactsViewHolder> {



    public ContactAdapter(Context context, Cursor cursor, String id) {
        super(context, cursor, id);
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row_view,parent,false);
        return new ContactsViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ContactsViewHolder viewHolder, Cursor cursor) {


        String username = cursor.getString(cursor.getColumnIndex(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? ContactsContract.Data.DISPLAY_NAME_PRIMARY : ContactsContract.Data.DISPLAY_NAME));
        // String phone_num = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


        long contact_id = getItemId(cursor.getPosition());
        viewHolder.setContact_username(username);
        // Uri cUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contact_id);
        // Uri phoneuri = Uri.withAppendedPath(cUri,ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
        // viewHolder.setContact_username(phone_num);



        long photo_id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data.PHOTO_ID));
        if(photo_id !=0)
        {
            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contact_id);
            Uri photouri = Uri.withAppendedPath(contactUri,ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
            viewHolder.contact_image.setImageURI(photouri);
        }
        else
        {
            viewHolder.contact_image.setBackgroundResource(R.drawable.background);
            viewHolder.contact_image.setImageResource(R.mipmap.user);
        }



    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder{

        TextView contact_username;
        ImageView contact_image;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            contact_username = (TextView) itemView.findViewById(R.id.contact_username);
            contact_image = (ImageView) itemView.findViewById(R.id.imageView_contact_display);

        }
        public void setContact_username(String username)
        {
            contact_username.setText(username);
            
        }


    }





}
