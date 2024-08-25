package com.example.expensetracker.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.expensetracker.entity.Expense;
import com.example.expensetracker.service.ExpenseService;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

	@Autowired
	private ExpenseService expenseService;

	@GetMapping("/")
	public String viewHomePage(Model model) {
		List<Expense> allExpenses = expenseService.getAllExpenses();

		// Calculate the total amount from all expenses
		BigDecimal totalAmount = allExpenses.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		// Add the list of expenses and the total amount to the model
		model.addAttribute("expenses", allExpenses);
		model.addAttribute("totalAmount", totalAmount);

		// Return the name of the view (e.g., "index.html" or "index.jsp")
		return "index";
	}

	// Show the form to create a new expense
	@GetMapping("/addExpense")
	public String showCreateExpenseForm(Model model) {
		model.addAttribute("expense", new Expense());
		return "add-expense"; // View for creating a new expense
	}

	// Save a new or updated expense
	@PostMapping("/saveExpense")
	public String saveExpense(@ModelAttribute("expense") Expense expense, Model model) {
		expenseService.saveExpense(expense);
		return "redirect:/"; // Redirect to the list after saving
	}

	// Show the form to edit an existing expense
	@GetMapping("/editExpense/{id}")
	public String showEditExpenseForm(@PathVariable("id") Long id, Model model) {
		Expense expense = expenseService.getExpenseById(id);
		if (expense != null) {
			model.addAttribute("expense", expense);
			return "update-expense"; // View for editing an existing expense
		} else {
			return "redirect:/"; // Redirect to the list if not found
		}
	}
	
	@PutMapping("/update/{id}")
	public String updateExpense(@PathVariable("id") Long id, @ModelAttribute("expense") Expense updatedExpense) {
	    Expense existingExpense = expenseService.getExpenseById(id);

	    if (existingExpense != null) {
	        existingExpense.setDescription(updatedExpense.getDescription());
	        existingExpense.setAmount(updatedExpense.getAmount());
	        // You can update other fields as needed

	        expenseService.saveExpense(existingExpense);
	    }

	    return "redirect:/"; // Redirect to the list after updating
	}


	// Delete an expense by ID
	@GetMapping("/delete/{id}")
	public String deleteExpense(@PathVariable("id") Long id) {
		expenseService.deleteById(id);
		return "redirect:/"; // Redirect to the list after deleting
	}
}
