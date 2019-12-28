package com.example.paybill.ui.share;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.paybill.R;
import com.example.paybill.Utils.UserDetailData;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ReceiveFragment extends Fragment {

    ImageView img;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    private ShareViewModel shareViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recieve, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        img=(ImageView)view.findViewById(R.id.img_scan);
        WindowManager manager=(WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display=manager.getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);
        int width=point.x;
        int height=point.y;
        int smallerdimension = width < height ? width:height;
        smallerdimension=smallerdimension*3/4;
        qrgEncoder=new QRGEncoder(UserDetailData.userAccountID,null, QRGContents.Type.TEXT,smallerdimension);
        try {
            bitmap=qrgEncoder.encodeAsBitmap();
            img.setImageBitmap(bitmap);
        }
        catch (Exception e){

        }


    }

}