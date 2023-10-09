package com.example.prm391x_searchfood_vietcvfx12045.MSSQL;


import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.prm391x_searchfood_vietcvfx12045.fragment.favorite.Favorite;
import com.example.prm391x_searchfood_vietcvfx12045.model.Cart;
import com.example.prm391x_searchfood_vietcvfx12045.model.Categories;
import com.example.prm391x_searchfood_vietcvfx12045.model.MyOrder;
import com.example.prm391x_searchfood_vietcvfx12045.model.MyOrderDetails;
import com.example.prm391x_searchfood_vietcvfx12045.model.Product;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TruyVanSanPhamMSSQL {

    private final Connection connection = new ConnectionMSSQL().ConnectionClass();

    //I. Lấy danh sách categories
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Categories> getListCategories() throws SQLException {

        if (connection != null) {

            String SQL = "Select * from Categories";

            PreparedStatement statement = connection.prepareStatement(SQL);

            ResultSet result = statement.executeQuery();

            List<Categories> listCategories = new ArrayList<>();

            while (result.next()) {

                int id_categories = result.getInt("id_categories");
                String categories_name = result.getString("categories_name");

                //Chuyển ảnh về dạng Blob -> mảng byte ->
                Blob imageBlob = result.getBlob("categories_image");
                byte [] blobAsBytes = imageBlob.getBytes(1, (int) imageBlob.length());

                int views = result.getInt("views");

                Categories categories = new Categories(id_categories, categories_name, blobAsBytes, views);
                listCategories.add(categories);
            }

            return listCategories;
        }

        return null;
    }

    //II.1 Lấy danh sách Product (Theo nhóm id_categories_in)
    public List<Product> getListProduct(int id_categories_in) throws SQLException {

        if (connection != null) {

            String SQL = "Select * from Product where id_categories = ?";

            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, id_categories_in);

            ResultSet result = statement.executeQuery();

            List<Product> listProduct = new ArrayList<>();

            while (result.next()) {

                int id_product = result.getInt("id_product");
                int id_categories = result.getInt("id_categories");
                String product_name = result.getString("product_name");

                //Chuyển ảnh về mảng byte
                Blob imgBlob_product_image = result.getBlob("product_image");
                byte [] product_image = imgBlob_product_image.getBytes(1, (int) imgBlob_product_image.length());

                String description = result.getString("description");
                double price = result.getDouble("price");
                String unit = result.getString("unit");
                Long views = result.getLong("views");

                //Thêm product vào list
                Product product = new Product(id_product, id_categories, product_name, product_image, description, price, unit, views);
                listProduct.add(product);

            }

            return listProduct;

        } else {
            Log.d("errorSQL", "Can't connect Database");
        }

        return null;
    }

    //II.2 Lấy danh sách Product theo key
    public List<Product> getListProductSearchKey(String searchKey) throws SQLException {

            String SQL = "Select * from Product where product_name like '%" + searchKey + "%'";

            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet result = statement.executeQuery();

            List<Product> listProduct = new ArrayList<>();

            while (result.next()) {

                int id_product = result.getInt("id_product");
                int id_categories = result.getInt("id_categories");
                String product_name = result.getString("product_name");

                //Chuyển ảnh về mảng byte
                Blob imgBlob_product_image = result.getBlob("product_image");
                byte [] product_image = imgBlob_product_image.getBytes(1, (int) imgBlob_product_image.length());

                String description = result.getString("description");
                double price = result.getDouble("price");
                String unit = result.getString("unit");
                Long views = result.getLong("views");

                //Thêm product vào list
                Product product = new Product(id_product, id_categories, product_name, product_image, description, price, unit, views);
                listProduct.add(product);

            }

            return listProduct;

    }

    //II.3 Lấy danh sách Product (Toàn bộ)
    public List<Product> getListAllProduct() throws SQLException {

        String SQL = "Select * from Product";

        PreparedStatement statement = connection.prepareStatement(SQL);
        ResultSet result = statement.executeQuery();

        List<Product> listProduct = new ArrayList<>();

        while (result.next()) {

            int id_product = result.getInt("id_product");
            int id_categories = result.getInt("id_categories");
            String product_name = result.getString("product_name");

            //Chuyển ảnh về mảng byte
            Blob imgBlob_product_image = result.getBlob("product_image");
            byte [] product_image = imgBlob_product_image.getBytes(1, (int) imgBlob_product_image.length());

            String description = result.getString("description");
            double price = result.getDouble("price");
            String unit = result.getString("unit");
            Long views = result.getLong("views");

            //Thêm product vào list
            Product product = new Product(id_product, id_categories, product_name, product_image, description, price, unit, views);
            listProduct.add(product);
        }

        return listProduct;

    }

    //III. Lấy thông tin một sản phẩm
    public Product getProduct(int id_product) throws SQLException {

        if (connection != null) {

            String SQL = "Select * from Product where id_product = ?";

            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, id_product);

            ResultSet result = statement.executeQuery();

            Product product = new Product();

            while (result.next()) {
                //Set các thông tin cho Product
                product.setId_product(result.getInt("id_product"));
                product.setId_categories(result.getInt("id_categories"));
                product.setProduct_name(result.getString("product_name"));

                //Chuyển ảnh về mảng byte
                Blob imgBlob_product_image = result.getBlob("product_image");
                byte [] product_image = imgBlob_product_image.getBytes(1, (int) imgBlob_product_image.length());
                product.setProduct_image(product_image);

                product.setDescription(result.getString("description"));
                product.setPrice(result.getDouble("price"));
                product.setUnit(result.getString("unit"));
                product.setViews(result.getLong("views"));

            }

            return product;

        } else {
            Log.d("errorSQL", "Can't connect Database");
        }

        return null;
    }

    //IV. Lấy danh sách Cart của id_account đang login
    public List<Cart> getListCart(int id_account, int status) throws SQLException {

        if (connection != null) {

            String SQL = "Select id_cart, id_account, Cart.id_product, amount, sum, status, date_register, product_name, product_image, price, unit \n" +
                        "from Cart join Product on Cart.id_product = Product.id_product\n" +
                        "where id_account = ? and status = ? ORDER BY date_register ASC";

            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, id_account);
            statement.setInt(2, status);

            ResultSet result = statement.executeQuery();

            List<Cart> listCart = new ArrayList<>();

            while (result.next()) {

                int id_cart = result.getInt("id_cart");
                int id_account_sql = result.getInt("id_account");
                int id_product = result.getInt("id_product");
                int amount = result.getInt("amount");
                double sum = result.getDouble("sum");
                int status_sql = result.getInt("status");
                String date_register = result.getString("date_register");
                String product_name = result.getString("product_name");

                Blob imageBlob = result.getBlob("product_image");
                byte [] product_image = imageBlob.getBytes(1, (int) imageBlob.length());

                double price = result.getDouble("price");
                String unit = result.getString("unit");


                //Thêm product vào list
                Cart cart = new Cart(id_cart, id_account_sql, id_product, amount, sum, status_sql, date_register, product_name, product_image, price, unit);
                listCart.add(cart);

            }

            return listCart;

        } else {
            Log.d("errorSQL", "Can't connect Database");
        }

        return null;
    }

    //V. Cập nhật số lượng và tổng tiền của một (dòng) cart
    public void updateCartItem(int amount, double sum, int id_cart) throws SQLException {

        if (connection != null) {

            String SQL = "Update Cart set amount = ?, sum = ? where id_cart = ?";

            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, amount);
            statement.setDouble(2, sum);
            statement.setInt(3, id_cart);

            statement.executeUpdate();

        } else {
            Log.d("errorSQL", "Can't connect Database");
        }
    }

    //VI. Thêm một sản phẩm (hoặc thêm số lượng nếu đã có) vào Cart (hiện tại đều đang insert với status là 1)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void InsertProductToCart(int id_account, int id_product, int amount, double sum) throws SQLException {

        //Lấy thời gian ngày (thời điểm khi thực hiện hàm này)
        LocalDateTime myDateTime = LocalDateTime.now();
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date_register = myDateTime.format(myFormat);

        //1. Nếu chưa có trong Cart của CSDL (check cả status = 1 và 2) thì thêm vào CSDL
        // (Cũng gần giống check id_cart nhưng tra theo id_account thì sau này dễ khôi phục hơn, vì ở trạng thái 2 (xoá) thì cũng phải tra theo id_account và id_product mới tìm được id_cart)
        if(!checkProductInCart(id_account, id_product, 1) && !checkProductInCart(id_account, id_product, 2)) {
            String SQL = "Insert into Cart values (?,?,?,?,?,?)";
            PreparedStatement statement1 = connection.prepareStatement(SQL);
            statement1.setInt(1, id_account);
            statement1.setInt(2, id_product);
            statement1.setInt(3, amount);
            statement1.setDouble(4, sum);
            statement1.setInt(5, 1); //status
            statement1.setString(6, date_register);

            //Thực hiện
            statement1.executeUpdate();

        //2. Nếu đã có trong Cart -> đặt lại trạng thái, đặt lại số lượng và sum theo đầu vào
        } else {

            //a. Nếu đã có trong giỏ hàng nhưng status = 2 (xoá) -> chuyển status = 1 (để hiển thị với người dùng), cập nhật số lượng và sum (theo đầu vào chứ không dùng dữ liệu có sẵn)
            if(checkProductInCart(id_account, id_product, 2)){
                //Lấy product để sửa lại số lượng và sum (bằng giá 1 đơn vị)
                Product product = getProduct(id_product);

                String SQL = "Update Cart set status = 1, amount = ?, sum =?, date_register =? where id_account=? and id_product=?";
                PreparedStatement statement = connection.prepareStatement(SQL);
                statement.setInt(1, amount);
                statement.setDouble(2, sum);
                statement.setString(3, date_register);
                statement.setInt(4, id_account);
                statement.setInt(5, id_product);
                statement.executeUpdate();

            //b. Nếu đã có trong giỏ hàng và status đang là 1 -> thêm số lượng và tổng tiền
            } else if (checkProductInCart(id_account, id_product, 1)) {

                String SQL = "Update Cart set amount = amount + ? , sum = sum + ?, date_register=? where id_account =? and id_product =?";
                PreparedStatement statement2 = connection.prepareStatement(SQL);
                statement2.setInt(1, amount);
                statement2.setDouble(2, sum);
                statement2.setString(3, date_register);
                statement2.setInt(4, id_account);
                statement2.setInt(5, id_product);

                statement2.executeUpdate();
            }
        }

    }

    //VII. Kiểm tra một sản phẩm có tồn tại trong giỏ hàng (Theo id_account và trạng thái status cart)
    public boolean checkProductInCart(int id_account, int id_product, int status) throws SQLException {

            String SQL = "Select * from Cart where id_account = ? and id_product = ? and status = ?";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, id_account);
            statement.setInt(2, id_product);
            statement.setInt(3, status); //Trong giỏ hàng đặt trạng thái là 1

            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()){
                count++;
            }

            if(count == 0){
                return false;
            } else if (count >0){
                return true;
            }

        return false;
    }

    //VIII.1 Lấy danh sách Product theo views
    public List<Product> getListProductViews() throws SQLException {

        if (connection != null) {

            String SQL = "Select TOP 10 * from Product order by views DESC";

            PreparedStatement statement = connection.prepareStatement(SQL);

            ResultSet result = statement.executeQuery();

            List<Product> listProduct = new ArrayList<>();

            while (result.next()) {

                int id_product = result.getInt("id_product");
                int id_categories = result.getInt("id_categories");
                String product_name = result.getString("product_name");

                //Chuyển ảnh về mảng byte
                Blob imgBlob_product_image = result.getBlob("product_image");
                byte [] product_image = imgBlob_product_image.getBytes(1, (int) imgBlob_product_image.length());

                String description = result.getString("description");
                double price = result.getDouble("price");
                String unit = result.getString("unit");
                Long views = result.getLong("views");

                //Thêm product vào list
                Product product = new Product(id_product, id_categories, product_name, product_image, description, price, unit, views);
                listProduct.add(product);

            }

            return listProduct;

        } else {
            Log.d("errorSQL", "Can't connect Database");
        }

        return null;
    }

    //VIII.2 Lấy danh sách Product theo views (Truy vấn riêng theo id_categories và khác id_product đang xem)
    public List<Product> getListProductViews(int id_categories_in, int id_product_in) throws SQLException {

        if (connection != null) {

            String SQL = "Select * from Product where id_categories = ? and id_product != ? order by views DESC";

            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, id_categories_in);
            statement.setInt(2, id_product_in);
            ResultSet result = statement.executeQuery();

            List<Product> listProduct = new ArrayList<>();

            while (result.next()) {

                int id_product = result.getInt("id_product");
                int id_categories = result.getInt("id_categories");
                String product_name = result.getString("product_name");

                //Chuyển ảnh về mảng byte
                Blob imgBlob_product_image = result.getBlob("product_image");
                byte [] product_image = imgBlob_product_image.getBytes(1, (int) imgBlob_product_image.length());

                String description = result.getString("description");
                double price = result.getDouble("price");
                String unit = result.getString("unit");
                Long views = result.getLong("views");

                //Thêm product vào list
                Product product = new Product(id_product, id_categories, product_name, product_image, description, price, unit, views);
                listProduct.add(product);

            }

            return listProduct;

        } else {
            Log.d("errorSQL", "Can't connect Database");
        }

        return null;
    }

    //IX. Cập nhật số lượng và tổng tiền của một (dòng) cart
    public void updateProductViews(int id_product) throws SQLException {

        if (connection != null) {

            String SQL = "Update Product set views = views + 1 where id_product = ?";

            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, id_product);
            statement.executeUpdate();

        } else {
            Log.d("errorSQL", "Can't connect Database");
        }
    }

    //X. Xoá một (dòng) cart (đã có trong Cart và có id_cart) -> Chuyển sang status = 2
    public void DeleteCart(int id_cart) throws SQLException {

        if (connection != null) {
            String SQL = "Update Cart set status = 2 where id_cart = ?";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, id_cart);
            statement.executeUpdate();
        } else {
            Log.d("errorSQL", "Can't connect Database");
        }
    }

    //X. Xoá tất cả cart của một id_account (chuyển trạng thái sang 2) (Trạng thái 1: Trong giỏ hàng, 2: xoá, 3: đã đặt hàng)
    public void UpdateStatusAllCart(int id_account, int status) throws SQLException {

            String SQL = "Update Cart set status = ? where id_account = ?";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, status);
            statement.setInt(2, id_account);
            statement.executeUpdate();
    }

    //XI.1. Kiểm tra một sản phẩm có tồn tại (Đã từng tương tác) trong Favorite (Theo id_account và id_product)
    public boolean checkExistFavorite(int id_account, int id_product) throws SQLException {

        String SQL = "Select * from Favorite where id_account = ? and id_product = ?";
        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setInt(1, id_account);
        statement.setInt(2, id_product);

        ResultSet result = statement.executeQuery();

        int count = 0;

        while (result.next()){
            count++;
        }

        if(count == 0){
            return false;
        } else if (count >0){
            return true;
        }

        return false;
    }

    //XI.2 Thêm một sản phẩm vào Favorite
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertFavorite(int id_account, int id_product) throws SQLException {

        if (connection != null) {

            String SQL = "Insert into Favorite values (?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, id_account);
            statement.setInt(2, id_product);
            statement.setInt(3, 1); //Trạng thái mặc định là 1 (Yêu thích)

            //Thời gian tạo thì thực hiện trong hàm này
            LocalDateTime myDateTime = LocalDateTime.now();
            DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String date_register = myDateTime.format(myFormat);
            statement.setString(4, date_register);

            statement.executeUpdate();

        } else {
            Log.d("errorSQL", "Can't connect Database");
        }
    }

    //XI.3 Lấy tình trạng Favorite (số)
    public int getStatusFavorite(int id_account, int id_product) throws SQLException {
        String SQL = "Select status from Favorite where id_account = ? and id_product=?";

        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setInt(1, id_account);
        statement.setInt(2, id_product);

        ResultSet result = statement.executeQuery();

        int status = 0;

        while (result.next()){
            status = result.getInt("status");
        }

        return status;
    }

    //XI.4 Cập nhật trạng thái Favorite
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateStatusFavorite (int id_account, int id_product, int status) throws SQLException {

        String SQL = "Update Favorite set status = ?, date_register = ? where id_account = ? and id_product = ?";

        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setInt(1, status);

        //Cập nhật luôn ngày tương tác (cũng có thể tạo ngày tương tác cuối thành cột riêng nếu cần)
        LocalDateTime myDateTime = LocalDateTime.now();
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date_register = myDateTime.format(myFormat);

        statement.setString(2, date_register);
        statement.setInt(3, id_account);
        statement.setInt(4, id_product);

        statement.executeUpdate();
    }

    //XI.5 Cập nhật trạng thái Favorite (1 là yêu thích, 2 là xoá nhưng vẫn lưu trong CSDL, nghĩa là đã được tương tác)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateAllStatusFavorite (int id_account, int status) throws SQLException {

        String SQL = "Update Favorite set status = ?, date_register = ? where id_account = ?";

        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setInt(1, status);

        //Cập nhật luôn ngày tương tác (cũng có thể tạo ngày tương tác cuối thành cột riêng nếu cần)
        LocalDateTime myDateTime = LocalDateTime.now();
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date_register = myDateTime.format(myFormat);

        statement.setString(2, date_register);
        statement.setInt(3, id_account);

        statement.executeUpdate();
    }

    //XII. Lấy danh sách Favorite của một account
    public List<Favorite> getListFavorite(int id_account, int status) throws SQLException {
        String SQL = "Select id_favorite, id_account, F.id_product, F.status, F.date_register, product_name, product_image, price, unit \n" +
                    "from Favorite F join Product P on F.id_product = P.id_product\n" +
                    "where id_account = ? and status = ? ORDER by date_register ASC";

        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setInt(1, id_account);
        statement.setInt(2, status);

        ResultSet result = statement.executeQuery();

        List<Favorite> favoriteList = new ArrayList<>();

        while (result.next()){
            int id_favorite = result.getInt("id_favorite");
            int id_account_sql = result.getInt("id_account");
            int id_product = result.getInt("id_product");
            int status_sql = result.getInt("status");
            String date_register = result.getString("date_register");
            String product_name = result.getString("product_name");

            Blob product_image_blob = result.getBlob("product_image");
            byte [] product_image_arr = product_image_blob.getBytes(1, (int) product_image_blob.length());

            double price = result.getDouble("price");
            String unit = result.getString("unit");

            //Tạo Object và add vào List
            Favorite favorite = new Favorite(product_image_arr, product_name, unit, price, id_favorite, id_account, id_product, status_sql, date_register);
            favoriteList.add(favorite);
        }

        return favoriteList;
    }

    //XIII. Thêm một danh sách Cart List vào bảng đặt hàng MyOrder và bảng thông tin đơn hàng MyOrder_Details
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void InsertCartList(int id_account, String phone, String address, byte [] img_product_largest_sum, double total_money, List<Cart> cartList) throws SQLException {

        //1. Thời gian tại thời điểm thực hiện
        LocalDateTime myDateTime = LocalDateTime.now();
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeNow = myDateTime.format(myFormat);

        String SQL1 = "Insert into MyOrder values (?,?,?,?,?,?,?,?,?,?,?,?)"; //id_order tự động nên không cần set trong hàm, mà lấy id_order vừa tạo bằng Statement.RETURN_GENERATED_KEYS

        //2. Set các dữ liệu tương ứng dấu ? -> Thực hiện câu lệnh
        PreparedStatement statement = connection.prepareStatement(SQL1, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, id_account);
        statement.setString(2, phone);
        statement.setString(3, address);
        statement.setBytes(4, img_product_largest_sum);
        statement.setDouble(5, total_money);
        statement.setString(6, timeNow);
        statement.setString(7, null);
        statement.setString(8, null);
        statement.setString(9, null);
        statement.setString(10, null);
        statement.setString(11, null);
        statement.setInt(12, 1); //Trạng thái đặt hàng là 1 (các trạng thái tiếp theo tương ứng ngày diễn ra theo thứ tự tăng lên)

        statement.executeUpdate();

        //3. Lấy key mới vừa tạo (tự động identity) trong CSDL
        ResultSet result = statement.getGeneratedKeys();
        int id_order = 0;
        while (result.next()){
            id_order = result.getInt(1);
        }

        //4. Thêm danh sách dữ liệu Cart List vào bảng MyOrder_Details (cùng id_order vừa chèn mới của bảng MyOrder, cùng một giỏ hàng)
        String SQL2 = "Insert into MyOrder_Details values(?,?,?,?,?)"; //id_order ở đây là foreign key nên phải điền vào

        PreparedStatement statement2 = connection.prepareStatement(SQL2);

        //Sử dụng vòng lặp để thực hiện câu lệnh executeUpdate() đối với từng đối tượng được lưu trong Cart list
        for(int i=0; i<cartList.size(); i++){
            statement2.setInt(1, id_order);
            statement2.setInt(2, cartList.get(i).getId_product());
            statement2.setInt(3, cartList.get(i).getAmount());
            statement2.setDouble(4, cartList.get(i).getPrice());
            statement2.setDouble(5, cartList.get(i).getAmount()*cartList.get(i).getPrice());

            statement2.executeUpdate();// Thực hiện câu lệnh SQL2
        }

    }

    //XIV. Lấy tổng tiền trong giỏ hàng của một account (những sản phẩm có trạng thái là 1)
    public double getTotalMoneyCartOfAccount(int id_account) throws SQLException {

        String SQL = "Select SUM(sum) as total_money from Cart where id_account = ? and status = 1";

        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setInt(1, id_account);
        ResultSet result = statement.executeQuery();

        //Lấy tổng tiền vừa truy vấn
        double total_money = 0.00;

        if(result.next()){
            total_money = result.getDouble("total_money");
        }

        //Làm tròn kết quả 2 số sau dấu phẩy nếu cần
//        double total_final = (double) Math.round(total_money * 100) / 100;

        return total_money;

    }

    //XV. Chọn đơn hàng của một id_account theo trạng thái
    public List<MyOrder> getListMyOrder(int id_account, int status) throws SQLException {

        String SQL = "Select * from MyOrder where id_account = ? and status = ?";

        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setInt(1, id_account);
        statement.setInt(2, status);
        ResultSet result = statement.executeQuery();

        List<MyOrder> myOrderList = new ArrayList<>();

        while (result.next()){
            int id_order = result.getInt("id_order");
            int id_account_sql = result.getInt("id_account");
            String phone = result.getString("phone");
            String address = result.getString("address");

            //Lấy ảnh bằng getBlob và chuyển về định dạng mảng byte []. Nếu ảnh bị null thì truy vấn sẽ bị lỗi
            Blob image = result.getBlob("img_product_largest_sum");
            byte [] img_product_largest_sum = image.getBytes(1, (int) image.length());

            double total_money = result.getDouble("total_money");
            String order_date = result.getString("order_date");
            String wait_ship_date = result.getString("wait_ship_date");
            String shipping_date = result.getString("shipping_date");
            String receive_date = result.getString("receive_date");
            String cancel_order_date = result.getString("cancel_order_date");
            String return_order_date = result.getString("return_order_date");
            int status_sql = result.getInt("status");

            //Gán vào list
            MyOrder myOrder = new MyOrder(id_order, id_account_sql, phone, address, img_product_largest_sum, total_money, order_date, wait_ship_date, shipping_date, receive_date, cancel_order_date, return_order_date, status_sql);
            myOrderList.add(myOrder);
        }

        //Trả về list
        return myOrderList;
    }

    //XV. Xem chi tiết thông tin một đơn hàng -> Truy vấn tất cả sản phẩm theo id_order
    public List<MyOrderDetails> getListMyOrderDetails (int id_order) throws SQLException {

        String SQL = "Select * from MyOrder_Details where id_order = ?";
        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setInt(1, id_order);

        ResultSet result = statement.executeQuery();

        List<MyOrderDetails> myOrderDetailsList = new ArrayList<>();

        while (result.next()){
            int id_order_sql = result.getInt("id_order");
            int id_product = result.getInt("id_product");
            int amount = result.getInt("amount");
            double price = result.getDouble("price");
            double sum = result.getDouble("sum");

            MyOrderDetails myOrderDetails = new MyOrderDetails(id_order_sql, id_product, amount, price, sum);
            myOrderDetailsList.add(myOrderDetails);
        }

        return myOrderDetailsList;

    }

}

