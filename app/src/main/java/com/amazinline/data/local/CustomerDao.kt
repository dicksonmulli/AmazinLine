package com.amazinline.data.local

import androidx.room.*
import com.amazinline.data.model.Customer

@Dao
interface CustomerDao {

    /**
     * Select customer by id from the customers table.
     *
     * @return the customer.
     */
    @Query("SELECT * FROM customers WHERE id = :id")
    fun getCustomerById(id: Int): Customer

    /**
     * Select customer by email from the customers table.
     *
     * @return the customer.
     */
    @Query("SELECT * FROM customers WHERE email = :email")
    fun getCustomerByEmail(email: String): Customer

    /**
     * Update an customer.
     *
     * @param customer customer to be updated
     */
    @Update
    fun updateCustomer(customer: Customer): Int

    /**
     * Insert a customer in the database. If the customer already exists, replace.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCustomer(customer: Customer)
}