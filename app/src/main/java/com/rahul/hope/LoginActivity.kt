package com.rahul.hope

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class LoginActivity : AppCompatActivity() {

    private var mFirebaseAuth: FirebaseAuth? = null
    private var mAuthStateListner: FirebaseAuth.AuthStateListener? = null
    private var mUsername: String? = null
    val ANONYMOUS = "anonymous"
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mUsername = ANONYMOUS
        val sharedPreferences = this.getSharedPreferences(sharedPath, 0)
        mFirebaseAuth = FirebaseAuth.getInstance()

        mAuthStateListner = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                val progress = sharedPreferences.getFloat(STATUS, 0f).toInt()
                if(progress == 0){
                    startActivity(Intent(this, TestActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }

                onSignedInInitialize(user.displayName)
            } else {
                onSignedOutCleanup()
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder().setIsSmartLockEnabled(false)
                        .setAvailableProviders(
                            Arrays.asList<AuthUI.IdpConfig>(
                                AuthUI.IdpConfig.GoogleBuilder().build(),
                                AuthUI.IdpConfig.EmailBuilder().build()
                            )
                        )
                        .build(),
                    RC_SIGN_IN
                )
            }
        }
    }

    fun onSignedInInitialize(userName: String?) {
        mUsername = userName
    }

    fun onSignedOutCleanup() {
        mUsername = ANONYMOUS
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show()

            } else if (resultCode == RESULT_CANCELED) {
                finish()
            }
        }
    }

    protected override fun onResume() {
        super.onResume()
        if (mAuthStateListner != null) {
            mFirebaseAuth!!.addAuthStateListener(mAuthStateListner!!)
        }
    }

    protected override fun onPause() {
        super.onPause()
        if (mAuthStateListner != null) {
            mFirebaseAuth!!.removeAuthStateListener(mAuthStateListner!!)
        }
    }
}
