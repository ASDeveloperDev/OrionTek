package com.example.oriontek


import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList

/**
OrionTek.
 *created by ASDeveloper on 27/09/2022 at 07:42.
 */


class MainActivity : AppCompatActivity() {

    private  var actionBar: ActionBar? = null

    //db Helper
    lateinit var dbHelper:MyDbHelper

    //orden / query, por id Desc y Asc
    private var NEWEST_FIRST = "${Constantes.ID_C} DESC"
    private var OLDEST_FIRST = "${Constantes.ID_C} ASC"

    private var recentSortOrder = NEWEST_FIRST

    private var totalClient : Int? = null

    lateinit var adapterClientes : AdapterClientes
    lateinit var rvClientes : RecyclerView

    lateinit var BtFabAddClient : FloatingActionButton

    lateinit var chipGroupDir : ChipGroup
    lateinit var chipAll : Chip

    val listDirMain : MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Inicializar db helper
        dbHelper = MyDbHelper(this)

        //Obtener total de clientes en subtitulo de barra
        totalClient = dbHelper.TotalClientes()
        actionBar = supportActionBar
        actionBar!!.setTitle(getString(R.string.app_name))
        actionBar!!.subtitle = "OrionTek tienes $totalClient clientes"

        //Inicializar vistas
        rvClientes = findViewById(R.id.rvClientes)
        BtFabAddClient = findViewById(R.id.BtFABAddAddC)
        chipGroupDir = findViewById(R.id.chipGroupDirC)
        chipAll = findViewById(R.id.chipAllDirc)


        // CargarClientes(NEWEST_FIRST)  //Cargar clientes en Rv, orden = mas nuevo.

        BtFabAddClient.setOnClickListener {

            val intent = Intent(this, AddClientActivity::class.java)
            intent.putExtra("IsEditMode", false)  //Modo Editar cliente, false , no editar,
            startActivity(intent)
        }

        ///listDirMain.clear()
        ChipGroupDir()

    }

    public override fun onResume() {
        super.onResume()

        CargarClientes(recentSortOrder) //Mas nuevo default
        RefrescarActionBar()

        //ChipGroupDir()
    }

    private fun ChipGroupDir(){

        //obtener lista de Direcciones
        listDirMain.clear()
        for (i in dbHelper.getAllDirecciones().indices){
            listDirMain.add(dbHelper.getAllDirecciones()[i])
        }

       // Toast("${dbHelper.getAllDirecciones()}")
        chipAll.setOnClickListener { CargarClientes(recentSortOrder) }//Mas nuevo default

        //variables para aumentar id chip
        val idCount : MutableList< Int? > = mutableListOf()
        var count = 0
        val ChipFun = object {

            @SuppressLint("ResourceType")
            fun AddChip(text: String?){

                val chip = Chip(this@MainActivity)
                //chip.id = View.generateViewId()
                chip.id = count++ ///toma el valor de 1,2,3....
                idCount.add(chip.id)
                chip.text = text
                chip.isCheckable = true

                chip.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.teal_200))
                //chip.chipIcon = this@MainActivity.resources.getDrawable(R.drawable.ic_location)


                chipGroupDir.addView(chip)

                //Al hacer click en un  chip
                chip.setOnClickListener {

                    chipAll.isChecked = false  //chipAll

                   // Toast("${listDirMain[chip.id]}")
                    //Toast("${dbHelper.getClientPorDir(listDirMain[chip.id])}")
                   // Toast("${dbHelper.getAllClientesPorCliente(dbHelper.getIdCliente( listDirMain[chip.id]), recentSortOrder)}")

                  //  Toast("${dbHelper.getAllClientesPorCliente(dbHelper.getClientPorDir(listDirMain[chip.id]), recentSortOrder)}")


                    //Toast("${dbHelper.getAllClientesPorCliente(dbHelper.getClientPorDir(listDirMain[chip.id]), recentSortOrder)}")

                    adapterClientes = AdapterClientes(this@MainActivity, dbHelper.getAllClientesPorCliente(dbHelper.getClientPorDir(listDirMain[chip.id]), recentSortOrder))
                    rvClientes.adapter = adapterClientes

                }
            }

        }

        for (i in listDirMain.indices)
            ChipFun.AddChip(listDirMain[i])
    }


    private fun RefrescarActionBar(){

        totalClient = dbHelper.TotalClientes()  //Retorna cantidad de clientes
        actionBar = supportActionBar
        actionBar!!.setTitle(getString(R.string.app_name))
        actionBar!!.subtitle = "OrionTek tienes $totalClient clientes"

    }

    private fun CargarClientes(orden:String) {
        recentSortOrder = orden

        val adapterClientes = AdapterClientes(this, dbHelper.getAllClientes(orden))

        rvClientes.adapter = adapterClientes

    }

    private fun BuscarClientes(query:String){

        val adapterClientes = AdapterClientes(this, dbHelper.SearchClientes(query))

        rvClientes.adapter = adapterClientes
    }

    //para buscar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        //Inflar menu
        menuInflater.inflate(R.menu.menu_main, menu)

        //searchView
        val item = menu.findItem(R.id.BtSearch)
        val SearchView = item.actionView as SearchView

        SearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                //Buscar tu tipo
                if (newText != null) {
                    BuscarClientes(newText)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    BuscarClientes(query)
                }
                return true
            }
        }
        )
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when(id){
            R.id.BtAjustes -> {DialogoAjuste()}
            R.id.BtEliminarAll -> {
                // Dialogo para confirmar eliminado de todos los cliente
                AlertDialog.Builder(this)
                    //   .setTitle("${tvNomAppM.text}")
                    .setMessage("Desea eliminar todos los clientes? ")
                    .setIcon(R.drawable.ic_delete_all)
                    // .setTitle(Html.fromHtml("<font color='#5d4037'> ${tvNomAppM.text} </font>")) //Color de Titulo
                    //  mBuilder.setMessage(Html.fromHtml("<font color=''>This is a test</font>")) //Color de Mensaje

                    .setPositiveButton(android.R.string.ok,
                        DialogInterface.OnClickListener { dialog, which ->
                            //botón OK pulsado
                            dbHelper.DeleteAllClientes()
                            dbHelper.DeleteAllDirecciones()
                            recreate()
                            //Refrescar Lista clientes para cuando elimine todos los clientes
                            this.onResume()

                            //Refrescar ToolBar despues de Eliminar
                            RefrescarActionBar()
                        })
                    .setNegativeButton(android.R.string.cancel,
                        DialogInterface.OnClickListener { dialog, which ->
                            //botón cancel pulsado
                        })
                    .show()
            }
            R.id.BtSort -> OrdenarDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun OrdenarDialog() {
        //optiones
        val options = arrayOf("Ascendente", "Descendente")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Ordenar por")

            .setItems(options){ dialog, option ->
                if (option == 0){
                    //Nombre asc
                    //Mas viejo
                    CargarClientes(OLDEST_FIRST)
                }
                else if (option == 1){
                    //Mas Nuevo
                    CargarClientes(NEWEST_FIRST)
                }
            }
            .show()
    }

    private fun DialogoAjuste(){

        //Dialog Ajustes cliente
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.ajustes_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)  //Mostrar detalle de Dialog
        //   .setTitle(getString(R.string.Ajustes))
        //   .setIcon(R.drawable.ic_settings)
        //    .setIcon(R.drawable.ic_ajustes_black)
        //     .setTitle(Html.fromHtml("<font color='#29AB87'> ${getString(R.string.app_name)} </font>")) //Color de Titulo
        //  mBuilder.setMessage(Html.fromHtml("<font color=''>This is a test</font>")) //Color de Mensaje

        val mAlertDialog = mBuilder.show()

    }
    private fun Toast(message : String) = android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()

    //Salir App
    override fun onBackPressed() {
        super.onBackPressed()

        moveTaskToBack(true)

    }


}