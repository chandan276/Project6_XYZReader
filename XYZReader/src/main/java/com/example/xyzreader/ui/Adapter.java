package com.example.xyzreader.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;

public class Adapter extends CursorRecyclerViewAdapter<Adapter.ViewHolder> {
    private Context mContext;

    Adapter(Cursor cursor, Context context) {
        super(cursor);
        mContext = context;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final Cursor cursor) {
        holder.titleView.setText(cursor.getString(ArticleLoader.Query.TITLE));

        String thumb = cursor.getString(ArticleLoader.Query.THUMB_URL);
        String subtitleText = DateUtils.getRelativeTimeSpanString(
                cursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString()
                + " by "
                + cursor.getString(ArticleLoader.Query.AUTHOR);

        holder.subtitleView.setText(subtitleText);
        holder.thumbnailView.setAspectRatio(cursor.getFloat(ArticleLoader.Query.ASPECT_RATIO));
        //Glide.clear(holder.thumbnailView);
        Glide.with(holder.thumbnailView.getContext())
                .load(thumb)
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                //.dontAnimate()
                .centerCrop()
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        Bitmap bitmap = ((GlideBitmapDrawable) resource.getCurrent()).getBitmap();
//                        Palette palette = Palette.generate(bitmap);
//                        int defaultColor = 0xFF333333;
//                        int color = palette.getVibrantColor(defaultColor);
//                        holder.itemView.setBackgroundColor(color);
//                        return false;
//                    }
//                })
                .into(holder.thumbnailView);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_article, parent, false);
        final ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View views) {
                Bundle bundle = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    bundle = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) mContext, vh.thumbnailView, vh.thumbnailView.getTransitionName()).toBundle();
                    parent.getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                            ItemsContract.Items.buildItemUri(getItemId(vh.getAdapterPosition()))), bundle);
                } else {
                    parent.getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                            ItemsContract.Items.buildItemUri(getItemId(vh.getAdapterPosition()))));
                }

            }
        });
        return vh;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public DynamicHeightNetworkImageView thumbnailView;
        TextView titleView;
        TextView subtitleView;

        ViewHolder(View view) {
            super(view);
            thumbnailView = (DynamicHeightNetworkImageView) view.findViewById(R.id.thumbnail);
            titleView = (TextView) view.findViewById(R.id.article_title);
            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
        }
    }

}
