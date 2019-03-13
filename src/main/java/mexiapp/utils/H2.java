package mexiapp.utils;

import mexiapp.data.Account;

import java.sql.*;

public class H2 {
    private static H2 instance;

    private Connection conn;

    private H2() {
        connect();
    }

    public static H2 getInstance() {
        if (instance == null)
            instance = new H2();
        return instance;
    }

    private void connect() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:h2:./vcredistx64;IFEXISTS=TRUE",
                    "mexiapp",
                    "&8McqKMnkn>v/~`4`NL#$U*fJ>~D=rkL{Hf<6e8bRw7vZH5AUk.}x6V/DvEm-8X5"
            );
        } catch (SQLException e) {
            Log.exception(e);
        }
    }

    public boolean isActive() {
        try {
            ResultSet r = conn.createStatement().executeQuery("SELECT ACTIVE FROM ACTIVE LIMIT 1");
            r.first();
            return r.getBoolean("ACTIVE");
        } catch (SQLException e) {
            Log.exception(e);
            return false;
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            Log.exception(e);
        }
    }

    public boolean updateAccount(String user, String pass) {
        PreparedStatement prep;
        try {
            prep = conn.prepareStatement("UPDATE EMAIL SET USERNAME = ?, PASSWORD = ? WHERE ID = 1");
            prep.setString(1, user);
            prep.setString(2, pass);
            prep.execute();
            return true;
        } catch (SQLException e) {
            Log.exception(e);
            return false;
        }

    }

    public Account getAccount() {
        try {
            ResultSet r = conn.prepareStatement("SELECT * FROM EMAIL WHERE ID = 1").executeQuery();
            r.first();
            return new Account(r.getString("USERNAME"), r.getString("PASSWORD"));
        } catch (SQLException e) {
            return null;
        }
    }

    public void setActive(boolean active) {
        try {
            if (conn.isClosed()) {
                connect();
            }
            PreparedStatement prep = conn.prepareStatement("UPDATE ACTIVE SET ACTIVE = ?");
            prep.setBoolean(1, active);
            prep.execute();
        } catch (SQLException e) {
            Log.exception(e);
        }
    }
}
