package com.example.prm391x_searchfood_vietcvfx12045.searchproduct;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.model.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.SearchProductViewHolder> implements Filterable {
    private List<Product> productList;
    private List<Product> productListOld; //Trung gian để tạo filter
    private final IClickItemSearchProductListener itemAddProductListener, itemImageProductListener;

    public SearchProductAdapter(List<Product> productList, IClickItemSearchProductListener itemAddProductListener, IClickItemSearchProductListener itemImageProductListener) {
        this.productList = productList;
        this.productListOld = productList;
        this.itemAddProductListener = itemAddProductListener;
        this.itemImageProductListener = itemImageProductListener;
    }

    @NonNull
    @Override
    public SearchProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_product, parent, false);
        return new SearchProductViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SearchProductViewHolder holder, int position) {
        Product product = productList.get(position);

        //1. Gán giá trị hiển thị
        Bitmap bitmap = BitmapFactory.decodeByteArray(product.getProduct_image(), 0, product.getProduct_image().length);
        holder.img_product.setImageBitmap(bitmap);
        holder.txt_product_name.setText(product.getProduct_name());
        holder.txt_product_price.setText("$" + product.getPrice());
        holder.txt_product_unit.setText("/" + product.getUnit());

        //2. Truyền product trong sự kiện click add to cart
        holder.layout_add_to_cart.setOnClickListener(v -> {
            try {
                itemAddProductListener.onClickItemSearchProduct(product);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        //3. Truyền product trong sự kiện click image
        holder.img_product.setOnClickListener(v -> {
            try {
                itemImageProductListener.onClickItemSearchProduct(product);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    class SearchProductViewHolder extends RecyclerView.ViewHolder{
        private final ImageView img_product;
        private final TextView txt_product_name, txt_product_price, txt_product_unit;
        private final LinearLayout layout_add_to_cart;

        public SearchProductViewHolder(@NonNull View itemView) {
            super(itemView);

            //Ánh xạ
            img_product = itemView.findViewById(R.id.imageView_item_search_product_image);
            txt_product_name = itemView.findViewById(R.id.textView_item_search_product_name);
            txt_product_price = itemView.findViewById(R.id.textView_item_search_product_price);
            txt_product_unit = itemView.findViewById(R.id.textView_item_search_product_unit);

            layout_add_to_cart = itemView.findViewById(R.id.layout_add_to_cart_search_product);
        }
    }

    public interface IClickItemSearchProductListener{
        void onClickItemSearchProduct(Product product) throws SQLException;
    }

    //Hàm filter, tìm kiếm nhanh trong list
    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchKey = constraint.toString(); //Từ khoá nhập dùng tìm kiếm

                //1. Nếu searchKey trống thì dùng list ban đầu
                if(searchKey.isEmpty()){
                    productList = productListOld;

                //2. Nếu có searchKey, lọc list chứa những product có tên chứa searchKey
                } else {

                    List<Product> list = new ArrayList<>();

                    for(Product product : productListOld){
                        if(product.getProduct_name().toLowerCase().contains(searchKey.toLowerCase())){
                            list.add(product);
                        }
                    }

                    //List sử dụng sẽ bằng list mới
                    productList = list;
                }

                //3. Gán list mới cho filter
                FilterResults filterResults = new FilterResults();
                filterResults.values = productList;

                //4. Trả về filterResults
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                productList = (List<Product>) results.values; //Trả về list kết quả
                notifyDataSetChanged(); //Cập nhật adapter
            }
        };
    }
}
