package com.daemin.lib;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "timedao_group");
        Entity groupList = schema.addEntity("GroupListFromServer");
        groupList.addIdProperty();
        groupList.addStringProperty("name");
        new DaoGenerator().generateAll(schema, args[0]);
    }
}