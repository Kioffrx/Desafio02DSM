package udb.edu.sv.dsm.aerogo

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import udb.edu.sv.dsm.aerogo.databinding.ActivityDestinoFormBinding
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * Se usa tanto para Crear (Create) como para Editar (Update) un destino:
 * si llega EXTRA_DESTINO_ID en el intent, se precarga el documento y se
 * actualiza al guardar; si no llega, se crea un documento nuevo.
 */
class DestinoFormActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DESTINO_ID = "extra_destino_id"
    }

    private lateinit var binding: ActivityDestinoFormBinding
    private val db = FirebaseFirestore.getInstance()

    private var destinoId: String? = null
    private var imagenSeleccionadaUri: Uri? = null
    private var imagenActualPath: String? = null

    private val selectorImagen =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imagenSeleccionadaUri = uri
                Glide.with(this).load(uri).centerCrop().into(binding.imagePreview)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDestinoFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val paises = resources.getStringArray(R.array.paises)
        binding.spinnerPais.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item, paises
        )

        destinoId = intent.getStringExtra(EXTRA_DESTINO_ID)
        if (destinoId != null) {
            binding.textTitulo.text = getString(R.string.destination_form_title_edit)
            cargarDestino(destinoId!!)
        } else {
            binding.textTitulo.text = getString(R.string.destination_form_title_new)
        }

        binding.buttonSeleccionarImagen.setOnClickListener {
            selectorImagen.launch("image/*")
        }

        binding.buttonGuardar.setOnClickListener { guardarDestino() }
        binding.buttonCancelar.setOnClickListener { finish() }
    }

    private fun cargarDestino(id: String) {
        db.collection("destinos").document(id).get()
            .addOnSuccessListener { documento ->
                val destino = documento.toObject(Destino::class.java) ?: return@addOnSuccessListener

                binding.editNombre.setText(destino.nombre)
                binding.editPrecio.setText(destino.precio.toString())
                binding.editDescripcion.setText(destino.descripcion)

                val paises = resources.getStringArray(R.array.paises)
                val indice = paises.indexOf(destino.pais)
                if (indice >= 0) binding.spinnerPais.setSelection(indice)

                imagenActualPath = destino.imagenPath
                if (destino.imagenPath.isNotEmpty()) {
                    Glide.with(this).load(File(destino.imagenPath)).centerCrop()
                        .into(binding.imagePreview)
                }
            }
            .addOnFailureListener {
                mostrarError(getString(R.string.error_save_destination))
            }
    }

    private fun guardarDestino() {
        val nombre = binding.editNombre.text.toString().trim()
        val pais = binding.spinnerPais.selectedItem?.toString() ?: ""
        val precioTexto = binding.editPrecio.text.toString().trim()
        val descripcion = binding.editDescripcion.text.toString().trim()

        // Validación: ningún campo obligatorio puede quedar vacío.
        if (nombre.isEmpty() || pais.isEmpty() || precioTexto.isEmpty() || descripcion.isEmpty()) {
            mostrarError(getString(R.string.error_required_field))
            return
        }

        // Validación: el precio debe ser numérico y mayor a 0.
        val precio = precioTexto.toDoubleOrNull()
        if (precio == null || precio <= 0) {
            mostrarError(getString(R.string.error_price_invalid))
            return
        }

        // Validación: descripción con al menos 20 caracteres.
        if (descripcion.length < 20) {
            mostrarError(getString(R.string.error_description_short))
            return
        }

        // Validación: se requiere una imagen (nueva o la que ya tenía si se edita).
        if (imagenSeleccionadaUri == null && imagenActualPath.isNullOrEmpty()) {
            mostrarError(getString(R.string.error_image_required))
            return
        }

        val rutaImagen = if (imagenSeleccionadaUri != null) {
            guardarImagenEnAlmacenamientoLocal(imagenSeleccionadaUri!!)
        } else {
            imagenActualPath!!
        }

        val destino = Destino(
            id = destinoId ?: "",
            nombre = nombre,
            pais = pais,
            precio = precio,
            descripcion = descripcion,
            imagenPath = rutaImagen
        )

        if (destinoId == null) {
            db.collection("destinos").add(destino)
                .addOnSuccessListener { finish() }
                .addOnFailureListener { mostrarError(getString(R.string.error_save_destination)) }
        } else {
            db.collection("destinos").document(destinoId!!).set(destino)
                .addOnSuccessListener { finish() }
                .addOnFailureListener { mostrarError(getString(R.string.error_save_destination)) }
        }
    }

    /**
     * Copia la imagen elegida en la galería al almacenamiento interno de la
     * app (Storage Local, según lo permitido en el enunciado) y devuelve la
     * ruta absoluta del archivo, que es lo que se guarda en Firestore.
     */
    private fun guardarImagenEnAlmacenamientoLocal(uri: Uri): String {
        val nombreArchivo = "destino_${UUID.randomUUID()}.jpg"
        val archivo = File(filesDir, nombreArchivo)
        contentResolver.openInputStream(uri)?.use { entrada ->
            FileOutputStream(archivo).use { salida ->
                entrada.copyTo(salida)
            }
        }
        return archivo.absolutePath
    }

    private fun mostrarError(mensaje: String) {
        binding.textError.text = mensaje
        binding.textError.visibility = View.VISIBLE
    }
}
