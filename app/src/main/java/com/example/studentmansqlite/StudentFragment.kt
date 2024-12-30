package com.example.studentmansqlite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.studentmansqlite.databinding.FragmentStudentBinding


class StudentFragment : Fragment() {

    lateinit var binding: FragmentStudentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentBinding.inflate(layoutInflater)

        val editName = binding.editName
        val editMssv = binding.editMssv

        //lay data tu arguments
        var id = arguments?.getInt("id", -1)
        val name = arguments?.getString("name")
        val mssv = arguments?.getString("mssv")

        if (id != -1) {
            //chinh sua thong tin sinh vien
            editName.setText(name)
            editMssv.setText(mssv)
        }

        binding.btnSave.setOnClickListener({
            if (editName.text.isNotEmpty() && editMssv.text.isNotEmpty()) {
                val name = editName.text.toString()
                val mssv = editMssv.text.toString()

                //luu du lieu vao db
                val db = (activity as MainActivity).db
                if (id == -1) {
                    //them moi sinh vien
                    db.beginTransaction()
                    try {
                        db.execSQL("insert into student(name, mssv) values ('$name', '$mssv')")
                        db.setTransactionSuccessful()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        db.endTransaction()
                    }
                } else {
                    //thay doi thong tin sinh vien
                    db.beginTransaction()
                    try {
                        db.execSQL("update student set name='$name', mssv='$mssv' where id=$id")
                        db.setTransactionSuccessful()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        db.endTransaction()
                    }
                }


                findNavController().navigate(R.id.action_studentFragment_to_listFragment)
            }
        })

        binding.btnCancel.setOnClickListener({
            findNavController().navigate(R.id.action_listFragment_to_studentFragment)
        })

        return binding.root
    }


}