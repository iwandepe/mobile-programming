package com.iwan.ets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iwan.ets.databinding.FragmentContactListBinding


class ContactListFragment : Fragment(), View.OnClickListener {
    lateinit var contactAdapter: ContactListAdapater
    lateinit var contactList: List<ContactModel>
    lateinit var rvContact: RecyclerView

    lateinit var databaseHandler: DatabaseHandler

    var activeContact: ContactModel? = null


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentContactListBinding>(inflater,
                R.layout.fragment_contact_list, container, false)

        val btnAdd: AppCompatImageButton = binding.btnAdd
        val btnEdit: AppCompatImageButton = binding.btnEdit
        val btnDelete: AppCompatImageButton = binding.btnDelete
        val btnViewContact: AppCompatImageButton = binding.btnFind

        btnAdd.setOnClickListener(this)
        btnEdit.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
        btnViewContact.setOnClickListener(this)

        databaseHandler = DatabaseHandler(requireActivity())
        contactList = databaseHandler.viewContacts()

        contactAdapter = ContactListAdapater(contactList, R.layout.listview_contact_item)

        rvContact = binding.rvContact
        rvContact.adapter = contactAdapter;
        rvContact.layoutManager = LinearLayoutManager(requireContext())

        contactAdapter.onItemClick = { contact ->
            activeContact = contact
        }

        return binding.root
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnAdd -> {
                showAddDialog()
            }
            R.id.btnEdit -> {

            }
            R.id.btnDelete -> {

            }
            R.id.btnToContact -> {

            }
        }
    }

    private fun showAddDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.input_dialog)

        val etName = dialog.findViewById(R.id.etName) as EditText
        val etPhone = dialog.findViewById(R.id.etPhone) as EditText
        val databaseHandler = DatabaseHandler(requireContext())

        val btnPositive = dialog.findViewById(R.id.btnPositive) as Button
        val btnNegative = dialog.findViewById(R.id.btnNegative) as Button
        btnPositive.setOnClickListener {
            if(etName.text.toString().trim()!="" && etPhone.text.toString().trim()!=""){
                println(etName.text.toString())
                println(etPhone.text.toString())
                val status = databaseHandler.addContact(ContactModel(etName.text.toString(), etPhone.text.toString()))
                if(status > -1){
                    Toast.makeText(requireContext(), "Contact saved", Toast.LENGTH_SHORT).show()
                    contactList = contactList + listOf(ContactModel(etName.text.toString(), etPhone.text.toString()))
                    println(contactList.size)
                    contactAdapter.setData(contactList)
                    contactAdapter.notifyDataSetChanged()
                }
            }
            else{
                Toast.makeText(requireContext(), "Name and phone cannot be empty", Toast.LENGTH_LONG).show()
            }

            dialog.dismiss()
        }
        btnNegative.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

//    fun udpateContact(){
//        val dialogBuilder = AlertDialog.Builder(this)
//        val inflater = this.layoutInflater
//        val dialogView = inflater.inflate(R.layout.update_dialog, null)
//        dialogBuilder.setView(dialogView)
//
//        val edtId = dialogView.findViewById(R.id.updateId) as EditText
//        val edtName = dialogView.findViewById(R.id.updateName) as EditText
//        val edtEmail = dialogView.findViewById(R.id.updateEmail) as EditText
//
//        dialogBuilder.setTitle("Update Record")
//        dialogBuilder.setMessage("Enter data below")
//        dialogBuilder.setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->
//
//            val updateId = edtId.text.toString()
//            val updateName = edtName.text.toString()
//            val updateEmail = edtEmail.text.toString()
//            //creating the instance of DatabaseHandler class
//            val databaseHandler: DatabaseHandler= DatabaseHandler(this)
//            if(updateId.trim()!="" && updateName.trim()!="" && updateEmail.trim()!=""){
//                //calling the updateEmployee method of DatabaseHandler class to update record
//                val status = databaseHandler.updateEmployee(EmpModelClass(Integer.parseInt(updateId),updateName, updateEmail))
//                if(status > -1){
//                    Toast.makeText(applicationContext,"record update",Toast.LENGTH_LONG).show()
//                }
//            }else{
//                Toast.makeText(applicationContext,"id or name or email cannot be blank",Toast.LENGTH_LONG).show()
//            }
//
//        })
//        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
//            //pass
//        })
//        val b = dialogBuilder.create()
//        b.show()
//    }
//    //method for deleting records based on id
//    fun deleteRecord(view: View){
//        //creating AlertDialog for taking user id
//        val dialogBuilder = AlertDialog.Builder(this)
//        val inflater = this.layoutInflater
//        val dialogView = inflater.inflate(R.layout.delete_dialog, null)
//        dialogBuilder.setView(dialogView)
//
//        val dltId = dialogView.findViewById(R.id.deleteId) as EditText
//        dialogBuilder.setTitle("Delete Record")
//        dialogBuilder.setMessage("Enter id below")
//        dialogBuilder.setPositiveButton("Delete", DialogInterface.OnClickListener { _, _ ->
//
//            val deleteId = dltId.text.toString()
//            //creating the instance of DatabaseHandler class
//            val databaseHandler: DatabaseHandler= DatabaseHandler(this)
//            if(deleteId.trim()!=""){
//                //calling the deleteEmployee method of DatabaseHandler class to delete record
//                val status = databaseHandler.deleteEmployee(EmpModelClass(Integer.parseInt(deleteId),"",""))
//                if(status > -1){
//                    Toast.makeText(applicationContext,"record deleted",Toast.LENGTH_LONG).show()
//                }
//            }else{
//                Toast.makeText(applicationContext,"id or name or email cannot be blank",Toast.LENGTH_LONG).show()
//            }
//
//        })
//        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
//            //pass
//        })
//        val b = dialogBuilder.create()
//        b.show()
//    }
}