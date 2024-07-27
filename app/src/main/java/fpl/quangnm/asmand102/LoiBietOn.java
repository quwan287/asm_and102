package fpl.quangnm.asmand102;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpl.quangnm.asmand102.adapter.TODOAdapterBietOn;
import fpl.quangnm.asmand102.dao.ToDoDaoBietOn;
import fpl.quangnm.asmand102.model.TodoBietOn;

public class LoiBietOn extends AppCompatActivity {
    private EditText txtTitle, txtContent, txtDates, txtType;
    private Button btnAdd;
    private RecyclerView recyclerView;
    private TODOAdapterBietOn adapter;
    private ToDoDaoBietOn dao;
    private List<TodoBietOn> todoList;
    private TodoBietOn currenEdittingTodo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        txtTitle = findViewById(R.id.demo2TxtTitle);
        txtContent = findViewById(R.id.demo2TxtContent);
        txtDates = findViewById(R.id.demo2TxtDate);
        txtType = findViewById(R.id.demo2TxtType);
        txtType.setOnClickListener(view -> {
            String[] loaiCV = {"Loai 1", "loai 2", "loai 3"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Con Type");
            builder.setItems(loaiCV, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txtType.setText(loaiCV[i]);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        btnAdd = findViewById(R.id.demo2_btnADD);
        recyclerView = findViewById(R.id.demo2_RecyclerView);

        dao = new ToDoDaoBietOn(this);
        todoList = dao.getAllTodos();
        adapter = new TODOAdapterBietOn(todoList,this,dao);
        recyclerView.setLayoutManager(new LinearLayoutManager(LoiBietOn.this));
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currenEdittingTodo == null) {
                    addToDo();
                }
                else {
                    updateTodo();
                }
            }
        });
        adapter.setOnItemClickListener(new TODOAdapterBietOn.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                deleteTodo(position);
            }

            @Override
            public void onEditClick(int position) {
                editTodo(position);
            }

            @Override
            public void onStatusChange(int position, boolean isDone) {
                updateTodoStatus(position, isDone);
            }
        });
    }
    private void updateTodoStatus(int pos, boolean isDone){
        TodoBietOn todo= todoList.get(pos);
        todo.setStatus(isDone?1:0);
        dao.updateToDOStatus(todo.getId(), todo.getStatus());
//        adapter.notifyItemChanged(pos);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemChanged(pos);
            }
        });
        Toast.makeText(this, "Update thành công", Toast.LENGTH_SHORT).show();
    }
    private void addToDo(){
        String title = txtTitle.getText().toString();
        String content = txtContent.getText().toString();
        String date = txtDates.getText().toString();
        String type = txtType.getText().toString();
        TodoBietOn todo = new TodoBietOn(0,title, content, date, type, 0);
        dao.addToDo(todo);
        todoList.add(0,todo);
        adapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);
        clearFields();
    }
    private void updateTodo(){
        String title = txtTitle.getText().toString();
        String content = txtContent.getText().toString();
        String date = txtDates.getText().toString();
        String type = txtType.getText().toString();
        currenEdittingTodo.setTitle(title);
        currenEdittingTodo.setContent(content);
        currenEdittingTodo.setDate(date);
        currenEdittingTodo.setType(type);
        dao.updataToDO(currenEdittingTodo);

        int pos = todoList.indexOf(currenEdittingTodo);
        adapter.notifyItemChanged(pos);
        currenEdittingTodo = null;
        btnAdd.setText("Add");
        clearFields();
    }
    private void editTodo(int pos){
        currenEdittingTodo = todoList.get(pos);
        txtTitle.setText(currenEdittingTodo.getTitle());
        txtContent.setText(currenEdittingTodo.getContent());
        txtDates.setText(currenEdittingTodo.getDate());
        txtType.setText(currenEdittingTodo.getType());
        btnAdd.setText("Update");
    }
    private void deleteTodo(int pos){
        TodoBietOn todo = todoList.get(pos);
        dao.deleteTodo(todo.getId());
        todoList.remove(pos);
        adapter.notifyItemRemoved(pos);
    }
    private void clearFields(){
        txtTitle.setText("");
        txtContent.setText("");
        txtDates.setText("");
        txtType.setText("");
    }
}