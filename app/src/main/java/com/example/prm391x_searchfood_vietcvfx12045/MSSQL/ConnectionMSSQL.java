package com.example.prm391x_searchfood_vietcvfx12045.MSSQL;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionMSSQL {

    //I. Tạo kết nối SQL Server
    @SuppressLint("NewApi")
    public Connection ConnectionClass(){

        String ip = "192.168.1.2", port="30661", dbname="PRMAssignment3", dbuser="sa", dbpass="123456";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection conn = null;
        String connectURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + dbname + ";User=" + dbuser + ";password=" + dbpass + ";";
            conn = DriverManager.getConnection(connectionUrl);

        } catch (Exception ex){
            Log.e("Set Error", ex.getMessage());
            Log.d("Error", "Can't connect MS SQL!");
        }

        return conn;
    }
}
