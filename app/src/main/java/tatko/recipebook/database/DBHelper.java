package tatko.recipebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Recipe_db";
    public static final String RECIPES_TABLE_NAME = "recipes";
    public static final String RECIPES_COLUMN_ID = "id";
    public static final String RECIPES_COLUMN_RECIPENAME = "recipename";
    public static final String RECIPES_COLUMN_INGREDIENT = "ingredient";
    public static final String RECIPES_COLUMN_INSTRUCTION = "instruction";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE recipes (id INTEGER PRIMARY KEY, recipename TEXT, ingredient TEXT, instruction TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recipes");
        onCreate(db);
    }

    public boolean insertRecipe (String recipename, String ingredient, String instruction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("recipename", recipename);
        contentValues.put("ingredient", ingredient);
        contentValues.put("instruction", instruction);

        db.insert("recipes", null, contentValues);

        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery("select * from recipes where id = "+id+ "", null);
    return cursor;
    }

    public ArrayList<String> getAllRecipes() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from recipes", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(RECIPES_COLUMN_RECIPENAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public int getRecipesCount(){
        String countQuery = "SELECT * FROM recipes ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public boolean updateRecipe(Integer id, String recipename, String ingredient, String instruction) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("recipename", recipename);
        contentValues.put("ingredient", ingredient);
        contentValues.put("instruction", instruction);
        db.update("recipes", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteRecipe(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete("recipes",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

}
