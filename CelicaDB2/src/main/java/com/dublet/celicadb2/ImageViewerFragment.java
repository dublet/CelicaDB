package com.dublet.celicadb2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dublet on 27/12/13.
 */
public class ImageViewerFragment extends Fragment {
    public final static String FRAGMENT_BIG_PICTURE_RESOURCE = "com.dublet.celicadb2.big_picture_resource";
    public final static String FRAGMENT_BIG_PICTURE_NARRATIVE = "com.dublet.celicadb2.big_picture_narrative";
    private int _pictureResource;
    private String _narrative;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ImageViewerFragment create(int resourceId, String narrative) {
        ImageViewerFragment fragment = new ImageViewerFragment();
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_BIG_PICTURE_RESOURCE, resourceId);
        args.putString(FRAGMENT_BIG_PICTURE_NARRATIVE, narrative);
        fragment.setArguments(args);
        return fragment;
    }

    public ImageViewerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _pictureResource = getArguments().getInt(FRAGMENT_BIG_PICTURE_RESOURCE);
        _narrative = getArguments().getString(FRAGMENT_BIG_PICTURE_NARRATIVE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.image_viewer, container, false);


        ImageView imageView = (ImageView)rootView.findViewById(R.id.big_image);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) rootView.getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);

        imageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), _pictureResource,
                metrics.widthPixels, metrics.heightPixels));

        ((TextView)rootView.findViewById(R.id.big_image_narrative)).setText(Html.fromHtml(_narrative));

        return rootView;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}