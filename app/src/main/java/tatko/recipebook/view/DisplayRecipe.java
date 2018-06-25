package tatko.recipebook.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import tatko.recipebook.R;
import tatko.recipebook.database.DBHelper;

public class DisplayRecipe extends AppCompatActivity {
    int numb = 0;
    private DBHelper mydb;

    TextView recipeName;
    TextView ingredient;
    TextView instruction;
    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);

        recipeName = (TextView)findViewById(R.id.editTextRecipe);
        ingredient = (TextView)findViewById(R.id.editTextIngredient);
        instruction = (TextView)findViewById(R.id.editTextInstruction);

        mydb = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            int Value = extras.getInt("id");

            if(Value > 0){
                Cursor cursor = mydb.getData(Value);
                id_To_Update = Value;
                cursor.moveToFirst();

                String recipe = cursor.getString(cursor.getColumnIndex(DBHelper.RECIPES_COLUMN_RECIPENAME));
                String ingr = cursor.getString(cursor.getColumnIndex(DBHelper.RECIPES_COLUMN_INGREDIENT));
                String instruct = cursor.getString(cursor.getColumnIndex(DBHelper.RECIPES_COLUMN_INSTRUCTION));

                if (!cursor.isClosed()) {
                    cursor.close();
                }
                Button b = (Button)findViewById(R.id.button1);
                b.setVisibility(View.INVISIBLE);

                recipeName.setText(recipe);
                recipeName.setFocusable(false);
                recipeName.setClickable(false);

                ingredient.setText(ingr);
                ingredient.setFocusable(false);
                ingredient.setClickable(false);

                instruction.setText(instruct);
                instruction.setFocusable(false);
                instruction.setClickable(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Bundle extras = getIntent().getExtras();

        if (extras != null){
            int Value = extras.getInt("id");
            if (Value > 0) {
                getMenuInflater().inflate(R.menu.display_recipe, menu);
            } else {
                getMenuInflater().inflate(R.menu.main_menu, menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.Edit_Recipe:
            Button b = (Button)findViewById(R.id.button1);
            b.setVisibility(View.VISIBLE);

            recipeName.setEnabled(true);
            recipeName.setFocusableInTouchMode(true);
            recipeName.setClickable(true);

            ingredient.setEnabled(true);
            ingredient.setFocusableInTouchMode(true);
            ingredient.setClickable(true);

            instruction.setEnabled(true);
            instruction.setFocusableInTouchMode(true);
            instruction.setClickable(true);

            return true;
            case R.id.Delete_Recipe:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteRecipe)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteRecipe(id_To_Update);
                                Toast.makeText(getApplicationContext(), "Deleted successfully",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });

                AlertDialog d = builder.create();
                d.setTitle("Are you sure?");
                d.show();

                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0){
                if (mydb.updateRecipe(id_To_Update,
                        recipeName.getText().toString(),
                        ingredient.getText().toString(),
                        instruction.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (mydb.insertRecipe(
                        recipeName.getText().toString(),
                        ingredient.getText().toString(),
                        instruction.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "not Done", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
