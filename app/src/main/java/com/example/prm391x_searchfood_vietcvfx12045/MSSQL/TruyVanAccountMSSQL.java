package com.example.prm391x_searchfood_vietcvfx12045.MSSQL;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.prm391x_searchfood_vietcvfx12045.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TruyVanAccountMSSQL {
    private Connection connection = new ConnectionMSSQL().ConnectionClass();

    //I. Kiểm tra tài khoản có tồn tại
    public boolean checkAccount(String email) throws SQLException {

        if (connection != null){
            //Khai báo dữ liệu, câu lệnh truy vấn MS SQL
            String SQL = "Select * from Account where email = ?";

            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, email); //Tham số truyền vào (dấu ?)

            ResultSet result = statement.executeQuery(); //Thực hiện truy vấn trả về

            int count = 0;
            while (result.next()){
                count++;
            }

            if (count == 0){
                return false;
            }

            if (count > 0){
                return true;
            }

            statement.close();
        }

        return false;
    }

    //II. Thêm tài khoản vào CSDL | Chú ý dùng đúng câu lệnh executeUpdate()
    public void InsertAccount(String email, String password, String datetime) throws SQLException {

        if (connection != null){
            //Khai báo dữ liệu, câu lệnh truy vấn MS SQL
            String SQL = "Insert into Account values (?,?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, email); //Tham số truyền vào (dấu ?)
            statement.setString(2, password);
            statement.setString(3,null);
            statement.setString(4, null);
            statement.setString(5, datetime);

            statement.executeUpdate(); //Thực hiện cập nhật (chú ý phải đúng kiểu câu lệnh là update hay query)

            statement.close();
        }
    }

    //III. Kiểm tra khớp mật khẩu và email (khi login)
    public boolean checkEmailPassword(String email, String password) throws SQLException {

        if (connection != null){
            //Khai báo dữ liệu, câu lệnh truy vấn MS SQL
            String SQL = "Select email, password from Account where email = ?";

            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, email); //Tham số truyền vào (dấu ?)

            ResultSet result = statement.executeQuery(); //Thực hiện truy vấn trả về bảng tạm

            String emailSQL = "";
            String passwordSQL = "";

            while (result.next()){
                emailSQL = result.getString("email");
                passwordSQL = result.getString("password");
            }

            if (email.equalsIgnoreCase(emailSQL) && password.equals(passwordSQL)){
                return true;
            } else {
                return false;
            }

        }

        return false;
    }

    //IV.1 Lấy thông tin tài khoản theo email và password
    public Account getAccount(String email, String password) throws SQLException {

        String SQL = "Select * from Account where email = ? and password = ?";

        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setString(1, email);
        statement.setString(2, password);

        ResultSet result = statement.executeQuery();

        Account account = new Account();

        while (result.next()){
            account.setId_account(result.getInt("id_account"));
            account.setEmail(result.getString("email"));
            account.setPassword(result.getString("password"));
            account.setPhone(result.getString("phone"));
            account.setAddress(result.getString("address"));
            account.setRegister_date(result.getString("register_date"));
        }

        return account;
    }

    //IV.1 Lấy thông tin tài khoản theo id
    public Account getAccount(int id_account) throws SQLException {

        String SQL = "Select * from Account where id_account = ?";

        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setInt(1, id_account);

        ResultSet result = statement.executeQuery();

        Account account = new Account();

        while (result.next()){
            account.setId_account(result.getInt("id_account"));
            account.setEmail(result.getString("email"));
            account.setPassword(result.getString("password"));
            account.setPhone(result.getString("phone"));
            account.setAddress(result.getString("address"));
            account.setRegister_date(result.getString("register_date"));
        }

        return account;
    }

    //V.1 Thêm ảnh avatar
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertImageAccount (int id_account, byte [] imageArr) throws SQLException {

        //Lấy thời gian ngay khi sử dụng hàm này
        LocalDateTime myDateTime = LocalDateTime.now();
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date_register = myDateTime.format(myFormat);

        //Check xem đã có avatar hay chưa, nếu chưa có thì thêm mới
        if(!checkAvatar(id_account, 1)) {

            String SQL = "Insert into ImageAccount values (?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, id_account);
            statement.setBytes(2, imageArr); //Chú ý set mảng byte [] là dùng setBytes

            statement.setString(3, date_register);
            statement.setInt(4, 1); //Status để mặc định là 1 (Tuỳ ý sử dụng sau, vd như 1 là lưu, 2 là xoá | Hoặc 1 là ảnh avatar, 2 là ảnh banner ...)

            statement.executeUpdate();

            //Nếu đã có rồi thì update
        } else {

            String SQL = "Update ImageAccount set image = ?, date_register = ?  where id_account = ? and status = ?";
            PreparedStatement statement = connection.prepareStatement(SQL);

            statement.setBytes(1, imageArr);
            statement.setString(2, date_register);
            statement.setInt(3, id_account);
            statement.setInt(4, 1); //status avatar

            statement.executeUpdate();
        }
    }

    //V.2 Lấy ảnh đại diện (về mảng byte)
    public byte [] getAvatar(int id_account, int status) throws SQLException {

        String SQL = "Select image from ImageAccount where id_account = ? and status = ?";

        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setInt(1, id_account);
        statement.setInt(2, status);

        ResultSet result = statement.executeQuery();

        byte [] image = null;

        while (result.next()){
            image = result.getBytes("image");
        }

        return image;
    }

    //V.3 Check xem đã có ảnh đại diện chưa
    public boolean checkAvatar(int id_account, int status) throws SQLException {

        String SQL = "Select * from ImageAccount where id_account = ? and status = ?";

        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setInt(1, id_account);
        statement.setInt(2, status);

        ResultSet result = statement.executeQuery();

        int count = 0;

        while (result.next()){
            count++;
        }

        if(count == 0){
            return false;
        }

        if(count > 0){
            return true;
        }

        return false;
    }

    //VI.1 Update phone
    public void UpdateAccountPhone(int id_account, String phone) throws SQLException {

        String SQL = "Update Account set phone = ? where id_account = ?";

        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setString(1, phone);
        statement.setInt(2, id_account);

        statement.executeUpdate();
        statement.close();

    }

    //VI.2 Update Address
    public void UpdateAccountAddress(int id_account, String address) throws SQLException {

        String SQL = "Update Account set address = ? where id_account = ?";

        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setString(1, address);
        statement.setInt(2, id_account);

        statement.executeUpdate();
        statement.close();

    }

    //VI.3. Update password
    public void UpdateAccountPassword(int id_account, String new_password) throws SQLException {

        String SQL = "Update Account set password = ? where id_account = ?";

        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setString(1, new_password);
        statement.setInt(2, id_account);

        statement.executeUpdate();
        statement.close();


    }
}
