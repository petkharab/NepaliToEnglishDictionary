package com.makkhay.androiddictionary.database;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class DbBackend extends DbObject{

    public DbBackend(Context context) {
        super(context);
    }

    public List<QuizObject> dictionaryWords(){
        List<QuizObject> mItems = new ArrayList<QuizObject>();
        String query = "Select * from final";
        Cursor cursor = this.getDbConnection().rawQuery(query, null);
        ArrayList<String> wordTerms = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String word = cursor.getString(cursor.getColumnIndexOrThrow("word"));
                mItems.add(new QuizObject(id, word));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return mItems;
    }

    public QuizObject getQuizById(int quizId){

        QuizObject quizObject = null;
        String query = "select * from final where _id = " + quizId;
        Cursor cursor = this.getDbConnection().rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String word = cursor.getString(cursor.getColumnIndexOrThrow("word"));
                String meaning = cursor.getString(cursor.getColumnIndexOrThrow("meaning"));
                quizObject = new QuizObject(id, word, meaning);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return quizObject;
    }
}
