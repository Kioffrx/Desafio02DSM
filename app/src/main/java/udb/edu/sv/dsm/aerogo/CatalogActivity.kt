package udb.edu.sv.dsm.aerogo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import udb.edu.sv.dsm.aerogo.databinding.ActivityCatalogBinding

class CatalogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    private val destinos = mutableListOf<Destino>()
    private lateinit var adapter: DestinoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        adapter = DestinoAdapter(
            destinos,
            onEditar = { destino -> abrirFormulario(destino) },
            onEliminar = { destino -> confirmarEliminar(destino) }
        )

        binding.recyclerCatalogo.layoutManager = LinearLayoutManager(this)
        binding.recyclerCatalogo.adapter = adapter

        binding.fabAgregar.setOnClickListener { abrirFormulario(null) }

        binding.buttonCerrarSesion.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // Se recarga cada vez que se vuelve al catálogo (por ejemplo, tras
        // crear/editar un destino) para reflejar los cambios sin recibir
        // resultados manuales de las otras actividades.
        cargarDestinos()
    }

    private fun cargarDestinos() {
        db.collection("destinos")
            .get()
            .addOnSuccessListener { resultado ->
                destinos.clear()
                for (documento in resultado) {
                    val destino = documento.toObject(Destino::class.java)
                    destino.id = documento.id
                    destinos.add(destino)
                }
                adapter.notifyDataSetChanged()
                binding.textVacio.visibility =
                    if (destinos.isEmpty()) View.VISIBLE else View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.error_load_destinations), Toast.LENGTH_SHORT).show()
            }
    }

    private fun abrirFormulario(destino: Destino?) {
        val intent = Intent(this, DestinoFormActivity::class.java)
        if (destino != null) {
            intent.putExtra(DestinoFormActivity.EXTRA_DESTINO_ID, destino.id)
        }
        startActivity(intent)
    }

    private fun confirmarEliminar(destino: Destino) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_confirm_title))
            .setMessage(getString(R.string.delete_confirm_message))
            .setPositiveButton(getString(R.string.delete_confirm_yes)) { _, _ -> eliminarDestino(destino) }
            .setNegativeButton(getString(R.string.delete_confirm_no), null)
            .show()
    }

    private fun eliminarDestino(destino: Destino) {
        db.collection("destinos").document(destino.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.destination_deleted_success), Toast.LENGTH_SHORT).show()
                cargarDestinos()
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.error_delete_destination), Toast.LENGTH_SHORT).show()
            }
    }
}
