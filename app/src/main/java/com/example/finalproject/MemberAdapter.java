package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    interface OnItemClickListener {
        //      點選事件
        void onItemClick(View view, int position);
    }
    public void setOnItemClickLitener(OnItemClickListener mLitener) {
        mOnItemClickListener = mLitener;
    }

    private Context context;
    private List<Member> memberList;

    MemberAdapter(Context context, List<Member> memberList) {
        this.context = context;
        this.memberList = memberList;
    }

    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MemberAdapter.ViewHolder holder,  final int position) {
        final Member member = memberList.get(position);
        holder.imageId.setImageResource(member.getImage());
        holder.textName.setText(member.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ImageView imageView = new ImageView(context);
                //imageView.setImageResource(member.getImage());
                //Toast toast = new Toast(context);
                //toast.setView(imageView);
                //toast.setDuration(Toast.LENGTH_SHORT);
                //toast.show();
                mOnItemClickListener.onItemClick(holder.itemView, position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return memberList.size();
    }

    //Adapter 需要一個 ViewHolder，只要實作它的 constructor 就好，保存起來的view會放在itemView裡面
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageId;
        TextView textId, textName;
        ViewHolder(View itemView) {
            super(itemView);
            imageId = (ImageView) itemView.findViewById(R.id.imageId);
            textId = (TextView) itemView.findViewById(R.id.textId);
            textName = (TextView) itemView.findViewById(R.id.textName);
        }

        public void setFirebaseMember(FirebaseMember Fmember) {
            textName.setText(Fmember.getName());
            Glide.with(imageId.getContext())
                    .load(Fmember.getImageURL())
                    .into(imageId);
        }
    }

    public List<Member> getDataList(){
        return memberList;
    }

    public void addItem(int image,String name,String description){
        memberList.add(new Member(image,name,description));
        //notifyItemInserted(position);
    }

}
