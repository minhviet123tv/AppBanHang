package com.example.prm391x_searchfood_vietcvfx12045.fragment.home;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.model.Categories;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {
    private List<Categories> categoriesList;
    private IClickItemCategoriesListener itemCategoriesListener;
    private int checkedPosition = 0; //-1: Khong co select mac dinh | 0: item duoc select

    //II. Khởi tạo
    public CategoriesAdapter(List<Categories> categoriesList, IClickItemCategoriesListener itemCategoriesListener) {
        this.categoriesList = categoriesList;
        this.itemCategoriesListener = itemCategoriesListener;
    }

    //2. Tạo View cho CategoriesViewHolder (Sử dụng CardView đã tạo)
    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categories, parent, false);
        return new CategoriesViewHolder(view);
    }

    //3. Xử lý dữ liệu từ list vào từng CardView | Xử lý trong từng item (CardView) của categoriesList
    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {

        //a. Lấy dữ liệu tương ứng của list
        Categories categories = categoriesList.get(position);

        //b. Gán dữ liệu (của list) vào CardView
        holder.txt_categories_name.setText(categories.getCategories_name());

        //Xử lý ảnh (Chuyển mảng hình ảnh byte sang Bitmap) | byte array, 0 (là số mặc định để chuyển hết ảnh sang định dạng), byte array length
        Bitmap bitmap = BitmapFactory.decodeByteArray(categories.getCategories_image(), 0, categories.getCategories_image().length);
        holder.cimg_card_categories.setImageBitmap(bitmap);

        //c. Sự kiện khi click vào item -> Gửi dữ liệu vào interface
        holder.itemView.setOnClickListener(v -> {
            //Truyền đối tượng vào interface
            itemCategoriesListener.onClickItemCategories(categories);
        });
    }

    //1. Chiều dài list
    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    //I. ViewHolder
    public class CategoriesViewHolder extends RecyclerView.ViewHolder {

//        private final RelativeLayout layout_item_categories; //Không cần khai báo lại cả layout của item -> Dùng itemView (Tương đương)
        private final CircleImageView cimg_card_categories;
        private TextView txt_categories_name, ic_selected;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            cimg_card_categories = itemView.findViewById(R.id.circleImageView_categories_card);
            txt_categories_name = itemView.findViewById(R.id.textView_card_categories);
        }
    }

}
