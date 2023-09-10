package com.daniel.organisation.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.organisation.R;
import com.daniel.organisation.activities.MainActivity;
import com.daniel.organisation.activities.MisNotasActivity;
import com.daniel.organisation.activities.MisTareasActivity;
import com.daniel.organisation.activities.ModificarNotaActivity;
import com.daniel.organisation.activities.ModificarTareaActivity;
import com.daniel.organisation.model.Nota;
import com.daniel.organisation.model.NotaLocalStore;
import com.daniel.organisation.model.Tarea;
import com.daniel.organisation.model.TareaLocalStore;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Clase que controla el adaptador del RecyclerView de Tareas
 */
public class TareaListAdapter  extends RecyclerView.Adapter<TareaListAdapter.MyViewHolder>{
    private final List<Tarea> listaTareas;
    private List<Tarea> listaOriginal;
    private TareaLocalStore tareaLocalStore;
    Context c;

    /**
     * Constructor que va a recibir la lista de tareas y
     * el contexto.
     * @param listaTareas
     * @param c
     */
    public TareaListAdapter(List<Tarea> listaTareas, Context c){
        this.listaTareas = listaTareas;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(this.listaTareas);
        this.c=c;
    }

    /**
     * El onCreate del ViewHolder
     * @param parent
     * @param viewType
     * @return el ViewHolder
     */
    @NonNull
    @Override
    public TareaListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarea_row, parent, false);
        tareaLocalStore = new TareaLocalStore(view.getContext());
        return new MyViewHolder(view);
    }

    /**
     * Para cada elemento del recyclerView
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull TareaListAdapter.MyViewHolder holder, int position) {
        holder.tvTituloTarea.setText(listaTareas.get(position).getTitulo());
        LocalDate ld = listaTareas.get(position).getFechaLimite()
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int day = ld.getDayOfMonth();
        int month = ld.getMonthValue();
        int year = ld.getYear();
        //Esto lo hago porque el mes sino me lo pone como si fuese el mes anterior
        if(c.getClass()== MisTareasActivity.class){
            month++;
        }
        //Establezco la fecha a la tarjeta del recyclerView
        String selectedDate = day + "/" + month + "/" + year;
        holder.tvFechaTarea.setText(selectedDate);
        holder.tarea = listaTareas.get(position);
        holder.cvRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    tareaLocalStore.storeUltimaTarea(holder.tarea);
                    holder.context.startActivity(new Intent(holder.context, ModificarTareaActivity.class));
            }
        });

    }
    /**
     * Filtra la lista original y guarda en la lista que
     * se le va a pasar al recyclerView la lista filtrada
     * por el título.
     * @param buscar
     */
    public void filtrado(String buscar){
        int longitud = buscar.length();
        if(longitud==0){
            listaTareas.clear();
            listaTareas.addAll(listaOriginal);
        }else{
            List<Tarea> coleccion = listaOriginal.stream()
                    .filter(i -> i.getTitulo().toLowerCase().contains(buscar.toLowerCase()))
                    .collect(Collectors.toList());
            listaTareas.clear();
            listaTareas.addAll(coleccion);
        }
        notifyDataSetChanged();
    }

    /**
     *
     * @return el numeor de elementos de la lista
     */
    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    /**
     * Clase para manejar el viewHolder del recyclerView
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public Context context;
        public Tarea tarea;
        public CardView cvRecycler;
        public TextView tvTituloTarea, tvFechaTarea;
        public MyViewHolder(View view){
            super(view);

            this.context=view.getContext();
            tvTituloTarea = view.findViewById(R.id.tvTituloTarea);
            tvFechaTarea = view.findViewById(R.id.tvFechaTarea);
            cvRecycler = view.findViewById(R.id.cvRecyclerTarea);
        }
    }
}
