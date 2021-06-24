import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.*;
import java.util.Date;

public class DatabaseUtils
{
    public static HikariDataSource pull = DatabaseUtils.getHikariDataSource();

    public static HikariDataSource getHikariDataSource()
    {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName( "com.mysql.cj.jdbc.Driver" );
        config.setJdbcUrl("jdbc:mysql://localhost:3306/lab4");
        config.setUsername("root");
        config.setPassword("952259");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("maximumPoolSize", "25");
        return new HikariDataSource(config);
    }

    public static void createEntity(){
        String sql = "CREATE TABLE IF NOT EXISTS entities(" +
                "id_entities int NOT NULL AUTO_INCREMENT," +
                "title VARCHAR(45) NOT NULL," +
                "creationTime DATE NOT NULL," +
                "deathTime DATE," +
                "PRIMARY KEY(id_entities)" +
                ");";
        try (Connection connection = pull.getConnection()){
            Statement s = connection.createStatement();
            s.executeUpdate(sql);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static int insertNewEntity(String title){
        createEntity();
        String sql = "insert into entities(title, creationTime) values(?, ?)";
        Date date = new Date();
        try (Connection connection = pull.getConnection()){
            PreparedStatement st=connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            st.setString(1, title);
            st.setTimestamp(2, new Timestamp(date.getTime()));
            st.executeUpdate();
            ResultSet resultSet=st.getGeneratedKeys();
            if (resultSet.next()){
                return resultSet.getInt(1);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return 0;
    }

    public static void updateEntity(Entity e){
        createEntity();
        String sql = "update entities SET deathTime = ? where id_entities = ?;";
        Date date = new Date();
        try (Connection connection = pull.getConnection()){
            PreparedStatement st = connection.prepareStatement(sql);
            st.setTimestamp(1, new Timestamp(date.getTime()));
            st.setLong(2, e.getId());
            st.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        System.out.println("Запись");
    }

    public static void dropEntity(){
        String sql = "drop table IF EXISTS entities;";
        try (Connection connection = pull.getConnection()){
            Statement s = connection.createStatement();
            s.executeUpdate(sql);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void createPlayers(){
        String sql = "CREATE TABLE IF NOT EXISTS players(" +
                "id_players int NOT NULL AUTO_INCREMENT," +
                "id_entities int NOT NULL," +
                "nickname VARCHAR(128) NOT NULL," +
                "exp int NOT NULL," +
                "PRIMARY KEY(id_players)," +
                "FOREIGN KEY (id_entities) REFERENCES entities (id_entities)" +
                ");";
        try (Connection connection = pull.getConnection()){
            Statement s = connection.createStatement();
            s.executeUpdate(sql);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void insertPlayers(EntityPlayer e){
        createPlayers();
        String sql = "insert into players(id_entities, nickname, exp) values(?, ?, 0)";
        Date date = new Date();
        try (Connection connection = pull.getConnection()){
            PreparedStatement st=connection.prepareStatement(sql);
            st.setInt(1, (int) e.id);
            st.setString(2, e.getName());
            st.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void updateExp(EntityPlayer e){
        createPlayers();
        String sql = "update players SET exp = ? where id_entities = ?;";
        Date date = new Date();
        try (Connection connection = pull.getConnection()){
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, e.getExp());
            st.setLong(2, e.getId());
            st.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static int selectExp(EntityPlayer e)
    {
        try(Connection connection = pull.getConnection())
        {
            String sql = "SELECT exp FROM players where id_entities=?;";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, (int) e.id);
            ResultSet resultSet = st.executeQuery();
            if(resultSet.next())
            {
                return resultSet.getInt("exp");
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return 0;
    }

    public static void dropPlayers(){
        String sql = "drop table IF EXISTS players;";
        try (Connection connection = pull.getConnection()){
            Statement s = connection.createStatement();
            s.executeUpdate(sql);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void createBattle_logs(){
        String sql = "CREATE TABLE IF NOT EXISTS battlelogs(" +
                "idLog int NOT NULL AUTO_INCREMENT," +
                "attackId int NOT NULL," +
                "victimId int NOT NULL," +
                "deathTime DATETIME NOT NULL," +
                "PRIMARY KEY(idLog)," +
                "FOREIGN KEY (attackId)  REFERENCES entities (id_entities), " +
                "FOREIGN KEY (victimId)  REFERENCES entities (id_entities)" +
                ");";
        try (Connection connection = pull.getConnection()){
            Statement s = connection.createStatement();
            s.executeUpdate(sql);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void insertBattle_logs(long id_attack, long id_killed){
        createBattle_logs();
        String sql = "insert into battlelogs(attackId, victimId, deathTime) values(?, ?, ?)";
        Date date = new Date();
        try (Connection connection = pull.getConnection()){
            PreparedStatement st=connection.prepareStatement(sql);
            st.setInt(1, (int) id_attack);
            st.setInt(2, (int) id_killed);
            st.setTimestamp(3, new Timestamp(date.getTime()));
            st.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void selectBattle(){
        String sql = "select * from battlelogs;";
        try (Connection connection = pull.getConnection()){
            Statement s = connection.createStatement();
            ResultSet result = s.executeQuery(sql);
            while(result.next()) {
                int attacker_id = result.getInt("attackId");
                int victim_id = result.getInt("victimId");
                Date deathTime  = result.getDate("deathTime");
                System.out.println("attacker id: " + attacker_id + "; victim id: " + victim_id + "; death time: " + deathTime + ";");
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void deleteBattle(){
        String sql = "delete from battlelogs;";
        try (Connection connection = pull.getConnection()){
            Statement s = connection.createStatement();
            s.executeUpdate(sql);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void dropBattle_logs(){
        String sql = "drop table IF EXISTS battlelogs;";
        try (Connection connection = pull.getConnection()){
            Statement s = connection.createStatement();
            s.executeUpdate(sql);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
