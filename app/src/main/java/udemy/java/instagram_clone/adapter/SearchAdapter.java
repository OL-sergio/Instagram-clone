package udemy.java.instagram_clone.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import udemy.java.instagram_clone.R;
import udemy.java.instagram_clone.model.User;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private List<User> listUsers;
    private Context context;

    public SearchAdapter (List<User> list,Context cont){
        this.listUsers = list;
        this.context = cont;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userPhoto;
        TextView titleName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.circleImageView_photoTitle);
            titleName = itemView.findViewById(R.id.textView_titleName);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View listView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view_search_users, parent, false);

        return new MyViewHolder(listView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User user = listUsers.get(position);
        holder.titleName.setText( user.getName() );

            if (user.getUrlPhoto() != null ){
                Uri  url = Uri.parse( user.getUrlPhoto() );
                Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.avatar)
                        .into(holder.userPhoto);
            }


    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }
}
