package com.example.prm391x_searchfood_vietcvfx12045.fragment.account.myorder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.model.MyOrder;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.OrderViewHolder>{
    private final List<MyOrder> myOrderList;
    private final IClickItemOrderListener itemOrderListener;

    public MyOrderAdapter(List<MyOrder> orderList, IClickItemOrderListener itemOrderListener) {
        this.myOrderList = orderList;
        this.itemOrderListener = itemOrderListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    //Xử lý từng item
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        //1. Lấy order tương ứng vị trí
        MyOrder myOrder = myOrderList.get(position);

        //2. Gán giá trị cho item
        Bitmap bitmap = BitmapFactory.decodeByteArray(myOrder.getImg_product_largest_sum(), 0, myOrder.getImg_product_largest_sum().length);
        holder.img_icon_order.setImageBitmap(bitmap);
        holder.txt_order_id.setText( "Mã: #" + myOrder.getId_order());

        //Chọn date theo trạng thái
        String date = "";
        switch (myOrder.getStatus()){
            case 1:
                date = myOrder.getOrder_date();
                break;
            case 2:
                date = myOrder.getWait_ship_date();
                break;
            case 3:
                date = myOrder.getShipping_date();
                break;
            case 4:
                date = myOrder.getReceive_date();
                break;
            case 5:
                date = myOrder.getCancel_order_date();
                break;
            case 6:
                date = myOrder.getReturn_order_date();
                break;
            default:
                date = "00/00/00";
        }

        holder.txt_order_date.setText(date);
        holder.txt_order_sum_money.setText( "$" + myOrder.getTotal_money());

        //3. Sự kiện click vào layout, truyền dữ liệu order vào hàm interface
        holder.layout_order_item.setOnClickListener(v -> {
            itemOrderListener.onClickItemOrder(myOrder);
        });

    }

    @Override
    public int getItemCount() {
        return myOrderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder{
        private final ImageView img_icon_order;
        private final TextView txt_order_id, txt_order_date, txt_order_sum_money;
        private final RelativeLayout layout_order_item;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            //Ánh xạ
            img_icon_order = itemView.findViewById(R.id.imageView_order_item_image);
            txt_order_id = itemView.findViewById(R.id.textView_order_id);
            txt_order_date = itemView.findViewById(R.id.textView_order_date);
            txt_order_sum_money = itemView.findViewById(R.id.textView_order_sum_money);
            layout_order_item = itemView.findViewById(R.id.layout_order_item);
        }
    }

    //Truyền dữ liệu một model Order
    public interface IClickItemOrderListener{
        void onClickItemOrder(MyOrder myOrder);
    }
}
