package com.example.usahaq_skripsi.repo.repository


import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.usahaq_skripsi.adapter.BusinessAdapter
import com.example.usahaq_skripsi.model.*
import com.example.usahaq_skripsi.ui.add.business.AddBusinessActivity
import com.example.usahaq_skripsi.ui.add.product.AddProductActivity
import com.example.usahaq_skripsi.ui.add.purchase.AddPurchaseActivity
import com.example.usahaq_skripsi.ui.dashboard.DashboardActivity
import com.example.usahaq_skripsi.ui.edit.EditBusinessActivity
import com.example.usahaq_skripsi.ui.edit.EditProductActivity
import com.example.usahaq_skripsi.ui.edit.EditPurchaseActivity
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
                EditBusinessActivity.isSuccess = true
            }
            .addOnFailureListener { e->
                Log.e("EDIT", "EDIT Busines Failed because $e")
                EditBusinessActivity.isSuccess = false
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

    override fun showListProduct(businessId : String): LiveData<List<Product>> {
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
                EditProductActivity.isSuccess = true
            }
            .addOnFailureListener { e->
                Log.e("EDIT", "EDIT Product Failed because $e")
                EditProductActivity.isSuccess = true
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

    override fun createPurchase(purchase: Purchase) {
        firestore.collection("purchase").document(purchase.purchaseId.toString())
            .set(purchase)
            .addOnSuccessListener {
                Log.d("CREATE", "Purhcase ${purchase.purchaseId} succesfully made!")
                AddPurchaseActivity.isSuccess = true
            }
            .addOnFailureListener { e ->
                Log.e("CREATE", "Purchase Failed because $e")
                AddPurchaseActivity.isSuccess = false
            }
    }

    override fun showListPurchase(businessId: String): LiveData<List<Purchase>> {
        val purchaseData = MutableLiveData<List<Purchase>>()
        val purchaseList = ArrayList<Purchase>()
        firestore.collection("purchase").whereEqualTo("businessId", businessId)
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Show Purchase", "Purchase succesfully shown")
                for(document in documents) {
                    purchaseList.add(
                        Purchase(
                            name = document.getString("name").toString(),
                            purchaseId = document.getString("purchaseId").toString(),
                            businessId = document.getString("businessId").toString(),
                            price = document.getString("price").toString(),
                            stocks = document.getString("stocks").toString(),
                            imageUrl = document.getString("imageUrl").toString(),
                            date = document.getString("date").toString()
                        )
                    )
                }
                purchaseData.postValue(purchaseList)
            }
            .addOnFailureListener { e ->
                Log.e("Show Purchase", "$e")
            }
        return purchaseData
    }

    override fun editPurchase(purchase: Purchase) {
        firestore.collection("purchase").document(purchase.purchaseId!!)
            .update("name", purchase.name, "price", purchase.price,
            "stocks", purchase.stocks, "date", purchase.date, "imageUrl", purchase.imageUrl)
            .addOnSuccessListener {
                Log.d("EDIT", "Purchase ${purchase.purchaseId} succesfully editted!")
                EditPurchaseActivity.isSuccess = true
            }
            .addOnFailureListener { e->
                Log.e("EDIT", "EDIT Purchase Failed because $e")
                EditPurchaseActivity.isSuccess = false
            }
    }

    override fun detailPurchase(purchaseId: String): LiveData<Purchase> {
        val purchaseData = MutableLiveData<Purchase>()
        firestore.collection("purchase").document(purchaseId).get()
            .addOnSuccessListener { document ->
                Log.d("Show Purchase", "Purchase succesfully shown")
                purchaseData.postValue(
                    Purchase(
                        name = document.getString("name").toString(),
                        purchaseId = document.getString("purchaseId").toString(),
                        businessId = document.getString("businessId").toString(),
                        price = document.getString("price").toString(),
                        stocks = document.getString("stocks").toString(),
                        imageUrl = document.getString("imageUrl").toString(),
                        date = document.getString("date").toString()
                    )
                )
            }
            .addOnFailureListener { e ->
                Log.e("Show Purchase", "$e")
            }
        return purchaseData
    }

    override fun deletePurchase(purchaseId: String) {
        firestore.collection("purchase").document(purchaseId).delete()
    }

    override fun createSales(sales: Sales, productSold: ArrayList<ProductSold>) {
        firestore.collection("sales").document(sales.salesId.toString())
            .set(sales)
            .addOnSuccessListener {
                Log.d("CREATE", "Sales ${sales.salesId} succesfully made!")
                for (i in productSold){
                    firestore.collection("productSold").document(i.productSoldId.toString())
                        .set(i)
                }
            }
            .addOnFailureListener { e ->
                Log.e("CREATE", "Purchase Failed because $e")
            }

    }

    override fun deleteSales(salesId: String) {
        firestore.collection("sales").document(salesId).delete()
        firestore.collection("productSold").whereEqualTo("salesId", salesId).get()
            .addOnSuccessListener { documents ->
                for (i in documents){
                    firestore.collection("productSold").document(
                        i.getString("productSoldId").toString()
                    ).delete()
                }
            }
    }

    override fun showListSales(businessId: String): LiveData<List<Sales>> {
        val salesData = MutableLiveData<List<Sales>>()
        val salesList = ArrayList<Sales>()
        firestore.collection("sales").whereEqualTo("businessId", businessId)
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Show Sales", "Sales succesfully shown")
                for(document in documents) {
                    salesList.add(
                        Sales(
                            salesId = document.getString("salesId").toString(),
                            businessId = document.getString("businessId").toString(),
                            totalPrice = document.getString("totalPrice").toString(),
                            date = document.getString("date").toString(),
                            paymentMethod = document.getString("paymentMethod").toString()
                        )
                    )
                }
                salesData.postValue(salesList)
            }
            .addOnFailureListener { e ->
                Log.e("Show Sales", "$e")
            }
        return salesData
    }

    override fun detailSales(salesId: String): LiveData<Sales> {
        val salesData = MutableLiveData<Sales>()
        firestore.collection("sales").document(salesId).get()
            .addOnSuccessListener { document ->
                Log.d("Show Sales", "Sales succesfully shown")
                salesData.postValue(
                    Sales(
                        salesId = document.getString("salesId").toString(),
                        businessId = document.getString("businessId").toString(),
                        totalPrice = document.getString("totalPrice").toString(),
                        date = document.getString("date").toString(),
                        paymentMethod = document.getString("paymentMethod").toString()
                    )
                )
            }
            .addOnFailureListener { e ->
                Log.e("Show Purchase", "$e")
            }
        return salesData
    }

    override fun detailProductSold(salesId: String): LiveData<List<ProductSold>> {
        val productSoldData = MutableLiveData<List<ProductSold>>()
        val productSoldList = ArrayList<ProductSold>()
        firestore.collection("productSold").whereEqualTo("salesId", salesId)
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Show product sold", "product sold succesfully shown")
                for(document in documents) {
                    productSoldList.add(
                        ProductSold(
                            name = document.getString("name").toString(),
                            amount = document.getString("amount").toString(),
                            price = document.getString("price").toString(),
                            note = document.getString("note").toString(),
                            salesId = document.getString("sales_id").toString()
                        )
                    )
                }
                productSoldData.postValue(productSoldList)
            }
            .addOnFailureListener { e ->
                Log.e("Show Sales", "$e")
            }
        return productSoldData
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