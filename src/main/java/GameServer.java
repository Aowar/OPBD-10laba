import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameServer
{
    public static GameServer instance;
    private GameConfig config;
    private World serverWorld;
    private int serverTicks;

    public GameServer(GameConfig config, World serverWorld, int serverTicks) {

        this.serverWorld = serverWorld;
        this.serverTicks = serverTicks;
        instance = this;
    }


    public GameServer() {
        instance = this;
        this.config = null;
        this.serverWorld=null;
    }

    public static void main(String[] args)
    {
        new GameServer();
        try {
            instance.loadWorld();
            instance.loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getInstance().serverTicks = 10;
        int j = GameServer.getInstance().getServerTicks();
        for(int i = 0; i < j; i++)
        {
            System.out.println(i);
            getInstance().updateServer();
            try {
                Thread.sleep(GameServer.getInstance().getConfig().getUpdatePeriod());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(GameServer.getInstance().getServerWorld());
        }
        DatabaseUtils.selectBattle();
//        DatabaseUtils.deleteBattle();
    }

    private void updateServer()
    {
        getInstance().serverWorld.update();
        instance.serverTicks++;
        if(instance.serverTicks == instance.config.getSavePeriod()){
            instance.serverTicks = 0;
            File file = new File("D:/Учёба/РПМ Java/4 Лаба/Worlds/world.txt");
            if(!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileUtils.saveWorld(file, instance.serverWorld);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(instance.getConfig().getUpdatePeriod());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() throws IOException
    {
        try {
            config = FileUtils.loadConfig(new File("D:/Учёба/РПМ Java/4 Лаба/Config/config.txt"));
            if (config == null) {
                config = new GameConfig();
                File file = new File("D:/Учёба/РПМ Java/4 Лаба/Config/config.txt");
                file.createNewFile();
                System.out.println("Файл не обнаружен, генерирую конфиг и сохраняю");
                FileUtils.saveConfig(file, config);
            }
        } catch (IOException e) {
            System.out.println("Ошибка с доступом к данным, генерирую новый файл");
            config = new GameConfig();
        } catch (NumberFormatException | NullPointerException e){
            System.out.println("Информация внутри файла была испорчена, генерирую новый файл");
            config = new GameConfig();
        }
    }

    private void loadWorld() throws IOException
    {
        try {
            serverWorld = FileUtils.loadWorld(new File("D:/Учёба/РПМ Java/4 Лаба/Worlds/world.txt"));
            if (serverWorld == null) {
                serverWorld = new World(1, "world1");
                File file = new File("D:/Учёба/РПМ Java/4 Лаба/Worlds/world.txt");
                file.createNewFile();
                System.out.println("Файл не обнаружен, генерирую мир и сохраняю");
                FileUtils.saveWorld(file, serverWorld);
            }
        } catch (IOException e){
            System.out.println("Ошибка с доступом к данным, генерирую новый файл");
            serverWorld = new World(1, "world1");
        } catch (NumberFormatException | NullPointerException e){
            System.out.println("Информация внутри файла была испорчена, генерирую новый файл мира");
            serverWorld = new World(1, "world1");
        }
    }
    @Override
    public String toString() {
        return "GameServer{" +
                "config=" + config +
                ", serverWorld=" + serverWorld +
                ", serverTicks=" + serverTicks +
                '}';
    }

    public static GameServer getInstance() {
        return instance;
    }

    public static void setInstance(GameServer instance) {
        GameServer.instance = instance;
    }

    public GameConfig getConfig() {
        return config;
    }

    public void setConfig(GameConfig config) {
        this.config = config;
    }

    public World getServerWorld() {
        return serverWorld;
    }

    public void setServerWorld(World serverWorld) {
        this.serverWorld = serverWorld;
    }

    public int getServerTicks() {
        return serverTicks;
    }

    public void setServerTicks(int serverTicks) {
        this.serverTicks = serverTicks;
    }
}
