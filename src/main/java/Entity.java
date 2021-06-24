import java.util.List;

public class Entity
{
    protected final long id;
    protected World world;
    protected String title;
    protected double posX;
    protected double posZ;
    protected double distance;
    protected boolean agressive;
    protected int maxHealth;
    protected int health;
    protected int attackDamage;
    protected Entity target;

    public Entity(int id, World world, String title, double posX, double posZ, boolean agressive, int maxHealth, int health, int attackDamage) {
        this.id = id;
        this.world = world;
        this.title = title;
        this.posX = posX;
        this.posZ = posZ;
        this.distance = 0.0;
        this.agressive = agressive;
        this.maxHealth = maxHealth;
        this.health = health;
        this.attackDamage = attackDamage;
        world.getEntities().add(this);
        DatabaseUtils.insertNewEntity(this.title);
    }

    public Entity(World world, String title, double posX, double posZ, boolean agressive, int maxHealth, int health, int attackDamage) {
        this.world = world;
        this.title = title;
        this.posX = posX;
        this.posZ = posZ;
        this.distance = 0.0;
        this.agressive = agressive;
        this.maxHealth = maxHealth;
        this.health = health;
        this.attackDamage = attackDamage;
        id = DatabaseUtils.insertNewEntity(title);
        world.getEntities().add(this);
    }

    public void update()
    {
        double moduleX, moduleZ;
        if(this.isAgressive())
        {
            if(this.getTarget() == null)
            {
                this.searchTarget();
            }
            if (this.getTarget() != null)
            {
                if (target.getHealth() > 0)
                {
                    if (this.getHealth() > 0)
                    {
                        if (this.posX < target.getPosX()) {
                            this.posX++;
                        }
                        if (this.posX > target.getPosX()) {
                            this.posX--;
                        }
                        if (this.posZ < target.getPosZ()) {
                            this.posZ++;
                        }
                        if (this.posZ > target.getPosZ()) {
                            this.posZ--;
                        }
                        moduleX = this.posX - target.getPosX();
                        moduleZ = this.posZ - target.getPosZ();
                        if ((Math.abs(moduleX) < 2) && (Math.abs(moduleZ) < 2)) {
                            this.attack(target);
                        }
                    }
                }
            }
        }
    }

    public void searchTarget()
    {
        List<Entity> entityList = world.getEntitiesNearEntity(this, 20);
        for (int i = 0; i != entityList.size(); i++)
        {
            if (!entityList.get(i).isAgressive())
            {
                this.setTarget(entityList.get(i));
                break;
            }
        }
    }

    public void attack(Entity entity)
    {
        if (entity.health >= 0 && health >= 0) {
            entity.health -= attackDamage + 0.5 * GameServer.getInstance().getConfig().getDifficulty();
            if (entity.health <= 0) {
                System.out.println("\n" + entity.title + " убит " + title + "\n");
                DatabaseUtils.updateEntity(entity);
                DatabaseUtils.insertBattle_logs(id, entity.id);
                this.target = null;
                return;
            }
            if (entity instanceof EntityPlayer) {
                health -= entity.attackDamage + 0.5 * GameServer.getInstance().getConfig().getDifficulty();
            }
            if (health <= 0) {
                System.out.println("\n" + title + " убит " + entity.title + "\n");
                DatabaseUtils.updateEntity(this);
                DatabaseUtils.insertBattle_logs(entity.id, id);
            }
        }
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posX=" + posX +
                ", posZ=" + posZ +
                ", agressive=" + agressive +
                ", maxHealth=" + maxHealth +
                ", health=" + health +
                ", attackDamage=" + attackDamage +
                ", target=" + target +
                '}';
    }

    public long getId() {
        return id;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isAgressive() {
        return agressive;
    }

    public void setAgressive(boolean agressive) {
        this.agressive = agressive;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }
}
