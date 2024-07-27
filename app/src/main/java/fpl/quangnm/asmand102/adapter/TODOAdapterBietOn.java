package fpl.quangnm.asmand102.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpl.quangnm.asmand102.R;
import fpl.quangnm.asmand102.dao.ToDoDaoBietOn;
import fpl.quangnm.asmand102.model.TodoBietOn;

public class TODOAdapterBietOn extends RecyclerView.Adapter<TODOAdapterBietOn.ToDoViewHolder> {

    private List<TodoBietOn> todolist; // list
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onEditClick(int position);
        void onStatusChange(int position, boolean isDone);
    }

    public  void setOnItemClickListener(OnItemClickListener listenner){
        this.listener = listenner;
    }

    private Context context;
    private ToDoDaoBietOn dao;
        public TODOAdapterBietOn(List<TodoBietOn> todolist, Context context, ToDoDaoBietOn dao){  // Ham khoi tao
            this.todolist = todolist;
            this.context = context;
            this.dao = dao;
    }


    // Tao View
    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.demo21_item_view,parent,false);
        return new ToDoViewHolder(view,listener);
    }

    // Gan du lieu cho view
    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TodoBietOn currentTodo = todolist.get(position);  // lay ve vi tri hien tai
        holder.tvToDoName.setText(currentTodo.getTitle()); // dien du lieu vao truong title
        holder.checkBox.setChecked(currentTodo.getStatus()==1); // an vao =1 con de trong la 0
        holder.date_demo211.setText(currentTodo.getDate());  // lay ve vi tri hien tai
        // xu ly su kien check box
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (listener != null) {
                    listener.onStatusChange(position, isChecked);
                }
            }
        });
        // mo ham xoa
        holder.btnDELETE.setOnClickListener(view -> {
            showDeleteComfirmDialog(holder.getAdapterPosition());
        });
        // mo ham sua
        holder.btnEdit.setOnClickListener(view -> {
            ShowEditDiaLog(holder.getAdapterPosition());
        });
    }
    private void ShowEditDiaLog(int pos){
            TodoBietOn todo = todolist.get(pos);
            View diaLogView = LayoutInflater.from(context)
                    .inflate(R.layout.item_dialog_demo31,null);
        EditText txtTiT = diaLogView.findViewById(R.id.demo33_edtlogin_form);
        EditText txtContent = diaLogView.findViewById(R.id.demo33_pass_edt);
        EditText txtDate = diaLogView.findViewById(R.id.demo33_pass2_edt);
        EditText txtType = diaLogView.findViewById(R.id.demo33_pass1_edt);
        Button btnDelete = diaLogView.findViewById(R.id.demo33_btnDelete);
        Button btnUpdate = diaLogView.findViewById(R.id.demo33_btnUpdate);

        // setText
        txtTiT.setText(todo.getTitle());
        txtContent.setText(todo.getContent());
        txtType.setText(todo.getType());
        txtDate.setText(todo.getDate());

        // Tao dialog
        AlertDialog dialog = new AlertDialog.Builder(context).setView(diaLogView).create();

        // xu ly su Kien
        btnUpdate.setOnClickListener(view -> {
            String newTitle = txtTiT.getText().toString();
            String newContent = txtContent.getText().toString();
            String newType = txtType.getText().toString();
            String newDate = txtDate.getText().toString();

            if (!newTitle.isEmpty() && !newContent.isEmpty() && !newDate.isEmpty()  && !newType.isEmpty()){
                todo.setTitle(newTitle);
                todo.setContent(newContent);
                todo.setDate(newDate);
                todo.setType(newType);
                dao.updataToDO(todo);
                Toast.makeText(context, "Sua thanh cong", Toast.LENGTH_SHORT).show();
                notifyItemChanged(pos);
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Can nhap du thong tin", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();    // hien thi dialog
    }




    // Lay tong so item
    @Override
    public int getItemCount() {
        return todolist.size();
    }
    // Dialog cho ham xoa
    private void showDeleteComfirmDialog(int pos){
        new AlertDialog.Builder(context).setTitle("Xac nhan xoa")
                .setMessage("Muon xoa khong?")
                .setPositiveButton("OK",(dialogInterface, i) -> deleteTodoItem(pos))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteTodoItem(int pos){
        TodoBietOn todo = todolist.get(pos);
        dao.deleteTodo(todo.getId());
        todolist.remove(pos);
        notifyItemRemoved(pos);
        notifyItemChanged(pos, todolist.size());
    }



    class  ToDoViewHolder extends RecyclerView.ViewHolder{
        TextView tvToDoName,date_demo211;
        CheckBox checkBox;
        Button btnEdit, btnDELETE;


        public ToDoViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            // anh xa
            tvToDoName = itemView.findViewById(R.id.demo21_tvName);
            date_demo211 = itemView.findViewById(R.id.date_demo211);
            btnEdit = itemView.findViewById(R.id.demo21_btnEDIT);
            btnDELETE = itemView.findViewById(R.id.demo21_btnDELETE);
            checkBox = itemView.findViewById(R.id.demo21_item_CheckBox);

            btnDELETE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){  // lay ve vi tri xoa
                            //listener.onDeleteClick(pos);
                        }
                    }
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                           // listener.onEditClick(pos);
                        }
                    }
                }
            });
        }


    }
}
