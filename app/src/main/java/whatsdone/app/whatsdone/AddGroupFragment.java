package whatsdone.app.whatsdone;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;

public class AddGroupFragment extends Fragment {

    private static final int RESULT_LOAD_IMAGE =0 ;
    private OnAddFragmentInteractionListener mListener;
    private Toolbar toolbar;
    private TextView teamPhoto;
    private Button addMembers;
    private Button save;
    private CircleImageView circleImageView;
    private Uri selectedImage;

    public AddGroupFragment() {
        // Required empty public constructor
    }


    public static GroupContainerFragment newInstance(String param1, String param2) {
        GroupContainerFragment fragment = new GroupContainerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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

        View view = inflater.inflate(R.layout.fragment_add_group, container, false);


        circleImageView = (CircleImageView) view.findViewById(R.id.group_photo_image_view);
        teamPhoto = (TextView) view.findViewById(R.id.teamPhoto);
        teamPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RESULT_LOAD_IMAGE);

            }
        });


        addMembers = (Button) view.findViewById(R.id.addMembers);
        addMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSave();
        }
    }

    public void setListener(OnAddFragmentInteractionListener handler) {
       mListener = handler;
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
    public interface OnAddFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSave();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data)
        {
            selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {

                e.printStackTrace();
            }
            circleImageView.setImageBitmap(bmp);
        }

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException
    {
        ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    

}
