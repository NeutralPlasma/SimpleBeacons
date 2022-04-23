package eu.virtusdevelops.simplebeacons.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.data.BeaconLocation;
import eu.virtusdevelops.simplebeacons.managers.Module;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLStorage {
    private HikariDataSource dataSource;
    private SimpleBeacons plugin;
    private static String TABLE_NAME = "simplebeacons_data";
    private static String TABLE_NAME_MODULES = "simplebeacons_beacon_modules";
    private static String TABLE_NAME_LOCATIONS = "simplebeacons_beacon_locations";

    public SQLStorage(SimpleBeacons plugin){
        this.plugin = plugin;
        String path = plugin.getDataFolder().getPath();

        HikariConfig config = new HikariConfig();
        config.setPoolName("SimpleBeacons");
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:" + path + "/database.sqlite");
        config.setMaxLifetime(60000);
        config.setMaximumPoolSize(10);


        this.dataSource = new HikariDataSource(config);
    }


    public void closeConnections(){
        if(!dataSource.isClosed()){
            dataSource.close();
        }
    }


    public void createTable(){
        String SQL_BEACONS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "location_x INTEGER NOT NULL," +
                "location_y INTEGER NOT NULL," +
                "location_z INTEGER NOT NULL," +
                "world VARCHAR(30) NOT NULL," +
                "level SMALLINT NOT NULL," +
                "effect VARCHAR(36)," +
                "placed_by VARCHAR(36) NOT NULL);";

        String SQL_BEACON_MODULES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_MODULES + "( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "beacon_id INTEGER," +
                "module VARCHAR(30)," +
                "FOREIGN KEY(beacon_id) REFERENCES " + TABLE_NAME + "(id))";

        String SQL_BEACON_LOCATIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_LOCATIONS + "( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "beacon_id INTEGER," +
                "location_x INTEGER NOT NULL," +
                "location_y INTEGER NOT NULL," +
                "location_z INTEGER NOT NULL," +
                "world VARCHAR(36)," +
                "FOREIGN KEY(beacon_id) REFERENCES " + TABLE_NAME + "(id))";

        try(Connection connection = dataSource.getConnection();){
            PreparedStatement st1 = connection.prepareStatement(SQL_BEACONS_TABLE);
            st1.execute();
        }catch (SQLException error){
            error.printStackTrace();
        }

        try(Connection connection = dataSource.getConnection();){
            PreparedStatement st2 = connection.prepareStatement(SQL_BEACON_MODULES);
            st2.execute();
        }catch (SQLException error){
            error.printStackTrace();
        }

        try(Connection connection = dataSource.getConnection();){
            PreparedStatement st3 = connection.prepareStatement(SQL_BEACON_LOCATIONS);
            st3.execute();
        }catch (SQLException error){
            error.printStackTrace();
        }
    }


    public List<Module> getBeaconModules(int id){
        List<Module> modules = new ArrayList<>();
        String SQL = "SELECT * FROM " + TABLE_NAME_MODULES +
                " WHERE beacon_id = ?";

        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, id);
            ResultSet data = statement.executeQuery();

            while(data.next()){
                modules.add(Module.valueOf(data.getString("module")));
            }
        }catch (SQLException error){
            error.printStackTrace();
        }


        return modules;
    }

    public List<BeaconLocation> getLinkedLocations(int id){
        List<BeaconLocation> locations = new ArrayList<>();
        String SQL = "SELECT * FROM " + TABLE_NAME_LOCATIONS +
                " WHERE beacon_id = ?";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, id);
            ResultSet data = statement.executeQuery();
            while(data.next()){
                locations.add(new BeaconLocation(
                        data.getInt("location_x"),
                        data.getInt("location_y"),
                        data.getInt("location_z"),
                        data.getString("world")
                ));
            }
        }catch (SQLException error){
            error.printStackTrace();
        }

        return locations;
    }

    public BeaconData getBeacon(int id){
        String SQL = "SELECT * FROM " + TABLE_NAME +
                " WHERE id=?";

        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, id);
            ResultSet data = statement.executeQuery();


            BeaconData beaconData = new BeaconData(
                    data.getInt("level"),
                    data.getString("effect"),
                    UUID.fromString(data.getString("placed_by")),
                    new BeaconLocation(data.getInt("location_x"),
                            data.getInt("location_y"),
                            data.getInt("location_z"),
                            data.getString("world")),
                    getBeaconModules(id),
                    getLinkedLocations(id),
                    id
            );
            return beaconData;
        }catch (SQLException error){
            error.printStackTrace();
        }
        return null;
    }

    public BeaconData getBeacon(BeaconData bdata){
        String SQL = "SELECT * FROM " + TABLE_NAME +
                " WHERE location_x = ? AND location_y = ? AND location_z = ? AND world = ? AND placed_by = ?";

        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);
            BeaconLocation loc = bdata.getBeaconLocation();
            statement.setInt(1, loc.x);
            statement.setInt(2, loc.y);
            statement.setInt(3, loc.z);
            statement.setString(4, loc.world);
            statement.setString(5, bdata.getPlacedBy().toString());

            ResultSet data = statement.executeQuery();
            bdata.setId(data.getInt("id"));
            return bdata;
        }catch (SQLException error){
            error.printStackTrace();
        }
        return null;
    }

    public BeaconData addBeacon(BeaconData data){
        String SQL = "INSERT INTO " + TABLE_NAME + "(location_x, location_y, location_z, world, level, effect, placed_by) VALUES ( ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);
            BeaconLocation loc = data.getBeaconLocation();
            statement.setInt(1, loc.x);
            statement.setInt(2, loc.y);
            statement.setInt(3, loc.z);
            statement.setString(4, loc.world);
            statement.setInt(5, data.getLevel());
            statement.setString(6, data.getSelectedEffect());
            statement.setString(7, data.getPlacedBy().toString());

            statement.execute();

            return getBeacon(data);

        }catch (SQLException error){
            error.printStackTrace();
        }
        return null;
    }

    public void addLinkedLocation(BeaconData data ,BeaconLocation location){
        String SQL = "INSERT INTO " + TABLE_NAME_LOCATIONS + "(location_x, location_y, location_z, world, beacon_id) VALUES(?, ?, ?, ?, ?)";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, location.x);
            statement.setInt(2, location.y);
            statement.setInt(3, location.z);
            statement.setString(4, location.world);
            statement.setInt(5, data.getId()); // data.getid?
            statement.execute();

        }catch (SQLException error){
            error.printStackTrace();
        }
    }

    public void removeLinkedLocation(BeaconLocation location, BeaconData data){
        String SQL = "DELETE FROM " + TABLE_NAME_LOCATIONS + " WHERE beacon_id = ? AND location_x = ? AND location_y = ? AND location_z = ? AND world = ?";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(2, location.x);
            statement.setInt(3, location.y);
            statement.setInt(4, location.z);
            statement.setString(5, location.world);
            statement.setInt(1, data.getId()); // data.getid?
            statement.execute();

        }catch (SQLException error){
            error.printStackTrace();
        }
    }

    public void addBeaconModule(Module module, BeaconData data){
        String SQL = "INSERT INTO " + TABLE_NAME_MODULES + "(module, beacon_id) VALUES(?, ?)";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, module.toString());
            statement.setInt(2, data.getId()); // data.getid?
            statement.execute();
        }catch (SQLException error){
            error.printStackTrace();
        }
    }

    public void removeBeaconModule(Module module, BeaconData data){
        String SQL = "DELETE FROM " +TABLE_NAME_MODULES + " WHERE module = ? AND beacon_id = ?";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, module.toString());
            statement.setInt(2, data.getId()); // data.getid?
            statement.execute();
        }catch (SQLException error){
            error.printStackTrace();
        }
    }


    public List<BeaconData> getAllBeacons(){
        List<BeaconData> beacons = new ArrayList<>();
        String SQL = "SELECT * FROM " + TABLE_NAME;

        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);
            ResultSet data = statement.executeQuery();

            while(data.next()){
                int id = data.getInt("id");

                BeaconData beaconData = new BeaconData(
                        data.getInt("level"),
                        data.getString("effect"),
                        UUID.fromString(data.getString("placed_by")),
                        new BeaconLocation(data.getInt("location_x"),
                                data.getInt("location_y"),
                                data.getInt("location_z"),
                                data.getString("world")),
                        getBeaconModules(id),
                        getLinkedLocations(id),
                        id
                );
                beacons.add(beaconData);
            }

        }catch (SQLException error){
            error.printStackTrace();
        }
        return beacons;
    }

    public void updateBeacon(BeaconData data){
        String SQL = "UPDATE " + TABLE_NAME + " SET level = ?, effect = ? WHERE id = ?";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, data.getLevel());
            statement.setString(2, data.getSelectedEffect());
            statement.setInt(3, data.getId());
            statement.execute();
        }catch (SQLException error){
            error.printStackTrace();
        }
    }

    public void removeBeacon(BeaconData data){
        String SQL = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, data.getId());
            statement.execute();
        }catch (SQLException error){
            error.printStackTrace();
        }
    }

    public void removeAllLocations(BeaconData data){
        String SQL = "DELETE FROM " + TABLE_NAME_LOCATIONS + " WHERE beacon_id = ?";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, data.getId());
            statement.execute();
        }catch (SQLException error){
            error.printStackTrace();
        }
    }
}
