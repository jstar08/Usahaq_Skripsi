package com.example.usahaq_skripsi.repo.repository


import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.usahaq_skripsi.adapter.BusinessAdapter
import com.example.usahaq_skripsi.adapter.ProductAdapter
import com.example.usahaq_skripsi.model.Account
import com.example.usahaq_skripsi.model.Business
import com.example.usahaq_skripsi.model.Product
import com.example.usahaq_skripsi.ui.add.AddBusinessActivity
import com.example.usahaq_skripsi.ui.add.AddProductActivity
import com.example.usahaq_skripsi.ui.dashboard.DashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

@Suppress("UNCHECKED_CAST")
class Repository(val auth : FirebaseAuth, val firestore: FirebaseFirestore, val context : Context) :
    DataSource {

    override fun createAccount(account: Account) {
        auth.createUserWithEmailAndPassword(
            account.customEmail.toString(), account.password.toString()
        ).addOnSuccessListener {
            val uid = auth.currentUser?.uid.toString()
            firestore.collection("users").document(uid).set(account)
                .addOnSuccessListener {
                    Log.d("REGISTER", "Users $uid succesfully sign up!")
                    val intent = Intent(context, DashboardActivity::class.java)
                    startActivity(context,intent, null)
                }
                .addOnFailureListener {
                    Log.e("REGISTER", "$uid failed to sign up because of $it")
                }
        }.addOnFailureListener {
            Log.e("REGISTER", "failed to sign up because of $it")
        }
    }

    override fun signIn(email : String, password : String) {
        val uid = auth.currentUser?.uid.toString()
        auth.signInWithEmailAndPassword(
            email, password
        ).addOnSuccessListener {
            Log.d("SIGN_IN", "Users $uid succesfully sign in!")
            val intent = Intent(context, DashboardActivity::class.java)
            startActivity(context, intent, null)
        }.addOnFailureListener {
            Log.e("SIGN_IN", "Sign In Failed because of $it")
        }
    }

    override fun createBusiness(business: Business) {
        firestore.collection("business").document(business.businessId.toString())
            .set(business)
            .addOnSuccessListener {
                Log.d("CREATE", "Business ${business.businessId} succesfully made!")
                AddBusinessActivity.isSuccess = true
            }
            .addOnFailureListener { e ->
                Log.e("CREATE", "Add Busines Failed because $e")
                AddBusinessActivity.isSuccess = false
            }
    }

    override fun showlistBusiness(adapter : BusinessAdapter) : LiveData<List<Business>> {
        val uid = auth.currentUser?.uid.toString()
        val businessData = MutableLiveData<List<Business>>()
        val businessList = ArrayList<Business>()
        firestore.collection("business").whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Show Business", "Business ${uid} succesfully shown")
                for(document in documents) {
                    businessList.add(
                        Business(
                            businessId = document.getString("businessId").toString(),
                            name = document.getString("name").toString(),
                            address = document.getString("address").toString(),
                            userId = document.getString("userid").toString(),
                            imageUrl = document.getString("imageUrl").toString()
                        )
                    )
                }
                businessData.postValue(businessList)
            }
            .addOnFailureListener { e ->
                Log.e("Show Business", "$e")
            }
        return businessData
    }

    override fun DetailBusiness(businessId: String): LiveData<Business> {
        val businessData = MutableLiveData<Business>()
        firestore.collection("business").document(businessId).get()
            .addOnSuccessListener { document ->
               businessData.postValue(
                   Business(
                       businessId = document.getString("businessId").toString(),
                       name = document.getString("name").toString(),
                       address = document.getString("address").toString(),
                       userId = document.getString("userid").toString(),
                       imageUrl = document.getString("imageUrl").toString()
                   )
               )
             }
            .addOnFailureListener { e->
                Log.e("detail Business", "$e")
            }
        return businessData
    }

    override fun editBusiness(business: Business) {
        firestore.collection("business").document(business.businessId!!)
            .update("address", business.address!!, "imageUrl", business.imageUrl!!,
            "name", business.name!!)
            .addOnSuccessListener {
                Log.d("EDIT", "Business ${business.businessId} succesfully editted!")
            }
            .addOnFailureListener { e->
                Log.e("EDIT", "EDIT Busines Failed because $e")
            }
    }

    override fun showUserProfile() : LiveData<Account> {
        val uid = auth.currentUser?.uid.toString()
        val userDetail = MutableLiveData<Account>()

        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { user ->
                val userData = Account(
                    phoneNumber = user.getString("phoneNumber").toString(),
                    location = user.getString("location").toString(),
                    customEmail = user.getString("customEmail").toString(),
                    imageUrl = user.getString("imageUrl").toString(),
                    name = user.getString("name").toString(),
                )
                userDetail.postValue(userData)
            }
            .addOnFailureListener { e->
                Log.e("Show Profile", "$e")
            }
        return userDetail
    }

    override fun createProduct(product: Product) {
        firestore.collection("product").document(product.productId.toString())
            .set(product)
            .addOnSuccessListener {
                Log.d("CREATE", "Product ${product.businessId} succesfully made!")
                AddProductActivity.isSuccess = true
            }
            .addOnFailureListener { e ->
                Log.e("CREATE", "Product Failed because $e")
                AddProductActivity.isSuccess = false
            }
    }

    override fun showListProduct(businessId : String, adapter: ProductAdapter): LiveData<List<Product>> {
        val productData = MutableLiveData<List<Product>>()
        val productList = ArrayList<Product>()
        firestore.collection("product").whereEqualTo("businessId", businessId)
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Show Product", "Product succesfully shown")
                for(document in documents) {
                    productList.add(
                        Product(
                            productId = document.getString("productId").toString(),
                            businessId = businessId,
                            name = document.getString("name").toString(),
                            stocks = document.getString("stocks").toString(),
                            price = document.getString("price").toString(),
                            imageUrl = document.getString("imageUrl").toString(),
                            description = document.getString("description").toString()
                        )
                    )
                }
                productData.postValue(productList)
            }
            .addOnFailureListener { e ->
                Log.e("Show Product", "$e")
            }
        return productData
    }

    override fun editProduct(product: Product) {
        firestore.collection("product").document(product.productId!!)
            .update("description", product.description, "imageUrl", product.imageUrl,
                "name", product.name, "stocks", product.stocks, "price", product.price)
            .addOnSuccessListener {
                Log.d("EDIT", "Product ${product.productId} succesfully editted!")
            }
            .addOnFailureListener { e->
                Log.e("EDIT", "EDIT Product Failed because $e")
            }
    }

    override fun detailProduct(productId: String): LiveData<Product> {
        val productData = MutableLiveData<Product>()
        firestore.collection("product").document(productId).get()
            .addOnSuccessListener { document ->
                productData.postValue(
                    Product(
                        productId = document.getString("productId").toString(),
                        businessId = document.getString("businessId").toString(),
                        name = document.getString("name").toString(),
                        stocks = document.getString("stocks").toString(),
                        price = document.getString("price").toString(),
                        imageUrl = document.getString("imageUrl").toString(),
                        description = document.getString("description").toString()
                    )
                )
            }
            .addOnFailureListener { e->
                Log.e("detail Product", "$e")
            }
        return productData
    }

    override fun deleteProduct(productId: String) {
        firestore.collection("product").document(productId).delete()
    }


    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(auth : FirebaseAuth, firestore: FirebaseFirestore, context : Context): Repository
        = instance ?: synchronized(this) {
                instance ?: Repository(auth, firestore, context).apply { instance = this }
            }
    }
}