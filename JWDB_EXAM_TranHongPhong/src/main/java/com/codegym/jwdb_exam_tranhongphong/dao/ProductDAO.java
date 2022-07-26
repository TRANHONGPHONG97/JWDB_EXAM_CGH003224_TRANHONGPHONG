package com.codegym.jwdb_exam_tranhongphong.dao;


import com.codegym.jwdb_exam_tranhongphong.model.Product;
import com.codegym.jwdb_exam_tranhongphong.utils.MySQLConnUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProductDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/product_manager?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "CHUcu123456";
    private int noOfRecords;
    public int getNoOfRecords() {
        return noOfRecords;
    }
    protected Connection getConnection() {

        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }

    private static final String INSERT_PRODUCT_SQL = "INSERT INTO product" + "(name, price,quantity, color, description,category_id) VALUES " + " (?,?,?, ?, ?, ?);";
    private static final String SELECT_PRODUCT_BY_ID = "select id,name, image , price, quantity, category_id from product where id =?";
    private static final String SELECT_ALL_PRODUCT = "select * from product";
    private static final String DELETE_PRODUCT_SQL = "delete from product where id = ?;";
    private static final String UPDATE_PRODUCT_SQL = "update product set name = ?,price= ?, quantity =?, color =?, description=?, category_id=? where id = ?;";

    public ProductDAO() {
    }

    public boolean insertProduct(Product product) throws SQLException {
//        System.out.println(INSERT_PRODUCT_SQL);
        boolean success = false;

        try (Connection connection = MySQLConnUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT_SQL)) {
            System.out.println(preparedStatement);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setString(4, product.getColor());
            preparedStatement.setString(5, product.getDescription());
            preparedStatement.setInt(6, product.getCategory_id());
            preparedStatement.executeUpdate();
            success =true;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return success;
    }

    public Product selectProduct(int id) {
        Product product = null;

        try (Connection connection = getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID);) {
            System.out.println(preparedStatement);
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String color = rs.getString("color");
                String description = rs.getString("description");
                int category_id = rs.getInt("category_id");
                product = new Product(id, name, price, quantity, color, description, category_id);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return product;
    }

    public List<Product> selectAllProduct() {
        List<Product> product = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCT);) {
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String color = rs.getString("color");
                String description = rs.getString("description");
                int category_id = rs.getInt("category_id");
                product.add(new Product(id, name, price, quantity, color, description, category_id));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return product;
    }

    public boolean deleteProduct(int id) throws SQLException {
        boolean rowDeleted;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT_SQL);) {
            System.out.println(statement);
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateProduct(Product product) throws SQLException {
        boolean success = false;

        try (Connection connection = MySQLConnUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT_SQL);) {
            System.out.println(statement);
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setString(4,product.getColor());
            statement.setString(5,product.getDescription());
            statement.setInt(6, product.getCategory_id());
            statement.setInt(7, product.getId());
            statement.executeUpdate();
            success = true;
        } catch (SQLException e) {
            MySQLConnUtils.printSQLException(e);
        }
        return success;
    }
    public List<Product> selectProductsPaging(int offset, int noOfRecords) {
        String query = "select SQL_CALC_FOUND_ROWS * from product limit " + offset + ", " + noOfRecords;
        List<Product> list = new ArrayList<Product>();
        Product product = null;
        Statement stmt = null;
        Connection connection = null;
        try {
            connection = getConnection();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setColor(rs.getString("color"));
                product.setDescription(rs.getString("description"));
                product.setCategory_id(rs.getInt("category_id"));
                list.add(product);

            }
            rs.close();

            rs = stmt.executeQuery("SELECT FOUND_ROWS()");
            if (rs.next())
                this.noOfRecords = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    public List<Product> selectProductsPaging(int offset, int noOfRecords, String q, int category_id) {
        List<Product> list = new ArrayList<Product>();
        Product product = null;
        PreparedStatement stmt = null;
        Connection connection = null;
        try {
            connection = getConnection();
            if (category_id != -1) {
                String query = "select SQL_CALC_FOUND_ROWS * from product where name like ? and category_id = ? limit "
                        + offset + ", " + noOfRecords;
                stmt = connection.prepareStatement(query);
                stmt.setString(1, '%' + q + '%');
                stmt.setInt(2, category_id);
            } else {
                if (category_id == -1) {
                    String query = "select SQL_CALC_FOUND_ROWS * from product where name like ? limit "
                            + offset + ", " + noOfRecords;
                    stmt = connection.prepareStatement(query);
                    stmt.setString(1, '%' + q + '%');
                }
            }
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(Double.parseDouble(rs.getString("price")));
                product.setQuantity(Integer.parseInt(rs.getString("quantity")));
                product.setColor(rs.getString("color"));
                product.setDescription(rs.getString("description"));
                product.setCategory_id(rs.getInt("category_id"));
                list.add(product);
            }
            rs.close();

            rs = stmt.executeQuery("SELECT FOUND_ROWS()");
            if (rs.next())
                this.noOfRecords = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
