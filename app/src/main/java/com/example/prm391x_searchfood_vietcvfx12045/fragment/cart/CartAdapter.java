package com.example.prm391x_searchfood_vietcvfx12045.fragment.cart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apachat.swipereveallayout.core.SwipeLayout;
import com.apachat.swipereveallayout.core.ViewBinder;
import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanSanPhamMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.model.Cart;

import java.sql.SQLException;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private List<Cart> cartList;
    private IClickCartItemListener cartItemListener;
    private TruyVanSanPhamMSSQL truyVanSanPham = new TruyVanSanPhamMSSQL();
    private ViewBinder viewBinder = new ViewBinder(); //Dùng để setup swipe cho RecyclerView | Nếu không có sẽ bị lỗi hiện lại swipe

    public CartAdapter(List<Cart> cartList, IClickCartItemListener cartItemListener) {
        this.cartList = cartList;
        this.cartItemListener = cartItemListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    //Xử lý từng dòng item Cart
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        //1. Set giá trị của list vào layout item
        Cart cart = cartList.get(position);

        Bitmap bitmap = BitmapFactory.decodeByteArray(cart.getProduct_image(), 0, cart.getProduct_image().length);
        holder.img_item_cart_image.setImageBitmap(bitmap);

        holder.txt_item_cart_product_name.setText(cart.getProduct_name());
        holder.txt_item_cart_product_price.setText(cart.getPrice() + "$");
        holder.txt_item_cart_unit.setText("/" + cart.getUnit());
        holder.txt_item_cart_product_sum.setText("$"+ cart.getSum());
        holder.edt_number_item_cart.setText(cart.getAmount() + "");

        //2. Sự kiện click giảm số lượng 1 trong item
        holder.img_reduce_item_cart.setOnClickListener(v -> {

            String numberStr = holder.edt_number_item_cart.getText().toString().trim();
            int number = Integer.parseInt(numberStr);

            number--;
            if(number <= 0) number = 1;

            holder.edt_number_item_cart.setText(number + "");
        });

        //3. Sự kiện click tăng số lượng 1 trong item
        holder.img_plus_item_cart.setOnClickListener(v -> {

            String numberStr = holder.edt_number_item_cart.getText().toString().trim();
            int number = Integer.parseInt(numberStr);

            number++;
            if(number <= 0) {
                number = 1;
            } else if(number > 1000){
                Toast.makeText(v.getContext(), "Can't not > 1000", Toast.LENGTH_SHORT).show();
                return;
            }

            holder.edt_number_item_cart.setText(number + "");
        });

        //4. Sự kiện khi thay đổi EditText (số lượng product của một dòng cart) -> cập nhật cart đang dùng, amount và sum trên dòng, tổng ngoài CartFragment
        holder.edt_number_item_cart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //Hàm thực hiện ngay khi có thay đổi
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //a. Kiểm tra nếu editText not empty
                if(!TextUtils.isEmpty(holder.edt_number_item_cart.getText())) {

                    //Check lại số lượng editText, set không vượt quá 1000
                    int amount_current = Integer.parseInt(holder.edt_number_item_cart.getText().toString().trim());

                    if(amount_current > 1000){
                        amount_current = 1000;
                        holder.edt_number_item_cart.setText(1000 + "");
                    }

                    //b. Thực hiện gán giá trị hiển thị tổng số tiền của dòng cart
                    double price_product = cart.getPrice();
                    double sum_current = amount_current * price_product;
                    double sum_final = (double) Math.round(sum_current * 100) / 100; //làm tròn 2 số thập phân
                    holder.txt_item_cart_product_sum.setText("$" + sum_final);

                    //c. Gán lại giá trị cho cart (gốc) hiện tại, chỉ cần cập nhật lại số lượng
                    cart.setAmount(amount_current);

                    //d. Cập nhật dữ liệu của dòng Cart trong CSDL (Số lượng và tổng tiền)
                    try {
                        truyVanSanPham.updateCartItem(amount_current, sum_final, cart.getId_cart());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    //d. Set lại editText = 1 nếu bị trống
                } else {
                    holder.edt_number_item_cart.setText(1 + "");
                }

                //e. Load lại hiển thị tổng tiền trong CartFragment theo mỗi lần thay đổi số của item
                try {
                    CartFragment.Instances().setTotalMoneyOfCart();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //5. Sự kiện truyền dữ liệu cart ra màn hình sử dụng
        holder.img_item_cart_image.setOnClickListener(v -> {
            cartItemListener.onClickItemCart(cart);
        });

        //6. Xoá item trong list
        //Setup cho thư viện ViewBinder: truyền layout swipe, id của product (theo thứ tự position trong list)
        viewBinder.bind(holder.swipeLayout, String.valueOf(cart.getId_cart()));

        //Sự kiện xoá ở (recyclerView hiển thị) khi click (đang dùng cả layout (xoá) để xoá, có thể tách riêng image hoặc từng đối tượng, nếu nhiều ô khi swipe thì nên dùng LinearLayout)
        //Phải xoá cả ở CSDL bên trong sự kiện
        holder.layout_swipe_delete_item_cart.setOnClickListener(v -> {

            cartList.remove(holder.getLayoutPosition()); //Xoá vị trí của list (hiển thị) tương ứng vị trí được chọn
            notifyItemRemoved(holder.getLayoutPosition()); //Cập nhật rcv (với vị trí đã tương tác xoá)

            //Xoá dòng cart trong CSDL (theo id_cart)
            try {
                truyVanSanPham.DeleteCart(cart.getId_cart());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_item_cart_image, img_reduce_item_cart, img_plus_item_cart;
        private TextView txt_item_cart_product_name, txt_item_cart_product_price, txt_item_cart_product_sum, txt_item_cart_unit;
        private EditText edt_number_item_cart;
        private SwipeLayout swipeLayout;
        private RelativeLayout layout_swipe_delete_item_cart, layout_item_cart;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            img_item_cart_image = itemView.findViewById(R.id.imageView_item_cart_image);
            txt_item_cart_product_name = itemView.findViewById(R.id.textView_item_cart_product_name);
            txt_item_cart_product_price = itemView.findViewById(R.id.textView_item_cart_product_price);
            txt_item_cart_unit = itemView.findViewById(R.id.textView_item_cart_product_unit);

            img_reduce_item_cart = itemView.findViewById(R.id.reduce_item_cart);
            edt_number_item_cart = itemView.findViewById(R.id.editText_number_item_cart);
            img_plus_item_cart = itemView.findViewById(R.id.plus_item_cart);
            txt_item_cart_product_sum = itemView.findViewById(R.id.textView_item_cart_product_sum);

            //Ánh xạ các layout phục vụ swipe
            swipeLayout = itemView.findViewById(R.id.layout_all_item_cart);
            layout_swipe_delete_item_cart = itemView.findViewById(R.id.layout_swipe_delete_item_cart);
            layout_item_cart = itemView.findViewById(R.id.layout_item_cart);
        }
    }

    //Dùng interface truyền dữ liệu (Tạo dự phòng chứ chưa sử dụng để truyền dữ liệu ra Fragment)
    public interface IClickCartItemListener {
        void onClickItemCart(Cart cart);
    }
}
