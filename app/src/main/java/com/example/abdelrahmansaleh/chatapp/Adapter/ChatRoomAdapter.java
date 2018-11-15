package com.example.abdelrahmansaleh.chatapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abdelrahmansaleh.chatapp.Model.ChatObject;
import com.example.abdelrahmansaleh.chatapp.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.END;
import static android.view.Gravity.START;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {
    private Context context;
    private List<ChatObject> data;
    private int count;

    public ChatRoomAdapter(Context context) {
        this.context = context;

    }

    @Override
    public ChatRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 =null;
            view1= LayoutInflater.from( context ).inflate( R.layout.massagecard,parent,false );

        return new ChatRoomViewHolder(view1);

    }

    @Override
    public void onBindViewHolder(final ChatRoomViewHolder holder, int position) {
        ChatObject object1=data.get( position );
        if (data.get( holder.getAdapterPosition()).getMassage().isEmpty()){
            holder.mass.setVisibility( View.GONE );
        }
        holder.mass.setText( object1.getMassage());


        if (object1.getCurrentUser()){
            holder.mass.setGravity( START);
            holder.mass.setTextColor( Color.parseColor( "#ffffff" ) );
            holder.mass.setBackgroundColor( Color.parseColor("#FF0DB4F4") );

            holder.mediaView.setBackgroundColor( Color.parseColor("#FF0DB4F4") );
            holder.mediaView.setTextColor( Color.parseColor( "#ffffff" ) );
            holder.mediaView.setTextAlignment( View.TEXT_ALIGNMENT_CENTER );

        }else{
            holder.mass.setGravity( END);
            holder.mass.setTextColor( Color.parseColor( "#ffffff" ) );
            holder.mass.setBackgroundColor( Color.parseColor("#acacac") );

            holder.mediaView.setBackgroundColor( Color.parseColor("#acacac") );
            holder.mediaView.setTextColor( Color.parseColor( "#ffffff" ) );
        }
        if (data.get( holder.getAdapterPosition()).getImageMassage().isEmpty()){
            holder.mediaView.setVisibility( View.GONE );
        }else{
            if (holder.mass.length()<20){
                holder.mass.setWidth( 350 );
            }
            holder.mediaView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ImageViewer.Builder(v.getContext(),data.get( holder.getAdapterPosition()).getImageMassage() )
                            .setStartPosition(0)
                            .show();
                }
            } );
        }
    }

    @Override
    public int getItemCount() {
        return data!=null?data.size():0;
    }
    public void setData(List<ChatObject> data){
        this.data=data;
    }

    public class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        TextView mass;
        Button mediaView;
        public ChatRoomViewHolder(View itemView) {
            super( itemView );
            mass=itemView.findViewById( R.id.massTextView );
            mediaView=itemView.findViewById( R.id.imageViewMedia );
        }
    }
}
