package com.example.prm391x_searchfood_vietcvfx12045.fragment.account.myorder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanSanPhamMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.model.MyOrder;
import com.example.prm391x_searchfood_vietcvfx12045.model.MyOrderDetails;
import com.example.prm391x_searchfood_vietcvfx12045.model.Product;

import java.sql.SQLException;
import java.util.List;

public class MyOrderDetailsAdapter extends RecyclerView.Adapter<MyOrderDetailsAdapter.MyOrderDetailsViewHolder>{
    private List<MyOrderDetails> myOrderDetailsList;
    private IClickItemMyOrderDetailsListener itemMyOrderDetailsListener;
    private TruyVanSanPhamMSSQL truyVanSanPham = new TruyVanSanPhamMSSQL();

    //Hàm khởi tạo
    public MyOrderDetailsAdapter(List<MyOrderDetails> myOrderDetailsList, IClickItemMyOrderDetailsListener itemMyOrderDetailsListener) {
        this.myOrderDetailsList = myOrderDetailsList;
        this.itemMyOrderDetailsListener = itemMyOrderDetailsListener;
    }

    @NonNull
    @Override
    public MyOrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order_details, parent, false);
        return new MyOrderDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderDetailsViewHolder holder, int position) {
        //1. Lấy đối tượng tại position
        MyOrderDetails myOrderDetails = myOrderDetailsList.get(position);

        //Lấy Product
        Product product = new Product();
        try {
            product = truyVanSanPham.getProduct(myOrderDetails.getId_product());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //2. Gán giá trị cho các đối tượng trong layout (Dùng cả MyOrderDetails và Product)
        Bitmap bitmap = BitmapFactory.decodeByteArray(product.getProduct_image(), 0, product.getProduct_image().length);
        holder.img_product.setImageBitmap(bitmap);
        holder.txt_product_name.setText(product.getProduct_name());
        holder.txt_price.setText("$" + product.getPrice());
        holder.txt_unit.setText("/" + product.getUnit());
        holder.txt_amount.setText("Số lượng: "  + " " + myOrderDetails.getAmount());
        holder.txt_sum_product.setText("$" + myOrderDetails.getSum());

        //3. Truyền dữ liệu trong sự kiện click
        holder.img_product.setOnClickListener(v -> {
            itemMyOrderDetailsListener.onClickItemMyOrderDetails(myOrderDetails);
        });
    }

    @Override
    public int getItemCount() {
        return myOrderDetailsList.size();
    }

    class MyOrderDetailsViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_product;
        private TextView txt_product_name, txt_price, txt_unit, txt_amount, txt_sum_product;

        public MyOrderDetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            img_product = itemView.findViewById(R.id.imageView_item_myorder_details_image);
            txt_product_name = itemView.findViewById(R.id.textView_item_myorder_details_name);
            txt_price = itemView.findViewById(R.id.textView_item_myorder_details_price);
            txt_unit = itemView.findViewById(R.id.textView_item_myorder_details_unit);
            txt_amount = itemView.findViewById(R.id.textView_amount_item_myorder_details);
            txt_sum_product = itemView.findViewById(R.id.textView_item_myorder_details_sum);

        }
    }

    //Truyền MyOrder
    public interface IClickItemMyOrderDetailsListener{
        void onClickItemMyOrderDetails(MyOrderDetails myOrderDetails);
    }
}
