package com.amazinline.data.repository

import com.amazinline.data.local.CustomerDataSource
import com.amazinline.data.local.LocalDataSource
import com.amazinline.data.model.Customer

/**
 * Implementation to load items from the data sources.
 *
 *
 * Implements synchronisation between locally persisted data and data
 * obtained from the server(if there is)
 */
class CustomerRepository(val itemDataSource: LocalDataSource) : CustomerDataSource {

    private val TAG = javaClass.simpleName

    override fun getCustomerById(id: Int, callback: CustomerDataSource.GetCustomerCallback) {
        itemDataSource.getCustomerById(id, object: CustomerDataSource.GetCustomerCallback {
            override fun onCustomerLoaded(customer: Customer) {
                callback.onCustomerLoaded(customer)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }

        })
    }

    override fun saveCustomer(customer: Customer) {
        itemDataSource.saveCustomer(customer)
    }

    override fun updateCustomer(customer: Customer) {
        itemDataSource.updateCustomer(customer)
    }

    override fun getCustomerByEmail(email: String, callback: CustomerDataSource.GetCustomerCallback) {
        itemDataSource.getCustomerByEmail(email, object: CustomerDataSource.GetCustomerCallback {
            override fun onCustomerLoaded(customer: Customer) {
                callback.onCustomerLoaded(customer)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }

        })
    }

    companion object {

        private var INSTANCE: CustomerRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         */
        @JvmStatic
        fun getInstance(itemLocalDataSource: LocalDataSource) =
            INSTANCE ?: synchronized(CustomerRepository::class.java) {
                INSTANCE ?: CustomerRepository(itemLocalDataSource)
                    .also { customerRepository ->
                        INSTANCE = customerRepository
                    }
            }

    }
}