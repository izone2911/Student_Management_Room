package com.example.studentmansqlite

import android.content.DialogInterface
import android.os.Bundle
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.studentmansqlite.databinding.FragmentListBinding
import com.google.android.material.snackbar.Snackbar


class ListFragment : Fragment() {

    lateinit var binding: FragmentListBinding

    lateinit var students: MutableList<StudentModel>
    lateinit var studentAdapter: StudentAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater)

        students = mutableListOf()
        //lay danh sach student tu database
        val db = (activity as MainActivity).db

        db.beginTransaction()
        try {
            //query
            val cs = db.rawQuery("select * from student", null)
            cs.moveToFirst()

            do {
                val id = cs.getInt(0)
                val name = cs.getString(1)
                val mssv = cs.getString(2)
                students.add(StudentModel(id, name, mssv))
            } while (cs.moveToNext())

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }

        studentAdapter = StudentAdapter(students)

        val listView = binding.listViewStudent
        listView.adapter = studentAdapter

        registerForContextMenu(listView)

        return binding.root
    }



    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        requireActivity().menuInflater.inflate(R.menu.menu_context, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val pos = (item.menuInfo as AdapterContextMenuInfo).position

        //xac dinh xem action nao duoc chon
        when (item.itemId) {
            R.id.action_edit -> {
                val args = Bundle()
                args.putInt("id", students[pos].id)
                args.putString("name", students[pos].name)
                args.putString("mssv", students[pos].mssv)
                findNavController().navigate(R.id.action_listFragment_to_studentFragment, args)
            }
            R.id.action_delete -> {
                val deleteStd = students[pos]
                context?.let {
                    AlertDialog.Builder(it)
                        .setTitle("Are you sure to delete ?")
                        .setMessage(students[pos].name)
                        .setPositiveButton("YES"){ dialogInterface: DialogInterface, i: Int ->
                            students.removeAt(pos)
                            studentAdapter.notifyDataSetChanged()
                            val db = (activity as MainActivity).db
                            db.beginTransaction()
                            try {
                                var isUndo = false
                                view?.let { it1 ->
                                    Snackbar.make(it1.findViewById(R.id.list_view_student),"Student removed", Snackbar.LENGTH_LONG)
                                        .setAction("UNDO"){
                                            isUndo = true
                                            students.add(pos, deleteStd)
                                            studentAdapter.notifyDataSetChanged()
                                        }.addCallback(object : Snackbar.Callback() {
                                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                                if (!isUndo) {
                                                    //xoa sinh vien khoi database
                                                    db.execSQL("delete from student where id=${deleteStd.id}")
                                                }
                                            }
                                        }).show()
                                }
                                db.setTransactionSuccessful()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            } finally {
                                db.endTransaction()
                            }
                        }
                        .setNegativeButton("CANCEL",null)
                        .show()
                }
            }
        }
        return super.onContextItemSelected(item)
    }
}