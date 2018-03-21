package com.labelwall.mywall.util.image;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by Administrator on 2018-03-16.
 */

public class ImageUtil {

    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .dontAnimate()
            .centerCrop();

    public static void loadImage(Activity activity, Uri uri, AppCompatImageView view) {
        Glide.with(activity)
                .load(uri)
                .apply(OPTIONS)
                .into(view);
    }
}
