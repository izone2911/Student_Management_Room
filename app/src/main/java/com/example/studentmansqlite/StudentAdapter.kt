package com.example.studentmansqlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class StudentAdapter(val students: List<StudentModel>): BaseAdapter() {
    override fun getCount(): Int = students.size

    override fun getItem(p0: Int): Any = students[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            itemView = LayoutInflater.from(parent?.context).inflate(R.layout.layout_student_item, parent, false)
            viewHolder = ViewHolder(itemView)
            itemView.tag = viewHolder
        } else {
            itemView = convertView
            viewHolder = itemView.tag as ViewHolder
        }

        val student = students[position]
        viewHolder.textMssv.text = student.mssv
        viewHolder.textName.text = student.name

        return itemView
    }

    class ViewHolder(itemView: View) {
        val textName: TextView
        val textMssv: TextView
        init {
            textName = itemView.findViewById(R.id.text_student_name)
            textMssv = itemView.findViewById(R.id.text_student_id)
        }
    }
}