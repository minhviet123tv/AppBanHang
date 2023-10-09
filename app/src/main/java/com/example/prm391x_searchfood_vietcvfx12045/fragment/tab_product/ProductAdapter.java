package com.example.prm391x_searchfood_vietcvfx12045.fragment.tab_product;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<Product> listProduct;
    private final IClickItemProductListener itemProductListener;
    private IAddToCartFromCard addToCartFromCard;

    public ProductAdapter(List<Product> listProduct, IClickItemProductListener itemProductListener, IAddToCartFromCard addToCartFromCard) {
        this.listProduct = listProduct;
        this.itemProductListener = itemProductListener;
        this.addToCartFromCard = addToCartFromCard;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    //Xử lý mỗi dòng item
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = listProduct.get(position);

        //1. Cài đặt dữ liệu của product vào CardView
        Bitmap bitmap = BitmapFactory.decodeByteArray(product.getProduct_image(), 0, product.getProduct_image().length);
        holder.imgCardProduct.setImageBitmap(bitmap);
        holder.txtProductName.setText(product.getProduct_name());
        holder.txtCardUnit.setText(product.getUnit());
        holder.txtCardPrice.setText("$ " + product.getPrice());

        //2. Sự kiện khi click vào ảnh Card Product -> Sử dụng interface 1 -> Truyền dữ liệu vào màn hình sử dụng
        holder.imgCardProduct.setOnClickListener(v -> {
            itemProductListener.onClickItemProduct(product);
        });

        //3. Sự kiện khi click vào nút add trong Card Product -> Sử dụng interface 2 -> Truyền dữ liệu vào màn hình sử dụng
        holder.imgCardAddToCart.setOnClickListener(v -> {
            addToCartFromCard.addToCartFromCard(product);
        });

    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imgCardProduct;
        private final ImageView imgCardAddToCart;
        private final TextView txtProductName;
        private final TextView txtCardUnit;
        private final TextView txtCardPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            txtProductName = itemView.findViewById(R.id.textView_card_product_name);
            txtCardUnit = itemView.findViewById(R.id.textView_card_product_unit);
            txtCardPrice = itemView.findViewById(R.id.textView_card_product_price);

            imgCardProduct = itemView.findViewById(R.id.imageView_card_product_image);
            imgCardAddToCart = itemView.findViewById(R.id.imageView_card_addToCart);
        }
    }

    //Truyền dữ liệu riêng của nút Add
    public interface IAddToCartFromCard {
        void addToCartFromCard(Product product);
    }
}
