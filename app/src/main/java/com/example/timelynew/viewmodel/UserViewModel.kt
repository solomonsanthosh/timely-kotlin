package com.example.timelynew.viewmodel

import androidx.lifecycle.*
import com.example.timelynew.model.entity.User

class UserViewModel:ViewModel() {

  private var user = MutableLiveData<User>()
    val userData: LiveData<User>
    get() = user




    fun updateUser(usernew: User){
        user.value = usernew
    }
  fun getUser(): LiveData<User> {
    return userData
  }





}
//class UserViewModelFactory(private val user: User): ViewModelProvider.Factory{
// override fun <T : ViewModel> create(modelClass: Class<T>): T {
//     if(modelClass.isAssignableFrom(UserViewModel::class.java)){
//       return UserViewModel(user) as T
//     } else {
//         throw IllegalArgumentException("unknown view model")
//     }
// }
//}

