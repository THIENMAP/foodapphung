package com.example.foodapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodapp.Interface.ImageClickListenner;
import com.example.foodapp.R;

import com.example.foodapp.activity.ActivityGiohang;
import com.example.foodapp.model.EventBus.DeleteEvent;
import com.example.foodapp.model.EventBus.TinhTongEvent;
import com.example.foodapp.model.Giohang;
import com.example.foodapp.model.Sanpham;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GiohangAdapter extends RecyclerView.Adapter<GiohangAdapter.MyViewHolder> {
    Context context;
    List<Giohang> giohangList;

    public GiohangAdapter(Context context, List<Giohang> giohangList) {
        this.context = context;
        this.giohangList = giohangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.giohangitem1,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //hbnhbb
        //ihuhuhu
        Giohang giohang = giohangList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.giasp.setText(decimalFormat.format(Double.parseDouble(Long.toString(giohang.getGia())))+"đ");
        //String txt12 = ""+giohang.getHinhanh()+"-"+giohang.getTensp()+"-"+giohang.getSoluong()+"-"+giohang.getGia();
        Glide.with(context).load(giohang.getHinhanh()).into(holder.anhsanpham);
        holder.tensp.setText(giohang.getTensp());
        holder.soluongsp.setText(giohang.getSoluong());
        int sl = Integer.parseInt(giohang.getSoluong());
        int gia = Integer.parseInt(Long.toString(giohang.getGia()));
        int tamtinh = sl*gia;
        //DecimalFormat decimalFormat1 = new DecimalFormat("###,###,###");
        holder.giatt.setText(decimalFormat.format(Double.parseDouble(Integer.toString(tamtinh)))+"đ");
        holder.setImageClickListenner(new ImageClickListenner() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if(giatri == 1){
                    if(Integer.parseInt(giohangList.get(pos).getSoluong()) >1){
                        int slmoi = Integer.parseInt(giohangList.get(pos).getSoluong())-1;
                        giohangList.get(pos).setSoluong(Integer.toString(slmoi));
                        //cap nhat
                        holder.soluongsp.setText(giohangList.get(pos).getSoluong());
                        int sl = Integer.parseInt(giohangList.get(pos).getSoluong());
                        int gia = Integer.parseInt(Long.toString(giohangList.get(pos).getGia()));
                        int tamtinh = sl*gia;
                        //DecimalFormat decimalFormat1 = new DecimalFormat("###,###,###");
                        holder.giatt.setText(decimalFormat.format(Double.parseDouble(Integer.toString(tamtinh)))+"đ");
                        //tinh lai tong tien
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    }else{
                        EventBus.getDefault().post(new DeleteEvent(pos));
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    }

                }else if(giatri==2){
                    int slmoi = Integer.parseInt(giohangList.get(pos).getSoluong())+1;
                    giohangList.get(pos).setSoluong(Integer.toString(slmoi));
                    //cap nhat
                    holder.soluongsp.setText(giohangList.get(pos).getSoluong());
                    int sl = Integer.parseInt(giohangList.get(pos).getSoluong());
                    int gia = Integer.parseInt(Long.toString(giohangList.get(pos).getGia()));
                    int tamtinh = sl*gia;
                    //DecimalFormat decimalFormat1 = new DecimalFormat("###,###,###");
                    holder.giatt.setText(decimalFormat.format(Double.parseDouble(Integer.toString(tamtinh)))+"đ");
                    //tinh lai tong tien
                    EventBus.getDefault().postSticky(new TinhTongEvent());

                }else if(giatri==0){
                    //deletesp(giohangList.get(pos).getIdgiohang());
                    //giohangList.remove(pos);

                    //tinh lai tong tien
                    EventBus.getDefault().post(new DeleteEvent(pos));


                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return giohangList.size();
    }

    private void deletesp(int idgiohang){

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tensp,giasp,soluongsp,giatt;
        ImageView anhsanpham,addsl,removesl,deletesp11;
        ImageClickListenner imageClickListenner;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            removesl = itemView.findViewById(R.id.removesl);
            addsl = itemView.findViewById(R.id.addsl);
            deletesp11 = itemView.findViewById(R.id.xoagh1);
            tensp = itemView.findViewById(R.id.tenspgiohang);
            giasp = itemView.findViewById(R.id.giaspgiohang);
            soluongsp = itemView.findViewById(R.id.slspgiohang);
            giatt = itemView.findViewById(R.id.slspgiohang1);
            anhsanpham = itemView.findViewById(R.id.item_giohang);
            //event click
            addsl.setOnClickListener(this);
            removesl.setOnClickListener(this);
        }

        public void setImageClickListenner(ImageClickListenner imageClickListenner) {
            this.imageClickListenner = imageClickListenner;
        }

        @Override
        public void onClick(View view) {
            if(view == removesl){
                imageClickListenner.onImageClick(view,getAdapterPosition(),1);
            }else if(view==addsl){
                imageClickListenner.onImageClick(view,getAdapterPosition(),2);
            }else if(view==deletesp11){
                imageClickListenner.onImageClick(view,getAdapterPosition(),0);
            }
        }
    }
}
