package com.amazinline.data.local

import com.amazinline.AppExecutors
import com.amazinline.data.model.Customer

class LocalDataSource private constructor(private val appExecutors: AppExecutors, private val customerDao: CustomerDao):
    CustomerDataSource {

    /**
     * Get a customer by id
     * Fetch from db
     */
    override fun getCustomerById(id: Int, callback: CustomerDataSource.GetCustomerCallback) {
        // Run on another thread while fetching
        appExecutors.diskIO.execute {
            val customer = customerDao.getCustomerById(id)

            // Run on the main thread
            appExecutors.mainThread.execute {
                if (customer != null) {
                    callback.onCustomerLoaded(customer)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun saveCustomer(customer: Customer) {
        appExecutors.diskIO.execute {
            customerDao.insertCustomer(customer)
        }
    }

    override fun updateCustomer(customer: Customer) {
        appExecutors.diskIO.execute {
            customerDao.updateCustomer(customer)
        }
    }

    override fun getCustomerByEmail(email: String, callback: CustomerDataSource.GetCustomerCallback) {
        // Run on another thread while fetching
        appExecutors.diskIO.execute {
            val customer = customerDao.getCustomerByEmail(email)

            // Run on the main thread
            appExecutors.mainThread.execute {
                if (customer != null) {
                    callback.onCustomerLoaded(customer)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    /**
     * Return an instance of this class
     */
    companion object {
        private var INSTANCE: LocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, customerDao: CustomerDao): LocalDataSource {
            if (INSTANCE == null) {
                synchronized(LocalDataSource::javaClass) {
                    INSTANCE = LocalDataSource(appExecutors, customerDao)
                }
            }
            return INSTANCE!!
        }
    }

}