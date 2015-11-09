package de.shutterstock.android.shutterstock.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import de.shutterstock.android.shutterstock.R;
import de.shutterstock.android.shutterstock.content.model.Image;

/**
 * Created by emanuele on 07/11/15.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        private final SimpleDraweeView mImageView;
        private Image mImage;
        public ImageViewHolder(View itemView) {
            super(itemView);
            mImageView = (SimpleDraweeView) itemView.findViewById(R.id.image);
        }

        public void updateImage(Image image) {
            mImage = image;
            mImageView.setImageURI(Uri.parse(image.assets.preview.url));
            mImageView.setAspectRatio(image.aspect);
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
