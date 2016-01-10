package com.lm.android.library.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {
    public static void main(String[] args) throws Exception {
        // 数据库版本号与自动生成代码的路径
        Schema schema = new Schema(1, "com.lm.android.gankapp.dao");

        addReadTable(schema);
        addFavoriteTable(schema);
        addPropertyTable(schema);

        new DaoGenerator().generateAll(schema, args[0]);
    }

    private static void addReadTable(Schema schema) {
        Entity readContent = schema.addEntity("ReadContent");
        readContent.addIdProperty();
        readContent.addStringProperty("objectId").notNull();
    }

    private static void addFavoriteTable(Schema schema) {
        Entity favoriteContent = schema.addEntity("FavoriteContent");
        favoriteContent.addIdProperty();
        favoriteContent.addStringProperty("objectId").notNull();
        favoriteContent.addStringProperty("contentObjectId").notNull();
        favoriteContent.addStringProperty("type").notNull();
        favoriteContent.addStringProperty("desc").notNull();
        favoriteContent.addStringProperty("url").notNull();
        favoriteContent.addLongProperty("favoriteAt").notNull();
    }

    // 用来代替SharedPreference的属性表
    private static void addPropertyTable(Schema schema) {
        Entity propertyContent = schema.addEntity("PropertyContent");
        propertyContent.addIdProperty();
        propertyContent.addStringProperty("key").notNull();
        propertyContent.addStringProperty("value");
    }
}