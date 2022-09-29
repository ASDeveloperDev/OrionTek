package com.example.oriontek

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
/**
OrionTek.
 *created by ASDeveloper on 27/09/2022 at 07:42.
 */

class AddClientActivity : AppCompatActivity() {

    //actionBar
    private var actionBar: androidx.appcompat.app.ActionBar? = null

    //db Helper
    lateinit var dbHelper:MyDbHelper

    //variables para guardar datos en DB
    private var id:String? = ""
    private var cliente:String? = ""
    private var telefono:String? = ""
    private var fechaNac:String? = ""
    private var email:String? = ""
    private var direccion:String? = ""
    private var idDireccion:String? = ""
    private var notas:String? = ""
    private var fechaActualizada:String? = ""
    private var fechaAgregada:String? = ""

    private var IsEditMode = false


    lateinit var tvId : TextView
    lateinit var EdCliente : TextInputEditText
    lateinit var EdTelefono : TextInputEditText
    lateinit var EdDireccion : TextInputEditText
    lateinit var EdFechaNac : TextInputEditText
    lateinit var EdEmail : TextInputEditText
    lateinit var EdNotas : TextInputEditText
    lateinit var tvFechaUpdate : TextView
    lateinit var tvFechaAdd : TextView

    lateinit var BtAddCliente : FloatingActionButton


    //Grupo de Chip, Direcciones
    lateinit var chipGroupDir : ChipGroup
    lateinit var chipAllDir : Chip

    // lateinit var BtAddDirChip : Button

    var listDir : MutableList<String> = mutableListOf()
    var listAlmDirDialog : MutableList<String> = mutableListOf()
    var listDirAlm : MutableList<String> = mutableListOf()
    var listDirDialog : MutableList<String> = mutableListOf()
    var lvDirDialog: ListView? = null
    var arrayAdapterDialog: ArrayAdapter<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_client)

        //inicializar actionBar
        actionBar = supportActionBar
        //titulo de actionBar
        // actionBar!!.title = "Agregar Clientes"
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        //Inicializar db helper
        dbHelper = MyDbHelper(this)

        //Inicializar vistas
        tvId = findViewById(R.id.tvIdAddC)
        EdCliente = findViewById(R.id.EdClientAddC)
        EdTelefono = findViewById(R.id.EdTelefonoAddC)
        EdFechaNac = findViewById(R.id.EdFechaNacAddC)
        EdEmail = findViewById(R.id.EdEmailAddC)
        EdNotas = findViewById(R.id.EdNotaAddC)
        tvFechaUpdate = findViewById(R.id.tvFechaUpdate)
        tvFechaAdd = findViewById(R.id.tvFechaAdd)
        EdDireccion = findViewById(R.id.EdDirAddC)

        chipGroupDir = findViewById(R.id.chipGroupDirAddC)
        chipAllDir = findViewById(R.id.chipAllAddC)
        //BtAddDirChip = findViewById(R.id.BtAddDirChip)


        BtAddCliente =findViewById(R.id.BtAddClientAddC)

        val intent = intent.extras
        IsEditMode = intent?.getBoolean("IsEditMode", false)!!

        if (IsEditMode){
            //Editar datos
            actionBar!!.title = "Editar cliente"

            id = intent.getString("id")
            cliente = intent.getString("cliente")
            telefono = intent.getString("telefono")
            fechaNac = intent.getString("fechaNac")
            // direccion = intent.getString("direccion")
            idDireccion = intent.getString("idDireccion")
            email = intent.getString("email")
            notas = intent.getString("notas")
            fechaActualizada = intent.getString("fechaActualizada")
            //fechaAgregada = intent.getString("fechaAgregada")

            //Llenar TextView con datos
            tvId.setText(id)
            EdCliente.setText(cliente)
            EdTelefono.setText(telefono)
            EdFechaNac.setText(fechaNac)
            // EdDireccion.setText(direccion)
            EdEmail.setText(email)
            EdNotas.setText(notas)
            tvFechaUpdate.setText(fechaActualizada)
            //  tvFecha.setText(FechaHora())



            //obtener lista de Direcciones
            //llenar arreglo desde dbHelper
            for (i in dbHelper.getDirPorId(tvId.text.toString()).indices){

                if (listDir.any { it == dbHelper.getDirPorId(tvId.text.toString())[i] })
                else listDir.add(dbHelper.getDirPorId(tvId.text.toString())[i])
            }
        }
        else{
            //Agregar datos
            actionBar!!.title = "Nuevo cliente"

            //idCliente = id de tbClientes
            var idCliente = dbHelper.TotalClientes()
            if ( idCliente == 0){
                idCliente = 1
                tvId.setText("1")
            }
            else {
                tvId.setText("${ dbHelper.TotalClientes() + 1}")
                idCliente = dbHelper.TotalClientes() + 1

            }
        }

        FechaNacDatePicker() //Al hacer click en EdFechaNac

        //Add data o update data, del cliente
        BtAddCliente.setOnClickListener { InputData() }


        EdDireccion.setOnClickListener { listDir() }


        // ChipGroupDir()

    }

    private fun listDir(){

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.list_dir_add_client, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)  //Mostrar detalle de Dialog
        //   .setTitle(getString(R.string.Ajustes))
        //   .setIcon(R.drawable.ic_settings)
        //    .setIcon(R.drawable.ic_ajustes_black)
        //     .setTitle(Html.fromHtml("<font color='#29AB87'> ${getString(R.string.app_name)} </font>")) //Color de Titulo
        //  mBuilder.setMessage(Html.fromHtml("<font color=''>This is a test</font>")) //Color de Mensaje

        val mAlertDialog = mBuilder.show()

        //Inicializar db helper
        val dbHelperDialog : MyDbHelper = MyDbHelper(this)
        this.dbHelper = dbHelperDialog
        dbHelper.getDirPorId(tvId.text.toString())


        val lvDir = mDialogView.findViewById<View>(R.id.lvDirDialog) as ListView
        val EdDirAddCDialog = mDialogView.findViewById<TextInputEditText>(R.id.EdDirAddCDialog)
        val BtAddDir = mDialogView.findViewById<Button>(R.id.BtAddDirChip)

        this.lvDirDialog = lvDir

        //listDirDialog.clear()


        for (i in dbHelper.getAllDirecciones().indices)
            listDirDialog.add( dbHelper.getAllDirecciones()[i])

//        Toast("$listDirDialog")


        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listDirDialog)
        this.arrayAdapterDialog = adapter

        lvDirDialog!!.adapter = arrayAdapterDialog
        notifyList()
        lvDirDialog!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            if (IsEditMode){ //Modo Ediccion.................


                if (listDir.any { it == listDirDialog[position] })
                else {
                    listDir.clear()
                    listDir.add(listDirDialog[position])
                }

                 //listAlmDirDialog.clear()
                if (listAlmDirDialog.any { it == listDirDialog[position] } )
                else {
                    listDirAlm.add(listDirDialog[position])  //para almacenar dir
                    listAlmDirDialog.add(listDirDialog[position]) //para chip
                }

                EdDireccion.setText(listDirDialog[position])
                this.onResume()

                listAlmDirDialog.clear() //limpiar arreglo, insertar un solo elemnto
                //listDirAlm.clear() //limpiar arreglo
                mAlertDialog.dismiss()  //cerrar dialog



            }else{ //Modo Nuevo Cliente................................


                //listAlmDirDialog.clear()
                if (listAlmDirDialog.any { it == listDirDialog[position] } )
                else {
                    listDirAlm.add(listDirDialog[position])  //para almacenar dir
                    listAlmDirDialog.add(listDirDialog[position]) //para chip
                }


                EdDireccion.setText(listDirDialog[position])
                this.onResume()

                listAlmDirDialog.clear() //limpiar arreglo, insertar un solo elemnto
                //listDirAlm.clear() //limpiar arreglo
                mAlertDialog.dismiss()  //cerrar dialog
            }


        }

        //Bt Add Direccion
        BtAddDir.setOnClickListener {

            if (EdDirAddCDialog.text!!.isNotEmpty()){

                //Agregar direccion sin datos de cliente,
                    // para tener Direcciones independientes del client...
                //Add Direccion
                val id = dbHelper.InsertDirecciones(
                    "",//+ EdCliente.text.toString().trim(),
                    ""+ EdDirAddCDialog.text.toString().trim(),
                    "",//+ tvId.text.toString().trim(),
                    ""+ FechaHora().trim(),
                    ""+  FechaHora().trim()
                )
                Toast("Agregado correctamente!")
                EdDirAddCDialog.setText("")

                notifyList()
            }






        }
    }

    private fun notifyList() {

        listDirDialog.clear()

//        //llenar array
        for (i in dbHelper.getAllDirecciones().indices)
            listDirDialog.add( dbHelper.getAllDirecciones()[i])

        //Notificar cambio en Adapter
        arrayAdapterDialog?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()

        if (IsEditMode)
            ChipGroupDirEdit()
        else
            ChipGroupDirAddd()

    }


    private fun ChipGroupDirEdit(){

        chipGroupDir.visibility = View.GONE
        chipGroupDir.visibility = View.VISIBLE

        //llenar arreglo desde lista Dialog
        for (i in listAlmDirDialog.indices){

            if (listDir.any { it == listAlmDirDialog[i] })
            else listDir.add(listAlmDirDialog[i])
        }

        //variables para aumentar id chip
        val idCount : MutableList<Int?> = mutableListOf()
        var count = 0
        val ChipFun = object {

            @SuppressLint("ResourceType")
            fun AddChip(text: String?){

                val chip = Chip(this@AddClientActivity)
                //chip.id = View.generateViewId()
                chip.id = count++ ///toma el valor de 1,2,3....
                idCount.add(chip.id)
                chip.text = text
                chip.isCheckable = true


                //icon de location
                chip.setChipIconResource(R.drawable.ic_location)

                chip.isCloseIconVisible = true
                chip.setOnCloseIconClickListener{

                    //Al hacer click en un  x de chip
                    chipGroupDir.removeView(chip)

                    // chip.id                    //0
                    // Toast(listDir[chip.id])   //Nagua

                    //Toast("${dbHelper.getIdDireccion(listDir[chip.id])}")
                    //dbHelper.getIdDireccion(listDir[chip.id], EdCliente.text.toString())
                    dbHelper.DeleteDireccionPorId(dbHelper.getIdDireccion(listDir[chip.id], EdCliente.text.toString()))



                    //remover de la lista ese elemnto
                    //
                    // listDir.removeAll { it == listDir[chip.id] }
                }


                chipGroupDir.addView(chip)

                //Al hacer click en un  chip
                chip.setOnClickListener {

                }
            }

        }

        // Toast("$listDir")

        for (i in listDir.indices){

            ChipFun.AddChip(listDir[i])
        }


    }

    private fun ChipGroupDirAddd(){

        chipGroupDir.visibility = View.GONE
        chipGroupDir.visibility = View.VISIBLE

        //obtener lista de Direcciones

        listDir.clear()

        //llenar arreglo desde lista Dialog
        for (i in listAlmDirDialog.indices){

            if (listDir.any { it == listAlmDirDialog[i] })
            else listDir.add(listAlmDirDialog[i])
        }

        /*  for (i in dbHelper.getAllDirecciones1().indices){
              listDir.add(dbHelper.getAllDirecciones1()[i])
          }*/


        //variables para aumentar id chip
        val idCount : MutableList<Int?> = mutableListOf()
        var count = 0
        val ChipFun = object {

            @SuppressLint("ResourceType")
            fun AddChip(text: String?){

                val chip = Chip(this@AddClientActivity)
                //chip.id = View.generateViewId()
                chip.id = count++ ///toma el valor de 1,2,3....
                idCount.add(chip.id)
                chip.text = text
                chip.isCheckable = true

                //icon de location
                chip.setChipIconResource(R.drawable.ic_location)

                chip.isCloseIconVisible = true
                chip.setOnCloseIconClickListener{

                    //Al hacer click en un  x de chip
                    chipGroupDir.removeView(chip)

                    // Toast(listDir[chip.id])

                    //remover de la lista ese elemnto
                    //
                    // listDir.removeAll { it == listDir[chip.id] }
                }

                chipGroupDir.addView(chip)

                chip.setOnClickListener {  }
            }

        }

        // Toast("$listDir")


        for (i in listDir.indices)
            ChipFun.AddChip(listDir[i])


    }

    private fun FechaNacDatePicker(){

        val mcurrentTime = Calendar.getInstance()
        val year = mcurrentTime.get(Calendar.YEAR)
        val month = mcurrentTime.get(Calendar.MONTH)
        val day = mcurrentTime.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { view, year, month, dayOfMonth ->

            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy") //Formato de Fecha


            //date
            val date = String.format("%d/%d/%d", dayOfMonth, month + 1, year) // 21/1/2022

            val sdf = simpleDateFormat  //Formato -> 21/01/2022

            val dateConvert = sdf.parse(date)  //String a Date      21/1/2022 (a) 21/01/2022

            val dateFormat = sdf.format(dateConvert!!)  // 21/01/2022

            //Colocar fecha a tvDate
            EdFechaNac.setText(dateFormat)


        }, year, month, day)
        EdFechaNac.setOnClickListener { datePicker.show() }
    }


    //Entrada de datos
    private fun InputData() {


        if (IsEditMode){ //En modo Edicion
            //si se actualizara, true
            val id = dbHelper.UpdateClientes(
                ""+ tvId.text.toString().trim(),
                ""+ EdCliente.text.toString().trim(),
                ""+ EdTelefono.text.toString().trim(),
                ""+ EdFechaNac.text.toString().trim(),
               // ""+ idDireccion.toString().trim(),
                ""+ EdEmail.text.toString().trim(),
                ""+ EdNotas.text.toString().trim(),
                ""+ FechaHora(),
            )


            //Add Direccion
            for (i in listDirAlm.indices){

                //Toast("${listAlmDirDialog[i]}"

                val id = dbHelper.InsertDirecciones(
                    ""+ EdCliente.text.toString().trim(),
                    ""+ listDirAlm[i].trim(),
                    ""+ tvId.text.toString().trim(),
                    ""+ FechaHora().trim(),
                    ""+  FechaHora().trim()
                )
            }
            Toast("Agregado correctamente!")
            listDirAlm.clear()
            listAlmDirDialog.clear()
            onBackPressed()
        }
        else{
            //Se agregara, false

            var idDir = dbHelper.TotalClientes()
            if (idDir == 0)
                idDir += 1


            //Add Clinete
            val id = dbHelper.InsertClientes(
                ""+ EdCliente.text.toString().trim(),
                ""+ EdTelefono.text.toString().trim(),
                ""+ EdFechaNac.text.toString().trim(),
                ""+ idDir.toString().trim(),  //obtener el idCliente de direcciones
                ""+ EdEmail.text.toString().trim(),
                ""+ EdNotas.text.toString().trim(),
                ""+ FechaHora(),
                ""+ FechaHora()
            )

            //Add Direccion
            for (i in listDirAlm.indices){

                //Toast("${listAlmDirDialog[i]}"

                val id = dbHelper.InsertDirecciones(
                    ""+ EdCliente.text.toString().trim(),
                    ""+ listDirAlm[i].trim(),
                    ""+ tvId.text.toString().trim(),
                    ""+ FechaHora().trim(),
                    ""+  FechaHora().trim()
                )
            }
            Toast("Agregado correctamente!")
            listDirAlm.clear()
            listAlmDirDialog.clear()
            // onBackPressed()

            startActivity(Intent(this, MainActivity::class.java))

        }
    }

    fun FechaHora() : String{
        //Guardar Hora y fecha
        //Otener Tiempo
        val calendar = Calendar.getInstance()
        val mdFormat = SimpleDateFormat("hh:mm aaa")
        val strTime = mdFormat.format(calendar.time)

        //Obtener Fecha   EE dd MMM yyyy
        val sdf = SimpleDateFormat("EE dd MMM")
        val strDate = sdf.format(calendar.time)

        val fechaHora = "$strDate   $strTime"

        return fechaHora
    }

    private fun Toast(message : String) = android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()

    //Funcion presionar boton atras ActionBar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()

    }

}