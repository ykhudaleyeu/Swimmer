package com.develop.rs_school.swimmer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.develop.rs_school.swimmer.model.*
import com.develop.rs_school.swimmer.network.SwimmerApi
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    private var customers = MutableLiveData<List<Customer>>()
    private var calendar = MutableLiveData<List<CustomerCalendarItem>>()
    private var lessons = MutableLiveData<List<CustomerLesson>>()

    private var coroutineJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + coroutineJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        testAPI()
        button_signin.setOnClickListener {
            val toolbarActivity = Intent(this, HomeScreenActivity::class.java)
            startActivity(toolbarActivity)
        }
    }

    private fun testAPI() {

        uiScope.launch {
            SwimmerApi.firstAuth()
            customers.value = SwimmerApi.getCustomersImpl()
            //TODO parallel ?
            calendar.value = SwimmerApi.getCustomerCalendarImpl("2376")//name.value ?: "")
            lessons.value = SwimmerApi.getCustomerLesson("2376")//name.value ?: "")
        }
        customers.observe(this, Observer { newValue ->
            Toast.makeText(this, newValue.toString(), Toast.LENGTH_LONG).show()
        })
        calendar.observe(this, Observer { newValue ->
            Toast.makeText(this, newValue.toString(), Toast.LENGTH_LONG).show()
        })
        lessons.observe(this, Observer { newValue ->
            Toast.makeText(this, newValue.toString(), Toast.LENGTH_LONG).show()
        })
    }

    //TODO need paid_count
    fun getCustomerLessonsWithFullInfo(customerId: String){
        uiScope.launch {
            val calendarList = SwimmerApi.getCustomerCalendarImpl("2385")
            val lessonList = SwimmerApi.getCustomerLesson("2385")

            var resultList = mutableListOf<CustomerLessonWithFullInfo>()
            for(item in calendarList)
            {
            }
        }
    }

    private fun auth(authData: String){
        uiScope.launch {
            SwimmerApi.firstAuth()
            try {
                val res = SwimmerApi.getCustomersImpl().first { it.phone.contains(authData) }
                Log.d("1", res.name)
            }
            catch (e: NoSuchElementException){
                Log.d("1", "authError")
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        coroutineJob.cancel()
    }
}
