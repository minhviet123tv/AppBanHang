package com.example.prm391x_searchfood_vietcvfx12045.fragment.favorite;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apachat.swipereveallayout.core.SwipeLayout;
import com.apachat.swipereveallayout.core.ViewBinder;
import com.example.prm391x_searchfood_vietcvfx12045.R;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>{
    private List<Favorite> favoriteList;
    private IClickItemFavoriteListener itemFavoriteListener, itemFavoriteListener_delete;
    private IClickImageFavoriteListener imageFavoriteListener;
    private ViewBinder viewBinder = new ViewBinder(); //Dùng để setup swipe cho RecyclerView

    public FavoriteAdapter(List<Favorite> favoriteList, IClickItemFavoriteListener itemFavoriteListener1, IClickImageFavoriteListener imageFavoriteListener, IClickItemFavoriteListener itemFavoriteListener_delete) {
        this.favoriteList = favoriteList;
        this.itemFavoriteListener = itemFavoriteListener1;
        this.imageFavoriteListener = imageFavoriteListener;
        this.itemFavoriteListener_delete = itemFavoriteListener_delete;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Favorite favorite = favoriteList.get(position);

        //1. Set giá trị trong item
        Bitmap bitmap = BitmapFactory.decodeByteArray(favorite.getImage_product(), 0, favorite.getImage_product().length);
        holder.img_product_image.setImageBitmap(bitmap);
        holder.txt_product_name.setText(favorite.getProduct_name());
        holder.txt_price.setText("$" + favorite.getProduct_price());
        holder.txt_unit.setText("/" + favorite.getProduct_unit());

        //2. Set sự kiện khi click vào các đối tượng
        holder.img_product_image.setOnClickListener(v -> {
            //chỉ truyền mình id_product (để mở details ở activity)
            imageFavoriteListener.onClickImageFavorite(favorite.getId_product());
        });

        holder.layout_add_to_cart_favorite.setOnClickListener(v -> {
            //truyền cả một favorite (để add to cart)
            itemFavoriteListener.onClickItemFavorite(favorite);
        });

        //3. Set sự kiên xoá item: layout swipe, id của favorite
        viewBinder.bind(holder.swipeLayout, String.valueOf(favorite.getId_favorite()));

        holder.ic_delete_favorite.setOnClickListener(v -> {

            favoriteList.remove(holder.getLayoutPosition());
            notifyItemRemoved(holder.getLayoutPosition()); //Cập nhật rcv (với vị trí đã tương tác xoá)

            //Truyền dữ liệu ra ngoài màn hình sử dụng để xoá (cập nhật status trong CSDL)
            itemFavoriteListener_delete.onClickItemFavorite(favorite);
        });


    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_product_image, ic_delete_favorite;
        private TextView txt_product_name, txt_price, txt_unit;
        private LinearLayout layout_add_to_cart_favorite;
        private SwipeLayout swipeLayout;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            //Ánh xạ
            img_product_image = itemView.findViewById(R.id.imageView_item_favorite_image);
            txt_product_name = itemView.findViewById(R.id.textView_item_favorite_product_name);
            txt_price = itemView.findViewById(R.id.textView_item_favorite_product_price);
            txt_unit = itemView.findViewById(R.id.textView_item_favorite_product_unit);

            layout_add_to_cart_favorite = itemView.findViewById(R.id.layout_add_to_cart_favorite);
            swipeLayout = itemView.findViewById(R.id.layout_item_swipe_favorite);
            ic_delete_favorite = itemView.findViewById(R.id.ic_delete_favorite);
        }
    }

    /*
    + Truyền nhiều dữ liệu khác nhau thì vẫn có thể dùng 1 interface
    + Vị trí click khác nhau, cùng một dữ liệu thì chỉ cần khai báo thêm tham số interface và trong Constructor
    + Nhưng vị trí click khác nhau, dữ liệu truyền khác nhau thì nên dùng nhiều interface
     */
    public interface IClickItemFavoriteListener{
        void onClickItemFavorite(Favorite favorite);
    }

    public interface IClickImageFavoriteListener{
        void onClickImageFavorite(int id_product);
    }

}
