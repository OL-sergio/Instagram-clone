package udemy.java.instagram_clone.adapter;


import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.listener.ThumbnailListener;
import udemy.java.instagram_clone.model.ThumbnailItem;



public class ThumbnailsAdapter extends RecyclerView.Adapter<ThumbnailsAdapter.MyViewHolder> {

        private static final String TAG = "THUMBNAILS_ADAPTER";
        private static int lastPosition = -1;

        private ThumbnailListener thumbnailCallback;
        private List<ThumbnailItem> itemList;

        public ThumbnailsAdapter(List<ThumbnailItem> dataSet, ThumbnailListener thumbnailCallback) {

            Log.v(TAG, "Thumbnails Adapter has " + dataSet.size() + " items");

            this.itemList = dataSet;
            this.thumbnailCallback = thumbnailCallback;
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);
                this.thumbnail = (ImageView) view.findViewById(R.id.imageView_filters);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            Log.v(TAG, "On Create View Holder Called");

            View itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.row_view_filters, viewGroup, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

            final ThumbnailItem thumbnailItem = itemList.get(position);

            Log.v(TAG, "On Bind View Called");

            holder.thumbnail.setImageBitmap(thumbnailItem.image);
            holder.thumbnail.setScaleType(ImageView.ScaleType.FIT_START);

            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastPosition != position) {
                        thumbnailCallback.onThumbnailClick(thumbnailItem.filter);
                        lastPosition = position;
                    }
                }

            });
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
}
