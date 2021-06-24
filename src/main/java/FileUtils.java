import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils
{
    public static void saveConfig(File path, GameConfig config) throws IOException
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(config.getIp() + "\n");
        bw.write(config.getPort() + "\n");
        bw.write(config.getDifficulty() + "\n");
        bw.write(config.getUpdatePeriod() + "\n");
        bw.write(config.getSavePeriod() + "\n");
        bw.close();
    }

    public static GameConfig loadConfig(File path) throws IOException
    {
        if(!path.exists()) return null;
        BufferedReader br = new BufferedReader(new FileReader(path));
        GameConfig gameConfig = new GameConfig();
        gameConfig.setIp(br.readLine());
        gameConfig.setPort(Integer.parseInt(br.readLine()));
        gameConfig.setDifficulty(Integer.parseInt(br.readLine()));
        gameConfig.setUpdatePeriod(Integer.parseInt(br.readLine()));
        gameConfig.setSavePeriod(Integer.parseInt(br.readLine()));
        System.out.println("Загрузка успешна");
        br.close();
        return gameConfig;
    }

    public static void saveWorld(File path, World world) throws IOException
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(world.getId() + "\n" + world.getWorldName() + "\n"+ world.getEntities().size() + "\n");
        for(Entity e : world.getEntities()) {
            bw.write(e.title + "\n"+ e.posX + "\n" + e.posZ + "\n");
            bw.write(e.agressive + "\n" + e.maxHealth + "\n" + e.health + "\n");
            bw.write(e.attackDamage + "\n");
            if(e instanceof EntityPlayer) bw.write(((EntityPlayer) e).getName() + "\n");
            else bw.write("f\n");
        }
        bw.close();
    }

    public static World loadWorld(File path) throws IOException
    {
        if(!path.exists()) return null;
        BufferedReader br = new BufferedReader(new FileReader(path));
        List<Entity> list = new ArrayList<>();
        World world = new World(1, "world1", list);
        world.setId(Integer.parseInt(br.readLine()));
        world.setWorldName(br.readLine());
        double a1=0,a2=0;
        int i = Integer.parseInt(br.readLine()),a3=0,a4=0,a5=0,id;
        boolean agr;
        String title;
        String nik;
        while(i>0) {
            id=Integer.parseInt(br.readLine());
            title=br.readLine();
            a1=Double.parseDouble(br.readLine());
            a2=Double.parseDouble(br.readLine());
            agr = Boolean.parseBoolean(br.readLine());
            a3=Integer.parseInt(br.readLine());
            a4=Integer.parseInt(br.readLine());
            a5=Integer.parseInt(br.readLine());
            nik=br.readLine();
            if (nik.equals("f")) world.getEntities().add(new Entity(id,world,title,a1,a2,agr,a3,a4,a5));
            else new EntityPlayer(world,title,a1,a2,a3,a4,a5,nik);
            i--;
        }
        System.out.println("Загрузка успешна");
        br.close();
        return world;
    }
}
