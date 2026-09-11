package udb.edu.sv.dsm.aerogo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import udb.edu.sv.dsm.aerogo.databinding.ItemDestinoBinding
import java.io.File
import java.text.NumberFormat
import java.util.Locale

/**
 * Adaptador del catálogo de destinos. Recibe callbacks para editar y
 * eliminar en lugar de manejar la navegación/lógica directamente, para
 * mantenerlo enfocado solo en dibujar la lista (Single Responsibility).
 */
class DestinoAdapter(
    private val destinos: List<Destino>,
    private val onEditar: (Destino) -> Unit,
    private val onEliminar: (Destino) -> Unit
) : RecyclerView.Adapter<DestinoAdapter.DestinoViewHolder>() {

    inner class DestinoViewHolder(val binding: ItemDestinoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinoViewHolder {
        val binding = ItemDestinoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DestinoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DestinoViewHolder, position: Int) {
        val destino = destinos[position]
        val formatoPrecio = NumberFormat.getCurrencyInstance(Locale.US)

        holder.binding.textNombre.text = destino.nombre
        holder.binding.textPrecio.text = formatoPrecio.format(destino.precio)
        holder.binding.textDescripcion.text = destino.descripcion

        Glide.with(holder.binding.imageDestino.context)
            .load(File(destino.imagenPath))
            .placeholder(R.drawable.ic_placeholder_imagen)
            .error(R.drawable.ic_placeholder_imagen)
            .centerCrop()
            .into(holder.binding.imageDestino)

        holder.binding.buttonEditar.setOnClickListener { onEditar(destino) }
        holder.binding.buttonEliminar.setOnClickListener { onEliminar(destino) }
    }

    override fun getItemCount(): Int = destinos.size
}
