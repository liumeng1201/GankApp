package com.lm.android.library.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {
    public static void main(String[] args) throws Exception {
        // 正如你所见的，你创建了一个用于添加实体（Entity）的模式（Schema）对象。
        // 两个参数分别代表：数据库版本号与自动生成代码的包路径。
        Schema schema = new Schema(1, "com.lm.android.gankapp.dao");
        // 当然，如果你愿意，你也可以分别指定生成的 Bean 与 DAO 类所在的目录，只要如下所示：
        // Schema schema = new Schema(1, "me.itangqi.bean");
        // schema.setDefaultJavaPackageDao("me.itangqi.dao");

        // 模式（Schema）同时也拥有两个默认的 flags，分别用来标示 entity 是否是 activie 以及是否使用 keep sections。
        // schema2.enableActiveEntitiesByDefault();
        // schema2.enableKeepSectionsByDefault();

        // 一旦你拥有了一个 Schema 对象后，你便可以使用它添加实体（Entities）了。
        // addNote(schema);
        addReadTable(schema);
        addFavoriteTable(schema);
        addPropertyTable(schema);

        // 最后我们将使用 DAOGenerator 类的 generateAll() 方法自动生成代码，此处你需要根据自己的情况更改输出目录。
        new DaoGenerator().generateAll(schema, args[0]);
    }

    private static void addNote(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity note = schema.addEntity("Note");
        // 你也可以重新给表命名
        // note.setTableName("NOTE");

        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        // 与在 Java 中使用驼峰命名法不同，默认数据库中的命名是使用大写和下划线来分割单词的。
        // For example, a property called “creationDate” will become a database column “CREATION_DATE”.
        note.addStringProperty("comment");
        note.addDateProperty("date");
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
        favoriteContent.addStringProperty("type").notNull();
        favoriteContent.addStringProperty("desc").notNull();
        favoriteContent.addStringProperty("url").notNull();
        favoriteContent.addStringProperty("who").notNull();
        favoriteContent.addStringProperty("publishedAt").notNull();
        favoriteContent.addDateProperty("favoriteAt").notNull();
    }

    // 用来代替SharedPreference的属性表
    private static void addPropertyTable(Schema schema) {
        Entity propertyContent = schema.addEntity("PropertyContent");
        propertyContent.addIdProperty();
        propertyContent.addStringProperty("key").notNull();
        propertyContent.addStringProperty("value");
    }
}