package com.example.timelynew.viewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.timelynew.dataClass.Activity

class ActivityViewModel(activity: ArrayList<Activity>) :ViewModel() {
    private var activities = MutableLiveData<ArrayList<Activity>>(activity)
    val activitiesData: LiveData<ArrayList<Activity>>
        get() = activities




    fun updateActivity(activity: Activity){

        var temp =  activities.value
        if (temp != null) {
            temp.add(activity)
        }
       activities.value = temp!!

    }
    fun updateActivityContent(activity: Activity){

        activities.value?.forEachIndexed{i,item ->
            if(item.id == activity.id){
                activities.value?.set(i,activity)
                return
            }
        }

    }
    fun removeActivity(activity: Activity){
        print(activities.value.toString())
        var temp =  activities.value
        if (temp != null) {
            temp.remove(activity)
        }
        activities.value = temp!!

    }

}
class ActivityViewModelFactory(private val activity: ArrayList<Activity>): ViewModelProvider.Factory{
 override fun <T : ViewModel> create(modelClass: Class<T>): T {
     if(modelClass.isAssignableFrom(ActivityViewModel::class.java)){
       return ActivityViewModel(activity) as T
     } else {
         throw IllegalArgumentException("unknown view model")
     }
 }
}