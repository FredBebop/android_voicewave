package com.example.test.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MysqlService {
    String url = "jdbc:mysql://42.192.55.234:3306/flash";
    String name = "root";
    String passwd = "612330Zf";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement statement=null;
    ResultSet resultSet = null;

    private void init() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, name, passwd);
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }

    public void register(String username, String password) {
        try {
            String GBKusername= URLEncoder.encode(username, "GBK");
            String sql = "insert into account (username,password) values(?,?)";
            if (connection == null) {
                init();
            }
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, GBKusername);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
        } catch (SQLException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public boolean isUsername(String username) {
        try {
            String GBKusername= URLEncoder.encode(username, "GBK");
            String sql = "select username from account";
            if (connection == null) {
                init();
            }
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("username").equals(GBKusername)) {
                    return true;
                }
            }
            return false;
        } catch (SQLException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String queryUsername(String uid){
        try{
            String sql ="select id,username from account where id=\""+uid+"\"";
            if (connection == null) {
                init();
            }
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getString("username");
            } else return "无名小辈";
        } catch (SQLException e) {
            e.printStackTrace();
            return "无名小辈";
        }
    }

    public int queryUid(String username) {

        try {
            String GBKusername= URLEncoder.encode(username, "GBK");
            String sql = "select id,username from account where username=\"" + GBKusername + "\"";
            if (connection == null) {
                init();
            }
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else return 0;
        } catch (SQLException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean isPassword(String username, String password) {
        try {
            String GBKusername= URLEncoder.encode(username, "GBK");
            String sql = "select password,username from account where username=\"" + GBKusername + "\"";
            if (connection == null) {
                init();
            }
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getString("password").equals(password);
        } catch (SQLException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void release() {
        closeConn(connection);
        closeRs(resultSet);
        closeSt(preparedStatement);
    }

    private void closeRs(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs = null;
        }
    }

    private void closeSt(PreparedStatement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            st = null;
        }
    }

    private void closeConn(Connection ct) {
        try {
            if (ct != null) {
                ct.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ct = null;
        }
    }


}
