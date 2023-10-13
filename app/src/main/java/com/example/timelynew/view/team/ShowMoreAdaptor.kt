package com.example.timelynew.view.team

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.timelynew.R

class ShowMoreAdaptor(val context:Activity,val members:ArrayList<String>):ArrayAdapter<String>(context,R.layout.showmorecard,members) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.showmorecard,null)
        val member: TextView = view.findViewById(R.id.memberShowMore)
        val status: TextView = view.findViewById(R.id.statusShowMore)

        val memberList = ArrayList<String>()
        val statusList = ArrayList<String>()

        for(data in members){
            val i = data.split("com@")[0]
            memberList.add("${i}com")
            statusList.add(data.split("com@")[1])
        }




        member.text =memberList.get(position)
        status.text =statusList.get(position)


        return view
    }
}