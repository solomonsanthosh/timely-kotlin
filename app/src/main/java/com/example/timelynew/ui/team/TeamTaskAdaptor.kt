package com.example.timelynew.ui.team


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.timelynew.R
import com.example.timelynew.dataClass.TeamTask

class TeamTaskAdaptor(val context:Activity,val teamTask:ArrayList<TeamTask>):ArrayAdapter<TeamTask>(context,R.layout.membercard,teamTask) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view:View = inflater.inflate(R.layout.membercard,null)
        val title:TextView = view.findViewById(R.id.teamTitle)
        val content:TextView = view.findViewById(R.id.teamContent)
        val date:TextView = view.findViewById(R.id.teamDate)
        val member:TextView = view.findViewById(R.id.member)
        val status:TextView = view.findViewById(R.id.memberStatus)
        val attachment:TextView = view.findViewById(R.id.attachmentTeam)

        if(teamTask.get(position).file == null) {

            attachment.visibility = View.GONE
        } else {

            attachment.setOnClickListener{

                val uris = Uri.parse(teamTask.get(position).file)
                val intents = Intent(Intent.ACTION_VIEW, uris)
                val b = Bundle()
                b.putBoolean("new_window", true)
                intents.putExtras(b)
                context.startActivity(intents)

            }
        }



        title.text = teamTask.get(position).title
        content.text = teamTask.get(position).content
        date.text = teamTask.get(position).date.toString()
        member.text = teamTask.get(position).members.get(0)
        status.text = teamTask.get(position).status.get(0)

        return view
    }


}