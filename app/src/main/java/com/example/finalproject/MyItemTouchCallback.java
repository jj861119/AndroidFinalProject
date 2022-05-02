package com.example.finalproject;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MyItemTouchCallback extends  ItemTouchHelper.Callback  {
    private  final FirebaseRecyclerAdapter<FirebaseMember, testFirebase.PhotoViewHolder> adapter;

    public  MyItemTouchCallback (FirebaseRecyclerAdapter<FirebaseMember, testFirebase.PhotoViewHolder> adapter)  { this .adapter = adapter;    }



    @Override
    public  int  getMovementFlags (RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)  {
        int dragFlag;
        int swipeFlag;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper .RIGHT | ItemTouchHelper.LEFT;
            swipeFlag = 0 ;
        }
        else {
            dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP;
            swipeFlag = ItemTouchHelper.END;
        }
        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override public boolean onMove (RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) { return false ;     }



    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper. END ) {
            Log.i("item",adapter.getItem(position).getName());

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("myCards").child(adapter.getItem(position).getName());
            ref.removeValue();

            //adapter.getDataList().remove(position );
            //adapter.notifyItemRemoved( position );
        }
    }







}
