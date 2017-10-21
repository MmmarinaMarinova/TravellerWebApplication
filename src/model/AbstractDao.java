package model;

import java.sql.Connection;

/**
 * Created by Marina on 19.10.2017 ?..
 */
public abstract class AbstractDao {
    private Connection con = DBManager.getInstance().getConnection();

    public Connection getCon() {
        return this.con;
    }
}
