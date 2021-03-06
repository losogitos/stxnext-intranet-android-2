package com.stxnext.intranet2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.stxnext.intranet2.backend.model.impl.User;
import com.stxnext.intranet2.backend.model.team.Client;
import com.stxnext.intranet2.backend.model.team.Project;
import com.stxnext.intranet2.backend.model.team.Team;
import com.stxnext.intranet2.backend.model.team.TeamProject;

import java.sql.SQLException;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static String TAG = DatabaseHelper.class.getName();

    private static final String DATABASE_NAME = "stxIntranet.db";
    private static final int DATABASE_VERSION = 4;

    private Dao<User, Integer> simpleDao = null;
    private Dao<Client, Long> clientDao = null;
    private Dao<Project, Long> projectDao = null;
    private Dao<Team, Long> teamDao = null;
    private Dao<TeamProject, Long> teamProjectDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.d(TAG, "onCreate ==== Database created from scratch ====");
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Client.class);
            TableUtils.createTable(connectionSource, Project.class);
            TableUtils.createTable(connectionSource, Team.class);
            TableUtils.createTable(connectionSource, TeamProject.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)  {
            try {
                if (oldVersion == 1 && newVersion == 2) {
                    getUserDao().executeRaw("ALTER TABLE 'user' ADD COLUMN isClient BOOLEAN DEFAULT 0;");
                    Log.i(TAG, "==== Database upgraded from v1 to v2 ====");
                }
                db.execSQL("DROP TABLE IF EXISTS " + "user");
                TableUtils.dropTable(connectionSource, Client.class, true);
                TableUtils.dropTable(connectionSource, Project.class, true);
                TableUtils.dropTable(connectionSource, Team.class, true);
                TableUtils.dropTable(connectionSource, TeamProject.class, true);
                onCreate(db, connectionSource);
            } catch (SQLException exc) {
                Log.e(TAG, "Exception on database upgrade: " + exc.toString());
            }
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<User, Integer> getUserDao() throws SQLException {
        if (simpleDao == null) {
            simpleDao = getDao(User.class);
        }
        return simpleDao;
    }

    public Dao<Client, Long> getClientDao() throws  SQLException {
        if (clientDao == null)
            clientDao = getDao(Client.class);
        return clientDao;
    }

    public Dao<Project, Long> getProjectDao() throws  SQLException {
        if (projectDao == null)
            projectDao = getDao(Project.class);
        return projectDao;
    }

    public Dao<Team, Long> getTeamDao() throws  SQLException {
        if (teamDao == null)
            teamDao = getDao(Team.class);
        return teamDao;
    }

    public Dao<TeamProject, Long> getTeamProjectDao() throws SQLException {
        if (teamProjectDao == null)
            teamProjectDao = getDao(TeamProject.class);
        return teamProjectDao;
    }

    public void clearTable(Class tableClass) {
        try {
            TableUtils.clearTable(getConnectionSource(), tableClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        simpleDao = null;
        clientDao = null;
        projectDao = null;
        teamDao = null;
    }
}