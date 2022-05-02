package com.example.finalproject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class testFirebase extends AppCompatActivity {

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageId);
            name = (TextView) itemView.findViewById(R.id.textName);
        }

        public void setPhoto(FirebaseMember Fmember) {
            name.setText(Fmember.getName());
            Glide.with(image.getContext())
                    .load(Fmember.getImageURL())
                    .into(image);
            //Log.i("set","set photooooooooooooooooooooooooo");
        }
    }

    private  FirebaseRecyclerAdapter<FirebaseMember, PhotoViewHolder> adapter;
    private RecyclerView recyclerView;

    public static boolean isPlay;
    public static MediaPlayer mp = new MediaPlayer();

    public static PendingIntent sender;
    public static AlarmManager alarm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_firebase);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(testFirebase.this));

        setupRecyclerView();


        FloatingActionButton fab1 = findViewById(R.id.floatingActionButton);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(testFirebase.this, "Fab1 clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(testFirebase.this  , QuestionActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.floatingActionButton2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(testFirebase.this, "Fab2 clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(testFirebase.this  , AddMemberActivity.class);
                startActivity(intent);
            }
        });

        //FloatingActionButton fab4 = findViewById(R.id.floatingActionButton4);
        //fab4.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //   public void onClick(View v) {
        //        Toast.makeText(testFirebase.this, "Fab4 clicked", Toast.LENGTH_LONG).show();
        //        Notify();
        //    }
        //});



        mp = MediaPlayer.create(this, R.raw.music);
        mp.setLooping(true);
        mp.start();

        Intent intent = new Intent(this,AlarmReceiver.class);
        intent.setAction("android.alarm.demo.action");
        sender = PendingIntent.getBroadcast(testFirebase.this, 0, intent, 0);
        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), sender);



        //alarm.setE(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3 * 100, sender);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 設置要用哪個menu檔做為選單
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // 取得點選項目的id
        int id = item.getItemId();

        // 依照id判斷點了哪個項目並做相應事件
        if (id == R.id.Music_setting) {
            // 按下「設定」要做的事
            //Toast.makeText(this, "設定", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(testFirebase.this  , MusicSetting.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupRecyclerView() {
        DatabaseReference picRef = FirebaseDatabase.getInstance()
                .getReference("myCards");
        picRef.limitToLast(80).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot msgSnapshot : dataSnapshot.getChildren()) {
                    FirebaseMember photo = msgSnapshot.getValue(FirebaseMember.class);
                    //Log.i("Photo's Title:", photo.getImageURL());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Photo", "failed: " + databaseError.getMessage());
            }
        });

        FirebaseRecyclerOptions<FirebaseMember> options =
                new FirebaseRecyclerOptions.Builder<FirebaseMember>()
                        .setQuery(picRef, FirebaseMember.class)
                        .build();

        adapter =
                new FirebaseRecyclerAdapter<FirebaseMember, PhotoViewHolder>(options) {
                    @NonNull
                    @Override
                    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
                        //Log.i("create","createeeeeeeeeeeeeeeeeee");
                        return new PhotoViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull PhotoViewHolder holder, int position, @NonNull final FirebaseMember model) {
                        holder.setPhoto(model);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(testFirebase.this, "firebase click", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent();
                                intent.putExtra("name",model.getName());
                                intent.putExtra("description",model.getDescription());
                                intent.putExtra("imageURL",model.getImageURL());
                                intent.setClass(testFirebase.this  , MemberDetailActivity.class);
                                startActivity(intent);


                            }
                        });
                    }

                };

        ItemTouchHelper helper = new ItemTouchHelper( new MyItemTouchCallback(adapter));
        helper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    // new 一個 method
    public void Notify() {
        Log.i("notify","notifyyyyyyyyyyyyyyyyyyyy");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("xx特價")
                .setContentTitle("標題example")
                .setContentText("內容example");

        NotificationChannel channel;
        String channelId = "some_channel_id";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            channel = new NotificationChannel(channelId
                    ,"Notify Test"
                    ,NotificationManager.IMPORTANCE_HIGH);
            builder.setChannelId(channelId);
            notificationManager.createNotificationChannel(channel);
        }else{
            builder.setDefaults(Notification.DEFAULT_ALL)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }


        Notification notification = builder.build();
        notificationManager.notify(0, notification);

    }



}
