package com.daniel.organisation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.organisation.activities.MainActivity;
import com.daniel.organisation.activities.MisNotasActivity;
import com.daniel.organisation.activities.ModificarNotaActivity;
import com.daniel.organisation.model.NotaLocalStore;
import com.daniel.organisation.R;
import com.daniel.organisation.model.Nota;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Clase que controla el adaptador del RecyclerView de notas
 */
public class NotaListAdapter extends RecyclerView.Adapter<NotaListAdapter.MyViewHolder> {
    private final List<Nota> notasList; //La lista de notas que tiene el usuario
    NotaLocalStore notaLocalStore; //Donde voy a guardar la información de la sesión
    private List<Nota> listaOriginal;
    public NotaListAdapter(List<Nota> notasList){
        this.notasList = notasList;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(notasList);
    }

    /**
     * El onCreate del ViewHolder
     * @param parent
     * @param viewType
     * @return el ViewHolder
     */
    @NonNull
    @Override
    public NotaListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        notaLocalStore = new NotaLocalStore(view.getContext());
        return new MyViewHolder(view);
    }

    /**
     * Voy a manejar los textos y el cardView de cada nota
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull NotaListAdapter.MyViewHolder holder, int position) {
        holder.tvTituloNota.setText(notasList.get(position).getTitulo());
        holder.nota = notasList.get(position);
        holder.cvRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    notaLocalStore.storeUltimaNota(holder.nota);
                    System.out.println("CLICKANDO "+holder.nota.getTitulo());
                    holder.context.startActivity(new Intent(holder.context, ModificarNotaActivity.class));

            }
        });
    }

    /**
     *
     * @return el tamaño de la lista de notas
     */
    @Override
    public int getItemCount() {
        return notasList.size();
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
            notasList.clear();
            notasList.addAll(listaOriginal);
        }else{
            List<Nota> coleccion = listaOriginal.stream()
                    .filter(i -> i.getTitulo().toLowerCase().contains(buscar.toLowerCase()))
                    .collect(Collectors.toList());
            notasList.clear();
            notasList.addAll(coleccion);
        }
        notifyDataSetChanged();
    }

    /**
     * ViewHolder con el texto, cardview, nota de cada nota y el contexto.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public Context context;
        public Nota nota;
        public CardView cvRecycler;
        public TextView tvTituloNota;
        public MyViewHolder(View view){
            super(view);

            this.context=view.getContext();
            tvTituloNota = view.findViewById(R.id.tvTituloNota);
            cvRecycler = view.findViewById(R.id.cvRecyclerNota);
        }
    }
}
