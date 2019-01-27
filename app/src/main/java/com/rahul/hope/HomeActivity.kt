package com.rahul.hope

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.rahul.hope.data.DataRepository
import com.rahul.hope.data.database.EmergencyContactEntry
import com.rahul.hope.fragments.EmergencyCallFragment
import com.rahul.hope.listeners.JobListener
import com.rahul.hope.listeners.LaunchBottomSheetListener
import com.rahul.hope.viewmodels.ContactsViewModel

import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeActivity : AppCompatActivity(), LaunchBottomSheetListener, JobListener {
    private var phoneNo = ""

    private lateinit var sharedPreferences: SharedPreferences
    private val TAG = HomeActivity::class.java.simpleName
    private val REQUEST_CONTACTS_PERMISSION = 1234
    private val REQUEST_CALL_PERMISSION = 1235
    private val REQUEST_SMS_PERMISSION = 1236
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var bottomSheetBehavior2: BottomSheetBehavior<View>
    private lateinit var dataRepository: DataRepository
    private lateinit var viewModel : ContactsViewModel
    private var currentSheet = 0

    private var displayName = FirebaseAuth.getInstance().currentUser?.displayName!!
    private val quotes = arrayOf(
        "Do not let what you cannot do interfere with what you can do.",
        "Although the world is full of suffering, it is also full of the overcoming of it.",
        "Never confuse a single defeat with a final defeat.",
        "Depression is a prison where you are both the suffering prisoner and the cruel jailer.",
        "Once you choose hope, anything is possible.",
        "A positive attitude gives you power over your circumstances instead of your circumstances having power over you."
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        sharedPreferences = this.getSharedPreferences(sharedPath, 0)
        dataRepository = (application as HopeApplication).applicationComponent.getRepository()
        val viewModelFactory = (application as HopeApplication).applicationComponent.getViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ContactsViewModel::class.java)

        shadowView.visibility = View.GONE
        shadowView2.visibility = View.GONE

        userNameTextView.text = "Hello, $displayName"
        val progress = sharedPreferences.getFloat(STATUS, 30f).toInt()
        statusTextView.text = "$progress %"
        statusProgressBar.progress = progress
        quoteTextView.text = quotes[(0..5).random()]
        fab.setOnClickListener {
            bottomSheetBehavior2.state = BottomSheetBehavior.STATE_EXPANDED
        }

        bottomSheetBehavior = BottomSheetBehavior.from(design_bottom_sheet.view)
        bottomSheetBehavior2 = BottomSheetBehavior.from(design_bottom_sheet2.view)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior2.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        shadowView.visibility = View.GONE
                        fab.show()
                    }
                    else -> {
                        shadowView.visibility = View.VISIBLE
                        fab.hide()
                    }
                }
            }

        })

        bottomSheetBehavior2.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        shadowView2.visibility = View.GONE
                        fab.show()
                    }
                    else -> {
                        shadowView2.visibility = View.VISIBLE
                        fab.hide()
                    }
                }
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {

                val outRect = Rect()
                design_bottom_sheet.view?.getGlobalVisibleRect(outRect)

                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

                    return true
                }

            } else if (bottomSheetBehavior2.state == BottomSheetBehavior.STATE_EXPANDED) {

                val outRect = Rect()
                design_bottom_sheet2.view?.getGlobalVisibleRect(outRect)

                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    bottomSheetBehavior2.state = BottomSheetBehavior.STATE_HIDDEN

                    return true
                }

            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onBackPressed() {
        if(bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else if(bottomSheetBehavior2.state != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior2.state = BottomSheetBehavior.STATE_HIDDEN
        }else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == EmergencyCallFragment.REQUEST_CONTACT) {
            if(resultCode == Activity.RESULT_OK) {
                val result = data?.data

                val id = result?.lastPathSegment
                val c = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone._ID + "=?",
                    arrayOf(id), null)

                val phoneIdx = c!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val nameIdx = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                if(c.count == 1) { // contact has a single phone number
                    // get the only phone number
                    if(c.moveToFirst()) {
                        val phone = c.getString(phoneIdx)
                        val name = c.getString(nameIdx)
                        dataRepository.addContact(EmergencyContactEntry(phone, name))
                    } else {
                        Log.w(TAG, "No results")
                    }
                }
                c.close()
                return
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            REQUEST_CONTACTS_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onJobRequested(EmergencyCallFragment.REQUEST_CONTACT)
                } else {
                    Snackbar.make(fab, "Denied permission for contacts", Snackbar.LENGTH_LONG)
                        .setAction("Ask Again") {
                            onJobRequested(EmergencyCallFragment.REQUEST_CONTACT)
                        }.show()
                }
                return
            }
            REQUEST_CALL_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    makeCall(phoneNo)
                } else {
                    Snackbar.make(fab, "Denied permission to Call", Snackbar.LENGTH_LONG)
                        .setAction("Ask Again") {
                            makeCall(phoneNo)
                        }.show()
                }
                return
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }

    }

    override fun makeCall(number: String) {
        phoneNo = number
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PERMISSION)
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNo")
            startActivity(callIntent)
        }
    }

    override fun onJobRequested(code: Int) {
        if(code == EmergencyCallFragment.REQUEST_CONTACT) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CONTACTS_PERMISSION)
            } else {
                val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
                startActivityForResult(intent, EmergencyCallFragment.REQUEST_CONTACT)
            }
        }
    }

    override fun launchBottomSheet(id: Int) {
        if(id == 1) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else if (id == 2) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.SEND_SMS), REQUEST_SMS_PERMISSION)
            } else {
                sendMessageToEveryOne()
            }

        } else if(id == 3) {
            bottomSheetBehavior2.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun sendMessageToEveryOne() {
        viewModel.personalEmergencyList.observe(this, Observer {
            it?.let { allList ->
                val smsManager = SmsManager.getDefault()
                for(contact in allList.subList(0,2)) {
                    smsManager.sendTextMessage(contact.phoneNo, null, "Hello ${contact.name}",
                        null, null)
                }
                viewModel.personalEmergencyList.removeObservers(this)
            }
        })
    }
}
