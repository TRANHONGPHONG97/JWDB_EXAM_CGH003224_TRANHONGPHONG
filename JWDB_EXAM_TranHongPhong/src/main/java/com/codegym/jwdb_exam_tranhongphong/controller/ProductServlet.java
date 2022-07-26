package com.codegym.jwdb_exam_tranhongphong.controller;


import com.codegym.jwdb_exam_tranhongphong.dao.CategoryDAO;
import com.codegym.jwdb_exam_tranhongphong.dao.ProductDAO;
import com.codegym.jwdb_exam_tranhongphong.model.Product;
import com.codegym.jwdb_exam_tranhongphong.utils.ValidateUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 50,
        maxRequestSize = 1024 * 1024 * 50)
@WebServlet(name = "product", urlPatterns = "/product")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    public ProductServlet() {
        super();

    }

//    public int getNoOfRecords() {
//        return noOfRecords;
//    }

    private String errors = "";

    public void init() {
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
        if (this.getServletContext().getAttribute("listCategory") == null) {
            this.getServletContext().setAttribute("listCategory", categoryDAO.selectAllCategory());

        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html/charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    showCreateForm(req, resp);
                    break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "delete":
                    showDeleteForm(req, resp);
                    break;
                default:
//                case "list":
//                    listUser(req, resp);
                    listProductPaging(req, resp);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    insertProduct(request, response);
                    break;
                case "edit":
                    updateProduct(request, response);
                    break;
                default:
                    listProduct(request, response);
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listProductPaging(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1;
        int recordsPerPage = 5;
        String q = "";
        int category_id = -1;
        if (request.getParameter("q") != null) {
            q = request.getParameter("q");
        }
        if (request.getParameter("category_id") != null) {
            category_id = Integer.parseInt(request.getParameter("category_id"));
        }
        System.out.println(category_id + " tao la role");
        if (request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));
        List<Product> listProduct = productDAO.selectProductsPaging((page - 1) * recordsPerPage, recordsPerPage, q, category_id);
        int noOfRecords = productDAO.getNoOfRecords();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

        System.out.println(listProduct);
        request.setAttribute("list", listProduct);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("q", q);
        request.setAttribute("category_id", category_id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/products/listProduct.jsp");
        dispatcher.forward(request, response);
    }


    private void showDeleteForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        productDAO.deleteProduct(id);
        List<Product> list = productDAO.selectAllProduct();
        request.setAttribute("list", list);


        response.sendRedirect("/product");
//
//        RequestDispatcher dispatcher = request.getRequestDispatcher("/user_manager");
//        dispatcher.forward(request, response);
//        int page = 1;
//        int recordsPerPage = 3;
//        String q = "";
//        int idrole = -1;
//        if (request.getParameter("q") != null) {
//            q = request.getParameter("q");
//        }
//        if (request.getParameter("idrole") != null) {
//            idrole = Integer.parseInt(request.getParameter("idrole"));
//        }
//        System.out.println(idrole + " tao la role");
//        if (request.getParameter("page") != null)
//            page = Integer.parseInt(request.getParameter("page"));
//        List<User> listUser = userDao.selectUsersPaging((page - 1) * recordsPerPage, recordsPerPage,q, idrole);
//        int noOfRecords = userDao.getNoOfRecords();
//        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
//
//        request.setAttribute("list", listUser);
//        request.setAttribute("noOfPages", noOfPages);
//        request.setAttribute("currentPage", page);
//        request.setAttribute("q", q);
//        request.setAttribute("idrole", idrole);
//        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/listUser.jsp");
//        dispatcher.forward(request, response);
    }


    private void showCreateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Product product = new Product();
        req.setAttribute("product", product);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/products/create_product.jsp");
        dispatcher.forward(req, resp);

    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Product existingUser = productDAO.selectProduct(id);
        req.setAttribute("product", existingUser);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/products/edit_product.jsp");
        dispatcher.forward(req, resp);
    }

    private void listProduct(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> list = productDAO.selectAllProduct();
        req.setAttribute("list", list);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/products/listProduct.jsp");
        dispatcher.forward(req, resp);

    }


    private void updateProduct(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {
//        int id = Integer.parseInt(request.getParameter("id"));
//        String name = request.getParameter("name");
//        String image = request.getParameter("image");
//        double price = Double.parseDouble(request.getParameter("price"));
//        int quantity = Integer.parseInt(request.getParameter("quantity"));
//
//        int idCategory = Integer.parseInt(request.getParameter("category_id"));
//        Product product = new Product(id, name, image, price, quantity, idCategory);
//        productDAO.updateProduct(product);
////        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/edit_product.jsp");
////          dispatcher.forward(request, response);
//        response.sendRedirect("/product");

        Product product;

//         String name = req.getParameter("name").trim();
//        String image = req.getParameter("image").trim();
//     String price = (req.getParameter("price").trim());
//       String quantity = (req.getParameter("quantity").trim());
//        String category_id = String.valueOf(Integer.parseInt(req.getParameter("category_id").trim()));

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/products/edit_product.jsp");
        int id = (Integer.parseInt(req.getParameter("id").trim()));
        String name = req.getParameter("name").trim();
        String price = req.getParameter("price").trim();
        String quantity = req.getParameter("quantity").trim();
        String color = req.getParameter("color").trim();
        String description = req.getParameter("description").trim();
        int category_id = Integer.parseInt(req.getParameter("category_id").trim());

        List<String> errors = new ArrayList<>();
        boolean isPrice = ValidateUtils.isPriceValid(String.valueOf(price));
        boolean isQuantity = ValidateUtils.isQuantityValid(String.valueOf(quantity));
        product = new Product(id, name, price, quantity, color, description, category_id);

        if (name.isEmpty() ||
                price.isEmpty() ||
                quantity.isEmpty() ||
                color.isEmpty() ||
                description.isEmpty()) {
            errors.add("Vui lòng nhập đủ và chính xác thông tin!");
        }
        if (name.isEmpty()) {
            errors.add("Tên sản phẩm không được để trống!");
        }
        if (price.isEmpty()) {
            errors.add("Giá không được để trống!");
        }
        if (quantity.isEmpty()) {
            errors.add("Số lượng không được  trống!");
        }
        if (color.isEmpty()) {
            errors.add("Màu không được để trống!");
        }
        if (description.isEmpty()) {
            errors.add("Mô tả không được để trống!");
        }
        if (!isQuantity) {
            errors.add("Số lượng không đúng định dạng!");
        }
        if (!isPrice) {
            errors.add("Giá không đúng định dạng!");

        } else if (errors.size() == 0) {
            product = new Product(id, name, price, quantity, color, description, category_id);
            boolean success = false;
            success = productDAO.updateProduct(product);
            if (success) {
                req.setAttribute("success", true);
            } else {
                req.setAttribute("errors", true);
                errors.add("Dữ liệu không đúng, vui lòng kiểm tra lại!");
            }
        }
        if (errors.size() > 0) {
            req.setAttribute("errors", errors);
            req.setAttribute("product", product);
        }
        dispatcher.forward(req, resp);
    }

    private void insertProduct(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, IOException, ServletException {


//        String name = request.getParameter("name");
//        String image = request.getParameter("image");
//       double price = Double.parseDouble(request.getParameter("price"));
//        int quantity = Integer.parseInt(request.getParameter("quantity"));
//
//        int category_id = Integer.parseInt(request.getParameter("category_id"));
//
//        Product product = new Product(name, image, price, quantity, category_id);
//        productDAO.insertProduct(product);
//        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/products/create_product.jsp");
//        dispatcher.forward(request, response);

        Product product;
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/products/create_product.jsp");
        String name = req.getParameter("name").trim();
        String price = (req.getParameter("price").trim());
        String quantity = (req.getParameter("quantity").trim());
        String color = req.getParameter("color").trim();
        String description = req.getParameter("description").trim();
        String category_id = String.valueOf(Integer.parseInt(req.getParameter("category_id").trim()));

        List<String> errors = new ArrayList<>();

        boolean isPrice = ValidateUtils.isPriceValid(price);
        boolean isQuantity = ValidateUtils.isQuantityValid(quantity);

        product = new Product(name, price, quantity, color, description, category_id);

        if (name.isEmpty() ||
                price.isEmpty() ||
                quantity.isEmpty() ||
                color.isEmpty() ||
                description.isEmpty() ||
                category_id.isEmpty()) {
            errors.add("Vui lòng nhâp đầy đủ và chính xác thông tin!");
        }
        if (name.isEmpty()) {
            errors.add("Tên sản phẩm không được để trống!");
        }
        if (price.isEmpty()) {
            errors.add("Giá không được để trống!");
        }
        if (color.isEmpty()) {
            errors.add("Màu không được để trống!");
        }
        if (description.isEmpty()) {
            errors.add("Mô tả không được để trống!");
        }
        if (!isPrice) {
            errors.add("Giá không đúng định dạng!");
        }
        if (quantity.isEmpty()) {
            errors.add("Số lượng không được để trống!");
        }
        if (!isQuantity) {
            errors.add("Số lượng không đúng định dạng!");
        } else if (errors.size() == 0) {
            product = new Product(name, price, quantity, color, description, category_id);
            boolean success = false;
            success = productDAO.insertProduct(product);

            if (success) {
                req.setAttribute("success", true);
            } else {
                req.setAttribute("errors", true);
                errors.add("Dữ liệu không hợp lệ, vui lòng kiểm tra lại! ");
            }
        }
        if (errors.size() > 0) {
            req.setAttribute("errors", errors);
            req.setAttribute("product", product);
        }
        dispatcher.forward(req, resp);
    }
}


//        response.sendRedirect("/user_manager");

//        Product product = new Product();
//        boolean flag = true;
//        Map<String, String> hashMap = new HashMap<String, String>();
//
//
////            product.setId(Integer.parseInt(request.getParameter("id")));
//            product.setName(request.getParameter("name"));
//            product.setImage(request.getParameter("image"));
//            product.setPrice(Double.parseDouble(request.getParameter("price")));
//            product.setQuantity(Integer.parseInt(request.getParameter("quantity")));
//            product.setCategory_id(Integer.parseInt(request.getParameter("category_id")));


//            ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
//            Validator validator = validatorFactory.getValidator();
//            Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);

//            if (!constraintViolations.isEmpty()) {
//
//                errors = "";
//                // constraintViolations is has error
//                for (ConstraintViolation<Product> constraintViolation : constraintViolations) {
//                    errors += "" + constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage()
//                            + "";
//                }
//                errors += "";
//                request.setAttribute("product", product);
//                request.setAttribute("errors", errors);
//                List<Product> productList = productDAO.selectAllProduct();
//                request.setAttribute("listRole", productList);
//                request.getRequestDispatcher("/WEB-INF/view/create_product.jsp").forward(request, response);
//            } else {
//                if (userDao.selectUserByEmail(email) != null) {
//                    flag = false;
//                    hashMap.put("email", "Email exits in database");
//                    System.out.println(this.getClass() + " Email exits in database");
//                }
//                if (userDao.selectUserByUserName(userName) != null) {
//                    flag = false;
//                    hashMap.put("user", "UserName exits in database");
//                    System.out.println(this.getClass() + " User Name exits in database");
//                }
//                if (userDao.selectUserByPhone(phone) != null) {
//                    flag = false;
//                    hashMap.put("phone", "Phone exits in database");
//                    System.out.println(this.getClass() + " User Name exits in database");
//                }
//                if (iRoleDao.selectRole(idRole) == null) {
//                    flag = false;
//                    hashMap.put("user", "Country value invalid");
//                    System.out.println(this.getClass() + " Country invalid");
//                }
//                if (flag) {
//                    userDao.insertUser(user);
//                    User u = new User();
//                    request.setAttribute("user", u);
//                    request.getRequestDispatcher("WEB-INF/view/create_product.jsp").forward(request, response);
//                } else {
//                    errors = "";
//                    hashMap.forEach(new BiConsumer<String, String>() {
//                        @Override
//                        public void accept(String keyError, String valueError) {
//                            errors += "" + valueError
//                                    + "";
//                        }
//                    });
//                    errors += "";
//
//                    request.setAttribute("user", user);
//                    request.setAttribute("errors", errors);
//                    request.getRequestDispatcher("/WEB-INF/view/create_product.jsp").forward(request, response);
//                }
//            }
//        } catch (NumberFormatException ex) {
//            errors = "";
//            errors += "" + "Input format not right"
//                    + "";
//            errors += "";
//            request.setAttribute("user", user);
//            request.setAttribute("errors", errors);
//            request.getRequestDispatcher("/WEB-INF/view/create_product.jsp").forward(request, response);
//        } catch (Exception ex) {
//        }
