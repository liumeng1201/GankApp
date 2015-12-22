package com.lm.android.gankapp.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.lm.android.gankapp.dao.FavoriteContent;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "FAVORITE_CONTENT".
*/
public class FavoriteContentDao extends AbstractDao<FavoriteContent, Long> {

    public static final String TABLENAME = "FAVORITE_CONTENT";

    /**
     * Properties of entity FavoriteContent.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ObjectId = new Property(1, String.class, "objectId", false, "OBJECT_ID");
        public final static Property Type = new Property(2, String.class, "type", false, "TYPE");
        public final static Property Desc = new Property(3, String.class, "desc", false, "DESC");
        public final static Property Url = new Property(4, String.class, "url", false, "URL");
        public final static Property Who = new Property(5, String.class, "who", false, "WHO");
        public final static Property PublishedAt = new Property(6, String.class, "publishedAt", false, "PUBLISHED_AT");
        public final static Property FavoriteAt = new Property(7, java.util.Date.class, "favoriteAt", false, "FAVORITE_AT");
    };


    public FavoriteContentDao(DaoConfig config) {
        super(config);
    }
    
    public FavoriteContentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"FAVORITE_CONTENT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"OBJECT_ID\" TEXT NOT NULL ," + // 1: objectId
                "\"TYPE\" TEXT NOT NULL ," + // 2: type
                "\"DESC\" TEXT NOT NULL ," + // 3: desc
                "\"URL\" TEXT NOT NULL ," + // 4: url
                "\"WHO\" TEXT NOT NULL ," + // 5: who
                "\"PUBLISHED_AT\" TEXT NOT NULL ," + // 6: publishedAt
                "\"FAVORITE_AT\" INTEGER NOT NULL );"); // 7: favoriteAt
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FAVORITE_CONTENT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, FavoriteContent entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getObjectId());
        stmt.bindString(3, entity.getType());
        stmt.bindString(4, entity.getDesc());
        stmt.bindString(5, entity.getUrl());
        stmt.bindString(6, entity.getWho());
        stmt.bindString(7, entity.getPublishedAt());
        stmt.bindLong(8, entity.getFavoriteAt().getTime());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public FavoriteContent readEntity(Cursor cursor, int offset) {
        FavoriteContent entity = new FavoriteContent( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // objectId
            cursor.getString(offset + 2), // type
            cursor.getString(offset + 3), // desc
            cursor.getString(offset + 4), // url
            cursor.getString(offset + 5), // who
            cursor.getString(offset + 6), // publishedAt
            new java.util.Date(cursor.getLong(offset + 7)) // favoriteAt
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, FavoriteContent entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setObjectId(cursor.getString(offset + 1));
        entity.setType(cursor.getString(offset + 2));
        entity.setDesc(cursor.getString(offset + 3));
        entity.setUrl(cursor.getString(offset + 4));
        entity.setWho(cursor.getString(offset + 5));
        entity.setPublishedAt(cursor.getString(offset + 6));
        entity.setFavoriteAt(new java.util.Date(cursor.getLong(offset + 7)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(FavoriteContent entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(FavoriteContent entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}