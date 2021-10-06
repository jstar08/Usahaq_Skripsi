package com.example.usahaq_skripsi.repo.repository


import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.usahaq_skripsi.adapter.BusinessAdapter
import com.example.usahaq_skripsi.model.Account
import com.example.usahaq_skripsi.model.Business
import com.example.usahaq_skripsi.repo.response.MachineLearningResponse
import com.example.usahaq_skripsi.ui.dashboard.AddBusinessActivity
import com.example.usahaq_skripsi.ui.dashboard.DashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

@Suppress("UNCHECKED_CAST")
class Repository(private val remoteData: RemoteDataSource, val auth : FirebaseAuth,
val firestore: FirebaseFirestore, val context : Context) :
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
                Log.d("CREATE", "Business ${business.userId} succesfully made!")
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

    override fun showDetailBusiness(businessId: Int) {
        TODO("Not yet implemented")
    }

    override fun showUserProfile() : LiveData<Account> {
        val uid = auth.currentUser?.uid.toString()
        val userDetail = MutableLiveData<Account>()

        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { user ->
                val userData = Account(
                    address = user.getString("address").toString(),
                    city = user.getString("city").toString(),
                    customEmail = user.getString("customEmail").toString(),
                    identityNumber = user.getString("identityNumber").toString(),
                    imageUrl = user.getString("imageUrl").toString(),
                    name = user.getString("name").toString(),
                    postalCode = user.getString("postalCode").toString(),
                    username = user.getString("username").toString()
                )
                userDetail.postValue(userData)
            }
            .addOnFailureListener { e->
                Log.e("Show Profile", "$e")
            }
        return userDetail
    }


    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(remoteData: RemoteDataSource, auth : FirebaseAuth,
                        firestore: FirebaseFirestore, context : Context): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(remoteData, auth, firestore, context).apply { instance = this }
            }
    }
}