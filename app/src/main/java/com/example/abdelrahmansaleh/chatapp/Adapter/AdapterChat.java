package com.example.abdelrahmansaleh.chatapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.abdelrahmansaleh.chatapp.MainActivity;
import com.example.abdelrahmansaleh.chatapp.Model.User;
import com.example.abdelrahmansaleh.chatapp.R;

import java.util.List;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.ChatViewHolder> {
    private Context context;
    private List<User> data;
    private int count;

    public AdapterChat(Context context, int count) {
        this.context = context;
        this.count = count;
    }
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if(count==1){
            view= LayoutInflater.from( context ).inflate( R.layout.card_chat,parent,false );
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        User userData=data.get( position );
        holder.name1.setText( userData.getName());
        Glide.with( context ).load( userData.getUrl() ).into( holder.imageView );

    }

    @Override
    public int getItemCount() {
        return data!=null?data.size():0;
    }
    public void setData(List<User> data){
        this.data=data;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView name1;
        ImageView imageView;
        public ChatViewHolder(View itemView) {
            super( itemView );
            name1=itemView.findViewById( R.id.Username );
            imageView=itemView.findViewById( R.id.imageView );
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPostion=getAdapterPosition();
                    User data1=data.get( adapterPostion );
                    ((MainActivity)context).chatUser( data1 );
                }
            } );
        }
    }
}
