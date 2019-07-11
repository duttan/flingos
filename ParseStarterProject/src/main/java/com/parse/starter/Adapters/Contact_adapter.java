package com.parse.starter.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.starter.Contact_model;
import com.parse.starter.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class Contact_adapter extends RecyclerView.Adapter<Contact_adapter.Contacts_ViewHolder> {


    private ArrayList<Contact_model> contact_arrayList;
    private LayoutInflater mInflater;
    public Context context;



    public Contact_adapter(Context context,ArrayList<Contact_model> arrayList)
    {
        this.mInflater = LayoutInflater.from(context);
        this.contact_arrayList = arrayList;
        this.context = context;
    }

    @Override
    public Contacts_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.contact_row_view, parent, false);
        return new Contacts_ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(Contacts_ViewHolder holder, int position) {
        holder.bind(contact_arrayList.get(position));


        }

    @Override
    public int getItemCount() {
        return contact_arrayList.size();
    }



    public class Contacts_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView contact_image;
        TextView contact_username;
        TextView contact_email;
        TextView contact_number;
        TextView contact_otherdetails;


        public Contacts_ViewHolder(View itemView) {
            super(itemView);

            contact_image = (ImageView) itemView.findViewById(R.id.imageView_contact_display);
            contact_username = (TextView) itemView.findViewById(R.id.contact_username);
//            contact_email = (TextView) itemView.findViewById(R.id.contact_username);
//            contact_number = (TextView) itemView.findViewById(R.id.contact_username);
//            contact_otherdetails = (TextView) itemView.findViewById(R.id.contact_username);


        }

        public void bind(final Contact_model model)
        {
            //Username
            if (!model.getContactName().equals("") && model.getContactName() != null) {
                contact_username.setText(model.getContactName());
            } else {
                contact_username.setText("A girl has no name");
            }

//            //Email
//            if (!model.getContactEmail().equals("") && model.getContactEmail() != null) {
//                contact_email.setText("EMAIL ID - n" + model.getContactEmail());
//            } else {
//                contact_email.setText("EMAIL ID - n" + "No EmailId");
//            }
//
//            //Contact number
//            if (!model.getContactNumber().equals("") && model.getContactNumber() != null) {
//                contact_number.setText("CONTACT NUMBER - n" + model.getContactNumber());
//            } else {
//                contact_number.setText("CONTACT NUMBER - n" + "No Contact Number");
//            }
//
//            //Other details
//            if (!model.getContactOtherDetails().equals("") && model.getContactOtherDetails() != null) {
//                contact_otherdetails.setText("OTHER DETAILS - n" + model.getContactOtherDetails());
//            } else {
//               contact_otherdetails.setText("OTHER DETAILS - n" + "Other details are empty");
//            }

            //TODO - Change this image view -- use glide -- for initializing
            //

            //Image
            Bitmap image = null;
            if (!model.getContactPhoto().equals("") && model.getContactPhoto() != null) {
                image = BitmapFactory.decodeFile(model.getContactPhoto());// decode
                // the
                // image
                // into
                // bitmap
                if (image != null)
                    contact_image.setImageBitmap(image);// Set image if bitmap
                    // is not null
                else {
                    image = BitmapFactory.decodeResource(context.getResources(), R.mipmap.user);// if bitmap is null then set
                    // default bitmap image to
                    // contact image
                    contact_image.setImageBitmap(image);
                }
            } else {
                image = BitmapFactory.decodeResource(context.getResources(), R.mipmap.user);
                contact_image.setImageBitmap(image);
            }

        }


        @Override
        public void onClick(View view) {

        }
    }


}


