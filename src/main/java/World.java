import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class World
{
    private int id;
    private String worldName;
    private List<Entity> entities;

    public World(int id, String worldName, List<Entity> entities) {
        this.id = id;
        this.worldName = worldName;
        this.entities = entities;
    }

    public World(int id, String worldName) {
        this.id = id;
        this.worldName = worldName;
    }

    public void update()
    {
        for (Entity entity : entities) {
            entity.update();
        }
    }

    public List<Entity> getEntitiesInRegion(double x, double z, double range)
    {
        List<Entity> e = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.getPosZ() - z != 0 && entity.getPosX() - x != 0) {
                entity.setDistance(Math.sqrt(Math.pow(Math.abs(entity.posX - x), 2) + Math.pow(Math.abs(entity.posZ - z), 2)));
                if (entity.getDistance() < range) {
                    e.add(entity);
                }
            }
        }
        e.sort(Comparator.comparingDouble(Entity::getDistance));
        return e;
    }

    public List<Entity> getEntitiesNearEntity(Entity entity, double range)
    {
        return getEntitiesInRegion(entity.getPosX(), entity.getPosZ(), range);
    }



    public void deleteEntity ()
    {
        ListIterator<Entity> listIterator = entities.listIterator();
        Entity e;
        while(listIterator.hasNext())
        {
            e = listIterator.next();
            if (e.getHealth() <= 0)
            {
                listIterator.remove();
            }
        }
    }

    @Override
    public String toString() {
        return "World{" +
                "id=" + id +
                ", worldName='" + worldName + '\'' +
                ", entities=" + entities +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }
}