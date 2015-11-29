package de.shutterstock.android.shutterstock.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.shutterstock.android.shutterstock.R;
import de.shutterstock.android.shutterstock.activities.PictureDetailsActivity;
import de.shutterstock.android.shutterstock.content.model.Image;

/**
 * Created by emanuele on 07/11/15.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final SimpleDraweeView mSimpleDraweeView;
        private final ImageView mImageView;
        private Image mImage;

        public ImageViewHolder(View itemView) {
            super(itemView);
            final Context context = itemView.getContext();
            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(context.getResources());
            List<Drawable> drawables = new ArrayList<>();
            drawables.add(new ColorDrawable(ContextCompat.getColor(context, R.color.accent)));
            drawables.add(new ColorDrawable(ContextCompat.getColor(context, R.color.accent_material_light)));
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(300)
                    .setPlaceholderImage(new ColorDrawable(ContextCompat.getColor(context, R.color.accent_material_light)))
                    .setBackgrounds(drawables)
                    .setProgressBarImage(ContextCompat.getDrawable(context, R.mipmap.ic_launcher))
                    .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                    .build();
            mSimpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.image2);
            // mSimpleDraweeView.setHierarchy(hierarchy);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
            mImageView.setOnClickListener(this);
        }

        public void updateImage(Image image) {
            mImage = image;
            //mImageView.setImageURI(Uri.parse(image.assets.preview.url));
            //mImageView.setAspectRatio(image.aspect);
            Picasso.with(mImageView.getContext())
                    .load(image.assets.preview.url)
                    .into(mImageView);
        }

        @Override
        public void onClick(View v) {
            if (!(v.getContext() instanceof Activity)) {
                return;
            }
            final Activity activity = (Activity) v.getContext();
            Intent intent = new Intent(activity, PictureDetailsActivity.class);
            intent.putExtra(PictureDetailsActivity.IMAGE_KEY, mImage);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(activity, mImageView, "profile");
            ActivityCompat.startActivity(activity, intent, options.toBundle());
        }
    }


    final List<Image> mDataSet = new ArrayList<>();


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        final Image image;
        synchronized (mDataSet) {
            image = mDataSet.get(position);
        }
        holder.updateImage(image);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void addAll(List<Image> images) {
        final int currentCount = mDataSet.size();
        synchronized (mDataSet) {
            mDataSet.addAll(images);
        }
        notifyItemRangeInserted(currentCount, images.size());
    }
}
