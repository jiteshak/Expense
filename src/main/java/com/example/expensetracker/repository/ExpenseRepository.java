package com.example.expensetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.expensetracker.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long>{

}
