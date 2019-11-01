package com.amazinline.data.local

import com.amazinline.data.model.Customer

/**
 * Main entry point for accessing item(customer) data.
 *
 *
 * should add callbacks to other methods to inform the user of network/database errors or successful operations.
 * usually every operation on database or network should be executed in a different thread.
 */
interface CustomerDataSource {

//    interface LoadItemsCallback {
//        fun onCustomerLoaded(items: List<Item>)
//        fun onDataNotAvailable()
//    }

    interface GetCustomerCallback {

        fun onCustomerLoaded(customer: Customer)

        fun onDataNotAvailable()
    }

    fun getCustomerById(id: Int, callback: GetCustomerCallback)
    fun saveCustomer(customer: Customer)
    fun updateCustomer(customer: Customer)
    fun getCustomerByEmail(email: String, callback: GetCustomerCallback)
}