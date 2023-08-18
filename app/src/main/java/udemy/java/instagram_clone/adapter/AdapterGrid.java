package udemy.java.instagram_clone.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import udemy.java.instagram_clone.R;

public class AdapterGrid extends ArrayAdapter<String> {

    private Context context;
    private int layoutResource;
    private List<String> urlPhotos;


    public AdapterGrid(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);

        this.context = context;
        this.layoutResource = resource;
        this.urlPhotos = objects;

    }

    public class ViewHolder {

        ImageView image;
        ProgressBar progressBar;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        if ( convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate( layoutResource, parent, false );
            viewHolder.progressBar = convertView.findViewById(R.id.progressBar_rowView_gridPosts);
            viewHolder.image =  convertView.findViewById(R.id.imageView_rowView_gridPosts);

            convertView.setTag( viewHolder );

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        String imageUrl = getItem(position);
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(imageUrl, viewHolder.image, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {

                viewHolder.progressBar.setVisibility( View.VISIBLE );

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                viewHolder.progressBar.setVisibility( View.GONE );
                //viewHolder.imageView.setImageResource(R.drawable.ic_close_24);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                viewHolder.progressBar.setVisibility( View.GONE );

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

                viewHolder.progressBar.setVisibility( View.GONE );

            }
        });

        return convertView;
    }
}
