package com.codegym.jwdb_exam_tranhongphong.controller;

import com.codegym.jwdb_exam_tranhongphong.dao.CategoryDAO;
import com.codegym.jwdb_exam_tranhongphong.dao.ICategoryDAO;
import com.codegym.jwdb_exam_tranhongphong.model.Category;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CategoryServlet", urlPatterns = "/category")
public class CategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ICategoryDAO iCategoryDAO;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null) {
			action = "";
		}
		try {
			switch (action) {
				case "create":
					showNewForm(request, response);
					break;
				case "edit":
					showEditForm(request, response);
					break;
				case "delete":
					deleteCategory(request, response);
					break;
				default:
					listCategory(request, response);
					break;
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
	private void listCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Category> listCategory = iCategoryDAO.selectAllCategory();
		request.setAttribute("listCategory", listCategory);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/categories/listCategory.jsp");
		dispatcher.forward(request, response);
	}
	private void deleteCategory(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		iCategoryDAO.deleteCategory(id);
		List<Category> listCategory = iCategoryDAO.selectAllCategory();
		request.setAttribute("listCategory", listCategory);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/categories/listCategory.jsp");
		dispatcher.forward(request, response);
	}
	private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Category existingCategory = iCategoryDAO.selectCategory(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/categories/edit_product.jsp");
		request.setAttribute("category", existingCategory);
		dispatcher.forward(request, response);
	}
	private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/categories/create_product.jsp");
		dispatcher.forward(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null) {
			action = "";
		}
		try {
			switch (action) {
				case "create":
					insertCategory(request, response);
					break;
				case "edit":
					updateCategory(request, response);
					break;
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
	private void updateCategory(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		Category category = new Category(id, name);
		iCategoryDAO.updateCategory(category);
		response.sendRedirect("/category");
	}
	private void insertCategory(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		String name = request.getParameter("name");
		Category category = new Category();
		category.setName(name);
		iCategoryDAO.insertCategory(category);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/categories/create_product.jsp");
		dispatcher.forward(request, response);
	}
	@Override
	public void init() throws ServletException {
		iCategoryDAO = new CategoryDAO();
	}
}
